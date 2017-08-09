package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class GridPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel label_Name;
    private JLabel label_Grid;
    private Border border_Blackline = BorderFactory.createLineBorder(Color.black);
    private Border border_Yellowline = BorderFactory.createLineBorder(Color.yellow);

    private int myNumber;

    private int rows;
    private int columns;

    private final int X_START = 0;
    private final int Y_START = 0;

    private GuiManager manager;
    private int sample_num;

    /**
     * Create a new GridPanel
     * @param num The number of this GridPanel
     */
    public GridPanel(GuiManager manager, int sample_num, int num, int rows, int columns)
    {
        this.manager = manager;
        this.sample_num = sample_num;
        myNumber = num;
        setup();
        setRowsAndColumns(rows, columns);
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
                manager.setSample_CurrentGrid(sample_num, myNumber - 1);
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

        //Setup Name label
        label_Name = new JLabel("Grid " + myNumber);
        label_Name.setBounds(0, 0, (int)label_Name.getPreferredSize().getWidth(),
                (int)label_Name.getPreferredSize().getHeight());
        label_Name.setLocation(this.getWidth()/2 - label_Name.getWidth()/2, Y_START);
        add(label_Name);

        //Setup grid dimensions label
        label_Grid = new JLabel("0x0");
        label_Grid.setLocation(0, label_Name.getY() + label_Name.getHeight() + 5);
        add(label_Grid);

        this.setVisible(true);
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

    public void setRowsAndColumns(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        label_Grid.setText(rows + "x" + columns);
        label_Grid.setBounds(this.getWidth() / 2 - (int)label_Grid.getPreferredSize().getWidth() / 2, label_Grid.getY(),
                (int)label_Grid.getPreferredSize().getWidth(), (int)label_Grid.getPreferredSize().getHeight());
    }
}