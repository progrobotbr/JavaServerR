package com.jsv.client.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.Box;

public class DesktopScreenPainter extends JFrame implements ActionListener{
	
	private JPanel mDs=null;
	private JPanel mTd=null;
	private JPanel mMd=null;
	private JPanel mBd=null;
	private JPanel mTl=null;
	
	private JLabel lbMsg;
	private List <JComponent> componentList = new ArrayList<JComponent>();
	
	private ComponentWrangler wrangler = new ComponentWrangler();   
    private final int PAD = 0;   
    
    public void setLabelMsg(JLabel msg){
    	lbMsg = msg;
    }
    public void msg(String s){
    	lbMsg.setText(s);
    	
    }
    public void create(){
    	
		JButton btPro, btLab, btBut, btInp, btTab, btBox, btDel; //Objetos
		JButton btGen, btSave;
		
		FlowLayout ofl = new FlowLayout();
		ofl.setAlignment(FlowLayout.LEFT);
		ofl.setHgap(1);
		
		mDs = new JPanel();
		mTd = new JPanel();mTd.setBackground(new Color(58,58,58));
		mMd = new JPanel();mMd.setLayout(null);
		mBd = new JPanel();
		mTl = new JPanel();mTl.setBackground(new Color(102,102,102));
		JPanel mTlf = new JPanel();
		
		btPro=new JButton("PRP");btPro.setPreferredSize(new Dimension(32,30));
		btPro.setMargin(new Insets(1,1,1,1));
		btPro.setActionCommand("PROP");
		btPro.addActionListener(this);
		btBut=new JButton("BUT");btBut.setPreferredSize(new Dimension(32,30));
		btBut.setMargin(new Insets(1,1,1,1));
		btBut.setActionCommand("BUT");
		btBut.addActionListener(this);
		btInp=new JButton("INP");btInp.setPreferredSize(new Dimension(32,30));
		btInp.setMargin(new Insets(1,1,1,1));
		btInp.setActionCommand("INP");
		btInp.addActionListener(this);
		btLab=new JButton("LAB");btLab.setPreferredSize(new Dimension(32,30));
		btLab.setMargin(new Insets(1,1,1,1));
		btLab.setActionCommand("LAB");
		btLab.addActionListener(this);
		btTab=new JButton("TAB");btTab.setPreferredSize(new Dimension(32,30));
		btTab.setMargin(new Insets(1,1,1,1));
		btTab.setActionCommand("TAB");
		btTab.addActionListener(this);
		btBox=new JButton("BOX");btTab.setPreferredSize(new Dimension(32,30));
		btBox.setMargin(new Insets(1,1,1,1));
		btBox.setActionCommand("BOX");
		btBox.addActionListener(this);
		btDel=new JButton("DEL");btTab.setPreferredSize(new Dimension(32,30));
		btDel.setMargin(new Insets(1,1,1,1));
		btDel.setActionCommand("DEL");
		btDel.addActionListener(this);
		
		mDs.setLayout(new BorderLayout());
		mDs.add(mTd,BorderLayout.NORTH);
		mDs.add(mMd,BorderLayout.CENTER);
		mDs.add(mBd,BorderLayout.SOUTH);
		mDs.add(mTl,BorderLayout.WEST);
		mTl.setLayout(new BorderLayout());
		mTl.add(mTlf,BorderLayout.NORTH);
		
		mTlf.setLayout(new GridLayout(7,1));
		mTlf.add(btPro);
		mTlf.add(btDel);
		mTlf.add(btLab);
		mTlf.add(btInp);
		mTlf.add(btBut);
		mTlf.add(btTab);
		mTlf.add(btBox);
		
		btSave=new JButton("Gravar");btSave.setPreferredSize(new Dimension(130,20));
		btSave.setMargin(new Insets(1,1,1,1));
		btGen=new JButton("Gerar Metadados");btGen.setPreferredSize(new Dimension(130,20));
		btGen.setMargin(new Insets(1,1,1,1));
		btGen.setActionCommand("META");
		btGen.addActionListener(this);
		mTd.setLayout(ofl);
		mTd.add(btSave);
		mTd.add(btGen);
		
		wrangler.setCollection(componentList);
		this.setLayout(new GridLayout(0,1));
		this.add(mDs);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,400);
		this.setVisible(true);
	}
    
    public void actionPerformed(ActionEvent e){
    	Border bd1;
    	String s = e.getActionCommand();
    	JComponent jc;
    	attribComp atrib;
    	if(s.equals("BUT")){
    		if(wrangler.windowPropIsOpen==true) return;
    		bd1 = new LineBorder(Color.LIGHT_GRAY,1);
    		JButton bt = new JButton();
    		bt.setName(s);
    		bt.setBorder(bd1);
    		bt.setPreferredSize(new Dimension(80,20));
    		bt.setText(s);
    		atrib = this.addNext(bt);
    		atrib.tx.setText(s);
    	}else if(s.equals("INP")){
    		if(wrangler.windowPropIsOpen==true) return;
    		JTextField tf = new JTextField();
    		bd1 = new LineBorder(Color.LIGHT_GRAY,1);
    		tf.setBorder(bd1);
    		tf.setName(s);
    		tf.setEditable(false);
    		tf.setBackground(Color.WHITE);
    		tf.setPreferredSize(new Dimension(80,20));
    		this.addNext(tf);
    	}else if(s.equals("LAB")){
    		if(wrangler.windowPropIsOpen==true) return;
    		JLabel lb = new JLabel("obj");
    		bd1 = new LineBorder(Color.LIGHT_GRAY,1);
    		lb.setName(s);
    		lb.setText(s);
    		lb.setBorder(bd1);
    		lb.setPreferredSize(new Dimension(80,20));
    		atrib = this.addNext(lb);
    		atrib.tx.setText(s);
    	}else if(s.equals("TAB")){
    		if(wrangler.windowPropIsOpen==true) return;
    		int lin=5*21+12;
    		JPanel jp = new JPanel();
    		JPanel jp1 = new JPanel();
    		String sh[] = { "Col1", "Col2" };
    		String ln[][] = new String[5][2];
    		JTable tb = new JTable(ln,sh);
    		tb.setRowHeight(20);
    		tb.setRowSelectionAllowed(false);
    		tb.setEnabled(false);
    		tb.setAutoscrolls(true);
    		this.addMove(tb);
    		jp.setLayout(new GridLayout(1,0));
    		jp1.setLayout(new BorderLayout());
    		
    		jp.setPreferredSize(new Dimension(120,lin));
    		jp.setBackground(Color.LIGHT_GRAY);
    		
    		jp1.add(tb.getTableHeader(),BorderLayout.NORTH);
    		jp1.add(tb,BorderLayout.CENTER);
    		jp1.add(tb);
    		
    		jp.add(jp1);
    	    jp.setName(s);
    	    jp.repaint();
    	    
    	    this.addNext(jp);
    		this.setVisible(true); //Força o repaint de toda a janela, show de bola
    		
    	}else if(s.equals("BOX")){
    		JPanel jp = new JPanel();
    		jp.setBorder(BorderFactory.createTitledBorder("Novo"));
    		jp.setPreferredSize(new Dimension(100,100));
    		jp.setName(s);
    		atrib = this.addNext(jp);
    		atrib.ln.setText("100");
    		atrib.id.setText("Novo");
    		this.setVisible(true); 
    		
    	}else if(s.equals("DEL")){
    		if(wrangler.windowPropIsOpen==true) return;
    		jc = wrangler.selectedComponent;
    		if(jc==null){ return; }
    		Component cp = jc;
    		JPanel jf = (JPanel) cp.getParent();
    		jf.remove(cp);
    		componentList.remove(jc);
    		wrangler.mem.remove(jc);
    		wrangler.selectedComponent=null;
    		mDs.repaint();
    		this.repaint();
    		this.setVisible(true);
    	}else if(s.equals("CLOS")){
    		JButton bt2=(JButton) e.getSource();
    		JPanel of2 = (JPanel) bt2.getParent();
    		JPanel of3 = (JPanel) of2.getParent();
    		componentList.remove(of2);
    		of3.remove(of2);
    		mDs.repaint();
    		this.repaint();
    		//this.setVisible(true);
    	}else if(s.equals("PROP")){
    		jc = wrangler.selectedComponent;
    		if(jc==null){return;}
    		if(wrangler.windowPropIsOpen==true) {return;}
    		if(jc instanceof JLabel){
    			this.windowLab(jc);
    		}else if(jc instanceof JTextField){
    			this.windowInp(jc);
    		}else if(jc instanceof JButton){
    			this.windowBut(jc);
    		}else if(jc.getName().equals("TAB")){
    			this.windowTab(jc);
    		}else if(jc.getName().equals("BOX")){
    			this.windowTab(jc);
    		}
    	}else if(s.equals("PASS")){
    		attribComp ac;
    		if(wrangler.mem.containsKey(wrangler.selectedComponent)){
    			ac = wrangler.mem.get(wrangler.selectedComponent);
    			ac.moveAttrib(wrangler.selectedComponent);
    		}
    		JButton bt2=(JButton) e.getSource();
    		JPanel of2 = (JPanel) bt2.getParent();
    		JPanel of3 = (JPanel) of2.getParent();
    		componentList.remove(of2);
    		of3.remove(of2);
    		mDs.repaint();
    		this.repaint();
    		wrangler.windowPropIsOpen=false;
    	}else if(s.equals("META")){
    		this.metadados();
    	}
    	
    }
    
    private void windowInp(JComponent jc){
    	JLabel lb;
    	JButton bt;
    	JTextField tf;
    	Border bd;
    	JPanel jp = new JPanel();
    	JPanel jpdsk = new JPanel();
    	jpdsk.setName("PROP");
    	attribComp ac;
    	
    	if(!wrangler.mem.containsKey(jc)){
    		return;
    	}
    	
    	ac = wrangler.mem.get(jc);
    	
    	jpdsk.setLayout(new BorderLayout());
		jp.setLayout(new GridLayout(4,2));
		jpdsk.setPreferredSize(new Dimension(140,120));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Id", ac.id);
		this.putPair2(jp,"Caption", ac.tx);
		this.putPair2(jp,"Largura", ac.wd);
		this.putPair2(jp,"ReadOnly", ac.ro);
		
		bt=new JButton("Fechar");
		bt.setPreferredSize(new Dimension(60,20));
		bt.setMargin(new Insets(1,1,1,1));
		bt.setActionCommand("PASS");
		bt.addActionListener(this);
		
		lb=new JLabel("TextField");
		jpdsk.add(jp, BorderLayout.CENTER);
		jpdsk.add(bt,BorderLayout.SOUTH);
		jpdsk.add(lb,BorderLayout.NORTH);
		bd = new LineBorder(Color.BLUE,1);
		jpdsk.setBorder(bd);
		this.addNext2(jpdsk);
		this.setVisible(true); //Força o repaint de toda a janela, show de bola    
		wrangler.windowPropIsOpen=true;
		wrangler.windowProp=jpdsk;
    }
    
    private void windowBut(JComponent jc){
    	JLabel lb;
    	JButton bt;
    	JTextField tf;
    	Border bd;
    	attribComp ac;
    	
    	if(!wrangler.mem.containsKey(jc)){
    		return;
    	}
    	
    	ac = wrangler.mem.get(jc);
    	
    	JPanel jp = new JPanel();
    	JPanel jpdsk = new JPanel();
    	jpdsk.setName("PROP");
    	
    	jpdsk.setLayout(new BorderLayout());
		jp.setLayout(new GridLayout(4,2));
		jpdsk.setPreferredSize(new Dimension(140,120));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Id", ac.id);
		this.putPair2(jp,"Caption", ac.tx);
		this.putPair2(jp,"Largura", ac.wd);
		
		bt=new JButton("Fechar");
		bt.setPreferredSize(new Dimension(60,20));
		bt.setMargin(new Insets(1,1,1,1));
		bt.setActionCommand("PASS");
		bt.addActionListener(this);
		
		lb=new JLabel("Button");
		jpdsk.add(jp, BorderLayout.CENTER);
		jpdsk.add(bt,BorderLayout.SOUTH);
		jpdsk.add(lb,BorderLayout.NORTH);
		bd = new LineBorder(Color.BLUE,1);
		jpdsk.setBorder(bd);
		this.addNext2(jpdsk);
		this.setVisible(true); //Força o repaint de toda a janela, show de bola
		wrangler.windowPropIsOpen=true;  
		wrangler.windowProp=jpdsk;
    }
    
    private void windowLab(JComponent jc){
    	JLabel lb;
    	JButton bt;
    	JTextField tf;
    	Border bd;
    	attribComp ac;
    	
    	if(!wrangler.mem.containsKey(jc)){
    		return;
    	}
    	
    	ac = wrangler.mem.get(jc);
    	
    	JPanel jp = new JPanel();
    	JPanel jpdsk = new JPanel();
    	jpdsk.setName("PROP");
    	
    	jpdsk.setLayout(new BorderLayout());
		jp.setLayout(new GridLayout(4,2));
		jpdsk.setPreferredSize(new Dimension(140,120));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Id", ac.id);
		this.putPair2(jp,"Caption", ac.tx);
		this.putPair2(jp,"Largura", ac.wd);
		
		bt=new JButton("Fechar");
		bt.setPreferredSize(new Dimension(60,20));
		bt.setMargin(new Insets(1,1,1,1));
		bt.setActionCommand("PASS");
		bt.addActionListener(this);
		
		lb=new JLabel("Label");
		jpdsk.add(jp, BorderLayout.CENTER);
		jpdsk.add(bt,BorderLayout.SOUTH);
		jpdsk.add(lb,BorderLayout.NORTH);
		bd = new LineBorder(Color.BLUE,1);
		jpdsk.setBorder(bd);
		this.addNext2(jpdsk);
		this.setVisible(true); //Força o repaint de toda a janela, show de bola   
		wrangler.windowPropIsOpen=true;
		wrangler.windowProp=jpdsk;
    }

    private void windowTab(JComponent jc){
    	JLabel lb;
    	JButton bt;
    	Border bd;
    	attribComp ac;
    	
    	if(!wrangler.mem.containsKey(jc)){
    		return;
    	}
    	
    	ac = wrangler.mem.get(jc);
    	
    	JPanel jp = new JPanel();
    	JPanel jpdsk = new JPanel();
    	jpdsk.setName("PROP");
    	
    	jpdsk.setLayout(new BorderLayout());
		jp.setLayout(new GridLayout(4,2));
		jpdsk.setPreferredSize(new Dimension(140,120));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Nome", ac.id);
		this.putPair2(jp,"Largura", ac.wd);
		this.putPair2(jp,"Qtd Linhas", ac.ln);
		
		bt=new JButton("Fechar");
		bt.setPreferredSize(new Dimension(60,20));
		bt.setMargin(new Insets(1,1,1,1));
		bt.setActionCommand("PASS");
		bt.addActionListener(this);
		
		lb=new JLabel("Tabela");
		jpdsk.add(jp, BorderLayout.CENTER);
		jpdsk.add(bt,BorderLayout.SOUTH);
		jpdsk.add(lb,BorderLayout.NORTH);
		bd = new LineBorder(Color.BLUE,1);
		jpdsk.setBorder(bd);
		this.addNext2(jpdsk);
		this.setVisible(true); //Força o repaint de toda a janela, show de bola   
		wrangler.windowPropIsOpen=true;
		wrangler.windowProp=jpdsk;
    }

    private void putPair(JPanel p, String label, String id){
    	Border bd = new LineBorder(Color.DARK_GRAY,1);
    	JLabel lb = new JLabel(label);
    	lb.setBorder(bd);
		JTextField tf = new JTextField();
		tf.setName(id);
		p.add(lb);p.add(tf);
    }

    private void putPair2(JPanel p, String label, JTextField tf){
    	Border bd = new LineBorder(Color.DARK_GRAY,1);
    	JLabel lb = new JLabel(label);
    	lb.setBorder(bd);
		p.add(lb);p.add(tf);
		
    }
    
    private static void createAndShowGUI() {
		DesktopScreenPainter oDsp = new DesktopScreenPainter();
		oDsp.create();
	}
	
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	private Point getNextLocation(Dimension d){   
        int maxX = 0, maxY = 0;   
        JComponent c, last = null;   
        Rectangle r;   
        // find level of lowest component(s)   
        for(int j = 0; j < componentList.size(); j++)   
        {   
            c = componentList.get(j);   
            r = c.getBounds();   
            if(r.y + r.height > maxY)   
            {   
                maxY = r.y + r.height;   
                last = c;   
            }   
        }   
        // find last (in row) of lowest components   
        for(int j = 0; j < componentList.size(); j++)   
        {   
            c = componentList.get(j);   
            r = c.getBounds();   
            if(r.y + r.height == maxY && r.x + r.width > maxX)   
            {   
                maxX = r.x + r.width;   
                last = c;   
            }   
        }   
        // determine location of next component based on location of last   
        Point p = new Point();   
        if(last == null)                                // first component   
        {   
            p.x = PAD;   
            p.y = PAD;   
            return p;   
        }   
        r = last.getBounds();   
        int x, y;   
        if(r.x + r.width + PAD + d.width < getWidth())  // next in row   
        {   
            p.x = r.x + r.width + PAD;   
            p.y = r.y;   
        }   
        else                                            // skip to new row   
        {   
            p.x = PAD;   
            p.y = r.y + r.height + PAD;   
        }   
        return p;   
    }   
    
	public void setBorderHigh(JComponent c){
		Border bd = new LineBorder(Color.BLUE,1);
		if(wrangler.selectedComponent!=null){
			this.setBorderLow(wrangler.selectedComponent);
		}
		wrangler.selectedComponent = c;
		c.setBorder(bd);
	}
	
	public void setBorderLow(JComponent c){
		Border bd;
		wrangler.selectedComponent = c;
		attribComp at = wrangler.mem.get(c);
		bd=at.OrignalBorder;
		c.setBorder(bd);
	}
	
	public attribComp addNext(JComponent c){
		int i,t,z,y;
		String s;
		Component cps[];
		s=c.getName();
		attribComp atrib = new attribComp();
		atrib.jc = c;
		
		atrib.OrignalBorder = c.getBorder();
		wrangler.mem.put(c,atrib);
		
        componentList.add(c);   
        c.addMouseListener(wrangler);   
        c.addMouseMotionListener(wrangler);
        mMd.add(c,0);
        
        //Esta parte foi necessária por causa do BOX
        //Ela faz com que o ZOrder do box sempre fique em ordem mais alta
        cps=mMd.getComponents();
        z=t=cps.length;
        z--;
        y=0;
        for(i=0;i<t;i++){
        	s=cps[i].getName();
        	if(s!=null && s.equals("BOX")){
        		mMd.setComponentZOrder(cps[i], z--);
        	}else{
        		mMd.setComponentZOrder(cps[i], y++);
        	}
        }
        Dimension d = c.getPreferredSize();   
        Point p = getNextLocation(d);   
        c.setBounds(p.x, p.y, d.width, d.height);
        atrib.wd.setText(""+(int)c.getSize().getWidth());
        this.setBorderHigh(c);
        mMd.repaint(); 
        mDs.repaint();
        repaint();
        return(atrib);
    }   
	
	public void addMove(JComponent c){
		c.addMouseListener(wrangler);   
        c.addMouseMotionListener(wrangler);   
	}
	
	public void addNext2(JComponent c){   
        componentList.add(c);   
        c.addMouseListener(wrangler);   
        c.addMouseMotionListener(wrangler);   
        mMd.add(c);   
        Dimension d = c.getPreferredSize();   
        Point p = getNextLocation(d);   
        c.setBounds(p.x, p.y, d.width, d.height);
        mMd.repaint(); 
        mDs.repaint();
        repaint();
    }
	
	public void metadados(){
		boolean ok=true;
		int i,i2,t,x,y,r1,r2;
		String s;
		JComponent jc;
		attribComp acs[], ac, acold;
		t=wrangler.componentList.size();
		acs = new attribComp[t];
		for(i=0;i<t;i++){
			jc = wrangler.componentList.get(i);
			acs[i] = wrangler.mem.get(jc);
			acs[i].setBounds(jc.getBounds());
		}
		
		while(ok){
			ok=false;
			for(i=1;i<t;i++){
				i2 = i-1;
				r1=acs[i2].getBounds().y*10000+acs[i2].getBounds().x;
				r2=acs[i].getBounds().y*10000+acs[i].getBounds().x;
				if(r1>r2){
					acold = acs[i2];
					acs[i2] = acs[i];
					acs[i] = acold;
					ok=true;
				}
			}
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append("<ss id='1000'>");
		for(i=0;i<t;i++){
			ac = acs[i];
			System.out.println("Y " + ac.getBounds().y + " X " + ac.getBounds().x);
			jc=ac.jc;
			s=jc.getName();
			if(s.equals("LAB")){
				sb.append("<lb id='");
				sb.append(ac.id.getText().toUpperCase());
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' tx='");
				sb.append(ac.tx.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("'/>");
			}else if(s.equals("INP")){
				sb.append("<if id='");
				sb.append(ac.id.getText().toUpperCase());
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' nc='");
				sb.append(ac.nc.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("'/>");
			}else if(s.equals("BUT")){
				sb.append("<bt id='");
				sb.append(ac.id.getText().toUpperCase());
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' tx='");
				sb.append(ac.tx.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("'/>");
			}else if(s.equals("BOX")){
				sb.append("<bx id='");
				sb.append(ac.id.getText().toUpperCase());
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' tx='");
				sb.append(ac.tx.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("'/>");
			}
		}
		sb.append("</ss>");
		System.out.println(sb.toString());
		
	}

	private class ComponentWrangler extends MouseInputAdapter{
		
		private List <JComponent> componentList = new ArrayList<JComponent>();
		private HashMap<JComponent,attribComp> mem = new HashMap<JComponent,attribComp>();
		
		public JComponent selectedComponent;
		public JComponent windowProp;;
	    Point offset;   
	    boolean dragging; 
	    public boolean windowPropIsOpen=false;
	    
	    public void setCollection(List <JComponent> jc){
	    	componentList = jc;
	    }
	    
	    public ComponentWrangler()   
	    {   
	    	dragging = false;   
	    }   
	
	    public void mousePressed(MouseEvent e)   
	    {   
	    	JComponent c;
	    	
	    	/*
	    	 * windowPropIsOpen => indica que a janela de propriedades está ativa
	    	 * windowProp       => objeto que representa a janela de propriedades
	    	 */
	    	
	    	if(windowPropIsOpen==true) {
	    		c = (JComponent) e.getSource();
	    		/*
	    		 * Isto evita que a janela de propriedades fique louca quando
	    		 * ela estiver ativa e for clicado em outro objeto
	    		 */
	    		if(c != this.windowProp) { return; }
	    		
	    		//Pega os dados de posicionamento
	    		offset = e.getPoint();   
		        dragging = true;
	    		return;
	    	}	
	    	
	    	if(selectedComponent!=null){
	    		c = selectedComponent;
	    		this.setBorderLow(c);
	    	}
	    	
	    	selectedComponent = (JComponent)e.getSource(); 
	    	//Tratamento para tabela
	    	if(selectedComponent instanceof JTable){
	    		selectedComponent = (JComponent) selectedComponent.getParent().getParent();
	    	}
	    	
	    	c = selectedComponent;
	    	
	    	
	    	this.setBorderHigh(c);
	    	offset = e.getPoint();   
	        dragging = true;   
	    }   
	
	    public void mouseReleased(MouseEvent e){   
	        dragging = false;
	    }   
	
	    public void mouseDraggedOrginal(MouseEvent e){   
	    	if(dragging){
	        	Rectangle r = selectedComponent.getBounds();   
	            r.x += e.getX() - offset.x;   
	            r.y += e.getY() - offset.y;
	            selectedComponent.setBounds(r);  
	        }   
	    }
	    
	    public void mouseDragged(MouseEvent e){   
	    	int i,t,z,df;
	    	Rectangle r,r1;
	    	JComponent lselectedComponent;
	    	
	    	if(this.windowPropIsOpen==true){
	    		lselectedComponent = this.windowProp;
	    	}else{
	    		lselectedComponent=this.selectedComponent;
	    	}
	    	
	        if(dragging){
	        	r = lselectedComponent.getBounds();
	        	r1 = lselectedComponent.getBounds();
	        	i=e.getY() - offset.y;
	            r.x += e.getX() - offset.x;  
	            r.y += i;
	            if(r.y<0) r.y=0;
	            if(r.x<0) r.x=0;
	            z=t=r.y;
	            t/=22;
	            t*=22;
	            if(z>t){
	            	df=z-t;
	            	if(df>13) {
	            		t+=22;
	            		r.y=t;
	            	}else{
	            		r.y=t;
	            	}
	            }
	            if(checkColision(lselectedComponent,r)) {
	            	lselectedComponent.setBounds(r1);
	            	return;
	            }
	            lselectedComponent.setBounds(r);
	            //log(r.x);
	        }   
	    } 
	    
	    public boolean checkColision(JComponent pjc, Rectangle r1){
	    	int i,x1,x2,y1,y2,xx1,xx2,yy1,yy2;
	    	String s;
	    	Rectangle r2; 
	    	JComponent jc;
	    	s=pjc.getName();
	    	
	    	//Box pode navegar livremente pelos objetos
	    	if(s!=null && s.equals("BOX")){
	    		return(false);
	    	}
	    	
	    	x1=r1.x;
			y1=r1.y;
			x2=r1.x+r1.width;
			y2=r1.y+r1.height;
	    	for(i=0;i<componentList.size();i++){
	    		jc=componentList.get(i);
	    		s=jc.getName();
	    		if(s!=null && s.equals("BOX")){
	    			continue;
	    		}
	    		if(pjc!=jc){
	    			r2=jc.getBounds();
	    			xx1=r2.x;
	    			xx2=r2.x+r2.width;
	    			yy1=r2.y;
	    			yy2=r2.y+r2.height;
	    			if(xx1>=x1 && xx1<=x2 && yy1>=y1 && yy1 <=y2){return(true);}
	    			if(xx2>=x1 && xx2<=x2 && yy2>=y1 && yy2 <=y2){return(true);}
	    			//
	    			if(x1>=xx1 && x1<=xx2 && y1>=yy1 && y1 <=yy2){return(true);}
	    			if(x2>=xx1 && x2<=xx2 && y2>=yy1 && y2 <=yy2){return(true);}
	    		}
	    	}	
	    	return(false);
	    }
	    
	    
	    public void log(int x){
	    	System.out.println(x);
	    }
	    
	    public void setBorderHigh(JComponent c){
			Border bd = new LineBorder(Color.BLUE,1);
			c.setBorder(bd);
		}
		
		public void setBorderLow(JComponent c){
			attribComp at = mem.get(c);
			Border bd1 = at.OrignalBorder;
			c.setBorder(bd1);
		}
	} 
	
	private class attribComp extends JComponent{
		public JTextField id=new JTextField(), 
		                  wd=new JTextField(),
		                  tx=new JTextField(),
		                  nc=new JTextField(),
		                  tm=new JTextField(),
		                  tl=new JTextField(),
		                  hd=new JTextField(),
		                  ro=new JTextField(), //readonly???
		                  vl=new JTextField(),
						  ln=new JTextField();
		
		public JComponent jc;
		public Border OrignalBorder;
		
		public void moveAttrib(JComponent jc){
			JLabel lb; JTextField tf; JButton bt; Dimension dm;
			int i,t;
			if(jc instanceof JLabel){
				lb=(JLabel) jc;
				try{
					i=Integer.parseInt(wd.getText()); 
				}catch(Exception ex){ i=(int)lb.getSize().getWidth(); }
				t = (int)lb.getSize().getHeight();
				lb.setSize(i,t);
				lb.setText(tx.getText());
			}else if(jc instanceof JTextField){
				tf = (JTextField)jc;
				try{
					i=Integer.parseInt(wd.getText()); 
				}catch(Exception ex){ i=(int)tf.getSize().getWidth(); }
				t = (int)tf.getSize().getHeight();
				tf.setSize(i,t);
				tf.setText(tx.getText());
			}else if(jc instanceof JButton){
				bt = (JButton)jc;
				try{
					i=Integer.parseInt(wd.getText()); 
				}catch(Exception ex){ i=(int)bt.getSize().getWidth(); }
				t = (int)bt.getSize().getHeight();
				bt.setSize(i,t);
				bt.setText(tx.getText());
			}else{ //Tabela
				String sName;
				sName = jc.getName();
				if(sName==null){ return; }
				if(sName=="TAB"){
					JTable tb;
					JComponent jc2;
					try{
						i=Integer.parseInt(wd.getText()); 
					}catch(Exception ex){ i=(int)jc.getSize().getWidth(); }
					try{
						int qlin = Integer.parseInt(ln.getText());
						t = Integer.parseInt(ln.getText());
						t = t*21+12;
						jc2=(JComponent) jc.getComponent(0);
						tb=(JTable) jc2.getComponent(1);
						TableModel tm = tb.getModel();
						int cc = tm.getColumnCount();
						String s[][] = new String[qlin][cc];
						String h[] = { "Col1", "Col2" };  
						DefaultTableModel dtm = new DefaultTableModel(s, h);
						tb.setModel(dtm);
						tb.setName("tb1");
						
					}catch(Exception ex){ 
						t = (int)jc.getSize().getHeight();
					}
					jc.setSize(i,t);
					jc.validate();
					jc.repaint();
				}else if(sName.equals("BOX")){
					Border bd;
					try{
						i=Integer.parseInt(wd.getText()); 
					}catch(Exception ex){ i=(int)jc.getSize().getWidth(); }
					try{
						t=Integer.parseInt(ln.getText()); 
					}catch(Exception ex){ t=(int)jc.getSize().getHeight(); }
					bd=BorderFactory.createTitledBorder(id.getText());
					this.OrignalBorder=bd;
					jc.setBorder(bd);
					jc.setSize(i,t);
				}
			}
		}
	}
}
