package com.jsv.trz.local.component;

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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.utils.Utils;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.gui.components.rbox;
import com.jsv.gui.components.rbutton;
import com.jsv.gui.components.rinput;
import com.jsv.gui.components.rlabel;
import com.jsv.gui.components.rtable;
import com.jsv.gui.components.rtabstrip;

public class ScreenPainter extends JPanel implements ActionListener{
	
	public String mMetaDadosP1;  //Retorna o XML com o protocolo de comunicação p1
	public String mMetaDadosP2;  //Retorna o XML com o protocolo de comunicação p2
	public String mScreen;
	
	private JPanel mDs=null;
	private JPanel mTd=null;
	private JPanel mMd=null;
	private JPanel mBd=null;
	private JPanel mTl=null;
	
	private JLabel lbMsg;
	
	//Funções apoio
	public void setLabelMsg(JLabel msg){
		lbMsg = msg;
	}
	public void setScreenNr(String s){
		mScreen=s;
	}
	public void msg(String s){
	  	lbMsg.setText(s);
	}
	
	//Objetos globais
	private List <JComponent> componentList = new ArrayList<JComponent>();
	
	private ComponentWrangler wrangler = new ComponentWrangler();   
    private final int PAD = 0;   
    
    public void create(){
    	
		JButton btPro, btLab, btBut, btInp, btTab, btBox, btDel, btTbs; //Objetos
		JButton btGen, btSave;
		
		mMetaDadosP1 = "";
		mMetaDadosP2 = "";
		
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
		btTbs=new JButton("TBS");btTab.setPreferredSize(new Dimension(32,30));
		btTbs.setMargin(new Insets(1,1,1,1));
		btTbs.setActionCommand("TBS");
		btTbs.addActionListener(this);
		btDel=new JButton("DEL");btTab.setPreferredSize(new Dimension(32,30));
		btDel.setMargin(new Insets(1,1,1,1));
		btDel.setActionCommand("DEL");
		btDel.addActionListener(this);
		
		mDs.setLayout(new BorderLayout());
		//mDs.add(mTd,BorderLayout.NORTH);
		mDs.add(mMd,BorderLayout.CENTER);
		//mDs.add(mBd,BorderLayout.SOUTH);  //Não é mais utilizado pois usa a área de msg do pai
		mDs.add(mTl,BorderLayout.WEST);
		mTl.setLayout(new BorderLayout());
		mTl.add(mTlf,BorderLayout.NORTH);
		
		mTlf.setLayout(new GridLayout(8,1));
		mTlf.add(btPro);
		mTlf.add(btDel);
		mTlf.add(btLab);
		mTlf.add(btInp);
		mTlf.add(btBut);
		mTlf.add(btTab);
		mTlf.add(btTbs);
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
		
		//JScrollPane jsp = new JScrollPane(mDs);
		//jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//jsp.add(mDs);
		this.add(mDs);
		
		//this.setLayout(new GridLayout());
		//
		//this.setSize(500,400);
		//this.setVisible(true);
		//
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
    		atrib.Type=3;
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
    		atrib = this.addNext(tf);
    		atrib.Type=2;
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
    		atrib.Type=1;
    	}else if(s.equals("TAB")){
    		if(wrangler.windowPropIsOpen==true) return;
    		rtable.ScreenPainterCreate(this, s);
    		/*
    		int lin=5*21+12;
    		JPanel jp = new JPanel();
    		JPanel jp1 = new JPanel();
    		String sh[] = { "Col1" };
    		String ln[][] = new String[1][1];
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
    	    
    	    atrib =this.addNext(jp);
    	    atrib.Type=4;
    	    */
    		this.setVisible(true); //Força o repaint de toda a janela, show de bola
    		
    	}else if(s.equals("BOX")){
    		rbox.ScreenPainterCreate(this, s);
    		/*
    		JPanel jp = new JPanel();
    		jp.setBorder(BorderFactory.createTitledBorder("Novo"));
    		jp.setPreferredSize(new Dimension(100,100));
    		jp.setName(s);
    		atrib = this.addNext(jp);
    		atrib.ln.setText("100");
    		atrib.id.setText("Novo");
    		atrib.Type=5;
    		*/
    		this.setVisible(true); 
    	
    	}else if(s.equals("TBS")){ //TabStrip
    		rtabstrip.ScreenPainterCreate(this, s);
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
    		}else if(jc.getName().equals("TBS")){
    			this.windowTbs(jc);
    		}else if(jc.getName().equals("BOX")){
    			this.windowTab(jc);
    		}
    	}else if(s.equals("PASS")){
    		boolean b;
    		attribComp ac;
    		if(wrangler.mem.containsKey(wrangler.selectedComponent)){
    			ac = wrangler.mem.get(wrangler.selectedComponent);
    			ac.moveAttrib(wrangler.selectedComponent);
    			b=this.validBlanks(ac);
    			if(b==false){
    				msg("Preencher todos os parâmetros");
    				return;
    			}
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
    		this.metadados(null);
    	}
    	
    }
    
    public void clearPainter(){
    	mMetaDadosP1 = "";
    	mMetaDadosP2 = "";
    	componentList = new ArrayList<JComponent>();
    	wrangler = new ComponentWrangler();
    	wrangler.setCollection(componentList);
    	mMd.removeAll();
    	mMd.repaint();
    	mDs.repaint();
    	//mDs.add(mMd,BorderLayout.CENTER);
    }
    
    private boolean isBlank(JTextField tf){
    	if(tf.getText().trim().length()==0){
    		return(true);
    	}else{
    		return(false);
    	}
    }
    private boolean validBlanks(attribComp ac){
    	int i;
    	i=ac.Type;
    	switch(i){
    	case 1:  //Label
    		if(isBlank(ac.id) || isBlank(ac.tx) || isBlank(ac.wd)){
    			msg("Preencher propriedades");
    			return(false);
    		}
    		break;
    	case 2: //InputField
    		if(isBlank(ac.id) || isBlank(ac.wd) || isBlank(ac.nc)){
    			msg("Preencher propriedades");
    			return(false);
    		}
    		break;
    	case 3: //Button
    		if(isBlank(ac.id) || isBlank(ac.tx) || isBlank(ac.wd)){
    			msg("Preencher propriedades");
    			return(false);
    		}
    		break;
    	case 4: //Table
    		if(isBlank(ac.id) || isBlank(ac.tx) || isBlank(ac.ln)){
    			msg("Preencher propriedades");
    			return(false);
    		}
    		break; 
    	case 5: //Box
    		break;
    	}
    	return(true);
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
		jp.setLayout(new GridLayout(5,2));
		jpdsk.setPreferredSize(new Dimension(140,120));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Id", ac.id);
		this.putPair2(jp,"Texto", ac.tx);
		this.putPair2(jp,"Largura", ac.wd);
		this.putPair2(jp,"Max.Car.", ac.nc);
		this.putPair2(jp,"ReadOnly.", ac.ro);
		
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
		this.validate();
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
		this.validate();
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
		this.validate();
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
		jp.setLayout(new GridLayout(6,2));
		jpdsk.setPreferredSize(new Dimension(180,180));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Id", ac.id);
		this.putPair2(jp,"Titulo", ac.tx);
		this.putPair2(jp,"Largura", ac.wd);
		this.putPair2(jp,"Altura", ac.hd);
		this.putPair2(jp,"Qtd Linhas", ac.ln);
		this.putPair2(jp,"Colunas", ac.cl);
		
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
		this.validate();
    }

    private void windowTbs(JComponent jc){
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
		jp.setLayout(new GridLayout(5,2));
		jpdsk.setPreferredSize(new Dimension(140,120));
		jp.setBackground(Color.LIGHT_GRAY);
		
		this.putPair2(jp,"Id", ac.id);
		this.putPair2(jp,"Id Screen", ac.tx);
		this.putPair2(jp,"Largura", ac.wd);
		this.putPair2(jp,"Altura", ac.hd);
		this.putPair2(jp,"Abas", ac.cl);
		
		bt=new JButton("Fechar");
		bt.setPreferredSize(new Dimension(60,20));
		bt.setMargin(new Insets(1,1,1,1));
		bt.setActionCommand("PASS");
		bt.addActionListener(this);
		
		lb=new JLabel("Tab Strip");
		jpdsk.add(jp, BorderLayout.CENTER);
		jpdsk.add(bt,BorderLayout.SOUTH);
		jpdsk.add(lb,BorderLayout.NORTH);
		bd = new LineBorder(Color.BLUE,1);
		jpdsk.setBorder(bd);
		this.addNext2(jpdsk);
		this.setVisible(true); //Força o repaint de toda a janela, show de bola
		wrangler.windowPropIsOpen=true;  
		wrangler.windowProp=jpdsk;
		this.validate();
    	
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
		ScreenPainter oDsp = new ScreenPainter();
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
        //Ela faz com que o ZOrder do box sempre fique em ordem mais baixa
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
	
	/*********************************************************
	 * Protocolo P1 e P2
	 *********************************************************/
	public boolean metadados(String pScreen){
		boolean ok=true,b;
		int i,i2,t,x,y,r1,r2;
		String s, stemp;
		StringBuilder sb,sb1;
		JComponent jc;
		attribComp acs[], ac, acold;
		
		mMetaDadosP1="";
		mMetaDadosP2="";
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
		
		/*
		 * Valida componentes
		 * Valida IDs - em branco não deixa passar
		 */
		ok=true;
		Border bd=new LineBorder(Color.RED,1);;
		for(i=0;i<t;i++){
			ac = acs[i];
			b=validBlanks(ac); 
			if(b==false){
				ac.jc.setBorder(bd);
				ok=false;
			}
		}
		if(ok==false){
			msg("Preencher atributo do componente");
			return(false);
		}
		//Fim valida ID
		
		sb=new StringBuilder();
		sb1=new StringBuilder();
		//sb.append("<ss id='");sb.append(pScreen);sb.append("'>");
		//sb1.append("<sd id='");sb1.append(pScreen);sb1.append("'>");
		for(i=0;i<t;i++){
			ac = acs[i];
			//System.out.println("Y " + ac.getBounds().y + " X " + ac.getBounds().x);
			jc=ac.jc;
			s=jc.getName();
			if(s.equals("LAB")){
				rlabel.ScreenPainterCreateProtocolP1P2(ac, sb, sb1);
				/*
				stemp = ac.id.getText().toUpperCase(); //id
				//p1
				sb.append("<lb id='");
				sb.append(stemp);
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' tx='");
				sb.append(ac.tx.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("'/>");
				*/
				//p2
				//sb1.append("<lb id='");sb1.append(stemp);sb1.append("' vl=''/>");
			}else if(s.equals("INP")){
				rinput.ScreenPainterCreateProtocolP1P2(ac, sb, sb1);
				/*
				stemp = ac.id.getText().toUpperCase(); //id
				//p1
				sb.append("<if id='");
				sb.append(stemp);
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' nc='");
				sb.append(ac.nc.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("' ro='");
				sb.append(ac.ro.getText());
				sb.append("'/>");
				sb1.append("<if id='");sb1.append(stemp);sb1.append("' vl=''/>");
				*/
			}else if(s.equals("BUT")){
				rbutton.ScreenPainterCreateProtocolP1P2(ac, sb, sb1);
				/*
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
				*/
			}else if(s.equals("BOX")){
				rbox.ScreenPainterCreateProtocolP1P2(ac, sb, sb1);
				/*
				sb.append("<bx id='");
				sb.append(ac.id.getText().toUpperCase());
				sb.append("' wd='");
				sb.append(ac.wd.getText());
				sb.append("' hd='");
				sb.append(ac.ln.getText());
				sb.append("' tx='");
				sb.append(ac.tx.getText());
				sb.append("' x='");
				sb.append(ac.jc.getBounds().x);
				sb.append("' y='");
				sb.append(ac.jc.getBounds().y);
				sb.append("'/>");
				*/
			}else if(s.equals("TAB")){
				rtable.ScreenPainterCreateProtocolP1P2(ac,sb, sb1);
				/*
				String sColTitle, sColName, sColWidth, sp1="", sp2="";
				String scs[] = ac.cl.getText().split(";");
				Vector vcol=new Vector(), vpro;
				
				for(int ii=0; ii<scs.length; ii++){
					String sc[] = scs[ii].split(",");
					vpro = new Vector();
					for(int iii=0; iii<sc.length; iii++){
						vpro.add(sc[iii]);
					}
					vcol.add(vpro);
				}
				int cc = vcol.size();
				
				sp1=sp2="";
				for(int ii=0;ii<cc;ii++){
					sColName = (String)((Vector)vcol.get(ii)).get(attribComp.TABCOLNAME);
					sColTitle = (String)((Vector)vcol.get(ii)).get(attribComp.TABCOLTITLE);
					sColWidth = (String)((Vector)vcol.get(ii)).get(attribComp.TABCOLWIDTH);
					sp1 += "<tc id=\""+sColName.toUpperCase()+"\" vl=\""+sColTitle+"\" wd=\""+sColWidth+"\" ro=\""+ii+"\"/>";
					sp2 += "<tc id=\""+sColName.toUpperCase()+"\" vl=\"\" ps=\""+ii+"\"/>";
				}
				sp1 = "<tb id=\""+ac.id.getText().toUpperCase()+"\" tm=\""+cc+"\" tl=\""+ac.ln.getText()+"\" wd=\""+ac.wd.getText()+"\" hd=\""+ac.hd.getText()+"\" x=\""+ ac.jc.getBounds().x + "\" y=\""+ ac.jc.getBounds().y + "\">"+
				      "<th>"+sp1+"</th></tb>";
				sp2 = "<tb id=\""+ac.id.getText().toUpperCase()+"\"><th ps=\"1\">"+sp2+"</th></tb>";
					
				sb.append(sp1);
				sb1.append(sp2);
				*/
				
			}else if(s.equals("TBS")){
				rtabstrip.ScreenPainterCreateProtocolP1P2(ac, sb, sb1);
				
			}
		}
		//sb.append("</ss>");
		//sb1.append("</sd>");
		mMetaDadosP1 = sb.toString();
		mMetaDadosP2 = sb1.toString();
		msg("Metadados do protocolo gerados com sucesso");
		//System.out.println(sb.toString());
		return(true);
	}
	
	public String getProtocolP1(String pScreen){
		StringBuilder sb = new StringBuilder();
		sb.append("<ss id='");
		sb.append(pScreen);
		sb.append("'>");
		sb.append(mMetaDadosP1);
		sb.append("</ss>");
		return(sb.toString());
	}

	public String getProtocolP2(String pScreen){
		StringBuilder sb = new StringBuilder();
		sb.append("<sd id='");
		sb.append(pScreen);
		sb.append("'>");
		sb.append(mMetaDadosP2);
		sb.append("</sd>");
		return(sb.toString());
	}
	/*********************************************************
	 *  Fim protocolo
	 *********************************************************/
	
	/*********************************************************
	 *  Load
	 *********************************************************/
	public void loadObjects(String s){
		int iCodCp;
		String s1, s2, s3, s4, sX, sY, sW, sH;
		if(s==null||s.length()==0){
			return;
		}
		Document doc=null;
		try{
			InputStream is = new ByteArrayInputStream(s.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			doc = docBuilder.parse(is);
		}catch(Exception ex){ return;}
		
		Border bd1;
		attribComp atrib;
		int t,i,i2,t2, iX,iY,iW,iH;
		Element oElem = doc.getDocumentElement();
		NodeList oNl1 = oElem.getChildNodes();
		Node oNo1 = oNl1.item(0);
		Node oNo2,oNo3;
		NodeList oNl3;
		//oNl2 = oNo1.getChildNodes();
		t=oNl1.getLength();
		for(i=0;i<t;i++){
			oNo2 = oNl1.item(i); // tr?
			//oNl3 = oNo2.getChildNodes();
			//t2=oNl3.getLength();
			//oNo3=oNl3.item(1);
			s2 = oNo2.getNodeName();
			iCodCp = ComponentsDictionary.dictionaryElement(s2);
			if (iCodCp == 0) { // Erro }
				return;
			}
			switch (iCodCp) {
			case ComponentsDictionary.LABEL: 
				rlabel.ScreenPainterLoad(this, oNo2);
				/*
				sX = Utils.getAttribute(oNo2,"x");
				sY = Utils.getAttribute(oNo2,"y");
				iX = Integer.parseInt(sX);
				iY = Integer.parseInt(sY);
				sW = Utils.getAttribute(oNo2,"wd");
				iW = Integer.parseInt(sW);
				s1 = Utils.getAttribute(oNo2,"id");
				s2 = Utils.getAttribute(oNo2,"tx");
				bd1 = new LineBorder(Color.LIGHT_GRAY,1);
	    		JLabel lb = new JLabel();
	    		lb.setText(s2);
	    		lb.setName("LAB");
	    		lb.setBorder(bd1);
	    		lb.setPreferredSize(new Dimension(iW,20));
	    		this.addNext3(lb, 1, s1, s2, "", "", iX,iY, iW, 0, null);
	    		*/
	    		break;
			case ComponentsDictionary.INPUTFIELD:
				rinput.ScreenPainterLoad(this, oNo2);
				/*
				sX = Utils.getAttribute(oNo2,"x");
				sY = Utils.getAttribute(oNo2,"y");
				iX = Integer.parseInt(sX);
				iY = Integer.parseInt(sY);
				sW = Utils.getAttribute(oNo2,"wd");
				iW = Integer.parseInt(sW);
				s1 = Utils.getAttribute(oNo2,"id");
				s2 = Utils.getAttribute(oNo2,"vl");
				s3 = Utils.getAttribute(oNo2,"nc");
				s4 = Utils.getAttribute(oNo2,"ro");
				bd1 = new LineBorder(Color.LIGHT_GRAY,1);
				JTextField tf = new JTextField();
	    		bd1 = new LineBorder(Color.LIGHT_GRAY,1);
	    		tf.setBorder(bd1);
	    		tf.setName("INP");
	    		tf.setEditable(false);
	    		tf.setBackground(Color.WHITE);
	    		tf.setPreferredSize(new Dimension(iW,20));
	    		this.addNext3(tf, 2, s1, "", s2, s3, iX, iY, iW,0, s4);
	    		*/
	    		break;
			case ComponentsDictionary.BUTTON:
				rbutton.ScreenPainterLoad(this, oNo2);
				/*
				sX = Utils.getAttribute(oNo2,"x");
				sY = Utils.getAttribute(oNo2,"y");
				iX = Integer.parseInt(sX);
				iY = Integer.parseInt(sY);
				sW = Utils.getAttribute(oNo2,"wd");
				iW = Integer.parseInt(sW);
				s1 = Utils.getAttribute(oNo2,"id");
				s2 = Utils.getAttribute(oNo2,"tx");
				bd1 = new LineBorder(Color.LIGHT_GRAY,1);
	    		JButton bt = new JButton();
	    		bt.setName("BUT");
	    		bt.setText(s2);
	    		bt.setBorder(bd1);
	    		bt.setPreferredSize(new Dimension(iW,20));
	    		atrib=this.addNext3(bt, 3, s1, s2, "", "", iX, iY, iW,0, null);
	    		atrib.tx.setText(s2);
	    		*/
	    		break;
			case ComponentsDictionary.BOX:
				rbox.ScreenPainterLoad(this, oNo2);
				/*
				sX = Utils.getAttribute(oNo2,"x");
				sY = Utils.getAttribute(oNo2,"y");
				iX = Integer.parseInt(sX);
				iY = Integer.parseInt(sY);
				sW = Utils.getAttribute(oNo2,"wd");
				iW = Integer.parseInt(sW);
				sH = Utils.getAttribute(oNo2,"hd");
				iH = Integer.parseInt(sH);
				s1 = Utils.getAttribute(oNo2,"id");
				s2 = Utils.getAttribute(oNo2,"tx");
				bd1 = new LineBorder(Color.LIGHT_GRAY,1);
	    		JPanel jp = new JPanel();
	    		jp.setBorder(BorderFactory.createTitledBorder(s2));
	    		jp.setPreferredSize(new Dimension(iW,iH));
	    		jp.setName("BOX");
	    		atrib=this.addNext3(jp, 5, s1, s2, "", "", iX, iY, iW, iH, null);
	    		atrib.Type=5;
	    		this.setVisible(true);
	    		*/
	    		break;
	    		
			case ComponentsDictionary.TABLE:
				rtable.ScreenPainterLoad(this, oNo2);
				//this.addNext(jp2);
				//this.setVisible(true);
				break;
			case ComponentsDictionary.TABSTRIP:
				rtabstrip.ScreenPainterLoad(this, oNo2);
			}
		}
		
	}
	
	public attribComp addNext3(JComponent c, int pType, String pId, String pCaption, String pValue, String pQtdChar, int pX, int pY, int pW, int pH, String pReadOnly){
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
        c.setBounds(pX, pY, d.width, d.height);
        atrib.wd.setText(""+(int)c.getSize().getWidth());
        atrib.ln.setText(""+(int)c.getSize().getHeight());
        
        //ReadOnly
        if(pReadOnly!=null && pReadOnly.trim().length()!=0) {
			c.setBackground(Color.YELLOW);
		}
        //c.setBounds(pX, pY, pW, pH);
        //this.setBorderHigh(c);
        mMd.repaint(); 
        mDs.repaint();
        repaint();
        atrib.Type=pType;
        atrib.id.setText(pId);
        atrib.tx.setText(pCaption);
        atrib.vl.setText(pValue);
        atrib.nc.setText(pQtdChar);
        atrib.ro.setText(pReadOnly);
        return(atrib);
    }
	
	public attribComp addNextTab(JComponent c, int pType, String pId, String pTl, String pWd, String pHd, String pCl, int px, int py){
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
        c.setBounds(px, py, d.width, d.height);
        
        mMd.repaint(); 
        mDs.repaint();
        repaint();
        atrib.Type=pType;
        atrib.id.setText(pId);
        atrib.ln.setText(pTl);
        atrib.wd.setText(pWd);
        atrib.hd.setText(pHd);
        atrib.tx.setText("<...>");
        atrib.cl.setText(pCl);
        return(atrib);
    }
	

	public attribComp addNextTabStrip(JComponent c, int pType, String pId, String pSc, String pWd, String pHd, String pCl, int px, int py){
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
        c.setBounds(px, py, d.width, d.height);
        
        mMd.repaint(); 
        mDs.repaint();
        repaint();
        atrib.Type=pType;
        atrib.id.setText(pId);
        //atrib.ln.setText(pTl);
        atrib.wd.setText(pWd);
        atrib.hd.setText(pHd);
        atrib.tx.setText(pSc);
        //atrib.tx.setText("<...>");
        atrib.cl.setText(pCl);
        return(atrib);
    }
	
	
	/*********************************************************
	 * Fim load Object
	*********************************************************/
	
	/*********************************************************
	 * Drag in Drop 
	 *********************************************************/
	
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
	
	public class attribComp extends JComponent{
		public JTextField id=new JTextField(), 
		                  wd=new JTextField(),
		                  tx=new JTextField(),
		                  nc=new JTextField(),
		                  tm=new JTextField(),
		                  tl=new JTextField(),
		                  hd=new JTextField(),
		                  ro=new JTextField(),
		                  vl=new JTextField(),
						  ln=new JTextField(),
						  cl=new JTextField(),
						  scid=new JTextField();
						  
		
		public static final int TABCOLNAME = 0;
		public static final int TABCOLTITLE = 1;
		public static final int TABCOLWIDTH = 2;
				
		public int Type;
		public JComponent jc;
		public Border OrignalBorder;
				
		public void moveAttrib(JComponent jc){
			JLabel lb; JTextField tf; JButton bt; Dimension dm;
			int i,t;
			if(jc instanceof JLabel){
				rlabel.ScreenPainterRedefine(this);
				/*
				lb=(JLabel) jc;
				try{
					i=Integer.parseInt(wd.getText()); 
				}catch(Exception ex){ i=(int)lb.getSize().getWidth(); }
				t = (int)lb.getSize().getHeight();
				lb.setSize(i,t);
				lb.setText(tx.getText());
				*/
			}else if(jc instanceof JTextField){
				rinput.ScreenPainterRedefine(this);
				/*
				tf = (JTextField)jc;
				try{
					i=Integer.parseInt(wd.getText()); 
				}catch(Exception ex){ i=(int)tf.getSize().getWidth(); }
				t = (int)tf.getSize().getHeight();
				tf.setSize(i,t);
				tf.setText(tx.getText());
				//Cor do ReadOnly
				if(ro.getText().trim().length()==0){
					tf.setBackground(Color.WHITE);
				}
				*/
			}else if(jc instanceof JButton){
				rbutton.ScreenPainterRedefine(this);
				/*
				bt = (JButton)jc;
				try{
					i=Integer.parseInt(wd.getText()); 
				}catch(Exception ex){ i=(int)bt.getSize().getWidth(); }
				t = (int)bt.getSize().getHeight();
				bt.setSize(i,t);
				bt.setText(tx.getText());
				*/
			}else{ //Tabela
				String sName, sCol, sColTitle;
				int iwidth=0, ihd=0, iwd=0;
				sName = jc.getName();
				if(sName==null){ return; }
				if(sName=="TAB"){
					rtable.ScreenPainterRedefine(this);
					/*
					JTable tb;
					JComponent jc2;
					try{
						iwd=Integer.parseInt(wd.getText()); 
						ihd=Integer.parseInt(hd.getText()); 
					}catch(Exception ex){ i=(int)jc.getSize().getWidth(); }
					try{
						Vector vcol = new Vector();
						Vector vpro;
						int qlin = Integer.parseInt(ln.getText());
						t = Integer.parseInt(ln.getText());
						t = t*21+16;
						sCol = cl.getText();
						jc2=(JComponent) jc.getComponent(0);
						tb=(JTable) jc2.getComponent(1);
						TableModel tm = tb.getModel();
						String scs[] = sCol.split(";");
						for(int ii=0; ii<scs.length; ii++){
							String sc[] = scs[ii].split(",");
							vpro = new Vector();
							for(int iii=0; iii<sc.length; iii++){
								vpro.add(sc[iii]);
							}
							vcol.add(vpro);
						}
						//int cc = tm.getColumnCount();
						int cc = vcol.size();
						String s[][] = new String[qlin][cc];
						//String h[] = { "Col1", "Col2" };
						String h[] = new String[cc];
						for(int ii=0;ii<cc;ii++){
							sColTitle = (String)((Vector)vcol.get(ii)).get(attribComp.TABCOLTITLE);
							h[ii] = sColTitle;
						}
						DefaultTableModel dtm = new DefaultTableModel(s, h);
						tb.setModel(dtm);
						for(int ii=0;ii<cc;ii++){
							vpro = (Vector)vcol.get(ii);
							sColTitle = (String)vpro.get(attribComp.TABCOLTITLE );
							try{
								iwidth = Integer.parseInt((String)vpro.get(attribComp.TABCOLWIDTH));
							}catch(Exception ex){}
							tb.getColumn(sColTitle).setPreferredWidth(iwidth);
						}
						tb.setName(id.toString());
						tb.setPreferredSize(new Dimension(iwd,ihd));
						
						
					}catch(Exception ex){ 
						t = (int)jc.getSize().getHeight();
					}
					jc.setSize(iwd,ihd);
					jc.validate();
					jc.repaint();
					*/
				}else if(sName.equals("BOX")){
					rbox.ScreenPainterRedefine(this);
					/*
					Border bd;
					try{
						i=Integer.parseInt(wd.getText()); 
					}catch(Exception ex){ i=(int)jc.getSize().getWidth(); }
					try{
						t=Integer.parseInt(ln.getText()); 
					}catch(Exception ex){ t=(int)jc.getSize().getHeight(); }
					bd=BorderFactory.createTitledBorder(tx.getText());
					this.OrignalBorder=bd;
					jc.setBorder(bd);
					jc.setSize(i,t);
					*/
				}else if(sName.equals("TBS")){
					rtabstrip.ScreenPainterRedefine(this);
					
				}
			}
		}
	}
}
