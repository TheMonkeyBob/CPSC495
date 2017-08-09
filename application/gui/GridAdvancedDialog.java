/*
 *   MAGIC Tool, A microarray image and data analysis program
 *   Copyright (C) 2003  Laurie Heyer
 *
 *   This program is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU General Public License
 *   as published by the Free Software Foundation; either version 2
 *   of the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *   Contact Information:
 *   Laurie Heyer
 *   Dept. of Mathematics
 *   PO Box 6959
 */

package application.gui;

import javax.swing.*;
import java.awt.*;

/**
 * GridOptionsDialog is a JDialog which allows user to set various grid properties
 * for gridding a microarray image.
 */
public class GridAdvancedDialog extends JDialog
{
    private JPanel panel_TopLeftSpot = new JPanel();
    private JLabel label_TopLeftSpotX = new JLabel();
    private JLabel label_TopLeftSpotY = new JLabel();
    private JTextField textField_TopLeftSpotX = new JTextField();
    private JTextField textField_TopLeftSpotY = new JTextField();
    private JPanel panel_HeightAndWidth = new JPanel();
    private JLabel label_Height = new JLabel();
    private JLabel label_Width = new JLabel();
    private JTextField textField_Height = new JTextField();
    private JTextField textField_Width = new JTextField();
    private JPanel panel_Angle = new JPanel();
    private JLabel label_Angle = new JLabel();
    private JTextField textField_Angle = new JTextField();
    private JPanel panel_RowsAndColumns = new JPanel();
    private JLabel label_Rows = new JLabel();
    private JLabel label_Columns = new JLabel();
    private JTextField textField_Rows = new JTextField();
    private JTextField textField_Columns = new JTextField();
    private JPanel panel_Buttons = new JPanel();
    private JButton button_OK = new JButton();
    private JButton button_Cancel = new JButton();

    private boolean ok=false;

    private int topLeftX;
    private int topLeftY;
    private int gridHeight;
    private int gridWidth;
    private double angle;
    private int columns;
    private int rows;

