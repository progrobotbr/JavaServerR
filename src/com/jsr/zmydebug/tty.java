package com.jsr.zmydebug;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.event.WatchpointEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;

public class tty implements Runnable{

	private static final String JVMDRV = "com.sun.jdi.CommandLineLaunch";
    private static final String JVMPAR = "com.sun.jdi.CommandLineLaunch:main=com.jsr.server.jvmtrz,options=-classpath C:\\Users\\Renato\\workspace\\JavaServerR\\bin";
	private boolean connected = true;
	
    public void launch(){
		try {
			Connector connector = this.findConnector(JVMDRV);
			LaunchingConnector launcher = (LaunchingConnector)connector;
			Map args = this.parseConnectorArgs(connector, JVMPAR);
			VirtualMachine vm = launcher.launch(args);
			Process process = vm.process();
			vm.setDebugTraceMode(0);
			if (vm.canBeModified()){
				 EventRequestManager erm = vm.eventRequestManager();
				 ThreadStartRequest tsr = erm.createThreadStartRequest();
			     tsr.enable();
			     ThreadDeathRequest tdr = erm.createThreadDeathRequest();
			     tdr.enable();
			}
		 } catch (Exception ex) {
			 
		 }
	}
	
    //Encontra a classe que chamará a vm
 	private Connector findConnector(String name) {
        List connectors = Bootstrap.virtualMachineManager().allConnectors();
        Iterator iter = connectors.iterator();
        while (iter.hasNext()) {
            Connector connector = (Connector)iter.next();
            if (connector.name().equals(name)) {
                return connector;
            }
        }
        return null;
    }
 	
 	private Map parseConnectorArgs(Connector connector, String argString) {
        Map arguments = connector.defaultArguments();

        /*
         * We are parsing strings of the form:
         *    name1=value1,[name2=value2,...]
         * However, the value1...valuen substrings may contain
         * embedded comma(s), so make provision for quoting inside
         * the value substrings. (Bug ID 4285874)
         */
        String regexPattern =
            "(quote=[^,]+,)|" +           // special case for quote=.,
            "(\\w+=)" +                   // name=
            "(((\"[^\"]*\")|" +           //   ( "l , ue"
            "('[^']*')|" +                //     'l , ue'
            "([^,'\"]+))+,)";             //     v a l u e )+ ,
        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(argString);
        while (m.find()) {
            int startPosition = m.start();
            int endPosition = m.end();
            if (startPosition > 0) {
                /*
                 * It is an error if parsing skips over any part of argString.
                 */
                throw new IllegalArgumentException("Illegal connector argument");
            }

            String token = argString.substring(startPosition, endPosition);
            int index = token.indexOf('=');
            String name = token.substring(0, index);
            String value = token.substring(index + 1,
                                           token.length() - 1); // Remove comma delimiter

            Connector.Argument argument = (Connector.Argument)arguments.get(name);
            if (argument == null) {
                throw new IllegalArgumentException("Argument is not defined for connector:" + connector.name());
            }
            argument.setValue(value);

            argString = argString.substring(endPosition); // Remove what was just parsed...
            m = p.matcher(argString);                     //    and parse again on what is left.
        }
        if ((! argString.equals(",")) && (argString.length() > 0)) {
            /*
             * It is an error if any part of argString is left over,
             * unless it was empty to begin with.
             */
            throw new IllegalArgumentException("Illegal connector argument");
        }
        return arguments;
    }
 	
 	public void run() { 
        EventQueue queue = Env.vm().eventQueue();
        while (connected) {
            try {
                EventSet eventSet = queue.remove();
                boolean resumeStoppedApp = false;
                EventIterator it = eventSet.eventIterator();
                while (it.hasNext()) {
                    resumeStoppedApp |= !handleEvent(it.nextEvent());
                }

                if (resumeStoppedApp) {
                    eventSet.resume();
                } else if (eventSet.suspendPolicy() == EventRequest.SUSPEND_ALL) {
                    setCurrentThread(eventSet);
                    notifier.vmInterrupted();
                }
            } catch (InterruptedException exc) {
                // Do nothing. Any changes will be seen at top of loop.
            } catch (VMDisconnectedException discExc) {
                handleDisconnectedException();
                break;
            }
        }
        synchronized (this) {
            //completed = true;
            notifyAll();
        }
        
        
    }
 	
 	private boolean handleEvent(Event event) {
 		
        notifier.receivedEvent(event);

        if (event instanceof ExceptionEvent) {
            return exceptionEvent(event);
        } else if (event instanceof BreakpointEvent) {
            return breakpointEvent(event);
        } else if (event instanceof WatchpointEvent) {
            return fieldWatchEvent(event);
        } else if (event instanceof StepEvent) {
            return stepEvent(event);
        } else if (event instanceof MethodEntryEvent) {
            return methodEntryEvent(event);
        } else if (event instanceof MethodExitEvent) {
            return methodExitEvent(event);
        } else if (event instanceof ClassPrepareEvent) {
            return classPrepareEvent(event);
        } else if (event instanceof ClassUnloadEvent) {
            return classUnloadEvent(event);
        } else if (event instanceof ThreadStartEvent) {
            return threadStartEvent(event);
        } else if (event instanceof ThreadDeathEvent) {
            return threadDeathEvent(event);
        } else if (event instanceof VMStartEvent) {
            return vmStartEvent(event);
        } else {
            return handleExitEvent(event);
        }
    }
}
