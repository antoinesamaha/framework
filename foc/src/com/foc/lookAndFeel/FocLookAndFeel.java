package com.foc.lookAndFeel;

import javax.swing.UIDefaults;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class FocLookAndFeel extends MetalLookAndFeel{

  protected void initClassDefaults(UIDefaults table)
  {
      super.initClassDefaults(table);
      final String focPackageName = "com.foc.lookAndFeel";

      Object[] uiDefaults = {
                 "TableUI", focPackageName + "FocTableUI",
      };

      table.putDefaults(uiDefaults);
  }

	
	
}
