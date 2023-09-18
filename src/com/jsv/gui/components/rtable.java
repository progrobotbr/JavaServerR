package com.jsv.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientHandleRequest;
import com.jsv.data.Variant;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.server.trz.base.TransactionProtocolJava;
import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.trz.local.component.ScreenPainter.attribComp;
import com.jsv.utils.Utils;

public class rtable {
	
	public static void ScreenPainterLoad(ScreenPainter pScrp, Node pNo){
		int i, ix, iy;
		String sid,sidt,stm,sx,sy,swdt, shdt, swd,shd,stl,svl,sro,scol="",sSep="",sdata[][];
		String h[];
		JTable tb=new JTable();
		JPanel jp = new JPanel();
		JPanel jp1 = new JPanel();
		jp.setLayout(new GridLayout(1,0));
		jp1.setLayout(new BorderLayout());
		
		sidt = Utils.getAttribute(pNo,"id");
		stm = Utils.getAttribute(pNo,"tm");
		sx  = Utils.getAttribute(pNo,"x");
		ix  = Integer.parseInt(sx);
		sy  = Utils.getAttribute(pNo,"y");
		iy  = Integer.parseInt(sy);
		swdt = Utils.getAttribute(pNo,"wd");
		shdt = Utils.getAttribute(pNo,"hd");
		stl = Utils.getAttribute(pNo,"tl");
		NodeList nlth = pNo.getChildNodes();
		Node nth = nlth.item(0);
		NodeList nltc = nth.getChildNodes();
		h=new String[nltc.getLength()];
		
		for(i=0;i<nltc.getLength();i++){
			Node ntc = nltc.item(i);
			sid=Utils.getAttribute(ntc,"id");
			svl=Utils.getAttribute(ntc,"vl");
			swd=Utils.getAttribute(ntc,"wd");
			sro=Utils.getAttribute(ntc,"ro");
			scol+=sSep+sid+","+svl+","+swd;
			sSep=";";
			h[i]=sid;
		}
		sdata=new String[Integer.parseInt(stl)][nltc.getLength()];
		DefaultTableModel dtm = new DefaultTableModel(sdata, h);
		tb.setModel(dtm);
		
		for(i=0;i<nltc.getLength();i++){
			Node ntc = nltc.item(i);
			sid=Utils.getAttribute(ntc,"id");
			swd=Utils.getAttribute(ntc,"wd");
			tb.getColumn(sid).setPreferredWidth(Integer.parseInt(swd));
		}
		tb.setName(sidt);
		tb.setRowHeight(20);
		tb.setRowSelectionAllowed(false);
		tb.setEnabled(false);
		tb.setAutoscrolls(true);
		//tb.setPreferredSize(new Dimension(Integer.parseInt(swd),Integer.parseInt(shd)));
		
		jp.setPreferredSize(new Dimension(Integer.parseInt(swdt),Integer.parseInt(shdt)));
		jp.setBackground(Color.LIGHT_GRAY);
		
		jp1.add(tb.getTableHeader(),BorderLayout.NORTH);
		jp1.add(tb,BorderLayout.CENTER);
		jp1.add(tb);
		

		jp.add(jp1);
		//jp.setSize(Integer.parseInt(swd),Integer.parseInt(shd));
	    jp.setName("TAB");
	    jp.repaint();
	    pScrp.addMove(tb);
	    pScrp.addNextTab(jp, 4, sidt, stl, swdt, shdt, scol, ix, iy);
	    //pScrp.addNext(jp);
	    pScrp.setVisible(true);
	}
	
	public static void ScreenPainterRedefine(attribComp atr){
		int iwd=0, ihd=0, t, i, iwidth=0;
		String sCol, sColTitle;
		JTable tb;
		JComponent jc2;
		try{
			iwd=Integer.parseInt(atr.wd.getText()); 
			ihd=Integer.parseInt(atr.hd.getText()); 
		}catch(Exception ex){ i=(int)atr.jc.getSize().getWidth(); }
		try{
			Vector vcol = new Vector();
			Vector vpro;
			int qlin = Integer.parseInt(atr.ln.getText());
			t = Integer.parseInt(atr.ln.getText());
			t = t*21+16;
			sCol = atr.cl.getText();
			jc2=(JComponent) atr.jc.getComponent(0);
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
			tb.setName(atr.id.toString());
			tb.setPreferredSize(new Dimension(iwd,ihd));
			
			
		}catch(Exception ex){ 
			t = (int)atr.jc.getSize().getHeight();
		}
		atr.jc.setSize(iwd,ihd);
		atr.jc.validate();
		atr.jc.repaint();
		
	}
	
