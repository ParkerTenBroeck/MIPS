/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.assembleride.plugin.exceptions;

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
