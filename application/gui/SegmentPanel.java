package application.gui;

import application.tools.GeneImageAspect;
import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Lukas Pihl
 */
public class SegmentPanel extends JPanel
{
    private Border blackline = BorderFactory.createLineBorder(Color.black);
    private final int X_START = 10;
    private final int Y_START = 20;
    private int myNumber;

    private GuiManager manager;

    private ButtonGroup buttonGroup_Segment;
    private ButtonGroup buttonGroup_Control;
    private JButton button_SpotLeft;
    private JButton button_SpotRight;
    private JButton button_ThreshMinus;
    private JButton button_ThreshPlus;
    private JButton button_GridLeft;
    private JButton button_GridRight;
    private JButton button_FlagSettings;
    private JCheckBox checkBox_Flag;
    private JCheckBox checkBox_AntiFlag;
    private JComboBox<String> comboBox_Expression;
    private JLabel label_GreenDisplay;
    private JLabel label_RedDisplay;
    private JLabel label_Control;
    private JLabel label_Thresh;
    private JLabel label_Grid;
    private JLabel label_Spot;
    private JLabel label_Flag;
    private JLabel label_AntiFlag;
    private JPanel panel_Diaplay;
    private JPanel panel_Green;
    private JPanel panel_Red;
    private JPanel panel_Settings;
    private JRadioButton radioButton_Fixed;
    private JRadioButton radioButton_Adaptive;
    private JRadioButton radioButton_Seeded;
    private JRadioButton radioButton_ControlGreen;
    private JRadioButton radioButton_ControlRed;
    private JScrollPane scrollPane_GreenDisplay;
    private JScrollPane scrollPane_RedDisplay;
    private JSlider slider_Thresh;
    private JTextArea textArea_GreenDisplay;
    private JTextArea textArea_RedDisplay;
    private JTextArea textArea_OtherData;
    private JFormattedTextField textField_GridNum;
    private JFormattedTextField textField_SpotNum;
    private SegmentDisplay display_Green;
    private SegmentDisplay display_Red;
    private int segmentation_method = GeneImageAspect.FIXED_CIRCLE;

    public SegmentPanel(GuiManager manager, int number)
    {
        super();
        this.manager = manager;
        myNumber = number;
        setup();
    }

