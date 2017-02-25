package com.foc.access;

public interface FocLogLineConst {
	public static final int LEN_MESSAGE = 200;
	
	public static final int FLD_DATE_TIME  = 1; 
	public static final int FLD_MESSAGE    = 2;
	public static final int FLD_TYPE       = 3;
	public static final int FLD_SUCCESSFUL = 4;

	public static final int TYPE_INFO      = 0;
	public static final int TYPE_ERROR     = 1;
	public static final int TYPE_WARNING   = 2;
	public static final int TYPE_FAILURE   = 3;
}