	public static void ScreenPainterCreateProtocolP1P2(attribComp ac, StringBuilder sb, StringBuilder sb1){
		
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
		sp2 = "<tb id=\""+ac.id.getText().toUpperCase()+"\" sz=\"\" pg=\"\"><th ps=\"1\">"+sp2+"</th></tb>";
		
		sb.append(sp1);
		sb1.append(sp2);

	}

	public static void ScreenPainterCreate(ScreenPainter pScrp, String sId){
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
		pScrp.addMove(tb);
		jp.setLayout(new GridLayout(1,0));
		jp1.setLayout(new BorderLayout());
		
		jp.setPreferredSize(new Dimension(120,lin));
		jp.setBackground(Color.LIGHT_GRAY);
		
		jp1.add(tb.getTableHeader(),BorderLayout.NORTH);
		jp1.add(tb,BorderLayout.CENTER);
		jp1.add(tb);
		
		jp.add(jp1);
	    jp.setName(sId);
	    jp.repaint();
	    
	    attribComp atrib =pScrp.addNext(jp);
	    atrib.Type=4;
	    
	}
	
	public static void ServerGenerateProtocolResponse(TransactionProtocolJava ptpj, Node oNo1){
		int t, ilp, idx, iqtr;
		String sId, sVal, sSz;
		table tb;
		structure st;
		Variant v1, v2;
		Node oNo2, oNo3, oNo3Clone, oNo3Clone2;
		NodeList oNl3; 
		
		sId = Utils.getAttribute(oNo1, ComponentsDictionary.ID);
		v1 = ptpj.loadOB(sId);
		v2 = ptpj.loadOB("GD" + sId);
		if (v1 != null && v2 != null) {
			tb = v1.getTable();
			iqtr = tb.getRowCount();
			st = v2.getStucture();
			v1 = st.get("INDEX");
			idx = v1.getInt();
			v1 = st.get("LINEBYPAGE");
			ilp = v1.getInt();
			tb.setIndex(idx);
			sSz = ""+tb.getRowCount();
			Utils.setAttribute(oNo1, ComponentsDictionary.SZ, sSz);
			oNl3 = oNo1.getChildNodes();
			oNo2 = oNl3.item(0); // tr?
			oNo3Clone = oNo2.cloneNode(true);
			t = iqtr - idx;
			if(t>ilp){
				t=ilp;
			}
			st=tb.foreach();
			for(int y=0;y<t;y++){
				if(st==null){break;}
				if(y==0){
					oNo3Clone2 = oNo2;
				}else{
					oNo3Clone2 = oNo3Clone.cloneNode(true);
				}
				
				oNl3 = oNo3Clone2.getChildNodes();
				for (int z = 0; z < oNl3.getLength(); z++) {
					Node oNo4 = oNl3.item(z);
					sVal=Utils.getAttribute(oNo4,ComponentsDictionary.ID);
					v2=st.get(sVal);
					sVal = v2.getString();
					sVal = Utils.encode(sVal);
					Utils.setAttribute(oNo4, ComponentsDictionary.VL, sVal);
					sVal=""+(z+1);
					Utils.setAttribute(oNo4, ComponentsDictionary.PS, sVal);
				}
				oNo1.appendChild(oNo3Clone2);  
				st=tb.foreach();
			}
		}
	}