    private void setup()
    {
        panel_Diaplay = new JPanel();
        panel_Diaplay.setLayout(null);

        panel_Green = new JPanel();
        panel_Green.setLayout(null);

        textArea_OtherData = new JTextArea();
        textArea_OtherData.setBounds(0, 0, 200, 20);
        textArea_OtherData.setEditable(false);
        panel_Diaplay.add(textArea_OtherData);

        label_GreenDisplay = new JLabel("Green");
        label_GreenDisplay.setBounds(0, 0, 40, 10);
        panel_Green.add(label_GreenDisplay);
        display_Green = null;
        //display_Green = new SegmentDisplay(myNumber, true, manager);
        //display_Green.setBounds(-10, -50, 200, 200);
        //scrollPane_GreenDisplay = new JScrollPane(display_Green);
        scrollPane_GreenDisplay = new JScrollPane();
        scrollPane_GreenDisplay.setBounds(0, label_GreenDisplay.getY() + label_GreenDisplay.getHeight() + 2, 200, 200);
        scrollPane_GreenDisplay.setWheelScrollingEnabled(false);
        scrollPane_GreenDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane_GreenDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel_Green.add(scrollPane_GreenDisplay);
        textArea_GreenDisplay = new JTextArea();
        textArea_GreenDisplay.setBounds(0, scrollPane_GreenDisplay.getY() + scrollPane_GreenDisplay.getHeight() + 2,
                200, 40);
        textArea_GreenDisplay.setEditable(false);
        panel_Green.add(textArea_GreenDisplay);

        panel_Red = new JPanel();
        panel_Red.setLayout(null);

        label_RedDisplay = new JLabel("Red");
        label_RedDisplay.setBounds(0, 0, 40, 10);
        panel_Red.add(label_RedDisplay);
        display_Red = null;
        //display_Red = new SegmentDisplay(myNumber, false, manager);
        //display_Red.setBounds(-10, -50, 200, 200);
        //scrollPane_GreenDisplay = new JScrollPane(display_Red);
        scrollPane_RedDisplay = new JScrollPane();
        scrollPane_RedDisplay.setBounds(0, label_RedDisplay.getY() + label_RedDisplay.getHeight() + 2, 200, 200);
        scrollPane_RedDisplay.setWheelScrollingEnabled(false);
        scrollPane_RedDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane_RedDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel_Red.add(scrollPane_RedDisplay);
        textArea_RedDisplay = new JTextArea();
        textArea_RedDisplay.setBounds(0, scrollPane_RedDisplay.getY() + scrollPane_RedDisplay.getHeight() + 2, 200, 40);
        textArea_RedDisplay.setEditable(false);
        panel_Red.add(textArea_RedDisplay);

        panel_Green.setBounds(0, textArea_OtherData.getY() + textArea_OtherData.getHeight() + 5, 200,
                textArea_GreenDisplay.getY() + textArea_GreenDisplay.getHeight());
        panel_Red.setBounds(panel_Green.getX() + panel_Green.getWidth(), panel_Green.getY(), 200,
                textArea_RedDisplay.getY() + textArea_RedDisplay.getHeight());
        panel_Diaplay.setBounds(X_START, Y_START, panel_Red.getWidth() + panel_Red.getX(), panel_Green.getHeight()
                + panel_Green.getY());
        panel_Diaplay.add(panel_Green);
        panel_Diaplay.add(panel_Red);
        this.add(panel_Diaplay);

        panel_Settings = new JPanel();
        panel_Settings.setLayout(null);
        int panelHeight = 10;

        buttonGroup_Segment = new ButtonGroup();
        radioButton_Fixed = new JRadioButton();
        radioButton_Fixed.setBounds(0, 0, 22, 20);
        radioButton_Fixed.setHorizontalAlignment(0);
        radioButton_Fixed.setVerticalAlignment(0);
        radioButton_Fixed.setIcon(ImgManager.FixedCircle_Off);
        radioButton_Fixed.setDisabledIcon(ImgManager.FixedCircle_Off);
        radioButton_Fixed.setDisabledSelectedIcon(ImgManager.FixedCircle_Off);
        radioButton_Fixed.setPressedIcon(ImgManager.FixedCircle_On);
        radioButton_Fixed.setSelectedIcon(ImgManager.FixedCircle_On);
        radioButton_Fixed.setRolloverIcon(ImgManager.FixedCircle_Hover);
        radioButton_Fixed.setRolloverSelectedIcon(ImgManager.FixedCircle_On);
        radioButton_Fixed.setToolTipText("Fixed Circle");
        radioButton_Fixed.setSelected(true);
        radioButton_Fixed.addActionListener(FixedRadioButtonAction ->
        {
            manager.setSample_SegmentationMethod(myNumber, GeneImageAspect.FIXED_CIRCLE);
            segmentation_method = GeneImageAspect.FIXED_CIRCLE;
            updateSegmentation();
        });
        buttonGroup_Segment.add(radioButton_Fixed);
        panel_Settings.add(radioButton_Fixed);
        radioButton_Adaptive = new JRadioButton();
        radioButton_Adaptive.setBounds(radioButton_Fixed.getX() + radioButton_Fixed.getWidth() + 10,
                radioButton_Fixed.getY() - 1, 22, 22);
        radioButton_Adaptive.setHorizontalAlignment(0);
        radioButton_Adaptive.setVerticalAlignment(0);
        radioButton_Adaptive.setIcon(ImgManager.AdaptiveCircle_Off);
        radioButton_Adaptive.setDisabledIcon(ImgManager.AdaptiveCircle_Off);
        radioButton_Adaptive.setDisabledSelectedIcon(ImgManager.AdaptiveCircle_Off);
        radioButton_Adaptive.setPressedIcon(ImgManager.AdaptiveCircle_On);
        radioButton_Adaptive.setSelectedIcon(ImgManager.AdaptiveCircle_On);
        radioButton_Adaptive.setRolloverIcon(ImgManager.AdaptiveCircle_Hover);
        radioButton_Adaptive.setRolloverSelectedIcon(ImgManager.AdaptiveCircle_On);
        radioButton_Adaptive.setToolTipText("Adaptive Circle");
        radioButton_Adaptive.addActionListener(AdaptiveRadioButtonAction ->
        {
            manager.setSample_SegmentationMethod(myNumber, GeneImageAspect.ADAPTIVE_CIRCLE);
            segmentation_method = GeneImageAspect.ADAPTIVE_CIRCLE;
            updateSegmentation();
        });
        buttonGroup_Segment.add(radioButton_Adaptive);
        panel_Settings.add(radioButton_Adaptive);
        radioButton_Seeded = new JRadioButton();
        radioButton_Seeded.setBounds(radioButton_Adaptive.getX() + radioButton_Adaptive.getWidth() + 10,
                radioButton_Adaptive.getY(), 22, 22);
        radioButton_Seeded.setHorizontalAlignment(0);
        radioButton_Seeded.setVerticalAlignment(0);
        radioButton_Seeded.setIcon(ImgManager.SeededRegion_Off);
        radioButton_Seeded.setDisabledIcon(ImgManager.SeededRegion_Off);
        radioButton_Seeded.setDisabledSelectedIcon(ImgManager.SeededRegion_Off);
        radioButton_Seeded.setPressedIcon(ImgManager.SeededRegion_On);
        radioButton_Seeded.setSelectedIcon(ImgManager.SeededRegion_On);
        radioButton_Seeded.setRolloverIcon(ImgManager.SeededRegion_Hover);
        radioButton_Seeded.setRolloverSelectedIcon(ImgManager.SeededRegion_On);
        radioButton_Seeded.setToolTipText("Seeded Region");
        radioButton_Seeded.addActionListener(SeededRadioButtonAction ->
        {
            manager.setSample_SegmentationMethod(myNumber, GeneImageAspect.SEEDED_REGION);
            segmentation_method = GeneImageAspect.SEEDED_REGION;
            updateSegmentation();
        });
        buttonGroup_Segment.add(radioButton_Seeded);
        panel_Settings.add(radioButton_Seeded);

        label_Control = new JLabel("Conrol:");
        label_Control.setBounds(0, radioButton_Fixed.getY() + radioButton_Fixed.getHeight() + 10,
                (int)label_Control.getPreferredSize().getWidth(), (int)label_Control.getPreferredSize().getHeight());
        //panel_Settings.add(label_Control);
        buttonGroup_Control = new ButtonGroup();
        radioButton_ControlGreen = new JRadioButton("Green");
        radioButton_ControlGreen.setBounds(label_Control.getX() + label_Control.getWidth() + 10, label_Control.getY()
                - 4, (int)radioButton_ControlGreen.getPreferredSize().getWidth(),
                (int)radioButton_ControlGreen.getPreferredSize().getHeight());
        radioButton_ControlGreen.setSelected(true);
        buttonGroup_Control.add(radioButton_ControlGreen);
        //panel_Settings.add(radioButton_ControlGreen);
        radioButton_ControlRed = new JRadioButton("Red");
        radioButton_ControlRed.setBounds(radioButton_ControlGreen.getX() + radioButton_ControlGreen.getWidth() + 10,
                radioButton_ControlGreen.getY(), (int)radioButton_ControlRed.getPreferredSize().getWidth(),
                (int)radioButton_ControlRed.getPreferredSize().getHeight());
        buttonGroup_Control.add(radioButton_ControlRed);
        //panel_Settings.add(radioButton_ControlRed);

        button_ThreshMinus = new JButton();
        button_ThreshMinus.setBounds(2, label_Control.getY() + label_Control.getHeight() + 10, 20, 20);
        button_ThreshMinus.setIcon(ImgManager.Minus_Up);
        button_ThreshMinus.setDisabledIcon(ImgManager.Minus_Down);
        button_ThreshMinus.setDisabledSelectedIcon(ImgManager.Minus_Down);
        button_ThreshMinus.setPressedIcon(ImgManager.Minus_Down);
        button_ThreshMinus.setRolloverIcon(ImgManager.Minus_Up);
        button_ThreshMinus.setRolloverSelectedIcon(ImgManager.Minus_Up);
        button_ThreshMinus.setSelectedIcon(ImgManager.Minus_Up);
        button_ThreshMinus.setBorder(BorderFactory.createEmptyBorder());
        button_ThreshMinus.addActionListener(ThreshMinus ->
        {
            if (slider_Thresh.getValue() > 5)
            {
                slider_Thresh.setValue(slider_Thresh.getValue()-1);
            }
        });
        panel_Settings.add(button_ThreshMinus);
        label_Thresh = new JLabel("50%");
        slider_Thresh = new JSlider(5, 50);
        slider_Thresh.setValue(20);
        slider_Thresh.setBounds(button_ThreshMinus.getX() + button_ThreshMinus.getWidth(), button_ThreshMinus.getY(),
                100, (int)slider_Thresh.getPreferredSize().getHeight());
        slider_Thresh.addChangeListener(ThreshSliderChange ->
        {
            label_Thresh.setText(slider_Thresh.getValue() + "%");
            manager.setSample_MethodThreshold(myNumber, slider_Thresh.getValue());
        });
        panel_Settings.add(slider_Thresh);
        button_ThreshPlus = new JButton();
        button_ThreshPlus.setBounds(slider_Thresh.getX() + slider_Thresh.getWidth(), button_ThreshMinus.getY(),
                20, 20);
        button_ThreshPlus.setIcon(ImgManager.Plus_Up);
        button_ThreshPlus.setDisabledIcon(ImgManager.Plus_Down);
        button_ThreshPlus.setDisabledSelectedIcon(ImgManager.Plus_Down);
        button_ThreshPlus.setPressedIcon(ImgManager.Plus_Down);
        button_ThreshPlus.setRolloverIcon(ImgManager.Plus_Up);
        button_ThreshPlus.setRolloverSelectedIcon(ImgManager.Plus_Up);
        button_ThreshPlus.setSelectedIcon(ImgManager.Plus_Up);
        button_ThreshPlus.setBorder(BorderFactory.createEmptyBorder());
        button_ThreshPlus.addActionListener(ThreshPlus ->
        {
            if (slider_Thresh.getValue() < 50)
            {
                slider_Thresh.setValue(slider_Thresh.getValue()+1);
            }
        });
        panel_Settings.add(button_ThreshPlus);
        label_Thresh.setBounds(slider_Thresh.getX() + slider_Thresh.getWidth()/2
                - (int)label_Thresh.getPreferredSize().getWidth()/2, slider_Thresh.getY() + slider_Thresh.getHeight(),
                (int)label_Thresh.getPreferredSize().getWidth(), (int)label_Thresh.getPreferredSize().getHeight());
        label_Thresh.setText(slider_Thresh.getValue() + "%");
        panel_Settings.add(label_Thresh);

        label_Grid = new JLabel("Grid");
        label_Grid.setBounds(0, button_ThreshMinus.getY() + button_ThreshMinus.getHeight() + 15, 30, 14);
        panel_Settings.add(label_Grid);
        button_GridLeft = new JButton();
        button_GridLeft.setBounds(label_Grid.getX() + label_Grid.getWidth() + 2, label_Grid.getY(), 20, 20);
        button_GridLeft.setIcon(ImgManager.LeftArrow_Up);
        button_GridLeft.setDisabledIcon(ImgManager.LeftArrow_Down);
        button_GridLeft.setDisabledSelectedIcon(ImgManager.LeftArrow_Down);
        button_GridLeft.setPressedIcon(ImgManager.LeftArrow_Down);
        button_GridLeft.setRolloverIcon(ImgManager.LeftArrow_Up);
        button_GridLeft.setRolloverSelectedIcon(ImgManager.LeftArrow_Up);
        button_GridLeft.setSelectedIcon(ImgManager.LeftArrow_Up);
        button_GridLeft.setBorder(BorderFactory.createEmptyBorder());
        button_GridLeft.addActionListener(GridLeftButtonAction ->
        {
            if (manager.getSample_GridCount(myNumber) > 0)
            {
                if ((int)textField_GridNum.getValue() > 1)
                {
                    textField_GridNum.setValue((int)textField_GridNum.getValue() - 1);
                }
            }
        });
        panel_Settings.add(button_GridLeft);
        textField_GridNum = new JFormattedTextField(new NumberFormatter());
        textField_GridNum.setValue(0);
        textField_GridNum.setEditable(false);
        textField_GridNum.setBounds(button_GridLeft.getX() + button_GridLeft.getWidth(), button_GridLeft.getY(),
                60, 20);
        DocumentListener docListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateNumber();
            }
            public void removeUpdate(DocumentEvent e) {
                updateNumber();
            }
            public void insertUpdate(DocumentEvent e) {
                updateNumber();
            }

