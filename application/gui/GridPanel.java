package application.gui;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class GridPanel extends JPanel
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
    private JPanel panel_Grids;
    private JScrollPane scrollPane_Grids;

    private final int X_START = 10;
    private final int Y_START = 20;

    private TabPanel parent_panel;
    private MainWindow main;
    private ArrayList<GridPanelPanel> grid_list;
    private GridPanelPanel new_gpp;

    public GridPanel(MainWindow main, TabPanel parent)
    {
        this.main = main;
        parent_panel = parent;
        setup();
    }

    private void setup()
    {
        grid_list = new ArrayList<>();
        new_gpp = null;

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
        button_MoveGridTo.addActionListener(MoveGridToButtonAction -> this_MoveGridToButtonAction());
        this.add(button_MoveGridTo);

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
        this.add(button_AdvancedSettings);

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
        button_AddGrid.addActionListener(AddGridButtonAction -> newGridPanelPanel());
        button_AddGrid.setToolTipText("Add grid");
        this.add(button_AddGrid);

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
        button_RemoveGrid.setToolTipText("Remove selected grid");
        this.add(button_RemoveGrid);

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
        button_Settings.setToolTipText("Change griding settings");
        this.add(button_Settings);

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

        panel_Grids = new JPanel();
        scrollPane_Grids = new JScrollPane(panel_Grids);
        scrollPane_Grids.setBounds(X_START, button_ZoomToGrid.getY() + button_ZoomToGrid.getHeight() + 5, 696, 220);
        this.add(scrollPane_Grids);
    }

    public void coordinatesFound(int x, int y)
    {
        if (new_gpp != null)
        {
            new_gpp.addCoordinates(x, y);
        }
    }

    private void newGridPanelPanel()
    {
        setModeNormal();
        new_gpp = new GridPanelPanel(this, grid_list.size()+1);
        panel_Grids.removeAll();
        panel_Grids.add(new_gpp);
        parent_panel.setGridMode(SharedData.GRIDMODE_SETUP);
        mode = SharedData.GRIDMODE_SETUP;
    }

    public void finalizeGridPanelPanel()
    {
        if (new_gpp != null)
        {
            grid_list.add(new_gpp);
            new_gpp = null;
            panel_Grids.removeAll();
            for (GridPanelPanel gpp: grid_list)
            {
                panel_Grids.add(gpp);
            }
            parent_panel.setGridMode(SharedData.GRIDMODE_NORMAL);
            mode = SharedData.GRIDMODE_NORMAL;
        }
    }

    public void cancelGridPanelPanel()
    {
        new_gpp = null;
        panel_Grids.removeAll();
        for (GridPanelPanel gpp: grid_list)
        {
            panel_Grids.add(gpp);
        }
        this.repaint();
    }

    public void addGrid(int tlX, int tlY, int trX, int trY, int bX, int bY, int rows, int columns)
    {
        parent_panel.addGrid(tlX, tlY, trX, trY, bX, bY, rows, columns);
    }

    private int mode = SharedData.GRIDMODE_NORMAL;
    private void this_MoveGridToButtonAction()
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
                parent_panel.setGridMode(SharedData.GRIDMODE_MOVETO);
                mode = SharedData.GRIDMODE_MOVETO;
            }
        }
    }

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
                parent_panel.setGridMode(SharedData.GRIDMODE_MOVE);
                mode = SharedData.GRIDMODE_MOVE;
            }
        }
    }

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
                parent_panel.setGridMode(SharedData.GRIDMODE_ROTATE);
                mode = SharedData.GRIDMODE_ROTATE;
            }
        }
    }

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
                parent_panel.setGridMode(SharedData.GRIDMODE_RESIZE);
                mode = SharedData.GRIDMODE_RESIZE;
            }
        }
    }

    private void setModeNormal()
    {
        //TODO: Set all buttons to narmal mode
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
        parent_panel.setGridMode(SharedData.GRIDMODE_NORMAL);
        mode = SharedData.GRIDMODE_NORMAL;
    }

    private void this_ZoomToGridButtonAction()
    {
        parent_panel.zoomToGrid();
    }

    public void setSelectedGrid(int grid)
    {
        parent_panel.setSelectedGrid(grid);
        for (int i = 0; i < grid_list.size(); i++)
        {
            if (i == grid)
            {
                grid_list.get(i).setSelected(true);
            }
            else
            {
                grid_list.get(i).setSelected(false);
            }
        }
    }
}
