package com.foc.helpBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.foc.shared.xmlView.XMLViewKey;
import com.foc.web.server.FocWebServer;

public class FocHelpBook {

	private HashMap<String, FocHelpPage> pageMap             = null;
	private HashMap<String, FocHelpPage> viewKey2PageCodeMap = null;
	private ArrayList<String>            pageList = null;

	private FocHelpPage page = null;
	
	public FocHelpBook(){
		declarePages();
	}
	
	public void dispose(){
		
	}
	
	protected void declarePages(){
		
	}
	
	private HashMap<String, FocHelpPage> getPageMap(){
		if(pageMap == null){
			pageMap = new HashMap<String, FocHelpPage>();
		}
		return pageMap;
	}
	
	private HashMap<String, FocHelpPage> getViewKey2PageMap(){
		if(viewKey2PageCodeMap == null){
			viewKey2PageCodeMap = new HashMap<String, FocHelpPage>();
		}
		return viewKey2PageCodeMap;
	}
	
	private List<String> getPagesListCode(){
		if(pageList == null){
			pageList = new ArrayList<String>();
		}
		return pageList;
	}
	
	public int getLastPageIndex(){
		return getPagesArrayListSize() - 1;
	}
	
	public String getPageCodeByPageIndex(int pageIndex){
		return getPagesListCode().get(pageIndex);
	}
	
	public int getPagesArrayListSize(){
		return getPagesListCode().size();
	}

	private void setCurrentPage(FocHelpPage page){
		this.page = page;
	}
	
	private FocHelpPage getCurrentPage(){
		return page;
	}
	
	public int getCurrentPageIndex(){
		int pageIndex = 0;
		String pageCode = getCurrentPage() != null ? getCurrentPage().getPageCode() : null;
		if(pageCode != null){
			pageIndex = getPagesListCode().indexOf(pageCode);
		}
		return pageIndex;
	}
	
	public FocHelpPage getFirstPage(){
		String firstPageCode = getPagesListCode().get(0); 
		return getPage(firstPageCode);
	}
	
	public FocHelpPage getPage(XMLViewKey key){
		String helpKey = getHelpPageKey(key);
		FocHelpPage page = getViewKey2PageMap().get(helpKey);
		setCurrentPage(page);
		return page;
	}
	
	public FocHelpPage getPage(String code){
		FocHelpPage page = getPageMap().get(code);
		setCurrentPage(page);
		return page;
	}

	public FocHelpPage addPage(String code, String fileName){
		FocHelpPage page = new FocHelpPage(code, fileName);
		getPageMap().put(code, page);
		getPagesListCode().add(code);
		return page;
	}
	
	public void addView2HelpPageMapping(XMLViewKey xmlViewKey, FocHelpPage page){
		addView2HelpPageMapping(xmlViewKey.getStorageName(), xmlViewKey.getContext(), xmlViewKey.getType(), page);
	}

	public void addView2HelpPageMapping(String storage, String context, int type, FocHelpPage page){
		String key = getHelpPageKey(storage, context, type);
		getViewKey2PageMap().put(key, page);
	}

	private String getHelpPageKey(XMLViewKey xmlViewKey){
		int viewType       = xmlViewKey.getType();
		String viewContext = xmlViewKey.getContext();
		String storageName = xmlViewKey.getStorageName();
		
		return getHelpPageKey(storageName, viewContext, viewType);
	}
	
	private String getHelpPageKey(String storage, String context, int type){
		return type + "|" + context + "|" + storage;
	}
	
  public static FocHelpBook getInstance() {
  	FocHelpBook helpBook = null;
    if(FocWebServer.getInstance() != null){
      helpBook = FocWebServer.getInstance().getHelpBook();
    }
    return helpBook;
  }
  
  public void popupPage(String pageCode){
  	popupPage(getPage(pageCode));
  }
  
  public void popupPage(FocHelpPage page){
  	if(page != null){
  		
  	}
  }
}
