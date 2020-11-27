/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.SystemCall;

/**
 *
 * @author parke
 */
public class Test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        
        SystemCallPluginLoader.loadDefaultPlugins();
        
//        String path = "org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls";
//
//        try {
//            SystemCallPluginClassLoader loader = new SystemCallPluginClassLoader(new URL[]{new File(MIPS.JAR_PATH).toURI().toURL()}, path, Test.class.getClassLoader(), null);
//            
//        } catch (Exception ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}
