package com.foc.gridView;

import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.tree.TreeScanner;

public class GridLineList extends FocList implements TreeScanner<FNode>{

	private GridDefinition definition    = null;
	private GridLine       currentFather = null;
	
	public GridLineList(GridDefinition definition){
		super(new FocLinkSimple(definition.getGridLineDesc()));
		setDirectlyEditable(false);
		setDirectImpactOnDatabase(false);
		this.definition = definition;
		build();
	}
	
	public void dispose(){
		super.dispose();
		definition = null;
	}
	
	public GridDefinition getGridDefinition(){
		return definition;
	}

	public void build(){
		FTree tree = definition.getTree();
		tree.scan(this);
	}

	public void afterChildren(FNode node) {
		if(node.getNodeDepth() < definition.getLevel()){
			currentFather = (GridLine) currentFather.getFatherObject();
		}
	}

	public boolean beforChildren(FNode node) {
		if(node.getNodeDepth() < definition.getLevel()){
			GridLine line = (GridLine) newEmptyItem();
			
			line.setFatherObject(currentFather);
			line.setCode(node.getCode());
			line.setTitle(node.getTitle());
			line.setNode(node);
			
			add(line);
			
			currentFather = line;
		}
		return true;
	}
}
