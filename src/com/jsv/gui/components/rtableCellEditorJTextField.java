package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class rtableCellEditorJTextField extends AbstractCellEditor implements TableCellEditor{

	JComponent component;
			
	public rtableCellEditorJTextField(boolean isEditable, Color pcolor){
		component = new JTextField();
		component.setBackground(pcolor);
		component.setEnabled(isEditable);
		component.setForeground(Color.red);
	}
		
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelect, int rowIndex, int vColIndex){
		((JTextField) component).setText((String)value);
		return(component);
	}
			
	public Object getCellEditorValue(){
		return((JTextField) component).getText();
	}
			
}

