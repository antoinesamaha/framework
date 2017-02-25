package com.foc.gui;

import com.foc.desc.FocObject;
import com.foc.tree.FNode;

public class DoubleClickEvent {
	private int       row       = -1;
	private int       col       = -1;
	private FocObject focObject = null;
	private FNode     node      = null;
	
	public DoubleClickEvent(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public void dispose(){
		focObject = null;
		node = null;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public FocObject getFocObject() {
		return focObject;
	}

	public void setFocObject(FocObject focObject) {
		this.focObject = focObject;
	}

	public FNode getNode() {
		return node;
	}

	public void setNode(FNode node) {
		this.node = node;
	}
}
