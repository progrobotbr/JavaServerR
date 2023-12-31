/*
 * @(#)Env.java	1.37 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Copyright (c) 1997-1999 by Sun Microsystems, Inc. All Rights Reserved.
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

import com.sun.jdi.*;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.tools.jdi.*;
import java.util.*;
import java.io.*;


class Environment {

    EventRequestSpecList specList;

    private VMConnection connection;

    private SourceMapper sourceMapper = new SourceMapper("");
    private List excludes;

    private final int SOURCE_CACHE_SIZE = 5;
    private List sourceCache = new LinkedList();

    private HashMap savedValues = new HashMap();
    
    public messageOutput MessageOutput;

    public Environment( messageOutput pmsgout){
    	this.MessageOutput = pmsgout;
    }
    
    public void init(messageOutput pmsgout, String connectSpec, boolean openNow, int flags) {
    	this.MessageOutput = pmsgout;
    	specList = new EventRequestSpecList(this, pmsgout);
        connection = new VMConnection(this, this.MessageOutput, connectSpec, flags);
        if (!connection.isLaunch() || openNow) {
            connection.open();
        }
    }

    public InputStream getProcessInputStream(){
    	return(connection.getProcessInputStream());
    }
    
    public OutputStream getProcessOutputStream(){
    	return(connection.getProcessOutputStream());
    }

    public VMConnection connection() {
        return connection;
    }

    public VirtualMachine vm() {
        return connection.vm();
    }

    public void shutdown() {
        shutdown(null);
    }

    public void shutdown(String message) {
        if (connection != null) {
            try {
                connection.disposeVM();
            } catch (VMDisconnectedException e) {
                // Shutting down after the VM has gone away. This is
                // not an error, and we just ignore it. 
            }
        }
        if (message != null) {
            MessageOutput.lnprint(message);
            MessageOutput.println();
        }
        //RAL System.exit(0);
    }

    public void setSourcePath(String srcPath) {
        sourceMapper = new SourceMapper(srcPath);
        sourceCache.clear();
    }

    public void setSourcePath(List srcList) {
        sourceMapper = new SourceMapper(srcList);
        sourceCache.clear();
    }

    public String getSourcePath() {
        return sourceMapper.getSourcePath();
    }

    private List excludes() {
        if (excludes == null) {
            setExcludes("java.*, javax.*, sun.*, com.sun.*");
        }
        return excludes;
    }

    public String excludesString() {
        Iterator iter = excludes().iterator();
        StringBuffer buffer = new StringBuffer();
        while (iter.hasNext()) {
            String pattern = (String)iter.next();
            buffer.append(pattern);
            buffer.append(",");
        }
        return buffer.toString();
    }

    public void addExcludes(StepRequest request) {
        Iterator iter = excludes().iterator();
        while (iter.hasNext()) {
            String pattern = (String)iter.next();
            request.addClassExclusionFilter(pattern);
        }
    }

    public void addExcludes(MethodEntryRequest request) {
        Iterator iter = excludes().iterator();
        while (iter.hasNext()) {
            String pattern = (String)iter.next();
            request.addClassExclusionFilter(pattern);
        }
    }

    public void addExcludes(MethodExitRequest request) {
        Iterator iter = excludes().iterator();
        while (iter.hasNext()) {
            String pattern = (String)iter.next();
            request.addClassExclusionFilter(pattern);
        }
    }

    public void setExcludes(String excludeString) {
        StringTokenizer t = new StringTokenizer(excludeString, " ,;");
        List list = new ArrayList();
        while (t.hasMoreTokens()) {
            list.add(t.nextToken());
        }
        excludes = list;
    }

    /**
     * Return a Reader cooresponding to the source of this location.
     * Return null if not available.
     * Note: returned reader must be closed.
     */
    public BufferedReader sourceReader(Location location) {
        return sourceMapper.sourceReader(location);
    }

    public synchronized String sourceLine(Location location, int lineNumber) 
                                          throws IOException {
        if (lineNumber == -1) {
            throw new IllegalArgumentException();
        }

        try {
            String fileName = location.sourceName();
    
            Iterator iter = sourceCache.iterator();
            SourceCode code = null;
            while (iter.hasNext()) {
                SourceCode candidate = (SourceCode)iter.next();
                if (candidate.fileName().equals(fileName)) {
                    code = candidate;
                    iter.remove();
                    break;
                }
            }
            if (code == null) {
                BufferedReader reader = sourceReader(location);
                if (reader == null) {
                    throw new FileNotFoundException(fileName);
                }
                code = new SourceCode(fileName, reader);
                if (sourceCache.size() == SOURCE_CACHE_SIZE) {
                    sourceCache.remove(sourceCache.size() - 1);
                }
            }
            sourceCache.add(0, code);
            return code.sourceLine(lineNumber);
        } catch (AbsentInformationException e) {
            throw new IllegalArgumentException();
        }
    }

    /** Return a description of an object. */
    public String description(ObjectReference ref) {
        ReferenceType clazz = ref.referenceType();
        long id = ref.uniqueID();  
        if (clazz == null) {
            return toHex(id);
        } else {
            return MessageOutput.format("object description and hex id",
                                        new Object [] {clazz.name(),
                                                       toHex(id)});
        }
    }

    /** Convert a long to a hexadecimal string. */
    public String toHex(long n) {
	char s1[] = new char[16];
	char s2[] = new char[18];

	/* Store digits in reverse order. */
	int i = 0;
	do {
	    long d = n & 0xf;
	    s1[i++] = (char)((d < 10) ? ('0' + d) : ('a' + d - 10));
	} while ((n >>>= 4) > 0);

	/* Now reverse the array. */
	s2[0] = '0';
	s2[1] = 'x';
	int j = 2;
	while (--i >= 0) {
	    s2[j++] = s1[i];
	}
	return new String(s2, 0, j);
    }

    /** Convert hexadecimal strings to longs. */
    public long fromHex(String hexStr) {
	String str = hexStr.startsWith("0x") ?
	    hexStr.substring(2).toLowerCase() : hexStr.toLowerCase();
	if (hexStr.length() == 0) {
	    throw new NumberFormatException();
	}
	
	long ret = 0;
	for (int i = 0; i < str.length(); i++) {
	    int c = str.charAt(i);
	    if (c >= '0' && c <= '9') {
		ret = (ret * 16) + (c - '0');
	    } else if (c >= 'a' && c <= 'f') {
		ret = (ret * 16) + (c - 'a' + 10);
	    } else {
		throw new NumberFormatException();
	    }
	}
	return ret;
    }
    
    public ReferenceType getReferenceTypeFromToken(String idToken) {
        ReferenceType cls = null;
        if (Character.isDigit(idToken.charAt(0))) {
            cls = null;
        } else if (idToken.startsWith("*.")) {
        // This notation saves typing by letting the user omit leading
        // package names. The first 
        // loaded class whose name matches this limited regular
        // expression is selected.
        idToken = idToken.substring(1);
        List classes = this.vm().allClasses();
        Iterator iter = classes.iterator();
        while (iter.hasNext()) {
            ReferenceType type = ((ReferenceType)iter.next());
            if (type.name().endsWith(idToken)) {
                cls = type;
                break;
            }
        }
    } else {
            // It's a class name
            List classes = this.vm().classesByName(idToken);
            if (classes.size() > 0) {
                // TO DO: handle multiples
                cls = (ReferenceType)classes.get(0);
            }
        }
        return cls;
    }

    public Set getSaveKeys() {
        return savedValues.keySet();
    }

    public Value getSavedValue(String key) {
        return (Value)savedValues.get(key);
    }

    public void setSavedValue(String key, Value value) {
        savedValues.put(key, value);
    }

    public class SourceCode {
        private String fileName;
        private List sourceLines = new ArrayList();

        SourceCode(String fileName, BufferedReader reader)  throws IOException {
            this.fileName = fileName;
            try {
                String line = reader.readLine();
                while (line != null) {
                    sourceLines.add(line);
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }
        }

        String fileName() {
            return fileName;
        }

        String sourceLine(int number) {
            int index = number - 1; // list is 0-indexed
            if (index >= sourceLines.size()) {
                return null;
            } else {
                return (String)sourceLines.get(index);  
            }
        }
    }
}
