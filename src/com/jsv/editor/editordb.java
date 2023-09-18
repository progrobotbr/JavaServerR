package com.jsv.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.jsv.compiler.compiler;
import com.jsv.db.base.sqlbase;

import com.jsv.lang.program.format;
import com.jsv.lang.program.line;
import com.jsv.lang.program.program;
import com.jsv.lang.vm.function;
import com.jsv.lang.vm.vm;

public class editordb extends JPanel implements ActionListener {

	/**
	 * @param args
	 */
	private boolean bPrgIniciado = false;
	private String sTexto="";
	private JTextPane textArea1= new JTextPane();
	private JTextArea textArea2 = new JTextArea();
	JTextField tfLinhas = new JTextField();
	JTextField tfNmPrg = new JTextField();
	JTextField tfDsPrg = new JTextField();
	private int iTam=0;
	private program gProgram=null;
	private BParser runner=new BParser();
	private vm vmc = new vm();
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Schedule a job for the event dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });

	}

	editordb() {
		setLayout(new BorderLayout());
		//setLayout(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints(); 
		textArea2.setEditable(false);
		JScrollPane areaScrollPane1 = new JScrollPane(textArea1);
		areaScrollPane1.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
		});
		areaScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane1.setPreferredSize(new Dimension(500, 300));
		areaScrollPane1.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Editor"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane1.getBorder()));
		
		JScrollPane areaScrollPane2 = new JScrollPane(textArea2);
		areaScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane2.setPreferredSize(new Dimension(500, 100));
		areaScrollPane2.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Avisos"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane2.getBorder()));
		areaScrollPane2.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		//JPanel topPane = new JPanel(new GridLayout(3,2));
		JPanel topPane = new JPanel();
		//topPane.setLayout(new GridBagLayout());
		topPane.setLayout(new BorderLayout());
		
		c.fill = GridBagConstraints.NORTH;
	    //c.gridheight =  GridBagConstraints.REMAINDER;
		//c.ipady = 40;      //make this component tall
		c.gridx = 0;
		c.gridy = 0;
		topPane.add(areaScrollPane1,BorderLayout.CENTER);
		
		//c.fill = GridBagConstraints.BOTH;
		//c.weightx = 0.0;
		//c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		topPane.add(areaScrollPane2,BorderLayout.SOUTH);
		
		JButton button  = new JButton("Carregar");
		button.addActionListener(this);
		button.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		JButton button1 = new JButton("Gravar");
		button1.addActionListener(this);
		JButton button2 = new JButton("Compilar");
		button2.addActionListener(this);
		JButton button3 = new JButton("Maiuscula");
		button3.addActionListener(this);
		JButton button4 = new JButton("Minuscula");
		button4.addActionListener(this);
		JButton button5 = new JButton("Step");
		button5.addActionListener(this);
		JButton button6 = new JButton("Carregar2");
		button6.addActionListener(this);
		tfLinhas.setColumns(3);
		tfLinhas.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		tfNmPrg.setColumns(5);
		tfDsPrg.setColumns(3);
		JPanel botPane = new JPanel();
		//c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		
		topPane.add(botPane,BorderLayout.NORTH);
		botPane.add(button);
		botPane.add(button1);
		botPane.add(button2);
		botPane.add(button3);
		botPane.add(button4);
		botPane.add(button5);
		botPane.add(button6);
		botPane.add(tfNmPrg);
		botPane.add(tfDsPrg);
		botPane.add(tfLinhas);
		add(topPane);
	    this.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		botPane.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		topPane.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		this.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
		
		textArea1.addCaretListener(new CaretListener() {
			 public void caretUpdate(CaretEvent e) {
				 JTextPane editArea = (JTextPane)e.getSource();
				 int linenum = 1;                 
				 int columnnum = 1;                 
				 try {                                  
					 int caretpos = editArea.getCaretPosition();
					 Rectangle caretCoords = editArea.modelToView(caretpos);
					 int lineHeight = editArea.getFontMetrics(editArea.getFont()).getHeight();
					 int posLine = ((int)caretCoords.getY() / lineHeight) + 1; 
					 //linenum = editArea.get.getLineOfOffset(caretpos);                     
					 //columnnum = caretpos - editArea.getLineStartOffset(linenum);         
					 //updateStatus(1,1);
					 tfLinhas.setText(""+posLine);
					 tfLinhas.setAlignmentX(2);
					} catch(Exception ex) { }
				 }
			 });
		
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new editordb());
		
		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				
			}
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		String sCmd = e.getActionCommand().toUpperCase();
		if(sCmd.equals("CARREGAR")){
			textArea1.setText(load());
			setMsg("Carregamento efetuado");
		}else if(sCmd.equals("GRAVAR")){
			if(save(textArea1.getText())){
				setMsg("Ok Gravado");
			}else{
				setMsg("Erro ao Gravar");
			}
		}else if(sCmd.equals("COMPILAR")){
			compile2();
		}else if(sCmd.equals("MAIUSCULA")){
			String s1 = textArea1.getText();
			s1 = s1.toUpperCase();
			textArea1.setText(s1);
		}else if(sCmd.equals("MINUSCULA")){
			String s1 = textArea1.getText();
			s1 = s1.toLowerCase();
			textArea1.setText(s1);
			StyledDocument doc = (StyledDocument)textArea1.getDocument();
			Style style = doc.addStyle("StyleName", null);
			StyleConstants.setBackground(style, Color.BLUE);
			StyleConstants.setForeground(style, Color.WHITE);
			doc.setCharacterAttributes(0,19, style, true);
			textArea1.getCaretPosition();
			System.out.println(textArea1.getCaretPosition());
			//doc.setLogicalStyle(3, style);
		}else if(sCmd.equals("STEP")){
			this.compileStep();
		}else if(sCmd.equals("CARREGAR2")){
			//this.load2();
			this.load3();
		}

	}
	
	public String load(){
		String sLine="";
		StringBuilder sb = new StringBuilder();
		try{
			FileInputStream fstream = new FileInputStream("C:\\prog.bas");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((sLine = br.readLine()) != null){
			  sb.append(sLine+"\n");
			}
			in.close();
		}catch(Exception e){}
		return(sb.toString());
		
	}
	
	public boolean save(String text){
		try{
			FileOutputStream fstream = new FileOutputStream("C:\\prog.bas");
			fstream.write(text.getBytes());
			fstream.close();
			return(true);
		}catch(Exception e){ }
		return(false);
		
	}
	public void compile(){
		int i=0;
		bPrgIniciado=false;
		String s=textArea1.getText();
		format source = new format();
		StringReader st = new StringReader(s);
		editor edd = new editor();
		AParserAll yyparser = new AParserAll(st,edd);
		//HashMap <String,function>fs;
		//function f;
		i=yyparser.yyparse();
		if(i==0){
			//Criar a lista de subs
			gProgram = source.getEditor(s);
			runner.setVM(vmc);
			vmc.setParser(runner);
			i = vmc.createSubDecl(gProgram);
			//Compilação ok
			if(i==0){
				gProgram.saveProgram();
				setMsg("Compilação OK");
			}
		}
	}
	
	public void compile2(){
		compiler cp = new compiler();
		sqlbase oSqlBase = new sqlbase();
		String s=textArea1.getText();
		String s1=tfNmPrg.getText().toUpperCase();
		String s2=tfDsPrg.getText().toUpperCase();
		if(s1.length()>0){
			cp.compile(oSqlBase,s,s1,s2);
			setMsg(cp.errmsg);
		}else{
			setMsg("Informar nome do programa");
		}
	}
	
	public void compileStep(){
		int i;
		runner.setVM(vmc);
		vmc.setParser(runner);
		if(bPrgIniciado==false){
			if(vmc.loadInitialSub("MAIN",false)==vm.PRG_LINE_SUCCESS){
				bPrgIniciado=true;
			}else{
				setMsg("Erro ao chamar a MAIN");
				return;
			}
		}
		i=vmc.executeLine();
		switch(i){
		case vm.PRG_END_WITH_SUCCESS :
			setMsg("Processamento concluído");
			break;
		case vm.PRG_LINE_SUCCESS :
        	setMsg("Linha processada com sucesso");
        	break;
        default: setMsg("Erro ao processar a Linha");
        }
		
	}
	
	public void compileStepAll(){
		int i=gProgram.programCount;
		int t;
		String s;
		line l[]=gProgram.lines;
		t = gProgram.finalSize-1;
		i=0;
		while(i++<t){
			s=l[i].lineData;
			if(runner.yyparse(s)==0){
				gProgram.programCount++;
			}else {
				break;
			}
		}
		
	}
	
	public void load2(){
		int i;
		i = vmc.loadProgram(runner, "prog");
		if(i==0){
			bPrgIniciado = false;
			setMsg("Load Ok");
		}else{
			setMsg("Load Err");
		}
	}

	public void load3(){
		//Simplesmente um teste para ver se carrega do banco
		int i;
		String s="REN001";
		program p = new program();
		p.Name = s;
		String s1[] = new String[1];
		p.init(s1);
		i=p.loadPogramDB(s);
		if(i==0){
			bPrgIniciado = false;
			setMsg("Load Ok");
		}else{
			setMsg("Load Err");
		}
	}
	public void setMsg(String s){
		s+='\n';
		textArea2.insert(s,iTam);
		iTam+=s.length();
	}
}
