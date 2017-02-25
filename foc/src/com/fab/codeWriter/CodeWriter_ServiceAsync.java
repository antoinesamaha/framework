package com.fab.codeWriter;

public class CodeWriter_ServiceAsync extends CodeWriter {

	public CodeWriter_ServiceAsync(CodeWriterSet set){
		super(set);
	}

	@Override
	public boolean hasInternalFile() {
		return true;
	}

	@Override
	public boolean hasExternalFile() {
		return true;
	}

	@Override
	public String getFileSuffix() {
		return "ServiceAsync";
	}

	@Override
	public boolean isServerSide() {
		return false;
	}

	@Override
	public void generateCode() {
		initFiles();
		
		CodeWriter_OneFile intWriter = getInternalFileWriter(); 
		CodeWriter_OneFile extWriter = getExternalFileWriter();
		
		String webCltClassName = getClassName_GwfObject(); 
		String focObjClassName = getClassName_FocObject();
		
		//  Internal
		//  --------
		intWriter.addImport("com.google.gwt.user.client.rpc.AsyncCallback");
		intWriter.addImport(extWriter.getPackageName()+"."+webCltClassName);		
		intWriter.addImport(extWriter.getPackageName()+"."+getClassName_GwfList());
		
		intWriter.printCore("public interface "+intWriter.getClassName()+" {\n\n");
		
	  if(getTblDef().isSingleInstance()){
			intWriter.printCore("  void get"+focObjClassName+"(AsyncCallback<"+webCltClassName+"> callback) throws IllegalArgumentException;\n");		
	  }else{
			intWriter.printCore("  void get"+getClassName_GwfList()+"(AsyncCallback<"+getClassName_GwfList()+"> callback);\n");
			intWriter.printCore("  void get"+focObjClassName+"(int input, AsyncCallback<"+webCltClassName+"> callback) throws IllegalArgumentException;\n");		
			intWriter.printCore("  void delete"+focObjClassName+"(int input, AsyncCallback<Boolean> callback);\n");
	  }
		intWriter.printCore("  void submit"+focObjClassName+"("+webCltClassName+" clt, AsyncCallback<Integer> callback);\n");
		
		//End of the class
		intWriter.printCore("}\n");
		intWriter.compile();
		
		//  External
		//  --------
		
		//extWriter.addImport("b01.foc.desc.FocConstructor");
		extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());
		
		extWriter.printCore("public interface "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");
		
//		extWriter.printCore("  public "+extWriter.getClassName()+"(FocConstructor constr){\n");
//		extWriter.printCore("    super(constr);\n");
//		extWriter.printCore("  }\n");
		
		extWriter.printCore("}");
		extWriter.compile();
		
		closeFiles();
	}
}
