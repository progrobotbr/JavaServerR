package com.jsv.zteste;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DropMode;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class teste2 implements ActionListener {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		teste();
		teste2 on = new teste2();
		on.init();
	}
	
	private static void teste(){
		System.out.println("teste2");
	}
	
	Button ob=null;
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_SPACE)
			System.out.println("SPACE is pressed!");
	}
	public void init() {
		
		JFrame oFr = new JFrame();
		oFr.setLayout(null);
		JPanel op = new JPanel();
		JTextField of = new JTextField();
		JEditorPane je = new JEditorPane();
		of.addActionListener(this);
		je.setText("Teste\nTeste");
		//Button ob;
		/*
		op.setLayout(null);
		ob = new Button("Gravar");ob.setBounds(10,20,200,25);op.add(ob);
		ob = new Button("Alterar");ob.setBounds(10,47,200,25);op.add(ob);
		ob = new Button("3");ob.setBounds(200,80,200,25);op.add(ob);
		ob = new Button("4");ob.setBounds(10,110,200,25);op.add(ob);
		ob = new Button("5");ob.setBounds(10,140,200,25);op.add(ob);
		ob = new Button("R");ob.setBounds(10,170,200,25);op.add(ob);
		ob = new Button("Dummy");ob.setBounds(1,1,1,1);op.add(ob);
		of.setBounds(10,200,200,21);op.add(of);
		op.setSize(50,100);
		oFr.add(op);*/
		//oFr.add(new Button("4"));
		StyleConstants sc;
		// Get the text pane's document
        JTextPane textPane = new JTextPane();
        StyledDocument doc = (StyledDocument)textPane.getDocument();
    
        // Create a style object and then set the style attributes
        Style style = doc.addStyle("StyleName", null);

        StyleConstants.setBackground(style, Color.blue);
        try{
        // Append to document
        doc.insertString(doc.getLength(), "Some Text", style);
        StyleConstants.setBackground(style, Color.red);
        doc.setCharacterAttributes(1, 3, style, true);
        //doc.setLogicalStyle(3, style);
        
        //doc.setParagraphAttributes(1, 3, style, false);
        
        }catch(Exception e){}
		//sc.
		//oFr.add(textPane);
		oFr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		oFr.setSize(400,400);
		oFr.add(of);
		of.setEnabled(false);
		
		of.setBounds(10,10,300,50);
		//textPane.add(of);
		oFr.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		         //ob.requestFocus();
		         //ob.setVisible(false);
		      }
		    } ); 
		textPane.addKeyListener(
        	new java.awt.event.KeyListener() {               
            public void keyPressed(KeyEvent e) { 
            	
                System.out.println(e);         
            }   
           public void keyReleased(KeyEvent e){   
                         //testaBtnGravar();   
            }   
           public void keyTyped(KeyEvent e)   {         
                //testaBtnGravar();   
        	   }   
          }
           );
		//oFr.pack();
		oFr.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		System.out.println("kkkk");
	}
		 

	


}
