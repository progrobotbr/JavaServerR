package com.jsv.server.screen;

import java.net.URLDecoder;
import java.sql.ResultSet;

import com.jsv.db.base.sqlbase;

public class ProtocolV1 {
	
	private sqlbase mSqlBase;
	
	public void setSqlDB(sqlbase oSql){
		mSqlBase=oSql;
	}
	public String makeP1(String pTrz){
		String sSql,sPrg,sP1="";
		StringBuilder sb1=new StringBuilder();
		StringBuilder sb2=new StringBuilder();
		ResultSet rs1,rs2;
		sb1.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
		sb1.append("<md id='");
		sb1.append(pTrz);
		sb1.append("' trztp='0'><gt cm='gotoscr' vl='1000' rc='0' vs='1'/>");
		sSql = "select obccd, obctp from prtr where obfcd = '"+pTrz+"' and obftp = 'TRZZ'";
		rs1=mSqlBase.select(sSql);
		try{
			while(rs1.next()){
				sPrg=rs1.getString(1);
				sSql = "select lintx from prsc where objcd = '"+sPrg+"' and objtp='PCL1'";
				rs2=mSqlBase.select(sSql);
				while(rs2.next()){
					sb2.append(rs2.getString(1));
				}
			}
			sP1 = sb2.toString();
			sP1 = URLDecoder.decode(sP1, "iso-8859-1");
			
		}catch(Exception ex){}
		
		sb1.append(sP1);
		sb1.append("</md>");
		sP1=sb1.toString();
		return(sP1);
	}

	public String makeP2(String pTrz, String pScreen){
		String sSql,sPrg,sP2="";
		StringBuilder sb1=new StringBuilder();
		StringBuilder sb2=new StringBuilder();
		ResultSet rs1;
		
		sPrg = "SCN"+pScreen+pTrz;
		sb1.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
		sb1.append("<mp id='");
		sb1.append(pTrz);
		sb1.append("'><gt cm='"+pScreen+"' vl='"+pScreen+"' rc='0' vs='1'/>");
		sSql = "select lintx from prsc where objcd = '"+sPrg+"' and objtp='PCL2'";
		rs1=mSqlBase.select(sSql);
		try{
			while(rs1.next()){
				sb2.append(rs1.getString(1));
			}
			sP2 = sb2.toString();
			sP2 = URLDecoder.decode(sP2, "iso-8859-1");
			
		}catch(Exception ex){}
		
		sb1.append(sP2);
		sb1.append("</mp>");
		sP2=sb1.toString();
		return(sP2);
	}
}
