/*
 * @(#)Commands.java	1.85 04/02/09
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Copyright (c) 1997-2001 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package com.jsr.zdebug;

import com.jsr.util._;
import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.request.*;
import com.sun.tools.example.debug.expr.ExpressionParser;
import com.sun.tools.example.debug.expr.ParseException;

import java.text.*;
import java.util.*;
import java.io.*;

class Commands {

	Environment Env;
	messageOutput MessageOutput;
	
    abstract class AsyncExecution {
	abstract void action();
	Environment Env;
	messageOutput MessageOutput;

	
	AsyncExecution(Environment pEnv, messageOutput pmsg) {
		    this.Env = pEnv;
		    this.MessageOutput = pmsg;
            execute();
	}

	
	void execute() {
            /*
             * Save current thread and stack frame. (BugId 4296031)
             */
            final ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
            final int stackFrame = threadInfo == null? 0 : threadInfo.getCurrentFrameIndex();
            Thread thread = new Thread("asynchronous jdb command") {
                    public void run() {
                        try {
                            action();
                        } catch (UnsupportedOperationException uoe) {
                            //(BugId 4453329)
                            MessageOutput.println("Operation is not supported on the target VM");
                        } catch (Exception e) {
                            MessageOutput.println("Internal exception during operation:",
                                                  e.getMessage());
                        } finally {
                            /*
                             * This was an asynchronous command.  Events may have been
                             * processed while it was running.  Restore the thread and
                             * stack frame the user was looking at.  (BugId 4296031)
                             */
                            if (threadInfo != null) {
                                ThreadInfo.setCurrentThreadInfo(threadInfo);
                                try {
                                    threadInfo.setCurrentFrameIndex(stackFrame);
                                } catch (IncompatibleThreadStateException e) {
                                    MessageOutput.println("Current thread isnt suspended.");
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    MessageOutput.println("Requested stack frame is no longer active:",
                                                          new Object []{new Integer(stackFrame)});
                                }
                            }
                            MessageOutput.printPrompt();
                        }
                    }
                };
            thread.start();
	}
    }

    Commands(Environment pEnv, messageOutput pmsgout) {
    	this.Env = pEnv;
    	this.MessageOutput = pmsgout;
    }

    private Value evaluate(String expr) {
        Value result = null;
        ExpressionParser.GetFrame frameGetter = null;
        try {
            final ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
            if ((threadInfo != null) && (threadInfo.getCurrentFrame() != null)) {
                frameGetter = new ExpressionParser.GetFrame() {
                        public StackFrame get() throws IncompatibleThreadStateException {
                            return threadInfo.getCurrentFrame();
                        }
                    };
            }
            result = ExpressionParser.evaluate(expr, Env.vm(), frameGetter);
        } catch (InvocationException ie) {
            MessageOutput.println("Exception in expression:",
                                  ie.exception().referenceType().name());
        } catch (Exception ex) {
            String exMessage = ex.getMessage();
            if (exMessage == null) {
                MessageOutput.printException(exMessage, ex);
            } else {
                String s;
                try {
                    s = MessageOutput.format(exMessage);
                } catch (MissingResourceException mex) {
                    s = ex.toString();
                }
                MessageOutput.printDirectln(s);// Special case: use printDirectln()
            }
        }
        return result;
    }

    private String getStringValue() {
         Value val = null;
         String valStr = null;
         try {
              val = ExpressionParser.getMassagedValue();
              valStr = val.toString();
         } catch (ParseException e) { 
              String msg = e.getMessage();
              if (msg == null) {
                  MessageOutput.printException(msg, e);
              } else {
                  String s;
                  try {
                      s = MessageOutput.format(msg);
                  } catch (MissingResourceException mex) {
                      s = e.toString();
                  }
                  MessageOutput.printDirectln(s);    
              }
         }
         return valStr;
    }

    private ThreadInfo doGetThread(String idToken) {
        ThreadInfo threadInfo = ThreadInfo.getThreadInfo(this.Env, this.MessageOutput,idToken);
        if (threadInfo == null) {
            MessageOutput.println("is not a valid thread id", idToken);
        }
        return threadInfo;
    }

    String typedName(Method method) {
        StringBuffer buf = new StringBuffer();
        buf.append(method.name());
        buf.append("(");

        List args = method.argumentTypeNames();
        int lastParam = args.size() - 1;
        // output param types except for the last
        for (int ii = 0; ii < lastParam; ii++) {
            buf.append((String)args.get(ii));
            buf.append(", ");
        }
        if (lastParam >= 0) {
            // output the last param
            String lastStr = (String)args.get(lastParam);
            if (method.isVarArgs()) {
                // lastParam is an array.  Replace the [] with ...
                buf.append(lastStr.substring(0, lastStr.length() - 2));
                buf.append("...");
            } else {
                buf.append(lastStr);
            }
        }
        buf.append(")");
        return buf.toString();
    }   
                            
    void commandConnectors(VirtualMachineManager vmm) {
        Iterator iter = vmm.allConnectors().iterator();
        if (iter.hasNext()) {
            MessageOutput.println("Connectors available");
        }
        while (iter.hasNext()) {
            Connector cc = (Connector)iter.next();
            String transportName =
                cc.transport() == null ? "null" : cc.transport().name();
            MessageOutput.println();
            MessageOutput.println("Connector and Transport name",
                                  new Object [] {cc.name(), transportName});
            MessageOutput.println("Connector description", cc.description());

            Iterator argIter = cc.defaultArguments().values().iterator();
            if (argIter.hasNext()) {
                while (argIter.hasNext()) {
                    Connector.Argument aa = (Connector.Argument)argIter.next();
                    MessageOutput.println();
                    
                    boolean requiredArgument = aa.mustSpecify();
                    if (aa.value() == null || aa.value() == "") {
                        //no current value and no default.
                        MessageOutput.println(requiredArgument ?
                                              "Connector required argument nodefault" :
                                              "Connector argument nodefault", aa.name());
                    } else {
                        MessageOutput.println(requiredArgument ?
                                              "Connector required argument default" :
                                              "Connector argument default",
                                              new Object [] {aa.name(), aa.value()});
                    } 
                    MessageOutput.println("Connector description", aa.description());
                    
                }
            }
        }
        
    }

    void commandClasses() {
        List list = Env.vm().allClasses();

        StringBuffer classList = new StringBuffer();
        for (int i = 0 ; i < list.size() ; i++) {
            ReferenceType refType = (ReferenceType)list.get(i);
            classList.append(refType.name());
            classList.append("\n");
        }
        MessageOutput.print("** classes list **", classList.toString());
    }

    void commandClass(StringTokenizer t) {
        List list = Env.vm().allClasses();

        if (!t.hasMoreTokens()) {
            MessageOutput.println("No class specified.");
            return;
        }

        String idClass = t.nextToken();
        boolean showAll = false;

        if (t.hasMoreTokens()) {
            if (t.nextToken().toLowerCase().equals("all")) {
                showAll = true;
            } else {
                MessageOutput.println("Invalid option on class command");
                return;
            }
        }
        ReferenceType type = Env.getReferenceTypeFromToken(idClass);
        if (type == null) {
            MessageOutput.println("is not a valid id or class name", idClass);
            return;
        }
        if (type instanceof ClassType) {
            ClassType clazz = (ClassType)type;
            MessageOutput.println("Class:", clazz.name());

            ClassType superclass = clazz.superclass();
            while (superclass != null) {
                MessageOutput.println("extends:", superclass.name());
                superclass = showAll ? superclass.superclass() : null;
            }

            List interfaces = showAll ? clazz.allInterfaces() 
                                      : clazz.interfaces();
            Iterator iter = interfaces.iterator();
            while (iter.hasNext()) {
                InterfaceType interfaze = (InterfaceType)iter.next();
                MessageOutput.println("implements:", interfaze.name());
            }

            List subs = clazz.subclasses();
            iter = subs.iterator();
            while (iter.hasNext()) {
                ClassType sub = (ClassType)iter.next();
                MessageOutput.println("subclass:", sub.name());
            }
            List nested = clazz.nestedTypes();
            iter = nested.iterator();
            while (iter.hasNext()) {
                ReferenceType nest = (ReferenceType)iter.next();
                MessageOutput.println("nested:", nest.name());
            }
        } else if (type instanceof InterfaceType) {
            InterfaceType interfaze = (InterfaceType)type;
            MessageOutput.println("Interface:", interfaze.name());
            List supers = interfaze.superinterfaces();
            Iterator iter = supers.iterator();
            while (iter.hasNext()) {
                InterfaceType superinterface = (InterfaceType)iter.next();
                MessageOutput.println("extends:", superinterface.name());
            }
            List subs = interfaze.subinterfaces();
            iter = subs.iterator();
            while (iter.hasNext()) {
                InterfaceType sub = (InterfaceType)iter.next();
                MessageOutput.println("subinterface:", sub.name());
            }
            List implementors = interfaze.implementors();
            iter = implementors.iterator();
            while (iter.hasNext()) {
                ClassType implementor = (ClassType)iter.next();
                MessageOutput.println("implementor:", implementor.name());
            }
            List nested = interfaze.nestedTypes();
            iter = nested.iterator();
            while (iter.hasNext()) {
                ReferenceType nest = (ReferenceType)iter.next();
                MessageOutput.println("nested:", nest.name());
            }
        } else {  // array type
            ArrayType array = (ArrayType)type;
            MessageOutput.println("Array:", array.name());
        }
    }

    void commandMethods(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No class specified.");
            return;
        }

        String idClass = t.nextToken();
        ReferenceType cls = Env.getReferenceTypeFromToken(idClass);
        if (cls != null) {
            List methods = cls.allMethods();
            StringBuffer methodsList = new StringBuffer();
            for (int i = 0; i < methods.size(); i++) {
                Method method = (Method)methods.get(i);
                methodsList.append(method.declaringType().name());
                methodsList.append(" ");
                methodsList.append(typedName(method));
                methodsList.append('\n');
            }
            MessageOutput.print("** methods list **", methodsList.toString());
        } else {
            MessageOutput.println("is not a valid id or class name", idClass);
        }
    }

    void commandFields(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No class specified.");
            return;
        }

        String idClass = t.nextToken();
        ReferenceType cls = Env.getReferenceTypeFromToken(idClass);
        if (cls != null) {
            List fields = cls.allFields();
            List visible = cls.visibleFields();
            StringBuffer fieldsList = new StringBuffer();
            for (int i = 0; i < fields.size(); i++) {
                Field field = (Field)fields.get(i);
                String s;
                if (!visible.contains(field)) {
                    s = MessageOutput.format("list field typename and name hidden",
                                             new Object [] {field.typeName(),
                                                            field.name()});
                } else if (!field.declaringType().equals(cls)) {
                    s = MessageOutput.format("list field typename and name inherited",
                                             new Object [] {field.typeName(),
                                                            field.name(),
                                                            field.declaringType().name()});
                } else {
                    s = MessageOutput.format("list field typename and name",
                                             new Object [] {field.typeName(),
                                                            field.name()});
                }
                fieldsList.append(s);
            }
            MessageOutput.print("** fields list **", fieldsList.toString());
        } else {
            MessageOutput.println("is not a valid id or class name", idClass);
        }
    }

    private void printThreadGroup(ThreadGroupReference tg) {
        ThreadIterator threadIter = new ThreadIterator(Env, tg);

        MessageOutput.println("Thread Group:", tg.name());
        int maxIdLength = 0;
        int maxNameLength = 0;
        while (threadIter.hasNext()) {
            ThreadReference thr = (ThreadReference)threadIter.next();
            maxIdLength = Math.max(maxIdLength,Env.description(thr).length());
            maxNameLength = Math.max(maxNameLength,thr.name().length());
        }

        threadIter = new ThreadIterator(Env, tg);
        while (threadIter.hasNext()) {
            ThreadReference thr = (ThreadReference)threadIter.next();
	    if (thr.threadGroup() == null) {
                continue; 
            }
            // Note any thread group changes
            if (!thr.threadGroup().equals(tg)) {
                tg = thr.threadGroup();
                MessageOutput.println("Thread Group:", tg.name());
            }

            /*
             * Do a bit of filling with whitespace to get thread ID
             * and thread names to line up in the listing, and also
             * allow for proper localization.  This also works for
             * very long thread names, at the possible cost of lines
             * being wrapped by the display device.
             */
            StringBuffer idBuffer = new StringBuffer(Env.description(thr));
            for (int i = idBuffer.length(); i < maxIdLength; i++) {
                idBuffer.append(" ");
            }
            StringBuffer nameBuffer = new StringBuffer(thr.name());
            for (int i = nameBuffer.length(); i < maxNameLength; i++) {
                nameBuffer.append(" ");
            }
            
            /*
             * Select the output format to use based on thread status
             * and breakpoint.
             */
            String statusFormat;
            switch (thr.status()) {
            case ThreadReference.THREAD_STATUS_UNKNOWN:
                if (thr.isAtBreakpoint()) {
                    statusFormat = "Thread description name unknownStatus BP";
                } else {
                    statusFormat = "Thread description name unknownStatus";
                }
                break;
            case ThreadReference.THREAD_STATUS_ZOMBIE:
                if (thr.isAtBreakpoint()) {
                    statusFormat = "Thread description name zombieStatus BP";
                } else {
                    statusFormat = "Thread description name zombieStatus";
                }
                break;
            case ThreadReference.THREAD_STATUS_RUNNING:
                if (thr.isAtBreakpoint()) {
                    statusFormat = "Thread description name runningStatus BP";
                } else {
                    statusFormat = "Thread description name runningStatus";
                }
                break;
            case ThreadReference.THREAD_STATUS_SLEEPING:
                if (thr.isAtBreakpoint()) {
                    statusFormat = "Thread description name sleepingStatus BP";
                } else {
                    statusFormat = "Thread description name sleepingStatus";
                }
                break;
            case ThreadReference.THREAD_STATUS_MONITOR:
                if (thr.isAtBreakpoint()) {
                    statusFormat = "Thread description name waitingStatus BP";
                } else {
                    statusFormat = "Thread description name waitingStatus";
                }
                break;
            case ThreadReference.THREAD_STATUS_WAIT:
                if (thr.isAtBreakpoint()) {
                    statusFormat = "Thread description name condWaitstatus BP";
                } else {
                    statusFormat = "Thread description name condWaitstatus";
                }
                break;
            default:
                throw new InternalError(MessageOutput.format("Invalid thread status."));
            }
            MessageOutput.println(statusFormat,
                                  new Object [] {idBuffer.toString(),
                                                 nameBuffer.toString()});
        }
    }

    void commandThreads(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            printThreadGroup(ThreadInfo.group(Env));
            return;
        }
        String name = t.nextToken();
        ThreadGroupReference tg = ThreadGroupIterator.find(Env,name);
        if (tg == null) {
            MessageOutput.println("is not a valid threadgroup name", name);
        } else {
            printThreadGroup(tg);
        }
    }

    void commandThreadGroups() {
        ThreadGroupIterator it = new ThreadGroupIterator(Env);
        int cnt = 0;
        while (it.hasNext()) {
            ThreadGroupReference tg = it.nextThreadGroup();
            ++cnt;
            MessageOutput.println("thread group number description name",
                                  new Object [] { new Integer (cnt),
            		Env.description(tg),
                                                  tg.name()});
        }
    }
    
    void commandThread(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Thread number not specified.");
            return;
        }
        ThreadInfo threadInfo = doGetThread(t.nextToken());
        if (threadInfo != null) {
            ThreadInfo.setCurrentThreadInfo(threadInfo);
        }
    }
    
    void commandThreadGroup(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Threadgroup name not specified.");
            return;
        }
        String name = t.nextToken();
        ThreadGroupReference tg = ThreadGroupIterator.find(Env, name);
        if (tg == null) {
            MessageOutput.println("is not a valid threadgroup name", name);
        } else {
            ThreadInfo.setThreadGroup(tg);
        }
    }
    
    void commandRun(StringTokenizer t) {
        /*
         * The 'run' command makes little sense in a 
         * that doesn't support restarts or multiple VMs. However,
         * this is an attempt to emulate the behavior of the old
         * JDB as much as possible. For new users and implementations
         * it is much more straightforward to launch immedidately
         * with the -launch option.
         */
        VMConnection connection = Env.connection();
        if (!connection.isLaunch()) {
            if (!t.hasMoreTokens()) {
                commandCont();
            } else {
                MessageOutput.println("run <args> command is valid only with launched VMs");
            }
            return;
        } 
        if (connection.isOpen()) {
            MessageOutput.println("VM already running. use cont to continue after events.");
            return;
        }

        /*
         * Set the main class and any arguments. Note that this will work
         * only with the standard launcher, "com.sun.jdi.CommandLineLauncher"
         */
        String args;
        if (t.hasMoreTokens()) {
            args = t.nextToken("");
            boolean argsSet = connection.setConnectorArg("main", args);
            if (!argsSet) {
                MessageOutput.println("Unable to set main class and arguments");
                return;
            } 
        } else {
            args = connection.connectorArg("main");
            if (args.length() == 0) {
                MessageOutput.println("Main class and arguments must be specified");
                return;
            }
        }
        MessageOutput.println("run", args);

        /*
         * Launch the VM.
         */
        connection.open();
        
    }

    void commandLoad(StringTokenizer t) {
        MessageOutput.println("The load command is no longer supported.");
    }

    private List allThreads(ThreadGroupReference group) {
        List list = new ArrayList();
        list.addAll(group.threads());
        Iterator iter = group.threadGroups().iterator();
        while (iter.hasNext()) {
            ThreadGroupReference child = (ThreadGroupReference)iter.next();
            list.addAll(allThreads(child));
        }
        return list;
    }

    void commandSuspend(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
        	Env.vm().suspend();
            MessageOutput.println("All threads suspended.");
        } else {
            while (t.hasMoreTokens()) {
                ThreadInfo threadInfo = doGetThread(t.nextToken());
                if (threadInfo != null) {
                    threadInfo.getThread().suspend();
                }                
            }
        }
    }

    void commandResume(StringTokenizer t) {
         if (!t.hasMoreTokens()) {
             ThreadInfo.invalidateAll(this.Env, this.MessageOutput);
             Env.vm().resume();
             MessageOutput.println("All threads resumed.");
         } else {
             while (t.hasMoreTokens()) {
                ThreadInfo threadInfo = doGetThread(t.nextToken());
                if (threadInfo != null) {
                    threadInfo.invalidate();
                    threadInfo.getThread().resume();
                }
            }
        }
    }

    void commandCont() {
        if (ThreadInfo.getCurrentThreadInfo() == null) {
            MessageOutput.println("Nothing suspended.");
            _.lg("nothing sus");
            return;
        }
        ThreadInfo.invalidateAll(this.Env, this.MessageOutput);
        _.lg("nothing res");
        Env.vm().resume();
    }

    void clearPreviousStep(ThreadReference thread) {
        /*
         * A previous step may not have completed on this thread; 
         * if so, it gets removed here. 
         */
         EventRequestManager mgr = Env.vm().eventRequestManager();
         List requests = mgr.stepRequests();
         Iterator iter = requests.iterator();
         while (iter.hasNext()) {
             StepRequest request = (StepRequest)iter.next();
             if (request.thread().equals(thread)) {
                 mgr.deleteEventRequest(request);
                 break;
             }
         }
    }
    /* step
     *
     */
    void commandStep(StringTokenizer t) {
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("D010_Nothing suspended.");
            return;
        }
        int depth;
        if (t.hasMoreTokens() &&
                  t.nextToken().toLowerCase().equals("up")) {
            depth = StepRequest.STEP_OUT;
        } else {
            depth = StepRequest.STEP_INTO;
        }

        clearPreviousStep(threadInfo.getThread());
        EventRequestManager reqMgr = Env.vm().eventRequestManager();
        StepRequest request = reqMgr.createStepRequest(threadInfo.getThread(),
                                                       StepRequest.STEP_LINE, depth);
        if (depth == StepRequest.STEP_INTO) {
        	Env.addExcludes(request);
        }
        // We want just the next step event and no others
        request.addCountFilter(1);
        request.enable();
        ThreadInfo.invalidateAll(this.Env, this.MessageOutput);
        Env.vm().resume();
    }

    /* stepi
     * step instruction.
     */
    void commandStepi() {
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("Nothing suspended.");
            return;
        }
        clearPreviousStep(threadInfo.getThread());
        EventRequestManager reqMgr = Env.vm().eventRequestManager();
        StepRequest request = reqMgr.createStepRequest(threadInfo.getThread(),
                                                       StepRequest.STEP_MIN,
                                                       StepRequest.STEP_INTO);
        Env.addExcludes(request);
        // We want just the next step event and no others
        request.addCountFilter(1);
        request.enable();
        ThreadInfo.invalidateAll(this.Env, this.MessageOutput);
        Env.vm().resume();
    }

    void commandNext() {
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("Nothing suspended.");
            return;
        }
        clearPreviousStep(threadInfo.getThread());
        EventRequestManager reqMgr = Env.vm().eventRequestManager();
        StepRequest request = reqMgr.createStepRequest(threadInfo.getThread(),
                                                       StepRequest.STEP_LINE,
                                                       StepRequest.STEP_OVER);
        Env.addExcludes(request);
        // We want just the next step event and no others
        request.addCountFilter(1);
        request.enable();
        ThreadInfo.invalidateAll(this.Env, this.MessageOutput);
        Env.vm().resume();
    }

    void doKill(ThreadReference thread, StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No exception object specified.");
            return;
        }
        String expr = t.nextToken("");
        Value val = evaluate(expr);
        if ((val != null) && (val instanceof ObjectReference)) {
            try {
                thread.stop((ObjectReference)val);
                MessageOutput.println("killed", thread.toString());
            } catch (InvalidTypeException e) {
                MessageOutput.println("Invalid exception object");
            }
        } else {
            MessageOutput.println("Expression must evaluate to an object");
        }
    }

    void doKillThread(final ThreadReference threadToKill,
                      final StringTokenizer tokenizer) {
        new AsyncExecution(this.Env, this.MessageOutput) {
                void action() {
                    doKill(threadToKill, tokenizer);
                }
            }; 
    }

    void commandKill(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Usage: kill <thread id> <throwable>");
            return;
        }
        ThreadInfo threadInfo = doGetThread(t.nextToken());
        if (threadInfo != null) {
            MessageOutput.println("killing thread:", threadInfo.getThread().name());
            doKillThread(threadInfo.getThread(), t);
            return;
        }
    }

    void listCaughtExceptions() {
        boolean noExceptions = true;

        // Print a listing of the catch patterns currently in place
        Iterator iter = Env.specList.eventRequestSpecs().iterator();
        while (iter.hasNext()) {
            EventRequestSpec spec = (EventRequestSpec)iter.next();
            if (spec instanceof ExceptionSpec) {
                if (noExceptions) {
                    noExceptions = false;
                    MessageOutput.println("Exceptions caught:");
                }
                MessageOutput.println("tab", spec.toString());
            }
        }
        if (noExceptions) {
            MessageOutput.println("No exceptions caught.");
        }
    }

    private EventRequestSpec parseExceptionSpec(StringTokenizer t) {
        String notification = t.nextToken();
        boolean notifyCaught = false;
        boolean notifyUncaught = false;
        EventRequestSpec spec = null;
        String classPattern = null;
        
        if (notification.equals("uncaught")) {
            notifyCaught = false;
            notifyUncaught = true;
        } else if (notification.equals("caught")) {
            notifyCaught = true;
            notifyUncaught = false;
        } else if (notification.equals("all")) {
            notifyCaught = true;
            notifyUncaught = true;
        } else {
            /*
             * Handle the same as "all" for backward
             * compatibility with existing .jdbrc files.
             *
             * Insert an "all" and take the current token as the
             * intended classPattern
             *
             */
            notifyCaught = true;
            notifyUncaught = true;
            classPattern = notification;
        }
        if (classPattern == null && t.hasMoreTokens()) {
            classPattern = t.nextToken();
        }
        if ((classPattern != null) && (notifyCaught || notifyUncaught)) {
            try {
                spec = Env.specList.createExceptionCatch(classPattern,
                                                         notifyCaught,
                                                         notifyUncaught);
            } catch (ClassNotFoundException exc) {
                MessageOutput.println("is not a valid class name", classPattern);
            }
        }
        return spec;
    }

    void commandCatchException(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            listCaughtExceptions();
        } else { 
            EventRequestSpec spec = parseExceptionSpec(t);
            if (spec != null) {
                resolveNow(spec);
            } else {
                MessageOutput.println("Usage: catch exception");
            }
        }
    }
    
    void commandIgnoreException(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            listCaughtExceptions();
        } else { 
            EventRequestSpec spec = parseExceptionSpec(t);
            if (Env.specList.delete(spec)) {
                MessageOutput.println("Removed:", spec.toString());
            } else {
                if (spec != null) {
                    MessageOutput.println("Not found:", spec.toString());
                }
                MessageOutput.println("Usage: ignore exception");
            }
        }
    }
    
    void commandUp(StringTokenizer t) {
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("Current thread not set.");
            return;
        }

        int nLevels = 1;
        if (t.hasMoreTokens()) {
            String idToken = t.nextToken();
            int i;
            try {
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setParseIntegerOnly(true);
                Number n = nf.parse(idToken);
                i = n.intValue();
            } catch (java.text.ParseException jtpe) {
                i = 0;
            }
            if (i <= 0) {
                MessageOutput.println("Usage: up [n frames]");
                return;
            }
            nLevels = i;
        }

        try {
            threadInfo.up(nLevels);
        } catch (IncompatibleThreadStateException e) {
            MessageOutput.println("Current thread isnt suspended.");
        } catch (ArrayIndexOutOfBoundsException e) {
            MessageOutput.println("End of stack.");
        }
    }

    void commandDown(StringTokenizer t) {
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("Current thread not set.");
            return;
        }

        int nLevels = 1;
        if (t.hasMoreTokens()) {
            String idToken = t.nextToken();
            int i;
            try {
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setParseIntegerOnly(true);
                Number n = nf.parse(idToken);
                i = n.intValue();
            } catch (java.text.ParseException jtpe) {
                i = 0;
            }
            if (i <= 0) {
                MessageOutput.println("Usage: down [n frames]");
                return;
            }
            nLevels = i;
        }

        try {
            threadInfo.down(nLevels);
        } catch (IncompatibleThreadStateException e) {
            MessageOutput.println("Current thread isnt suspended.");
        } catch (ArrayIndexOutOfBoundsException e) {
            MessageOutput.println("End of stack.");
        }
    }

    private void dumpStack(ThreadInfo threadInfo, boolean showPC) {
        List stack = null;
        try {
            stack = threadInfo.getStack();
        } catch (IncompatibleThreadStateException e) {
            MessageOutput.println("Current thread isnt suspended.");
            return;
        }
        if (stack == null) {  
            MessageOutput.println("Thread is not running (no stack).");
        } else {
            int nFrames = stack.size();
            for (int i = threadInfo.getCurrentFrameIndex(); i < nFrames; i++) {
                StackFrame frame = (StackFrame)stack.get(i);
                dumpFrame (i, showPC, frame);
            }
        }
    }

    private void dumpFrame (int frameNumber, boolean showPC, StackFrame frame) {
        Location loc = frame.location();
        long pc = -1;
        if (showPC) {
            pc = loc.codeIndex();
        }
        Method meth = loc.method();

        long lineNumber = loc.lineNumber();
        String methodInfo = null;
        if (meth instanceof Method && ((Method)meth).isNative()) {
            methodInfo = MessageOutput.format("native method");
        } else if (lineNumber != -1) {
            try {
                methodInfo = loc.sourceName() +
                    MessageOutput.format("line number",
                                         new Object [] {new Long(lineNumber)});
            } catch (AbsentInformationException e) {
                methodInfo = MessageOutput.format("unknown");
            }
        }
        if (pc != -1) {
            MessageOutput.println("stack frame dump with pc",
                                  new Object [] {new Integer(frameNumber + 1),
                                                 meth.declaringType().name(),
                                                 meth.name(),
                                                 methodInfo,
                                                 new Long(pc)});
        } else {
            MessageOutput.println("stack frame dump",
                                  new Object [] {new Integer(frameNumber + 1),
                                                 meth.declaringType().name(),
                                                 meth.name(),
                                                 methodInfo});
        }
    }

    void commandWhere(StringTokenizer t, boolean showPC) {
        if (!t.hasMoreTokens()) {
            ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
            if (threadInfo == null) {
                MessageOutput.println("No thread specified.");
                return;
            }
            dumpStack(threadInfo, showPC);
        } else {
            String token = t.nextToken();
            if (token.toLowerCase().equals("all")) {
                Iterator iter = ThreadInfo.threads(this.Env, this.MessageOutput).iterator();
                while (iter.hasNext()) {
                    ThreadInfo threadInfo = (ThreadInfo)iter.next();
                    MessageOutput.println("Thread:",
                                          threadInfo.getThread().name());
                    dumpStack(threadInfo, showPC);
                }
            } else {
                ThreadInfo threadInfo = doGetThread(token);
                if (threadInfo != null) {
                    ThreadInfo.setCurrentThreadInfo(threadInfo);
                    dumpStack(threadInfo, showPC);
                }
            }
        }
    }

    void commandInterrupt(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
            if (threadInfo == null) {
                MessageOutput.println("No thread specified.");
                return;
            }
            threadInfo.getThread().interrupt();
        } else {
            ThreadInfo threadInfo = doGetThread(t.nextToken());
            if (threadInfo != null) {
                threadInfo.getThread().interrupt();
            }
        }
    }

    void commandMemory() {
        MessageOutput.println("The memory command is no longer supported.");
    }

    void commandGC() {
        MessageOutput.println("The gc command is no longer necessary.");
    }

    /*
     * The next two methods are used by this class and by EventHandler
     * to print consistent locations and error messages.
     */
    static String locationString(Location loc) {
        return messageOutput.format("locationString",
                                    new Object [] {loc.declaringType().name(),
                                                   loc.method().name(),
                                                   new Integer (loc.lineNumber()),
                                                   new Long (loc.codeIndex())});
    }

    void listBreakpoints() {
        boolean noBreakpoints = true;

        // Print set breakpoints
        Iterator iter = Env.specList.eventRequestSpecs().iterator();
        while (iter.hasNext()) {
            EventRequestSpec spec = (EventRequestSpec)iter.next();
            if (spec instanceof BreakpointSpec) {
                if (noBreakpoints) {
                    noBreakpoints = false;
                    MessageOutput.println("Breakpoints set:");
                }
                MessageOutput.println("tab", spec.toString());
            }
        }
        if (noBreakpoints) {
            MessageOutput.println("No breakpoints set.");
        }
    }


    private void printBreakpointCommandUsage(String atForm, String inForm) {
        MessageOutput.println("printbreakpointcommandusage",
                              new Object [] {atForm, inForm});
    }

    protected BreakpointSpec parseBreakpointSpec(StringTokenizer t, 
                                             String atForm, String inForm) {
        EventRequestSpec breakpoint = null;
        try {
            String token = t.nextToken(":( \t\n\r");

            // We can't use hasMoreTokens here because it will cause any leading
            // paren to be lost.
            String rest;
            try {
                rest = t.nextToken("").trim();
            } catch (NoSuchElementException e) {
                rest = null;
            }

            if ((rest != null) && rest.startsWith(":")) {
                t = new StringTokenizer(rest.substring(1));
                String classId = token;
                String lineToken = t.nextToken();

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setParseIntegerOnly(true);
                Number n = nf.parse(lineToken);
                int lineNumber = n.intValue();

                if (t.hasMoreTokens()) {
                    printBreakpointCommandUsage(atForm, inForm);
                    return null;
                }
                try { 
                    breakpoint = Env.specList.createBreakpoint(classId, 
                                                               lineNumber);
                } catch (ClassNotFoundException exc) {
                    MessageOutput.println("is not a valid class name", classId);
                }
            } else {
                // Try stripping method from class.method token.
                int idot = token.lastIndexOf(".");
                if ( (idot <= 0) ||                     /* No dot or dot in first char */
                     (idot >= token.length() - 1) ) { /* dot in last char */
                    printBreakpointCommandUsage(atForm, inForm);
                    return null;
                }
                String methodName = token.substring(idot + 1);
                String classId = token.substring(0, idot);
                List argumentList = null;
                if (rest != null) {
                    if (!rest.startsWith("(") || !rest.endsWith(")")) {
                        MessageOutput.println("Invalid method specification:",
                                              methodName + rest);
                        printBreakpointCommandUsage(atForm, inForm);
                        return null;
                    }
                    // Trim the parens
                    rest = rest.substring(1, rest.length() - 1);

                    argumentList = new ArrayList();
                    t = new StringTokenizer(rest, ",");
                    while (t.hasMoreTokens()) {
                        argumentList.add(t.nextToken());
                    }
                }
                try {
                    breakpoint = Env.specList.createBreakpoint(classId, 
                                                               methodName, 
                                                               argumentList);
                } catch (MalformedMemberNameException exc) {
                    MessageOutput.println("is not a valid method name", methodName);
                } catch (ClassNotFoundException exc) {
                    MessageOutput.println("is not a valid class name", classId);
                }
            }
        } catch (Exception e) {
            printBreakpointCommandUsage(atForm, inForm);
            return null;
        }
        return (BreakpointSpec)breakpoint;
    }

    private void resolveNow(EventRequestSpec spec) {
        boolean success = Env.specList.addEagerlyResolve(spec);
        if (success && !spec.isResolved()) {
            MessageOutput.println("Deferring.", spec.toString());
        }
    }

    void commandStop(StringTokenizer t) {
        Location bploc;
        String atIn;
        byte suspendPolicy = EventRequest.SUSPEND_ALL;

        if (t.hasMoreTokens()) {
            atIn = t.nextToken();
            if (atIn.equals("go") && t.hasMoreTokens()) {
                suspendPolicy = EventRequest.SUSPEND_NONE;
                atIn = t.nextToken();
            } else if (atIn.equals("thread") && t.hasMoreTokens()) {
                suspendPolicy = EventRequest.SUSPEND_EVENT_THREAD;
                atIn = t.nextToken();
            }
        } else {
            listBreakpoints();
            return;
        }        

        BreakpointSpec spec = parseBreakpointSpec(t, "stop at", "stop in");
        if (spec != null) {
            // Enforcement of "at" vs. "in". The distinction is really 
            // unnecessary and we should consider not checking for this 
            // (and making "at" and "in" optional).
            if (atIn.equals("at") && spec.isMethodBreakpoint()) {
                MessageOutput.println("Use stop at to set a breakpoint at a line number");
                printBreakpointCommandUsage("stop at", "stop in");
                return;
            }
            spec.suspendPolicy = suspendPolicy;
            resolveNow(spec);
        }
    }

    void commandClear(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            listBreakpoints();
            return;
        }
        
        BreakpointSpec spec = parseBreakpointSpec(t, "clear", "clear");
        if (spec != null) {         
            if (Env.specList.delete(spec)) {
                MessageOutput.println("Removed:", spec.toString());
            } else {
                MessageOutput.println("Not found:", spec.toString());
            }
        }
    }

    private List parseWatchpointSpec(StringTokenizer t) {
        List list = new ArrayList();
        boolean access = false;
        boolean modification = false;
        int suspendPolicy = EventRequest.SUSPEND_ALL;

        String fieldName = t.nextToken();
        if (fieldName.equals("go")) {
            suspendPolicy = EventRequest.SUSPEND_NONE;
            fieldName = t.nextToken();
        } else if (fieldName.equals("thread")) {
            suspendPolicy = EventRequest.SUSPEND_EVENT_THREAD;
            fieldName = t.nextToken();
        }
        if (fieldName.equals("access")) {
            access = true;
            fieldName = t.nextToken();
        } else if (fieldName.equals("all")) {
            access = true;
            modification = true;
            fieldName = t.nextToken();
        } else {
            modification = true;
        }
        int dot = fieldName.lastIndexOf('.');
        if (dot < 0) {
            MessageOutput.println("Class containing field must be specified.");
            return list;
        }
        String className = fieldName.substring(0, dot);
        fieldName = fieldName.substring(dot+1);

        try {
            EventRequestSpec spec;
            if (access) {
                spec = Env.specList.createAccessWatchpoint(className, 
                                                           fieldName);
                spec.suspendPolicy = suspendPolicy;
                list.add(spec);
            }
            if (modification) {
                spec = Env.specList.createModificationWatchpoint(className, 
                                                                 fieldName);
                spec.suspendPolicy = suspendPolicy;
                list.add(spec);
            }
        } catch (MalformedMemberNameException exc) {
            MessageOutput.println("is not a valid field name", fieldName);
        } catch (ClassNotFoundException exc) {
            MessageOutput.println("is not a valid class name", className);
        }
        return list;
    }

    void commandWatch(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Field to watch not specified");
            return;
        }

        Iterator iter = parseWatchpointSpec(t).iterator();
        while (iter.hasNext()) {
            resolveNow((WatchpointSpec)iter.next());
        }
    }

    void commandUnwatch(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Field to unwatch not specified");
            return;
        }

        Iterator iter = parseWatchpointSpec(t).iterator();
        while (iter.hasNext()) {
            WatchpointSpec spec = (WatchpointSpec)iter.next();
            if (Env.specList.delete(spec)) {
                MessageOutput.println("Removed:", spec.toString());
            } else {
                MessageOutput.println("Not found:", spec.toString());
            }
        }
    }

    void commandTrace(StringTokenizer t) {
        String modif;
        int suspendPolicy = EventRequest.SUSPEND_ALL;

        if (t.hasMoreTokens()) {
            modif = t.nextToken();
            if (modif.equals("go")) {
                suspendPolicy = EventRequest.SUSPEND_NONE;
                modif = t.nextToken();
            } else if (modif.equals("thread")) {
                suspendPolicy = EventRequest.SUSPEND_EVENT_THREAD;
                modif = t.nextToken();
            }
            if (modif.equals("methods")) {
                // nothing to do until other traces
            } else {
                MessageOutput.println("Specify kind for example methods");
            }
        }
        ThreadInfo threadInfo = null;
        if (t.hasMoreTokens()) {
            threadInfo = doGetThread(t.nextToken());
        }
        EventRequestManager erm = Env.vm().eventRequestManager();
        MethodEntryRequest entry = erm.createMethodEntryRequest();
        MethodExitRequest exit = erm.createMethodExitRequest();
        if (threadInfo != null) {
            entry.addThreadFilter(threadInfo.getThread());
            exit.addThreadFilter(threadInfo.getThread());
        }
        Env.addExcludes(entry);
        Env.addExcludes(exit);

        entry.setSuspendPolicy(suspendPolicy);
        exit.setSuspendPolicy(suspendPolicy);
        entry.enable();
        exit.enable();
    }

    void commandUntrace(StringTokenizer t) {
        EventRequestManager erm = Env.vm().eventRequestManager();
        Iterator it = erm.methodEntryRequests().iterator();
        while (it.hasNext()) {
            ((EventRequest)it.next()).disable();
        }
        it = erm.methodExitRequests().iterator();
        while (it.hasNext()) {
            ((EventRequest)it.next()).disable();
        }
    }
    
    void commandList(StringTokenizer t) {
        StackFrame frame = null;
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("No thread specified.");
            return;
        }
        try {
            frame = threadInfo.getCurrentFrame();
        } catch (IncompatibleThreadStateException e) {
            MessageOutput.println("Current thread isnt suspended.");
            return;
        }

        if (frame == null) {
            MessageOutput.println("No frames on the current call stack");
            return;
        }
        
        Location loc = frame.location();
        if (loc.method().isNative()) {
            MessageOutput.println("Current method is native");
            return;
        }

        String sourceFileName = null;
        try {
            sourceFileName = loc.sourceName();

            ReferenceType refType = loc.declaringType();
            int lineno = loc.lineNumber();
    
            if (t.hasMoreTokens()) {
                String id = t.nextToken();
    
                // See if token is a line number.
                try {
                    NumberFormat nf = NumberFormat.getNumberInstance();
                    nf.setParseIntegerOnly(true);
                    Number n = nf.parse(id);
                    lineno = n.intValue();
                } catch (java.text.ParseException jtpe) {
                    // It isn't -- see if it's a method name.
                        List meths = refType.methodsByName(id);
                        if (meths == null || meths.size() == 0) {
                            MessageOutput.println("is not a valid line number or method name for",
                                                  new Object [] {id, refType.name()});
                            return;
                        } else if (meths.size() > 1) {
                            MessageOutput.println("is an ambiguous method name in",
                                                  new Object [] {id, refType.name()});
                            return;
                        }
                        loc = ((Method)meths.get(0)).location();
                        lineno = loc.lineNumber();
                }
            }
            int startLine = Math.max(lineno - 4, 1);
            int endLine = startLine + 9;
            if (lineno < 0) {
                MessageOutput.println("Line number information not available for");
            } else if (Env.sourceLine(loc, lineno) == null) {
                MessageOutput.println("is an invalid line number for",
                                      new Object [] {new Integer (lineno),
                                                     refType.name()});
            } else {
                for (int i = startLine; i <= endLine; i++) {
                    String sourceLine = Env.sourceLine(loc, i);
                    if (sourceLine == null) {
                        break;
                    }
                    if (i == lineno) {
                        MessageOutput.println("source line number current line and line",
                                              new Object [] {new Integer (i),
                                                             sourceLine});
                    } else {
                        MessageOutput.println("source line number and line",
                                              new Object [] {new Integer (i),
                                                             sourceLine});
                    }
                }
            }
        } catch (AbsentInformationException e) {
            MessageOutput.println("No source information available for:", loc.toString());
        } catch(FileNotFoundException exc) {
            MessageOutput.println("Source file not found:", sourceFileName);
        } catch(IOException exc) {
            MessageOutput.println("I/O exception occurred:", exc.toString());
        } 
    }

    void commandLines(StringTokenizer t) { // Undocumented command: useful for testing
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Specify class and method");
        } else {
            String idClass = t.nextToken();
            String idMethod = t.hasMoreTokens() ? t.nextToken() : null;
            try {
                ReferenceType refType = Env.getReferenceTypeFromToken(idClass);
                if (refType != null) {
                    List lines = null;
                    if (idMethod == null) {
                        lines = refType.allLineLocations();
                    } else {
                        List methods = refType.allMethods();
                        Iterator iter = methods.iterator();
                        while (iter.hasNext()) {
                            Method method = (Method)iter.next();
                            if (method.name().equals(idMethod)) {
                                lines = method.allLineLocations();
                            }
                        }
                        if (lines == null) {
                            MessageOutput.println("is not a valid method name", idMethod);
                        }
                    }
                    Iterator iter = lines.iterator();
                    while (iter.hasNext()) {
                        Location line = (Location)iter.next();
                        MessageOutput.printDirectln(line.toString());// Special case: use printDirectln()
                    }
                } else {
                    MessageOutput.println("is not a valid id or class name", idClass);
                }
            } catch (AbsentInformationException e) {
                MessageOutput.println("Line number information not available for", idClass);
            }
        }
    }

    void commandClasspath(StringTokenizer t) {
        if (Env.vm() instanceof PathSearchingVirtualMachine) {
            PathSearchingVirtualMachine vm = (PathSearchingVirtualMachine)Env.vm();
            MessageOutput.println("base directory:", vm.baseDirectory());
            MessageOutput.println("classpath:", vm.classPath().toString());
            MessageOutput.println("bootclasspath:", vm.bootClassPath().toString());
        } else {
            MessageOutput.println("The VM does not use paths");
        }
    }

    /* Get or set the source file path list. */
    void commandUse(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.printDirectln(Env.getSourcePath());// Special case: use printDirectln()
        } else {
            /*
             * Take the remainder of the command line, minus
             * leading or trailing whitespace.  Embedded
             * whitespace is fine.
             */
            Env.setSourcePath(t.nextToken("").trim());
        }
    }

    /* Print a stack variable */
    private void printVar(LocalVariable var, Value value) {
        MessageOutput.println("expr is value",
                              new Object [] {var.name(), 
                                             value == null ? "null" : value.toString()});
    }

    /* Print all local variables in current stack frame. */
    void commandLocals() {
        StackFrame frame;
        ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
        if (threadInfo == null) {
            MessageOutput.println("No default thread specified:");
            return;
        }
        try {
            frame = threadInfo.getCurrentFrame();
            if (frame == null) {
                throw new AbsentInformationException();
            }
            List vars = frame.visibleVariables();
    
            if (vars.size() == 0) {
                MessageOutput.println("No local variables");
                return;
            }
            Map values = frame.getValues(vars);

            MessageOutput.println("Method arguments:");
            for (Iterator it = vars.iterator(); it.hasNext(); ) {
                LocalVariable var = (LocalVariable)it.next();
                if (var.isArgument()) {
                    Value val = (Value)values.get(var);
                    printVar(var, val);
                }
            }
            MessageOutput.println("Local variables:");
            for (Iterator it = vars.iterator(); it.hasNext(); ) {
                LocalVariable var = (LocalVariable)it.next();
                if (!var.isArgument()) {
                    Value val = (Value)values.get(var);
                    printVar(var, val);
                }
            }
        } catch (AbsentInformationException aie) {
            MessageOutput.println("Local variable information not available.");
        } catch (IncompatibleThreadStateException exc) {
            MessageOutput.println("Current thread isnt suspended.");
        }
    }

    private void dump(ObjectReference obj, ReferenceType refType,
                      ReferenceType refTypeBase) {
        for (Iterator it = refType.fields().iterator(); it.hasNext(); ) {
            StringBuffer o = new StringBuffer();
            Field field = (Field)it.next();
            o.append("    ");
            if (!refType.equals(refTypeBase)) {
                o.append(refType.name());
                o.append(".");
            }
            o.append(field.name());
            o.append(MessageOutput.format("colon space"));
            o.append(obj.getValue(field));
            MessageOutput.printDirectln(o.toString()); // Special case: use printDirectln()
        }
        if (refType instanceof ClassType) {
            ClassType sup = ((ClassType)refType).superclass();
            if (sup != null) {
                dump(obj, sup, refTypeBase);
            }
        } else if (refType instanceof InterfaceType) {
            List sups = ((InterfaceType)refType).superinterfaces();
            for (Iterator it = sups.iterator(); it.hasNext(); ) {
                dump(obj, (ReferenceType)it.next(), refTypeBase);
            }
        } else {
            /* else refType is an instanceof ArrayType */
            if (obj instanceof ArrayReference) {
                for (Iterator it = ((ArrayReference)obj).getValues().iterator();
                     it.hasNext(); ) {
                    MessageOutput.printDirect(it.next().toString());// Special case: use printDirect()
                    if (it.hasNext()) {
                        MessageOutput.printDirect(", ");// Special case: use printDirect()
                    }
                }
                MessageOutput.println();
            }
        }
    }

    /* Print a specified reference. 
     */
    void doPrint(StringTokenizer t, boolean dumpObject) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No objects specified.");
            return;
        }

        while (t.hasMoreTokens()) {
            String expr = t.nextToken("");
            Value val = evaluate(expr);
            if (val == null) {
                MessageOutput.println("expr is null", expr.toString());
            } else if (dumpObject && (val instanceof ObjectReference) &&
                       !(val instanceof StringReference)) {
                ObjectReference obj = (ObjectReference)val;
                ReferenceType refType = obj.referenceType();
                MessageOutput.println("expr is value",
                                      new Object [] {expr.toString(),
                                                     MessageOutput.format("grouping begin character")});
                dump(obj, refType, refType);
                MessageOutput.println("grouping end character");
            } else {
                  String strVal = getStringValue();
                  if (strVal != null) {
                     MessageOutput.println("expr is value", new Object [] {expr.toString(),
                                                                      strVal});
                   } 
            }
        }
    }

    void commandPrint(final StringTokenizer t, final boolean dumpObject) {
        new AsyncExecution(this.Env, this.MessageOutput) {
                void action() {
                    doPrint(t, dumpObject);
                }
            }; 
    }

    void commandSet(final StringTokenizer t) {
        String all = t.nextToken("");

        /*
         * Bare bones error checking. 
         */
        if (all.indexOf('=') == -1) {
            MessageOutput.println("Invalid assignment syntax");
            MessageOutput.printPrompt();
            return;
        }

        /*
         * The set command is really just syntactic sugar. Pass it on to the 
         * print command.
         */
        commandPrint(new StringTokenizer(all), false);
    }

    void doLock(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No object specified.");
            return;
        }

        String expr = t.nextToken("");
        Value val = evaluate(expr);

        try {
            if ((val != null) && (val instanceof ObjectReference)) {
                ObjectReference object = (ObjectReference)val;
                String strVal = getStringValue();
                if (strVal != null) {
                    MessageOutput.println("Monitor information for expr",
                                      new Object [] {expr.trim(),
                                                     strVal});
                } 
                ThreadReference owner = object.owningThread();
                if (owner == null) {
                    MessageOutput.println("Not owned");
                } else {
                    MessageOutput.println("Owned by:",
                                          new Object [] {owner.name(),
                                                         new Integer (object.entryCount())});
                }
                List waiters = object.waitingThreads();
                if (waiters.size() == 0) {
                    MessageOutput.println("No waiters");
                } else {
                    Iterator iter = waiters.iterator();
                    while (iter.hasNext()) {
                        ThreadReference waiter = (ThreadReference)iter.next();
                        MessageOutput.println("Waiting thread:", waiter.name());
                    }
                }
            } else {
                MessageOutput.println("Expression must evaluate to an object");
            }
        } catch (IncompatibleThreadStateException e) {
            MessageOutput.println("Threads must be suspended");
        }
    }

    void commandLock(final StringTokenizer t) {
        new AsyncExecution(this.Env, this.MessageOutput) {
                void action() {
                    doLock(t);
                }
            }; 
    }

    private void printThreadLockInfo(ThreadInfo threadInfo) {
        ThreadReference thread = threadInfo.getThread();
        try {
            MessageOutput.println("Monitor information for thread", thread.name());
            List owned = thread.ownedMonitors();
            if (owned.size() == 0) {
                MessageOutput.println("No monitors owned");
            } else {
                Iterator iter = owned.iterator();
                while (iter.hasNext()) {
                    ObjectReference monitor = (ObjectReference)iter.next();
                    MessageOutput.println("Owned monitor:", monitor.toString());
                }
            }
            ObjectReference waiting = thread.currentContendedMonitor();
            if (waiting == null) {
                MessageOutput.println("Not waiting for a monitor");
            } else {
                MessageOutput.println("Waiting for monitor:", waiting.toString());
            }
        } catch (IncompatibleThreadStateException e) {
            MessageOutput.println("Threads must be suspended");
        }
    }

    void commandThreadlocks(final StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
            if (threadInfo == null) {
                MessageOutput.println("Current thread not set.");
            } else {
                printThreadLockInfo(threadInfo);
            }
            return;
        }
        String token = t.nextToken();
        if (token.toLowerCase().equals("all")) {
            Iterator iter = ThreadInfo.threads(this.Env, this.MessageOutput).iterator();
            while (iter.hasNext()) {
                ThreadInfo threadInfo = (ThreadInfo)iter.next();
                printThreadLockInfo(threadInfo);
            }
        } else {
            ThreadInfo threadInfo = doGetThread(token);
            if (threadInfo != null) {
                ThreadInfo.setCurrentThreadInfo(threadInfo);
                printThreadLockInfo(threadInfo);
            }
        }
    }

    void doDisableGC(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No object specified.");
            return;
        }

        String expr = t.nextToken("");
        Value val = evaluate(expr);
        if ((val != null) && (val instanceof ObjectReference)) {
            ObjectReference object = (ObjectReference)val;
            object.disableCollection();
            String strVal = getStringValue();
            if (strVal != null) {
                 MessageOutput.println("GC Disabled for", strVal);
            } 
        } else {
            MessageOutput.println("Expression must evaluate to an object");
        }
    }

    void commandDisableGC(final StringTokenizer t) {
        new AsyncExecution(this.Env, this.MessageOutput) {
                void action() {
                    doDisableGC(t);
                }
            }; 
    }

    void doEnableGC(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No object specified.");
            return;
        }

        String expr = t.nextToken("");
        Value val = evaluate(expr);
        if ((val != null) && (val instanceof ObjectReference)) {
            ObjectReference object = (ObjectReference)val;
            object.enableCollection();
            String strVal = getStringValue();
            if (strVal != null) {
                 MessageOutput.println("GC Enabled for", strVal);
            } 
        } else {
            MessageOutput.println("Expression must evaluate to an object");
        }
    }

    void commandEnableGC(final StringTokenizer t) {
        new AsyncExecution(this.Env, this.MessageOutput) {
                void action() {
                    doEnableGC(t);
                }
            }; 
    }

    void doSave(StringTokenizer t) {// Undocumented command: useful for testing.
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No save index specified.");
            return;
        }

        String key = t.nextToken();

        if (!t.hasMoreTokens()) {
            MessageOutput.println("No expression specified.");
            return;
        }
        String expr = t.nextToken("");
        Value val = evaluate(expr);
        if (val != null) {
            Env.setSavedValue(key, val);
            String strVal = getStringValue();
            if (strVal != null) {
                 MessageOutput.println("saved", strVal);
            } 
        } else {
            MessageOutput.println("Expression cannot be void");
        }
    }

    void commandSave(final StringTokenizer t) { // Undocumented command: useful for testing.
        if (!t.hasMoreTokens()) {
            Set keys = Env.getSaveKeys();
            Iterator iter = keys.iterator();
            if (!iter.hasNext()) {
                MessageOutput.println("No saved values");
                return;
            }
            while (iter.hasNext()) {
                String key = (String)iter.next();
                Value value = Env.getSavedValue(key);
                if ((value instanceof ObjectReference) &&
                    ((ObjectReference)value).isCollected()) {
                    MessageOutput.println("expr is value <collected>",
                                          new Object [] {key, value.toString()});
                } else {
                    if (value == null){
                        MessageOutput.println("expr is null", key);
                    } else {
                        MessageOutput.println("expr is value",
                                              new Object [] {key, value.toString()});
                    }
                }
            }
        } else {
            new AsyncExecution(this.Env, this.MessageOutput) {
                    void action() {
                        doSave(t);
                    }
                }; 
        }

    }

   void commandBytecodes(final StringTokenizer t) { // Undocumented command: useful for testing.
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No class specified.");
            return;
        }
        String className = t.nextToken();

        if (!t.hasMoreTokens()) {
            MessageOutput.println("No method specified.");
            return;
        }
        // Overloading is not handled here.
        String methodName = t.nextToken();

        List classes = Env.vm().classesByName(className);
        // TO DO: handle multiple classes found
        if (classes.size() == 0) {
            if (className.indexOf('.') < 0) {
                MessageOutput.println("not found (try the full name)", className);
            } else {
                MessageOutput.println("not found", className);
            }
            return;
        } 
        
        ReferenceType rt = (ReferenceType)classes.get(0);
        if (!(rt instanceof ClassType)) {
            MessageOutput.println("not a class", className);
            return;
        }

        byte[] bytecodes = null;                                               
        List list = rt.methodsByName(methodName);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Method method = (Method)iter.next();
            if (!method.isAbstract()) {
                bytecodes = method.bytecodes();
                break;
            }
        }

        StringBuffer line = new StringBuffer(80);
        line.append("0000: ");
        for (int i = 0; i < bytecodes.length; i++) {
            if ((i > 0) && (i % 16 == 0)) {
                MessageOutput.printDirectln(line.toString());// Special case: use printDirectln()
                line.setLength(0);
                line.append(String.valueOf(i));
                line.append(": ");
                int len = line.length();
                for (int j = 0; j < 6 - len; j++) {
                    line.insert(0, '0');
                }
            }
            int val = 0xff & bytecodes[i];
            String str = Integer.toHexString(val);
            if (str.length() == 1) {
                line.append('0');
            }
            line.append(str);
            line.append(' ');
        }
        if (line.length() > 6) {
            MessageOutput.printDirectln(line.toString());// Special case: use printDirectln()
        }
    }

    void commandExclude(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.printDirectln(Env.excludesString());// Special case: use printDirectln()
        } else {
            String rest = t.nextToken("");
            if (rest.equals("none")) {
                rest = "";
            }
            Env.setExcludes(rest);
        }
    }

    void commandRedefine(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("Specify classes to redefine");
        } else {
            String className = t.nextToken(); 
            List classes = Env.vm().classesByName(className);
            if (classes.size() == 0) {
                MessageOutput.println("No class named", className);
                return;
            }
            if (classes.size() > 1) {
                MessageOutput.println("More than one class named", className);
                return;
            }
	    Env.setSourcePath(Env.getSourcePath());
            ReferenceType refType = (ReferenceType)classes.get(0);
            if (!t.hasMoreTokens()) {
                MessageOutput.println("Specify file name for class", className);
                return;
            }
            String fileName = t.nextToken(); 
            File phyl = new File(fileName);
            byte[] bytes = new byte[(int)phyl.length()];
            try {
                InputStream in = new FileInputStream(phyl);
                in.read(bytes);
                in.close();
            } catch (Exception exc) {
                MessageOutput.println("Error reading file",
                             new Object [] {fileName, exc.toString()});
                return;
            }
            Map map = new HashMap();
            map.put(refType, bytes);
            try {
                Env.vm().redefineClasses(map);
            } catch (Throwable exc) {
                MessageOutput.println("Error redefining class to file",
                             new Object [] {className,
                                            fileName,
                                            exc});
            }
        }
    }

    void commandPopFrames(StringTokenizer t, boolean reenter) {
        ThreadInfo threadInfo;

        if (t.hasMoreTokens()) {
            String token = t.nextToken();
            threadInfo = doGetThread(token);
            if (threadInfo == null) {
                return;
            }
        } else {
            threadInfo = ThreadInfo.getCurrentThreadInfo();
            if (threadInfo == null) {
                MessageOutput.println("No thread specified.");
                return;
            }
        }
       
        try {
            StackFrame frame = threadInfo.getCurrentFrame();
            threadInfo.getThread().popFrames(frame);
            threadInfo = ThreadInfo.getCurrentThreadInfo();
            ThreadInfo.setCurrentThreadInfo(threadInfo);
            if (reenter) {
                commandStepi();
            }
        } catch (Throwable exc) {
            MessageOutput.println("Error popping frame", exc.toString());
        }
    }

    void commandExtension(StringTokenizer t) {
        if (!t.hasMoreTokens()) {
            MessageOutput.println("No class specified.");            
            return;
        }

        String idClass = t.nextToken();
        ReferenceType cls = Env.getReferenceTypeFromToken(idClass);
        String extension = null;
        if (cls != null) {
            try {
                extension = cls.sourceDebugExtension();
                MessageOutput.println("sourcedebugextension", extension);
            } catch (AbsentInformationException e) {
                MessageOutput.println("No sourcedebugextension specified");
            }
        } else {
            MessageOutput.println("is not a valid id or class name", idClass);
        }
    }

    void commandVersion(String debuggerName,
                        VirtualMachineManager vmm) {
        MessageOutput.println("minus version",
                              new Object [] { debuggerName,
                                              new Integer(vmm.majorInterfaceVersion()),
                                              new Integer(vmm.minorInterfaceVersion()),
                                                  System.getProperty("java.version")});
        if (Env.connection() != null) {
            try {
                MessageOutput.printDirectln(Env.vm().description());// Special case: use printDirectln()
            } catch (VMNotConnectedException e) {
                MessageOutput.println("No VM connected");
            }
        }
    }
}
