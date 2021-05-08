/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.assembleride.gui.userpanes.hexeditor;

import org.parker.retargetableassembler.util.Memory;

import javax.swing.*;
import java.awt.*;

public class EditableMemoryHexGrid extends JPanel {

    private JScrollBar scrollBar;
    private MemoryEditGrid mGrid;
    private int currentScroll = 0;

    private Memory mem;

    private boolean bigEndianness = true;

    public EditableMemoryHexGrid(int rows, int columns, EditableHexGrid.GroupSize groupSize, Memory mem){

        this.mem = mem;

        mGrid = new MemoryEditGrid(rows, columns, groupSize);

        scrollBar = new JScrollBar();
        scrollBar.setOrientation(JScrollBar.VERTICAL);
        scrollBar.setMaximum(0);
        scrollBar.setMaximum((1 << (32 - (int)Math.log((columns - 1) * 8 * groupSize.value))) - (rows - 1));
        scrollBar.setUnitIncrement(1);
        scrollBar.addAdjustmentListener(e -> {
            //this.dispatchEvent(e);
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

    protected void updateThings(int rows, int columns, EditableHexGrid.GroupSize groupSize){

        SwingUtilities.invokeLater(() -> {
            this.remove(mGrid);
            mGrid = new MemoryEditGrid(rows, columns, groupSize);
            this.add(mGrid, BorderLayout.CENTER);

            //the new grid wont be drawn until the next draw cycle so a event needs to be fired
            //problem is i cant get it to fire the event manually so here we are
            int scrol = currentScroll;
            scrollBar.setValue(scrol + 1);
            scrollBar.setValue(scrol);
            });
    }

    public int getColumns(){
        return mGrid.columns;
    }
    public int getRows(){
        return mGrid.rows;
    }
    public EditableHexGrid.GroupSize getGroupSize(){
        return mGrid.groupSize;
    }


    protected void updateValues(){
        mGrid.refreshValues();
        this.repaint();
    }

    public void scrollToAddress(int value) {
        scrollBar.setValue(value / ((mGrid.columns - 1) * getGroupSize().value));
    }

    public void setBigEndian(boolean selected) {
        bigEndianness = selected;
        mGrid.refreshValues();
        repaint();
    }

    private class MemoryEditGrid extends EditableHexGrid {

        private long carrotPosition = 0;

        private MemoryEditGrid(int rows, int columns, GroupSize groupSize) {
            super(rows, columns, -1, -1, 4, 2, i -> i/columns > 0, groupSize);

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
                labels[i * columns].setText(String.format("%08X", groupSize.value * ((columns - 1) * currentScroll + (i - 1) * (columns - 1))));
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
                    value = 0xFF & mem.getByte(index);
                    break;
                case HalfWord:
                    value = 0xFFFF & mem.getShort(index, bigEndianness);
                    break;
                case Word:
                    value = mem.getWord(index,bigEndianness);
                    break;
            }
                return String.format("%0"+ groupSize.value * 2 +"X", value);
        }


        @Override
        public void commitModify(int newValue) {
            int index = groupSize.value * (currentScroll * (columns - 1) + ((currModify - columns)*(columns - 1))/columns);

            switch(groupSize) {
                case Byte:
                    mem.setByte(index, (byte) newValue);
                    break;
                case HalfWord:
                    mem.setShort(index, (short) newValue, bigEndianness);
                    break;
                case Word:
                    mem.setWord(index, newValue,bigEndianness);
                    break;
            }

            this.labels[currModify].setText(getStringFromIndex(index));
        }
    }

}