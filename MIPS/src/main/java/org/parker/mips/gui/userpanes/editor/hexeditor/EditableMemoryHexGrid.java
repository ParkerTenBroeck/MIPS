package org.parker.mips.gui.userpanes.editor.hexeditor;


import org.parker.mips.emulator.Memory;

import javax.swing.*;
import java.awt.*;

public class EditableMemoryHexGrid extends JPanel {

    private JScrollBar scrollBar;
    private MemoryEditGrid mGrid;
    private int currentScroll = 0;

    public EditableMemoryHexGrid(){

        mGrid = new MemoryEditGrid();

        scrollBar = new JScrollBar();
        scrollBar.setOrientation(JScrollBar.VERTICAL);
        scrollBar.setMaximum(0);
        scrollBar.setMaximum(0x08000000 - 8);
        scrollBar.setUnitIncrement(1);
        scrollBar.addAdjustmentListener(e -> {
            //this.dispatchEvent(e);
            System.out.println(e);
            if(mGrid.currModify != -1){
                return;
            }
            currentScroll = e.getValue();//e.getUnitsToScroll() / 4;
            if(currentScroll < 0){
                currentScroll = 0;
            }
            scrollBar.setValue(currentScroll);
            mGrid.refreshValues();
            this.repaint();
        });
        this.addMouseWheelListener(e -> {
            int newVal = scrollBar.getValue() + e.getUnitsToScroll() / 4;
            if(newVal < 0 ){
                newVal = 0;
            }
            if(newVal > scrollBar.getMaximum()){
                newVal = scrollBar.getMaximum();
            }
            scrollBar.setValue(newVal);
        });
        this.setLayout( new BorderLayout());
        this.add(scrollBar, BorderLayout.EAST);
        this.add(mGrid, BorderLayout.CENTER);
        //this.add(scrollBar);
    }

    protected void updateValues(){
        mGrid.refreshValues();
        this.repaint();
    }

    private class MemoryEditGrid extends EditableHexGrid {

        private MemoryEditGrid() {
            super(20, 17, -1, -1, 4, 2, i -> i/9 > 0, GroupSize.HalfWord);

            refreshValues();

            for(int i = 1; i < columns; i ++){
                this.labels[i].setText(String.format("%0"+ groupSize.value * 2 + "X", (i - 1) * groupSize.value));
            }

            { //this is the dumbest shit ive ever done
                FontMetrics fm = this.getFontMetrics(this.labels[0].getFont());
                int size = 0;
                for (int i = 0; i <= 10; i++) {
                    if (fm.charWidth((char) (i + 0x30)) > size) {
                        size = fm.charWidth((char) (i + 0x30));
                    }
                }
                for (int i = 0; i <= 6; i++) {
                    if (fm.charWidth((char) (i + 0x41)) > size) {
                        size = fm.charWidth((char) (i + 0x41));
                    }
                }
                for (int i = 0; i < columns * rows; i++) {
                    //this.labels[i].setFont(new Font("monospaced", this.labels[i].getFont().getStyle(), this.labels[i].getFont().getSize()));
                    this.labels[i].setPreferredSize(new Dimension(size * labels[i].getText().length(), 1));
                }
            }
        }
        public void refreshValues(){

            for(int i = 1; i < rows; i ++){
                labels[i * columns].setText(String.format("%08X",  (currentScroll * (columns - 1) + i - 1) * groupSize.value));
            }
            //long start = System.currentTimeMillis();
            for(int r = 1; r < rows; r ++) {
                int rr = r * columns;
                int ri = (r - 1) * (columns - 1) + currentScroll * (columns - 1);
                for (int c = 1; c < (columns); c++) {
                    int index = (ri + c - 1) * groupSize.value;

                    this.labels[c + rr].setText(getStringFromIndex(index));
                }
            }
            //long end = System.currentTimeMillis();
            //System.out.println(end-start);
        }

        private String getStringFromIndex(int index){
            int value = 0;
            switch(groupSize) {
                case Byte:
                    value = 0xFF & Memory.superGetByte(index);
                    break;
                case HalfWord:
                    value = 0xFFFF & Memory.superGetHalfWord(index);
                    break;
                case Word:
                    value = Memory.superGetWord(index);
                    break;
            }
                return String.format("%0"+ groupSize.value * 2 +"X", value);
        }


        @Override
        public void commitModify(int newValue) {
            int index = groupSize.value * (currentScroll * (columns - 1) + ((currModify - columns)*(columns - 1))/columns);
            Memory.superSetByte(index,newValue);

            this.labels[currModify].setText(getStringFromIndex(index));
        }
    }

}