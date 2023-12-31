/*
 * @(#)ThreadIterator.java	1.13 03/12/19
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

import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;

import java.util.List;
import java.util.Iterator;
                     
class ThreadIterator implements Iterator {
    Iterator it = null;
    ThreadGroupIterator tgi;
    Environment Env;

    ThreadIterator(Environment pEnv, ThreadGroupReference tg) {
    	Env = pEnv;
        tgi = new ThreadGroupIterator(pEnv, tg);
    }

    ThreadIterator(Environment pEnv, List tgl) {
    	Env = pEnv;
        tgi = new ThreadGroupIterator(pEnv, tgl);
    }

    ThreadIterator(Environment pEnv) {
    	Env = pEnv;
        tgi = new ThreadGroupIterator(pEnv);
    }

    public boolean hasNext() {
        while (it == null || !it.hasNext()) {
            if (!tgi.hasNext()) {
                return false; // no more
            }
            it = tgi.nextThreadGroup().threads().iterator();
        }
        return true;
    }

    public Object next() {
        return it.next();
    }

    public ThreadReference nextThread() {
        return (ThreadReference)next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}


