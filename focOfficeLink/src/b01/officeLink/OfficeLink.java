package b01.officeLink;

import java.io.File;

import javax.swing.JFileChooser;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.jasper.ExtensionFileFilter;

public class OfficeLink extends FocObject{
  public static final String FORMULA_START_IDENTIFIER = "{";
  public static final String FORMULA_END_IDENTIFIER   = "}";
  
  public static final String PREFIX_EXCEL_XP  = "xls";
  public static final String PREFIX_EXCEL_07  = "xlsx";
  public static final String PREFIX_WORD_XP = "doc";
  public static final String PREFIX_WORD_07 = "docx";
  
  public static final String defaultFilePath = "C:\\";
  public static final String defaultFileType = "";
  
  public OfficeLink(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
    
  protected String getSelectedPath(String defaultPath, String defaultFileName, String fileType, int fileGender){
    String outputPath = null;
    JFileChooser fch = null;
    
    if (defaultPath != null && defaultPath != ""){
      defaultPath = defaultPath.replaceAll("\\\\","/");
    }else{
      defaultPath = defaultFilePath;
    }
    fch = new JFileChooser(defaultPath);
    fch.setSelectedFile(new File(defaultFileName));
    
    if (fileType != null && fileType != ""){
      ExtensionFileFilter filter = new ExtensionFileFilter(fileType.substring(1, fileType.length()));
      fch.addChoosableFileFilter(filter);
    }else{
      fch.setAcceptAllFileFilterUsed(false);
    }
    
    if (fileGender == JFileChooser.DIRECTORIES_ONLY){
      fch.setDialogTitle("Select Directory");
    }else if (fileGender == JFileChooser.FILES_ONLY){
      fch.setDialogTitle("Select File");
    }
    fch.setFileSelectionMode(fileGender);
    int result = fch.showDialog(null, "OK");
    
    if (result == JFileChooser.CANCEL_OPTION ){
      return null;  
    }else{
      try{
        outputPath = fch.getSelectedFile().toString();
        outputPath = outputPath.replaceAll("\\\\","/");
      }catch( Exception e ){
      	Globals.logException(e);
      }
      return outputPath.toLowerCase();
    }
  }
}
