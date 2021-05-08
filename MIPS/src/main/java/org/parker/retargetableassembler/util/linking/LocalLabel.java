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
package org.parker.retargetableassembler.util.linking;

import org.parker.retargetableassembler.util.Line;

public class LocalLabel extends Label{

    private final long address;
    private final AssemblyUnit au;

    public LocalLabel(long address, AssemblyUnit au, String mnemonic, Line line) {
        super(mnemonic, line);
        this.address = address;
        this.au = au;
    }

    @Override
    public long getAddress() {
        return address + au.getStartingAddress();
    }
}
