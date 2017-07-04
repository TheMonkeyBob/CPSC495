package application.gui;

import ij.ImagePlus;

import application.GeneImageAspect;
import application.internal.Engine;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

class TabPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public TabPanel(MainWindow main, Engine engine, int number) {
        this.main = main;
        this.engine = engine;
        this.myNumber = number;
        setup();
    }

    private SegmentDisplay sdGreenSlide;
    private SegmentDisplay sdRedSlide;
    private int myNumber;

    private DecimalFormat df = new DecimalFormat("###.####");
    protected int ratioMethod;

    private JScrollPane gridScrollPane;
    private JPanel gridScrollPanePanel;
    private ArrayList<GridPanelPanel> gridPanelsList = new ArrayList<>();
    private Border blackline = BorderFactory.createLineBorder(Color.black);
    private JTextArea textArea_1;
    private JTextArea textArea_2;
    private JTextArea textArea_3;
    private JTextArea textArea_4;
    private JTextArea textArea_5;
    private ButtonGroup group1;
    private ButtonGroup group2;
    private ImageDisplayPanel imageDisplayPanel;
    protected MainWindow main;
    private JComboBox<String> comboBox;
    private JSlider slidderSeededThreshold;
    private JSpinner spnGrid;
    private JSpinner spnSpot;
    private SegmentPanel segment;

    public JScrollPane scrollGreen;
    private JScrollPane scrollRed;
    private JPanel greenPanel = new JPanel();
    private JPanel redPanel = new JPanel();
    private ImagePlus ipGrn;
    private ImagePlus ipRed;
    private int segmentMode;
    private GridPanel panel_Grid;

    private JScrollPane scrollPane_MainImage;

    private ImageDisplay imageDisplay;

    /**Automatic Flagging Options Dialog associated with the SegmentFrame associated with this SegmentPanel*/
    //protected AutoFlaggingOptionsDialog afod;
    /**flag manager associated with these grids*/
    //protected FlagManager flagman;

    public JSplitPane jSplitPaneVert = new JSplitPane();

    protected double cellHeight;

    private Engine engine;

    private int gridCount = 0;
    private int selectedProfileNum = 0;
    private boolean comboboxIsChanging = false;
    private boolean segmentationIsChanging = false;

    /** polygon containing the coordinates of current spot cell */
    protected Polygon cell;
    private int newTopLeftX, newTopLeftY, gridNum, spotNum;

    private void setup() {
        ratioMethod = GeneImageAspect.TOTAL_SIGNAL;

        this.setBorder(blackline);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 550, 325, 325 };
        gbl_panel.rowHeights = new int[] { 5, 290, 100, 440, 35 };
        gbl_panel.columnWeights = new double[] { 1.0 };
        gbl_panel.rowWeights = new double[] { Double.MIN_VALUE };
        this.setLayout(gbl_panel);

        GridBagConstraints gbc_scrollField = new GridBagConstraints();
        gbc_scrollField.insets = new Insets(5, 5, 5, 5);
        gbc_scrollField.fill = GridBagConstraints.BOTH;
        gbc_scrollField.gridx = 0;
        gbc_scrollField.gridy = 0;
        gbc_scrollField.gridheight = 4;
        gbc_scrollField.weightx = 1.0;
        gbc_scrollField.weighty = 1.0;
        gbc_scrollField.insets = new Insets(5, 5, 0, 0); // top, left,
        // bottom,
        // right
        imageDisplay = new ImageDisplay(myNumber, engine, this);
        this.add(imageDisplay, gbc_scrollField);
        // textField.setColumns(10);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        GridBagConstraints gbc_slider = new GridBagConstraints();
        gbc_slider.fill = GridBagConstraints.HORIZONTAL;
        gbc_slider.insets = new Insets(0, 0, 0, 5);
        gbc_slider.gridx = 0;
        gbc_slider.gridy = 4;
        this.add(slider, gbc_slider);

        JLabel lblZoomLevel = new JLabel("Zoom Level");
        GridBagConstraints gbc_lblZoomLevel = new GridBagConstraints();
        gbc_lblZoomLevel.anchor = GridBagConstraints.WEST;
        gbc_lblZoomLevel.insets = new Insets(0, 0, 0, 5);
        gbc_lblZoomLevel.gridx = 1;
        gbc_lblZoomLevel.gridy = 4;
        this.add(lblZoomLevel, gbc_lblZoomLevel);

        // Gridding panel starts here
        panel_Grid = new GridPanel(main, this);
        TitledBorder grid_title = BorderFactory.createTitledBorder(blackline, "Gridding");
        grid_title.setTitleJustification(TitledBorder.LEFT);
        panel_Grid.setBorder(grid_title);
        panel_Grid.setLayout(null);
        GridBagConstraints gbc_gridding = new GridBagConstraints();
        gbc_gridding.insets = new Insets(0, 5, 5, 0);
        gbc_gridding.fill = GridBagConstraints.BOTH;
        gbc_gridding.gridx = 1;
        gbc_gridding.gridy = 1;
        gbc_gridding.gridheight = 2;
        gbc_gridding.gridwidth = 2;
        gbc_gridding.weightx = 1.0;
        gbc_gridding.weighty = 1.0;
        gbc_gridding.insets = new Insets(0, 5, 5, 5); // top, left, bottom,
        // right
        this.add(panel_Grid, gbc_gridding);

        // Segmentation panel starts here
        segment = new SegmentPanel(main, myNumber, engine);
        TitledBorder seg_title = BorderFactory.createTitledBorder(blackline, "Segmentation");
        seg_title.setTitleJustification(TitledBorder.LEFT);
        segment.setBorder(seg_title);
        segment.setLayout(null);
        GridBagConstraints gbc_segment = new GridBagConstraints();
        gbc_segment.insets = new Insets(0, 5, 5, 0);
        gbc_segment.fill = GridBagConstraints.BOTH;
        gbc_segment.gridx = 1;
        gbc_segment.gridy = 3;
        gbc_segment.gridheight = 1;
        gbc_segment.gridwidth = 2;
        gbc_segment.weightx = 1.0;
        gbc_segment.weighty = 1.0;
        gbc_segment.insets = new Insets(0, 5, 5, 5); // top, left, bottom, right
        this.add(segment, gbc_segment);
    }

    public void addGrid(int tlX, int tlY, int trX, int trY, int bX, int bY, int row, int col)
    {
        gridCount++;
        engine.addSample_Grid(myNumber, tlX, tlY, trX, trY, bX, bY, row, col);
        imageDisplay.addGrid();
        refreshSegmentation();
    }

    public void coordinateFound(int x, int y)
    {
        panel_Grid.coordinatesFound(x, y);
    }

    public void addToComboBox(String s, boolean focused)
    {
        comboBox.addItem(s);
        if (focused)
        {
            comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
        }
    }

    public void changeInComboBox(String s, int i)
    {
        boolean refocus = false;
        if (comboBox.getSelectedIndex() == i)
        {
            refocus = true;
        }
        comboBox.removeItemAt(i);
        comboBox.insertItemAt(s, i);
        if (refocus)
        {
            comboBox.setSelectedIndex(i);
            //engine.setSample_GridCount(myNumber, engine.getGridProfile_Number(i));
            engine.setSample_GridHorizontal(myNumber, engine.getGridProfile_Horizontal(i));
            engine.setSample_GridVertical(myNumber, engine.getGridProfile_Vertical(i));
            engine.setSample_GridDirection(myNumber, engine.getGridProfile_Direction(i));
            //updateGridCount();
        }
    }

    public void removeFromComboBox(int i)
    {
        if (i == comboBox.getSelectedIndex())
        {
            comboBox.setSelectedIndex(0);
            //engine.setSample_GridCount(myNumber, 0);
            //updateGridCount();
        }
        comboBox.removeItemAt(i);
    }

    private void updateGridCount()
    {
        while (engine.getSample_GridCount(myNumber) < gridPanelsList.size())
        {
            gridScrollPanePanel.remove(gridPanelsList.get(gridPanelsList.size() - 1));
            gridPanelsList.remove(gridPanelsList.size() - 1);
            gridCount--;
        }

        while (engine.getSample_GridCount(myNumber) > gridPanelsList.size())
        {
            gridCount++;
            //GridPanelPanel gp = new GridPanelPanel(this, gridCount);
            //gridPanelsList.add(gp);
            //gridScrollPanePanel.add(gp);
        }

        gridScrollPanePanel.revalidate();
        gridScrollPanePanel.repaint();
        imageDisplayPanel.repaint();
        if (gridCount < (int)spnGrid.getValue())
        {
            segmentationIsChanging = true;
            spnGrid.setValue(1);
            spnSpot.setValue(1);
            segmentationIsChanging = false;
        }
        refreshSegmentation();
    }

    protected void refreshSegmentation()
    {
        segment.updateSegmentation();
        /*
        if (gridCount < 1 && sdGreenSlide != null)
        {
            segment.remove(scrollGreen);
            segment.remove(scrollRed);
            sdGreenSlide = null;
            sdRedSlide = null;
            scrollRed = null;
            scrollGreen = null;
            textArea_3.setText("");
            textArea_4.setText("");
            textArea_5.setText("");
            segment.repaint();
        }
        if (sdGreenSlide != null)
        {
            moveTo((int)spnGrid.getValue(), (int)spnSpot.getValue());
            updateGeneInfo(segmentMode);
            sdGreenSlide.repaint();
            sdRedSlide.repaint();
        }
        */
    }

    private void segmentationMode(JPanel target)
    {
        if (sdGreenSlide == null && engine.getSample_GridCount(myNumber) > 0
                && engine.getSample_Grid_Columns(myNumber, 0) > 0)
        {
            sdGreenSlide = new SegmentDisplay(myNumber, true, engine);
            sdGreenSlide.setBounds(-10, -50, 200, 200);

            sdRedSlide = new SegmentDisplay(myNumber, false, engine);
            sdRedSlide.setBounds(-20, -100, 200, 200);

            scrollGreen = new JScrollPane(sdGreenSlide);
            scrollGreen.setBounds(10, 45, 200, 200);
            target.add(scrollGreen);

            scrollRed = new JScrollPane(sdRedSlide);
            scrollRed.setBounds(215, 45, 200, 200);
            target.add(scrollRed);

            target.repaint();

            scrollRed.repaint();
            scrollGreen.repaint();

            sdRedSlide.zoom(10);
            sdGreenSlide.zoom(10);

            showCurrentCell();
            refreshSegmentation();
        }
    }

    public void moveTo(int grid, int spot)
    {
        setSpot(grid - 1, spot - 1); // zero based
        showCurrentCell();
    }

    /**
     * sets the current spot to the specified grid and (transformed) spot number
     * @param grid grid number
     * @param spot transformed spot number
     */
    public void setSpot(int grid, int spot) {
        if (grid >= 0 && grid < engine.getSample_GridCount(myNumber) && spot >= 0
                && spot < engine.getSample_Grid_NumberOfSpots(myNumber, grid))
        {
            gridNum = grid;
            spotNum = spot;
            engine.setSample_CurrentGrid(myNumber, grid);
            engine.setSample_CurrentSpot(myNumber, engine.getSample_Grid_ActualSpotNum(myNumber, grid, spot));
            showCurrentCell();
        }
    }

    /**
     * sets the segement displays to show the current spot cells for both the
     * red and green image
     */
    public void showCurrentCell()
    {
        setCurrentCell();
        newTopLeftX = ((int) (sdGreenSlide.getZoom() * cell.xpoints[0])) - 4;
        newTopLeftY = ((int) (sdGreenSlide.getZoom() * cell.ypoints[0])) - 4;
        scrollGreen.getViewport().setViewPosition(new Point(newTopLeftX, newTopLeftY));
        scrollRed.getViewport().setViewPosition(new Point(newTopLeftX, newTopLeftY));

        sdGreenSlide.repaint();
        sdRedSlide.repaint();
    }

    /**
     * sets the magnification in both segment displays to the spot level
     */
    public void zoomToCell()
    {
        sdGreenSlide
                .zoom(((jSplitPaneVert.getHeight() - jSplitPaneVert.getDividerSize() - (2 * redPanel.getHeight())) / 2)
                        / cellHeight);
        sdRedSlide
                .zoom(((jSplitPaneVert.getHeight() - jSplitPaneVert.getDividerSize() - (2 * redPanel.getHeight())) / 2)
                        / cellHeight);
        showCurrentCell();
    }

    /**
     * sets the current cell coordinates
     */
    public void setCurrentCell()
    {
        Polygon p = engine.getSample_Grid_Polygon_Outline(myNumber, engine.getSample_CurrentGridNum(myNumber));
        Polygon q = new Polygon();
        if (p != null)
        {
            for (int j = 0; j < p.xpoints.length; j++)
            {
                q.xpoints[j] = (int) ((sdGreenSlide.screenX(p.xpoints[j])) / sdGreenSlide.getZoom());
                q.ypoints[j] = (int) ((sdGreenSlide.screenX(p.ypoints[j])) / sdGreenSlide.getZoom());
            }
            engine.setSample_Grid_Spots(myNumber, engine.getSample_CurrentGridNum(myNumber), q);
            cell = engine.getSample_Grid_CurrentSpot(myNumber, engine.getSample_CurrentGridNum(myNumber));
        }
        cellHeight = cell.ypoints[3] - cell.ypoints[0];
    }

    protected void updateGeneInfo(int i){
        if(i == GeneImageAspect.SEEDED_REGION)
        {
            Object[] params = new Object[1];
            params[0] = slidderSeededThreshold.getValue();
            engine.setSample_Gene_Data(myNumber, sdRedSlide.getCellPixels(),sdGreenSlide.getCellPixels(),
                    sdRedSlide.getCellHeight(), sdRedSlide.getCellWidth(), 1, params);
            sdRedSlide.setSeededRegion(engine.getSample_Gene_CenterSpots(myNumber, true));
            sdGreenSlide.setSeededRegion(engine.getSample_Gene_CenterSpots(myNumber, false));
        }
        else if(i == GeneImageAspect.ADAPTIVE_CIRCLE)
        {
            Object[] params = new Object[3];
            params[0] = 3;
            params[1] = 8;
            params[2] = slidderSeededThreshold.getValue();
            engine.setSample_Gene_Data(myNumber, sdRedSlide.getCellPixels(),sdGreenSlide.getCellPixels(),
                    sdRedSlide.getCellHeight(), sdRedSlide.getCellWidth(), 2, params);
            sdRedSlide.setAdaptiveCircle(engine.getSample_Gene_CenterAndRadius(myNumber));
            sdGreenSlide.setAdaptiveCircle(engine.getSample_Gene_CenterAndRadius(myNumber));
        }

        if(i == GeneImageAspect.SEEDED_REGION || i == GeneImageAspect.ADAPTIVE_CIRCLE)
        {
            String flagText = "N/A";
            if(ratioMethod==GeneImageAspect.TOTAL_SIGNAL||ratioMethod==GeneImageAspect.TOTAL_SUBTRACT_BG)
            {
                textArea_3.setText("Green BG Total: " + df.format(engine.getSample_Gene_GreenBackgroundTotal(myNumber)) +
                        "\nGreen FG Total: " +  df.format(engine.getSample_Gene_GreenForegroundTotal(myNumber)) +
                        "\nRed BG Total: " + df.format(engine.getSample_Gene_RedBackgroundTotal(myNumber)) +
                        "\nRed FG Total: " + df.format(engine.getSample_Gene_RedForegroundTotal(myNumber)));
            }
            else
            {
                textArea_3.setText("Green BG Avg: " + df.format(engine.getSample_Gene_GreenBackgroundAvg(myNumber)) +
                        "\nGreen FG Avg: " + df.format(engine.getSample_Gene_GreenForegroundAvg(myNumber)) +
                        "\nRed BG Avg: " + df.format(engine.getSample_Gene_RedBackgroundAvg(myNumber)) +
                        "\nRed FG Avg: " + df.format(engine.getSample_Gene_RedForegroundAvg(myNumber)));
            }
            textArea_3.setText(textArea_3.getText() + "\nRatio: " + df.format(engine.getSample_Gene_Ratio(myNumber, ratioMethod)));
            textArea_3.setText(textArea_3.getText() + "\nFlagging Status: " + flagText);
        }
        else
        {
            textArea_4.setText("Ratio: N/A \nGreen Background: N/A \nGreen Foreground: N/A \nRed Background: N/A \nRed Foreground: N/A");
        }
    }

    public void setGridMode(int i)
    {
        imageDisplay.setGridMode(i);
    }

    public void zoomToGrid()
    {
        imageDisplay.zoomToCurrentGrid();
    }
}