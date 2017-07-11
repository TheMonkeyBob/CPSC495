package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

/**
 * Created by TheMonkeyBob on 2017-07-11.
 */
public class ImageDisplayTruePanel extends JPanel
{
    private int myNumber;
    private GuiManager manager;
    private TabPanel parent_panel;
    private ImageDisplay display;

    private JPanel panel_Contrast;
    private JPanel panel_Zoom;
    private JSlider slider_Contrast;
    private JButton button_Minus;
    private JButton button_Plus;
    private JLabel label_zoom;

    public ImageDisplayTruePanel(int num, GuiManager manager, TabPanel parent)
    {
        myNumber = num;
        this.manager = manager;
        this.parent_panel = parent;
        this.setLayout(null);
        setup();
    }

    private void setup()
    {
        display = new ImageDisplay(myNumber, manager, this);

        panel_Contrast = new JPanel();
        panel_Zoom = new JPanel();
        slider_Contrast = new JSlider();
        slider_Contrast.setMinimum(0);
        int inputContrast = 1000;
        //slider_Contrast.setMajorTickSpacing(inputContrast/4);
        //slider_Contrast.setMinorTickSpacing(inputContrast/8);
        //slider_Contrast.createStandardLabels(inputContrast/4);
        slider_Contrast.setMaximum(inputContrast);
        slider_Contrast.setValue(100);
        slider_Contrast.addChangeListener(ContrastSliderChange -> contrastSlider_stateChanged());
        panel_Contrast.add(slider_Contrast);

        button_Minus = new JButton();
        button_Minus.setIcon(ImgManager.Minus_Up);
        button_Minus.setDisabledIcon(ImgManager.Minus_Down);
        button_Minus.setDisabledSelectedIcon(ImgManager.Minus_Down);
        button_Minus.setPressedIcon(ImgManager.Minus_Down);
        button_Minus.setRolloverIcon(ImgManager.Minus_Up);
        button_Minus.setRolloverSelectedIcon(ImgManager.Minus_Up);
        button_Minus.setSelectedIcon(ImgManager.Minus_Up);
        button_Minus.setBorder(BorderFactory.createEmptyBorder());
        button_Minus.addActionListener(MinusButtonAction -> this_MinusButtonAction());
        label_zoom = new JLabel("100%");
        button_Plus = new JButton();
        button_Plus.setIcon(ImgManager.Plus_Up);
        button_Plus.setDisabledIcon(ImgManager.Plus_Down);
        button_Plus.setDisabledSelectedIcon(ImgManager.Plus_Down);
        button_Plus.setPressedIcon(ImgManager.Plus_Down);
        button_Plus.setRolloverIcon(ImgManager.Plus_Up);
        button_Plus.setRolloverSelectedIcon(ImgManager.Plus_Up);
        button_Plus.setSelectedIcon(ImgManager.Plus_Up);
        button_Plus.setBorder(BorderFactory.createEmptyBorder());
        button_Plus.addActionListener(PlusButtonAction -> this_PlusButtonAction());
        panel_Zoom.add(button_Minus);
        panel_Zoom.add(label_zoom);
        panel_Zoom.add(button_Plus);

        this.add(display);
        this.add(panel_Contrast);
        this.add(panel_Zoom);
    }

    int zoomText = 100;
    double zoom =  1.0;
    private void this_MinusButtonAction()
    {
        if (zoom >= 0.2)
        {
            zoom -= 0.1;
            zoomText -= 10;
            label_zoom.setText(zoomText + "%");
            display.setMagnification(zoom);
        }
    }

    private void this_PlusButtonAction()
    {
        if (zoom <= 9.9)
        {
            zoom += 0.1;
            zoomText += 10;
            label_zoom.setText(zoomText + "%");
            display.setMagnification(zoom);
        }
    }

    public void addGrid()
    {
        display.addGrid();
    }

    public void coordinateFound(int x, int y)
    {
        parent_panel.coordinateFound(x, y);
    }

    public void setGridMode(int i)
    {
        display.setGridMode(i);
    }

    public void zoomToCurrentGrid()
    {
        display.zoomToCurrentGrid();
    }

    public void setCurrentGrid(int grid)
    {
        display.setCurrentGrid(grid);
    }

    public void removeCurrentGrid()
    {
        display.removeCurrentGrid();
    }

    public void reSize(int w, int h)
    {
        display.setBounds(0, 0, w, h - 30);
        panel_Contrast.setBounds(0, display.getHeight(), w/2, 30);
        panel_Zoom.setBounds(panel_Contrast.getWidth(), display.getHeight(), w/2, 30);
        repaint();
    }

    private void contrastSlider_stateChanged()
    {
        if(!slider_Contrast.getValueIsAdjusting()){
            display.changeContrast(slider_Contrast.getValue());
        }
    }
}
