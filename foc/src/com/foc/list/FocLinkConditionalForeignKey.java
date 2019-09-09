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
package com.foc.list;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;
import com.foc.property.IFDescProperty;

public class FocLinkConditionalForeignKey extends FocLinkForeignKey implements Cloneable {
	private FFieldPath descPropertyPathFromCurrentObject = null;
	
	public FocLinkConditionalForeignKey(FFieldPath descPropertyPathFromCurrentObject, boolean transactionalWithChildren){
		super(null, FField.NO_FIELD_ID, transactionalWithChildren);
		this.descPropertyPathFromCurrentObject = descPropertyPathFromCurrentObject;
	}
	
  public FocDesc getSlaveDesc(FocObject currentFocObject) {
  	IFDescProperty prop = (IFDescProperty)descPropertyPathFromCurrentObject.getPropertyFromObject(currentFocObject);
  	FocDesc desc = prop.getSelectedFocDesc();
  	setSlaveDesc(desc);
    return desc;
  }
  
  public FocLinkConditionalForeignKey clone(){
  	FocLinkConditionalForeignKey focLinkForeignKey = null;
  	try {
			focLinkForeignKey = (FocLinkConditionalForeignKey) super.clone();
		} catch (CloneNotSupportedException e) {
			Globals.logException(e);
		}
  	return focLinkForeignKey;
  }
}
