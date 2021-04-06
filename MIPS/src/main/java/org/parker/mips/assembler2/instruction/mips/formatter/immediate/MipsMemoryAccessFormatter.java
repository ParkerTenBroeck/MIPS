package org.parker.mips.assembler2.instruction.mips.formatter.immediate;

import org.parker.mips.assembler2.instruction.mips.formatter.MipsImmediateFormatter;

public interface MipsMemoryAccessFormatter extends MipsImmediateFormatter {

    @Override
    default String modifyOperandExpression(String expression) {
        char[] chs = expression.toCharArray();
        int index = chs.length - 1;

        int start = 0;
        int end = 0;

        while(chs[index] != ')') index --;
        start = index;

        int into = 0;
        boolean string = false;
        while(index >= 0 && index < chs.length){
            if(chs[index] == ')'&& !string){
                into ++;
            }else if(chs[index] == '(' && !string){
                into --;
                if(into == 0){
                    end = index;
                    break;
                }
            }else if(chs[index] == '"'){
                string = !string;
            }else if(chs[index] == '\\' && string){
                if(chs[index + 1] == '"'){
                    index -= 2;
                    continue;
                }
            }

            index --;
        }

        chs[start] = ' ';
        chs[end] = ',';
        return new String(chs);
    }
}
