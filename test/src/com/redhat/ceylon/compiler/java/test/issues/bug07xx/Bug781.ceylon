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
@nomodel
class Bug781Class() {
    shared void c(){}
}

@nomodel
interface Bug781Interface {
    shared formal void i();
}

@nomodel
class Bug781_1<T>(T t) given T satisfies Bug781Interface & Bug781Class {
    t.c();
    t.i();
    void m<T>(T t) given T satisfies Bug781Interface & Bug781Class {
        t.c();
        t.i();
    }
}

@nomodel
interface Bug781_2<T> given T satisfies Bug781Interface & Bug781Class {
    void m<T>(T t) given T satisfies Bug781Interface & Bug781Class {
        t.c();
        t.i();
    }
}

@nomodel
shared void bug781<T>(T t) given T satisfies Bug781Interface & Bug781Class {
    t.c();
    t.i();
}
