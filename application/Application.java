package application;

import application.gui.MainWindow;
import application.internal.Engine;

import javax.swing.JFrame;
import java.awt.EventQueue;

/**
 * Created by Lukas Pihl
 */
public class Application
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Engine engine = new Engine();
                    MainWindow frame = new MainWindow(engine);
                    frame.pack();
                    frame.setVisible(true);

                    // Causes frame to open in the center of the screen.
                    frame.setLocationRelativeTo(null);

                    // Sets the frame to maximum size on start
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
