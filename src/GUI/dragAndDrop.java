/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import mips.FileWriteReader;

/**
 *
 * @author parke
 */
public class dragAndDrop {


    public dragAndDrop(JComponent component) {
        new DropTarget(component, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent dtde) {
                
            }

            public void dragExit(DropTargetEvent dte) {
                
            }

            public void dragOver(DropTargetDragEvent dtde) {
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            public void drop(DropTargetDropEvent dtde) {
                try {
                    // Ok, get the dropped object and try to figure out what it is
                    Transferable tr = dtde.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();
                    for (int i = 0; i < flavors.length; i++) {
                        // Check for file lists specifically
                        if (flavors[i].isFlavorJavaFileListType()) {
                            // Great!  Accept copy drops...
                            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            // And add the list of file names to our text area
                            java.util.List list = (java.util.List) tr.getTransferData(flavors[i]);
                            for (int j = 0; j < list.size(); j++) {
                                //filePathLable.setText(((File) list.get(j)).getPath());
                                FileWriteReader.loadFile((File) list.get(j));
                            }
                            // If we made it this far, everything worked.
                            dtde.dropComplete(true);
                            return;
                        } // Ok, is it another Java object?
                        else if (flavors[i].isFlavorSerializedObjectType()) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            Object o = tr.getTransferData(flavors[i]);
                            System.out.println(o);
                            dtde.dropComplete(true);
                            return;
                        } // How about an input stream?
                        else if (flavors[i].isRepresentationClassInputStream()) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            dtde.dropComplete(true);
                            return;
                        }
                    }
                    // Hmm, the user must not have dropped a file list
                    System.out.println("Drop failed: " + dtde);
                    dtde.rejectDrop();
                } catch (Exception e) {
                    e.printStackTrace();
                    dtde.rejectDrop();
                }
            }
        });
    }
}
