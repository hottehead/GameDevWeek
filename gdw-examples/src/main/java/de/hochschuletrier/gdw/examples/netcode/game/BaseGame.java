package de.hochschuletrier.gdw.examples.netcode.game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * Ignore what's in here, it's just a quick hack to get the app running and drawing.
 * 
 * @author Santo Pfingsten
 */
public abstract class BaseGame extends JApplet implements Runnable, KeyListener {
    private Thread thread;

	public BaseGame(String title) {
        init();
        Frame f = new Frame(title);
        f.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {System.exit(0);}
			@Override
            public void windowDeiconified(WindowEvent e) { start(); }
			@Override
            public void windowIconified(WindowEvent e) { stop(); }
        });
        f.add(this);
		f.addKeyListener(this);
        f.pack();
        f.setSize(new Dimension(640,480));
        f.setVisible(true);
	}

	@Override
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }


	@Override
    public synchronized void stop() {
        thread = null;
    }


	@Override
    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                thread.sleep(10);
            } catch (InterruptedException e) { break; }
        }
        thread = null;
    }

	
	@Override
    public void keyTyped(KeyEvent e) {
	}

	@Override
    public void keyPressed(KeyEvent e) {
	}

	@Override
    public void keyReleased(KeyEvent e) {
	}
}
