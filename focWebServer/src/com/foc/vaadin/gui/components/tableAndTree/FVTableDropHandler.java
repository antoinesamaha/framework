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
package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.ITableTree;
import com.vaadin.data.Container;
import com.vaadin.data.util.ContainerOrderedWrapper;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;

@SuppressWarnings("serial")
public abstract class FVTableDropHandler implements DropHandler {

	private ITableTree table = null;
	
	public FVTableDropHandler(ITableTree table){
		this.table = table;
	}
	
	public void dispose(){
		table = null;
	}
	
	public FocList getFocList(){
		return table != null ? table.getFocList() : null;
	}
	
	public AcceptCriterion getAcceptCriterion() {
		return com.vaadin.event.dd.acceptcriteria.AcceptAll.get();
	}

	public FocObject getSourceFocObject(DragAndDropEvent dropEvent){
		FocObject sourceObj = null; 
		if (dropEvent != null && dropEvent.getTransferable() instanceof DataBoundTransferable) {			
			DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();
			if (transferable != null && transferable.getSourceContainer() instanceof Container) {
				Container container = (Container) transferable.getSourceContainer();
				if(container != null){
					Object sourceItemId = transferable.getItemId();
					sourceObj = (FocObject) container.getItem(sourceItemId);
				}
			}
		}
		return sourceObj;
	}
	
	public FocObject getTargetFocObject(DragAndDropEvent dropEvent){
		FocObject targetObject = null;
		if (dropEvent != null && dropEvent.getTransferable() instanceof DataBoundTransferable) {
			AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
			Object targetItemId = dropData.getItemIdOver();
			if(targetItemId != null){
				targetObject = getFocList() != null ? getFocList().searchByReference((Long) targetItemId) : null;
			}
		}
		return targetObject;
	}	
	
	public void drop(DragAndDropEvent dropEvent) {
		FocObject targetFocObject = getTargetFocObject(dropEvent);
		FocObject sourceFocObject = getSourceFocObject(dropEvent);
		
		if (sourceFocObject != null && targetFocObject != null){
			FocDesc sourceDesc = sourceFocObject.getThisFocDesc();
			FocDesc targetDesc = targetFocObject.getThisFocDesc();
			if(sourceDesc != null && targetDesc != null && sourceDesc.equals(targetDesc) && sourceDesc.hasOrderField()){
				FocList focList = getFocList();
				if(focList != null){
					int initialPosition = focList.getFocListElementPosition(getFocList().getFocListElement(sourceFocObject));
					int finalPosition   = focList.getFocListElementPosition(getFocList().getFocListElement(targetFocObject));
					
					if (initialPosition >= 0 && finalPosition >= 0 && finalPosition != initialPosition) {
						focList.elementMoved(initialPosition, finalPosition);
						if (table != null) {
							table.getFocDataWrapper().refreshGuiForContainerChanges();										
						}
					}
				}
			}
		}
	};
	
}
