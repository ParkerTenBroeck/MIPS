/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

/**
 *
 * @author parke
 */
public interface Plugin {
    
    public void onLoad();
    
    public boolean onUnload();
    
    public void onEnable();
    
    public void onDissable();
}
