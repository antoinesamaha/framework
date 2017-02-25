package com.foc.file.export;

import java.io.PrintStream;
import java.util.ArrayList;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.desc.field.FFieldPath;
import com.foc.list.FocList;
import com.foc.property.FProperty;

public class FileExporter {
	
	private FocList                list           = null;
	private ArrayList<ExportField> fieldSelection = null;
	private String                 fileName       = null;
	private PrintStream            stream         = null;
	
	public FileExporter(FocList list, String fileName){
		fieldSelection = new ArrayList<ExportField>();
		this.fileName  = fileName;
		this.list      = list;
	}
	
	public void dispose(){
		list           = null;
		fieldSelection = null;
	}
	
	public void addFieldPath(FFieldPath fieldPath, ExportStringAdaptor adaptor){
		ExportField expField = new ExportField(fieldPath, adaptor);
		fieldSelection.add(expField);
	}
	
	public void addField(int field){
		addFieldPath(FFieldPath.newFieldPath(field), null);
	}

	public void addFieldPath(int field1, int field2){
		addFieldPath(FFieldPath.newFieldPath(field1, field2), null);
	}

	public void addFieldPath(int field1, int field2, int field3){
		addFieldPath(FFieldPath.newFieldPath(field1, field2, field3), null);
	}

	public boolean export(boolean withPopups){
		boolean error = false;
		try{
			stream = new PrintStream(fileName);
		}catch(Exception e){
			error = true;
			if(withPopups){
				Globals.getDisplayManager().popupMessage("Could not open file "+fileName);
			}
			Globals.logException(e);
		}
		if(!error){
			for(int i=0; i<list.size(); i++){
				FocObject objToExport = list.getFocObject(i);
				StringBuffer line = new StringBuffer();
				for(int f=0; f<fieldSelection.size(); f++){
					ExportField field = fieldSelection.get(f);
					if(field != null){
						String stringToExport = field.getString(objToExport);
						line.append(stringToExport);
						line.append(",");
					}
				}
				stream.println(line.toString());
				stream.flush();
			}
			stream.close();
			stream = null;
		}//Mail,Code,CO2,PROJECT
		
		return error;
	}
	
	public class ExportField {
		FFieldPath          fieldPath     = null;
		ExportStringAdaptor stringAdaptor = null;
		
		public ExportField(FFieldPath fieldPath, ExportStringAdaptor stringAdaptor){
			this.stringAdaptor = stringAdaptor;
			this.fieldPath     = fieldPath    ;
		}
		
		public void dispose(){
			if(fieldPath != null){
				fieldPath.dispose();
				fieldPath = null;
			}
			if(stringAdaptor != null){
				stringAdaptor.dispose();
				stringAdaptor = null;
			}
		}
		
		public String getString(FocObject obj){
			String str = "";
			if(fieldPath != null){
				FProperty prop = fieldPath.getPropertyFromObject(obj);
				if(prop != null){
					str = prop.getString();
					if(stringAdaptor != null){
						str = stringAdaptor.convertString(str);
					}
				}
			}
			return str != null ? str : "";
		}
	}
}
