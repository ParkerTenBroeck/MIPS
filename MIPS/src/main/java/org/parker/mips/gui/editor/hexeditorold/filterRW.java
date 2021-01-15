package org.parker.mips.gui.editor.hexeditorold;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class filterRW extends FileFilter {

   public boolean accept(File var1) {
      return var1.canWrite();
   }

   public String getDescription() {
      return "";
   }
}
