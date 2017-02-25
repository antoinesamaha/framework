package com.foc.gridView;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.tree.FNode;
import com.foc.tree.TreeScanner;

public class GridLineDesc extends FocDesc implements TreeScanner<FNode>{

	public final static int FLD_CODE  = 1;
	public final static int FLD_TITLE = 2;
	
	public final static String NON_REFLECTING_PREFIX = "NON_REFLECTING";
	
	private int firstFieldID         = 10000;
	private int nextFieldID          = 10000;
	
	private GridDefinition gridDefinition = null;
	
	public static final String DB_TABLE_NAME = "TREE_GRID_LINE";
	
  public GridLineDesc(GridDefinition gridDefinition){
    super(GridLine.class, FocDesc.NOT_DB_RESIDENT, DB_TABLE_NAME, false);
    this.gridDefinition = gridDefinition;

    addReferenceField();
    setWithObjectTree();
    
    FStringField codeFld  = new FStringField("CODE", "Code", FLD_CODE, false, 20 );
    addField(codeFld);
    
    FStringField titleFld = new FStringField("TITLE", "title", FLD_TITLE, false, 100);
    addField(titleFld);
    
    gridDefinition.getTree().scan(this);
  }

  public int getFirstFieldID(){
  	return firstFieldID;
  }

  public int getLastFieldID(){
  	return nextFieldID - 1;
  }

  public FField getNonReflectingField(String originalFieldName){
  	return getFieldByName(NON_REFLECTING_PREFIX + GridDefinition.FIELD_NAME_SEPARATOR + originalFieldName);
  }
  
  public String composeFieldName(String prefix, String suffix){
//  	return prefix + GridDefinition.FIELD_NAME_SEPARATOR + suffix;
  	suffix = suffix.replace('.', '#');
  	return prefix + "#" + suffix;
  }

  public String extractNodeName(String fieldName){
		int    indexOfSeparator = fieldName.indexOf(GridDefinition.FIELD_NAME_SEPARATOR);
		String nodeName         = fieldName.substring(0, indexOfSeparator);
		return nodeName;
  }
  
  public String extractOriginalFieldName(String fieldName){
		int    indexOfSeparator = fieldName.indexOf(GridDefinition.FIELD_NAME_SEPARATOR);
		String subFieldName     = fieldName.substring(indexOfSeparator+1, fieldName.length());
		return subFieldName;
  }
  
  protected int addSelectedFields(String prefix, int nextFieldIndex){
  	int fieldsCount = gridDefinition.getFieldCount() != 0 ? gridDefinition.getFieldCount() : gridDefinition.getFieldPathCount();
  	
		FocDesc focDesc = gridDefinition.getOriginalFocDesc();
		for(int i=0; i<fieldsCount; i++){
			Object fieldID = gridDefinition.getFieldAt_Object(i);
			FField srcField = null;
			String newFieldName = null;
			if(fieldID instanceof Integer){
				srcField = focDesc.getFieldByID((Integer) fieldID);
				newFieldName = composeFieldName(prefix, srcField.getName());
			}else if(fieldID instanceof String){
				srcField = (FField) focDesc.iFocData_getDataByPath((String) fieldID);
				newFieldName = composeFieldName(prefix, (String) fieldID);
			}
			if(getFieldByName(newFieldName) == null){
				try{
					if(srcField != null){
						FField tarField = (FField) srcField.clone();
						tarField.setName(newFieldName);
						tarField.setId(nextFieldIndex++);
						addField(tarField);
					}
				}catch(Exception e){
					Globals.logException(e);
				}
			}
		}
		
		return nextFieldIndex;
  }
  
  /*protected int addSelectedFields(String prefix, int nextFieldIndex){
		FocDesc focDesc = gridDefinition.getOriginalFocDesc();
		for(int i=0; i<gridDefinition.getFieldCount(); i++){
			int fieldID = gridDefinition.getFieldAt(i);
			
			FField srcField = focDesc.getFieldByID(fieldID);
			String newFieldName = composeFieldName(prefix, srcField.getName());
			
			if(getFieldByName(newFieldName) == null){
				try{
					FField tarField = (FField) srcField.clone();
					tarField.setName(newFieldName);
					tarField.setId(nextFieldIndex++);
					addField(tarField);
				}catch(Exception e){
					Globals.logException(e);
				}
			}
		}

		return nextFieldIndex;
  }*/

  // -----------------------------------
  // SCANNER IMPLEMENTATION
  // -----------------------------------  
	public void afterChildren(FNode node){
	}

	public boolean beforChildren(FNode node){
		if(node.getNodeDepth() == gridDefinition.getLevel()){
			nextFieldID = addSelectedFields(node.getTitle(), nextFieldID);
		}
		return true;
	}
}
