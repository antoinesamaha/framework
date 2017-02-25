package com.foc.web.dataModel;

import java.util.ArrayList;
import java.util.Collection;

import com.foc.admin.UserSession;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.tree.TreeScanner;
import com.foc.tree.criteriaTree.FCriteriaTree;
import com.vaadin.data.Container.Hierarchical;

@SuppressWarnings("serial")
public class FocTreeWrapper extends FocDataWrapper implements Hierarchical {

	private FocObject zoomObject = null;
	
  public FocTreeWrapper(FTree tree) {
    super(tree);
    FocList list = getFocList();
    byCompany = list != null && list.getFocDesc() != null ? list.getFocDesc().isByCompany() : false;
    if(byCompany){
      company = UserSession.getInstanceForThread().getCompany();
    }
  }
  
  public void dispose(){
  	super.dispose();
  	zoomObject = null;
  }
  
  public void setZoomObject(FocObject zoomObject){
  	this.zoomObject = zoomObject;
  	resetVisibleListElements();
  }

  public FocObject getZoomObject(){
  	return zoomObject;
  }
  
  protected boolean includeFocObject(FocObject focObj){
  	boolean includeFocObject = super.includeFocObject(focObj);
  	if(includeFocObject){
  		FocObject zoomObj = getZoomObject();
  		if(zoomObj != null){
  			boolean hasZoomFather = zoomObj.equalsRef(focObj);
  			while(focObj != null && !hasZoomFather){
  				focObj = focObj.getFatherObject();
  				if(focObj != null){
  					hasZoomFather = zoomObj.equalsRef(focObj);
  				}
  			}
  			includeFocObject = hasZoomFather;	
  		}
  	}
  	return includeFocObject;
  }
  
  public FocList getFocList() {
    FocList focList = null;

    if (getFTree() != null) {
      focList = getFTree().getFocList();
    }

    return focList;
  }

  public FTree getFTree() {
    return (FTree) getFocData();
  }

  public void setFTree(FTree tree) {
    setFocData(tree);
  }
  
  @Override
  protected ArrayList<FocObject> getVisibleListElements(boolean create) {
    if (visibleListElements == null && create) {
      visibleListElements = new ArrayList<FocObject>();
      FocList list = getFocList();
      if(list != null){
	      for (int i = 0; i < list.size(); i++) {
	        FocObject focObj = list.getFocObject(i);
	        if (focObj != null && includeFocObject(focObj)) {
	          visibleListElements.add(focObj);
	
	          if(getFTree() != null && getFTree() instanceof FCriteriaTree){
	          	FNode node = getFTree().findNode(focObj.getReference().getInteger());
		          if(node != null){
		          	while (node.getFatherNode() != null) {
		          		node = node.getFatherNode();
		          		if(!node.isRoot() || getFTree().isRootVisible()){
			          		FocObject fObj = (FocObject) node.getObject(); 
				            if (!visibleListElements.contains(fObj)) {
				              visibleListElements.add(fObj);
				            }
		          		}
			          }
		          }
	          	
	          }else{
		          FocObject fatherObject = focObj.getFatherObject();
		          while (fatherObject != null) {
		            if (!visibleListElements.contains(fatherObject)) {
		              visibleListElements.add(fatherObject);
		            }
		            fatherObject = fatherObject.getFatherObject();
		          }
	          }
	        }
	      }
	
	      if (getInitialValue() != null) {
	        int initialValueId = getInitialValue().getReference().getInteger();
	        if (!list.containsId(initialValueId)) {
	          visibleListElements.add(getInitialValue());
	        }
	      }
	
	      if (listOrder != null) {
	        // ATTENTION
	        // Collections.sort(visibleListElements, listOrder);
	      }
      }
    }
    return visibleListElements;
  }

