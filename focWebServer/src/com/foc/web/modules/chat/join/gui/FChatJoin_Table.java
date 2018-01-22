package com.foc.web.modules.chat.join.gui;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVTable;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.tableAndTree.FVColumnGenerator;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout_JoinTable;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.chat.FChat;
import com.foc.web.modules.chat.join.FChatJoin;
import com.foc.web.modules.chat.join.FChatJoinList;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FChatJoin_Table extends FocXMLLayout_JoinTable {

	private boolean withSelection = false;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		boolean createdHere = false;
		if (focData == null) {
			FChatJoinList list = new FChatJoinList(Globals.getApp().getUser_ForThisSession());
			focData = list;
			createdHere = true;
		}

		super.init(window, xmlView, focData);

		if (createdHere) setFocDataOwner(true);
	}
	
	@Override
	public FocDesc getOriginalFocDesc() {
		return FChat.getFocDesc();
	}

	@Override
	public int getOriginalObjectReference(FocObject focObject) {
		FChatJoin join = (FChatJoin) focObject;
		return join != null ? join.getChatReference() : 0;
	}

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
//		FVTableWrapperLayout tableDelegate = getTableWrapperLayout();
//		if(tableDelegate != null){
//			FocDataWrapper wrapper = tableDelegate.getFocDataWrapper();
//			if(wrapper != null){
//				wrapper.removeSiteFilter();
//			}
//		}
		
		TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
		if(tableTreeDelegate != null){
			tableTreeDelegate.toggleStatusStyleEnabled();
		}
	}

	private FVTableWrapperLayout getTableWrapperLayout() {
		return (FVTableWrapperLayout) getComponentByName("_Chatting");
	}

	private TableTreeDelegate getTableTreeDelegate() {
		return getTableWrapperLayout() != null ? getTableWrapperLayout().getTableTreeDelegate() : null;
	}

	private FocXMLGuiComponentDelegate getXMLGuiComponentDelegate() {
		FVTableWrapperLayout wrapper = getTableWrapperLayout();
		return wrapper != null ? ((FVTable) wrapper.getTableOrTree()).getDelegate() : null;
	}

	public void setNotEditable() {
		FocXMLGuiComponentDelegate delegate = getXMLGuiComponentDelegate();
		if (delegate != null) {
			delegate.setEditable(false);
			if (getTableTreeDelegate() != null) getTableTreeDelegate().refreshEditable();
		}
	}
	
	@Override
	public boolean table_LinkClicked(String tableName, ITableTree table, FVColumnGenerator colGen, FocObject focObject) {
		//boolean clicked = super.table_LinkClicked(tableName, table, colGen, focObject);
		boolean clicked = true;
		FChatJoin chat = (FChatJoin) focObject;
		if(chat != null) {
			String storageName = chat.getSubjectTableName();
			long   ref         = chat.getSubjectReference();
			if(!Utils.isStringEmpty(storageName) && ref > 0) {
				FocDesc focDesc = Globals.getApp().getFocDescByName(storageName);
				if(focDesc != null) {
					FocConstructor constr = new FocConstructor(focDesc, null);
					FocObject incident = constr.newItem();
					incident.setReference(ref);
					incident.load();
					XMLViewKey key = new XMLViewKey(focDesc.getStorageName(), XMLViewKey.TYPE_FORM);
					FocWebApplication.getFocWebSession_Static().setPrintingData(null, key, incident, false);
					FocWebApplication.getFocWebSession_Static().setFocDataOwner_ToPrint(true);
					UI.getCurrent().getPage().open(UI.getCurrent().getPage().getLocation().getPath(), "_blank");
				}
			}
		}
		return clicked;
	}

}