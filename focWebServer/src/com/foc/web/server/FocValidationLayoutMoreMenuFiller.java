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
package com.foc.web.server;

import com.foc.business.workflow.implementation.ILoggable;
import com.foc.business.workflow.implementation.Loggable;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.menuBar.FVMenuBarCommand;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.IValidationLayoutMoreMenuFiller;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.MenuBar.MenuItem;

public class FocValidationLayoutMoreMenuFiller implements IValidationLayoutMoreMenuFiller {

  @SuppressWarnings("serial")
  public void addMenuItems(FVValidationLayout validationLayout) {
    if (validationLayout != null) {
    	
      final FocObject focObject = validationLayout.getFocObject();

      if (focObject != null && focObject.workflow_IsLoggable() && focObject.workflow_IsAllowViewLog()) {//!(focData instanceof WBSPointer)

        if (validationLayout.getCentralPanel() != null) {
          XMLView xmlView = validationLayout.getCentralPanel().getXMLView();
          if (xmlView != null) {
            XMLViewKey key = xmlView.getXmlViewKey();

            if (key != null) {
              String storageName = key.getStorageName();

              if (!storageName.contains("WF_LOG") && (validationLayout.getValidationSettings() != null && validationLayout.getValidationSettings().isWithLog())) {
                validationLayout.addMoreItem("View Logs", new FVMenuBarCommand() {
                  public void menuSelected(MenuItem selectedItem) {
                    ILoggable iLoggable = (ILoggable) focObject;
                    Loggable loggable = iLoggable.iWorkflow_getWorkflow();

                    if (loggable != null) {
                    	String context = XMLViewKey.CONTEXT_DEFAULT;
                    	if(!focObject.workflow_IsWorkflowSubject()) {
                    		context = WorkflowWebModule.CTXT_WF_NO_WORKFLOW__LOG_ONLY;
                    	}
                    	
                      INavigationWindow mainWindow = (INavigationWindow) getValidationLayout().getCentralPanel().getMainWindow();
                      XMLViewKey xmlViewKey = new XMLViewKey(WFLogDesc.WF_LOG_VIEW_KEY, XMLViewKey.TYPE_FORM, context, XMLViewKey.VIEW_DEFAULT);
                      ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, xmlViewKey, focObject);
                      mainWindow.changeCentralPanelContent(centralPanel, true);
                    }
                  }
                });
              }
            }
          }
        }
      }
      /*
      if (focObject != null && focObject.code_hasCode() && focObject.code_prefixHasChanged() && focObject.workflow_IsAllowModification_Code()){
        validationLayout.addMoreItem("Reset Code", new FVMenuBarCommand() {
          public void menuSelected(MenuItem selectedItem) {
          	if(focObject != null){
          		focObject.code_resetCodeIfPrefixHasChanged();
          		
          	}
          }
        });
      }
      */
    }
  }
}
