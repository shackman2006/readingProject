package readingProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * @see http://stackoverflow.com/q/3066590/230513
 * 15-Mar-2011 r8 http://stackoverflow.com/questions/5274962
 * 26-Mar-2013 r17 per comment
 */
public class ReadingProject implements Runnable {

	private String[] args;
	private boolean waiting;
	private Queue<String> myQueue;
	private JButton b;
	private Timer timer;
	private JLabel label2;
	private int delay;
	
	
	
    public ReadingProject(String[] args) {
		this.args = args;
		this.waiting = true;
		this.delay = 100;
	}

	public static void main(String[] args) {
        EventQueue.invokeLater(new ReadingProject(args));
    }
	
	private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            //System.out.println("Button Pressed");
            if(waiting){
            	waiting=false;
            	b.setText("Pause");
            } else {
            	waiting= true;
            	b.setText("Start");
            }
        }
    }
    @Override
    public void run() {
    	
    	//Timer timer = new Timer();
    	//Panel
    	
    	//Add everything
        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();
        
        //North Panel
        final JLabel label = new JLabel("Text:", JLabel.CENTER);
        northPanel.add(label, BorderLayout.NORTH);
        
        //Center Panel
        label2 = new JLabel("text", JLabel.CENTER);
        label2.setFont(new Font(label2.getFont().toString(), Font.PLAIN, 26));
        centerPanel.add(label2, BorderLayout.SOUTH);
        
        //South Panel
        b = new JButton("Start");
        southPanel.add(b);
        b.addActionListener(new ButtonHandler());
        
    	//Visual Stuff
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.add(new MainPanel());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setSize(300,150);
        f.add(northPanel, BorderLayout.NORTH);
        f.add(centerPanel, BorderLayout.CENTER);
        f.add(southPanel, BorderLayout.SOUTH);
        
        
        //f.add(panel);
        f.repaint();
        
        f.setVisible(true);
        myQueue = new LinkedList<String>();
        //Load text into queue
        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String lines[] = line.split(" ");
				for (String s : lines) {
					myQueue.add(s);
				}
				//System.out.println(line);
				//label2.setText(line);
				//System.out.println("Waiting 1 second");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//Timer Stuff
        /*Timer timer = new Timer(175, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(!myQueue.isEmpty() && !waiting) {
            		String s = myQueue.remove();
                    label2.setText(s);
                    
                	//System.out.println("Tick tock!");
                    //repaint();
            	} else {
            		//label2.setText("END");
            	}
                
            }
        });*/
		timer = new Timer(175, tmpListener);
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();
		
		/*Timer timer = new Timer(150, null);
		timer.setRepeats(false);
		timer.setCoalesce(true);
		for(String s : myQueue) {
			label2.setText(s);
			System.out.println("Delay:" + timer.getDelay());
			timer.setDelay(100*s.length());
			timer.start();
		}*/
		
        
        //End Timer STuff
    }
    ActionListener tmpListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!myQueue.isEmpty() && !waiting) {
        		String s = myQueue.remove();
                label2.setText(s);
                if(!myQueue.isEmpty()) {
                	timer.setDelay(delay+(15*myQueue.peek().length()));
                }
                //System.out.println(timer.getDelay()+":"+s);

        	} else {
        		label2.setText("Text Complete");
        	}
		}
	
	};
}