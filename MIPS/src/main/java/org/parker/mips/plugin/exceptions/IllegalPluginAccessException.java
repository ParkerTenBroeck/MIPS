/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.exceptions;

/**
 * Thrown when a plugin attempts to interact with the server when it is not
 * enabled
 */
@SuppressWarnings("serial")
public class IllegalPluginAccessException extends RuntimeException {

    /**
     * Creates a new instance of <code>IllegalPluginAccessException</code>
     * without detail message.
     */
    public IllegalPluginAccessException() {
    }

    /**
     * Constructs an instance of <code>IllegalPluginAccessException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalPluginAccessException(String msg) {
        super(msg);
    }
}
