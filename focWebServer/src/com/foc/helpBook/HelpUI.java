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
package com.foc.helpBook;

import com.foc.Globals;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVButton;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class HelpUI extends FocWebApplication {
	
	private FVButton nextPageButton     = null;
	private FVButton previousPageButton = null;
	
	@Override
	public void init(VaadinRequest request){
		super.init(request);
	}

	@Override
	public FocCentralPanel newWindow() {
		FocCentralPanel focCentralPanel = new FocCentralPanel();
		focCentralPanel.fill();
		
		Label labelHelpContent = new Label();
		labelHelpContent.setContentMode(ContentMode.HTML);
		
		String htmlContent = (String) FocWebApplication.getFocWebSession_Static().getParameter("HELP_CONTENT");
		labelHelpContent.setValue(htmlContent);
		VerticalLayout mainLayout = new VerticalLayout();

		mainLayout.setSizeFull();
		
		HorizontalLayout navigationLayout = new HorizontalLayout();
		navigationLayout.setWidth("95%");

		navigationLayout.addComponent(getPreviousPageButton());
		navigationLayout.setComponentAlignment(getPreviousPageButton(), Alignment.BOTTOM_LEFT);
		
		navigationLayout.addComponent(getNextPageButton());
		navigationLayout.setComponentAlignment(getNextPageButton(), Alignment.BOTTOM_RIGHT);

		mainLayout.addComponent(labelHelpContent);
		
		mainLayout.addComponent(navigationLayout);
		mainLayout.setComponentAlignment(navigationLayout, Alignment.BOTTOM_CENTER);
		
		focCentralPanel.addComponent(mainLayout);
		return focCentralPanel;
	}
	
	private FVButton getNextPageButton(){
		if(nextPageButton == null){
			nextPageButton = new FVButton("Next");
			nextPageButton.setStyleName(Runo.BUTTON_LINK);
			
			nextPageButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					int currentPageIndex = FocHelpBook.getInstance().getCurrentPageIndex();
					int nextPageIndex    = currentPageIndex + 1;
					
					if(nextPageIndex < FocHelpBook.getInstance().getPagesArrayListSize()){
						changeHelpPage(nextPageIndex);
					}else{
						changeHelpPage(0);
					}
				}
			});
		}
		return nextPageButton;
	}
	
	private FVButton getPreviousPageButton(){
		if(previousPageButton == null){
			previousPageButton = new FVButton("Previous");
			previousPageButton.setStyleName(Runo.BUTTON_LINK);
			
			previousPageButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					int currentPageIndex  = FocHelpBook.getInstance().getCurrentPageIndex();
					int previousPageIndex = currentPageIndex - 1;
					
					if(previousPageIndex >= 0){
						changeHelpPage(previousPageIndex);
					}else{
						int lastPageIndex = FocHelpBook.getInstance().getLastPageIndex();
						changeHelpPage(lastPageIndex);
					}
				}
			});
		}
		return previousPageButton;
	}
	
	private void changeHelpPage(int pageIndex){
		String focHelpPageCode = FocHelpBook.getInstance().getPageCodeByPageIndex(pageIndex);
		if(focHelpPageCode != null && !focHelpPageCode.isEmpty()){
			FocHelpPage focHelpPage = FocHelpBook.getInstance().getPage(focHelpPageCode);
			if(focHelpPage != null){
				FocWebApplication.getFocWebSession_Static().putParameter("HELP_CONTENT", focHelpPage.getHelpMessage());
				FocCentralPanel centralComponent = newWindow();
				setContent(centralComponent);
			}
		}
	}

	@Override
	public String getThemeName() {
		return Globals.getIFocNotification() != null ? Globals.getIFocNotification().getThemeName() : "";
	}
}
