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
package org.parker.retargetableassembler.exception;

import org.parker.retargetableassembler.util.linking.Label;

public class IllegalLabelReference extends LinkingException{

    private final Label label;
    private final Label reference;

    public IllegalLabelReference(Label label, Label reference){
        this.label = label;
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Cannot reference \n" + reference.toString() + "\nwith " + label.toString();
    }
}
