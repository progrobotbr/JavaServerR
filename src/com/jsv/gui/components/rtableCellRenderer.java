package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

public class rtableCellRenderer implements TableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelect, boolean hasFocus, int row, int column){
		
		JTextField editor = new JTextField();
		if(value!=null){
			editor.setText(value.toString());
		}
		editor.setBackground((row % 2 == 0) ?  Color.white : new Color(255,242,0)); //Color.cyan);
		editor.setBorder(null);
		return(editor);
			
	}
}
