package com.foc.gui.tree.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.foc.Globals;
import com.foc.gui.tree.FTreeModel;
import com.foc.property.FProperty;
import com.foc.tree.FNode;
import com.foc.tree.FTree;

@SuppressWarnings("serial")
public class FDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
	//private Border     editorBorder = BorderFactory.createLineBorder(Color.BLACK);
	
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
  	Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    
  	comp.setFont(Globals.getDisplayManager().getDefaultFont());
    FTree fTree = ((FTreeModel)tree.getModel()).getTree();
    
    //tree.setBackground(Color.ORANGE);
    FNode node = (FNode) value;
    
    Color color = fTree.getColorForNode(node, row, null);
    if(color != null){
      setBackgroundSelectionColor(Color.LIGHT_GRAY);
      setBackgroundNonSelectionColor(color);  
    }else{
      setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
      setBackgroundNonSelectionColor(Color.WHITE);
    }
    
    Color foreground = fTree.getForegroundForNode(node);
    if(foreground != null){
    	setForeground(foreground);
    }
    
    //setBackgroundSelectionColor(Color.RED);
    //setBorderSelectionColor(Color.RED);
    
    Icon icon = fTree.getIconForNode(node);
    
    if(icon != null){
      setIcon(icon);
    }
    
    JLabel label = this;
    label.setForeground(fTree.getForegroundForNode(node));
    FProperty prop = fTree.getTreeSpecialProperty(node);
    if(prop != null){
      label.setText(""+prop.getTableDisplayObject(null));  
    }
    
    String code = null;
    if(!fTree.isHideCode()){
    	code = fTree.getCodeDisplayString_ForTreeRenderer(node);
    }
    
    if(code != null){
      String stringLabel = label.getText();
      label.setText(code+stringLabel);
    }
    
    /*
    if(Globals.getDisplayManager().getLookAndFeel() == DisplayManager.LOOK_AND_FEEL_NIMBUS){
	    Border border = UIManager.getBorder("Table.cellNoFocusBorder");
	    if (border == null) {
	        border = editorBorder;
	    } else {
	        // use compound with LAF to reduce "jump" text when starting edits
	        border = BorderFactory.createCompoundBorder(editorBorder, border);
	    }
	    ((JComponent)comp).setBorder(border);
    }
    */

    return label;
  }
}
