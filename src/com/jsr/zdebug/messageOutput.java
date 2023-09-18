/*
 * @(#)MessageOutput.java	1.7 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.jsr.zdebug;

import java.util.*;
import java.text.MessageFormat;

import com.jsr.process.zmsg;
import com.jsr.server.icommands;
/**
 * Internationalization (i18n) convenience methods for jdb.
 *
 * All program output should flow through these methods, and this is
 * the only class that should be printing directly or otherwise
 * accessing System.[out,err].
 *
 * @version     @(#) MessageOutput.java 1.7 03/12/19 10:20:22
 * @bug 4348376
 * @author Tim Bell
 */
public class messageOutput {
    /** 
     * The resource bundle containing localizable message content.
     * This is loaded by TTY.main() at start-up
     */
    static ResourceBundle textResources;
    private Environment  Env;
    private zchannelMsg  zchannel;
    private StringBuffer msb = new StringBuffer();
    
    public void putChannel(zchannelMsg pch){
    	this.zchannel = pch;
    }
    
    public void putEnvironment(Environment pEnv){
    	this.Env = pEnv;
    }
    
    /** Our message formatter.  Allocated once, used many times */
    private static MessageFormat messageFormat;
    
     /**
     * Fatal shutdown notification.  This is sent to System.err
     * instead of System.out
     */
    void fatalError(String messageKey) {
        System.err.println();
        System.err.println(format("Fatal error"));
        System.err.println(format(messageKey));
        Env.shutdown();
    }

    /**
     * "Format" a string by doing a simple key lookup.
     */
    public static String format(String key) {
        //return (textResources.getString(key));
    	return (key);
    }    

    /**
     * Fetch and format a message with one string argument.
     * This is the most common usage.
     */
    public static String format(String key, String argument) {
        return format(key, new Object [] {argument});
    }

    /**
     * Fetch a string by key lookup and format in the arguments.
     */
    public static synchronized String format(String key, Object [] arguments) {
        if (messageFormat == null) {
            messageFormat = new MessageFormat (textResources.getString(key));
        } else {
            messageFormat.applyPattern (textResources.getString(key));
        }
        return (messageFormat.format (arguments));
    }

    /**
     * Print directly to System.out.  
     * Every rule has a few exceptions.
     * The exceptions to "must use the MessageOutput formatters" are:
     *     VMConnection.dumpStream()
     *     TTY.monitorCommand()
     *     TTY.TTY() (for the '!!' command only)
     *     Commands.java (multiple locations)
     * These are the only sites that should be calling this
     * method.
     */
    public void printDirectln(String line) {
        //System.out.println(line);
    	msb.append(line);
    }
    public void printDirect(String line) {
        //System.out.print(line);
    	msb.append(line);
    }
    public void printDirect(char c) {
        //System.out.print(c);
    	char c1[] = { c };
    	msb.append(new String( c1 ));
    }

    /**
     * Print a newline.
     * Use this instead of '\n'
     */
    public void println() {
        //System.out.println();
    	msb.append("\n");
    }

    /**
     * Format and print a simple string.
     */
    public void print(String key) {
        //System.out.print(format(key));
    	msb.append(format(key));
    }
    /**
     * Format and print a simple string.
     */
    public void println(String key) {
        //System.out.println(format(key));
    	msb.append(format(key)+"\n");
    	
    }


    /**
     * Fetch, format and print a message with one string argument.
     * This is the most common usage.
     */
    public void print(String key, String argument) {
        //System.out.print(format(key, argument));
    	msb.append(format(key, argument));
    }
    public void println(String key, String argument) {
        //System.out.println(format(key, argument));
    	msb.append(format(key, argument)+"\n");
    }

    /**
     * Fetch, format and print a message with an arbitrary
     * number of message arguments.
     */
    public void println(String key, Object [] arguments) {
        //System.out.println(format(key, arguments));
    	msb.append(format(key, arguments));
    }

    /**
     * Print a newline, followed by the string.
     */
    public void lnprint(String key) {
        //System.out.println();
        //System.out.print(textResources.getString(key));
    	msb.append("\n");
    	msb.append(key);
    	//msb.append(textResources.getString(key));
    }

    public void lnprint(String key, String argument) {
        //System.out.println();
        //System.out.print(format(key, argument));
    	msb.append("\n");
    	msb.append(format(key, argument));
    	
    }

    public void lnprint(String key, Object [] arguments) {
        //System.out.println();
        //System.out.print(format(key, arguments));
    	msb.append("\n");
    	msb.append(format(key, arguments));
    }

    /**
     * Print an exception message with a stack trace.
     */
    public void printException(String key, Exception e) {
        if (key != null) {
            try {
                println(key);
            } catch (MissingResourceException mex) {
                printDirectln(key);
            }   
        }
        System.out.flush();
        e.printStackTrace();
    }

    public void printPrompt() {
    	String s;
    	zmsg omsg;
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            //System.out.print
        	//msb.append(format("jdb prompt with no current thread"));
        } else {
            //System.out.print
        	//msb.append(format("jdb prompt thread name and current stack frame",
              //                new Object [] {
                //              threadInfo.getThread().name(),
                  //            new Integer (threadInfo.getCurrentFrameIndex() + 1)}));
        }
        
        s = msb.toString();
        if(s.trim().equals(">")){
        	return;
        }
        if(s.trim().length()==0){
        	return;
        }
        //s=this.sFirst + "D" +s.replaceAll("\n", " ")+"\n";
        s=s.replaceAll("\n", " ");
        omsg = zmsg.createMsgSuccess(s);omsg.dir=icommands.IORIGDEB;
        msb = new StringBuffer();
        this.zchannel.putB(omsg);
        
        //System.out.flush();
    }
}
