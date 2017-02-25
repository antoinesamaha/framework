package com.foc.admin;

public interface FocUserHistoryConst {
  public static final int    FLD_USER                = 1;
  public static final int    FLD_HISTORY             = 2;
  public static final int    FLD_FULLSCREEN          = 3;
  public static final int    FLD_RECENT_TRANSACTIONS = 4;
  
  public static final int    HISTORY_SIZE      = 10;
  public static final String HISTORY_DELIMITER = ";";
  public static final String INNER_DELIMITER   = ",";
  
  public static final int    MODE_DEFAULT      = 0;
  public static final int    MODE_FULLSCREEN   = 1;
  public static final int    MODE_WINDOWED     = 2;
}
