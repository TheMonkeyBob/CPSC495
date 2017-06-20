package application.gui;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class GridPanelPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton button_Cancel;
    private JLabel label_Name;
    private JLabel label_Grid;
    private JLabel label_ClickTopLeft;
    private JLabel label_ClickTopRight;
    private JLabel label_ClickBottom;
    private JLabel label_Rows;
    private JLabel label_Columns;
    private JFormattedTextField textField_Rows;
    private JFormattedTextField textField_Columns;

    private int myNumber;
    private int mode = 0;
    private boolean setFlag = false;
    private boolean awaitingData = false;

    private int topLeftX_true;
    private int topLeftY_true;
    private int topLeftX_temp;
    private int topLeftY_temp;
    private int topRightX_true;
    private int topRightY_true;
    private int topRightX_temp;
    private int topRightY_temp;
    private int bottomX_true;
    private int bottomY_true;
    private int bottomX_temp;
    private int bottomY_temp;
    private int rows_true;
    private int rows_temp;
    private int columns_true;
    private int columns_temp;
    private final int X_START = 0;
    private final int Y_START = 0;

    private MainWindow main;

    private GridPanel parent_panel;

    public GridPanelPanel(GridPanel parentPanel, int num) {
        myNumber = num;
        parent_panel = parentPanel;
        setup();
    }

    private void setup() {
        button_Cancel = new JButton();
        button_Cancel.setBounds(0, 0, 10, 10);
        button_Cancel.addActionListener(cancelButton -> {
            cancelSetting();
        });
        label_Name = new JLabel("Grid " + myNumber);
        label_Name.setBounds(0, 0, (int)label_Name.getPreferredSize().getWidth(),
                (int)label_Name.getPreferredSize().getHeight());
        label_Grid = new JLabel();
        label_ClickTopLeft = new JLabel("Top left");
        label_ClickTopLeft.setBounds(0, 0, (int)label_ClickTopLeft.getPreferredSize().getWidth(),
                (int)label_ClickTopLeft.getPreferredSize().getHeight());
        label_ClickTopRight = new JLabel("Top right");
        label_ClickTopRight.setBounds(0, 0, (int)label_ClickTopRight.getPreferredSize().getWidth(),
                (int)label_ClickTopRight.getPreferredSize().getHeight());
        label_ClickBottom = new JLabel("Bottom");
        label_ClickBottom.setBounds(0, 0, (int)label_ClickBottom.getPreferredSize().getWidth(),
                (int)label_ClickBottom.getPreferredSize().getHeight());
        label_Rows = new JLabel("Rows");
        label_Rows.setBounds(0, 0, (int)label_Rows.getPreferredSize().getWidth(),
                (int)label_Rows.getPreferredSize().getHeight());
        label_Columns = new JLabel("Columns");
        label_Columns.setBounds(0, 0, (int)label_Columns.getPreferredSize().getWidth(),
                (int)label_Columns.getPreferredSize().getHeight());
        textField_Rows = new JFormattedTextField(new NumberFormatter());
        //textField_Rows.setText("00000");
        textField_Rows.setMinimumSize(new Dimension(50, 20));
        textField_Rows.setPreferredSize(new Dimension(50, 20));
        textField_Rows.setColumns(5);
        textField_Columns = new JFormattedTextField(new NumberFormatter());
        //textField_Columns.setText("00000");
        textField_Columns.setMinimumSize(new Dimension(50, 20));
        textField_Columns.setPreferredSize(new Dimension(50, 20));
        textField_Columns.setColumns(5);

        textField_Rows.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "rowInput");
        textField_Rows.getActionMap().put("rowInput", new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                rows_temp = Integer.parseInt(textField_Rows.getText());
                progressMode();
            }
        });
        textField_Columns.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "columnInput");
        textField_Columns.getActionMap().put("columnInput", new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                columns_temp = Integer.parseInt(textField_Columns.getText());
                progressMode();
            }
        });

        this.setSize(100, 40);
        this.setPreferredSize(new Dimension(100, 40));
        this.setLayout(null);
        progressMode();

        this.setVisible(true);
    }

    public boolean isAwaitingData() {
        return awaitingData;
    }

    public void addCoordinates(int x, int y)
    {
        switch (mode) {
        case 1:
            topLeftX_temp = x;
            topLeftY_temp = y;
            break;
        case 2:
            topRightX_temp = x;
            topRightY_temp = y;
            break;
        case 3:
            bottomX_temp = x;
            bottomY_temp = y;
            break;
        default:
            break;
        }
        progressMode();
    }

    public void progressMode()
    {
        mode++;
        removeAll();

        switch (mode) {
        case 1:
            label_ClickTopLeft.setLocation(this.getWidth()/2 - label_ClickTopLeft.getWidth()/2, Y_START);
            add(label_ClickTopLeft);
            button_Cancel.setLocation(this.getWidth()-button_Cancel.getWidth(), this.getHeight() -
                    button_Cancel.getHeight());
            add(button_Cancel);
            awaitingData = true;
            break;
        case 2:
            label_ClickTopRight.setLocation(this.getWidth()/2 - label_ClickTopRight.getWidth()/2, Y_START);
            add(label_ClickTopRight);
            add(button_Cancel);
            break;
        case 3:
            label_ClickBottom.setLocation(this.getWidth()/2 - label_ClickBottom.getWidth()/2, Y_START);
            add(label_ClickBottom);
            add(button_Cancel);
            break;
        case 4:
            label_Rows.setLocation(this.getWidth()/2 - label_Rows.getWidth()/2, Y_START);
            add(label_Rows);
            textField_Rows.setLocation(X_START, Y_START+20);
            //textField_Rows.setText("");
            add(textField_Rows);
            textField_Rows.requestFocus();
            add(button_Cancel);
            break;
        case 5:
            label_Columns.setLocation(this.getWidth()/2 - label_Columns.getWidth()/2, Y_START);
            add(label_Columns);
            textField_Columns.setLocation(this.getWidth()/2 - textField_Columns.getWidth()/2, label_Columns.getY() +
                    label_Columns.getHeight() + 5);
            //textField_Columns.setText("");
            add(textField_Columns);
            textField_Columns.requestFocus();
            add(button_Cancel);
            awaitingData = false;
            break;
        case 6:
            label_Name.setLocation(this.getWidth()/2 - label_Name.getWidth()/2, Y_START);
            label_Grid.setText(rows_temp + "x" + columns_temp);
            label_Grid.setBounds(this.getWidth()/2 - (int)label_Grid.getPreferredSize().getWidth()/2, label_Name.getY()
                    + label_Name.getHeight(), (int)label_Grid.getPreferredSize().getWidth(),
                    (int)label_Grid.getPreferredSize().getHeight());
            add(label_Name);
            add(label_Grid);
            setFlag = true;
            parent_panel.finalizeGridPanelPanel();
            commitValues();
            break;
        default:
            mode = 0;
            progressMode();
            break;
        }
        updateUI();
        parent_panel.updateUI();
    }

    public void cancelSetting()
    {
        mode = 0;
        removeAll();
        awaitingData = false;
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


    //Values are set to not allow rotating
    //Logic will need to be changed if rotation added
    private void commitValues() {
        topLeftX_true = topLeftX_temp;
        topLeftY_true = topLeftY_temp;
        topRightX_true = topRightX_temp;
        topRightY_true = topRightY_temp;
        bottomX_true = bottomX_temp;
        bottomY_true = bottomY_temp;
        rows_true = rows_temp;
        columns_true = columns_temp;
        //TODO: setup proper values
        parent_panel.addGrid(myNumber - 1, topLeftX_true, topLeftY_true, topRightX_true, topLeftY_true, topLeftX_true,
                bottomY_true, topRightX_true, bottomY_true, rows_true, columns_true);
    }
}