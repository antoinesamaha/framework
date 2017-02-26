package b01.officeLink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hwpf.model.PropertyNode;
import org.apache.poi.hwpf.model.TextPiece;
import org.apache.poi.hwpf.model.TextPieceTable;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.foc.desc.FocObject;
import com.foc.formula.FieldFormulaContext;
import com.foc.formula.Formula;


public class FocWordDocument extends ExtendedWordDocument{
 
  public FocWordDocument(InputStream istream) throws IOException{
    super(istream);
  }

  public FocWordDocument(POIFSFileSystem pfilesystem) throws IOException {
    super(pfilesystem);
  }
  
  public void export(FocObject object){
    TextPieceTable text = getTextTable();
    if(text != null){
      
      List list = text.getTextPieces();
      
      for(int i=0; i<list.size(); i++){
        PropertyNode propertyNode = (PropertyNode)list.get(i);
        if(propertyNode instanceof TextPiece){
          TextPiece textPiece = (TextPiece)propertyNode;
          StringBuffer buffer = textPiece.getStringBuffer();
          System.out.println("BEFORE REPLACE");
          System.out.println("Buffer = "+buffer);
          int startIndex = 0;
          int endIndex =  0;
          while(startIndex >= 0 && endIndex >= 0){
            startIndex = buffer.indexOf(OfficeLink.FORMULA_START_IDENTIFIER, endIndex);
            if(startIndex > 0){
              endIndex =  buffer.indexOf(OfficeLink.FORMULA_END_IDENTIFIER, startIndex);
              String formulaString = buffer.substring(startIndex + OfficeLink.FORMULA_START_IDENTIFIER.length(), endIndex);
              
              /*Formula formula = new Formula(object.getThisFocDesc(), FField.NO_FIELD_ID, formulaString);
            formula.setCurrentFocObject(object);
            String formulaResult = (String) formula.compute();
            replace(textPiece, startIndex, endIndex + OfficeLink.FORMULA_END_IDENTIFIER.length(), formulaResult);*/
              Formula formula = new Formula(formulaString);
              FieldFormulaContext context = new FieldFormulaContext(formula, null, object.getThisFocDesc());
              context.setCurrentFocObject(object);
              String formulaResult = (String) context.evaluateFormula();
              replace(textPiece, startIndex, endIndex + OfficeLink.FORMULA_END_IDENTIFIER.length(), formulaResult);
            }
          }
          System.out.println("AFTER REPLACE");
          System.out.println("Buffer = "+buffer);          
        }
      }
    }
  }
  
  public void write(OutputStream out){
    if(getHwpfDocument() != null){
      try {
        getHwpfDocument().write(out);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
}