  @Override
  public Collection<?> getChildren(Object itemId) {
    // FocObject focObj = (FocObject) getItem(itemId);
    FNode node = getFTree().vaadin_FindNode(itemId);
    ArrayList<Integer> array = null;
    if (node.getVisibleChildCount() > 0) {
      array = new ArrayList<Integer>();
      for (int i = 0; i < node.getVisibleChildCount(); i++) {
        FNode cNode = node.getVisibleChildAt(i);
        FocObject cObject = (FocObject) cNode.getObject();
        if (getVisibleListElements(true).contains(cObject)) {
        	cNode.setIndexInChildrenArray(array.size());        	
          array.add(cObject.getReference().getInteger());
        }else{
        	cNode.setIndexInChildrenArray(-1);
        }
      }
    }

    return array;

  }

  @Override
  public Object getParent(Object itemId) {
    return getFTree() != null ? getFTree().getParent(itemId) : null;
  }

  @Override
  public Collection<?> rootItemIds() {
    ArrayList<Integer> array = new ArrayList<Integer>();
    
//    if(getFTree().isRootVisible()){
    	FocObject obj = getFTree().getRoot() != null ? (FocObject) getFTree().getRoot().getObject() : null;
    	if (obj != null && obj.getReference().getInteger() != 0 && getFTree().isRootVisible()) {
    		if (getVisibleListElements(true).contains(obj)) {
    			array.add(obj.getReference().getInteger());
    		}
    	} else if (getFTree().getRoot() != null) {
    		for (int i = 0; i < getFTree().getRoot().getVisibleChildCount(); i++) {
    			FNode node = getFTree().getRoot().getVisibleChildAt(i);
    			obj = (FocObject) node.getObject();
    			if (obj != null && getVisibleListElements(true).contains(obj)) {
    				array.add(obj.getReference().getInteger());
    			}
    		}
    	}
    /*
    }
    else{
    	for(int i=0; i<getFTree().getRoot().getChildCount(); i++){
    		if(getFTree().getRoot() != null){
    			FocObject childObj = (FocObject) getFTree().getRoot().getChildAt(i).getObject();
    			
    			if(childObj != null){
    				array.add(childObj.getReference().getInteger());
    			}
    		}
    	}
    }
    */
    return array;
  }

  @Override
  public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
    return getFTree() != null ? getFTree().setParent(itemId, newParentId) : false;
  }

  @Override
  public boolean areChildrenAllowed(Object itemId) {
    return getFTree() != null ? getFTree().areChildrenAllowed(itemId) : false;
  }

  @Override
  public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
    return getFTree() != null ? getFTree().setChildrenAllowed(itemId, areChildrenAllowed) : false;
  }

  @Override
  public boolean isRoot(Object itemId) {
    return getFTree() != null ? getFTree().isRoot(itemId) : false;
  }

  @Override
  public boolean hasChildren(Object itemId) {
    return getFTree() != null ? getFTree().hasChildren(itemId) : false;
  }

  @Override
  public Collection<?> getItemIds() {
  	  final ArrayList<Integer> arrayRefs = new ArrayList<Integer>();  	
//  	if(arrayRefs == null){
	    
	    getFTree().scanVisible(new TreeScanner<FNode>() {
	
	      @Override
	      public boolean beforChildren(FNode node) {
	      	boolean toAdd = true;
	      	
	        FocObject focObj = (FocObject) node.getObject();
	        
					if(			node == null 
							|| 	focObj == null 
							|| 	node.getNodeDepth() > getFTree().getDepthVisibilityLimit() 
							|| (node.isRoot() && !getFTree().isRootVisible()) || !getVisibleListElements(true).contains(focObj)
							){
						toAdd = false;
					}
	
	        if (toAdd) {
	          int ref = focObj.getReference().getInteger();
	          if (ref != 0) {
	            arrayRefs.add(ref);
	          }
	        }
	        return true;
	      }
	
	      @Override
	      public void afterChildren(FNode node) {
	      }
	
	    });
//  	}
    
    return arrayRefs;
    // return getFocList() != null ? getFocList().getItemIds() : null;
  }
}