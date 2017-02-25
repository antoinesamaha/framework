package com.foc.business.currency;

public interface CurrencyConstants {
  public static final int FLD_NAME        = 1;
  public static final int FLD_DESCRIPTION = 2;
  public static final int FLD_RATE_RATIO  = 3;

  public static final int RATE_RATIO_BASIC_THIS = 0;
  public static final int RATE_RATIO_THIS_BASIC = 1;

  public static final String RATE_RATIO_LABEL_BASIC_THIS = "1 of the Basic Currency = X of this currency";
  public static final String RATE_RATIO_LABEL_THIS_BASIC = "1 of this Currency = X of the Basic currency";
}
