package com.foc.gui.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.foc.gui.tree.renderer.FDefaultTreeCellRenderer;
import com.foc.tree.FNode;

@SuppressWarnings("serial")
public class FGTree extends JTree {

	private boolean holdTreeExpansionReaction = false;
	
	public FGTree(TreeModel treeModel){
		super(treeModel);
		
		setRootVisible(((FTreeModel)treeModel).getTree().isRootVisible());
		setShowsRootHandles( true );
    //setEditable(true);
		FDefaultTreeCellRenderer renderer = new FDefaultTreeCellRenderer();
    //renderer.setLeafIcon(null);
		//renderer.setClosedIcon(null);
    //renderer.setOpenIcon(null); 
		
    setCellRenderer(renderer);
    setLineStyle(((FTreeModel)treeModel).getTree().isWithLineStyle());
    
    doLayout();
		//setBackground(Color.RED);
	}
	
  public void dispose(){
  }

  public void setLineStyle(boolean withLineStyle){
    if(((FTreeModel)treeModel).getTree().isWithLineStyle()){
    	putClientProperty( "JTree.lineStyle", "Angled" );
    }else{
    	putClientProperty( "JTree.lineStyle", "None" );
    }
    //putClientProperty( "JTree.lineStyle", "Horizontal" );
  }
  
  public void setIconsToNull(){
		FDefaultTreeCellRenderer renderer = (FDefaultTreeCellRenderer) getCellRenderer();
    renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
    renderer.setOpenIcon(null); 
  }

  public void setIconsToNullExceptLeaf(){
		FDefaultTreeCellRenderer renderer = (FDefaultTreeCellRenderer) getCellRenderer();
		renderer.setClosedIcon(null);
    renderer.setOpenIcon(null); 
  }

  public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
  	return ((FNode) value).getTitle();
  }
  
  public FNode getSimilarNode(FNode node){
    FNode similarNode = null;
    if(node != null){
      FNode rootNode = (FNode)getModel().getRoot();
      similarNode = rootNode.getSimilarNode(node);
    }
    return similarNode ;
  }
  
  public FNode getNodeByTitle(String nodeTitle){
  	FNode similarNode = null;
  	if(nodeTitle != null){
  		FNode rootNode = (FNode)getModel().getRoot();
  		similarNode = rootNode.getNodeByTitle(nodeTitle);
  	}
  	return similarNode;
  }
  
  public void expandAll(){
  	setHoldTreeExpansionReaction(true);
    for(int i = 0; i < getRowCount(); i++){
      expandRow(i);
  	}
    setHoldTreeExpansionReaction(false);
    if(getRowCount() > 0) expandRow(0);    
  }
  
  public void collapseAll(){
  	setHoldTreeExpansionReaction(true);
  	for(int i = getRowCount() - 1 ; i >= 0; i--){
      collapseRow(i);
  	}
  	setHoldTreeExpansionReaction(false);
    if(getRowCount() > 0) collapseRow(0);
  }

  public void copyExpansion(FGTree src){
    if(src != null && src.isRootVisible()){
      TreePath rootPath = src.getPathForRow(0);
      Enumeration enumer = src.getExpandedDescendants(rootPath);
      
      while(enumer != null && enumer.hasMoreElements()){
        TreePath expandedPath = (TreePath) enumer.nextElement();

        FNode[] objectPath = new FNode[expandedPath.getPathCount()];

        boolean targetPathContainsNullValue = false;
        for(int i = 0; i < expandedPath.getPathCount() && !targetPathContainsNullValue; i++){
          FNode node = (FNode)expandedPath.getPathComponent(i);
          FNode similarNode = getSimilarNode(node);
          if(similarNode == null){
            targetPathContainsNullValue = true;
          }
          objectPath[i] = similarNode;
        }

        TreePath targetPath = new TreePath(objectPath);        
        
        //For Debugging
        /*String pathDebug = "Expanded PATH : ";
        Object obj[] = expandedPath.getPath();
        for( int i = 0; i < obj.length; i++){
        	if(obj[i] != null){
        		pathDebug += "-"+obj[i].toString();
        	}else{
        		pathDebug += "-null";
        	}
        }
        Globals.logString(pathDebug+" "+targetPathContainsNullValue);
        
        pathDebug = "Target PATH : ";
        obj = targetPath.getPath();
        for( int i = 0; i < obj.length; i++){
        	if(obj[i] != null){
        		pathDebug += "-"+obj[i].toString();
        	}else{
        		pathDebug += "-null";
        	}
        }
        Globals.logString(pathDebug+" "+targetPathContainsNullValue);*/
        
        if(!targetPathContainsNullValue){
          expandPath(expandedPath);
          expandPath(targetPath);
        }
      }
    }
  }
  
  public ArrayList<ArrayList<String>> getExpandedPathesNodesTitles(){
  	ArrayList<ArrayList<String>> expandedPathesArray = new ArrayList<ArrayList<String>>();
  	TreePath rootPath = getPathForRow(0);
    Enumeration enumer = getExpandedDescendants(rootPath);
    while(enumer != null && enumer.hasMoreElements()){
      TreePath expandedPath = (TreePath) enumer.nextElement();
      ArrayList<String> pathNodeTitles = new ArrayList<String>();
      for(int i = 0; i < expandedPath.getPathCount(); i++){
      	FNode node = (FNode)expandedPath.getPathComponent(i);
      	pathNodeTitles.add(node.getTitle());
      }
      expandedPathesArray.add(pathNodeTitles);
    }
    return expandedPathesArray;
  }
  
  public void expandPathes(ArrayList<ArrayList<String>> PathesNodesTitleToExpand){
  	Iterator<ArrayList<String>> iter = PathesNodesTitleToExpand.iterator();
	  while(iter != null && iter.hasNext()){
	  	ArrayList<String> nodesPathTitles = iter.next();
	  	FNode[] objectPath = new FNode[nodesPathTitles.size()];
	  	boolean targetPathContainsNullVaLues = false;
	  	for(int i = 0; i < nodesPathTitles.size() && !targetPathContainsNullVaLues; i++){
	  		String nodeTitle = nodesPathTitles.get(i);
	  		FNode node = this.getNodeByTitle(nodeTitle);
	  		if(node == null){
	  			targetPathContainsNullVaLues = true;
	  		}
	  		objectPath[i] = node;
	  	}
	  	if(!targetPathContainsNullVaLues){
	  		TreePath targetPath = new TreePath(objectPath);
        this.expandPath(targetPath);
	  	}
	  }
  }

	public boolean isHoldTreeExpansionReaction() {
		return holdTreeExpansionReaction;
	}

	public void setHoldTreeExpansionReaction(boolean holdTreeExpansionReaction) {
		this.holdTreeExpansionReaction = holdTreeExpansionReaction;
	}
}