    /**
     * Constructs a dialog for users to set grid properties
     * @param owner parent frame
     */
    public GridAdvancedDialog(Frame owner)
    {
        super(owner);
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        label_TopLeftSpotX.setText("Top Left X:");
        label_TopLeftSpotY.setText("Top Left Y:");
        textField_TopLeftSpotX.setColumns(10);
        textField_TopLeftSpotY.setColumns(10);
        label_Height.setText("Height:");
        label_Width.setText("Width:");
        textField_Height.setColumns(10);
        textField_Width.setColumns(10);
        label_Angle.setText("Angle:");
        textField_Angle.setColumns(10);
        label_Columns.setText("Columns:");
        label_Rows.setText("Rows:");
        textField_Columns.setColumns(6);
        textField_Rows.setColumns(6);

        button_OK.setText("OK");
        this.getRootPane().setDefaultButton(button_OK);
        button_OK.addActionListener(okPressed-> okButton_actionPerformed());
        button_Cancel.setRequestFocusEnabled(false);
        button_Cancel.setFocusPainted(false);
        button_Cancel.setText("Cancel");
        button_Cancel.addActionListener(cancelPressed-> cancelButton_actionPerformed());

        label_TopLeftSpotX.setBounds(0, 5, (int)label_TopLeftSpotX.getPreferredSize().getWidth(),
                (int)label_TopLeftSpotX.getPreferredSize().getHeight());
        textField_TopLeftSpotX.setBounds(label_TopLeftSpotX.getX() + label_TopLeftSpotX.getWidth() + 5,
                label_TopLeftSpotX.getY(), (int)textField_TopLeftSpotX.getPreferredSize().getWidth(),
                (int)textField_TopLeftSpotX.getPreferredSize().getHeight());
        textField_TopLeftSpotY.setBounds(textField_TopLeftSpotX.getX(),
                textField_TopLeftSpotX.getY() + textField_TopLeftSpotX.getHeight() + 5,
                (int)textField_TopLeftSpotY.getPreferredSize().getWidth(),
                (int)textField_TopLeftSpotY.getPreferredSize().getHeight());
        label_TopLeftSpotY.setBounds(label_TopLeftSpotX.getX(), textField_TopLeftSpotY.getY(),
                (int)label_TopLeftSpotY.getPreferredSize().getWidth(),
                (int)label_TopLeftSpotY.getPreferredSize().getHeight());
        panel_TopLeftSpot.setLayout(null);
        panel_TopLeftSpot.setBounds(0, 0, label_TopLeftSpotX.getWidth() + 5 + textField_TopLeftSpotX.getWidth(),
                textField_TopLeftSpotY.getY() + textField_TopLeftSpotY.getHeight());
        panel_TopLeftSpot.add(label_TopLeftSpotX);
        panel_TopLeftSpot.add(textField_TopLeftSpotX);
        panel_TopLeftSpot.add(label_TopLeftSpotY);
        panel_TopLeftSpot.add(textField_TopLeftSpotY);

        label_Height.setBounds(0, 0, (int)label_Height.getPreferredSize().getWidth(),
                (int)label_Height.getPreferredSize().getHeight());
        textField_Height.setBounds(label_Height.getX() + label_Height.getWidth() + 5, label_Height.getY(),
                (int)textField_Height.getPreferredSize().getWidth(),
                (int)textField_Height.getPreferredSize().getHeight());
        textField_Width.setBounds(textField_Height.getX(), textField_Height.getY() + textField_Height.getHeight() + 5,
                (int)textField_Width.getPreferredSize().getWidth(),
                (int)textField_Width.getPreferredSize().getHeight());
        label_Width.setBounds(label_Height.getX(), textField_Width.getY(),
                (int)label_Width.getPreferredSize().getWidth(),
                (int)label_Width.getPreferredSize().getHeight());
        panel_HeightAndWidth.setLayout(null);
        panel_HeightAndWidth.setBounds(0, panel_TopLeftSpot.getY() + panel_TopLeftSpot.getHeight() + 5,
                label_Height.getWidth() + 5 + textField_Height.getWidth(), textField_Height.getHeight() +
                textField_Width.getHeight() + 5);
        panel_HeightAndWidth.add(textField_Height);
        panel_HeightAndWidth.add(textField_Width);
        panel_HeightAndWidth.add(label_Height);
        panel_HeightAndWidth.add(label_Width);

        label_Angle.setBounds(0, 0, (int)label_Angle.getPreferredSize().getWidth(),
                (int)label_Angle.getPreferredSize().getHeight());
        textField_Angle.setBounds(label_Angle.getX() + label_Angle.getWidth() + 5, label_Angle.getY(),
                (int)textField_Angle.getPreferredSize().getWidth(),
                (int)textField_Angle.getPreferredSize().getHeight());
        panel_Angle.setLayout(null);
        panel_Angle.setBounds(0, panel_HeightAndWidth.getY() + panel_HeightAndWidth.getHeight() + 5,
                label_Angle.getWidth() + textField_Angle.getWidth() + 5, textField_Angle.getHeight());
        panel_Angle.add(label_Angle);
        panel_Angle.add(textField_Angle);

        label_Columns.setBounds(0, 0, (int)label_Columns.getPreferredSize().getWidth(),
                (int)label_Columns.getPreferredSize().getHeight());
        textField_Columns.setBounds(label_Columns.getX() + label_Columns.getWidth() + 5, label_Columns.getY(),
                (int)textField_Columns.getPreferredSize().getWidth(),
                (int)textField_Columns.getPreferredSize().getHeight());
        textField_Rows.setBounds(textField_Columns.getX(), textField_Columns.getY() + textField_Columns.getHeight() + 5,
                (int)textField_Rows.getPreferredSize().getWidth(),
                (int)textField_Rows.getPreferredSize().getHeight());
        label_Rows.setBounds(label_Columns.getX(), textField_Rows.getY(),
                (int)label_Rows.getPreferredSize().getWidth(),
                (int)label_Rows.getPreferredSize().getHeight());
        panel_RowsAndColumns.setLayout(null);
        panel_RowsAndColumns.setBounds(0, panel_Angle.getY() + panel_Angle.getHeight() + 5,
                label_Columns.getWidth() + textField_Columns.getWidth() + 5, textField_Columns.getHeight() +
                textField_Rows.getHeight() + 5);
        panel_RowsAndColumns.add(label_Columns);
        panel_RowsAndColumns.add(textField_Columns);
        panel_RowsAndColumns.add(label_Rows);
        panel_RowsAndColumns.add(textField_Rows);

        button_OK.setBounds(0, 0, (int)button_OK.getPreferredSize().getWidth(),
                (int)button_OK.getPreferredSize().getHeight());
        button_Cancel.setBounds(button_OK.getX() + button_OK.getWidth() + 10, button_OK.getY(),
                (int)button_Cancel.getPreferredSize().getWidth(),
                (int)button_Cancel.getPreferredSize().getHeight());
        panel_Buttons.setLayout(null);
        panel_Buttons.setBounds(0, panel_RowsAndColumns.getY() + panel_RowsAndColumns.getHeight() + 10,
                button_OK.getWidth() + 10 + button_Cancel.getWidth(), button_Cancel.getHeight());
        panel_Buttons.add(button_OK);
        panel_Buttons.add(button_Cancel);

        this.setTitle("Advanced Grid Setup");
        this.getContentPane().setLayout(null);
        int largest_width = 0;
        int largest_widthB = 0;
        if (panel_Angle.getWidth() > largest_width)
        {
            largest_width = panel_Angle.getWidth();
            largest_widthB = panel_Angle.getWidth();
        }
        if (panel_RowsAndColumns.getWidth() > largest_width)
        {
            largest_width = panel_RowsAndColumns.getWidth();
            largest_widthB = panel_RowsAndColumns.getWidth();
        }
        if (panel_Buttons.getWidth() > largest_width)
        {
            largest_widthB = panel_Buttons.getWidth();
        }
        if (panel_HeightAndWidth.getWidth() > largest_width)
        {
            largest_width = panel_HeightAndWidth.getWidth();
            largest_widthB = panel_HeightAndWidth.getWidth();
        }
        if (panel_TopLeftSpot.getWidth() > largest_width)
        {
            largest_width = panel_TopLeftSpot.getWidth();
            largest_widthB = panel_TopLeftSpot.getWidth();
        }
        this.getContentPane().setBounds(this.getContentPane().getX(), this.getContentPane().getY(), largest_widthB + 20,
                panel_Buttons.getY() + panel_Buttons.getHeight() + 20);
        int content_pane_width = this.getContentPane().getWidth();
        panel_TopLeftSpot.setLocation(content_pane_width / 2 - panel_TopLeftSpot.getWidth() / 2 + (largest_width -
                panel_TopLeftSpot.getWidth()) / 2, panel_TopLeftSpot.getY());
        panel_HeightAndWidth.setLocation(content_pane_width / 2 - panel_HeightAndWidth.getWidth() / 2 + (largest_width -
                panel_HeightAndWidth.getWidth()) / 2, panel_HeightAndWidth.getY());
        panel_Angle.setLocation(content_pane_width / 2 - panel_Angle.getWidth() / 2 + (largest_width -
                panel_Angle.getWidth()) / 2, panel_Angle.getY());
        panel_RowsAndColumns.setLocation(content_pane_width / 2 - panel_RowsAndColumns.getWidth() / 2 + (largest_width -
                panel_RowsAndColumns.getWidth()) / 2, panel_RowsAndColumns.getY());
        panel_Buttons.setLocation(content_pane_width / 2 - panel_Buttons.getWidth() / 2, panel_Buttons.getY());

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screen.width)/2-200,(screen.height)/2-150);

        this.getContentPane().add(panel_TopLeftSpot);
        this.getContentPane().add(panel_HeightAndWidth);
        this.getContentPane().add(panel_Angle);
        this.getContentPane().add(panel_RowsAndColumns);
        this.getContentPane().add(panel_Buttons);
    }

    public void setOptions(int topLeftX, int topLeftY, int height, int width, double angle, int columns, int rows)
    {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.gridHeight = height;
        this.gridWidth = width;
        this.angle = angle;
        this.columns = columns;
        this.rows = rows;
        textField_TopLeftSpotX.setText("" + topLeftX);
        textField_TopLeftSpotY.setText("" + topLeftY);
        textField_Height.setText("" + height);
        textField_Width.setText("" + width);
        textField_Angle.setText("" + (angle * 100));
        textField_Columns.setText("" + columns);
        textField_Rows.setText("" + rows);
    }

    /**
     * returns whether or not the user has hit the ok button
     * @return whether or not the user has hit the ok button
     */
    public boolean getOK()
    {
        return ok;
    }

    //handles the ok button and ensures all fields entered correctly
    private void okButton_actionPerformed()
    {
        try
        {
            topLeftX = Integer.parseInt(textField_TopLeftSpotX.getText().trim());
            topLeftY = Integer.parseInt(textField_TopLeftSpotY.getText().trim());
            gridHeight = Integer.parseInt(textField_Height.getText().trim());
            gridWidth = Integer.parseInt(textField_Width.getText().trim());
            angle = Double.parseDouble(textField_Angle.getText().trim()) / 100;
            columns = Integer.parseInt(textField_Columns.getText().trim());
            rows = Integer.parseInt(textField_Rows.getText().trim());
            ok = true;
            this.dispose();
        }
        catch(NumberFormatException e1)
        {
            JOptionPane.showMessageDialog(this, "Please Enter Integer Value For Each Field");
        }
    }

    //handles the cancel button
    private void cancelButton_actionPerformed()
    {
        this.dispose();
    }

    public int getGridTopLeftX()
    {
        return topLeftX;
    }

    public int getGridTopLeftY()
    {
        return topLeftY;
    }

    public int getGridHeight()
    {
        return gridHeight;
    }

    public int getGridWidth()
    {
        return gridWidth;
    }

    public int getGridColumns()
    {
        return columns;
    }

    public int getGridRows()
    {
        return rows;
    }

    public double getGridAngle()
    {
        return angle;
    }
}