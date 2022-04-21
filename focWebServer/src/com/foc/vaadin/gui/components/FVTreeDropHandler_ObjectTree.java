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
package com.foc.vaadin.gui.components;

import com.foc.access.FocDataMap;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.tree.objectTree.FObjectNode;
import com.foc.tree.objectTree.FObjectTree;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.admin.OptionDialog_Form;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.util.ContainerOrderedWrapper;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVTreeDropHandler_ObjectTree implements DropHandler {

	private OptionDialog_Form optionDialogForm = null;
	private FObjectTree       objectTree       = null;
	private FObjectNode       sourceNode       = null;
	private FObjectNode       targetNode       = null;
	private FocXMLLayout      focXMLLayout     = null;
	private String            treeName         = null;
	private IFocData          focData          = null;
	
	public FVTreeDropHandler_ObjectTree(FocXMLLayout focXMLLayout, IFocData focData, String treeName) {
		this.focXMLLayout = focXMLLayout;
		this.treeName     = treeName;
		this.focData      = focData;
	}
	
	public void dispose(){
		objectTree = null;
		sourceNode = null;
  	targetNode = null;
		focData = null;
		optionDialogForm = null;
		focXMLLayout = null;
		treeName = null;
	}
	
	@Override
	public void drop(DragAndDropEvent dropEvent) {
		if(dropEvent.getTransferable() instanceof DataBoundTransferable){
			DataBoundTransferable t = (DataBoundTransferable) dropEvent.getTransferable();
			if(t.getSourceContainer() instanceof ContainerOrderedWrapper){
				ContainerOrderedWrapper source = (ContainerOrderedWrapper) t.getSourceContainer();
				Object sourceItemId = t.getItemId();
				FocObject objDroped = (FocObject) source.getItem(sourceItemId);
				
				AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
				Object targetItemId = dropData.getItemIdOver();

				if(focData != null && focData instanceof FocDataMap){
					FocDataMap focDataMap = (FocDataMap) focData;
					focData = focDataMap.getMainFocData();
				}

				if(focData != null && focData instanceof FObjectTree){
					FObjectTree objectTree = (FObjectTree) focData;
					setObjectTree(objectTree);
					if(objectTree != null){
						targetNode = (FObjectNode) objectTree.findNode(((Long)targetItemId).longValue());
						sourceNode = (FObjectNode) objectTree.findNode(objDroped.getReference().getLong());
						
						if(targetNode != null && sourceNode != null && focXMLLayout != null){
						
							XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.OPTION_WINDOW_STORAGE, XMLViewKey.TYPE_FORM);
							OptionDialog_Form optionDialogForm = (OptionDialog_Form) XMLViewDictionary.getInstance().newCentralPanel(focXMLLayout.getMainWindow(), xmlViewKey, null);
							
							setOptionDialog_Form(optionDialogForm);
							
							ClickListener clickListener = new ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									FocObject targetObject = (FocObject) targetNode.getObject();
									FocObject sourceObject = (FocObject) sourceNode.getObject();
									
									boolean shouldExecute = (sourceObject != null && sourceObject != targetObject) && (!sourceNode.isAncestorOf(targetNode));
									
									if(shouldExecute && getObjectTree() != null){
										
										sourceObject.setPropertyObject(getObjectTree().getFatherNodeId(), targetObject);
										sourceNode.moveTo(targetNode);
										
										FVTableWrapperLayout bkdnTreeWrapper = (FVTableWrapperLayout) focXMLLayout.getComponentByName(treeName);
										if(bkdnTreeWrapper != null){
											FVTreeTable treeTable = ((FVTreeTable)bkdnTreeWrapper.getTableOrTree());
											if(treeTable != null && treeTable instanceof FVTreeTable){
												getOptionDialog_Form().goBack(null);
												treeTable.markAsDirty();
												treeTable.applyFocListAsContainer();
												treeTable.refreshRowCache_Foc();
												focXMLLayout.refresh();
											}
										}
									}
								}
							};
							
							optionDialogForm.addButton("Confirm node move", clickListener);
							
							clickListener = new ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									getOptionDialog_Form().goBack(null);									
								}
								
							};
							optionDialogForm.addButton("Cancel", clickListener);
							
							FocCentralPanel centralPanel = new FocCentralPanel();
							centralPanel.fill();
							centralPanel.changeCentralPanelContent(getOptionDialog_Form(), false);
							Window optionWindow = centralPanel.newWrapperWindow();
							optionWindow.setCaption("Node Move Confirmation");
							optionWindow.setWidth("400px");
							optionWindow.setHeight("200px");
							focXMLLayout.getUI().addWindow(optionWindow);
						}
					}
				}
			}
		}
	}
	
	@Override
	public AcceptCriterion getAcceptCriterion() {
		return null;
	}
	
	private void setObjectTree(FObjectTree objectTree){
		this.objectTree = objectTree;
	}
	
	private FObjectTree getObjectTree(){
		return objectTree;
	}
	
	private void setOptionDialog_Form(OptionDialog_Form optionDialogForm){
		this.optionDialogForm = optionDialogForm;
	}
	
	private OptionDialog_Form getOptionDialog_Form(){
		return optionDialogForm;
	}

}
