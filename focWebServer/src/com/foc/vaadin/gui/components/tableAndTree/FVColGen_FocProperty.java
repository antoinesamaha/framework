package com.foc.vaadin.gui.components.tableAndTree;

import java.util.Collection;
import java.util.Locale;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FCloudStorageField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FImageField;
import com.foc.list.FocList;
import com.foc.property.FCloudStorageProperty;
import com.foc.property.FDate;
import com.foc.property.FDouble;
import com.foc.property.FInt;
import com.foc.property.FMultipleChoice;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.property.FString;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVComboBox;
import com.foc.vaadin.gui.components.FVImageField;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVLabelInTable;
import com.foc.vaadin.gui.components.FVMultipleChoiceComboBox;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.FVObjectPopupView;
import com.foc.vaadin.gui.components.FVObjectSelector;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.PopupView.PopupVisibilityListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class FVColGen_FocProperty extends FVColumnGenerator {

	private FocusListener formulaFocusListener  = null;
	private FocusListener readOnlyFocusListener = null;
	private BlurListener  readOnlyBlurListener  = null;
	private boolean       isDataPathFoundInDesc = false;

	private long lastFocusCalled = 0;

	public FVColGen_FocProperty(FVTableColumn tableColumn) {
		super(tableColumn);

		FocDesc focDesc = getTreeOrTable().getFocDesc();
		Collection<String> fieldNamesInFocDesc = focDesc.vaadin_getFieldNames();
		if(fieldNamesInFocDesc.contains(getTableColumn().getDataPath())){
			this.isDataPathFoundInDesc = true;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		hyperLinkButtonListener = null;
	}

	public boolean isUseReadOnlyFlag() {
		return false;
	}

	private int getPropertyAccessRight(FProperty property) {
		FocObject focObject = property != null ? property.getFocObject() : null;
		int accessRight = FocObject.PROPERTY_RIGHT_READ_WRITE;
		if(focObject != null){
			accessRight = focObject.getPropertyAccessRight(property.getFocField().getID());
		}
		return accessRight;
	}

	private Object getDisplayObject_ForProperty_Editable(FocObject focObject, FProperty property, FVTableColumn column, Object columnId) {
		Object objReturned = null;

		FField field = property != null ? property.getFocField() : null;

		if(field instanceof FImageField){
			objReturned = (AbstractComponent) getTableTreeDelegate().newGuiComponent(focObject, column, property);
			FVImageField imageField = (FVImageField) objReturned;
			imageField.setWidth("60px");
			imageField.setHeight("50px");
		}else if(field instanceof FCloudStorageField){
			FCloudStorageProperty csProp = (FCloudStorageProperty) property;
			if(csProp != null){
				FVImageField imageField = new FVImageField(csProp, column.getAttributes());
				if(imageField.getBufferedImage() == null){
					Resource resource = imageField.getResourceAndSetIcon();
					if(resource != null){
						Button docIcon = new Button();
						docIcon.setStyleName(Runo.BUTTON_LINK);
						docIcon.setWidth("-1px");
						docIcon.setHeight("-1px");
						docIcon.setIcon(resource);
						objReturned = docIcon;
					}
				}else{
					Image image = imageField.getEmbedded();
					if(image != null){
						int maxWidth = 150;
						int maxHeight = 150;
						imageField.resizeImage(image, maxWidth, maxHeight);
					}
					objReturned = imageField;
				}
			}
		}else{
			objReturned = "";
			if(property != null){
				AbstractComponent abstractComponent = (AbstractComponent) getTableTreeDelegate().newGuiComponent(focObject, column, property);

				if(abstractComponent != null){
					objReturned = abstractComponent;
					abstractComponent.addStyleName("editableStyle");

					if(abstractComponent instanceof FVTextField){
						// ((FVTextField) abstractComponent).selectAll();
						/*
						 * ((FVTextField) abstractComponent).addFocusListener(new
						 * FocusListener() { private boolean updating = false; public void
						 * focus(FocusEvent event) { if(!updating){ updating = true;
						 * ((FVTextField) abstractComponent).selectAll(); updating = false;
						 * } } });
						 */
						// Adding the focus listener on the component to be used in the
						// formula layout
						if(getFormulaFocusListener() != null){
							((FVTextField) abstractComponent).addFocusListener(getFormulaFocusListener());
						}
						if(isUseReadOnlyFlag()){
							((FVTextField) abstractComponent).addFocusListener(getReadOnlyFocusListener(true));
							((FVTextField) abstractComponent).addBlurListener(getReadOnlyBlurListener(true));
						}
					}else if(abstractComponent instanceof FVCheckBox){
						abstractComponent.setCaption("");
					}else if(abstractComponent instanceof FVComboBox){
						// Adding the focus listener on the component to be used in the
						// formula layout
						if(getFormulaFocusListener() != null){
							((FVComboBox) abstractComponent).addFocusListener(getFormulaFocusListener());
						}
						if(isUseReadOnlyFlag()){
							((FVComboBox) abstractComponent).addFocusListener(getReadOnlyFocusListener(true));
							((FVComboBox) abstractComponent).addBlurListener(getReadOnlyBlurListener(true));
						}
						
					}else if(abstractComponent instanceof FVObjectPopupView){
						// Adding the focus listener on the component to be used in the
						// formula layout
						FVObjectPopupView popupView = ((FVObjectPopupView) abstractComponent);
						if(popupView != null && getFormulaFocusListener() != null){
							popupView.addPopupVisibilityListener(new PopupVisibilityListener() {

								@Override
								public void popupVisibilityChange(PopupVisibilityEvent event) {
									if(getTableTreeDelegate() != null && event != null && event.getPopupView() != null && event.getPopupView() instanceof FVObjectPopupView){
										FVObjectPopupView popupView = (FVObjectPopupView) event.getPopupView();
										getTableTreeDelegate().adjustFormulaLayoutForComponent(popupView);
									}
								}
							});
//							popupView.addFocusListener(getFormulaFocusListener());
						}
//						if(isUseReadOnlyFlag()){
//							popupView.addFocusListener(getReadOnlyFocusListener(true));
//							popupView.addBlurListener(getReadOnlyBlurListener(true));
//						}
					}else if(abstractComponent instanceof FVObjectSelector){
						// Adding the focus listener on the component to be used in the
						// formula layout
						FVObjectComboBox comboBox = ((FVObjectSelector) abstractComponent).getComboBox();
						if(comboBox != null && getFormulaFocusListener() != null){
							comboBox.addFocusListener(getFormulaFocusListener());
						}
						if(isUseReadOnlyFlag()){
							comboBox.addFocusListener(getReadOnlyFocusListener(true));
							comboBox.addBlurListener(getReadOnlyBlurListener(true));
						}
					}else if(abstractComponent instanceof FVObjectComboBox && getFormulaFocusListener() != null){
						FVObjectComboBox comboBox = (FVObjectComboBox) abstractComponent;
						if(comboBox != null){
							comboBox.addFocusListener(getFormulaFocusListener());
						}
						if(isUseReadOnlyFlag()){
							comboBox.addFocusListener(getReadOnlyFocusListener(true));
							comboBox.addBlurListener(getReadOnlyBlurListener(true));
						}
					}else if(abstractComponent instanceof FVMultipleChoiceComboBox && getFormulaFocusListener() != null){
						FVMultipleChoiceComboBox comboBox = (FVMultipleChoiceComboBox) abstractComponent;
						if(comboBox != null){
							comboBox.addFocusListener(getFormulaFocusListener());
						}
						if(isUseReadOnlyFlag()){
							comboBox.addFocusListener(getReadOnlyFocusListener(true));
							comboBox.addBlurListener(getReadOnlyBlurListener(true));
						}
					}
					// Setting the column and row Ids of component
					FocXMLGuiComponent focXMLGuiComponent = (FocXMLGuiComponent) abstractComponent;
					FocXMLGuiComponentDelegate focXMLGuiComponentDelegate = focXMLGuiComponent.getDelegate();
					if(focXMLGuiComponentDelegate != null){
						focXMLGuiComponentDelegate.setColumnId(columnId);
						if(focObject != null && focObject.getReference() != null){
							focXMLGuiComponentDelegate.setRowId(focObject.getReference().getLong());
						}
					}
					abstractComponent.setId(column.getDataPath());
					abstractComponent.setWidth("100%");// This was added to get the node
																							// name text field in the BKDN
																							// tree big enough to enter or see
																							// the value
					// If we put 100% in the xml we get a small width extendable. but with
					// this solution we have a fixed minimum width in the xml + the 100%
					// allows us to change the width
					if(isUseReadOnlyFlag()){
						abstractComponent.setReadOnly(true);
					}
				}
			}
		}

		if(property != null && property.isInherited()){
			FProperty inheritedProp = null;
			try{
				inheritedProp = property.getFocField().getInheritedPropertyGetter().getInheritedProperty(property.getFocObject(), property);
			}catch (Exception e){
				Globals.logException(e);
			}
			if(inheritedProp != null){
				objReturned = inheritedProp;
			}
		}
		return objReturned;
	}

	private Object getDisplayObject_ForProperty_NonEditable(FocObject focObject, FProperty property, FVTableColumn column, Object columnId) {
		Object objReturned = null;
		FField field = property != null ? property.getFocField() : null;
		if(property != null){
			// Remove the editable component in case the editing status has changed
			// --------------------------------------------------------------------
			String objRef = focObject != null && focObject.getReference() != null ? focObject.getReference().toString() : null;
			String compName = TableTreeDelegate.newComponentName(getTableName(), objRef, (String) column.getName());
			FocXMLLayout focXMLLayout = getFocXMLLayout();
			if(focXMLLayout != null){
				FocXMLGuiComponent comp = focXMLLayout.removeComponentByName(compName);
				if(comp != null){
					comp.dispose();
				}
			}
			// --------------------------------------------------------------------
			objReturned = property.vaadin_TableDisplayObject(column.getFormat(), column.getCaptionProp());
			
			if(objReturned instanceof Boolean){
				if(((Boolean) objReturned).booleanValue()){
					objReturned = new Embedded("", new ThemeResource("../runo/icons/16/ok.png"));
				}else{
					// objReturned = new Embedded("", new
					// ThemeResource("../runo/icons/16/cancel.png"));
					objReturned = FVIconFactory.getInstance().getFVIcon_Embedded(FVIconFactory.ICON_EMPTY, FVIconFactory.SIZE_SMALL);
				}
			}else if(field instanceof FCloudStorageField){
				FCloudStorageProperty csProp = (FCloudStorageProperty) property;
				if(csProp != null){
					FVImageField imageField = new FVImageField(csProp, column.getAttributes());
					if(imageField.getBufferedImage() == null){
						Resource resource = imageField.getResourceAndSetIcon();
						if(resource != null){
							Button docIcon = new Button();
							docIcon.setStyleName(Runo.BUTTON_LINK);
							docIcon.setWidth("-1px");
							docIcon.setHeight("-1px");
							docIcon.setIcon(resource);
							objReturned = docIcon;
						}
					}else{
						Image image = imageField.getEmbedded();
						if(image != null){
							int maxWidth = 150;
							int maxHeight = 150;
							imageField.resizeImage(image, maxWidth, maxHeight);
						}
						objReturned = imageField;
					}
				}
			}else if(property instanceof FObject || property instanceof FDate || property instanceof FString || property instanceof FDouble || (property instanceof FInt && !(property instanceof FMultipleChoice))){
				FVLabel lbl = null;
				String  styleAttrib   = column.getAttributes() != null ? column.getAttributes().getValue(FXML.ATT_STYLE) : null;
				int     maxCharacters = column.getMaxCharacters();
				String ttt = null;
				
				if(styleAttrib != null && !styleAttrib.isEmpty()){
					lbl = new FVLabel((String) objReturned);
					lbl.parseStyleAttributeValue(styleAttrib);
					objReturned = lbl;
				}else if(property instanceof FDouble || property instanceof FInt){
					lbl = new FVLabelInTable(property, focObject, column);
					lbl.copyMemoryToGui();
					lbl.addStyleName("foc-text-right");
					objReturned = lbl;
				}
				
				//If RTL I have t put the String in a label to align right
				if(lbl == null && getFocXMLLayout().isRTL()){
					lbl = new FVLabelInTable(property, focObject, column);
					lbl.copyMemoryToGui();
					lbl.addStyleName("foc-text-right");
					objReturned = lbl;
				}
				
				//Setting the TTT is necessary
				//We have 2 conditions: either the objReturned is String or Label.
				//----------------------------
				if(lbl != null){
					String originalValue = lbl.getValue(); 
					if(maxCharacters > 0 && originalValue != null && originalValue.length() > maxCharacters){
						ttt = originalValue;
						
//						lbl.setLocale();
						if(getFocXMLLayout().isRTL()){
							lbl.addStyleName("foc-cellComment-left");
//							lbl.setLocale(new Locale("ar"));
							StringBuffer buff = new StringBuffer(ttt.substring(0, maxCharacters));
//							buff.append("...؟");
							buff.append("...");
							lbl.setValue(buff.toString());
						}else{
							lbl.addStyleName("foc-cellComment-right");
							lbl.setValue(ttt.substring(0, maxCharacters)+"...");
						}
					}
				}else if(objReturned instanceof String){
					String originalValue = (String) objReturned;
					if(maxCharacters > 0 && originalValue != null && originalValue.length() > maxCharacters){
						ttt = originalValue;
						if(getFocXMLLayout().isRTL()){
							objReturned = ttt.substring(0, maxCharacters)+"...";
						}
						else objReturned = ttt.substring(0, maxCharacters)+"...";
					}
				}
				if(ttt != null && getTableTreeDelegate() != null){
					getTableTreeDelegate().addCellTooltipText(focObject.getReference(), columnId, ttt);
				}
				//----------------------------
				
				if(lbl != null){
					if(getFocXMLLayout().isRTL()) lbl.addStyleName("foc-text-right"); 
					FocXMLGuiComponent focXMLGuiComponent = lbl;
					FocXMLGuiComponentDelegate componentDelegate = focXMLGuiComponent.getDelegate();
					if(componentDelegate != null){
						componentDelegate.setColumnId(columnId);
						if(focObject != null && focObject.getReference() != null){
							componentDelegate.setRowId(focObject.getReference().getLong());
						}
					}
				}
				if(lbl != null && getFocXMLLayout() != null) getFocXMLLayout().putComponent(compName, lbl);				
				FocXMLAttributes attributes = column.getAttributes();
				if(attributes != null && attributes.getValue(FXML.ATT_LINK) != null && attributes.getValue(FXML.ATT_LINK).equals("true")){
					HyperLinkButton button = new HyperLinkButton(focObject, objReturned);
					button.addClickListener(hyperLinkButtonListener);
					button.addStyleName("focLinkInTable");
					objReturned = button;
					
//					if(getTableTreeDelegate().getViewContainer_ForOpen() == ITableTree.VIEW_CONTAINER_NEW_BROWSER_TAB){
//				  	BrowserWindowOpener opener = null; 
//				  	opener = new BrowserWindowOpener(UI.getCurrent().getClass());
//				    opener.extend(button);
//					}
				}
			}else{
				objReturned = property;
				if((property instanceof FMultipleChoice)){
					FocXMLAttributes attributes = column.getAttributes();
					if(attributes != null && attributes.getValue(FXML.ATT_LINK) != null && attributes.getValue(FXML.ATT_LINK).equals("true")){
						HyperLinkButton button = new HyperLinkButton(focObject, property.getString());
						button.addClickListener(hyperLinkButtonListener);
						button.addStyleName("focLinkInTable");
						objReturned = button;
					}
				}
			}
		}
//		if(objReturned instanceof String){
//			objReturned = new Label(objReturned.toString());
//		}
		return objReturned;
	}

	private FocusListener getFormulaFocusListener() {
		TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
		if(formulaFocusListener == null && tableTreeDelegate != null && tableTreeDelegate.getFormulaForm() != null){
			formulaFocusListener = new FocusListener() {
				@Override
				public void focus(FocusEvent event) {
					if(event.getComponent() instanceof FocXMLGuiComponent){
						FocXMLGuiComponent component = (FocXMLGuiComponent) event.getComponent();
						getTableTreeDelegate().adjustFormulaLayoutForComponent(component);
					}
				}
			};
		}
		return formulaFocusListener;
	}

	private FocusListener getReadOnlyFocusListener(boolean create) {
		if(readOnlyFocusListener == null && create){
			readOnlyFocusListener = new FocusListener() {
				@Override
				public void focus(FocusEvent event) {
					if(event.getComponent() instanceof AbstractComponent){

						if(lastFocusCalled < System.currentTimeMillis() - 500){
							Globals.logString("---------------------- Focus : " + event.getComponent().getId() + "   " + (lastFocusCalled - System.currentTimeMillis()));
							lastFocusCalled = System.currentTimeMillis();
							// Thread.dumpStack();
							event.getComponent().setReadOnly(false);
						}
					}
				}
			};
		}
		return readOnlyFocusListener;
	}

	private BlurListener getReadOnlyBlurListener(boolean create) {
		if(readOnlyBlurListener == null && create){
			readOnlyBlurListener = new BlurListener() {
				@Override
				public void blur(BlurEvent event) {
					if(event.getComponent() instanceof AbstractComponent){
						if(lastFocusCalled < System.currentTimeMillis() - 500){
							Globals.logString("---------------------- Blur : " + event.getComponent().getId() + "   " + (lastFocusCalled - System.currentTimeMillis()));
							// Thread.dumpStack();
							event.getComponent().setReadOnly(true);
						}
					}
				}
			};
		}
		return readOnlyBlurListener;
	}

	public boolean isEditable() {
		return ((FocXMLGuiComponent) getTreeOrTable()).getDelegate().isEditable();
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object object = null;

		ITableTree tableTree = getTreeOrTable();
		FocList list = tableTree != null ? tableTree.getFocList() : null;
		FocObject focObject = list != null ? list.searchByReference((Long) itemId) : null;
		FProperty property = focObject != null ? focObject.getFocPropertyForPath(getTableColumn().getDataPath()) : null;
//		FProperty property = (FProperty) (focObject != null ? focObject.iFocData_getDataByPath(getTableColumn().getDataPath()) : null);
		FField field = property != null ? property.getFocField() : null;

		if(focObject != null && property != null && field != null){
			int accessRight = getPropertyAccessRight(property);

			if(accessRight == FocObject.PROPERTY_RIGHT_READ_WRITE){

				if(!isDataPathFoundInDesc && !getTableColumn().isEditable_ExplicitlyInAttribute()){
					accessRight = FocObject.PROPERTY_RIGHT_READ;
				}else{
					boolean editable = (getFocXMLLayout() == null || getFocXMLLayout().isEditable()) && !property.isValueLocked() && isEditable() && !field.isReflectingField() && getTableColumn().isColumnEditable();
					if(editable){
						FocObject directFatherFocObject = property.getFocObject();
						if(directFatherFocObject != null){
							editable = !directFatherFocObject.isPropertyLocked(property.getFocField().getID());
						}
					}

					if(editable){
						if(getTableTreeDelegate().getEditingMode() == TableTreeDelegate.MODE_EDITABLE_ON_SELECTION){
							if(focObject != null && focObject.getReference() != null){
								editable = getTable().isSelected(focObject.getReference().getLong());
							}
						}else if(getTableTreeDelegate().getEditingMode() == TableTreeDelegate.MODE_EDITABLE){

						}else{
							editable = false;
						}

						if(property.isInherited()){
							editable = false;
						}
					}

					if(!editable){
						accessRight = FocObject.PROPERTY_RIGHT_READ;
					}
				}
			}

			// At that stage I have the accessRight variable that contains exactly
			// what we need to create
			if(accessRight == FocObject.PROPERTY_RIGHT_READ_WRITE){
				object = getDisplayObject_ForProperty_Editable(focObject, property, getTableColumn(), columnId);
			}else if(accessRight == FocObject.PROPERTY_RIGHT_READ){
				object = getDisplayObject_ForProperty_NonEditable(focObject, property, getTableColumn(), columnId);
			}else{
				object = FField.NO_RIGHTS_STRING;
			}
		}

		if(object instanceof Component){
			((Component) object).addStyleName("foc-tablecellprint");
		}
		
		return object;
	}

	private ClickListener hyperLinkButtonListener = new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			HyperLinkButton hyperLinkButton = (HyperLinkButton) event.getButton();
			if(hyperLinkButton != null){
				FocObject focObject = hyperLinkButton.getFocObject();
				if(focObject != null){
					boolean consumed = getFocXMLLayout() == null ? false : getFocXMLLayout().table_LinkClicked(getTableName(), getTreeOrTable(), FVColGen_FocProperty.this, focObject);
					if(!consumed && getTableTreeDelegate() != null){
						getTableTreeDelegate().open(focObject);
					}
				}
			}
		}
	};

	private class HyperLinkButton extends FVButton {

		private FocObject focObject = null;

		private Object objReturned = null;

		public HyperLinkButton(FocObject focObject, Object objReturned) {
			super(objReturned instanceof String ? (String) objReturned : "");
			if(objReturned instanceof Label){
				setCaption(((Label)objReturned).getValue());
			}
			this.focObject = focObject;
			this.objReturned = objReturned;
			init();
		}

		private void init() {
			if(objReturned instanceof String){
				setCaption((String) objReturned);
			}
			setStyleName(BaseTheme.BUTTON_LINK);
		}

		public FocObject getFocObject() {
			return focObject;
		}
	}
	
	
	// LOCALE
	//--------
	public static void getArabicLocale(){
		Locale[] locs = Locale.getAvailableLocales();
		for(int i=0; i<locs.length; i++){
			Globals.logString("Local = "+locs[i].getCountry()+" | "+locs[i].getLanguage());
		}
	}

}
