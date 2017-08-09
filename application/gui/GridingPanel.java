package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class GridingPanel extends JPanel
{
    private JButton button_AddGrid;
    private JButton button_RemoveGrid;
    private JButton button_Settings;
    private JButton button_SaveGrids;
    private JButton button_LoadGrids;
    private JButton button_MoveGridTo;
    private JButton button_MoveGrid;
    private JButton button_RotateGrid;
    private JButton button_AdvancedSettings;
    private JButton button_ResizeGrid;
    private JButton button_ZoomToGrid;
    private JButton button_MultiAddGrid;
    private JButton button_MultiMoveGrid;
    private JPanel panel_Grids;
    private JScrollPane scrollPane_Grids;

    private JButton button_Cancel;
    private JLabel label_ClickTopLeft;
    private JLabel label_ClickTopRight;
    private JLabel label_ClickBottom;
    private JLabel label_Rows;
    private JLabel label_Columns;
    private JTextField textField_Rows;
    private JTextField textField_Columns;

    private final int X_START = 10; //Horizontal start padding for the layout
    private final int Y_START = 20; //Vertical start padding for the layout
    private int mode = SharedData.GRIDMODE_NORMAL;
    private int grid_setup_step = 0;
    private int current_grid = -1;
    private int topLeftX;
    private int topLeftY;
    private int topRightX;
    private int topRightY;
    private int bottomX;
    private int bottomY;
    private int rows;
    private int columns;

    private ArrayList<GridPanel> grid_list;
    private GridPanel new_gp;
    private GuiManager manager;
    private int myNumber;

    /**
     * Create a new GridingPanel.
     */
    public GridingPanel(GuiManager manager, int num)
    {
        this.manager = manager;
        this.myNumber = num;
        setup();
    }

    /**
     * Sets up all the components and settings required for the GridingPanel.
     */
    private void setup()
    {
        grid_list = new ArrayList<>();
        new_gp = null;

        Border border_BlackLine = BorderFactory.createLineBorder(Color.black);
        TitledBorder grid_title = BorderFactory.createTitledBorder(border_BlackLine, "Gridding");
        grid_title.setTitleJustification(TitledBorder.LEFT);
        this.setBorder(grid_title);
        this.setLayout(null);

        //Setup Zoom To Grid button
        button_ZoomToGrid = new JButton();
        button_ZoomToGrid.setIcon(ImgManager.GridZoomToButton_Up);
        button_ZoomToGrid.setDisabledIcon(ImgManager.GridZoomToButton_Down);
        button_ZoomToGrid.setDisabledSelectedIcon(ImgManager.GridZoomToButton_Down);
        button_ZoomToGrid.setPressedIcon(ImgManager.GridZoomToButton_Down);
        button_ZoomToGrid.setRolloverIcon(ImgManager.GridZoomToButton_Up);
        button_ZoomToGrid.setRolloverSelectedIcon(ImgManager.GridZoomToButton_Up);
        button_ZoomToGrid.setSelectedIcon(ImgManager.GridZoomToButton_Up);
        button_ZoomToGrid.setBounds(X_START, Y_START, 30, 30);
        button_ZoomToGrid.setBorder(BorderFactory.createEmptyBorder());
        button_ZoomToGrid.addActionListener(ZoomToGridButtonAction -> this_ZoomToGridButtonAction());
        button_ZoomToGrid.setToolTipText("Zoom to grid");
        this.add(button_ZoomToGrid);

        //Setup Move Grid button
        button_MoveGrid = new JButton();
        button_MoveGrid.setIcon(ImgManager.GridMoveButton_Up);
        button_MoveGrid.setDisabledIcon(ImgManager.GridMoveButton_Down);
        button_MoveGrid.setDisabledSelectedIcon(ImgManager.GridMoveButton_Down);
        button_MoveGrid.setPressedIcon(ImgManager.GridMoveButton_Down);
        button_MoveGrid.setRolloverIcon(ImgManager.GridMoveButton_Up);
        button_MoveGrid.setRolloverSelectedIcon(ImgManager.GridMoveButton_Up);
        button_MoveGrid.setSelectedIcon(ImgManager.GridMoveButton_Up);
        button_MoveGrid.setBounds(button_ZoomToGrid.getX() + button_ZoomToGrid.getWidth() + 5, Y_START, 30, 30);
        button_MoveGrid.setBorder(BorderFactory.createEmptyBorder());
        button_MoveGrid.addActionListener(MoveGridButtonAction -> this_MoveGridButtonAction());
        button_MoveGrid.setToolTipText("Move grid");
        this.add(button_MoveGrid);

        //Setup Rotate Grid button
        button_RotateGrid = new JButton();
        button_RotateGrid.setIcon(ImgManager.GridRotateButton_Up);
        button_RotateGrid.setDisabledIcon(ImgManager.GridRotateButton_Down);
        button_RotateGrid.setDisabledSelectedIcon(ImgManager.GridRotateButton_Down);
        button_RotateGrid.setPressedIcon(ImgManager.GridRotateButton_Down);
        button_RotateGrid.setRolloverIcon(ImgManager.GridRotateButton_Up);
        button_RotateGrid.setRolloverSelectedIcon(ImgManager.GridRotateButton_Up);
        button_RotateGrid.setSelectedIcon(ImgManager.GridRotateButton_Up);
        button_RotateGrid.setBounds(button_MoveGrid.getX() + button_MoveGrid.getWidth() + 5, Y_START, 30, 30);
        button_RotateGrid.setBorder(BorderFactory.createEmptyBorder());
        button_RotateGrid.addActionListener(RotateGridButtonAction -> this_RotateGridButtonAction());
        button_RotateGrid.setToolTipText("Rotate grid");
        this.add(button_RotateGrid);

        //Setup Resize Grid button
        button_ResizeGrid = new JButton();
        button_ResizeGrid.setIcon(ImgManager.GridResizeButton_Up);
        button_ResizeGrid.setDisabledIcon(ImgManager.GridResizeButton_Down);
        button_ResizeGrid.setDisabledSelectedIcon(ImgManager.GridResizeButton_Down);
        button_ResizeGrid.setPressedIcon(ImgManager.GridResizeButton_Down);
        button_ResizeGrid.setRolloverIcon(ImgManager.GridResizeButton_Up);
        button_ResizeGrid.setRolloverSelectedIcon(ImgManager.GridResizeButton_Up);
        button_ResizeGrid.setSelectedIcon(ImgManager.GridResizeButton_Up);
        button_ResizeGrid.setBounds(button_RotateGrid.getX() + button_RotateGrid.getWidth() + 5, Y_START, 30, 30);
        button_ResizeGrid.setBorder(BorderFactory.createEmptyBorder());
        button_ResizeGrid.addActionListener(ResizeGridButtonAction -> this_ResizeGridButtonAction());
        button_ResizeGrid.setToolTipText("Resize grid");
        this.add(button_ResizeGrid);

        //Setup Move Grid To button
        button_MoveGridTo = new JButton();
        button_MoveGridTo.setIcon(ImgManager.GridMoveToButton_Up);
        button_MoveGridTo.setDisabledIcon(ImgManager.GridMoveToButton_Down);
        button_MoveGridTo.setDisabledSelectedIcon(ImgManager.GridMoveToButton_Down);
        button_MoveGridTo.setPressedIcon(ImgManager.GridMoveToButton_Down);
        button_MoveGridTo.setRolloverIcon(ImgManager.GridMoveToButton_Up);
        button_MoveGridTo.setRolloverSelectedIcon(ImgManager.GridMoveToButton_Up);
        button_MoveGridTo.setSelectedIcon(ImgManager.GridMoveToButton_Up);
        button_MoveGridTo.setBounds(button_ResizeGrid.getX() + button_ResizeGrid.getWidth() + 5, Y_START, 30, 30);
        button_MoveGridTo.setBorder(BorderFactory.createEmptyBorder());
        button_MoveGridTo.setToolTipText("Move grid to");
        button_MoveGridTo.addActionListener(MoveGridToButtonAction -> listen_MoveGridToButtonAction());
        this.add(button_MoveGridTo);

        //Setup Advanced Grid Settings button
        button_AdvancedSettings = new JButton();
        button_AdvancedSettings.setIcon(ImgManager.GridAdvancedButton_Up);
        button_AdvancedSettings.setDisabledIcon(ImgManager.GridAdvancedButton_Down);
        button_AdvancedSettings.setDisabledSelectedIcon(ImgManager.GridAdvancedButton_Down);
        button_AdvancedSettings.setPressedIcon(ImgManager.GridAdvancedButton_Down);
        button_AdvancedSettings.setRolloverIcon(ImgManager.GridAdvancedButton_Up);
        button_AdvancedSettings.setRolloverSelectedIcon(ImgManager.GridAdvancedButton_Up);
        button_AdvancedSettings.setSelectedIcon(ImgManager.GridAdvancedButton_Up);
        button_AdvancedSettings.setBounds(button_MoveGridTo.getX() + button_MoveGridTo.getWidth() + 5, Y_START, 30, 30);
        button_AdvancedSettings.setBorder(BorderFactory.createEmptyBorder());
        button_AdvancedSettings.setToolTipText("Set grid aspects");
        button_AdvancedSettings.addActionListener(AdvancedSettingsButtonAction -> this_AdvancedSettingsButtonAction());
        this.add(button_AdvancedSettings);

        //Setup Add Grid button
        button_AddGrid = new JButton();
        button_AddGrid.setIcon(ImgManager.GridAddButton_Up);
        button_AddGrid.setDisabledIcon(ImgManager.GridAddButton_Down);
        button_AddGrid.setDisabledSelectedIcon(ImgManager.GridAddButton_Down);
        button_AddGrid.setPressedIcon(ImgManager.GridAddButton_Down);
        button_AddGrid.setRolloverIcon(ImgManager.GridAddButton_Up);
        button_AddGrid.setRolloverSelectedIcon(ImgManager.GridAddButton_Up);
        button_AddGrid.setSelectedIcon(ImgManager.GridAddButton_Up);
        button_AddGrid.setBounds(button_AdvancedSettings.getX() + button_AdvancedSettings.getWidth() + 25, Y_START, 30,
                30);
        button_AddGrid.setBorder(BorderFactory.createEmptyBorder());
        button_AddGrid.addActionListener(AddGridButtonAction -> this_AddGridButtonAction());
        button_AddGrid.setToolTipText("Add grid");
        this.add(button_AddGrid);

        //Setup Remove Grid button
        button_RemoveGrid = new JButton();
        button_RemoveGrid.setIcon(ImgManager.GridRemoveButton_Up);
        button_RemoveGrid.setDisabledIcon(ImgManager.GridRemoveButton_Down);
        button_RemoveGrid.setDisabledSelectedIcon(ImgManager.GridRemoveButton_Down);
        button_RemoveGrid.setPressedIcon(ImgManager.GridRemoveButton_Down);
        button_RemoveGrid.setRolloverIcon(ImgManager.GridRemoveButton_Up);
        button_RemoveGrid.setRolloverSelectedIcon(ImgManager.GridRemoveButton_Up);
        button_RemoveGrid.setSelectedIcon(ImgManager.GridRemoveButton_Up);
        button_RemoveGrid.setBounds(button_AddGrid.getX() + button_AddGrid.getWidth() + 5, Y_START, 30, 30);
        button_RemoveGrid.setBorder(BorderFactory.createEmptyBorder());
        button_RemoveGrid.addActionListener(RemoveGridButtonAction -> this_RemoveGridButtonAction());
        button_RemoveGrid.setToolTipText("Remove selected grid");
        this.add(button_RemoveGrid);

        //Setup Griding Settings button
        button_Settings = new JButton();
        button_Settings.setIcon(ImgManager.GridSettingsButton_Up);
        button_Settings.setDisabledIcon(ImgManager.GridSettingsButton_Down);
        button_Settings.setDisabledSelectedIcon(ImgManager.GridSettingsButton_Down);
        button_Settings.setPressedIcon(ImgManager.GridSettingsButton_Down);
        button_Settings.setRolloverIcon(ImgManager.GridSettingsButton_Up);
        button_Settings.setRolloverSelectedIcon(ImgManager.GridSettingsButton_Up);
        button_Settings.setSelectedIcon(ImgManager.GridSettingsButton_Up);
        button_Settings.setBounds(button_RemoveGrid.getX() + button_RemoveGrid.getWidth() + 5, Y_START, 30, 30);
        button_Settings.setBorder(BorderFactory.createEmptyBorder());
        button_Settings.addActionListener(SettingsButtonAction -> this_SettingsButtonAction());
        button_Settings.setToolTipText("Change griding settings");
        this.add(button_Settings);

        //Setup Save Grids button
        button_SaveGrids = new JButton();
        button_SaveGrids.setIcon(ImgManager.GridSaveButton_Up);
        button_SaveGrids.setDisabledIcon(ImgManager.GridSaveButton_Down);
        button_SaveGrids.setDisabledSelectedIcon(ImgManager.GridSaveButton_Down);
        button_SaveGrids.setPressedIcon(ImgManager.GridSaveButton_Down);
        button_SaveGrids.setRolloverIcon(ImgManager.GridSaveButton_Up);
        button_SaveGrids.setRolloverSelectedIcon(ImgManager.GridSaveButton_Up);
        button_SaveGrids.setSelectedIcon(ImgManager.GridSaveButton_Up);
        button_SaveGrids.setBounds(button_Settings.getX() + button_Settings.getWidth() + 25, Y_START, 30, 30);
        button_SaveGrids.setBorder(BorderFactory.createEmptyBorder());
        button_SaveGrids.setToolTipText("Save grids to file");
        this.add(button_SaveGrids);

        //Setup Load Grids button
        button_LoadGrids = new JButton();
        button_LoadGrids.setIcon(ImgManager.GridLoadButton_Up);
        button_LoadGrids.setDisabledIcon(ImgManager.GridLoadButton_Down);
        button_LoadGrids.setDisabledSelectedIcon(ImgManager.GridLoadButton_Down);
        button_LoadGrids.setPressedIcon(ImgManager.GridLoadButton_Down);
        button_LoadGrids.setRolloverIcon(ImgManager.GridLoadButton_Up);
        button_LoadGrids.setRolloverSelectedIcon(ImgManager.GridLoadButton_Up);
        button_LoadGrids.setSelectedIcon(ImgManager.GridLoadButton_Up);
        button_LoadGrids.setBounds(button_SaveGrids.getX() + button_SaveGrids.getWidth() + 5, Y_START, 30, 30);
        button_LoadGrids.setBorder(BorderFactory.createEmptyBorder());
        button_LoadGrids.setToolTipText("Load grids from file");
        this.add(button_LoadGrids);

        button_MultiAddGrid = new JButton();
        button_MultiAddGrid.setBounds(button_LoadGrids.getX() + button_LoadGrids.getWidth() + 5,
                button_LoadGrids.getY(), 30, 30);
        button_MultiAddGrid.addActionListener(MultiAddGridButtonAction -> listen_MultiAddGridButtonAction());
        this.add(button_MultiAddGrid);

        button_MultiMoveGrid = new JButton();
        button_MultiMoveGrid.setBounds(button_MultiAddGrid.getX() + button_MultiAddGrid.getWidth() + 5,
                button_MultiAddGrid.getY(), 30, 30);
        button_MultiMoveGrid.addActionListener(MultiMoveGridButtonAction -> listen_MultiMoveGridButtonAction());
        this.add(button_MultiMoveGrid);

        //Setup panel for displaying grids
        panel_Grids = new JPanel();
        panel_Grids.setLayout(null);
        scrollPane_Grids = new JScrollPane(panel_Grids);
        scrollPane_Grids.setBounds(X_START, button_ZoomToGrid.getY() + button_ZoomToGrid.getHeight() + 5, 696, 220);
        this.add(scrollPane_Grids);

        //Setup Cancel button
        button_Cancel = new JButton();
        button_Cancel.setBounds(0, 0, 10, 10);
        button_Cancel.addActionListener(cancelButton -> {
            //cancelSetting();
        });
        button_Cancel.setLocation(this.getWidth()-button_Cancel.getWidth(), this.getHeight() -
                button_Cancel.getHeight());
        add(button_Cancel);

        //Setup Click Top Left label
        label_ClickTopLeft = new JLabel("Top left spot");
        label_ClickTopLeft.setBounds(0, 0, (int)label_ClickTopLeft.getPreferredSize().getWidth(),
                (int)label_ClickTopLeft.getPreferredSize().getHeight());
        label_ClickTopLeft.setLocation(this.getWidth()/2 - label_ClickTopLeft.getWidth()/2, Y_START);
        label_ClickTopLeft.setVisible(false);
        add(label_ClickTopLeft);

        //Setup Click Top Right label
        label_ClickTopRight = new JLabel("Top right spot");
        label_ClickTopRight.setBounds(0, 0, (int)label_ClickTopRight.getPreferredSize().getWidth(),
                (int)label_ClickTopRight.getPreferredSize().getHeight());
        label_ClickTopRight.setLocation(this.getWidth()/2 - label_ClickTopRight.getWidth()/2, Y_START);
        label_ClickTopRight.setVisible(false);
        add(label_ClickTopRight);

        //Setup Click Bottom label
        label_ClickBottom = new JLabel("Bottom row");
        label_ClickBottom.setBounds(0, 0, (int)label_ClickBottom.getPreferredSize().getWidth(),
                (int)label_ClickBottom.getPreferredSize().getHeight());
        label_ClickBottom.setLocation(this.getWidth()/2 - label_ClickBottom.getWidth()/2, Y_START);
        label_ClickBottom.setVisible(false);
        add(label_ClickBottom);

        //Setup Rows label
        label_Rows = new JLabel("Rows");
        label_Rows.setBounds(0, 0, (int)label_Rows.getPreferredSize().getWidth(),
                (int)label_Rows.getPreferredSize().getHeight());
        label_Rows.setLocation(this.getWidth()/2 - label_Rows.getWidth()/2, Y_START);
        label_Rows.setVisible(false);
        add(label_Rows);

        //Setup Columns label
        label_Columns = new JLabel("Columns");
        label_Columns.setBounds(0, 0, (int)label_Columns.getPreferredSize().getWidth(),
                (int)label_Columns.getPreferredSize().getHeight());
        label_Columns.setLocation(this.getWidth()/2 - label_Columns.getWidth()/2, Y_START);
        label_Columns.setVisible(false);
        add(label_Columns);

        //Setup Rows text field
        textField_Rows = new JTextField();
        textField_Rows.setColumns(4);
        textField_Rows.setBounds(this.getWidth()/2 - (int)textField_Rows.getPreferredSize().getWidth()/2,
                label_Rows.getY() + label_Rows.getHeight() + 5, (int)textField_Rows.getPreferredSize().getWidth(),
                (int)textField_Rows.getPreferredSize().getHeight());
        textField_Rows.setVisible(false);
        add(textField_Rows);

        //Setup Columns text field
        textField_Columns = new JTextField();
        textField_Columns.setColumns(4);
        textField_Columns.setBounds(this.getWidth()/2 - (int)textField_Columns.getPreferredSize().getWidth()/2,
                label_Columns.getY() + label_Columns.getHeight() + 5,
                (int)textField_Columns.getPreferredSize().getWidth(),
                (int)textField_Columns.getPreferredSize().getHeight());
        textField_Columns.setVisible(false);
        add(textField_Columns);

        textField_Rows.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "rowInput");
        textField_Rows.getActionMap().put("rowInput", new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    rows = Integer.parseInt(textField_Rows.getText());
                    progressGridSetupStep();
                }
                catch(Exception ex)
                {
                    textField_Rows.setText("");
                }
            }
        });
        textField_Columns.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "columnInput");
        textField_Columns.getActionMap().put("columnInput", new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    columns = Integer.parseInt(textField_Columns.getText());
                    progressGridSetupStep();
                }
                catch (Exception ex)
                {
                    textField_Columns.setText("");
                }
            }
        });
    }

    /**
     * Start setting up a new GridPanel.
     */
    private void newGridMode()
    {
        panel_Grids.removeAll();
        grid_setup_step = -1;
        progressGridSetupStep();
        manager.setGridMode(SharedData.GRIDMODE_SETUP);
    }

    /**
     * Finishes adding the GridPanel to this GridingPanel.
     */
    public void finalizeGrid()
    {
        if (new_gp != null)
        {
            grid_list.add(new_gp);
            new_gp = null;
            refreshGrids();
            manager.setGridMode(SharedData.GRIDMODE_NORMAL);
            mode = SharedData.GRIDMODE_NORMAL;
        }
    }

    /**
     * Refreshes the alignment of the GridPanels
     */
    private void refreshGrids()
    {
        for (GridPanel gpp: grid_list)
        {
            panel_Grids.remove(gpp);
        }
        int pointX = 5;
        int pointY = 5;
        for (GridPanel gpp: grid_list)
        {
            gpp.setLocation(pointX, pointY);
            panel_Grids.add(gpp);
            pointX += gpp.getWidth() + 5;
        }
        repaint();
    }

    /**
     * Add a grid to the current sample by using the specified features.
     * @param rows The number of rows.
     * @param columns The number of columns.
     */
    public void addGrid(int rows, int columns)
    {
        grid_list.add(new GridPanel(manager, myNumber, grid_list.size() + 1, rows, columns));
        refreshGrids();
    }

    /**
     * The action to be performed when the Move Grid To button is used.
     */
    private void listen_MoveGridToButtonAction()
    {
        if (mode != SharedData.GRIDMODE_SETUP)
        {
            if (mode == SharedData.GRIDMODE_MOVETO)
            {
                setModeNormal();
            }
            else
            {
                if (mode != SharedData.GRIDMODE_NORMAL)
                {
                    setModeNormal();
                }
                button_MoveGridTo.setIcon(ImgManager.GridMoveToButton_Down);
                button_MoveGridTo.setDisabledIcon(ImgManager.GridMoveToButton_Up);
                button_MoveGridTo.setDisabledSelectedIcon(ImgManager.GridMoveToButton_Up);
                button_MoveGridTo.setPressedIcon(ImgManager.GridMoveToButton_Up);
                button_MoveGridTo.setRolloverIcon(ImgManager.GridMoveToButton_Down);
                button_MoveGridTo.setRolloverSelectedIcon(ImgManager.GridMoveToButton_Down);
                button_MoveGridTo.setSelectedIcon(ImgManager.GridMoveToButton_Down);
                manager.setGridMode(SharedData.GRIDMODE_MOVETO);
            }
        }
    }

    /**
     * Action to be performed when the Move Grid button is used.
     */
    private void this_MoveGridButtonAction()
    {
        if (mode != SharedData.GRIDMODE_SETUP)
        {
            if (mode == SharedData.GRIDMODE_MOVE)
            {
                setModeNormal();
            }
            else
            {
                if (mode != SharedData.GRIDMODE_NORMAL)
                {
                    setModeNormal();
                }
                button_MoveGrid.setIcon(ImgManager.GridMoveButton_Down);
                button_MoveGrid.setDisabledIcon(ImgManager.GridMoveButton_Up);
                button_MoveGrid.setDisabledSelectedIcon(ImgManager.GridMoveButton_Up);
                button_MoveGrid.setPressedIcon(ImgManager.GridMoveButton_Up);
                button_MoveGrid.setRolloverIcon(ImgManager.GridMoveButton_Down);
                button_MoveGrid.setRolloverSelectedIcon(ImgManager.GridMoveButton_Down);
                button_MoveGrid.setSelectedIcon(ImgManager.GridMoveButton_Down);
                manager.setGridMode(SharedData.GRIDMODE_MOVE);
            }
        }
    }

    /**
     * Action to be performed when the Rotate Grid button is used.
     */
    private void this_RotateGridButtonAction()
    {
        if (mode != SharedData.GRIDMODE_SETUP)
        {
            if (mode == SharedData.GRIDMODE_ROTATE)
            {
                setModeNormal();
            }
            else
            {
                if (mode != SharedData.GRIDMODE_ROTATE)
                {
                    setModeNormal();
                }
                button_RotateGrid.setIcon(ImgManager.GridRotateButton_Down);
                button_RotateGrid.setDisabledIcon(ImgManager.GridRotateButton_Up);
                button_RotateGrid.setDisabledSelectedIcon(ImgManager.GridRotateButton_Up);
                button_RotateGrid.setPressedIcon(ImgManager.GridRotateButton_Up);
                button_RotateGrid.setRolloverIcon(ImgManager.GridRotateButton_Down);
                button_RotateGrid.setRolloverSelectedIcon(ImgManager.GridRotateButton_Down);
                button_RotateGrid.setSelectedIcon(ImgManager.GridRotateButton_Down);
                manager.setGridMode(SharedData.GRIDMODE_ROTATE);
            }
        }
    }

    /**
     * Action to be performed when the Resize Grid button is used.
     */
    private void this_ResizeGridButtonAction()
    {
        if (mode != SharedData.GRIDMODE_SETUP)
        {
            if (mode == SharedData.GRIDMODE_RESIZE)
            {
                setModeNormal();
            }
            else
            {
                if (mode != SharedData.GRIDMODE_RESIZE)
                {
                    setModeNormal();
                }
                button_ResizeGrid.setIcon(ImgManager.GridResizeButton_Down);
                button_ResizeGrid.setDisabledIcon(ImgManager.GridResizeButton_Up);
                button_ResizeGrid.setDisabledSelectedIcon(ImgManager.GridResizeButton_Up);
                button_ResizeGrid.setPressedIcon(ImgManager.GridResizeButton_Up);
                button_ResizeGrid.setRolloverIcon(ImgManager.GridResizeButton_Down);
                button_ResizeGrid.setRolloverSelectedIcon(ImgManager.GridResizeButton_Down);
                button_ResizeGrid.setSelectedIcon(ImgManager.GridResizeButton_Down);
                manager.setGridMode(SharedData.GRIDMODE_RESIZE);
            }
        }
    }

    /**
     * Sets the mode back to normal and resets buttons that are toggled on.
     */
    private void setModeNormal()
    {
        switch (mode)
        {
            case SharedData.GRIDMODE_MOVETO:
                button_MoveGridTo.setIcon(ImgManager.GridMoveToButton_Up);
                button_MoveGridTo.setDisabledIcon(ImgManager.GridMoveToButton_Down);
                button_MoveGridTo.setDisabledSelectedIcon(ImgManager.GridMoveToButton_Down);
                button_MoveGridTo.setPressedIcon(ImgManager.GridMoveToButton_Down);
                button_MoveGridTo.setRolloverIcon(ImgManager.GridMoveToButton_Up);
                button_MoveGridTo.setRolloverSelectedIcon(ImgManager.GridMoveToButton_Up);
                button_MoveGridTo.setSelectedIcon(ImgManager.GridMoveToButton_Up);
                break;
            case SharedData.GRIDMODE_MOVE:
                button_MoveGrid.setIcon(ImgManager.GridMoveButton_Up);
                button_MoveGrid.setDisabledIcon(ImgManager.GridMoveButton_Down);
                button_MoveGrid.setDisabledSelectedIcon(ImgManager.GridMoveButton_Down);
                button_MoveGrid.setPressedIcon(ImgManager.GridMoveButton_Down);
                button_MoveGrid.setRolloverIcon(ImgManager.GridMoveButton_Up);
                button_MoveGrid.setRolloverSelectedIcon(ImgManager.GridMoveButton_Up);
                button_MoveGrid.setSelectedIcon(ImgManager.GridMoveButton_Up);
                break;
            case SharedData.GRIDMODE_ROTATE:
                button_RotateGrid.setIcon(ImgManager.GridRotateButton_Up);
                button_RotateGrid.setDisabledIcon(ImgManager.GridRotateButton_Down);
                button_RotateGrid.setDisabledSelectedIcon(ImgManager.GridRotateButton_Down);
                button_RotateGrid.setPressedIcon(ImgManager.GridRotateButton_Down);
                button_RotateGrid.setRolloverIcon(ImgManager.GridRotateButton_Up);
                button_RotateGrid.setRolloverSelectedIcon(ImgManager.GridRotateButton_Up);
                button_RotateGrid.setSelectedIcon(ImgManager.GridRotateButton_Up);
                break;
            case SharedData.GRIDMODE_RESIZE:
                button_ResizeGrid.setIcon(ImgManager.GridResizeButton_Up);
                button_ResizeGrid.setDisabledIcon(ImgManager.GridResizeButton_Down);
                button_ResizeGrid.setDisabledSelectedIcon(ImgManager.GridResizeButton_Down);
                button_ResizeGrid.setPressedIcon(ImgManager.GridResizeButton_Down);
                button_ResizeGrid.setRolloverIcon(ImgManager.GridResizeButton_Up);
                button_ResizeGrid.setRolloverSelectedIcon(ImgManager.GridResizeButton_Up);
                button_ResizeGrid.setSelectedIcon(ImgManager.GridResizeButton_Up);
                break;
            default:
                break;
        }
        manager.setGridMode(SharedData.GRIDMODE_NORMAL);
    }

    /**
     * Action to be performed when the Add Grid button is used.
     */
    private void this_AddGridButtonAction()
    {
        setModeNormal();
        newGridMode();
    }

    /**
     * Action to be performed when the Remove Grid button is used.
     */
    private void this_RemoveGridButtonAction()
    {
        setModeNormal();
        manager.removeSample_Grid(myNumber, current_grid);
    }

    /**
     * Action to be performed when the Zoom To Grid button is used.
     */
    private void this_ZoomToGridButtonAction()
    {
        manager.zoomToCurrentGrid();
    }

    /**
     * Sets the specified grid as selected as the selected grid. Does nothing if specified grid number is invalid.
     * @param grid The number of the grid to be selected.
     */
    public void setSelectedGrid(int grid)
    {
        if (grid >= 0 && grid < grid_list.size())
        {
            if (current_grid >= 0)
            {
                grid_list.get(current_grid).setSelected(false);
            }
            current_grid = grid;
            grid_list.get(current_grid).setSelected(true);
        }
    }

    /**
     * Adds the coordinate data to the grid. Does nothing if the grid is not waiting for coordinate data.
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void addCoordinates(int x, int y)
    {
        switch (grid_setup_step)
        {
            case 1:
                topLeftX = x;
                topLeftY = y;
                progressGridSetupStep();
                break;
            case 2:
                topRightX = x;
                topRightY = y;
                progressGridSetupStep();
                break;
            case 3:
                bottomX = x;
                bottomY = y;
                progressGridSetupStep();
                break;
            default:
                break;
        }
    }

    /**
     * Progresses the mode of the GridPanel by changing various components.
     */
    private void progressGridSetupStep()
    {
        grid_setup_step++;

        switch (grid_setup_step)
        {
            case 1:
                label_ClickTopLeft.setLocation(panel_Grids.getWidth() / 2 -
                        (int)label_ClickTopLeft.getPreferredSize().getWidth() / 2, 10);
                label_ClickTopLeft.setVisible(true);
                break;
            case 2:
                label_ClickTopRight.setLocation(panel_Grids.getWidth()/2 -
                        (int)label_ClickTopRight.getPreferredSize().getWidth()/2, 10);
                label_ClickTopLeft.setVisible(false);
                label_ClickTopRight.setVisible(true);
                break;
            case 3:
                label_ClickBottom.setLocation(panel_Grids.getWidth()/2 -
                        (int)label_ClickBottom.getPreferredSize().getWidth()/2, 10);
                label_ClickTopRight.setVisible(false);
                label_ClickBottom.setVisible(true);
                break;
            case 4:
                label_Rows.setLocation(panel_Grids.getWidth()/2 - (int)label_Rows.getPreferredSize().getWidth()/2, 10);
                textField_Rows.setLocation(panel_Grids.getWidth()/2 - (int)textField_Rows.getSize().getWidth()/2,
                        label_Rows.getY() + label_Rows.getHeight() + 10);
                label_ClickBottom.setVisible(false);
                label_Rows.setVisible(true);
                textField_Rows.setVisible(true);
                textField_Rows.requestFocus();
                break;
            case 5:
                label_Columns.setLocation(panel_Grids.getWidth() / 2 -
                        (int)label_Columns.getPreferredSize().getWidth() / 2, 10);
                textField_Columns.setLocation(panel_Grids.getWidth() / 2 -
                                (int)textField_Columns.getSize().getWidth() / 2,
                        label_Columns.getY() + label_Columns.getHeight() + 10);
                label_Rows.setVisible(false);
                textField_Rows.setVisible(false);
                label_Columns.setVisible(true);
                textField_Columns.setVisible(true);
                textField_Columns.requestFocus();
                break;
            case 6:
                label_Columns.setVisible(false);
                textField_Columns.setVisible(false);

                commitValues();
                manager.setSample_CurrentGrid(myNumber, grid_list.size()-1);
                setModeNormal();
                break;
            case 7:
            case 8:
                mode = 7;
                break;
            default:
                mode = 0;
                progressGridSetupStep();
                break;
        }
        updateUI();
    }

    /**
     * Commit the values obtained into making a new grid.
     */
    private void commitValues()
    {
        if (topLeftX > topRightX)
        {
            int temp = topRightX;
            topRightX = topLeftX;
            topLeftX = temp;
            temp = topRightY;
            topRightY = topLeftY;
            topLeftY = temp;
        }

        manager.addGrid(topLeftX, topLeftY, topRightX, topRightY, bottomX, bottomY, rows, columns);
    }

    public void setGridMode(int mode)
    {
        this.mode = mode;
    }

    public void setCurrentGrid(int grid)
    {
        current_grid = grid;
        setSelectedGrid(current_grid);
    }

    public void removeGrid(int grid)
    {
        panel_Grids.remove(grid_list.get(grid));
        grid_list.remove(grid);
        for (int i = current_grid; i < grid_list.size(); i++)
        {
            grid_list.get(i).setNumber(grid_list.get(i).getNumber() - 1);
        }
        if (current_grid > grid_list.size() - 1)
        {
            current_grid = grid_list.size() - 1;
        }
        setSelectedGrid(current_grid);
        refreshGrids();
    }

    private void this_AdvancedSettingsButtonAction()
    {
        GridAdvancedDialog gad = new GridAdvancedDialog(manager.getWindow());
        Polygon p = manager.getSample_Grid_Polygon_Master(myNumber, current_grid);
        double d = manager.getSample_Grid_Angle(myNumber, current_grid);
        int[] rc = manager.getSample_Grid_RowsAndColumns(myNumber, current_grid);
        gad.setOptions(p.xpoints[0], p.ypoints[0], p.ypoints[3] - p.ypoints[0], p.xpoints[1] - p.xpoints[0], d,
                rc[1], rc[0]);
        gad.setModal(true);
        gad.pack();
        gad.setVisible(true);
        if (gad.getOK())
        {
            manager.setSample_Grid_MoveTo(myNumber, current_grid, gad.getGridTopLeftX(), gad.getGridTopLeftY());
            manager.setSample_Grid_ResizeTo(myNumber, current_grid, gad.getGridHeight(), gad.getGridWidth());
            manager.setSample_Grid_RotateTo(myNumber, current_grid, gad.getGridAngle());
            rc[0] = gad.getGridRows();
            rc[1] = gad.getGridColumns();
            manager.setSample_Grid_RowsAndColumns(myNumber, current_grid, rc);
        }
    }

    private void listen_MultiAddGridButtonAction()
    {

    }

    private void listen_MultiMoveGridButtonAction()
    {

    }

    public void reloadGrid(int grid)
    {
        int[] rc = manager.getSample_Grid_RowsAndColumns(myNumber, grid);
        grid_list.get(grid).setRowsAndColumns(rc[0], rc[1]);
        repaint();
    }

    private void this_SettingsButtonAction()
    {
        GridSetupDialog gsd = new GridSetupDialog(manager.getWindow());
        gsd.setOptions(manager.getSample_GridHorizontal(myNumber), manager.getSample_GridVertical(myNumber),
                manager.getSample_GridDirection(myNumber));
        gsd.setModal(true);
        gsd.pack();
        gsd.setVisible(true);
        if (gsd.getOK())
        {
            manager.setSample_GridHorizontal(myNumber, gsd.getHorizontal());
            manager.setSample_GridVertical(myNumber, gsd.getVertical());
            manager.setSample_GridDirection(myNumber, gsd.getFirstSpot());
        }
    }
}
