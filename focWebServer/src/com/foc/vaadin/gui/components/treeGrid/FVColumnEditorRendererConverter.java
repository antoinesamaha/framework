package com.foc.vaadin.gui.components.treeGrid;

import java.util.Iterator;
import java.util.Locale;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FDateTimeField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.desc.field.FNumField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.renderers.TextRenderer;

public class FVColumnEditorRendererConverter {

	private FVTableColumn tableColumn = null;
	private Column gridColumn = null;
	private Field editorField = null;
	private Converter converter = null;
	private Renderer renderer = null;

	public FVColumnEditorRendererConverter(FVTableColumn tableColumn, Column gridColumn) {
		this.tableColumn = tableColumn;
		this.gridColumn = gridColumn;
		createComponents();
	}

	public void dispose() {
		editorField = null;
		converter = null;
		renderer = null;

		tableColumn = null;
		gridColumn = null;
	}

	public Field getEditorField() {
		return editorField;
	}

	public void setEditorField(Field editableField) {
		this.editorField = editableField;
	}

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public FVTableColumn getTableColumn() {
		return tableColumn;
	}

	public Column getGridColumn() {
		return gridColumn;
	}

	@SuppressWarnings("serial")
	public void createComponents() {
		if(getTableColumn() != null && getGridColumn() != null){
			FField focField = getTableColumn().getField();
			if(focField != null){
				// -----------------------------------------------------------
				// FDateField
				// -----------------------------------------------------------
				/*
				if(focField instanceof FDateField || focField instanceof FDateTimeField){
					DateField dateField = new DateField();

					FVAbstractConverter<String, java.sql.Date> converter = new FVAbstractConverter<String, java.sql.Date>(this) {
						public java.sql.Date convertToModel(String value, Class<? extends java.sql.Date> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
							java.sql.Date date = null;
							try{
								date = new java.sql.Date((FDate.getDateFormat().parse(value)).getTime());
							}catch (Exception e){
								date = null;
							}
							return date;
						}

						public String convertToPresentation(java.sql.Date value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
							String stringValue = "";
							try{
								if(value == null){
									stringValue = FDate.convertDateToDisplayString(value);
								}
							}catch (Exception e){
								stringValue = "";
							}
							return stringValue;
						}

						public Class<java.sql.Date> getModelType() {
							return java.sql.Date.class;
						}

						public Class<String> getPresentationType() {
							return String.class;
						}
					};
					getGridColumn().setConverter(converter);
					 //dateField.setConverter((Converter)converter);
					getGridColumn().setRenderer(new DateRenderer(FDate.getDateFormat()));

					// -----------------------------------------------------------
					// FMultipleChoiceField
					// -----------------------------------------------------------
				}else*/ if(focField instanceof FMultipleChoiceField){

					// Editor
					// ------
					ComboBox combo = new ComboBox();
					FMultipleChoiceField mFld = (FMultipleChoiceField) focField;
					Iterator<FMultipleChoiceItem> iter = mFld.getChoiceIterator();
					while (iter != null && iter.hasNext()){
						FMultipleChoiceItem item = (FMultipleChoiceItem) iter.next();
						combo.addItem(item.getTitle());
					}
					getGridColumn().setEditorField(combo);

					FVAbstractConverter<String, FMultipleChoiceItem> converter = new FVAbstractConverter<String, FMultipleChoiceItem>(this) {
						public FMultipleChoiceItem convertToModel(String value, Class<? extends FMultipleChoiceItem> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
							FMultipleChoiceItem foundItem = null;

							FMultipleChoiceField mFld = (FMultipleChoiceField) getTableColumn().getField();
							Iterator iter = mFld.getChoiceIterator();
							while (iter != null && iter.hasNext()){
								FMultipleChoiceItem item = (FMultipleChoiceItem) iter.next();
								if(item.getTitle().equals(value)){
									foundItem = item;
								}
							}

							return foundItem;
						}

						public String convertToPresentation(FMultipleChoiceItem value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
							return value != null ? value.getTitle() : null;
						}

						public Class<FMultipleChoiceItem> getModelType() {
							return FMultipleChoiceItem.class;
						}

						public Class<String> getPresentationType() {
							return String.class;
						}
					};
					getGridColumn().setConverter(converter);
					combo.setConverter((Converter) converter);
					getGridColumn().setRenderer(new TextRenderer(), converter);

					// -----------------------------------------------------------
					// FObjectField
					// -----------------------------------------------------------
				}else if(focField instanceof FObjectField){
					// Editor
					// ------
					FObjectField oFld = (FObjectField) focField;
					FVObjectComboBox combo = new FVObjectComboBox(oFld, getTableColumn().getCaptionProp());
					getGridColumn().setEditorField(combo);

					FVAbstractConverter<String, Integer> converter = new FVAbstractConverter<String, Integer>(this) {

						private FocList getFocList() {
							FObjectField oFld = (FObjectField) getTableColumn().getField();
							FocList list = oFld != null ? oFld.getSelectionList() : null;
							return list;
						}

						public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
							Integer foundItem = null;

							FocList list = getFocList();
							if(list != null){
								for(int i = 0; i < list.size(); i++){
									FocObject fObj = list.getFocObject(i);
									FProperty prop = fObj.getFocPropertyForPath(getTableColumn().getCaptionProp());
									if(prop != null && prop.getValue() == value){
										foundItem = fObj.getReferenceInt();
									}
								}
							}
							return foundItem;
						}

						public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
							String stirngDisplay = "";
							if(value == null){
								if(getTableColumn() != null && getTableColumn().getField() != null && ((FObjectField) getTableColumn().getField()).getNullValueDisplayString() != null){
									stirngDisplay = ((FObjectField) getTableColumn().getField()).getNullValueDisplayString();
								}
							}else{
								FocList list = getFocList();
								FocObject foudnObject = null;
								try{
									foudnObject = list != null ? list.searchByReference(value) : null;
								}catch (Exception e){
									foudnObject = list != null ? list.searchByReference(value) : null;
									Globals.logException(e);
								}

								String captionPath = getTableColumn().getCaptionProp();
								if(captionPath == null || captionPath.isEmpty()){
									FObjectField objField = (getTableColumn() != null) ? (FObjectField) getTableColumn().getField() : null;
									FocDesc focDesc = objField != null ? objField.getFocDesc() : null;

									if(focDesc != null){
										if(focDesc.getFieldByID(FField.FLD_CODE) != null){
											captionPath = FField.FNAME_CODE;
										}else if(focDesc.getFieldByID(FField.FLD_NAME) != null){
											captionPath = FField.FNAME_NAME;
										}else if(focDesc.getFieldByID(FField.FLD_DESCRIPTION) != null){
											captionPath = FField.FNAME_DESCRIPTION;
										}
									}
								}

								FProperty prop = foudnObject != null ? foudnObject.getFocPropertyForPath(captionPath) : null;
								stirngDisplay = prop != null ? prop.getString() : "";
							}
							return stirngDisplay;
						}

						public Class<Integer> getModelType() {
							return Integer.class;
						}

						public Class<String> getPresentationType() {
							return String.class;
						}
					};
					getGridColumn().setConverter(converter);
					// combo.setConverter((Converter)converter);
					getGridColumn().setRenderer(new TextRenderer(), converter);
				}else if(focField instanceof FNumField){
					FNumField numField = (FNumField) focField;
					if(numField != null && numField.getFormat() != null){
						TextField textField = new FVEditorField_Numeric(getTableColumn()); //new TextField();
						getGridColumn().setEditorField(textField);
						
						NumberRenderer numberRenderer = new NumberRenderer(numField.getFormat());
						getGridColumn().setRenderer(numberRenderer);
					}
				}else if(focField instanceof FBoolField){
					FBoolField bField = (FBoolField) focField;

					if(bField != null){
						CheckBox cb = new CheckBox();
						cb.setImmediate(true);
						getGridColumn().setEditorField(cb);
						getGridColumn().setRenderer(new ImageRenderer(),
								new Converter<Resource, Boolean>() {
									@Override
					        public Boolean convertToModel(Resource value,
				            Class<? extends Boolean> targetType, Locale l)
				            throws Converter.ConversionException {
				            return false;
					        }
		
									@Override
					        public Resource convertToPresentation(Boolean value, Class<? extends Resource> targetType, Locale l) throws Converter.ConversionException {
										Resource res = null;
										if(value){
											res = FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_APPLY);
										}else{
											res = FVIconFactory.getInstance().getFVIcon_Small("cancel-icon.png");
										}
				            return res;
					        }
		
									@Override
					        public Class<Boolean> getModelType() {
				            return Boolean.class;
					        }
		
									@Override
					        public Class<Resource> getPresentationType() {
				            return Resource.class;
					        }
								});
/*
						cb.setConverter(new Converter<Boolean, Boolean>(){

							@Override
							public Boolean convertToModel(Boolean value, Class<? extends Boolean> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
								return value;
							}

							@Override
							public Boolean convertToPresentation(Boolean value, Class<? extends Boolean> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
								return value;
							}

							@Override
							public Class<Boolean> getModelType() {
								return Boolean.class;
							}

							@Override
							public Class<Boolean> getPresentationType() {
								return Boolean.class;
							}
							
						});
						*/
					}
				}else if(focField instanceof FDateTimeField){
//					DateField dateField = new DateField();
//					getGridColumn().setEditorField(dateField);
					
//					dateField.setConverter(new Converter<Date, Date>(){
//
//						@Override
//						public Date convertToModel(Date value, Class<? extends Date> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
//							return value;
//						}
//
//						@Override
//						public Date convertToPresentation(Date value, Class<? extends Date> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
//							// TODO Auto-generated method stub
//							return value;
//						}
//
//						@Override
//						public Class<Date> getModelType() {
//							// TODO Auto-generated method stub
//							return Date.class;
//						}
//
//						@Override
//						public Class<Date> getPresentationType() {
//							// TODO Auto-generated method stub
//							return Date.class;
//						}
//						
//					});
				}
			}
		}
	}
}
