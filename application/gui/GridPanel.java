package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class GridPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton button_Cancel;
    private JLabel label_Name;
    private JLabel label_Grid;
    private JLabel label_ClickTopLeft;
    private JLabel label_ClickTopRight;
    private JLabel label_ClickBottom;
    private JLabel label_Rows;
    private JLabel label_Columns;
    private JTextField textField_Rows;
    private JTextField textField_Columns;
    private Border border_Blackline = BorderFactory.createLineBorder(Color.black);
    private Border border_Yellowline = BorderFactory.createLineBorder(Color.yellow);

    private int myNumber;
    private int mode = 0;
    private boolean awaitingData = false;

    private int topLeftX_temp;
    private int topLeftY_temp;
    private int topRightX_temp;
    private int topRightY_temp;
    private int bottomX_temp;
    private int bottomY_temp;
    private int rows_temp;
    private int columns_temp;

    private final int X_START = 0;
    private final int Y_START = 0;

    private GridingPanel parent_panel;
    private GuiManager manager;

    /**
     * Create a new GridPanel
     * @param parentPanel The GridingPanel that will use this GridPanel
     * @param num The number of this GridPanel
     */
    public GridPanel(GuiManager manager, GridingPanel parentPanel, int num)
    {
        this.manager = manager;
        myNumber = num;
        parent_panel = parentPanel;
        setup();
    }

    /**
     * Sets up the GridPanel for use.
     */
    private void setup()
    {
        this.setSize(80, 40);
        this.setPreferredSize(new Dimension(80, 40));
        this.setLayout(null);

        MouseAdapter ma = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {}

            @Override
            public void mousePressed(MouseEvent e)
            {
                parent_panel.setSelectedGrid(myNumber-1);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {}

            @Override
            public void mouseEntered(MouseEvent e)
            {}

            @Override
            public void mouseExited(MouseEvent e)
            {}

            @Override
            public void mouseDragged(MouseEvent e)
            {}
        };

        this.addMouseListener(ma);

        //Setup Cancel button
        button_Cancel = new JButton();
        button_Cancel.setBounds(0, 0, 10, 10);
        button_Cancel.addActionListener(cancelButton -> {
            cancelSetting();
        });
        button_Cancel.setLocation(this.getWidth()-button_Cancel.getWidth(), this.getHeight() -
                button_Cancel.getHeight());
        add(button_Cancel);

        //Setup Name label
        label_Name = new JLabel("Grid " + myNumber);
        label_Name.setBounds(0, 0, (int)label_Name.getPreferredSize().getWidth(),
                (int)label_Name.getPreferredSize().getHeight());
        label_Name.setLocation(this.getWidth()/2 - label_Name.getWidth()/2, Y_START);
        label_Name.setVisible(false);
        add(label_Name);

        //Setup grid dimensions label
        label_Grid = new JLabel("0x0");
        label_Grid.setVisible(false);
        add(label_Grid);

        //Setup Click Top Left label
        label_ClickTopLeft = new JLabel("Top left");
        label_ClickTopLeft.setBounds(0, 0, (int)label_ClickTopLeft.getPreferredSize().getWidth(),
                (int)label_ClickTopLeft.getPreferredSize().getHeight());
        label_ClickTopLeft.setLocation(this.getWidth()/2 - label_ClickTopLeft.getWidth()/2, Y_START);
        label_ClickTopLeft.setVisible(false);
        add(label_ClickTopLeft);

        //Setup Click Top Right label
        label_ClickTopRight = new JLabel("Top right");
        label_ClickTopRight.setBounds(0, 0, (int)label_ClickTopRight.getPreferredSize().getWidth(),
                (int)label_ClickTopRight.getPreferredSize().getHeight());
        label_ClickTopRight.setLocation(this.getWidth()/2 - label_ClickTopRight.getWidth()/2, Y_START);
        label_ClickTopRight.setVisible(false);
        add(label_ClickTopRight);

        //Setup Click Bottom label
        label_ClickBottom = new JLabel("Bottom");
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
                    rows_temp = Integer.parseInt(textField_Rows.getText());
                    progressMode();
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
                    columns_temp = Integer.parseInt(textField_Columns.getText());
                    progressMode();
                }
                catch (Exception ex)
                {
                    textField_Columns.setText("");
                }
            }
        });

        progressMode();

        this.setVisible(true);
    }

    /**
     * Adds the coordinate data to the grid. Does nothing if the grid is not waiting for coordinate data.
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void addCoordinates(int x, int y)
    {
        switch (mode) {
        case 1:
            topLeftX_temp = x;
            topLeftY_temp = y;
            progressMode();
            break;
        case 2:
            topRightX_temp = x;
            topRightY_temp = y;
            progressMode();
            break;
        case 3:
            bottomX_temp = x;
            bottomY_temp = y;
            progressMode();
            break;
        default:
            break;
        }
    }

    /**
     * Progresses the mode of the GridPanel by changing various components.
     */
    private void progressMode()
    {
        mode++;

        switch (mode)
        {
            case 1:
                label_ClickTopLeft.setVisible(true);
                awaitingData = true;
                break;
            case 2:
                label_ClickTopLeft.setVisible(false);
                label_ClickTopRight.setVisible(true);
                break;
            case 3:
                label_ClickTopRight.setVisible(false);
                label_ClickBottom.setVisible(true);
                break;
            case 4:
                label_ClickBottom.setVisible(false);
                label_Rows.setVisible(true);
                textField_Rows.setVisible(true);
                textField_Rows.requestFocus();
                break;
            case 5:
                label_Rows.setVisible(false);
                textField_Rows.setVisible(false);
                label_Columns.setVisible(true);
                textField_Columns.setVisible(true);
                textField_Columns.requestFocus();
                break;
            case 6:
                label_Columns.setVisible(false);
                textField_Columns.setVisible(false);
                label_Name.setVisible(true);
                label_Grid.setText(rows_temp + "x" + columns_temp);
                label_Grid.setBounds(this.getWidth()/2 - (int)label_Grid.getPreferredSize().getWidth()/2, label_Name.getY()
                                + label_Name.getHeight(), (int)label_Grid.getPreferredSize().getWidth(),
                        (int)label_Grid.getPreferredSize().getHeight());
                label_Grid.setVisible(true);

                remove(label_ClickTopLeft);
                label_ClickTopLeft = null;
                remove(label_ClickTopRight);
                label_ClickTopRight = null;
                remove(label_ClickBottom);
                label_ClickBottom = null;
                remove(label_Rows);
                label_Rows = null;
                remove(label_Columns);
                label_Columns = null;
                remove(textField_Columns);
                textField_Columns = null;
                remove(textField_Rows);
                textField_Rows = null;
                remove(button_Cancel);
                button_Cancel = null;

                parent_panel.finalizeGrid();
                awaitingData = false;
                commitValues();
                parent_panel.setSelectedGrid(myNumber-1);
                break;
            case 7:
            case 8:
                mode = 7;
                break;
            default:
                mode = 0;
                progressMode();
                break;
        }
        updateUI();
        parent_panel.updateUI();
    }

    /**
     * Cancel setting up the grid.
     */
    private void cancelSetting()
    {
        parent_panel.cancelGrid();
    }

    /*
    private void openPopup()
    {
        GridAdvancedDialog god = new GridAdvancedDialog(parent_panel.getFrame());
        god.setOptions(topLeftX_true, topLeftY_true, topRightX_true, topRightY_true, bottomX_true, bottomY_true,
                columns_true, rows_true);
        god.setModal(true);
        god.pack();
        god.setVisible(true);
        if (god.getOK())
        {
            topLeftX_temp = god.tlX;
            topLeftY_temp = god.tlY;
            topRightX_temp = god.trX;
            topRightY_temp = god.trY;
            bottomX_temp = god.bX;
            bottomY_temp = god.bY;
            columns_temp = god.columns;
            rows_temp = god.rows;
            commitValues();
        }
    }
    */

    /**
     * Commit the values obtained into making a new grid.
     */
    private void commitValues()
    {
        int topLeftX;
        int topLeftY;
        int topRightX;
        int topRightY;
        if (topLeftX_temp < topRightX_temp)
        {
            topLeftX = topLeftX_temp;
            topLeftY = topLeftY_temp;
            topRightX = topRightX_temp;
            topRightY = topRightY_temp;
        }
        else
        {
            topRightX = topLeftX_temp;
            topRightY = topLeftY_temp;
            topLeftX = topRightX_temp;
            topLeftY = topRightY_temp;
        }

        int rows = rows_temp;
        int columns = columns_temp;

        parent_panel.addGrid(topLeftX, topLeftY, topRightX, topRightY, bottomX_temp, bottomY_temp, rows, columns);
    }

    /**
     * Specifies whether this grid is selected. Changes the border accordingly.
     */
    public void setSelected(boolean selected)
    {
        if (selected)
        {
            this.setBorder(border_Yellowline);
        }
        else
        {
            this.setBorder(border_Blackline);
        }
    }

    /**
     * Returns the number assigned to this GridPanel
     * @return The number of this GridPanel
     */
    public int getNumber()
    {
        return myNumber;
    }

    /**
     * Sets the number of this GridPanel
     * @param num the new number of this GridPanel
     */
    public void setNumber(int num)
    {
        myNumber = num;
        label_Name.setText("Grid " + myNumber);
    }
}