package com.jsv.zteste;

	import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;

import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollButton;
import javax.swing.table.JTableHeader;
	
	public class TableExample2 extends JFrame {
	  public TableExample2() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLayout(null);
	    JPanel pn = new JPanel();
	    JPanel pn2 = new JPanel();
	    //pn.setLayout(new BorderLayout());
	    pn.setLayout(new GridLayout(0,1));
	    pn2.setLayout(new BorderLayout());
	    //pn.setLayout(null);
	    //pn.setPreferredSize(new Dimension(50,50));
	    //Container content = getContentPane();
	    String[] head = {"O","Two","Three"};
	    String[][] data = {{"R", "12345678", "R1-C3"},
	                       {"R", "R2-C2", "R2-C3"},
	                       {"R", "R3-C2", "R3-C3"},
	                       {"R", "R2-C2", "R2-C3"},
	                       {"R", "R3-C2", "R3-C3"},
	                       {"R", "R3-C2", "R3-C3"},
	                       {"R", "R2-C2", "R2-C3"},
	                       {"R", "R3-C2", "R3-C3"},
	                       {"R", "R2-C2", "R2-C3"},
	                       {"R", "R3-C2", "R3-C3"},
	                       {"R", "R3-C2", "R3-C3"}};
	    JTable jt = new JTable(data, head);
	    
	   //ListSelectionModel lsm;
	    //lsm.se
	    JScrollBar jsb = new JScrollBar(JScrollBar.VERTICAL,0,60,0,300);
	    jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    jt.setRowSelectionAllowed(true);
	    jt.setAutoscrolls(false);
	    JScrollPane jsp = new JScrollPane(jt);
	    //jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
	    jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	    jsp.getVerticalScrollBar().getModel().setMaximum(10);
	    jsp.getVerticalScrollBar().getModel().setMinimum(2);
	    jsp.getVerticalScrollBar().getModel().setValue(8);
	    jsp.setAutoscrolls(false);
	    //jsp.setVerticalScrollBar(jsb);
	    MetalScrollButton msb; 
	    processBt prc = new processBt();
	    Component cp[]=jsp.getVerticalScrollBar().getComponents();
	    for(int i=0;i<cp.length;i++){
	    	msb=(MetalScrollButton)cp[i];
	    	if(i==0) msb.setActionCommand("BAIX"); else msb.setActionCommand("CIMA"); 
	    	msb.addActionListener(prc);
	    }
	    
	    JTextField jtf = new JTextField();
	    jtf.setDocument(new LimitedPlainDocument(10));
	    jt.setLayout(null);
	    jt.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jtf));
	    jt.setAutoscrolls(true);
	    jt.getModel().setValueAt("RENATO",2, 2);
	    /*
	    jt.getColumnModel().getColumn(2).setWidth(300);
	    jt.getColumnModel().getColumn(2).setPreferredWidth(200);
	    jt.getColumnModel().getColumn(1).setPreferredWidth(5);
	    jt.getColumnModel().getColumn(0).setPreferredWidth(5);
	    */
	    jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    jt.getColumnModel().getColumn(2).setWidth(300);
	    jt.getColumnModel().getColumn(2).setPreferredWidth(200);
	    jt.getColumnModel().getColumn(1).setPreferredWidth(70);
	    jt.getColumnModel().getColumn(0).setPreferredWidth(5);
	    
	    jt.setRowSelectionInterval(1, 1);
	    /*
	    jt.getColumnModel().getColumn(2).setWidth(200);
	    jt.getColumnModel().getColumn(1).setWidth(10);
	    jt.getColumnModel().getColumn(0).setWidth(10);
	    */
	    jt.setRowHeight(20);
	    setSize(500,500);
	    //pn.setSize(200,30);
	    pn.add(jsp);
	    JScrollBar jsb2 = new JScrollBar(JScrollBar.VERTICAL,0,60,0,300);
	    //JScrollBar jsb2 = new JScrollBar(JScrollBar.VERTICAL,0,60,0,300);
	    pn2.add(pn,BorderLayout.CENTER);
	    pn2.add(jsb2,BorderLayout.EAST);
	    /*
	    pn.add(jt,BorderLayout.CENTER);
	    pn.add(jsb,BorderLayout.EAST);
	    pn.add(jsb2,BorderLayout.SOUTH);
	    */
	    pn.setBounds(30,30,305,240);
	    pn2.setBounds(30,30,305,240);
	    
	    //pn.setBackground(new Color(145,222,132));
	    this.setLayout(new BorderLayout());
	    this.add(pn2, BorderLayout.CENTER);
	  }
	  public static void main(String[] args) { new TableExample2().setVisible(true); }
	
	
	class LimitedPlainDocument extends javax.swing.text.PlainDocument {
	  private int maxLen = -1;
	  /** Creates a new instance of LimitedPlainDocument */	
	  public LimitedPlainDocument() {}
	  public LimitedPlainDocument(int maxLen) { this.maxLen = maxLen; }
	  public void insertString(int param, String str, 
	                           javax.swing.text.AttributeSet attributeSet) 
	                      throws javax.swing.text.BadLocationException {
	    if (str != null && maxLen > 0 && this.getLength() + str.length() > maxLen) {
	      java.awt.Toolkit.getDefaultToolkit().beep();
	      return;
	    }
	    super.insertString(param, str, attributeSet);
	  }
	}
	
	class processBt implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			 System.out.println(ae.getActionCommand()); 
		}
		
	}
	
	

}
