package application.gui;

import application.tools.StringTool;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class LoadDataDialog  extends JDialog
{
    private JLabel label_Green;
    private JLabel label_Red;
    private JLabel label_SelectedGreen;
    private JLabel label_SelectedRed;
    private JLabel label_TotalGreen;
    private JLabel label_TotalRed;
    private JLabel label_SelectedGene;
    private JLabel label_TotalGene;
    private JLabel label_Error;
    private JButton button_GreenUp;
    private JButton button_GreenDown;
    private JButton button_GreenAdd;
    private JButton button_GreenRemove;
    private JButton button_RedUp;
    private JButton button_RedDown;
    private JButton button_RedAdd;
    private JButton button_RedRemove;
    private JButton button_GeneUp;
    private JButton button_GeneDown;
    private JButton button_GeneAdd;
    private JButton button_GeneRemove;
    private JButton button_Swap;
    private JButton button_OK;
    private JButton button_Cancel;
    private JList<String> list_Red;
    private JList<String> list_Green;
    private JList<String> list_Gene;
    private DefaultListModel<String> listModel_Red;
    private DefaultListModel<String> listModel_Green;
    private DefaultListModel<String> listModel_Gene;
    private ArrayList<String> arrayList_Red;
    private ArrayList<String> arrayList_Green;
    private ArrayList<String> arrayList_Gene;
    private JPanel panel_images;
    private JPanel panel_gene;
    private JPanel panel_buttons;
    private TitledBorder border_images;
    private TitledBorder border_gene;

    public LoadDataDialog(Frame owner)
    {
        super(owner);
        setup();
    }

    public void setup()
    {
        arrayList_Red = new ArrayList<>();
        arrayList_Green = new ArrayList<>();
        arrayList_Gene = new ArrayList<>();

        int listwidth = 200;
        int listheight = 350;
        int borderpad_x = 10;
        int borderpad_y = 10;
        int list_y, addbutton_y, removebutton_y, upbutton_y, downbutton_y, selectedlabel_y, totallabel_y, current_x;

        label_Green = new JLabel("Green");
        label_Green.setBounds(0, borderpad_y, (int)label_Green.getPreferredSize().getWidth(),
                (int)label_Green.getPreferredSize().getHeight());
        list_Green = new JList<>();
        list_y = label_Green.getY() + label_Green.getHeight() + 5;
        list_Green.setBounds(0, list_y, listwidth, listheight);
        list_Green.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list_Green.addListSelectionListener(GreenListSelection -> listen_GreenListSelection());
        listModel_Green = new DefaultListModel<>();
        list_Green.setModel(listModel_Green);
        current_x = borderpad_x;
        addbutton_y = list_y + 10;
        button_GreenAdd = new JButton();
        button_GreenAdd.setBounds(current_x, addbutton_y, 20, 20);
        button_GreenAdd.addActionListener(AddGreenButtonAction -> listen_AddGreenButtonAction());
        removebutton_y = addbutton_y + 10 + 30;
        button_GreenRemove = new JButton();
        button_GreenRemove.setBounds(current_x, removebutton_y, 20, 20);
        button_GreenRemove.addActionListener(RemoveGreenButtonAction -> listen_RemoveGreenButtonAction());
        downbutton_y = list_y + listheight - 10 - 30;
        button_GreenDown = new JButton();
        button_GreenDown.setBounds(current_x, downbutton_y, 20, 20);
        button_GreenDown.addActionListener(GreenDownButtonAction -> listen_GreenDownButtonAction());
        upbutton_y = downbutton_y - 10 - 30;
        button_GreenUp = new JButton();
        button_GreenUp.setBounds(current_x, upbutton_y, 20, 20);
        button_GreenUp.addActionListener(GreenUpButtonAction -> listen_GreenUpButtonAction());
        button_GreenUp.setEnabled(false);
        button_GreenDown.setEnabled(false);

        list_Green.setLocation(button_GreenAdd.getX() + button_GreenAdd.getWidth() + 5, list_Green.getY());
        label_Green.setLocation(list_Green.getX() + (list_Green.getWidth() / 2) - (label_Green.getWidth() / 2),
                label_Green.getY());
        label_SelectedGreen = new JLabel("Selected: 0");
        selectedlabel_y = list_Green.getY() + list_Green.getHeight() + 5;
        label_SelectedGreen.setBounds(0, selectedlabel_y, (int)label_SelectedGreen.getPreferredSize().getWidth(),
                (int)label_SelectedGreen.getPreferredSize().getHeight());
        updateLabelX(1);
        totallabel_y = selectedlabel_y + label_SelectedGreen.getHeight() + 5;
        label_TotalGreen = new JLabel("Total: 0");
        label_TotalGreen.setBounds(0, totallabel_y, (int)label_TotalGreen.getPreferredSize().getWidth(),
                (int)label_TotalGreen.getPreferredSize().getHeight());
        updateLabelX(2);

        button_Swap = new JButton();
        button_Swap.setBounds(list_Green.getX() + list_Green.getWidth() + 5, list_y + list_Green.getHeight() / 2 -
                30 / 2, 30, 30);

        list_Red = new JList<>();
        list_Red.setBounds(button_Swap.getX() + button_Swap.getWidth() + 5, list_y, listwidth, listheight);
        list_Red.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list_Red.addListSelectionListener(RedListSelection -> listen_RedListSelection());
        listModel_Red = new DefaultListModel<>();
        list_Red.setModel(listModel_Red);
        label_Red = new JLabel("Red");
        label_Red.setBounds(list_Red.getX() + list_Red.getWidth() / 2 - (int) label_Red.getPreferredSize().getWidth() /
                        2, borderpad_y, (int) label_Red.getPreferredSize().getWidth(),
                (int) label_Red.getPreferredSize().getHeight());

        current_x = list_Red.getX() + list_Red.getWidth() + 5;
        button_RedAdd = new JButton();
        button_RedAdd.setBounds(current_x, addbutton_y, 20, 20);
        button_RedAdd.addActionListener(AddRedButtonAction -> listen_AddRedButtonAction());
        button_RedRemove = new JButton();
        button_RedRemove.setBounds(current_x, removebutton_y, 20, 20);
        button_RedRemove.addActionListener(RemoveRedButtonAction -> listen_RemoveRedButtonAction());
        button_RedUp = new JButton();
        button_RedUp.setBounds(current_x, upbutton_y, 20, 20);
        button_RedUp.addActionListener(RedUpButtonAction -> listen_RedUpButtonAction());
        button_RedUp.setEnabled(false);
        button_RedDown = new JButton();
        button_RedDown.setBounds(current_x, downbutton_y, 20, 20);
        button_RedDown.setEnabled(false);
        button_RedDown.addActionListener(RedDownButtonAction -> listen_RedDownButtonAction());
        label_SelectedRed = new JLabel("Selected: 0");
        label_SelectedRed.setBounds(0, selectedlabel_y, (int)label_SelectedRed.getPreferredSize().getWidth(),
                (int)label_SelectedRed.getPreferredSize().getHeight());
        updateLabelX(3);
        label_TotalRed = new JLabel("Total: 0");
        label_TotalRed.setBounds(0, totallabel_y, (int)label_TotalRed.getPreferredSize().getWidth(),
                (int)label_TotalRed.getPreferredSize().getHeight());
        updateLabelX(4);

        panel_images = new JPanel();
        panel_images.setLayout(null);
        panel_images.add(button_GreenAdd);
        panel_images.add(button_GreenRemove);
        panel_images.add(button_GreenUp);
        panel_images.add(button_GreenDown);
        panel_images.add(list_Green);
        panel_images.add(label_Green);
        panel_images.add(label_SelectedGreen);
        panel_images.add(label_TotalGreen);
        panel_images.add(button_Swap);
        panel_images.add(label_Red);
        panel_images.add(list_Red);
        panel_images.add(label_SelectedRed);
        panel_images.add(label_TotalRed);
        panel_images.add(button_RedAdd);
        panel_images.add(button_RedRemove);
        panel_images.add(button_RedUp);
        panel_images.add(button_RedDown);
        border_images = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(178, 178, 178)),
                "Images");
        panel_images.setBorder(border_images);
        panel_images.setBounds(5, 5, button_RedAdd.getX() + button_RedAdd.getWidth() + borderpad_x,
                label_TotalGreen.getY() + label_TotalGreen.getHeight() + borderpad_x);

        list_Gene = new JList<>();
        list_Gene.setBounds(borderpad_x, list_y, listwidth, listheight);
        list_Gene.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list_Gene.addListSelectionListener(GeneListSelection -> listen_GeneListSelection());
        listModel_Gene = new DefaultListModel<>();
        list_Gene.setModel(listModel_Gene);
        current_x = list_Gene.getX() + list_Gene.getWidth() + 5;
        button_GeneAdd = new JButton();
        button_GeneAdd.setBounds(current_x, addbutton_y, 20, 20);
        button_GeneAdd.addActionListener(AddGeneButtonAction -> listen_AddGeneButtonAction());
        button_GeneRemove = new JButton();
        button_GeneRemove.setBounds(current_x, removebutton_y, 20, 20);
        button_GeneRemove.addActionListener(RemoveGeneButtonAction -> listen_RemoveGeneButtonAction());
        button_GeneUp = new JButton();
        button_GeneUp.setBounds(current_x, upbutton_y, 20, 20);
        button_GeneUp.addActionListener(GeneUpButtonAction -> listen_GeneUpButtonAction());
        button_GeneUp.setEnabled(false);
        button_GeneDown = new JButton();
        button_GeneDown.setBounds(current_x, downbutton_y, 20, 20);
        button_GeneDown.addActionListener(GeneDownButtonAction -> listen_GeneDownButtonAction());
        button_GeneDown.setEnabled(false);
        label_SelectedGene = new JLabel("Selected: 0");
        label_SelectedGene.setBounds(0, selectedlabel_y, (int)label_SelectedGene.getPreferredSize().getWidth(),
                (int)label_SelectedGene.getPreferredSize().getHeight());
        updateLabelX(5);
        label_TotalGene = new JLabel("Total: 0");
        label_TotalGene.setBounds(0, totallabel_y, (int)label_TotalGene.getPreferredSize().getWidth(),
                (int)label_TotalGene.getPreferredSize().getHeight());
        updateLabelX(6);

        panel_gene = new JPanel();
        panel_gene.setLayout(null);
        panel_gene.add(list_Gene);
        panel_gene.add(button_GeneAdd);
        panel_gene.add(button_GeneRemove);
        panel_gene.add(button_GeneUp);
        panel_gene.add(button_GeneDown);
        panel_gene.add(label_SelectedGene);
        panel_gene.add(label_TotalGene);
        border_gene = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(178, 178, 178)),
                "Gene data");
        panel_gene.setBorder(border_gene);
        panel_gene.setBounds(panel_images.getX() + panel_images.getWidth() + 5, panel_images.getY(),
                button_GeneAdd.getX() + button_GeneAdd.getWidth() + borderpad_x,
                label_TotalGene.getY() + label_TotalGene.getHeight() + borderpad_x);

        label_Error = new JLabel("TEST MESSAGE");
        label_Error.setBounds(0, panel_images.getY() + panel_images.getHeight() + 5,
                (int)label_Error.getPreferredSize().getWidth(),
                (int)label_Error.getPreferredSize().getHeight());
        label_Error.setText("");

        button_OK = new JButton("OK");
        button_Cancel = new JButton("Cancel");
        button_OK.setBounds(0, 0, (int)button_OK.getPreferredSize().getWidth(),
                (int)button_OK.getPreferredSize().getHeight());
        button_OK.setEnabled(false);
        button_Cancel.setBounds(button_OK.getX() + button_OK.getWidth() + 100, button_OK.getY(),
                (int)button_Cancel.getPreferredSize().getWidth(), (int)button_Cancel.getPreferredSize().getHeight());
        panel_buttons = new JPanel();
        panel_buttons.setLayout(null);
        panel_buttons.add(button_OK);
        panel_buttons.add(button_Cancel);
        panel_buttons.setBounds(0, label_Error.getY() + label_Error.getHeight() + 5,
                button_Cancel.getX() + button_Cancel.getWidth(), button_OK.getY() + button_OK.getHeight());
        panel_buttons.setLocation((panel_gene.getX() + panel_gene.getWidth() - 5) / 2 -
                panel_buttons.getWidth() / 2 + 5, panel_buttons.getY());

        this.getContentPane().setLayout(null);
        this.getContentPane().add(panel_images);
        this.getContentPane().add(panel_gene);
        this.getContentPane().add(label_Error);
        this.getContentPane().add(panel_buttons);
        this.setSize(panel_gene.getX() + panel_gene.getWidth() + 10,
                panel_buttons.getY() + panel_buttons.getHeight() + 45);
        updateLabelX(7);
        this.setResizable(false);
    }

    private void updateLabelX(int n)
    {
        switch (n)
        {
            case 1: //Green selected
                label_SelectedGreen.setLocation(list_Green.getX() + list_Green.getWidth() / 2 -
                        (int)label_SelectedGreen.getPreferredSize().getWidth() / 2, label_SelectedGreen.getY());
                break;
            case 2: //Green total
                label_TotalGreen.setLocation(list_Green.getX() + list_Green.getWidth() / 2 -
                        (int)label_TotalGreen.getPreferredSize().getWidth() / 2, label_TotalGreen.getY());
                break;
            case 3: //Red selected
                label_SelectedRed.setLocation(list_Red.getX() + list_Red.getWidth() / 2 -
                        (int)label_SelectedRed.getPreferredSize().getWidth() / 2, label_SelectedRed.getY());
                break;
            case 4: //Red total
                label_TotalRed.setLocation(list_Red.getX() + list_Red.getWidth() / 2 -
                        (int)label_TotalRed.getPreferredSize().getWidth() / 2, label_TotalRed.getY());
                break;
            case 5: //Gene selected
                label_SelectedGene.setLocation(list_Gene.getX() + list_Gene.getWidth() / 2 -
                        (int)label_SelectedGene.getPreferredSize().getWidth() / 2, label_SelectedGene.getY());
                break;
            case 6: //Gene total
                label_TotalGene.setLocation(list_Gene.getX() + list_Gene.getWidth() / 2 -
                        (int)label_TotalGene.getPreferredSize().getWidth() / 2, label_TotalGene.getY());
                break;
            case 7: //Error message
                label_Error.setLocation((panel_gene.getX() + panel_gene.getWidth() - 5) / 2 -
                        (int)label_Error.getPreferredSize().getWidth() / 2 + 5, label_Error.getY());
                break;
        }
    }

    private void listen_AddGreenButtonAction()
    {
        lockState();
        Thread importThread = new Thread()
        {
            public void run()
            {
                class ImportGreen extends SwingWorker<Void, Void>
                {
                    private JFileChooser chooser = new JFileChooser();
                    private FileNameExtensionFilter filter;
                    private File[] greenPaths;
                    private boolean pass = false;

                    @Override
                    public Void doInBackground() throws Exception
                    {
                        try
                        {
                            filter = new FileNameExtensionFilter("TIF file", "tif");
                            chooser.setMultiSelectionEnabled(true);
                            chooser.setFileFilter(filter);

                            chooser.setDialogTitle("Load Green Image Files...");
                            chooser.showOpenDialog(null);

                            greenPaths = chooser.getSelectedFiles();

                            pass = true;
                        }
                        catch (NullPointerException ex)
                        {
                            unlockState();
                            //Do nothing
                        }
                        return null;
                    }

                    @Override
                    public void done()
                    {
                        if (pass)
                        {
                            pass = false;
                            for (int i = 0; i < greenPaths.length; i++)
                            {
                                arrayList_Green.add(greenPaths[i].getAbsolutePath());
                                listModel_Green.addElement(StringTool.parse_FilePath_File(greenPaths[i].getAbsolutePath()));
                                label_TotalGreen.setText("Total: " + arrayList_Green.size());
                                updateLabelX(2);
                                unlockState();
                            }
                        }
                    }
                }
                new ImportGreen().execute();
            }
        };
        importThread.start();
    }

    private void listen_RemoveGreenButtonAction()
    {
        int[] selected = list_Green.getSelectedIndices();
        for (int i = selected.length - 1; i >= 0; i--)
        {
            arrayList_Green.remove(selected[i]);
            listModel_Green.removeElementAt(selected[i]);
            label_TotalGreen.setText("Total: " + arrayList_Green.size());
            updateLabelX(2);
        }
    }

    private void listen_GreenListSelection()
    {
        if (!lockdown)
        {
            if (list_Green.getSelectedIndices().length != 1)
            {
                button_GreenUp.setEnabled(false);
                button_GreenDown.setEnabled(false);
            }
            else
            {
                button_GreenUp.setEnabled(true);
                button_GreenDown.setEnabled(true);
            }
        }
        else
        {
            if (list_Green.getSelectedIndices().length != 1)
            {
                states[2] = false;
                states[3] = false;
            }
            else
            {
                states[2] = true;
                states[3] = true;
            }
        }
        label_SelectedGreen.setText("Selected: " + list_Green.getSelectedIndices().length);
        updateLabelX(1);
    }

    private void listen_GreenUpButtonAction()
    {
        int i = list_Green.getSelectedIndex();
        if (i != 0)
        {
            String temp = arrayList_Green.get(i);
            arrayList_Green.set(i, arrayList_Green.get(i - 1));
            arrayList_Green.set(i - 1, temp);
            temp = listModel_Green.getElementAt(i);
            listModel_Green.setElementAt(listModel_Green.getElementAt(i - 1), i);
            listModel_Green.setElementAt(temp, i - 1);
            list_Green.setSelectedIndex(i - 1);
        }
    }

    private void listen_GreenDownButtonAction()
    {
        int i = list_Green.getSelectedIndex();
        if (i != arrayList_Green.size() - 1)
        {
            String temp = arrayList_Green.get(i);
            arrayList_Green.set(i, arrayList_Green.get(i + 1));
            arrayList_Green.set(i + 1, temp);
            temp = listModel_Green.getElementAt(i);
            listModel_Green.setElementAt(listModel_Green.getElementAt(i + 1), i);
            listModel_Green.setElementAt(temp, i + 1);
            list_Green.setSelectedIndex(i + 1);
        }
    }

    private void listen_AddRedButtonAction()
    {
        lockState();
        Thread importThread = new Thread()
        {
            public void run()
            {
                class ImportRed extends SwingWorker<Void, Void>
                {
                    private JFileChooser chooser = new JFileChooser();
                    private FileNameExtensionFilter filter;
                    private File[] redPaths;
                    private boolean pass = false;

                    @Override
                    public Void doInBackground() throws Exception
                    {
                        try
                        {
                            filter = new FileNameExtensionFilter("TIF file", "tif");
                            chooser.setMultiSelectionEnabled(true);
                            chooser.setFileFilter(filter);

                            chooser.setDialogTitle("Load Red Image Files...");
                            chooser.showOpenDialog(null);

                            redPaths = chooser.getSelectedFiles();

                            pass = true;
                        }
                        catch (NullPointerException ex)
                        {
                            unlockState();
                            //Do nothing
                        }
                        return null;
                    }

                    @Override
                    public void done()
                    {
                        if (pass)
                        {
                            pass = false;
                            for (int i = 0; i < redPaths.length; i++)
                            {
                                arrayList_Red.add(redPaths[i].getAbsolutePath());
                                listModel_Red.addElement(StringTool.parse_FilePath_File(redPaths[i].getAbsolutePath()));
                                label_TotalRed.setText("Total: " + arrayList_Red.size());
                                updateLabelX(4);
                                unlockState();
                            }
                        }
                    }
                }
                new ImportRed().execute();
            }
        };
        importThread.start();
    }

    private void listen_RemoveRedButtonAction()
    {
        int[] selected = list_Red.getSelectedIndices();
        for (int i = selected.length - 1; i >= 0; i--)
        {
            arrayList_Red.remove(selected[i]);
            listModel_Red.removeElementAt(selected[i]);
            label_TotalRed.setText("Total: " + arrayList_Red.size());
            updateLabelX(4);
        }
    }

    private void listen_RedListSelection()
    {
        if (!lockdown)
        {
            if (list_Red.getSelectedIndices().length != 1)
            {
                button_RedUp.setEnabled(false);
                button_RedDown.setEnabled(false);
            }
            else
            {
                button_RedUp.setEnabled(true);
                button_RedDown.setEnabled(true);
            }
        }
        else
        {
            if (list_Red.getSelectedIndices().length != 1)
            {
                states[6] = false;
                states[7] = false;
            }
            else
            {
                states[6] = true;
                states[7] = true;
            }
        }
        label_SelectedRed.setText("Selected: " + list_Red.getSelectedIndices().length);
        updateLabelX(3);
    }

    private void listen_RedUpButtonAction()
    {
        int i = list_Red.getSelectedIndex();
        if (i != 0)
        {
            String temp = arrayList_Red.get(i);
            arrayList_Red.set(i, arrayList_Red.get(i - 1));
            arrayList_Red.set(i - 1, temp);
            temp = listModel_Red.getElementAt(i);
            listModel_Red.setElementAt(listModel_Red.getElementAt(i - 1), i);
            listModel_Red.setElementAt(temp, i - 1);
            list_Red.setSelectedIndex(i - 1);
        }
    }

    private void listen_RedDownButtonAction()
    {
        int i = list_Red.getSelectedIndex();
        if (i != arrayList_Red.size() - 1)
        {
            String temp = arrayList_Red.get(i);
            arrayList_Red.set(i, arrayList_Red.get(i + 1));
            arrayList_Red.set(i + 1, temp);
            temp = listModel_Red.getElementAt(i);
            listModel_Red.setElementAt(listModel_Red.getElementAt(i + 1), i);
            listModel_Red.setElementAt(temp, i + 1);
            list_Red.setSelectedIndex(i + 1);
        }
    }

    private void listen_AddGeneButtonAction()
    {
        lockState();
        Thread importThread = new Thread()
        {
            public void run()
            {
                class ImportGene extends SwingWorker<Void, Void>
                {
                    private JFileChooser chooser = new JFileChooser();
                    private FileNameExtensionFilter filter;
                    private File[] genePaths;
                    private boolean pass = false;

                    @Override
                    public Void doInBackground() throws Exception
                    {
                        try
                        {
                            filter = new FileNameExtensionFilter("TXT file", "txt");
                            chooser.setMultiSelectionEnabled(true);
                            chooser.setFileFilter(filter);

                            chooser.setDialogTitle("Load Gene Data Files...");
                            chooser.showOpenDialog(null);

                            genePaths = chooser.getSelectedFiles();

                            pass = true;
                        }
                        catch (NullPointerException ex)
                        {
                            unlockState();
                            //Do nothing
                        }
                        return null;
                    }

                    @Override
                    public void done()
                    {
                        if (pass)
                        {
                            pass = false;
                            for (int i = 0; i < genePaths.length; i++)
                            {
                                arrayList_Gene.add(genePaths[i].getAbsolutePath());
                                listModel_Gene.addElement(StringTool.parse_FilePath_File(genePaths[i].getAbsolutePath()));
                                label_TotalGene.setText("Total: " + arrayList_Gene.size());
                                updateLabelX(6);
                            }
                        }
                        unlockState();
                    }
                }
                new ImportGene().execute();
            }
        };
        importThread.start();
    }

    private void listen_RemoveGeneButtonAction()
    {
        int[] selected = list_Gene.getSelectedIndices();
        for (int i = selected.length - 1; i >= 0; i--)
        {
            arrayList_Gene.remove(selected[i]);
            listModel_Gene.removeElementAt(selected[i]);
            label_TotalGene.setText("Total: " + arrayList_Gene.size());
            updateLabelX(6);
        }
    }

    private void listen_GeneListSelection()
    {
        if (!lockdown)
        {
            if (list_Gene.getSelectedIndices().length != 1)
            {
                button_GeneUp.setEnabled(false);
                button_GeneDown.setEnabled(false);
            }
            else
            {
                button_GeneUp.setEnabled(true);
                button_GeneDown.setEnabled(true);
            }
        }
        else
        {
            if (list_Gene.getSelectedIndices().length != 1)
            {
                states[10] = false;
                states[11] = false;
            }
            else
            {
                states[10] = true;
                states[11] = true;
            }
        }
        label_SelectedGene.setText("Selected: " + list_Gene.getSelectedIndices().length);
        updateLabelX(5);
    }

    private void listen_GeneUpButtonAction()
    {
        int i = list_Gene.getSelectedIndex();
        if (i != 0)
        {
            String temp = arrayList_Gene.get(i);
            arrayList_Gene.set(i, arrayList_Gene.get(i - 1));
            arrayList_Gene.set(i - 1, temp);
            temp = listModel_Gene.getElementAt(i);
            listModel_Gene.setElementAt(listModel_Gene.getElementAt(i - 1), i);
            listModel_Gene.setElementAt(temp, i - 1);
            list_Gene.setSelectedIndex(i - 1);
        }
    }

    private void listen_GeneDownButtonAction()
    {
        int i = list_Gene.getSelectedIndex();
        if (i != arrayList_Gene.size() - 1)
        {
            String temp = arrayList_Gene.get(i);
            arrayList_Gene.set(i, arrayList_Gene.get(i + 1));
            arrayList_Gene.set(i + 1, temp);
            temp = listModel_Gene.getElementAt(i);
            listModel_Gene.setElementAt(listModel_Gene.getElementAt(i + 1), i);
            listModel_Gene.setElementAt(temp, i + 1);
            list_Gene.setSelectedIndex(i + 1);
        }
    }

    boolean[] states = new boolean[15];
    private boolean lockdown = false;
    private void lockState()
    {
        lockdown = true;
        states[0] = button_GreenAdd.isEnabled();
        states[1] = button_GreenRemove.isEnabled();
        states[2] = button_GreenUp.isEnabled();
        states[3] = button_GreenDown.isEnabled();
        states[4] = button_RedAdd.isEnabled();
        states[5] = button_RedRemove.isEnabled();
        states[6] = button_RedUp.isEnabled();
        states[7] = button_RedDown.isEnabled();
        states[8] = button_GeneAdd.isEnabled();
        states[9] = button_GeneRemove.isEnabled();
        states[10] = button_GeneUp.isEnabled();
        states[11] = button_GeneDown.isEnabled();
        states[12] = button_Swap.isEnabled();
        states[13] = button_OK.isEnabled();
        states[14] = button_Cancel.isEnabled();

        button_GreenAdd.setEnabled(false);
        button_GreenRemove.setEnabled(false);
        button_GreenUp.setEnabled(false);
        button_GreenDown.setEnabled(false);
        button_RedAdd.setEnabled(false);
        button_RedRemove.setEnabled(false);
        button_RedUp.setEnabled(false);
        button_RedDown.setEnabled(false);
        button_GeneAdd.setEnabled(false);
        button_GeneRemove.setEnabled(false);
        button_GeneUp.setEnabled(false);
        button_GeneDown.setEnabled(false);
        button_Swap.setEnabled(false);
        button_OK.setEnabled(false);
        button_Cancel.setEnabled(false);
    }

    private void unlockState()
    {
        lockdown = false;
        button_GreenAdd.setEnabled(states[0]);
        button_GreenRemove.setEnabled(states[1]);
        button_GreenUp.setEnabled(states[2]);
        button_GreenDown.setEnabled(states[3]);
        button_RedAdd.setEnabled(states[4]);
        button_RedRemove.setEnabled(states[5]);
        button_RedUp.setEnabled(states[6]);
        button_RedDown.setEnabled(states[7]);
        button_GeneAdd.setEnabled(states[8]);
        button_GeneRemove.setEnabled(states[9]);
        button_GeneUp.setEnabled(states[10]);
        button_GeneDown.setEnabled(states[11]);
        button_Swap.setEnabled(states[12]);
        button_OK.setEnabled(states[13]);
        button_Cancel.setEnabled(states[14]);
    }
}
