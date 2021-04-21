package org.parker.mips.architectures.mips.assembler;

import org.parker.mips.assembler.base.preprocessor.BaseExpressionCompiler;
import org.parker.mips.assembler.base.preprocessor.BasePreProcessor;
import org.parker.mips.assembler.util.ExpressionCompiler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class MipsPreProcessor extends BasePreProcessor<MipsAssembler> {

    private static final Logger LOGGER = Logger.getLogger(MipsPreProcessor.class.getName());

    public MipsPreProcessor(MipsAssembler assembler) {
        super(assembler);
    }

    @Override
    protected BaseExpressionCompiler getExpressionCompiler() {
        return new MipsExpressionCompiler();
    }

    private static final Set<String> memoryAccessFormattedInstructionMnemonics = new HashSet<>();

    static{
        memoryAccessFormattedInstructionMnemonics.add("lb");
        memoryAccessFormattedInstructionMnemonics.add("lbu");
        memoryAccessFormattedInstructionMnemonics.add("lh");
        memoryAccessFormattedInstructionMnemonics.add("lhu");
        memoryAccessFormattedInstructionMnemonics.add("lw");
        memoryAccessFormattedInstructionMnemonics.add("sb");
        memoryAccessFormattedInstructionMnemonics.add("sh");
        memoryAccessFormattedInstructionMnemonics.add("sw");
    }

    @Override
    protected String preProcessAssemblyArguments(String instructionMnemonic, String arguments) {
        if (memoryAccessFormattedInstructionMnemonics.contains(instructionMnemonic) &&
                ExpressionCompiler.countTopLevelExpressions(arguments) == 2) {
            char[] chs = arguments.toCharArray();
            int index = chs.length - 1;

            int start = 0;
            int end = 0;

            while (chs[index] != ')') index--;
            start = index;

            int into = 0;
            boolean string = false;
            while (index >= 0 && index < chs.length) {
                if (chs[index] == ')' && !string) {
                    into++;
                } else if (chs[index] == '(' && !string) {
                    into--;
                    if (into == 0) {
                        end = index;
                        break;
                    }
                } else if (chs[index] == '"') {
                    string = !string;
                } else if (chs[index] == '\\' && string) {
                    if (chs[index + 1] == '"') {
                        index -= 2;
                        continue;
                    }
                }

                index--;
            }

            chs[start] = ' ';
            chs[end] = ',';
            return new String(chs);
        }else{
            return arguments;
        }
    }
}
