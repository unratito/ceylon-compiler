/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
shared class IsOperatorTest() {
    object o_ extends Object() {
        shared actual Boolean equals(Object o) {
            return false;
        }
        shared actual Integer hash = 0;
    }
    Void o = o_;
    object i_ extends Object() satisfies Identifiable {
        shared actual Boolean equals(Object o) {
            return false;
        }
        shared actual Integer hash = 0;
    }
    Void i = i_;
    object io_ extends IdentifiableObject() {}
    Void io = io_;
    Void nowt = null;
    
    @test
    shared void ifIs() {
        if (is Nothing nowt) {
           
        } else {
            throw;
        }
        if (is Object nowt) {
            throw;
        }
        if (is Identifiable nowt) {
            throw;
        }
        if (is IdentifiableObject nowt) {
            throw;
        }
        
        if (is Nothing o) {
            throw;
        }
        if (is Object o) {
        
        } else {
            throw;
        }
        if (is Identifiable o) {
            throw;
        }
        if (is IdentifiableObject o) {
            throw;
        }
        
        if (is Nothing i) {
            throw;
        }
        if (is Object i) {
        
        } else {
            throw;
        }
        if (is Identifiable i) {
        
        } else {
            throw;
        }
        if (is IdentifiableObject i) {
            throw;
        }
        
        if (is Nothing io) {
            throw;
        }
        if (is Object io) {
        
        } else {
            throw;
        }
        if (is Identifiable io) {
        
        } else {
            throw;
        }
        if (is IdentifiableObject io) {
            
        } else {
            throw;
        }
    }
    
    @test
    shared void operatorIs() {
        variable Boolean b := false;
        b := is Nothing nowt;
        if (b) {
           
        } else {
            throw;
        }
        b:= is Object nowt;
        if (b) {
            throw;
        }
        b := is Identifiable nowt;
        if (b) {
            throw;
        }
        b := is IdentifiableObject nowt;
        if (b) {
            throw;
        }
        
        b:= is Nothing o;
        if (b) {
            throw;
        }
        b := is Object o;
        if (b) {
        
        } else {
            throw;
        }
        b := is Identifiable o;
        if (b) {
            throw;
        }
        b := is IdentifiableObject o;
        if (b) {
            throw;
        }
        
        b := is Nothing i;
        if (b) {
            throw;
        }
        b := is Object i;
        if (b) {
        
        } else {
            throw;
        }
        b := is Identifiable i;
        if (b) {
        
        } else {
            throw;
        }
        b := is IdentifiableObject i;
        if (b) {
            throw;
        }
        
        b := is Nothing io;
        if (b) {
            throw;
        }
        b := is Object io;
        if (b) {
        
        } else {
            throw;
        }
        b := is Identifiable io;
        if (b) {
        
        } else {
            throw;
        }
        b := is IdentifiableObject io;
        if (b) {
            
        } else {
            throw;
        }
    }
 
 
    
}