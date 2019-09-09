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
package com.foc.web.modules.admin;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.MenuAccessRightWeb;
import com.foc.admin.MenuAccessRightWebDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.FocMenuItemNode;
import com.foc.tree.TreeScanner;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.menuTree.FVMenuTree;

@SuppressWarnings("serial")
public class FocMenuItem_GroupMenuRight_Tree extends FVMenuTree {

	private FocGroup group = null;
	
	public FocMenuItem_GroupMenuRight_Tree() {
		super();
		setTreeType(TYPE_NORMAL);
	}
	
	public void dispose(){
		super.dispose();
		group = null;
	}
	
	@Override
	protected FocGroup getFocGroup(){
		return group;
	}

	public void setFocGroup(FocGroup group) {
		this.group = group;
	}
	
	public FocList getMenuAccessRightWebList(){
		return getFocGroup() != null ? getFocGroup().getMenuAccessRightWebList() : null;
	}
	
	public MenuAccessRightWeb findMenuAccessRightWeb(String code){
		MenuAccessRightWeb focObj = null;
		FocList list = getMenuAccessRightWebList();
		if(list != null){
			focObj = (MenuAccessRightWeb) list.searchByPropertyStringValue(MenuAccessRightWebDesc.FLD_MENU_CODE, code);
		}
		return focObj;
	}
	 
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		final ArrayList<MenuAccessRightWeb> accessRightWebTobeDeleted = new ArrayList<MenuAccessRightWeb>();
		if(!error){
			for(int i = 0;i<getMenuAccessRightWebList().size();i++){
				accessRightWebTobeDeleted.add((MenuAccessRightWeb) getMenuAccessRightWebList().getFocObject(i));
			}
			getMenuTree().scan(new TreeScanner<FocMenuItemNode>() {

				@Override
				public boolean beforChildren(FocMenuItemNode node) {
					FocMenuItem item = node.getObject();
					if(item != null && item.getHasAccess() == false){
						MenuAccessRightWeb foundRightWeb = findMenuAccessRightWeb(item.getCode());
						if(foundRightWeb == null){
							foundRightWeb = (MenuAccessRightWeb) getMenuAccessRightWebList().newEmptyItem();
							foundRightWeb.setCode(item.getCode());
							foundRightWeb.setRight(MenuAccessRightWebDesc.ALLOW_HIDE);
							getMenuAccessRightWebList().add(foundRightWeb);
						}else{
							accessRightWebTobeDeleted.remove(foundRightWeb);
						}
					}
					return true;
				}

				@Override
				public void afterChildren(FocMenuItemNode node) {
				}
			});
			
			for(int i = 0;i<accessRightWebTobeDeleted.size();i++){
				accessRightWebTobeDeleted.get(i).setDeleted(true);
			}

			getMenuAccessRightWebList().validate(true);
			//Start 
			//Scan the tree
			//For each node with Hide, Check if the line exists in the getMenuAccesRighWebList if not create the line and make sure the setRight(HIDE)
			//For lines in the getMenuAccesRighWebList that are not visited by the tree, delete them from the getMenuAccesRighWebList 
		}
		return error;
	}
	
	@Override
	protected void fillMainTree() {
		super.fillMainTree();
		
		getMenuTree().scan(new TreeScanner<FocMenuItemNode>() {

			@Override
			public boolean beforChildren(FocMenuItemNode node) {
				FocMenuItem item = node.getObject();
				if(getMenuTree().getFocList() !=null && item != null){
					MenuAccessRightWeb menuAccessGroup = findMenuAccessRightWeb(item.getCode());
					if(menuAccessGroup != null){
						int rightLevel = menuAccessGroup.getRight();
						if(rightLevel == MenuAccessRightWebDesc.ALLOW_HIDE){
							item.setHasAccess(false);
						}
					}
				}
				return true;
			}

			@Override
			public void afterChildren(FocMenuItemNode node) {
			}
			
		});
	}
}
