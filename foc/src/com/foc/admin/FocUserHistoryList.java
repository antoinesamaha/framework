package com.foc.admin;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FocUserHistoryList extends FocList {

  public FocUserHistoryList() {
    super(new FocLinkSimple(FocUserHistoryDesc.getInstance()));
    setDirectImpactOnDatabase(true);
    setDirectlyEditable(false);
  }

  public FocUserHistory findHistory(FocUser user) {
    FocUserHistory found = null;
    if(user != null){
    	found = (FocUserHistory) searchByPropertyObjectReference(FocUserHistoryConst.FLD_USER, user.getReference().getInteger());
    }

    return found;
  }

  private FocUserHistory getUserHistory(FocUserHistory userHistory, FocUser user){
    if(userHistory == null){
      userHistory = (FocUserHistory) newEmptyItem();
      userHistory.setUser(user);
      add(userHistory);
    }
    return userHistory;
  }
  
  public FocUserHistory getOrCreateUserHistory(FocUser user){
  	FocUserHistory userHistory = findHistory(user);
  	userHistory = getUserHistory(userHistory, user);
  	return userHistory;
  }
  
  public void addHistory(FocUserHistory userHistory, FocUser user, String menuCode){
  	userHistory = getUserHistory(userHistory, user);  	
    userHistory.addHistory(menuCode);
    validate(true);
  }
  
  public void addRecentTransaction(FocObject focObject){
  	FocUser user = Globals.getApp().getUser_ForThisSession();
  	if(user != null){
			FocUserHistory userHistory = findHistory(user);
	  	userHistory = getUserHistory(userHistory, user);
	    userHistory.addRecentTransaction(focObject);
	    validate(true);
    }
  }
  
  public void saveFullscreenSettings(FocUserHistory userHistory, FocUser user, int fullscreenMode){
  	if(user != null){
	    if(userHistory == null){
	      userHistory = (FocUserHistory) newEmptyItem();
	      userHistory.setUser(user);
	      add(userHistory);
	    }
	    userHistory.setFullscreen(fullscreenMode);
	    validate(true);
  	}
  }
  
  public int retrieveFullScreenSettings(FocUser user){
    int result = FocUserHistory.MODE_DEFAULT;
    if(user != null){
      FocUserHistory history = findHistory(user);
      
      if(history != null){
        result = history.isFullscreen();
      }      
    }
    
    return result;
  }

  public static synchronized void addHistory(String menuCode){
		FocUserHistoryList historyList = (FocUserHistoryList) FocUserHistoryDesc.getInstance().getFocList();
		
		historyList.loadIfNotLoadedFromDB();
		FocUserHistory userHistory = historyList.findHistory(Globals.getApp().getUser_ForThisSession());
	  if(userHistory != null){
	  	historyList.addHistory(userHistory, Globals.getApp().getUser_ForThisSession(), menuCode);
	  }
  }
}
