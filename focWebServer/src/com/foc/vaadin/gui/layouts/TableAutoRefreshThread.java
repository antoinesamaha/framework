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
package com.foc.vaadin.gui.layouts;

import com.foc.Globals;
import com.vaadin.ui.UI;

public class TableAutoRefreshThread implements Runnable {

	private static long LAPSE = 20000;
	
	private FVTableWrapperLayout wrapper = null;
	private boolean              stop  = false;
	private long                 time  = 0;
	
	public TableAutoRefreshThread(FVTableWrapperLayout wrapper){
		this(wrapper, LAPSE);
	}
	
	public TableAutoRefreshThread(FVTableWrapperLayout wrapper, long time){
		this.wrapper = wrapper;
		this.time = time;
		if(this.time < 500){
			this.time = LAPSE;
		}
	}		
	
	public void dispose(){
		stop = true;
		wrapper = null;
	}
	
	@Override
	public void run() {
		while(!stop){
			try{
				Thread.sleep(LAPSE);
				UI.getCurrent().access(new Runnable() {
					@Override
					public void run() {
						FVTableWrapperLayout tempWrapper = wrapper;
						if(tempWrapper != null){
							tempWrapper.reloadClickListener();
						}
					}
				});
			}catch (InterruptedException e){
				Globals.logException(e);
			}
		}
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
}

