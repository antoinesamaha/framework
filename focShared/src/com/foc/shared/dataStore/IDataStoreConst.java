package com.foc.shared.dataStore;

public interface IDataStoreConst {
	
	public static final int DATA_TYPE_OBJECT = 0;
	public static final int DATA_TYPE_LIST   = 1;
	
	public static final int COMMAND_QUERY  = 0;
	public static final int COMMAND_UPDATE = 1;
	public static final int COMMAND_DELETE = 2;
	public static final int COMMAND_INSERT = 3;
	
	//public static final String REQ_PARAM_TABLE       = "TABLE";
	//public static final String REQ_PARAM_REF         = "REF";
	
	//public static final String REQ_PARAM_LOAD_METHOD = "LOAD_METHOD";
	public static final String REQ_PARAM_JSON        = "JSON";

	public static final String JSON_KEY_DATA_KEY      = "KEY";
	public static final String JSON_KEY_COMMAND       = "CMND";
	public static final String JSON_KEY_SERIAL_NUMBER = "SN";
	public static final String JSON_KEY_FW_OBJECT     = "FW_OBJECT";
	public static final String JSON_KEY_FW_LIST       = "FW_LIST";
	public static final String JSON_KEY_FW_OBJECT_REF = "FW_OBJECT_REF";
	
	public static final int FIELD_TYPE_TEXT                         =  1;
	public static final int FIELD_TYPE_INT                          =  2;
	public static final int FIELD_TYPE_DOUBLE                       =  3;
	public static final int FIELD_TYPE_LONG                         =  4;
	public static final int FIELD_TYPE_DATE                         =  5;
	public static final int FIELD_TYPE_TIME                         =  6;
	public static final int FIELD_TYPE_OBJECT                       =  7;
	public static final int FIELD_TYPE_LIST                         =  8;
	public static final int FIELD_TYPE_BOOLEAN                      =  9;
	public static final int FIELD_TYPE_MULTIPLE_CHOICE              = 10;
	public static final int FIELD_TYPE_IMAGE                        = 11;
	public static final int FIELD_TYPE_PASSWORD                     = 12;
	public static final int FIELD_TYPE_BLOB_STRING                  = 13;
	public static final int FIELD_TYPE_MULTIPLE_CHOICE_FOC_DESC     = 14;
	public static final int FIELD_TYPE_MULTIPLE_CHOICE_STRING_BASED = 15;
	public static final int FIELD_TYPE_EMAIL_FIELD                  = 16;
	public static final int FIELD_TYPE_XML_VIEW_SELECTOR            = 17;
	public static final int FIELD_TYPE_BLOB_FILE                    = 18;
	public static final int FIELD_TYPE_DATE_TIME                    = 19;
}