            public void updateNumber() {
                if (manager.getSample_GridCount(myNumber) > 0)
                {
                    if ((int)textField_GridNum.getValue() < 1)
                    {
                        textField_GridNum.setValue(1);
                    }
                    else if ((int)textField_GridNum.getValue() > manager.getSample_GridCount(myNumber))
                    {
                        textField_GridNum.setValue(manager.getSample_GridCount(myNumber));
                    }
                }
                else if ((int)textField_GridNum.getValue() != 0)
                {
                    textField_GridNum.setValue(0);
                }
                if (manager.getSample_GridCount(myNumber) > 0)
                {
                    if ((int)textField_SpotNum.getValue() < 1)
                    {
                        textField_SpotNum.setValue(1);
                    }
                    else if ((int)textField_SpotNum.getValue() > manager.getSample_Grid_NumberOfSpots(myNumber,
                            (int)textField_GridNum.getValue() - 1))
                    {
                        textField_SpotNum.setValue(manager.getSample_Grid_NumberOfSpots(myNumber,
                                (int)textField_GridNum.getValue() - 1));
                    }
                }
                else if ((int)textField_SpotNum.getValue() != 0)
                {
                    textField_SpotNum.setValue(0);
                }
                updateSegmentation();
            }
        };
        textField_GridNum.getDocument().addDocumentListener(docListener);
        panel_Settings.add(textField_GridNum);
        button_GridRight = new JButton();
        button_GridRight.setBounds(textField_GridNum.getX() + textField_GridNum.getWidth(), textField_GridNum.getY(),
                20, 20);
        button_GridRight.setIcon(ImgManager.RightArrow_Up);
        button_GridRight.setDisabledIcon(ImgManager.RightArrow_Down);
        button_GridRight.setDisabledSelectedIcon(ImgManager.RightArrow_Down);
        button_GridRight.setPressedIcon(ImgManager.RightArrow_Down);
        button_GridRight.setRolloverIcon(ImgManager.RightArrow_Up);
        button_GridRight.setRolloverSelectedIcon(ImgManager.RightArrow_Up);
        button_GridRight.setSelectedIcon(ImgManager.RightArrow_Up);
        button_GridRight.setBorder(BorderFactory.createEmptyBorder());
        button_GridRight.addActionListener(GridRightButtonAction ->
        {
            if (manager.getSample_GridCount(myNumber) > 0)
            {
                if ((int)textField_GridNum.getValue() < manager.getSample_GridCount(myNumber))
                {
                    textField_GridNum.setValue((int)textField_GridNum.getValue() + 1);
                }
            }
        });
        panel_Settings.add(button_GridRight);

        label_Spot = new JLabel("Spot");
        label_Spot.setBounds(0, label_Grid.getY() + label_Grid.getHeight() + 10, 30, 14);
        panel_Settings.add(label_Spot);
        button_SpotLeft = new JButton();
        button_SpotLeft.setBounds(label_Spot.getX() + label_Spot.getWidth() + 2, label_Spot.getY(), 20, 20);
        button_SpotLeft.setIcon(ImgManager.LeftArrow_Up);
        button_SpotLeft.setDisabledIcon(ImgManager.LeftArrow_Down);
        button_SpotLeft.setDisabledSelectedIcon(ImgManager.LeftArrow_Down);
        button_SpotLeft.setPressedIcon(ImgManager.LeftArrow_Down);
        button_SpotLeft.setRolloverIcon(ImgManager.LeftArrow_Up);
        button_SpotLeft.setRolloverSelectedIcon(ImgManager.LeftArrow_Up);
        button_SpotLeft.setSelectedIcon(ImgManager.LeftArrow_Up);
        button_SpotLeft.setBorder(BorderFactory.createEmptyBorder());
        button_SpotLeft.addActionListener(SpotLeftButtonAction ->
        {
            if (manager.getSample_GridCount(myNumber) > 0)
            {
                if ((int)textField_SpotNum.getValue() > 1)
                {
                    textField_SpotNum.setValue((int)textField_SpotNum.getValue() - 1);
                }
            }

        });
        panel_Settings.add(button_SpotLeft);
        textField_SpotNum = new JFormattedTextField(new NumberFormatter());
        textField_SpotNum.setValue(0);
        textField_SpotNum.setEditable(false);
        textField_SpotNum.setBounds(button_SpotLeft.getX() + button_SpotLeft.getWidth(), button_SpotLeft.getY(), 60,
                20);
        textField_SpotNum.getDocument().addDocumentListener(docListener);
        panel_Settings.add(textField_SpotNum);
        button_SpotRight = new JButton();
        button_SpotRight.setBounds(textField_SpotNum.getX() + textField_SpotNum.getWidth(), textField_SpotNum.getY(),
                20, 20);
        button_SpotRight.setIcon(ImgManager.RightArrow_Up);
        button_SpotRight.setDisabledIcon(ImgManager.RightArrow_Down);
        button_SpotRight.setDisabledSelectedIcon(ImgManager.RightArrow_Down);
        button_SpotRight.setPressedIcon(ImgManager.RightArrow_Down);
        button_SpotRight.setRolloverIcon(ImgManager.RightArrow_Up);
        button_SpotRight.setRolloverSelectedIcon(ImgManager.RightArrow_Up);
        button_SpotRight.setSelectedIcon(ImgManager.RightArrow_Up);
        button_SpotRight.setBorder(BorderFactory.createEmptyBorder());
        button_SpotRight.addActionListener(SpotRightButtonAction ->
        {
            if (manager.getSample_GridCount(myNumber) > 0)
            {
                if ((int)textField_SpotNum.getValue() < manager.getSample_Grid_NumberOfSpots(myNumber,
                        (int)textField_GridNum.getValue() - 1))
                {
                    textField_SpotNum.setValue((int)textField_SpotNum.getValue() + 1);
                }
            }

        });
        panel_Settings.add(button_SpotRight);

        checkBox_Flag = new JCheckBox();
        checkBox_Flag.setBounds(0, label_Spot.getY() + label_Spot.getHeight() + 15,
                (int)checkBox_Flag.getPreferredSize().getWidth(), (int)checkBox_Flag.getPreferredSize().getHeight());
        panel_Settings.add(checkBox_Flag);
        label_Flag = new JLabel();
        label_Flag.setIcon(ImgManager.Flag_On);
        label_Flag.setDisabledIcon(ImgManager.Flag_Off);
        label_Flag.setEnabled(false);
        label_Flag.setBounds(checkBox_Flag.getX() + checkBox_Flag.getWidth(), checkBox_Flag.getY(),
                20, 20);
        panel_Settings.add(label_Flag);
        checkBox_Flag.addChangeListener(FlagCheckBoxChange ->
        {
            if(checkBox_Flag.isSelected())
            {
                label_Flag.setEnabled(true);
            }
            else
            {
                label_Flag.setEnabled(false);
            }
        });
        checkBox_AntiFlag = new JCheckBox();
        checkBox_AntiFlag.setBounds(label_Flag.getX() + label_Flag.getWidth() + 15, checkBox_Flag.getY(),
                (int)checkBox_AntiFlag.getPreferredSize().getWidth(),
                (int)checkBox_AntiFlag.getPreferredSize().getHeight());
        //panel_Settings.add(checkBox_AntiFlag);
        label_AntiFlag = new JLabel();
        label_AntiFlag.setIcon(ImgManager.AntiFlag_On);
        label_AntiFlag.setDisabledIcon(ImgManager.AntiFlag_Off);
        label_AntiFlag.setEnabled(false);
        label_AntiFlag.setBounds(checkBox_AntiFlag.getX() + checkBox_AntiFlag.getWidth(), checkBox_AntiFlag.getY(),
                20, 20);
        //panel_Settings.add(label_AntiFlag);
        checkBox_AntiFlag.addChangeListener(AntiFlagCheckBoxChange ->
        {
            if (checkBox_AntiFlag.isSelected())
            {
                label_AntiFlag.setEnabled(true);
            }
            else
            {
                label_AntiFlag.setEnabled(false);
            }
        });
        button_FlagSettings = new JButton();
        button_FlagSettings.setBounds(label_AntiFlag.getX() + label_AntiFlag.getWidth() + 15,
                checkBox_AntiFlag.getY(), 20, 20);
        button_FlagSettings.setIcon(ImgManager.SettingsButton_Up);
        button_FlagSettings.setDisabledIcon(ImgManager.SettingsButton_Down);
        button_FlagSettings.setDisabledSelectedIcon(ImgManager.SettingsButton_Down);
        button_FlagSettings.setPressedIcon(ImgManager.SettingsButton_Down);
        button_FlagSettings.setRolloverIcon(ImgManager.SettingsButton_Up);
        button_FlagSettings.setRolloverSelectedIcon(ImgManager.SettingsButton_Up);
        button_FlagSettings.setSelectedIcon(ImgManager.SettingsButton_Up);
        button_FlagSettings.setBorder(BorderFactory.createEmptyBorder());
        button_FlagSettings.addActionListener(FlagSettingsAction ->
        {
            AutoFlaggingOptionsDialog afod = new AutoFlaggingOptionsDialog(manager.getWindow());
            afod.setVisible(true);
        });
        panel_Settings.add(button_FlagSettings);

        comboBox_Expression = new JComboBox<>(new String[]{"Total Signal", "Average Signal",
                "Total Signal BG Subtraction", "Average Signal BG Subtraction"});
        comboBox_Expression.setBounds(0, checkBox_Flag.getY() + checkBox_Flag.getHeight() + 5,
                (int)comboBox_Expression.getPreferredSize().getWidth(),
                (int)comboBox_Expression.getPreferredSize().getHeight());
        comboBox_Expression.addActionListener(ExpressionComboBoxAction ->
        {
            if (display_Green != null)
            {
                switch (comboBox_Expression.getSelectedIndex())
                {
                    case 0:
                        manager.setSample_RatioMethod(myNumber, GeneImageAspect.TOTAL_SIGNAL);
                        ratioMethod = GeneImageAspect.TOTAL_SIGNAL;
                        break;
                    case 1:
                        manager.setSample_RatioMethod(myNumber, GeneImageAspect.AVG_SIGNAL);
                        ratioMethod = GeneImageAspect.AVG_SIGNAL;
                        break;
                    case 2:
                        manager.setSample_RatioMethod(myNumber, GeneImageAspect.TOTAL_SUBTRACT_BG);
                        ratioMethod = GeneImageAspect.TOTAL_SUBTRACT_BG;
                        break;
                    case 3:
                        manager.setSample_RatioMethod(myNumber, GeneImageAspect.AVG_SUBTRACT_BG);
                        ratioMethod = GeneImageAspect.AVG_SUBTRACT_BG;
                        break;
                }
                updateSegmentation();
            }
        });
        panel_Settings.add(comboBox_Expression);

        panelHeight = comboBox_Expression.getHeight() + comboBox_Expression.getY() + 2;
        panel_Settings.setBounds(panel_Diaplay.getX() + panel_Diaplay.getWidth() + 10,
                Y_START, 210, panelHeight);
        this.add(panel_Settings);
    }

    public void updateSegmentation()
    {
        if (manager.getSample_GridCount(myNumber) < 1 && display_Green != null)
        {
            panel_Green.removeAll();
            panel_Red.removeAll();
            display_Green = null;
            display_Red = null;
        }
        else if (manager.getSample_GridCount(myNumber) >= 1 && display_Green == null)
        {
            display_Green = new SegmentDisplay(myNumber, true, manager);
            display_Green.setBounds(-10, -50, 200, 200);
            display_Green.setViewSize(200, 200);
            scrollPane_GreenDisplay.setViewportView(display_Green);
            display_Red = new SegmentDisplay(myNumber, false, manager);
            display_Red.setBounds(-10, -50, 200, 200);
            scrollPane_RedDisplay.setViewportView(display_Red);
            display_Red.setViewSize(200, 200);
            display_Green.zoom(10);
            display_Red.zoom(10);
            textField_GridNum.setValue(1);
            textField_GridNum.setEditable(true);
            textField_SpotNum.setValue(1);
            textField_SpotNum.setEditable(true);
            setSpot(0, 0);
            updateSegmentation();
        }
        else if (manager.getSample_GridCount(myNumber) >= 1 && display_Green != null)
        {
            setSpot((int)textField_GridNum.getValue() - 1, (int)textField_SpotNum.getValue() - 1);
            if (segmentation_method == GeneImageAspect.FIXED_CIRCLE)
            {
                Object[] params = new Object[1];
                params[0] = 5;
                manager.setSample_Gene_Data(myNumber, display_Red.getCellPixels(),display_Green.getCellPixels(),
                        display_Red.getCellHeight(), display_Red.getCellWidth(), 1, params);
                display_Green.setFixedCircle(5);
                display_Red.setFixedCircle(5);
            }
            else if (segmentation_method == GeneImageAspect.ADAPTIVE_CIRCLE)
            {
                Object[] params = new Object[3];
                params[0] = 3;
                params[1] = 8;
                params[2] = slider_Thresh.getValue();
                manager.setSample_Gene_Data(myNumber, display_Red.getCellPixels(),display_Green.getCellPixels(),
                        display_Red.getCellHeight(), display_Red.getCellWidth(), 2, params);
                display_Green.setAdaptiveCircle(manager.getSample_Gene_CenterAndRadius(myNumber));
                display_Red.setAdaptiveCircle(manager.getSample_Gene_CenterAndRadius(myNumber));
            }
            else if (segmentation_method == GeneImageAspect.SEEDED_REGION)
            {
                Object[] params = new Object[1];
                params[0] = slider_Thresh.getValue();
                manager.setSample_Gene_Data(myNumber, display_Red.getCellPixels(),display_Green.getCellPixels(),
                        display_Red.getCellHeight(), display_Red.getCellWidth(), 1, params);
                display_Green.setSeededRegion(manager.getSample_Gene_CenterSpots(myNumber, true));
                display_Red.setSeededRegion(manager.getSample_Gene_CenterSpots(myNumber, false));
            }
            greenBG = "Pending...";
            greenFG = "Pending...";
            redBG = "Pending...";
            redFG = "Pending...";
            ratio = "Pending...";
            if(ratioMethod==GeneImageAspect.TOTAL_SIGNAL||ratioMethod==GeneImageAspect.TOTAL_SUBTRACT_BG)
            {
                textArea_GreenDisplay.setText("BG Total: " + greenBG +
                        "\nFG Total: " +  greenFG);
                textArea_RedDisplay.setText("BG Total: " + redBG +
                        "\nFG Total: " + redFG);
            }
            else
            {
                textArea_GreenDisplay.setText("BG Average: " + greenBG +
                        "\nFG Average: " + greenFG);
                textArea_RedDisplay.setText("BG Average: " + redBG +
                        "\nFG Average: " + redFG);
            }
            textArea_OtherData.setText("Ratio: " + ratio);
            manager.updateSegmentData(myNumber, 0);
        }
    }

    String greenBG = "";
    String greenFG = "";
    String redBG = "";
    String redFG = "";
    String ratio = "";

    public void setSegmentationValues(Object[] vals)
    {
        greenFG = "" + df.format((int)vals[0]);
        greenBG = "" + df.format((int)vals[1]);
        redFG = "" + df.format((int)vals[2]);
        redBG = "" + df.format((int)vals[3]);
        ratio = "" + df.format((double)vals[4]);
        if(ratioMethod==GeneImageAspect.TOTAL_SIGNAL||ratioMethod==GeneImageAspect.TOTAL_SUBTRACT_BG)
        {
            textArea_GreenDisplay.setText("BG Total: " + greenBG +
                    "\nFG Total: " +  greenFG);
            textArea_RedDisplay.setText("BG Total: " + redBG +
                    "\nFG Total: " + redFG);
        }
        else
        {
            textArea_GreenDisplay.setText("BG Average: " + greenBG +
                    "\nFG Average: " + greenFG);
            textArea_RedDisplay.setText("BG Average: " + redBG +
                    "\nFG Average: " + redFG);
        }
        textArea_OtherData.setText("Ratio: " + ratio);
    }

    private DecimalFormat df = new DecimalFormat("###.####");
    int ratioMethod;
    int gridNum;
    int spotNum;
    public void setSpot(int grid, int spot) {
        if (grid >= 0 && grid < manager.getSample_GridCount(myNumber) && spot >= 0
                && spot < manager.getSample_Grid_NumberOfSpots(myNumber, grid))
        {
            gridNum = grid;
            spotNum = spot;
            manager.setSample_CurrentGrid(myNumber, grid);
            manager.setSample_CurrentSpot(myNumber, manager.getSample_Grid_ActualSpotNum(myNumber, grid, spot));
            showCurrentCell();
        }
    }

    int newTopLeftX;
    int newTopLeftY;
    public void showCurrentCell()
    {
        setCurrentCell();
        newTopLeftX = ((int) (display_Green.getZoom() * cell.xpoints[0])) - 4;
        newTopLeftY = ((int) (display_Green.getZoom() * cell.ypoints[0])) - 4;
        scrollPane_GreenDisplay.getViewport().setViewPosition(new Point(newTopLeftX, newTopLeftY));
        scrollPane_RedDisplay.getViewport().setViewPosition(new Point(newTopLeftX, newTopLeftY));

        display_Green.repaint();
        display_Red.repaint();
    }

    Polygon cell;
    int cellHeight;
    public void setCurrentCell()
    {
        Polygon p = manager.getSample_Grid_Polygon_Outline(myNumber, manager.getSample_CurrentGridNum(myNumber));
        Polygon q = new Polygon();
        if (p != null)
        {
            for (int j = 0; j < p.xpoints.length; j++)
            {
                q.xpoints[j] = (int) ((display_Green.screenX(p.xpoints[j])) / display_Green.getZoom());
                q.ypoints[j] = (int) ((display_Green.screenX(p.ypoints[j])) / display_Green.getZoom());
            }
            manager.setSample_Grid_Spots(myNumber, manager.getSample_CurrentGridNum(myNumber), q);
            cell = manager.getSample_Grid_CurrentSpot(myNumber, manager.getSample_CurrentGridNum(myNumber));
        }
        cellHeight = cell.ypoints[3] - cell.ypoints[0];
    }
}
