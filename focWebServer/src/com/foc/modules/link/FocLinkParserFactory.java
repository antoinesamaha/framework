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
package com.foc.modules.link;

import java.util.HashMap;

public class FocLinkParserFactory {
	
	private HashMap<String, FocLinkHandlerCreator> map = null; 
	
  private FocLinkParserFactory() {
  	map = new HashMap<String, FocLinkHandlerCreator>(); 
  			
  	put(IFocLinkConst.TAG_LINK_DOWNLOAD, null, new FocLinkHanlderCreator_DownloadMessageBox());
  	put(IFocLinkConst.TAG_LINK_ACK, null, new FocLinkAcknowledgeHandlerCreator());
  	put(IFocLinkConst.TAG_LINK_CHECK, null, new FocLinkHandlerCreator_Check());
  }

  private String builKey(String tag, String nameAttribute){
  	String key = tag;
		if(nameAttribute != null && nameAttribute.isEmpty()){
			key += "|"+nameAttribute;
		}
  	return key;
  }
  
  public void put(String tag, String nameAttribute, FocLinkHandlerCreator creator){
  	map.put(builKey(tag, nameAttribute), creator);
  }
  
  public FocLinkHandlerCreator get(String tag, String nameAttribute){
  	return map.get(builKey(tag, nameAttribute));
  }
  
  private static FocLinkParserFactory instance = null;
  public static FocLinkParserFactory getInstance() {
    if (instance == null){
      instance = new FocLinkParserFactory();
    }
    return instance;
  }
}
