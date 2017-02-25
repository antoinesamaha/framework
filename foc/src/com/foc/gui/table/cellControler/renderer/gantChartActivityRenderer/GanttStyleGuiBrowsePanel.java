package com.foc.gui.table.cellControler.renderer.gantChartActivityRenderer;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class GanttStyleGuiBrowsePanel extends FListPanel{
  
  public GanttStyleGuiBrowsePanel(FocList list, int viewID){
    setFrameTitle("Gantt chart styles");
    setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
    FocDesc desc = GanttStyleDesc.getInstance();
    if(desc != null){
      if(list == null){
        list = GanttStyleDesc.getList(FocList.FORCE_RELOAD);
      }
      if(list != null){
        setFocList(list);

        FTableView tableView = getTableView();       
        tableView.addColumn(desc, GanttStyleDesc.FLD_NAME, false);
        //tableView.setDetailPanelViewID(viewID);
        
        construct();
        requestFocusOnCurrentItem();
        FValidationPanel savePanel = showValidationPanel(true);
        savePanel.addSubject(list);
        //setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
        setFill(FPanel.FILL_VERTICAL);
      }
    }
  }
}
