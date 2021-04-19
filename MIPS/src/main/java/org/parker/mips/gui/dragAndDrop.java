/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import org.parker.mips.gui.userpanes.editor.rsyntax.FormattedTextEditor;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;

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
                    for (DataFlavor flavor : flavors) {
                        // Check for file lists specifically
                        if (flavor.isFlavorJavaFileListType()) {
                            // Great!  Accept copy drops...
                            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            // And add the list of file names to our text area
                            java.util.List<?> list = (java.util.List<?>) tr.getTransferData(flavor);
                            for (Object o : list) {
                                //filePathLable.setText(((File) list.get(j)).getPath());
                                new FormattedTextEditor((File)o);
                                //Editor.loadFileIntoEditor((File) o);
                                MainGUI.refreshAll();
                                //list.get(j); is the file
                            }
                            // If we made it this far, everything worked.
                            dtde.dropComplete(true);
                            return;
                        } // Ok, is it another Java object?
                        else if (flavor.isFlavorSerializedObjectType()) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            //Object o = tr.getTransferData(flavor);
                            dtde.dropComplete(true);
                            return;
                        } // How about an input stream?
                        else if (flavor.isRepresentationClassInputStream()) {
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
