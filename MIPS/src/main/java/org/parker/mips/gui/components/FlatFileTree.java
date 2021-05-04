package org.parker.mips.gui.components;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTree;
import org.parker.mips.util.FileUtils;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FlatFileTree extends FlatTree {

    public FlatFileTree(File file) throws FileNotFoundException {

        this.setModel(new FileTreeModel(file));

        this.setCellRenderer(new IconFileTreeRenderer());

        //addTreeExpansionListener(new TreeExpansionHandler());

        this.addMouseListener(new AddingFileTHing());
    }

    public FlatFileTree(String path) throws FileNotFoundException{
        this(new File(path));
    }

    // Returns the full pathname for a path, or null
// if not a known path
    public String getPathName(TreePath path) {
        Object o = path.getLastPathComponent();
        if (o instanceof FileTreeNode) {
            return ((FileTreeNode)o).file.getAbsolutePath();
        } return null;
    }

    // Returns the File for a path, or null if not a known path
    public File getFile(TreePath path) {
        Object o = path.getLastPathComponent();
        if (o instanceof FileTreeNode) {
            return ((FileTreeNode)o).file;
        }
        return null;
    }


     static class IconFileTreeRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            //if (true) return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            File file = (File) value;

            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, file.getName(), sel, expanded, leaf, row, hasFocus);

            Icon icon = null;

            switch (FileUtils.getExtension(file.getAbsolutePath())) {
                case "mxn":
                case "bin":
                    icon = new FlatSVGIcon("Images/Icons/SVG/Files/binary.svg", 16, 16);
                    break;
                case "h":
                    icon = new FlatSVGIcon("Images/Icons/SVG/Files/h.svg", 16, 16);
                    break;
                case "asm":
                    icon = new FlatSVGIcon("Images/Icons/SVG/Files/asm.svg", 16, 16);
                    break;
                default:
                    break;
            }

            if (icon != null) {
                label.setIcon(icon);
            }

            return label;
        }
    }

    //mouse
    static class AddingFileTHing extends MouseAdapter {
        private void myPopupEvent(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            JTree tree = (JTree) e.getSource();
            TreePath path = tree.getPathForLocation(x, y);
            if (path == null)
                return;

            tree.setSelectionPath(path);

            String obj = path.getLastPathComponent().toString();

            String label = "popup: " + obj;//obj.getTreeLabel();
            JPopupMenu popup = new JPopupMenu();
            popup.add(new JMenuItem(label));
            popup.show(tree, x, y);
        }

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) myPopupEvent(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) myPopupEvent(e);
        }
    }

    //
    //
    //generates tree model
    class FileTreeModel implements TreeModel {

        /**
         * Creating an object of this class and setting its root to the provided
         * File object.
         * <p>
         * The root is the highest directory available in an object of this class.
         *
         * @param file - an object of type File, giving the root directory for an
         *             object of type FileTreeModel.
         */


        public FileTreeModel(File file) {
            this.root = file;

        }

        @Override
        public Object getRoot() {
            return this.root;
        }

        @Override
        public Object getChild(Object parent, int index) {
            File f = (File) parent;
            return listFiles(f).get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            File f = (File) parent;

            try {
                if (!f.isDirectory() && f.list() != null) {
                    return 0;
                } else {
                    return f.list().length;
                }
            } catch (NullPointerException ex) {
                return 0;
            }
        }

        @Override
        public boolean isLeaf(Object node) {
            File f = (File) node;
            return !f.isDirectory();
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            File par = (File) parent;
            File ch = (File) child;
            return listFiles(par).indexOf(ch);
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub
        }

        List<File> listFiles(File file){
            List<File> list = Arrays.asList(file.listFiles());
            list.sort((o1, o2) -> {
                if(o1.isFile() && o2.isFile()){
                    return 0;
                }else if(o1.isDirectory() && o2.isDirectory()){
                    return 0;
                }else if(o1.isDirectory() && o2.isFile()){
                    return -1;
                }else if(o1.isFile() && o2.isDirectory()){
                    return 1;
                }
                return 0;
            });
            return list;
        }

        private File root;
    }

    // Inner class that represents a node in this
// file system tree
    protected static class FileTreeNode extends DefaultMutableTreeNode {

        public FileTreeNode(File parent, String name)
                throws SecurityException, IOException {
            this.name = name;

            // See if this node exists and whether it
            // is a directory
            file = new File(parent, name);
            if (!file.exists()) {
                throw new FileNotFoundException("File " + name + " does not exist");
            }

            isDir = file.isDirectory();
            if(isDir){
                file.toPath().register(new WatchService() {
                    @Override
                    public void close()  {

                    }

                    @Override
                    public WatchKey poll() {
                        return null;
                    }

                    @Override
                    public WatchKey poll(long timeout, TimeUnit unit) throws InterruptedException {
                        return null;
                    }

                    @Override
                    public WatchKey take() throws InterruptedException {
                        return null;
                    }
                }, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            }

            // Hold the File as the user object.
            setUserObject(file);

        }

        // Override isLeaf to check whether this is a directory
        public boolean isLeaf() {
            return !isDir;
        }

        // Override getAllowsChildren to check whether
// this is a directory
        public boolean getAllowsChildren() {
            return isDir;
        }

        // For display purposes, we return our own name public String toString() { return name; }
// If we are a directory, scan our contents and populate
// with children. In addition, populate those children
// if the "descend" flag is true. We only descend once,
// to avoid recursing the whole subtree.
// Returns true if some nodes were added
        boolean populateDirectories(boolean descend) {
            boolean addedNodes = false;
// Do this only once
            if (populated == false) {
                if (interim == true) {
                    // We have had a quick look here before:
                    // remove the dummy node that we added last time
                    removeAllChildren();
                    interim = false;
                }

                String[] names = file.list();// Get list of contents

                // Process the directories
                for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    File d = new File(file, name);
                    try {
                        if (d.isDirectory()) {
                            FileTreeNode node =
                                    new FileTreeNode(file, name);
                            this.add(node);
                            if (descend) {
                                node.populateDirectories(false);
                            }
                            addedNodes = true;
                            if (descend == false) {
                                // Only add one node if not descending
                                break; }
                        }
                    } catch (Throwable t) {
                        // Ignore phantoms or access problems
                    }
                }

                // If we were scanning to get all subdirectories,
                // or if we found no subdirectories, there is no
                // reason to look at this directory again, so
                // set populated to true. Otherwise, we set interim
                // so that we look again in the future if we need to
                if (descend == true || addedNodes == false) {
                    populated = true;
                } else {
                    // Just set interim state
                    interim = true;
                }
            }
            return addedNodes;
        }

        protected File file;// File object for this node
        protected String name;// Name of this node
        protected boolean populated;
        // true if we have been populated
        protected boolean interim;
        // true if we are in interim state
        protected boolean isDir;// true if this is a directory
    }

}
