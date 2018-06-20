package com.foc.business.notifier;

public interface FocNotificationConst {
//  public static final int FLD_TEMPLATE_OBJECT          = 1;
//  public static final int FLD_TRANSACTION              = 2;
//  public static final int FLD_EVENT                    = 3;
//  public static final int FLD_TABLE_NAME               = 4;
  
  public static final int EVT_CREATE_USER_FROM_CONTACT = 1;
  public static final int EVT_TABLE_ADD                = 2;
  public static final int EVT_TABLE_DELETE             = 3;
  public static final int EVT_TABLE_UPDATE             = 4;
  public static final int EVT_TRANSACTION_APPROVE      = 5;
  public static final int EVT_TRANSACTION_CLOSE        = 6;
  public static final int EVT_TRANSACTION_CANCEL       = 7;
  public static final int EVT_SCHEDULED                = 8;
  public static final int EVT_TRANSACTION_SIGN         = 9;
  public static final int EVT_TRANSACTION_UNSIGN       =10;
}
