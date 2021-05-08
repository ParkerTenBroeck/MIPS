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
package org.parker.retargetableassembler.debugger;

import org.parker.retargetableassembler.util.Line;

import java.io.Serializable;

public class FinalizedLabel implements Serializable {

    final Line line;
    final String mnemonic;
    final long address;

    public FinalizedLabel(String mnemonic, Line line, long address) {
        this.line = line;
        this.mnemonic = mnemonic;
        this.address = address;
    }

    public long getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FinalizedLabel){
            if(mnemonic.equals(((FinalizedLabel) obj).mnemonic)
            && line.getFile().getAbsolutePath().equals(((FinalizedLabel) obj).line.getFile().getAbsolutePath())
            && getAddress() == ((FinalizedLabel) obj).getAddress()){
                return true;
            }
        }else if(obj instanceof String){
            if(obj.equals(mnemonic + ": " + line.getFile().getAbsolutePath())){
                return true;
            }
        }
        return super.equals(obj);
    }
}