	public static void ServerParseProtocolRequest(Variant pv, Variant pst, Node pNode){
		boolean bappend=false, bchange=false;
		int i,t, index, ipg;
		String s, stcid, stcvl, spg;
		Variant vv;
		structure st, sh;
		table tb;
		Node no1,no2;
		NodeList nl1,nl2;
			
		tb=pv.tb;
		sh=pst.getStucture();
		index = sh.get("INDEX").i;
		tb.setIndex(index);
		//tb.clearTable();
		//st=tb.getHeader();
		spg=Utils.getAttribute(pNode,ComponentsDictionary.PG);
		if(spg!=null && spg.trim().length()>0){
			ipg=Integer.parseInt(spg);
			tb.setPage(ipg);
		}
		nl1=pNode.getChildNodes();
		for(i=0;i<nl1.getLength();i++){
			no1=nl1.item(i);
			s=no1.getNodeName();
			if(s.equals(ComponentsDictionary.TH)){
				nl2=no1.getChildNodes();
				//st=st.clone();
				//st.clear();
				if(bappend==false){
					st=tb.foreach();
				}else{
					st=null;
				}
				if(st==null){
					//continue; /////////////////////////
					st=tb.getHeader();
					st=st.clone();
					bappend=true;
				}
				bchange=false;
				for(t=0;t<nl2.getLength();t++){
					no2 = nl2.item(t);
					stcid=Utils.getAttribute(no2,ComponentsDictionary.ID);
					stcvl=Utils.getAttribute(no2,ComponentsDictionary.VL);
					try{
						stcvl=URLDecoder.decode(stcvl,"iso-8859-1");
						vv=st.get(stcid);
						vv.moveStringToInternal(stcvl);
						if(!stcvl.equals("")){
							bchange=true;
						}
					}catch(Exception ex){
						Utils.log("Server (ServerParseProtocolRequestex)"+ ex.toString());
					}
					
				}
				if(bchange==true){
					if(bappend==true){
						tb.append(st,null);
					}
				}else{
					if(bappend==false){
						tb.deleteIndice(tb.getIndice()-1);
					}
				}
			}
		}
	}
	
