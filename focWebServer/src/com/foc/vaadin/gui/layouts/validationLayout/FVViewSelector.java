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
package com.foc.vaadin.gui.layouts.validationLayout;

import java.util.Collection;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.RightPanel;
import com.foc.vaadin.gui.components.FVComboBox;
import com.foc.vaadin.gui.components.FVGearWrapper;
import com.foc.vaadin.gui.windows.OptionSelectorWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVViewSelector extends FVGearWrapper<FVComboBox> {

	private ICentralPanel          centralPanel = null;
	private FVViewSelectorComboBox comboBox     = null;

	private boolean saveSelectionActive = true;

	public FVViewSelector(ICentralPanel centralPanel) {
		this(centralPanel, "View");
	}

	public FVViewSelector(ICentralPanel centralPanel, String caption) {
		super();
		
		addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
		
		this.centralPanel = centralPanel;
		comboBox = new FVViewSelectorComboBox();
		if(caption != null){
			comboBox.setCaption(caption);
		}
		comboBox.setWidth("100px");
		comboBox.setImmediate(true);

		XMLView xmlView = centralPanel.getXMLView();
		if(xmlView != null){

			XMLViewKey xmlViewKey = null;
			xmlViewKey = xmlView.getXmlViewKey();

			int rightLevel = centralPanel.getViewRights();
			comboBox.fillViewNames(xmlViewKey, rightLevel);

			// Adding the comboBox with or without gear depending on the right level.
			if(rightLevel == GroupXMLViewDesc.ALLOW_NOTHING || rightLevel == GroupXMLViewDesc.ALLOW_SELECTION){
				setComponent(comboBox, false);
			}else{
				setComponent(comboBox);
			}

			setView(xmlView.getXmlViewKey().getUserView());

			comboBox.addValueChangeListener(new Property.ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if(getCentralPanel() != null){
						getCentralPanel().beforeViewChangeListenerFired();
						
						String view = (String) event.getProperty().getValue();
						
						getCentralPanel().copyGuiToMemory();
						
						if(getCentralPanel().getXMLView() != null){
							XMLViewKey newKey = new XMLViewKey(getCentralPanel().getXMLView().getXmlViewKey());
							newKey.setUserView(view);
							XMLView xmlView = XMLViewDictionary.getInstance().get_WithoutAdjustToLastSelection(newKey);
							if(xmlView.getJavaClassName().equals(getCentralPanel().getClass().getName())){
								getCentralPanel().setXMLView(xmlView);
								getCentralPanel().re_parseXMLAndBuildGui();
							}else{
								ICentralPanel newCentralPanel = XMLViewDictionary.getInstance().newCentralPanel_NoAdjustmentToLastSelectedView(getWindow(), newKey, getCentralPanel().getFocData());
								FocCentralPanel window = getWindow(); 
								window.goBack(getCentralPanel());//This disposes the ViewSelector
								window.changeCentralPanelContent(newCentralPanel, false);
							}
							if(isSaveSelectionActive()){
								XMLViewDictionary.getInstance().userViewSelected_saveViewForUserAndKey(FocWebApplication.getFocUser(), newKey);
							}
						}
					}
				}
			});
		}
	}

	public void dispose() {
		super.dispose();
		if(comboBox != null){
			comboBox.dispose();
			comboBox = null;
		}
		centralPanel = null;
	}

	public FVComboBox getComboBox() {
		return (FVComboBox) getComponent();
	}

	public void addView(String viewName) {
		if(getComboBox() != null){
			getComboBox().addItem(viewName);
		}
	}

	public void removeView(String view) {
		if(getComboBox() != null){
			getComboBox().removeItem(view);
			// getComboBox().requestRepaint();
		}
	}

	public void setView_WithoutSavingSelection(String view) {
		setSaveSelectionActive(false);
		setView(view);
		setSaveSelectionActive(true);
	}

	public void setView(String view) {
		if(getComboBox() != null){
			getComboBox().setValue(view);
		}
	}

	public boolean containsView(String view) {
		boolean contains = false;
		try{
			if(view != null && !view.isEmpty() && getComboBox() != null){
				Collection<String> collection = (Collection<String>) getComboBox().getVisibleItemIds();
				if(collection != null){
					contains = collection.contains(view);
				}
			}
		}catch (Exception e){
			Globals.logException(e);
			contains = false;
		}
		return contains;
	}

	/*
	 * public void resetView(){
	 * 
	 * }
	 */

	@Override
	public void fillMenu(VerticalLayout root) {
		// Globals.getApp().getUser_ForThisSession().allowViewModification(xmlViewKey)
		PopupLinkButton modifyButton = new PopupLinkButton("Modify", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(getCentralPanel().getXMLView().isSystemView() && !ConfigInfo.isForDevelopment()){
					Globals.showNotification("Cannot modify system view.", "You can duplicate it.", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
				}else{
					if(centralPanel != null){
						INavigationWindow window = centralPanel.getMainWindow();
						if(window != null){
							RightPanel rightPanel = (RightPanel) centralPanel.getRightPanel(true);// new
																																										// RightPanel(centralPanel,
																																										// focDesc);
							window.addUtilityPanel(rightPanel);
						}
					}
				}
			}
		});
		root.addComponent(modifyButton);

		PopupLinkButton duplicateButton = new PopupLinkButton("Duplicate", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				OptionSelectorWindow displayWindow = new OptionSelectorWindow(FVViewSelector.this);
				displayWindow.duplicate();
				FocWebApplication.getInstanceForThread().addWindow(displayWindow);
			}
		});
		root.addComponent(duplicateButton);

		PopupLinkButton addButton = new PopupLinkButton("New", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				OptionSelectorWindow displayWindow = new OptionSelectorWindow(FVViewSelector.this);
				displayWindow.newViewWindow();
				FocWebApplication.getInstanceForThread().addWindow(displayWindow);
			}
		});
		root.addComponent(addButton);

		PopupLinkButton delButton = new PopupLinkButton("Delete", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(getCentralPanel().getXMLView().isSystemView()){
					Globals.showNotification("Cannot delete system views", "", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
				}else{
					OptionSelectorWindow displayWindow = new OptionSelectorWindow(FVViewSelector.this);
					displayWindow.deleteView(comboBox.getValue().toString());
					FocWebApplication.getInstanceForThread().addWindow(displayWindow);
				}
			}
		});
		root.addComponent(delButton);

		PopupLinkButton showIncludesButton = new PopupLinkButton("Show included views", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// if(getCentralPanel().getXMLView().isSystemView()){
				// Globals.showNotification("Cannot delete system views", "",
				// FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
				// }else{

				ICentralPanel centralPanel = getCentralPanel();
				if(centralPanel instanceof FocXMLLayout){
					FocXMLLayout lay = (FocXMLLayout) centralPanel;
					lay.scanLayoutsAndShowViewValidationLayouts();
				}

			}
		});
		root.addComponent(showIncludesButton);

		PopupLinkButton setForPrintingButton = new PopupLinkButton("Set as default printing view", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				XMLView xmlView = getCentralPanel() != null ? getCentralPanel().getXMLView() : null;
				if(xmlView != null){
					XMLViewKey xmlViewKey = xmlView.getXmlViewKey();
					if(xmlViewKey != null){
						XMLViewDictionary xmlViewDictionary = XMLViewDictionary.getInstance();
						xmlViewDictionary.userViewSelected_saveViewForUserPrintingView(FocWebApplication.getFocUser(), xmlViewKey);
						Globals.showNotification("Printing view has been updated.", "", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
					}
				}
			}
		});
		root.addComponent(setForPrintingButton);
	}

	public ICentralPanel getCentralPanel() {
		return centralPanel;
	}

	private boolean isSaveSelectionActive() {
		return saveSelectionActive;
	}

	private void setSaveSelectionActive(boolean saveSelectionActive) {
		this.saveSelectionActive = saveSelectionActive;
	}
}
