package com.foc.business.workflow.rights;

public interface RightLevelConst {
	public static final int FLD_READ                   =  1;
	public static final int FLD_INSERT                 =  2;
	public static final int FLD_DELETE_DRAFT           =  3;
	public static final int FLD_DELETE_APPROVED        =  4;
	public static final int FLD_MODIFY_DRAFT           =  5;
	public static final int FLD_MODIFY_APPROVED        =  6;
	public static final int FLD_PRINT_DRAFT            =  7;
	public static final int FLD_PRINT_APPROVE          =  8;
	public static final int FLD_APPROVE                =  9;
	public static final int FLD_CLOSE                  = 10;
	public static final int FLD_CANCEL                 = 11;
	public static final int FLD_MODIFY_CODE_DRAFT      = 12;
	public static final int FLD_MODIFY_CODE_APPROVED   = 13;
	public static final int FLD_UNDO_SIGNATURE         = 14;
	public static final int FLD_MODIFY_SIGNATRUE_STAGE = 15;
	public static final int FLD_VIEW_LOG               = 16;
	
	public static final String ALL_RIGHTS = "All Rights";
}