	public static void ClientParseProtocolCreateTable(ClientHandleRequest mCHR,HashMap pCollection,String pScreenId, JPanel pTela, Node pNode){
		
		//if(1!=2)return;
		int i,t, qtcol, qtlin, qtreg, qtlarg, ix=0, iy=0, ialt=0;
		String sX, sY, salt;
		//MetalScrollButton msb; //botões do scroll bar: cima, baixo
		JButton msb; //botões do scroll bar: cima, baixo
	    Node no1,no2;
		NodeList nl1,nl2;
		//JScrollPane jsp;
		JTable jt; //tabela
		JScrollPane pn; 
		JTableHeader hr;
		///JPanel pn;
	    JPanel pn2;
	    JPanel pn0;
	    JScrollBar jsb2, jsbb; 
		String s, sid, sqtcol, sqtlin, slarg, stotreg;
		String stcid, stcvl, stcwd;
		String smeta[][]=null; //metadados das colunas
		String sdata[][]=null; //matriz de strings em branco
		String shead[]=null;   //texto do título da tabela
		
		sX      =Utils.getAttribute(pNode,"x");
		sY      =Utils.getAttribute(pNode,"y");
		sid     =Utils.getAttribute(pNode,ComponentsDictionary.ID); //id da tabela
		sqtcol  =Utils.getAttribute(pNode,ComponentsDictionary.TM); //quantidade de colunas 
		sqtlin  =Utils.getAttribute(pNode,ComponentsDictionary.TL); //quantidade de linhas
		slarg   =Utils.getAttribute(pNode,ComponentsDictionary.WD); //largura da tabela
		salt    =Utils.getAttribute(pNode,ComponentsDictionary.HD); //largura da tabela
		stotreg =Utils.getAttribute(pNode,ComponentsDictionary.TT); //total de registros
		//sqtlin="4";
		if(stotreg==null){
			stotreg=sqtlin;
		}else if(stotreg.equals("")){
			stotreg=sqtlin;
		}
		
		qtcol=i=Integer.parseInt(sqtcol);
		qtlin=t=Integer.parseInt(sqtlin);
		qtreg=Integer.parseInt(stotreg);
		qtlarg=Integer.parseInt(slarg);
		ialt=Integer.parseInt(salt);
		
		smeta=new String[i][3]; //metadados
		sdata=new String[t][i]; //dados
		shead=new String[i];    //colunas
		
		for(int z=0; z<t;z++){
			for(int z1=0; z1<i;z1++){
				sdata[z][z1]=""; //new String();
			}
		}
		nl1=pNode.getChildNodes();
		for(i=0;i<nl1.getLength();i++){
			no1=nl1.item(i);
			s=no1.getNodeName();
			if(s.equals(ComponentsDictionary.TH)){
				nl2=no1.getChildNodes();
				for(t=0;t<nl2.getLength();t++){
					no2 = nl2.item(t);
					stcid=Utils.getAttribute(no2,ComponentsDictionary.ID);
					stcvl=Utils.getAttribute(no2,ComponentsDictionary.VL);
					stcwd=Utils.getAttribute(no2,ComponentsDictionary.WD);
					smeta[t][0]=stcid;
					smeta[t][1]=stcvl;
					if(stcwd.length()==0){ 
						stcwd="10";
					}
					smeta[t][2]=stcwd;
					shead[t]=stcvl;
				}
			}
		}
		
		jt = new JTable(sdata,shead);
		jt.setLayout(null);
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    jt.setRowSelectionAllowed(true);
	    jt.setAutoscrolls(true);
	    jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    jt.setRowHeight(20);
	    //jt.setBorder(BorderFactory.createLineBorder(new Color(255,150,255)));
	    jt.setDefaultRenderer(Object.class, new rtableCellRenderer());
	    
	    for(i=0;i<shead.length;i++){
	    	t=Integer.parseInt(smeta[i][2]);
	    	jt.getColumnModel().getColumn(i).setPreferredWidth(t);
	    	jt.getColumnModel().getColumn(i).setCellEditor(new rtableCellEditorJTextField(true, new Color(255,255,255)));
	    }
	    
	    //Scroll
	    jsb2 = new JScrollBar(JScrollBar.VERTICAL,0,1,0,qtreg/qtlin);
	    Component cp[]=jsb2.getComponents();
	    for(i=0;i<cp.length;i++){
	    	//msb=(MetalScrollButton)cp[i];
	    	msb=(JButton)cp[i];
	    	if(i==0) {
	    		s=ComponentsDictionary.SCR_ + pScreenId + "_" + ComponentsDictionary.TAB_BAIX_ + sid;
	    		msb.setActionCommand(s);
	    	}else{
	    		s=ComponentsDictionary.SCR_ + pScreenId + "_" + ComponentsDictionary.TAB_CIMA_ + sid;
	    		msb.setActionCommand(s); 
	    	}
	    	//msb.addActionListener(mCHR);
	    }
	    
	    jsb2.setName(ComponentsDictionary.SCR_ + pScreenId + "_" + ComponentsDictionary.TAB_PAGE_ + sid);
	    jsb2.addAdjustmentListener(mCHR);
	    
	    pn = new JScrollPane(jt,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    pn.setLayout(new ScrollPaneLayout());
	    
	    pn2 = new JPanel();
		pn2.setLayout(new BorderLayout());
	    
	    //i=qtreg/qtlin;
	    
	    hr = jt.getTableHeader();
	    pn2.add(hr,BorderLayout.NORTH);
	    pn2.add(pn,BorderLayout.CENTER);
	    
	    //i=(qtlin+1)*20;
	  	
	    ix=Integer.parseInt(sX);
    	iy=Integer.parseInt(sY);
	    pn.setBounds(ix,iy,qtlarg+200,ialt);
	    pn2.setBounds(ix,iy,qtlarg+200,ialt);
	    jt.setBounds(ix,iy,qtlarg+200,ialt);
	    
	    pn0 = new JPanel();
	    pn0.setLayout(new BorderLayout());
	    pn0.add(pn2, BorderLayout.CENTER);
	    pn0.add(jsb2, BorderLayout.EAST);
	    pn0.setBounds(ix,iy,qtlarg,ialt);
	    pn0.setBorder(BorderFactory.createLineBorder(new Color(255,150,255)));
	    
	    //Adiciona na tela
	    pTela.add(pn0);
	    
	    pCollection.put(ComponentsDictionary.TAB_ + sid, jt);
	    pCollection.put(ComponentsDictionary.TAM_ + sid, smeta);
	    pCollection.put(ComponentsDictionary.TAS_ + sid, jsb2);
	    
	    rgeral.setTam(pTela, ix+qtlarg, iy+ialt);
		
	}
	
	public static void ClientParseProtocolResponse(HashMap pCollection,  Node pNode){
		int i,t,i2,i3,t2,tam, isz=0;
		String s,sid, sid2, ssz, sidjb;
		JTable jt;
		JScrollBar jsb;
		TableModel tm;
		Node oNo2, oNo3;
		NodeList oNl2, oNl3, oNl4;
		sid2=sid=Utils.getAttribute(pNode,ComponentsDictionary.ID);
		sid=ComponentsDictionary.TAB_ + sid;
		ssz=Utils.getAttribute(pNode,ComponentsDictionary.SZ);
		if(ssz!=null && ssz.trim().length()>0){
			isz=Integer.parseInt(ssz);
		}
		jt = (JTable) pCollection.get(sid);
		
		/*
		 * Scroll Início
		 */
		jsb = (JScrollBar) pCollection.get(ComponentsDictionary.TAS_+sid2);
		//Tem que tirar o nome, pois quando houver serMaximum ocorre disparo de evento ClientHandleRequest
		sidjb = jsb.getName();
		jsb.setName("");
		i2 = jt.getRowCount();
		if(i2!=0){
			i2 =  (isz % i2 ) > 0 ? ( isz / i2 ) + 1 : isz / i2 ;
	    }else{
	    }
		
		try{
			jsb.setMaximum(i2);
		}catch(Exception ex){
			Utils.log("Client: rtable ClientParseProtocolResponse " + ex.toString());
		}
		jsb.setName(sidjb); //Colocar o nome de volta
		jsb.repaint();
		
		/*
		 * Scroll Fim
		 */
		tm=jt.getModel();
		tam=tm.getRowCount();
		oNl2 = pNode.getChildNodes();
		t=oNl2.getLength();
		for(i=0;i<t;i++){
			oNo2 = oNl2.item(i); // tr?
			oNl3 = oNo2.getChildNodes();
			t2=oNl3.getLength();
			for (i2=0; i2 < t2; i2++) {
				oNo3=oNl3.item(i2);
				s=Utils.getAttribute(oNo3,ComponentsDictionary.VL);
				s=Utils.decode(s);
				tm.setValueAt(s,i,i2);
			}
		}
		if(t<tam){
			t2=tm.getColumnCount();
			for(;i<tam;i++){
				for(i2=0;i2<t2;i2++){
					tm.setValueAt("",i,i2);
				}
			}
		}
	}

	public static String ClientGenerateProtocolRequest(JTable tb, JScrollBar pjsb, String pMeta[][], String pIdObj){
		int i,t,c,l, ip;
		String s, sid;
		StringBuilder sb=new StringBuilder();
		if(pMeta==null){
			return("");
		}
		ip=pjsb.getValue();
		sb.append("<tb id=\""+pIdObj+"\" pg=\""+ ip + "\">");
		TableModel tm=tb.getModel();
		c=tm.getColumnCount();
		l=tm.getRowCount();
		for(i=0;i<l;i++){
			sb.append("<th ps=\""+i+"\">");
			for(t=0;t<c;t++){
				try{
					s=(String)tm.getValueAt(i, t);
					s=URLEncoder.encode(s,"iso-8859-1");
					tb.getCellEditor(i,t).stopCellEditing();
				}catch(Exception ex){
					s="";
				}
				sid=pMeta[t][0];
				sb.append("<tc ps=\""+t+"\" id=\""+ sid + "\" vl=\""+s+"\"/>");
			}
			sb.append("</th>");
		}
		//if(l>0){
			//tb.getCellEditor().stopCellEditing();
		//}
		sb.append("</tb>");
		Utils.log(sb.toString());
		return(sb.toString());
	}
}
