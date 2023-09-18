package com.jsv.db.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class sqlbase {

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DBASE = "jdbc:sqlite:C:\\Renato\\programas\\rserver\\Rep\\db\\bsic.db";
	public int rc=0;
	public String errmsg="";
	private Connection conn;
	private boolean bcommit=false;
	private int rcsql=0;
	
	public void clearRcSql(){
		rcsql=0;
	}
	private void setRcSql(int i){
		if(rcsql==0){
			rcsql=i;
		}
	}
	public int getRcSql(){
		return(rcsql);
	}
	
	private int getConn(){
		rc=0;
		try{
		if(conn==null){
			Class.forName(DRIVER);
		    conn=DriverManager.getConnection(DBASE);
		} else if(conn.isClosed()){
	        conn=DriverManager.getConnection(DBASE);
		}
		//if(!conn.isValid(60)) { rc=4; errmsg="Coudn't open database"; return(rc);}
		conn.setAutoCommit(!bcommit);
		}catch(Exception ex){ rc=4; errmsg=ex.toString();}
		this.setRcSql(rc);
		return(rc);
	}
	
	public int commit(){
		rc=0;
		try{
			if(bcommit==true){
				conn.commit();
			}
			//conn.close();
		}catch(Exception ex){ rc=4; errmsg=ex.toString();}
		this.setRcSql(rc);
		return(rc);
	}
	
	public int rollback(){
		rc=0;
		try{
			conn.rollback();
			conn.close();
		}catch(Exception ex){ rc=4; errmsg=ex.toString();}
		this.setRcSql(rc);
		return(rc);
	}
	
	public int beginTransaction(){
		rc=0;
		try{
			this.bcommit=true;
			this.getConn();
			conn.rollback();
		}catch(Exception ex){ rc=4; errmsg=ex.toString();}
		return(rc);
	}
	
	public ResultSet select(String query, boolean pCommit){
		bcommit = pCommit;
		return(select(query));
	}
	
	public ResultSet select(String query){
		bcommit=false;
		ResultSet rs=null;
		Statement stat;
		if(this.getConn()!=0){ return(null); }
		try{
			stat = conn.createStatement();
		    rs=stat.executeQuery(query);
		    //conn.close();
	    }catch(Exception ex){ 
	    	if(rs!=null) 
	    		try{rs.close();}catch(Exception ex1){};
	    	if(conn!=null)
	    		try{conn.close();}catch(Exception ex1){};
		    rc=4; errmsg=ex.toString();
		}
	    this.setRcSql(rc);
		return(rs);
	}
	
	public void dispose(){
		try{
			conn.close();
		}catch(Exception ex){}
	}
	
	public int change(String command, boolean pCommit){
		bcommit = pCommit;
		return(this.change(command));
	}
	public int change(String command){
		Statement stat=null;
		
		if(this.getConn()!=0){ return(rc); }
		try{
			stat = conn.createStatement();
		    stat.executeUpdate(command);
	    }catch(Exception ex){
	    	if(conn!=null)
		    	try{conn.close();}catch(Exception ex1){};
	    	rc=4; errmsg=ex.toString();
	    }
	    this.setRcSql(rc);
		return(rc);
	}
}
