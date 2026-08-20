/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.business.notifier.FNotifTrigger;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationManager;
import com.foc.list.FocList;
import com.foc.util.Utils;
import com.foc.web.server.FocWebServer;

public class NotificationScheduledThread extends FocThreadWithSession {

	// Max number of concurrent trigger threads. If this limit is reached, the
	// current scan cycle is skipped to avoid unbounded DB connection accumulation.
	// Default of 200 is sized with headroom over observed live load (~45 triggers
	// legitimately fire every single 60s cycle on production) - this must stay well
	// above normal steady-state concurrency, or the guard will skip cycles under
	// ordinary operation instead of only during a real leak/hang. Overridable via
	// the "notification.maxConcurrentTriggerThreads" config property.
	private static final int  MAX_CONCURRENT_TRIGGER_THREADS =
		Utils.parseInteger(ConfigInfo.getProperty("notification.maxConcurrentTriggerThreads"), 200);

	// A trigger thread running longer than this (ms) is considered stuck and
	// will be interrupted so its finally/dispose block can reclaim DB connections.
	private static final long MAX_TRIGGER_RUNTIME_MS         = 5 * 60 * 1000; // 5 minutes

	// Registry of all live trigger threads so we can count and watchdog them.
	private final List<AtomicNotificationTriggerThread> liveThreads = new ArrayList<>();

	public NotificationScheduledThread(FocWebApplication initialWebApplication, FocWebServer webServer) {
		super(initialWebApplication, webServer);
	}

	@Override
	public void main() {
		FocNotificationEvent event = new FocNotificationEvent(FNotifTrigger.EVT_SCHEDULED, null);

		while (true){
			Globals.logString("NOTIFICATION TRIGGER: Starting a new scan cycle.");

			try{
				Thread.sleep(60000);
				Globals.logString("NOTIFICATION TRIGGER: Woke up after 60 seconds.");

				// --- Watchdog: interrupt threads that have been running too long ---
				interruptStuckThreads();

				// --- Guard: refuse to spawn more threads if too many are alive ---
				int liveCount = countLiveThreads();
				if (liveCount >= MAX_CONCURRENT_TRIGGER_THREADS) {
					Globals.logString("NOTIFICATION TRIGGER: WARNING - " + liveCount +
						" trigger threads still alive (limit=" + MAX_CONCURRENT_TRIGGER_THREADS +
						"). Skipping this scan cycle to protect DB connections.");
					continue;
				}

				FocNotificationManager manager = FocNotificationManager.getInstance();
				if(manager != null){
					Globals.logString("NOTIFICATION TRIGGER: Retrieved FocNotificationManager.");

					FocList eventNotifierList = manager.getEventNotifierList();
					if(eventNotifierList != null){
						Globals.logString("NOTIFICATION TRIGGER: Found " + eventNotifierList.size() + " notification triggers to scan.");

						for(int i = 0; i < eventNotifierList.size(); i++){
							FNotifTrigger trigger = (FNotifTrigger) eventNotifierList.getFocObject(i);

							if(trigger.getEvent() == FNotifTrigger.EVT_SCHEDULED && trigger.isEventMatch(event) && !trigger.isRunning()){
								Globals.logString("NOTIFICATION TRIGGER: Found matching trigger at index " + i + ", starting thread.");
								trigger.setRunning(true);
								AtomicNotificationTriggerThread atomicThread = new AtomicNotificationTriggerThread(getClassNameFocWebApplication(), getWebServer(), trigger, event);
								registerThread(atomicThread);
								atomicThread.start();
								Globals.logString("NOTIFICATION TRIGGER: Thread started for trigger at index " + i + ".");
							}
						}
						Globals.logString("NOTIFICATION TRIGGER: Completed scanning triggers.");
					}else{
						Globals.logString("NOTIFICATION TRIGGER: No notification triggers found.");
					}
				}else{
					Globals.logString("NOTIFICATION TRIGGER: FocNotificationManager not available.");
				}
			}catch(Exception e) {
				Globals.logString("Error while processing notification triggers !!!");
				Globals.logExceptionWithoutPopup(e);
			}
			Globals.logString("NOTIFICATION TRIGGER: Scan cycle completed.");
		}
	}

	private synchronized void registerThread(AtomicNotificationTriggerThread t) {
		liveThreads.add(t);
	}

	private synchronized int countLiveThreads() {
		Iterator<AtomicNotificationTriggerThread> it = liveThreads.iterator();
		while (it.hasNext()) {
			if (!it.next().isAlive()) it.remove();
		}
		return liveThreads.size();
	}

	private synchronized void interruptStuckThreads() {
		Iterator<AtomicNotificationTriggerThread> it = liveThreads.iterator();
		while (it.hasNext()) {
			AtomicNotificationTriggerThread t = it.next();
			if (!t.isAlive()) {
				it.remove();
			} else if (t.getRuntimeMs() > MAX_TRIGGER_RUNTIME_MS && !t.isInterruptRequested()) {
				Globals.logString("NOTIFICATION TRIGGER: WARNING - thread " + t.getName() +
					" has been running for " + (t.getRuntimeMs() / 1000) + "s (limit=" +
					(MAX_TRIGGER_RUNTIME_MS / 1000) + "s). Interrupting it. Note: interrupt() " +
					"has no effect on a thread blocked in plain blocking socket I/O (e.g. SMTP " +
					"send) - if it doesn't actually die, it stays in liveThreads and keeps " +
					"counting against MAX_CONCURRENT_TRIGGER_THREADS, which is the point.");
				t.interrupt();
				t.setInterruptRequested(true);
				// Do NOT remove here - only isAlive()==false above removes an entry.
				// A thread that ignores the interrupt is still holding a live DB
				// connection and must keep counting as live.
			}
		}
	}

	public class AtomicNotificationTriggerThread extends FocThreadWithSession {

		private FNotifTrigger trigger = null;
		private FocNotificationEvent event = null;
		private final long startTime = System.currentTimeMillis();
		private volatile boolean interruptRequested = false;

		public AtomicNotificationTriggerThread(String classNameFocWebApplication, FocWebServer webServer, FNotifTrigger trigger, FocNotificationEvent event) {
			super(classNameFocWebApplication, webServer);
			this.trigger = trigger;
			this.event = event;
			setInitiallSleep(0);
		}

		public long getRuntimeMs() {
			return System.currentTimeMillis() - startTime;
		}

		public boolean isInterruptRequested() {
			return interruptRequested;
		}

		public void setInterruptRequested(boolean interruptRequested) {
			this.interruptRequested = interruptRequested;
		}

		public void dispose() {
			super.dispose();
			if(trigger != null) {
				trigger.setRunning(false);
				trigger = null;
			}
			event = null;
		}

		@Override
		public void main() {
			try {
				trigger.executeAndReschedule(event);
			} catch(Exception e) {
				Globals.logString("NOTIFICATION TRIGGER: Error while executing trigger.");
				Globals.logExceptionWithoutPopup(e);
			} finally {
				dispose();
			}
		}
	}
}
