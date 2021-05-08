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
package org.parker.mips.architecture.assembler;

import org.parker.retargetableassembler.base.DataStatement;
import org.parker.mips.architecture.assembler.instructions.parser.MipsInstructionParser;
import org.parker.retargetableassembler.base.assembler.BaseAssembler;
import org.parker.retargetableassembler.base.preprocessor.BasePreProcessor;
import org.parker.retargetableassembler.instruction.InstructionParser;

import java.util.logging.Logger;

public class MipsAssembler extends BaseAssembler {

    private static final Logger LOGGER = Logger.getLogger(MipsAssembler.class.getName());

    private static final InstructionParser instructionParser = new MipsInstructionParser();

    @Override
    protected DataStatement getInstruction(String mnemonic) {
        return instructionParser.newInstance(mnemonic, this);
    }

    @Override
    protected BasePreProcessor createPreProcessor() {
        return new MipsPreProcessor(this);
    }
}
