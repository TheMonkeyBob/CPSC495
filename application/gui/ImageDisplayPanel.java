package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Lukas Pihl
 */
public class ImageDisplayPanel extends JPanel {
    private int myNumber;
    private GuiManager manager;
    private ImageDisplay imageDisplay;

    private JPanel panel_Contrast;
    private JPanel panel_Zoom;
    private JSlider slider_Contrast;
    private JButton button_Minus;
    private JButton button_Plus;
    private JLabel label_zoom;
    private JScrollPane scrollPane_Image;

    public ImageDisplayPanel(GuiManager manager, int num)
    {
        myNumber = num;
        this.manager = manager;
        this.setLayout(null);
        setup();
    }

    private void setup()
    {
        imageDisplay = new ImageDisplay(manager, myNumber);
        scrollPane_Image = new JScrollPane(imageDisplay);
        scrollPane_Image.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_Image.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        if (!scrollPane_Image.getComponentOrientation().isLeftToRight())
        {
            scrollPane_Image.getViewport().setViewPosition(new Point(Integer.MAX_VALUE, 0));
        }
        MouseAdapter ma = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e)
            {
                this_mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e)
            {
                this_mouseDragged(e);
            }
        };
        scrollPane_Image.getViewport().addMouseListener(ma);
        scrollPane_Image.getViewport().addMouseMotionListener(ma);

        panel_Contrast = new JPanel();
        panel_Zoom = new JPanel();
        slider_Contrast = new JSlider();
        slider_Contrast.setMinimum(0);
        int inputContrast = 1000;
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

        scrollPane_Image.getViewport().add(imageDisplay);
        imageDisplay.addScrollBars(scrollPane_Image.getHorizontalScrollBar(), scrollPane_Image.getVerticalScrollBar());
        this.add(scrollPane_Image);
        this.add(panel_Contrast);
        this.add(panel_Zoom);
    }

    int zoomText = 100;
    double zoom = 1.0;

    private void this_MinusButtonAction() {
        if (zoom >= 0.2) {
            zoom -= 0.1;
            zoomText -= 10;
            label_zoom.setText(zoomText + "%");
            imageDisplay.setMagnification(zoom);
        }
    }

    private void this_PlusButtonAction() {
        if (zoom <= 9.9) {
            zoom += 0.1;
            zoomText += 10;
            label_zoom.setText(zoomText + "%");
            imageDisplay.setMagnification(zoom);
        }
    }

    public void addGrid()
    {
        imageDisplay.addGrid();
    }

    public void setGridMode(int i) {
        imageDisplay.setGridMode(i);
    }

    public void setCurrentGrid(int grid) {
        imageDisplay.setCurrentGrid(grid);
    }

    public void removeCurrentGrid() {
        imageDisplay.removeCurrentGrid();
    }

    public void reSize(int w, int h) {
        scrollPane_Image.setBounds(0, 0, w, h - 30);
        panel_Contrast.setBounds(0, scrollPane_Image.getHeight(), w / 2, 30);
        panel_Zoom.setBounds(panel_Contrast.getWidth(), scrollPane_Image.getHeight(), w / 2, 30);
        repaint();
    }

    private void contrastSlider_stateChanged() {
        if (!slider_Contrast.getValueIsAdjusting()) {
            imageDisplay.changeContrast(slider_Contrast.getValue());
        }
    }

    private int mode = SharedData.GRIDMODE_NORMAL;
    private int x_last = 0;
    private int y_last = 0;
    private boolean refreshing = false;

    private void this_mousePressed(MouseEvent e)
    {
        x_last = e.getX();
        y_last = e.getY();
        System.out.println("SPAT");
    }

    private void this_mouseDragged(MouseEvent e)
    {
        switch (mode)
        {
            case SharedData.GRIDMODE_NORMAL:
                System.out.println("SPIT");
                int deltaX = x_last - e.getX();
                int deltaY = y_last - e.getY();
                Point point = scrollPane_Image.getViewport().getViewPosition();
                int beforeX = point.x;
                int beforeY = point.y;
                if (!refreshing)
                {
                    scrollPane_Image.getVerticalScrollBar().setValue(
                            scrollPane_Image.getVerticalScrollBar().getValue() + deltaY);
                    scrollPane_Image.getHorizontalScrollBar().setValue(
                            scrollPane_Image.getHorizontalScrollBar().getValue() + deltaX);
                }
                int afterX = point.x;
                int afterY = point.y;
                x_last += beforeX - afterX;
                y_last += beforeY - afterY;
                break;
            default:
                break;
        }
        refreshing = !refreshing;
    }

    public void zoomToCurrentGrid()
    {
        int x = 1000000000;
        int y = 1000000000;
        Polygon p = manager.getSample_Grid_Polygon_Outline(myNumber, manager.getSample_CurrentGridNum(myNumber));
        for (int i: p.xpoints)
        {
            if (i < x)
            {
                x = i;
            }
        }
        for (int i: p.ypoints)
        {
            if (i < y)
            {
                y = i;
            }
        }
        scrollPane_Image.getVerticalScrollBar().setValue(y - 10);
        scrollPane_Image.getHorizontalScrollBar().setValue(x - 10);
    }

    public void removeGrid(int grid)
    {
        imageDisplay.removeGrid(grid);
    }

    public void reloadGrid(int grid)
    {
        imageDisplay.reloadGrid(grid);
    }
}
