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
package com.foc.vaadin.gui;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;

public class FVIconFactory {
  
	private static String THEME_DIR = "focVaadinTheme";
	
  private static FVIconFactory instance = null;
  
  public final static String ICON_DOWN_1      = "arrow-down-1.png"; //back.png
  public final static String ICON_EMPTY       = "empty.png";
  public final static String ICON_MENU        = "menu.png";
  public final static String ICON_NAVIGATION  = "navigation.png";
  public final static String ICON_APPLY       = "Actions-dialog-ok-icon.png";//"apply.png" ;
  public final static String ICON_CANCEL      = "stop-icon.png";//"cancel.png";
  public final static String ICON_CANCEL_16   = "../runo/icons/16/cancel.png";
  public final static String ICON_DELETE      = "minus-icon.png";//"delete.png";
  public final static String ICON_PRINT       = "print.png" ;//"printer.png" ;
  public final static String ICON_PRINT_AND_EXIT= "printAndExit.png";
  public final static String ICON_NOTE        = "note.png"  ;
  public final static String ICON_FULL_SCREEN  = "window-full-screen-icon.png";//"full_screen.png"  ;
  public final static String ICON_SMALL_SCREEN = "window-no-full-screen-icon.png";//"small_screen.png"  ;
  public final static String ICON_HOME        = "home-icon.png";//"home.png"  ;
  public final static String ICON_SETTINGS    = "preferences-icon.png";//"settings.png"  ;
  public final static String ICON_PENDING_SIGNATURE = "Pen-icon.png";//"settings.png"  ;  
  public final static String ICON_EDIT        = "edit-file-icon.png";//"write.png"  ;
  public final static String ICON_EDIT2       = "edit2.png" ;
  public final static String ICON_OK          = "../runo/icons/32/ok.png";
  public final static String ICON_OK_16       = "../runo/icons/16/ok.png";
  public final static String ICON_SAVE        = "disk-save-yellow-icon.png";//"save.png"  ;
  public final static String ICON_ADD         = "math-add-icon.png";//"add.png"   ;
  public final static String ICON_BACK        = "../runo/icons/32/arrow-left.png"; //back.png
  public final static String ICON_DOWN        = "../runo/icons/32/arrow-down.png"; //back.png
  public final static String ICON_TRASH       = "trash.png" ;
  public final static String ICON_TRASH_SMALL = "trash_small.png" ;
  public final static String ICON_ATTACH      = "image-attach.png" ;
  //public final static String ICON_EMAIL       = "../runo/icons/32/email.png";
  public final static String ICON_EMAIL       = "mail-yellow-icon.png";//"email.png";
  public final static String ICON_EQUALS      = "equals.GIF";
  public final static String ICON_INSERT      = "add_att.gif";
  public final static String ICON_MAGNIFIER   = "search.png";//"magnifier.png";
  public final static String ICON_FORMULA     = "formula.png";
  public final static String ICON_REFRESH     = "refresh.png";
  public final static String ICON_TIPS        = "question-icon.png";
  public final static String ICON_ADOBE       = "adobe.png";
  public final static String ICON_EXPORT_TO_EXCEL = "exportToExcel.png";
  public final static String ICON_TRASH_BLACK  = "trash_black.png";
  public final static String ICON_TRASH_WHITE  = "trash_white.png";
  public final static String ICON_DUPLICATE_16 = "duplicate.png";
  public final static String ICON_PALETTE      = "palette.png";

  public final static String ICON_EXCEL    = "MS-Excel-Metro-icon.png";
  public final static String ICON_PPT      = "MS-PowerPoint-Metro-icon.png";
  public final static String ICON_WORD     = "MS-Word-Metro-icon.png";
  public final static String ICON_INFOPATH = "MS-InfoPath-icon.png";
  public final static String ICON_ONE_NOTE = "MS-Onenote-icon.png";
  public final static String ICON_DOWNLOAD = "Download-2-icon.png";
  
  public final static String ICON_PROPOSAL  = "proposal.png";
  public final static String ICON_APPROVED  = "approved.png";
  public final static String ICON_CLOSED    = "closed.png";
  public final static String ICON_CANCELLED = "cancelled.png";

