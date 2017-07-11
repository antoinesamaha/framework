package com.foc.vaadin.gui.components;

import java.text.Format;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FNumField;
import com.foc.formula.FocSimpleFormulaContext;
import com.foc.formula.Formula;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.FocMath;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_ChildCount;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_FocProperty;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_Formula;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_LineNumber;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_Select;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_TreeNodeTitle;
import com.foc.vaadin.gui.components.tableAndTree.FVColumnGenerator;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;

public class FVTableColumn {
  private String            dataPath      = "";
  private String            captionProp   = "";
  private String            editable      = "";
  private String            immediate     = "";
  private String            style         = "";
  private String            name          = "";
  private String            computeLevel  = null;
  private String            caption       = "";
  private String            width         = "";
  private FocXMLAttributes  attributes    = null;
  private ITableTree        tableOrTree   = null;
  private String            footerFormula = null;
  private Format            format        = null;
  private boolean           hasStyleForColor = false;
  private FField            field         = null;
  private int               maxCharacters = 0;
  private boolean           duringExcelExport = false;
  		
  private boolean isHtml                  = false;
  
  private FVColumnGenerator generator     = null;

	public static final String COL_ID_SELECT   = "_SELECT";
	public static final String COL_LINE_NUMBER = "_LINE_NUMBER";
	public static final String COL_CHILD_COUNT = "_COUNT";
  
  public FVTableColumn(ITableTree tableOrTree, FocXMLAttributes attributes){
    this.tableOrTree = tableOrTree;
    setAttributes(attributes);
  }
  
  public FVTableColumn(ITableTree tableOrTree, String dataPath, String captionProp, String editable, String immediate, String style, String name, String caption, String width){
    this.tableOrTree  = tableOrTree;
    this.attributes   = null;
    this.dataPath     = dataPath;    
    this.name         = name;
    this.caption      = caption;
    this.captionProp  = captionProp;
    this.editable     = editable;
    this.immediate    = immediate;
    this.width        = width;
    this.style        = style;
    
    if(dataPath == null){
      dataPath = name;
    }
    if(name == null){
      name = dataPath;
    }
       
    if (caption == null && getTableOrTree() != null && getTableOrTree().getFocDesc() != null) {
      FField field = (FField) getField();
      caption = field != null ? field.getTitle() : "";
    }
  }

  public void dispose(){
  	if(generator != null){
  		generator.dispose();
  		generator = null;
  	}
  	tableOrTree = null;
  	attributes = null;
  	format = null;
  }
  
  public FField getField(){
  	if(field == null){
	  	if(getTableOrTree() != null && getTableOrTree().getFocDesc() != null){
	  		String subDataPath = dataPath;
	  		int index = subDataPath.indexOf('>');
	  		if(index > 0){
	  			subDataPath = subDataPath.substring(0, index);
	  		}
	  		field = (FField) getTableOrTree().getFocDesc().iFocData_getDataByPath(subDataPath);
	  	}
  	}
  	return field;
  }
  
