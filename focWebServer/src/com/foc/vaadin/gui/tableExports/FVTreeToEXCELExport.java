/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