  /*
  public final static String ICON_EXCEL_16X16    = "Office-Apps-Excel-alt-1-Metro-icon(16x16).png";
  public final static String ICON_PPT_16X16      = "Office-Apps-PowerPoint-alt-1-Metro-icon(16x16).png";
  public final static String ICON_WORD_16X16     = "Office-Apps-Word-alt-2-Metro-icon(16x16).png";
  public final static String ICON_INFOPATH_16X16 = "Microsoft-Office-InfoPath-icon(16x16).png";
  public final static String ICON_ONE_NOTE_16X16 = "Microsoft-Office-Onenote-icon(16x16).png";
  public final static String ICON_DOWNLOAD_16X16 = "Download-2-icon(16x16).png";
  
  public final static String ICON_EXCEL_24X24    = "Office-Apps-Excel-alt-1-Metro-icon(24x24).png";
  public final static String ICON_PPT_24X24      = "Office-Apps-PowerPoint-alt-1-Metro-icon(24x24).png";
  public final static String ICON_WORD_24X24     = "Office-Apps-Word-alt-2-Metro-icon(24x24).png";
  public final static String ICON_INFOPATH_24X24 = "Microsoft-Office-InfoPath-icon(24x24).png";
  public final static String ICON_ONE_NOTE_24X24 = "Microsoft-Office-Onenote-icon(24x24).png";
  public final static String ICON_DOWNLOAD       = "document-arrow-down-icon.png";
  public final static String ICON_UPLOAD         = "document-arrow-up-icon.png";
  
  public final static String ICON_EXCEL_32X32    = "Office-Apps-Excel-alt-1-Metro-icon(32x32).png";
  public final static String ICON_PPT_32X32      = "Office-Apps-PowerPoint-alt-1-Metro-icon(32x32).png";
  public final static String ICON_WORD_32X32     = "Office-Apps-Word-alt-2-Metro-icon(32x32).png";
  public final static String ICON_INFOPATH_32X32 = "Microsoft-Office-InfoPath-icon(32x32).png";
  public final static String ICON_ONE_NOTE_32X32 = "Microsoft-Office-Onenote-icon(32x32).png";
  public final static String ICON_DOWNLOAD_32X32 = "Download-2-icon(32x32).png";
  
  public final static String ICON_EXCEL_48X48    = "Office-Apps-Excel-alt-1-Metro-icon(48x48).png";
  public final static String ICON_PPT_48X48      = "Office-Apps-PowerPoint-alt-1-Metro-icon(48x48).png";
  public final static String ICON_WORD_48X48     = "Office-Apps-Word-alt-2-Metro-icon(48x48).png";
  public final static String ICON_INFOPATH_48X48 = "Microsoft-Office-InfoPath-icon(48x48).png";
  public final static String ICON_ONE_NOTE_48X48 = "Microsoft-Office-Onenote-icon(48x48).png";
  public final static String ICON_DOWNLOAD_48X48 = "Download-2-icon(48x48).png";
  */
  public final static String ICON_ADDRESS_BOOK= "addressBook.png"  ;
  
  public final static String ICON_LOGIN       = "key-icon.png";
  public final static String ICON_CONTACT     = "phone-blue-icon.png";
  public final static String ICON_FILTER      = "filter-icon.png";
  
  public static final int SIZE_NONE  = 0;
  public static final int SIZE_SMALL = 1;
  public static final int SIZE_BIG   = 2;
  public static final int SIZE_48    = 3;
  public static final int SIZE_24    = 4;
  
  private FVIconFactory(){
  }
  
  public static FVIconFactory getInstance() {
    if (instance == null)
      instance = new FVIconFactory();
    
    return instance;
  }
  
  public Embedded getFVIcon(String iconName, boolean dispLayCaption){
    Resource iconResourse = new ThemeResource("../"+THEME_DIR+"/icons/" + iconName);

    Embedded embedded = new Embedded(null, iconResourse);
    
    if (!iconName.equals(ICON_TRASH_SMALL)) {
      embedded.setWidth("32px");
      embedded.setHeight("32px");      
    }
    
    if(dispLayCaption){
      embedded.setCaption(iconName);
    }

    return embedded;
  }
  
  public Resource getFVIcon(String iconName){
  	Resource resource = null;
  	if(iconName.startsWith("../")){
  		resource = new ThemeResource(iconName);
  	}else{
  		resource = new ThemeResource("../"+THEME_DIR+"/icons/" + iconName);	
  	}
  	
    return resource ;
  }

  public Embedded getFVIcon_Embedded(String iconName, int size){
  	Embedded embedded = null;
  	Resource resource = getFVIcon_Internal(iconName, size);
  	if(resource != null){
  		embedded = new Embedded("", resource);
  		if(size == SIZE_NONE){
  			
  		}else if(size == SIZE_SMALL){
  			embedded.setWidth("16px");
  			embedded.setHeight("16px");
  		}else if(size == SIZE_24){
  			embedded.setWidth("24px");
  			embedded.setHeight("24px");
  		}else if(size == SIZE_BIG){
  			embedded.setWidth("32px");
  			embedded.setHeight("32px");
  		}else if(size == SIZE_48){
  			embedded.setWidth("48px");
  			embedded.setHeight("48px");
  		}
  	}
  	return embedded;
  }
  
  public Resource getFVIcon_Internal(String iconName, int size){
  	Resource resource = null;
  	if(iconName.startsWith("../")){
  		resource = new ThemeResource(iconName);
  	}else{
  		if(size == SIZE_NONE){
  			resource = new ThemeResource("../"+THEME_DIR+"/icons/" + iconName);
  		}else if(size == SIZE_SMALL){
  			resource = new ThemeResource("../"+THEME_DIR+"/icons/16x16/" + iconName);
  		}else if(size == SIZE_24){
  			resource = new ThemeResource("../"+THEME_DIR+"/icons/24x24/" + iconName);  			
  		}else if(size == SIZE_BIG){
  			resource = new ThemeResource("../"+THEME_DIR+"/icons/32x32/" + iconName);
  		}else if(size == SIZE_48){
  			resource = new ThemeResource("../"+THEME_DIR+"/icons/48x48/" + iconName);
  		}
  	}

    return resource ;
  }

  public Resource getFVIcon_24(String iconName){
  	return getFVIcon_Internal(iconName, SIZE_24);
  }
  
  public Resource getFVIcon_48(String iconName){
  	return getFVIcon_Internal(iconName, SIZE_48);
  }
  	
  public Resource getFVIcon_Big(String iconName){
  	return getFVIcon_Internal(iconName, SIZE_BIG);
  }
  
  public Resource getFVIcon_Small(String iconName){
  	return getFVIcon_Internal(iconName, SIZE_SMALL);
  }
}
