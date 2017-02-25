package com.foc.vaadin.gui.mswordGenerator;

import java.math.BigInteger;
import java.util.List;
 
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
 
public class XWPFExtendedTable {
   protected CTTbl ctTbl;
   protected int nbRows;
 
   public XWPFExtendedTable(XWPFTableCell cell) {
     ctTbl = cell.getCTTc().addNewTbl();
 
     /* definition du style du tableau */
     CTTblPr tblPr = ctTbl.addNewTblPr();
     CTString style = tblPr.addNewTblStyle();
     style.setVal("Grilledutableau");
     CTTblWidth tblW = tblPr.addNewTblW();
     tblW.setW(new BigInteger("0"));
     tblW.setType(STTblWidth.AUTO);
 
     /* Creation d'un paragraph (Obligatoire pour eviter les erreur de lecture) */
     cell.getCTTc().addNewP();
   }
 
   public void setColumnDefinition(int[] width) {
     nbRows = width.length; 
     CTTblGrid tblGrig = ctTbl.addNewTblGrid();
 
     CTRow tr = ctTbl.addNewTr();
     String rsiRid = "00020A65";
     tr.setRsidR(rsiRid.getBytes());
 
     for (int i=0; i<nbRows ; i++) {
       tblGrig.addNewGridCol().setW(new BigInteger(""+width[i]));
       CTTc tc = tr.addNewTc();
       tc.addNewP();
     }
   }
 
   public int getNbRows() {
     return nbRows;
   }
 
   public CTTbl getCTTbl() {
    return ctTbl;
   }
}