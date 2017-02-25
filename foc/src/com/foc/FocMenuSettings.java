package com.foc;

public class FocMenuSettings {
  private boolean includeNavigation = true;
  private boolean includeAbout      = true;
  private boolean includeExit       = true;
  
  public FocMenuSettings(){
  }
  
  public boolean isIncludeNavigation() {
    return includeNavigation;
  }

  public void setIncludeNavigation(boolean includeNavigation) {
    this.includeNavigation = includeNavigation;
  }

  public boolean isIncludeAbout() {
    return includeAbout;
  }

  public void setIncludeAbout(boolean includeAbout) {
    this.includeAbout = includeAbout;
  }

  public boolean isIncludeExit() {
    return includeExit;
  }

  public void setIncludeExit(boolean includeExit) {
    this.includeExit = includeExit;
  }
}
