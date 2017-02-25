package com.fab;

import com.fab.model.table.UserDefined_WFLog_Desc;
import com.foc.IFocDescDeclaration;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;

public class DB_WFLog_FocDescDeclaration implements IFocDescDeclaration{
	private DBFocDescDeclaration workflowDescDeclaration = null;
	private FocDesc         focDesc            = null ;
	private boolean         gettingFocDesc     = false;
	
	public DB_WFLog_FocDescDeclaration(DBFocDescDeclaration workflowDescDeclaration){
		this.workflowDescDeclaration = workflowDescDeclaration;
	}
	
	public void dispose(){
		this.focDesc         = null;
		this.workflowDescDeclaration = null;
	}
	
	public boolean isGettingFocDesc(){
		return gettingFocDesc;
	}

	public void setGettingFocDesc(boolean getting){
		gettingFocDesc = getting;
	}
	
	public FocDesc getFocDescription() {
		if(focDesc == null && !isGettingFocDesc()){
			setGettingFocDesc(true);
			FocDesc transactionFocDesc = workflowDescDeclaration.getFocDescription();
			if(transactionFocDesc != null){
				focDesc = new UserDefined_WFLog_Desc((IWorkflowDesc)transactionFocDesc, transactionFocDesc.getStorageName());
			}
			setGettingFocDesc(false);
		}
		return focDesc;
	}
	
	/*
	public static IFocDescDeclaration getDbFocDescDeclaration(TableDefinition tableDefinition){
		IFocDescDeclaration declaration = null;
		if(tableDefinition != null){
			String name = tableDefinition.getName();
			declaration = (IFocDescDeclaration)Globals.getApp().getIFocDescDeclarationByName(name);
			if(declaration == null){
				//if(!ConfigInfo.isForDevelopment()){
					declaration = new DB_WFLog_FocDescDeclaration(FabModule.getInstance(), tableDefinition);
					Globals.getApp().putIFocDescDeclaration(name, declaration);
				//}
			}
		}
		return declaration;
	}
	*/
	
	public int getPriority() {
		return IFocDescDeclaration.PRIORITY_SECOND;
	}

	@Override
	public FocModule getFocModule() {
		return workflowDescDeclaration != null ? workflowDescDeclaration.getFocModule() : null;
	}

	@Override
	public String getName() {
		return (getFocDescription() != null) ? getFocDescription().getStorageName() : null;
	}
}
