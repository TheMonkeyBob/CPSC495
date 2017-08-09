package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame
{
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private GuiManager manager;
    private String myName;
    private String projectName;
    private String sampleName;

    /**
     * Create the frame.
     */
    public MainWindow(GuiManager manager)
    {
        super();
        setIconImage(ImgManager.Icon_Application.getImage());

        this.manager = manager;

        //TODO: set better close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(100, 100, 1200, 910);

        // MenuBar stuff here
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

        // File menu
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem menuItem_NewProject = new JMenuItem("New Project");
        menuItem_NewProject.setMnemonic(KeyEvent.VK_N);
        menuItem_NewProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem_NewProject.addActionListener(pickedItem_NewProject -> lambda_NewProject());
        mnFile.add(menuItem_NewProject);

        JMenuItem menuItem_OpenProject = new JMenuItem("Open Project");
        menuItem_OpenProject.setMnemonic(KeyEvent.VK_O);
        menuItem_OpenProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem_OpenProject.addActionListener(pickedItem_OpenProject -> lambda_OpenProject());
        mnFile.add(menuItem_OpenProject);

        JSeparator separator = new JSeparator();
        mnFile.add(separator);

        JMenuItem menuItem_SaveProject = new JMenuItem("Save Project");
        menuItem_SaveProject.setMnemonic(KeyEvent.VK_S);
        menuItem_SaveProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem_SaveProject.addActionListener(pickedItem_SaveProject ->
        {
            manager.saveProject();
        });
        mnFile.add(menuItem_SaveProject);

        JSeparator separator_1 = new JSeparator();
        mnFile.add(separator_1);

        JMenuItem menuItem_ImportSampleData = new JMenuItem("Import Sample Data....");
        menuItem_ImportSampleData.setMnemonic(KeyEvent.VK_I);
        menuItem_ImportSampleData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        menuItem_ImportSampleData.addActionListener(pickedItem_ImportSampleData -> lambda_ImportSampleData());
        mnFile.add(menuItem_ImportSampleData);

        // Help menu
        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem menuItem_UserGuide = new JMenuItem("User Guide");
        menuItem_UserGuide.setMnemonic(KeyEvent.VK_U);
        menuItem_UserGuide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        menuItem_UserGuide.addActionListener(pickedItem_UserGuide ->
        {
            //TODO: Add user guide
        });
        mnHelp.add(menuItem_UserGuide);

        JSeparator separator_3 = new JSeparator();
        mnHelp.add(separator_3);

        JMenuItem menuItem_About = new JMenuItem("About");
        menuItem_About.addActionListener(pickedItem_About ->
        {
            //TODO: add proper about page
            JOptionPane.showMessageDialog(null,
                    "CPSC 450: Bioinformatics team:\n Lee Callaghan\n Edward Greenop\n Darren Hendrickson\n Lukas Pihl\n",
                    "About us", JOptionPane.INFORMATION_MESSAGE);
        });
        mnHelp.add(menuItem_About);

        // Panel stuff starts here
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 0, 0 };
        gbl_contentPane.rowHeights = new int[] { 0, 0 };
        gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);

        LoadDataDialog ld = new LoadDataDialog(this);
        ld.setVisible(true);
    }

    public void setTabbedPane(JTabbedPane tp)
    {
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        gbc_tabbedPane.fill = GridBagConstraints.BOTH;
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 0;
        contentPane.add(tp, gbc_tabbedPane);
    }

    private void lambda_NewProject()
    {
        Thread importThread = new Thread()
        {
            public void run()
            {
                class NewProject extends SwingWorker<Void, Void>
                {
                    private JFileChooser chooser = new JFileChooser();
                    private String filePath = "";
                    private boolean pass = false;

                    @Override
                    public Void doInBackground() throws Exception
                    {
                        try
                        {
                            chooser.setDialogTitle("Select Project Location");
                            chooser.showSaveDialog(null);
                            filePath = chooser.getSelectedFile().getAbsolutePath();
                            pass = true;
                        }
                        catch (NullPointerException ex)
                        {
                            //Do nothing
                            filePath = "";
                        }
                        return null;
                    }

                    @Override
                    public void done()
                    {
                        if (pass)
                        {
                            pass = false;
                            if (!filePath.equals(""))
                            {
                                manager.newProject(filePath);
                            }
                        }
                    }
                }
                new NewProject().execute();
            }
        };
        importThread.start();
    }

    private void lambda_OpenProject()
    {
        Thread openThread = new Thread()
        {
            public void run()
            {
                class OpenProject extends SwingWorker<Void, Void>
                {
                    private JFileChooser chooser = new JFileChooser();
                    private FileNameExtensionFilter filter;
                    private String filePath = "";
                    private boolean pass = false;

                    @Override
                    public Void doInBackground() throws Exception
                    {
                        try
                        {
                            filter = new FileNameExtensionFilter("Project File", "proj");
                            chooser.setFileFilter(filter);

                            chooser.showOpenDialog(null);
                            filePath = chooser.getSelectedFile().getAbsolutePath();
                            pass = true;
                        }
                        catch (NullPointerException ex)
                        {
                            //Do nothing
                            filePath = "";
                        }
                        return null;
                    }

                    @Override
                    public void done()
                    {
                        if (pass)
                        {
                            pass = false;
                            if (!filePath.equals(""))
                            {
                                manager.openProject(filePath);
                            }
                        }
                    }

                }
                new OpenProject().execute();
            }
        };
        openThread.start();
    }

    private void lambda_ImportSampleData()
    {
        Thread importThread = new Thread()
        {
            public void run()
            {
                class ImportGene extends SwingWorker<Void, Void>
                {
                    private JFileChooser chooser = new JFileChooser();
                    private FileNameExtensionFilter filter;
                    private String greenPath = "";
                    private String redPath = "";
                    private String genePath = "";
                    private boolean pass = false;

                    @Override
                    public Void doInBackground() throws Exception
                    {
                        try
                        {
                            filter = new FileNameExtensionFilter("TIF file", "tif");
                            chooser.setFileFilter(filter);

                            chooser.setDialogTitle("Load Red Image File...");
                            chooser.showOpenDialog(null);
                            redPath = chooser.getSelectedFile().getAbsolutePath();

                            chooser.setDialogTitle("Load Green Image File...");
                            chooser.showOpenDialog(null);
                            greenPath = chooser.getSelectedFile().getAbsolutePath();

                            chooser.setFileFilter(new FileNameExtensionFilter("TXT file", "txt"));
                            chooser.setDialogTitle("Load Gene Data File...");
                            chooser.showOpenDialog(null);
                            genePath = chooser.getSelectedFile().getAbsolutePath();

                            pass = true;
                        }
                        catch (NullPointerException ex)
                        {
                            JOptionPane.showMessageDialog(null, "Pair selection canceled.", "File warning",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        return null;
                    }

                    @Override
                    public void done()
                    {
                        if (pass)
                        {
                            pass = false;
                            if (!greenPath.equals("") && !redPath.equals("") && !genePath.equals(""))
                            {
                                manager.newSample("", greenPath, redPath, genePath);
                                JOptionPane.showMessageDialog(null, "Import Complete.");
                            }
                        }
                    }
                }
                new ImportGene().execute();
            }
        };
        importThread.start();
    }
}
