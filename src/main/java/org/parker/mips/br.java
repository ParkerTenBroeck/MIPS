/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author parke
 */
public class br extends URLClassLoader {

    public br() throws MalformedURLException {
        super(new URL[]{new File(MIPS.JAR_PATH).toURI().toURL()});
    }

    @Override
    public Class<?> loadClass(String string, boolean bool) throws ClassNotFoundException {
        System.out.println(string);
        return super.loadClass(string, bool);
    }

    @Override
    public Class<?> loadClass(String string) throws ClassNotFoundException {
        System.out.println(string);
        return super.loadClass(string);
    }

    @Override
    public Class<?> findClass(String string) throws ClassNotFoundException {
        System.out.println(string);
        return super.findClass(string);
    }

}
