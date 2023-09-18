package com.jsr.zdebug;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.jsr.process.zmsg;

public class zchannelMsg {
	
	private BlockingQueue <zmsg> queueA = new ArrayBlockingQueue<zmsg>(ISIZE);
	private BlockingQueue <zmsg> queueB = new ArrayBlockingQueue<zmsg>(ISIZE);
	private static final int ISIZE = 20;
	
	public boolean putA(zmsg pmsg){
		boolean b;
		b=this.putGen(queueA, pmsg);
		return(b);
	}
	
	public boolean putB(zmsg pmsg){
		boolean b;
		b=this.putGen(queueB, pmsg);
		return(b);
	}
	
	public zmsg getA(){
		zmsg omsg=this.getGen(queueA);
		return(omsg);
	}
	
	public zmsg getB(){
		zmsg omsg=this.getGen(queueB);
		return(omsg);
	}
	
	public int getIB(){
		int i=queueB.size();
		return(i);
	}
	
	private boolean putGen(BlockingQueue <zmsg>pq, zmsg pmsg){
		try{
			pq.put(pmsg);
			return(true);
		}catch(Exception ex){
			return(false);
		}
	}
	
	private zmsg getGen(BlockingQueue <zmsg>pq ){
		zmsg omsg=zmsg.createMsgEmpty();
		try{
			//while(omsg.rc!=zmsg.IEND){
				omsg=pq.take();
			//}
		}catch(Exception ex){
			omsg=zmsg.createMsg(zmsg.IERR, ex.getMessage());
		}
		return(omsg);
	}


}
