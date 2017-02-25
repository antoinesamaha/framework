package com.foc.vaadin.gui.tableExports;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.tree.FNode;
import com.foc.tree.TreeScanner;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.web.dataModel.FocTreeWrapper;

public class FVTreeToEXCELExport extends FVTableTreeEXCELExport {
	public FVTreeToEXCELExport(TableTreeDelegate paramTableTreeDelegate) {
		super(paramTableTreeDelegate);
	}

	protected void setTableTreeColumnsTitle() {
		try{
			addCellValue("Level");
		}catch (Exception localException){
			Globals.logException(localException);
		}
		super.setTableTreeColumnsTitle();
	}

	public void scan() {
		FocTreeWrapper localFocTreeWrapper = getFocTreeWrapper();
		if((localFocTreeWrapper != null) && (localFocTreeWrapper.getFTree() != null)){
			localFocTreeWrapper.getFTree().scanVisible(new TreeScanner<FNode<?, ?>>() {
				int level = 1;

				public boolean beforChildren(FNode<?, ?> paramAnonymousFNode) {
					ArrayList localArrayList = FVTreeToEXCELExport.this.getVisibleColumns();
					if(localArrayList != null){
						FocObject localFocObject = (FocObject) paramAnonymousFNode.getObject();
						if(localFocObject != null){
							FVTreeToEXCELExport.this.addCellValue(Integer.valueOf(this.level));
							for(int i = 0; i < FVTreeToEXCELExport.this.getColumnCount(); i++){
								FVTableColumn localFVTableColumn = FVTreeToEXCELExport.this.getColumnAt(i);
								if(localFVTableColumn != null){
									localFVTableColumn.setDuringExcelExport(true);
									String str = FVTreeToEXCELExport.this.getPropertyStringValue(localFocObject, localFVTableColumn);
									localFVTableColumn.setDuringExcelExport(false);
									if(i == 0){
										FVTreeToEXCELExport.this.addCellValue(str, 2 * this.level);
									}else{
										FVTreeToEXCELExport.this.addCellValue(str);
									}
								}
							}
							FVTreeToEXCELExport.this.addNewLine();
							this.level += 1;
						}
					}
					return true;
				}

				public void afterChildren(FNode<?, ?> paramAnonymousFNode) {
					FocObject localFocObject = (FocObject) paramAnonymousFNode.getObject();
					if(localFocObject != null){
						this.level -= 1;
					}
				}
			});
		}
	}
}