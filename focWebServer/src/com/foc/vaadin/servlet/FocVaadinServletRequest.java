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
package com.foc.vaadin.servlet;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.foc.Globals;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletService;

@SuppressWarnings("serial")
public class FocVaadinServletRequest extends VaadinServletRequest {
//	private FocServletInputStream inputStreamWrapper = null; 
			
	public FocVaadinServletRequest(HttpServletRequest request, VaadinServletService vaadinService) {
		super(request, vaadinService);
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
//		try {
//			inputStreamWrapper = new FocServletInputStream(super.getInputStream());
//		} catch (IOException e) {
//			Globals.logException(e);
//		}
//		return inputStreamWrapper;
		return super.getInputStream();
	}
	
	private class FocServletInputStream extends ServletInputStream {
		private ServletInputStream original = null;
		private StringBuffer       buff     = null;
		
		public FocServletInputStream(ServletInputStream original){
			this.original = original;
			buff = new StringBuffer();
		}
		
		@Override
		public int readLine(byte[] b, int off, int len) throws IOException {
			return original.read(b, off, len);
		}
		
		@Override
		public int read() throws IOException {
			int c = original.read();
			if(c == -1){
			}else{
				buff.append(c);	
			}
			Globals.logString("FocServletInputStream :"+buff.toString());
			return c;
		}

		@Override
		public int read(byte[] b) throws IOException {
			return original.read(b);
		}

		@Override
		public int available() throws IOException {
			return original.available();
		}

		@Override
		public void close() throws IOException {
			original.close();
		}

		@Override
		public synchronized void mark(int readlimit) {
			original.mark(readlimit);
		}

		@Override
		public boolean markSupported() {
			return original.markSupported();
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return original.read(b, off, len);
		}

		@Override
		public synchronized void reset() throws IOException {
			original.reset();
		}

		@Override
		public long skip(long n) throws IOException {
			return original.skip(n);
		}
//
//		public boolean isFinished() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		public boolean isReady() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		public void setReadListener(ReadListener arg0) {
//			// TODO Auto-generated method stub
//			
//		}
	}
}
