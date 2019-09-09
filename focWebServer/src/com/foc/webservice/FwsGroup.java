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
package com.foc.webservice;

import com.foc.admin.FocGroup;

public class FwsGroup {
	

	
	
	private FocGroup group = null;
	public FocGroup getGroup() {
		return group;
	}
	public void setGroup(FocGroup group) {
		this.group = group;
	}

	private int ref = 0;
	public int getRef() {
		return ref;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}
	
	
}
