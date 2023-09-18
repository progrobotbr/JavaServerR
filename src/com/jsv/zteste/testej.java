package com.jsv.zteste;
import java.awt.*;
import java.applet.Applet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.jsv.lang.vm.vm;

import com.jsv.data.Variant;
		
public class testej {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testej on = new testej();
		//on.init();
		/*boolean b = false;
		float f = 1000;
		int i = 32;
		String s = "Renato";
		b = !s.equals("nato");
		System.out.print("Resultado " + b);*/
		
		Variant v1,v2;
		vm m = new vm();
		float f = 201;
		int i = 201;
		v1=new Variant(i);v2=new Variant(f);
		Variant v3 = m.bool('N', v1, v2);
		System.out.println(v3.getBoolean());
	} 
	
	public void init() {
		JFrame oFr = new JFrame();
		JPanel ms = new JPanel();
		JPanel op = new JPanel();
		op.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		ms.add(op);
		op.add(new Button("1"));
		
		JPanel op1 = new JPanel();
		ms.add(op1);
		op1.setLayout(new FlowLayout(FlowLayout.LEFT));
		Button ob = new Button("2");
		ob.setPreferredSize( new Dimension( 230, 20 ) ); 
		op1.add(ob);
		op1.add(new Button("3"));
		op1.add(new Button("4"));
		op1.add(new Button("5"));
		JPanel op2 = new JPanel();
		ms.add(op2);
		op2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		op2.add(new Button("6"));
		
		ms.setLayout(new GridLayout(2,1));
		
		oFr.add(ms);
		oFr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		oFr.pack();
		oFr.setVisible(true);
	}
		 

	
}
