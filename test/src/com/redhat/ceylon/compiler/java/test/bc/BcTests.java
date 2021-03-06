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
package com.redhat.ceylon.compiler.java.test.bc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.java.util.Util;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.file.ZipFileIndexCache;

public class BcTests extends CompilerTest {
    
    private final String providerModuleSrc = "provider/module.ceylon";
    private final String providerPackageSrc = "provider/package.ceylon";
    private final String clientModuleSrc = "client/module.ceylon";
    private final String clientModuleName = "com.redhat.ceylon.compiler.java.test.bc.client";
    private final String providerModuleName = "com.redhat.ceylon.compiler.java.test.bc.provider";
    
    /**
     * Checks that we can still compile a client after a change
     * @param ceylon
     */
    protected void source(String ceylon) {
        String providerPreSrc = "provider/" + ceylon + "_pre.ceylon";
        String providerPostSrc = "provider/" + ceylon + "_post.ceylon";
        String clientSrc = "client/" + ceylon + "_client.ceylon";
        
        // Compile provider
        compile(providerPreSrc,
                providerModuleSrc, providerPackageSrc);
        
        // Compile client
        compile(clientSrc,
                clientModuleSrc);
        
        // New version of provider
        compile(providerPostSrc,
                providerModuleSrc, providerPackageSrc);
        
        // Check the client still compilers
        compile(clientSrc,
                clientModuleSrc);
    }
    
    /**
     * Checks that we can still link an existing client after a change
     * @param ceylon
     */
    protected void binary(String main, String ceylon) {
        String providerPreSrc = "provider/" + ceylon + "_pre.ceylon";
        String providerPostSrc = "provider/" + ceylon + "_post.ceylon";
        String clientSrc = "client/" + ceylon + "_client.ceylon";
        
        // Compile provider
        compile(providerPreSrc,
                providerModuleSrc, providerPackageSrc);

        // Compile client
        compile(clientSrc,
                clientModuleSrc);
        
        // New version of provider
        compile(providerPostSrc,
                providerModuleSrc, providerPackageSrc);
        
        // Now try running the client
        File clientCar = getModuleArchive(clientModuleName, "0.1");
        File providerCar = getModuleArchive(providerModuleName, "0.1");
        run(clientModuleName + "." + main, 
                clientCar, providerCar);
    }
    
    @Test
    public void testClassMethAddDefaultedParam() {
        source("ClassMethAddDefaultedParam");
        binary("classMethAddDefaultedParam",
                "ClassMethAddDefaultedParam");
    }
    
    @Test
    public void testFunctionAddDefaultedParam() {
        source("FunctionAddDefaultedParam");
        binary("functionAddDefaultedParam_client",
                "FunctionAddDefaultedParam");
    }
    
    @Test
    public void testClassInitAddDefaultedParam() {
        source("ClassInitAddDefaultedParam");
        binary("classInitAddDefaultedParam",
                "ClassInitAddDefaultedParam");
    }

    @Test
    public void testBinaryVersionIncompatible(){
        compile("JavaOldVersion.java");
        assertErrors("CeylonNewVersion", 
                new CompilerError(-1, "You are using a Ceylon class compiled for an incompatible version of the Ceylon compiler (0.0).\n  This compiler supports 3.0.\n  Please try to recompile your module using a compatible compiler.\n  Binary compatibility will only be supported after Ceylon 1.0.\n  Offending class: com.redhat.ceylon.compiler.java.test.bc.JavaOldVersion"));
    }
    
    @Test
    public void testBinaryVersionIncompatibleModule() throws IOException {
        CompilationTask compiler = compileJava("binaryVersionOld/module_.java");
        Assert.assertTrue(compiler.call());
        
        // so now make a jar containing the java module
        File jarFolder = new File(destDir, "com/redhat/ceylon/compiler/java/test/bc/binaryVersionOld/1/");
        jarFolder.mkdirs();
        File jarFile = new File(jarFolder, "com.redhat.ceylon.compiler.java.test.bc.binaryVersionOld-1.jar");
        // now jar it up
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        ZipEntry entry = new ZipEntry("com/redhat/ceylon/compiler/java/test/bc/binaryVersionOld/module_.class");
        outputStream.putNextEntry(entry);
        
        File javaClass = new File(destDir, "com/redhat/ceylon/compiler/java/test/bc/binaryVersionOld/module_.class");
        FileInputStream inputStream = new FileInputStream(javaClass);
        Util.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
        
        assertErrors("binaryVersion/module", new CompilerError(21, "This module was compiled for an incompatible version of the Ceylon compiler (0.0).\n"
                +"  This compiler supports 3.0.\n"
                +"  Please try to recompile your module using a compatible compiler.\n"
                +"  Binary compatibility will only be supported after Ceylon 1.0."));
    }

    private CompilationTask compileJava(String... sourcePaths) {
        java.util.List<File> sourceFiles = new ArrayList<File>(sourcePaths.length);
        for(String file : sourcePaths){
            sourceFiles.add(new File(getPackagePath(), file));
        }

        JavaCompiler runCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager runFileManager = runCompiler.getStandardFileManager(null, null, null);

        // make sure the destination repo exists
        new File(destDir).mkdirs();

        List<String> options = new LinkedList<String>();
        options.addAll(Arrays.asList("-sourcepath", getSourcePath(), "-d", destDir));
        Iterable<? extends JavaFileObject> compilationUnits1 =
                runFileManager.getJavaFileObjectsFromFiles(sourceFiles);
        return runCompiler.getTask(null, runFileManager, null, 
                options, null, compilationUnits1);
    }
}
