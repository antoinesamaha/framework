package com.fab.codeWriter;

public class CodeWriter_Service extends CodeWriter {

	public CodeWriter_Service(CodeWriterSet set){
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
		return "Service";
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
		
		intWriter.addImport("com.google.gwt.user.client.rpc.RemoteService");
		intWriter.addImport(extWriter.getPackageName()+"."+webCltClassName);
		intWriter.addImport(extWriter.getPackageName()+"."+getClassName_GwfList());
		
		intWriter.printCore("public interface "+intWriter.getClassName()+" extends RemoteService {\n\n");

	  if(getTblDef().isSingleInstance()){
			intWriter.printCore("  "+webCltClassName+" get"+focObjClassName+"() throws IllegalArgumentException;\n");		
	  }else{
	  	intWriter.printCore("  "+getClassName_GwfList()+" get"+getClassName_GwfList()+"();\n");
			intWriter.printCore("  "+webCltClassName+" get"+focObjClassName+"(int ref) throws IllegalArgumentException;\n");		
			intWriter.printCore("  boolean delete"+focObjClassName+"(int ref);\n");
	  }
		intWriter.printCore("  int submit"+focObjClassName+"("+webCltClassName+" cltObj);\n");
		
		//End of the class
		intWriter.printCore("}\n");
		intWriter.compile();
		
		//  External
		//  --------
		
		extWriter.addImport("com.google.gwt.user.client.rpc.RemoteServiceRelativePath");
		extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());

		extWriter.printCore("@RemoteServiceRelativePath(\""+ getServiceName() +"\")\n");
		extWriter.printCore("public interface "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");
		
//		extWriter.printCore("  public "+extWriter.getClassName()+"(FocConstructor constr){\n");
//		extWriter.printCore("    super(constr);\n");
//		extWriter.printCore("  }\n");
		
		extWriter.printCore("}");
		extWriter.compile();
		
		closeFiles();
	}
}
