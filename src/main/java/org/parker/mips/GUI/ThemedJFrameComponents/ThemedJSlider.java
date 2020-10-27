/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import org.parker.mips.Holder;

/**
 *
 * @author parke
 */
public class ThemedJSlider extends JSlider implements ThemableComponent {

    public ThemedJSlider() {
        this.setUI(new ThemedSliderUI(this));
        this.setOpaque(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Holder<Integer> value;

    @Override
    public void setValue(int value) {
        if (this.value != null) {
            this.value.value = value;
        } else {
            super.setValue(value);
        }
    }

    public void setValue(Holder<Integer> value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        if (this.value != null) {
            return this.value.value;
        } else {
            return super.getValue();
        }
    }

}

class ThemedSliderUI extends BasicSliderUI implements ThemableComponent {

    private static Color thumbColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME);
    private static Color backgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME);
    private static Color sliderTrackOn = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME);
    private static Color sliderTrackOff = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME);

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME:
                backgroundColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME:
                thumbColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME:
                sliderTrackOn = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME:
                sliderTrackOff = (Color) pce.getNewValue();
                break;
        }
    }

    private static final int TRACK_HEIGHT = 8;
    private static final int TRACK_WIDTH = 8;
    private static final int TRACK_ARC = 5;
    private static final Dimension THUMB_SIZE = new Dimension(14, 14);
    private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();

    public ThemedSliderUI(final JSlider b) {
        super(b);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME, this);
    }

    @Override
    protected void calculateTrackRect() {
        super.calculateTrackRect();
        if (isHorizontal()) {
            trackRect.y = trackRect.y + (trackRect.height - TRACK_HEIGHT) / 2;
            trackRect.height = TRACK_HEIGHT;
        } else {
            trackRect.x = trackRect.x + (trackRect.width - TRACK_WIDTH) / 2;
            trackRect.width = TRACK_WIDTH;
        }
        trackShape.setRoundRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height, TRACK_ARC, TRACK_ARC);
    }

    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (isHorizontal()) {
            thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
        } else {
            thumbRect.x = trackRect.x + (trackRect.width - thumbRect.width) / 2;
        }
    }

    @Override
    protected Dimension getThumbSize() {
        return THUMB_SIZE;
    }

    private boolean isHorizontal() {
        return slider.getOrientation() == JSlider.HORIZONTAL;
    }

    @Override
    public void paint(final Graphics g, final JComponent c) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @Override
    public void paintTrack(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Shape clip = g2.getClip();

        boolean horizontal = isHorizontal();
        boolean inverted = slider.getInverted();

//        // Paint shadow.
//        g2.setColor(backGroundColor);
//        g2.fill(trackShape);
        // Paint track background.
        g2.setColor(sliderTrackOff);
        g2.setClip(trackShape);
        trackShape.y += 1;
        g2.fill(trackShape);
        trackShape.y = trackRect.y;

        g2.setClip(clip);

        // Paint selected track.
        if (horizontal) {
            boolean ltr = slider.getComponentOrientation().isLeftToRight();
            if (ltr) {
                inverted = !inverted;
            }
            int thumbPos = thumbRect.x + thumbRect.width / 2;
            if (inverted) {
                g2.clipRect(0, 0, thumbPos, slider.getHeight());
            } else {
                g2.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
            }

        } else {
            int thumbPos = thumbRect.y + thumbRect.height / 2;
            if (inverted) {
                g2.clipRect(0, 0, slider.getHeight(), thumbPos);
            } else {
                g2.clipRect(0, thumbPos, slider.getWidth(), slider.getHeight() - thumbPos);
            }
        }
        g2.setColor(sliderTrackOn);
        g2.fill(trackShape);
        g2.setClip(clip);
    }

    @Override
    public void paintThumb(final Graphics g) {
        g.setColor(thumbColor);
        g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
    }

}
