/*
 * Created on Jan 9, 2006
 */
package com.foc.join;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.desc.field.FLongField;
import com.foc.desc.field.FObjectField;

/** @author 01Barmaja */
public class JoinUsingLongField extends Join {

	private int objectFieldID = FField.NO_FIELD_ID;
	private boolean objectFieldIsInSource = true;
	private int targetObjectFieldID = 0;

	public JoinUsingLongField(TableAlias sourceTableAlias, int objectFieldID, boolean objectFieldIsInSource, int targetObjectFieldID) {
		super(sourceTableAlias);
		this.objectFieldID = objectFieldID;
		this.objectFieldIsInSource = objectFieldIsInSource;
		this.setTargetObjectID(targetObjectFieldID);
	}

	public int getTargetObjectID() {
		return targetObjectFieldID;
	}

	public void setTargetObjectID(int targetObjectID) {
		this.targetObjectFieldID = targetObjectID;
	}

	public String getLinkCondition() {
		String ret = null;
		try {
			if (getSourceAlias() != null && getTargetAlias() != null) {
				FocDesc srcDesc = getSourceAlias().getFocDesc();
				FocDesc tarDesc = getTargetAlias().getFocDesc();
				int provider = srcDesc.getProvider();
				FObjectField objField = null;
				FLongField targetObjField = null;
				if (objectFieldIsInSource) {
					objField = (FObjectField) srcDesc.getFieldByID(objectFieldID);
					if (getTargetObjectID() > 0 && tarDesc.getFieldByID(getTargetObjectID()) instanceof FLongField) {
						targetObjField = (FLongField) tarDesc.getFieldByID(getTargetObjectID());
					}
				} else {
					objField = (FObjectField) tarDesc.getFieldByID(objectFieldID);
					if (getTargetObjectID() > 0 && srcDesc.getFieldByID(getTargetObjectID()) instanceof FLongField) {
						targetObjField = (FLongField) srcDesc.getFieldByID(getTargetObjectID());
					}
				}
				if (objField != null && targetObjField != null) {
					if (DBManager.provider_FieldNamesBetweenSpeachmarks(provider)) {
						ret = getSourceAlias().getAlias() + ".\"" + objField.getDBName() + "\"=" + getTargetAlias().getAlias() + ".\"" + targetObjField.getDBName() + "\"";
					} else {
						ret = getSourceAlias().getAlias() + "." + objField.getDBName() + "=" + getTargetAlias().getAlias() + "." + targetObjField.getDBName();
					}
				}
			}
		} catch (Exception e) {
			Globals.logException(e);
		}
		return ret;
	}

	@Override
	protected int fillRequestDescWithJoinFields_Internal(FocRequestDesc desc, int firstJoinFieldID) {
		return 0;
	}

	@Override
	public String getUpdateCondition() {
		return null;
	}
}
