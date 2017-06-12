package application.gui;

import ij.ImagePlus;

import application.GeneImageAspect;
import application.internal.Engine;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
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
    private ArrayList<GridPanel> gridPanelsList = new ArrayList<>();
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
    private JPanel segment;

    public JScrollPane scrollGreen;
    private JScrollPane scrollRed;
    private JPanel greenPanel = new JPanel();
    private JPanel redPanel = new JPanel();
    private ImagePlus ipGrn;
    private ImagePlus ipRed;
    private int segmentMode;

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
    private int w, h, newTopLeftX, newTopLeftY, gridNum, spotNum;

    private void setup() {
        ratioMethod = GeneImageAspect.TOTAL_SIGNAL;

        this.setBorder(blackline);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 550, 325, 325 };
        gbl_panel.rowHeights = new int[] { 5, 290, 290, 290, 35 };
        gbl_panel.columnWeights = new double[] { 1.0 };
        gbl_panel.rowWeights = new double[] { Double.MIN_VALUE };
        this.setLayout(gbl_panel);

        imageDisplayPanel = new ImageDisplayPanel(engine, buildImage(), myNumber);
        imageDisplayPanel.getCanvas().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                coordinateFound(xCoordinate(e.getX()), yCoordinate(e.getY()));
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        JScrollPane scroll = new JScrollPane(imageDisplayPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        this.add(scroll, gbc_scrollField);
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
        JPanel gridding = new JPanel();
        TitledBorder grid_title = BorderFactory.createTitledBorder(blackline, "Gridding");
        grid_title.setTitleJustification(TitledBorder.LEFT);
        gridding.setBorder(grid_title);
        gridding.setLayout(null);
        GridBagConstraints gbc_gridding = new GridBagConstraints();
        gbc_gridding.insets = new Insets(0, 5, 5, 0);
        gbc_gridding.fill = GridBagConstraints.BOTH;
        gbc_gridding.gridx = 1;
        gbc_gridding.gridy = 1;
        gbc_gridding.gridheight = 1;
        gbc_gridding.gridwidth = 2;
        gbc_gridding.weightx = 1.0;
        gbc_gridding.weighty = 1.0;
        gbc_gridding.insets = new Insets(0, 5, 5, 5); // top, left, bottom,
        // right
        this.add(gridding, gbc_gridding);

        JLabel lblNewLabel = new JLabel("Select a previously saved grid or click Add to create a new one.");
        lblNewLabel.setBounds(10, 20, 360, 14);
        gridding.add(lblNewLabel);

        comboBox = new JComboBox<>();
        comboBox.addItem("Previously saved grid.");
        comboBox.setBounds(10, 45, 150, 20);
        comboBox.addActionListener(changedSelection -> {
            if (!comboboxIsChanging && comboBox.getSelectedIndex() != selectedProfileNum)
            {
                if (comboBox.getSelectedIndex() == 0)
                {
                    comboBox.setSelectedIndex(selectedProfileNum);
                }
                else
                {
                    comboboxIsChanging = true;
                    if (selectedProfileNum != 0)
                    {
                        int selection = JOptionPane.showOptionDialog(this,
                                "Changing the profile may remove grids from this sample.\n"
                                        + "This cannot be undone. Are you sure you want to change the profile?",
                                "Profile Change!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null,
                                null);
                        if (selection == JOptionPane.OK_OPTION) {
                            int i = comboBox.getSelectedIndex()-1;
                            engine.setSample_GridCount(myNumber, engine.getGridProfile_Number(i));
                            engine.setSample_GridHorizontal(myNumber, engine.getGridProfile_Horizontal(i));
                            engine.setSample_GridVertical(myNumber, engine.getGridProfile_Vertical(i));
                            engine.setSample_GridDirection(myNumber, engine.getGridProfile_Direction(i));
                            selectedProfileNum = i;
                            updateGridCount();
                        }
                        else
                        {
                            comboBox.setSelectedIndex(selectedProfileNum);
                        }
                    }
                    else
                    {
                        int i = comboBox.getSelectedIndex();
                        engine.setSample_GridCount(myNumber, engine.getGridProfile_Number(i));
                        engine.setSample_GridHorizontal(myNumber, engine.getGridProfile_Horizontal(i));
                        engine.setSample_GridVertical(myNumber, engine.getGridProfile_Vertical(i));
                        engine.setSample_GridDirection(myNumber, engine.getGridProfile_Direction(i));
                        selectedProfileNum = i;
                        updateGridCount();
                    }
                    comboboxIsChanging = false;
                }
            }
        });
        gridding.add(comboBox);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(addBut -> {
            GridSetupDialog god = new GridSetupDialog(main);
            god.setOptions("", engine.getSample_GridCount(myNumber), engine.getSample_GridHorizontal(myNumber),
                    engine.getSample_GridVertical(myNumber), engine.getSample_GridDirection(myNumber));
            god.setModal(true);
            god.pack();
            god.setVisible(true);
            if (god.getOK()) {
                comboboxIsChanging = true;
                int cont = JOptionPane.YES_OPTION;
                // TODO Deletion warning commented out for now
                // if(numDelete>0) cont =
                // JOptionPane.showConfirmDialog(this.getDesktopPane(),
                // "Warning! You Have Selected " + numDelete + " Fewer Grids For
                // Your Image.\nDo You Wish To Delete " + (numDelete==1? "This
                // Grid And All Data Related To It?":"These " + numDelete + "
                // Grids And All Data Related To Them"), "Warning! You May Be
                // Deleting Important Data", JOptionPane.YES_NO_OPTION,
                // JOptionPane.WARNING_MESSAGE);
                if (cont == JOptionPane.YES_OPTION) {
                    engine.setSample_GridCount(myNumber, god.getGridNum());
                    engine.setSample_GridHorizontal(myNumber, god.getHorizontal());
                    engine.setSample_GridVertical(myNumber, god.getVertical());
                    engine.setSample_GridDirection(myNumber, god.getFirstSpot());

                    updateGridCount();
                    main.addGridProfile(god.getProfileName(), god.getGridNum(), god.getHorizontal(), god.getVertical(),
                            god.getFirstSpot());
                }
                selectedProfileNum = comboBox.getItemCount()-1;
                comboboxIsChanging = false;
            }
        });
        btnAdd.setBounds(169, 45, 60, 23);
        gridding.add(btnAdd);

        JButton btnModify = new JButton("Modify");
        btnModify.addActionListener(modBut -> {
            if (comboBox.getSelectedIndex() != 0) {
                GridSetupDialog god = new GridSetupDialog(main);
                god.setOptions(comboBox.getItemAt(comboBox.getSelectedIndex()), engine.getSample_GridCount(myNumber),
                        engine.getSample_GridHorizontal(myNumber), engine.getSample_GridVertical(myNumber),
                        engine.getSample_GridDirection(myNumber));
                god.setModal(true);
                god.pack();
                god.setVisible(true);
                if (god.getOK()) {
                    comboboxIsChanging = true;
                    int cont = JOptionPane.YES_OPTION;
                    // TODO Deletion warning commented out for now
                    // if(numDelete>0) cont =
                    // JOptionPane.showConfirmDialog(this.getDesktopPane(),
                    // "Warning! You Have Selected " + numDelete + " Fewer Grids
                    // For Your Image.\nDo You Wish To Delete " + (numDelete==1?
                    // "This Grid And All Data Related To It?":"These " +
                    // numDelete + " Grids And All Data Related To Them"),
                    // "Warning! You May Be Deleting Important Data",
                    // JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (cont == JOptionPane.YES_OPTION) {
                        main.modifyGridProfile(comboBox.getSelectedIndex()-1, god.getProfileName(), god.getGridNum(),
                                god.getHorizontal(), god.getVertical(), god.getFirstSpot());
                    }
                    comboboxIsChanging = false;
                }
            }
        });
        btnModify.setBounds(240, 45, 75, 23);
        gridding.add(btnModify);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(deleteBut -> {
            if (comboBox.getSelectedIndex() != 0) {
                comboboxIsChanging = true;
                int selection = JOptionPane.showOptionDialog(this,
                        "This will delete the selected profile for all samples.\n"
                                + "All grids connected to the profile will be removed.\n"
                                + "This cannot be undone. Are you sure you want to delete?",
                        "Delete Grid Profile!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null,
                        null);
                if (selection == JOptionPane.OK_OPTION) {
                    main.removeGridProfile(comboBox.getSelectedIndex()-1);
                }
                selectedProfileNum = 0;
                comboboxIsChanging = false;
            }
        });
        btnDelete.setBounds(324, 45, 70, 23);
        gridding.add(btnDelete);

        gridScrollPanePanel = new JPanel();
        gridScrollPane = new JScrollPane(gridScrollPanePanel);
        gridScrollPane.setBounds(10, 70, 600, 100);
        gridding.add(gridScrollPane);

        // Segmentation panel starts here

        segment = new JPanel();
        TitledBorder seg_title = BorderFactory.createTitledBorder(blackline, "Segmentation");
        seg_title.setTitleJustification(TitledBorder.LEFT);
        segment.setBorder(seg_title);
        segment.setLayout(null);
        GridBagConstraints gbc_segment = new GridBagConstraints();
        gbc_segment.insets = new Insets(0, 5, 5, 0);
        gbc_segment.fill = GridBagConstraints.BOTH;
        gbc_segment.gridx = 1;
        gbc_segment.gridy = 2;
        gbc_segment.gridheight = 1;
        gbc_segment.gridwidth = 2;
        gbc_segment.weightx = 1.0;
        gbc_segment.weighty = 1.0;
        gbc_segment.insets = new Insets(0, 5, 5, 5); // top, left, bottom, right
        this.add(segment, gbc_segment);

        JLabel lblNewLabel_1 = new JLabel("Choose one of the following segmentation options:");
        lblNewLabel_1.setBounds(10, 20, 300, 14);
        segment.add(lblNewLabel_1);

        ipGrn = engine.getSample_GreenImagePlus(myNumber);
        ipRed = engine.getSample_RedImagePlus(myNumber);
        //Image green = ipGrn.getImage();
        //Image red = ipRed.getImage();

        JRadioButton rdbtnNewRadioButton = new JRadioButton("Adaptive Circle");
        rdbtnNewRadioButton.setBounds(310, 16, 110, 23);
        rdbtnNewRadioButton.addActionListener(adaptiveCircle -> {
            segmentationMode(segment);
            segmentMode = GeneImageAspect.ADAPTIVE_CIRCLE;
            engine.setSample_SegmentationMethod(myNumber, segmentMode);
            refreshSegmentation();
        });
        segment.add(rdbtnNewRadioButton);

        JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Seeded Region Growing");
        rdbtnNewRadioButton_1.setBounds(420, 16, 160, 23);
        rdbtnNewRadioButton_1.addActionListener(seededRegionButton -> {
            segmentationMode(segment);
            segmentMode = GeneImageAspect.SEEDED_REGION;
            engine.setSample_SegmentationMethod(myNumber, segmentMode);
            refreshSegmentation();
        });

        segment.add(rdbtnNewRadioButton_1);

        // Button group so only one radio button can be active at one time.

        group1 = new ButtonGroup();
        group1.add(rdbtnNewRadioButton);
        group1.add(rdbtnNewRadioButton_1);

        JLabel lblThreshold = new JLabel("Threshold");
        lblThreshold.setBounds(430, 50, 60, 14);
        segment.add(lblThreshold);

        JLabel lblSpinnerGrid = new JLabel("Grid");
        lblSpinnerGrid.setBounds(430, 110, 40, 14);
        segment.add(lblSpinnerGrid);

        SpinnerModel spinnerGrid = new SpinnerNumberModel(1, // initial value
                1, // min
                100, // max
                1);// step

        spnGrid = new JSpinner(spinnerGrid);
        spnGrid.setBounds(480, 110, 40, 20);
        segment.add(spnGrid);

        spnGrid.addChangeListener(changePosition -> {
            if (sdGreenSlide != null && !segmentationIsChanging) {
                moveTo((Integer) spnGrid.getValue(), (Integer) spnSpot.getValue());
                updateGeneInfo(segmentMode);
            }
        });

        JLabel lblSpinnerSpot = new JLabel("Spot");
        lblSpinnerSpot.setBounds(430, 150, 40, 14);
        segment.add(lblSpinnerSpot);

        SpinnerModel spinnerSpot = new SpinnerNumberModel(1, // initial
                // value
                1, // min
                552, // max
                1);// step

        spnSpot = new JSpinner(spinnerSpot);
        spnSpot.setBounds(480, 150, 40, 20);
        segment.add(spnSpot);

        spnSpot.addChangeListener(changePosition -> {
            if (sdGreenSlide != null && !segmentationIsChanging) {
                moveTo((Integer) spnGrid.getValue(), (Integer) spnSpot.getValue());
                updateGeneInfo(segmentMode);
            }
        });
        JCheckBox chckbxNewCheckBox = new JCheckBox("Flag spot");
        chckbxNewCheckBox.setBounds(530, 150, 97, 23);
        segment.add(chckbxNewCheckBox);

        slidderSeededThreshold = new JSlider(JSlider.HORIZONTAL, 5, 50, 10);
        slidderSeededThreshold.setMajorTickSpacing(15);
        slidderSeededThreshold.setMinorTickSpacing(5);
        slidderSeededThreshold.setPaintTicks(true);
        slidderSeededThreshold.setPaintLabels(true);
        slidderSeededThreshold.setBounds(500, 50, 150, 40);
        segment.add(slidderSeededThreshold);
        slidderSeededThreshold.addChangeListener(seededThresholdChange -> {
            if (sdGreenSlide != null) {
                updateGeneInfo(segmentMode);
                engine.setSample_MethodThreshold(myNumber, slidderSeededThreshold.getValue());
                sdGreenSlide.repaint();
                sdRedSlide.repaint();
            }
        });

        JLabel lblGreen = new JLabel("Green");
        lblGreen.setBounds(90, 250, 40, 14);
        segment.add(lblGreen);

        JLabel lblRed = new JLabel("Red");
        lblRed.setBounds(300, 250, 40, 14);
        segment.add(lblRed);

        // handler code

        // Expression panel starts here

        JPanel expression = new JPanel();
        TitledBorder exp_title = BorderFactory.createTitledBorder(blackline, "Gene Expression Ratios");
        exp_title.setTitleJustification(TitledBorder.LEFT);
        expression.setBorder(exp_title);
        expression.setLayout(null);
        GridBagConstraints gbc_expression = new GridBagConstraints();
        gbc_expression.insets = new Insets(0, 5, 5, 0);
        gbc_expression.fill = GridBagConstraints.BOTH;
        gbc_expression.gridx = 1;
        gbc_expression.gridy = 3;
        gbc_expression.gridheight = 1;
        gbc_expression.gridwidth = 2;
        gbc_expression.weightx = 1.0;
        gbc_expression.weighty = 1.0;
        gbc_expression.insets = new Insets(0, 5, 0, 5); // top, left,
        // bottom,
        // right
        this.add(expression, gbc_expression);

        JLabel lblNewLabel_2 = new JLabel("Select the colour representing the control in this microarray:");
        lblNewLabel_2.setBounds(10, 20, 350, 14);
        expression.add(lblNewLabel_2);

        JRadioButton rdbtnGreen = new JRadioButton("Green");
        rdbtnGreen.setBounds(370, 16, 60, 23);
        expression.add(rdbtnGreen);

        JRadioButton rdbtnRed = new JRadioButton("Red");
        rdbtnRed.setBounds(460, 16, 60, 23);
        expression.add(rdbtnRed);

        // Button group so only one radio button can be active at one time.

        group2 = new ButtonGroup();
        group2.add(rdbtnGreen);
        group2.add(rdbtnRed);

        JLabel lblNewLabel_3 = new JLabel("Select a method for calculating the gene expression levels:");
        lblNewLabel_3.setBounds(10, 60, 350, 14);
        expression.add(lblNewLabel_3);

        String[] signal = { "Total Signal", "Average Signal", "Total Signal BG Subtraction", "Average Signal BG Subtraction" };
        JComboBox<String> comboBox_1 = new JComboBox<String>(signal);
        comboBox_1.setBounds(360, 58, 200, 20);
        expression.add(comboBox_1);

        comboBox_1.addActionListener(combo_1 -> {
            if (sdGreenSlide != null){
                switch (comboBox_1.getSelectedIndex()) {
                    case 0:
                        ratioMethod = GeneImageAspect.TOTAL_SIGNAL;
                        break;
                    case 1:
                        ratioMethod = GeneImageAspect.AVG_SIGNAL;
                        break;
                    case 2:
                        ratioMethod = GeneImageAspect.TOTAL_SUBTRACT_BG;
                        break;
                    case 3:
                        ratioMethod = GeneImageAspect.AVG_SUBTRACT_BG;
                        break;
                }
                engine.setSample_RatioMethod(myNumber, ratioMethod);

                updateGeneInfo(segmentMode);
            }
        });
        comboBox_1.setSelectedIndex(0);

        textArea_3 = new JTextArea();
        textArea_3.setBounds(10, 85, 140, 140);
        expression.add(textArea_3);
        textArea_3.setColumns(10);

        JLabel lblGreen_1 = new JLabel("Green");
        lblGreen_1.setBounds(60, 250, 40, 14);
        expression.add(lblGreen_1);

        textArea_4 = new JTextArea();
        textArea_4.setBounds(215, 85, 140, 140);
        expression.add(textArea_4);
        textArea_4.setColumns(10);

        JLabel lblRed_1 = new JLabel("Red");
        lblRed_1.setBounds(280, 250, 40, 14);
        expression.add(lblRed_1);

        textArea_5 = new JTextArea();
        textArea_5.setBounds(410, 85, 140, 140);
        expression.add(textArea_5);
        textArea_5.setColumns(10);

        JLabel lblCombined = new JLabel("Combined");
        lblCombined.setBounds(450, 250, 100, 14);
        expression.add(lblCombined);
    }

    private Image buildImage() {
        Image green = engine.getSample_GreenImage(myNumber);
        Image red = engine.getSample_RedImage(myNumber);

        Dimension redDim = new Dimension(red.getWidth(null), red.getHeight(null));
        Dimension greenDim = new Dimension(green.getWidth(null), green.getHeight(null));
        int w = greenDim.width;
        int h = greenDim.height;

        // Use green as base.
        int[] pixels = new int[w * h];
        int[] redpixels = new int[w * h];

        PixelGrabber pg = new PixelGrabber(green, 0, 0, w, h, pixels, 0, w);
        PixelGrabber redpg = new PixelGrabber(red, 0, 0, w, h, redpixels, 0, w);
        try {
            pg.grabPixels();
            redpg.grabPixels();
        } catch (Exception e) {
            System.out.print("(Error Grabbing Pixels) " + e);
        }

        for (int i = 0; i < pixels.length; i++) {
            int p = pixels[i];
            int redp = redpixels[i];
            int a = (p >> 24) & 0xFF;
            int r = (redp >> 16) & 0xFF;
            int b = 0;
            int g = (p >> 8) & 0xFF;

            pixels[i] = (a << 24 | r << 16 | g << 8 | b);
        }
        return createImage(new MemoryImageSource(w, h, pixels, 0, w));
    }

    protected void drawGrid(int num, int tlX, int tlY, int trX, int trY, int blX, int blY, int brX, int brY, int row, int col)
    {
        engine.setSample_Grid_AllFeatures(myNumber, num, tlX, tlY, trX, trY, blX, blY, brX, brY, row, col);

        imageDisplayPanel.repaint();
    }

    private void coordinateFound(int x, int y) {
        for (GridPanel gp : gridPanelsList) {
            if (gp.isAwaitingData()) {
                gp.addCoordinates(x, y);
            }
        }
    }

    /**
     * gets the actual x-coordinate on the image based on the screen
     * x-coordinate
     *
     * @param ex
     *            screen x-coordinate
     * @return actual x-coordinate on the image based on the screen x-coordinate
     */
    public int xCoordinate(int ex) {
        return ((imageDisplayPanel.getCanvas().getSrcRect().x
                + Math.round((float) ((ex) / imageDisplayPanel.getZoom()))));
    }

    /**
     * gets the actual y-coordinate on the image based on the screen
     * y-coordinate
     *
     * @param ey
     *            screen y-coordinate
     * @return actual y-coordinate on the image based on the screen y-coordinate
     */
    public int yCoordinate(int ey) {
        return ((imageDisplayPanel.getCanvas().getSrcRect().y
                + Math.round((float) ((ey) / imageDisplayPanel.getZoom()))));
    }

    public void addToComboBox(String s, boolean focused) {
        comboBox.addItem(s);
        if (focused) {
            comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
        }
    }

    public void changeInComboBox(String s, int i) {
        boolean refocus = false;
        if (comboBox.getSelectedIndex() == i) {
            refocus = true;
        }
        comboBox.removeItemAt(i);
        comboBox.insertItemAt(s, i);
        if (refocus) {
            comboBox.setSelectedIndex(i);
            engine.setSample_GridCount(myNumber, engine.getGridProfile_Number(i));
            engine.setSample_GridHorizontal(myNumber, engine.getGridProfile_Horizontal(i));
            engine.setSample_GridVertical(myNumber, engine.getGridProfile_Vertical(i));
            engine.setSample_GridDirection(myNumber, engine.getGridProfile_Direction(i));
            updateGridCount();
        }
    }

    public void removeFromComboBox(int i) {
        if (i == comboBox.getSelectedIndex()) {
            comboBox.setSelectedIndex(0);
            engine.setSample_GridCount(myNumber, 0);
            updateGridCount();
        }
        comboBox.removeItemAt(i);
    }

    private void updateGridCount() {
        while (engine.getSample_GridCount(myNumber) < gridPanelsList.size()) {
            gridScrollPanePanel.remove(gridPanelsList.get(gridPanelsList.size() - 1));
            gridPanelsList.remove(gridPanelsList.size() - 1);
            gridCount--;
        }

        while (engine.getSample_GridCount(myNumber) > gridPanelsList.size()) {
            gridCount++;
            GridPanel gp = new GridPanel(this, gridCount);
            gridPanelsList.add(gp);
            gridScrollPanePanel.add(gp);
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
    }

    private void segmentationMode(JPanel target) {

        if (sdGreenSlide == null && engine.getSample_GridCount(myNumber) > 0 && engine.getSample_Grid_Columns(myNumber, 0) > 0) {
            int[][] grnPixels = new int[ipGrn.getHeight()][ipGrn.getWidth()];
            int[][] redPixels = new int[ipRed.getHeight()][ipRed.getWidth()];
            for(int i=0; i<(int)ipGrn.getHeight(); i++){
                for (int j=0; j<(int)ipGrn.getWidth(); j++){
                    grnPixels[i][j]=ipGrn.getProcessor().getPixel(j,i);
                }
            }
            for(int i = 0; i < (int)ipRed.getHeight(); i++) {
                for(int j = 0; j < (int)ipRed.getWidth(); j++) {
                    redPixels[i][j] = ipRed.getProcessor().getPixel(j,i);
                }
            }


            sdGreenSlide = new SegmentDisplay(myNumber, ipGrn, engine);
            sdGreenSlide.setBounds(-10, -50, 200, 200);
            sdGreenSlide.RawPixels = grnPixels;

            sdRedSlide = new SegmentDisplay(myNumber, ipRed, engine);
            sdRedSlide.setBounds(-20, -100, 200, 200);
            sdRedSlide.RawPixels = redPixels;

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

            //manager.getCurrentGrid().setCurrentSpot(1);
            // zoomToCell();
            showCurrentCell();
            refreshSegmentation();
        }
    }

    public void moveTo(int grid, int spot) {
        setSpot(grid - 1, spot - 1); // zero based
        showCurrentCell();
    }

    /*
     * sets the current spot to the specified grid and (transformed) spot number
     *
     * @param grid grid number
     *
     * @param spot transformed spot number
     */
    public void setSpot(int grid, int spot) {
        if (grid >= 0 && grid < engine.getSample_GridCount(myNumber) && spot >= 0 && spot < engine.getSample_Grid_NumberOfSpots(myNumber, grid))
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
    public void showCurrentCell() {
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
    public void zoomToCell() {
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
    public void setCurrentCell() {
        Polygon p = engine.getSample_Grid_TranslatedPolygon(myNumber, engine.getSample_CurrentGridNum(myNumber));
        Polygon q = new Polygon();
        if (p != null) {
            for (int j = 0; j < p.xpoints.length; j++) {
                q.xpoints[j] = (int) ((sdGreenSlide.screenX(p.xpoints[j])) / sdGreenSlide.getZoom());
                q.ypoints[j] = (int) ((sdGreenSlide.screenX(p.ypoints[j])) / sdGreenSlide.getZoom());
            }
            engine.setSample_Grid_Spots(myNumber, engine.getSample_CurrentGridNum(myNumber), q);
            cell = engine.getSample_Grid_CurrentSpot(myNumber, engine.getSample_CurrentGridNum(myNumber));
        }
        cellHeight = cell.ypoints[3] - cell.ypoints[0];
    }

    protected void updateGeneInfo(int i){
/*        boolean flagStatus = flagman.checkFlag(gridNum, spotNum);
        int[] autoThresh;
        if (afod.getOK()) autoThresh = afod.getThresholds();
        else{
            autoThresh = new int[4];
            autoThresh[0] = -1;
            autoThresh[1] = Integer.MAX_VALUE;
            autoThresh[2] = -1;
            autoThresh[3] = Integer.MAX_VALUE;
        }
*/
        if(i == GeneImageAspect.SEEDED_REGION){//methodCombo.getSelectedItem().toString().equals("Seeded Region Growing")){
            Object[] params = new Object[1];
            params[0] = slidderSeededThreshold.getValue();
            engine.setSample_Gene_Data(myNumber, sdRedSlide.getCellPixels(),sdGreenSlide.getCellPixels(),
                    sdRedSlide.getCellHeight(), sdRedSlide.getCellWidth(), 1, params);
            sdRedSlide.setSeededRegion(engine.getSample_Gene_CenterSpots(myNumber, true));
            sdGreenSlide.setSeededRegion(engine.getSample_Gene_CenterSpots(myNumber, false));
        }
        else if(i == GeneImageAspect.ADAPTIVE_CIRCLE){
            int minr, maxr;
            try{
                minr = 2;
                maxr = 8;
                /*if(minr<1||maxr<1||minr>maxr){
                    JOptionPane.showMessageDialog(segframe.getDesktopPane(), "Radii Must Be Positive Integer Values.\nThe Maximum Radius Must Also Be Greater Than Or Equal To The Minimum Radius.\nThe Radii Have Been Reset To The Default Values Of 3 and 8.", "Incorrect Entry!", JOptionPane.ERROR_MESSAGE);
                    minr=3;
                    maxr=8;
                    minradiusField.setText(""+3);
                    maxradiusField.setText(""+8);
                }*/
            }catch(Exception e){
                minr=3;
                maxr=8;
                //minradiusField.setText(""+3);
                //maxradiusField.setText(""+8);
                //JOptionPane.showMessageDialog(segframe.getDesktopPane(), "Radii Must Be Positive Integer Values.\nThe Radii Have Been Reset To The Default Values Of 3 and 8.", "Incorrect Entry!", JOptionPane.ERROR_MESSAGE);
            }
            Object[] params = new Object[3];
            params[0] = new Integer(minr);
            params[1] = new Integer(maxr);
            params[2] = slidderSeededThreshold.getValue();
            engine.setSample_Gene_Data(myNumber, sdRedSlide.getCellPixels(),sdGreenSlide.getCellPixels(),
                    sdRedSlide.getCellHeight(), sdRedSlide.getCellWidth(), 2, params);
            sdRedSlide.setAdaptiveCircle(engine.getSample_Gene_CenterAndRadius(myNumber));
            sdGreenSlide.setAdaptiveCircle(engine.getSample_Gene_CenterAndRadius(myNumber));
        }

        if(i == 1 || i == 2)
        {
            String flagText = "N/A";
//            if(flagStatus) flagText = "<html>Flagging Status: <font color=\"#0000FF\">MANUALLY FLAGGED</font></html>";
//            else if ((gd.getRedForegroundTotal() < autoThresh[0]) || (gd.getRedBackgroundTotal() > autoThresh[1]) || (gd.getGreenForegroundTotal() < autoThresh[2]) || (gd.getGreenBackgroundTotal() > autoThresh[3])) flagText = "<html>Flagging Status: <font color=\"FF9900\">AUTOMATICALLY FLAGGED</font></html>";
//            else flagText = "Flagging Status: Not Flagged";

            if(ratioMethod==GeneImageAspect.TOTAL_SIGNAL||ratioMethod==GeneImageAspect.TOTAL_SUBTRACT_BG) {
                textArea_3.setText("Green BG Total: " + df.format(engine.getSample_Gene_GreenBackgroundTotal(myNumber)) +
                        "\nGreen FG Total: " +  df.format(engine.getSample_Gene_GreenForegroundTotal(myNumber)) +
                        "\nRed BG Total: " + df.format(engine.getSample_Gene_RedBackgroundTotal(myNumber)) +
                        "\nRed FG Total: " + df.format(engine.getSample_Gene_RedForegroundTotal(myNumber)));
            }
             else{
                textArea_3.setText("Green BG Avg: " + df.format(engine.getSample_Gene_GreenBackgroundAvg(myNumber)) +
                        "\nGreen FG Avg: " + df.format(engine.getSample_Gene_GreenForegroundAvg(myNumber)) +
                        "\nRed BG Avg: " + df.format(engine.getSample_Gene_RedBackgroundAvg(myNumber)) +
                        "\nRed FG Avg: " + df.format(engine.getSample_Gene_RedForegroundAvg(myNumber)));
            }
            textArea_3.setText(textArea_3.getText() + "\nRatio: " + df.format(engine.getSample_Gene_Ratio(myNumber, ratioMethod)));
            textArea_3.setText(textArea_3.getText() + "\nFlagging Status: " + flagText);


        } else{
            textArea_4.setText("Ratio: N/A \nGreen Background: N/A \nGreen Foreground: N/A \nRed Background: N/A \nRed Foreground: N/A");
        }

    }

    public Object[] getCellData(int grid, int spot)
    {
        engine.setSample_CurrentGrid(myNumber, grid);
        engine.setSample_CurrentSpot(myNumber, engine.getSample_Grid_ActualSpotNum(myNumber, grid, spot));

        Object[] params = new Object[6];
        params[0] = sdRedSlide.getCellPixels();
        params[1] = sdGreenSlide.getCellPixels();
        params[2] = sdRedSlide.getCellHeight();
        params[3] = sdRedSlide.getCellWidth();
        params[4] = segmentMode;

        if (segmentMode == GeneImageAspect.SEEDED_REGION)
        {
            Object[] params2 = new Object[1];
            params[0] = slidderSeededThreshold.getValue();
            params[5] = params2;
        }
        else if (segmentMode == GeneImageAspect.ADAPTIVE_CIRCLE)
        {
            Object[] params2 = new Object[3];
            params[0] = new Integer(2);
            params[1] = new Integer(8);
            params[2] = slidderSeededThreshold.getValue();
            params[5] = params2;
        }
        else
        {
            params[5] = null;
        }

        engine.setSample_CurrentGrid(myNumber, gridNum);
        engine.setSample_CurrentSpot(myNumber, engine.getSample_Grid_ActualSpotNum(myNumber, gridNum, spotNum));

        return params;
    }
}