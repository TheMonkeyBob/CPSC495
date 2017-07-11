package application;

import application.engine.Engine;

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