  public String getName() {
    return name;
  }
  
  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }
  
  public String getDataPath() {
    return dataPath;
  }
  
  public String getCaptionProp() {
    return captionProp;
  }

  public String getFooterFormula() {
    return footerFormula;
  }

  public String getCaption() {
    return caption;
  }
  
  public String getImmediate() {
    return immediate;
  }
  
  public String getEditable() {
    return editable;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }
  
  public String getComputeLevel() {
    return computeLevel;
  }

  public void setComputeLevel(String computeLevel) {
    this.computeLevel = computeLevel;
  }

  public FocXMLAttributes getAttributes() {
    return attributes;
  }
  
  public int getWidth() {
    int result = -1;
    if(width != null){
      try{
      	if(width.endsWith("px") && width.length() >= 2) width=width.substring(0, width.length()-2);
        result = Integer.parseInt(width);
      }catch(NumberFormatException e){
      	Globals.logString("Warning ! Table column width "+width+" is not integer nor in px!");
        //Globals.logException(e);
      }
    }

    return result;
  }

  public void setAttributes(FocXMLAttributes attributes) {
    this.attributes = attributes;
    dataPath      = attributes.getValue(FXML.ATT_DATA_PATH);    
    name          = attributes.getValue(FXML.ATT_NAME);
    caption       = attributes.getValue(FXML.ATT_CAPTION);
    captionProp   = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
    editable      = attributes.getValue(FXML.ATT_EDITABLE);
    immediate     = attributes.getValue(FXML.ATT_IMMEDIATE);
    width         = attributes.getValue(FXML.ATT_WIDTH);
    style         = attributes.getValue(FXML.ATT_STYLE);
    footerFormula = attributes.getValue(FXML.ATT_FOOTER_FORMULA);
    String isHtml = attributes.getValue(FXML.ATT_IS_HTML);
    computeLevel  = attributes.getValue(FXML.ATT_VALUE_COMPUTE_LEVEL);
    
    String maxCharactersStr = attributes.getValue(FXML.ATT_MAX_CHARACTERS);
    if(!Utils.isStringEmpty(maxCharactersStr)){
    	maxCharacters = FocMath.parseInteger(maxCharactersStr);
    }
    
    String hasBAckgroundColor = attributes.getValue(FXML.ATT_HAS_BACKGROUND_COLOR);
    if(hasBAckgroundColor != null && (hasBAckgroundColor.toLowerCase().equals("true") || hasBAckgroundColor.toLowerCase().equals("1"))){
    	setHasStyleForColor(true);	
    }
    
    if(isHtml != null && !isHtml.isEmpty() && (isHtml.equalsIgnoreCase("true") || isHtml.equalsIgnoreCase("1"))){
    	setHtml(true);
    }else{
    	setHtml(false);
    }
    
    if(dataPath == null){
      dataPath = name;
    }
    if(name == null){
      name = dataPath;
    }
    
    int indexOfSuper = dataPath.indexOf(IFocData.DATA_PATH_SIGN);
    if(captionProp == null && indexOfSuper >= 0){
    	captionProp = dataPath.substring(indexOfSuper+1, dataPath.length());
    	dataPath = dataPath.substring(0, indexOfSuper);
    }
       
    if (caption == null && dataPath != null && getTableOrTree() != null && getTableOrTree().getFocList() != null) {
      FField field = (FField) getTableOrTree().getFocList().getFocDesc().iFocData_getDataByPath(dataPath);
      caption = field != null ? field.getTitle() : "";
    }

		String formatAtt = attributes.getValue(FXML.ATT_FORMAT);
		if(formatAtt != null && !formatAtt.isEmpty()){
			try{
				boolean groupSeparation = formatAtt.startsWith(",");
				formatAtt = groupSeparation ? formatAtt.substring(1) : formatAtt;
				int nbrDecimals = Integer.valueOf(formatAtt);
				format = FNumField.newNumberFormat(30, nbrDecimals);
			}catch(Exception e){
				Globals.logException(e);
			}
		}
  }
  
  public boolean isColumnEditable(){
    boolean isEditable = false;
    if(getAttributes() != null){
      String value = getAttributes().getValue(FXML.ATT_EDITABLE);
      if(value == null || (!value.isEmpty() && value.equals("true"))){
        isEditable = true;
      }
    } 
    return isEditable;
  }
  
  public boolean isEditable_ExplicitlyInAttribute(){
    boolean isEditable = false;
    if(getAttributes() != null){
      String value = getAttributes().getValue(FXML.ATT_EDITABLE);
      if(value != null && (!value.isEmpty() && value.equals("true"))){
        isEditable = true;
      }
    } 
    return isEditable;
  }

  public boolean isColumnFormula(){
    boolean isFormula = false;
    if(getAttributes() != null){
      String value = getAttributes().getValue(FXML.ATT_FORMULA);
      if(value!=null && !value.isEmpty()){
        isFormula = true;
      }
    } 
    return isFormula;
  }
  
  public void applyColumnSettings(ITableTree tableOrTree){
  	if(tableOrTree instanceof Table){
	  	((Table)tableOrTree).setColumnHeader(name, caption);
	    ((Table)tableOrTree).setColumnWidth(name, getWidth());
	    String expandRatio = getAttributes() != null ? getAttributes().getValue(FXML.ATT_EXPAND_RATIO) : null;
	    if(expandRatio != null){
	    	try{
		    	float expRat = Float.valueOf(expandRatio);
		    	((Table)tableOrTree).setColumnExpandRatio(name, expRat);
	    	}catch(Exception e){
	    		Globals.logException(e);
	    		if(getTableOrTree() != null && getTableOrTree().getTableTreeDelegate() != null){
	    			Globals.logString("Ratio is not a number "+expandRatio+" for column "+name+" in table "+getTableOrTree().getTableTreeDelegate().getTableName());
	    		}else{
	    			Globals.logString("Ratio is not a number "+expandRatio+" for column "+name);
	    		}
	    	}
	    }
  	}
    
    if(tableOrTree.getFocList() != null && tableOrTree.getFocList().getFocDesc() != null && tableOrTree instanceof Table){
      FField field = tableOrTree.getFocList().getFocDesc().getFieldByName(dataPath);
      if(field instanceof FNumField || ((field instanceof FIntField) && !(field instanceof FMultipleChoiceField))){
        ((Table)tableOrTree).setColumnAlignment(dataPath, Align.RIGHT);
        
      }
    }
  }

  /*public void applyColumnSettings(ITableTree tableOrTree){
    ((Table)tableOrTree).setColumnHeader(name, caption);
    ((Table)tableOrTree).setColumnWidth(name, getWidth());
    String expandRatio = getAttributes().getValue(FXML.ATT_EXPAND_RATIO);
    if(expandRatio != null){
    	try{
	    	float expRat = Float.valueOf(expandRatio);
	    	((Table)tableOrTree).setColumnExpandRatio(name, expRat);
    	}catch(Exception e){
    		Globals.logException(e);
    		if(getTableOrTree() != null && getTableOrTree().getTableTreeDelegate() != null){
    			Globals.logString("Ratio is not a number "+expandRatio+" for column "+name+" in table "+getTableOrTree().getTableTreeDelegate().getTableName());
    		}else{
    			Globals.logString("Ratio is not a number "+expandRatio+" for column "+name);
    		}
    	}
    }
    
    if(tableOrTree.getFocList() != null && tableOrTree.getFocList().getFocDesc() != null){
      FField field = tableOrTree.getFocList().getFocDesc().getFieldByName(dataPath);
      if(field instanceof FNumField || ((field instanceof FIntField) && !(field instanceof FMultipleChoiceField))){
        ((Table)tableOrTree).setColumnAlignment(dataPath, Align.RIGHT);
        
      }
    }
  }*/

  public ITableTree getTableOrTree() {
    return tableOrTree;
  }

  public Format getFormat(){
  	return format;
  }
  
  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  private String getFormaulaAttributeIfExists(){
    return getAttributes() != null ? getAttributes().getValue(FXML.ATT_VISIBLE_WHEN) : null;
  }
  
  public boolean isVisible(FocXMLLayout xmlLayout){
    boolean visible = true;
    IFocData focData = xmlLayout != null ? xmlLayout.getFocData() : null;
    if(focData != null){
      String formulaExpression = getFormaulaAttributeIfExists();
      if(formulaExpression != null && !formulaExpression.isEmpty()){
        FocSimpleFormulaContext formulaContext = new FocSimpleFormulaContext(new Formula(formulaExpression));
        Object valueObj = null;
        if(focData instanceof FocObject){
          valueObj = formulaContext.compute((FocObject)focData);
        }else{
          valueObj = formulaContext.compute();
        }
        
        if(valueObj instanceof Boolean){
          visible = ((Boolean)valueObj).booleanValue();
        }else if(valueObj instanceof String){
          visible = ((String)valueObj).toUpperCase().equals("TRUE");                    
        }
      }
    }
    return visible;
  }
  
  public ColumnGenerator getColumnGenerator(boolean create){
  	if(generator == null && create){
 
  		if(getTableOrTree() instanceof FVTreeTable && dataPath.equals(COL_CHILD_COUNT)){
  			generator = new FVColGen_ChildCount(this);
  		}else if(getTableOrTree() instanceof FVTreeTable && dataPath.equals(FXML.COL_TREE_NODE_TITLE)){
  			generator = new FVColGen_TreeNodeTitle(this);
  		}else if(isColumnFormula()){
  			generator = new FVColGen_Formula(this);
  		}else if(dataPath.equals(COL_ID_SELECT)){
  			generator = new FVColGen_Select(this);
			}else if(dataPath.equals(COL_LINE_NUMBER)){
				generator = new FVColGen_LineNumber(this);
			}else{
				generator = new FVColGen_FocProperty(this);
			}
  	}
  	return generator;
  }
  
	public Object computeFormula_ForFocObject(FocObject focObject){
		Object obj = null;
		if(focObject != null){
			String formulaStrg = getAttributes() != null ? getAttributes().getValue(FXML.ATT_FORMULA) : null;
			obj = focObject.computeFormula(formulaStrg);
		}
		return obj;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtmlDisplay) {
		this.isHtml = isHtmlDisplay;
	}

	public boolean isHasStyleForColor() {
		return hasStyleForColor;
	}

	public void setHasStyleForColor(boolean hasAlert) {
		this.hasStyleForColor = hasAlert;
	}

	public int getMaxCharacters() {
		return isDuringExcelExport() ? 0 : maxCharacters;
	}

	public void setMaxCharacters(int maxCharacters) {
		this.maxCharacters = maxCharacters;
	}

	public boolean isDuringExcelExport() {
		return duringExcelExport;
	}

	public void setDuringExcelExport(boolean duringExcelExport) {
		this.duringExcelExport = duringExcelExport;
	}
}