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
package com.foc.web.unitTesting;

public class FocUnitTestLevel {
	private FocUnitTest test = null;
	private int commandIndex = -1;
	
	public FocUnitTestLevel(FocUnitTest test, int commandIndex){
		this.test = test;
		this.commandIndex = commandIndex;
	}
	
	public void dispose(){
		test = null;
		commandIndex = -1;
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	public void setCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
	}

	public FocUnitTest getTest() {
		return test;
	}
}
