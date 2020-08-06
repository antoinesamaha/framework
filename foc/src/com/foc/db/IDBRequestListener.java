package com.foc.db;

import java.util.ArrayList;

import com.foc.desc.FocDesc;

public interface IDBRequestListener {

  public static final int INSERT = 0;
  public static final int UPDATE = 1;
  public static final int DELETE = 2;

	void newRequest(ArrayList<DBRequestAffectedObject> affectedObjects, int requestType, String req);
	
	public class DBRequestAffectedObject {
		private FocDesc focDesc = null;
		private long    ref     = 0;
		
		public DBRequestAffectedObject(FocDesc focDesc, long ref){
			this.focDesc = focDesc;
			this.ref     = ref;
		}
		
		public FocDesc getFocDesc() {
			return focDesc;
		}
		
		public void setFocDesc(FocDesc focDesc) {
			this.focDesc = focDesc;
		}
		
		public long getRef() {
			return ref;
		}
		
		public void setRef(long ref) {
			this.ref = ref;
		}
	}
	
}
