package org.parker.mips.assembler.mips;

import org.parker.mips.assembler.base.preprocessor.BaseExpressionCompiler;
import org.parker.mips.assembler.util.Register;

import java.util.HashMap;

public class MipsExpressionCompiler extends BaseExpressionCompiler<MipsAssembler, MipsPreProcessor> {

    private static final HashMap<String, Register> regMap = new HashMap<>();

    static{
        regMap.put("$0", new Register(0));
        regMap.put("$1", new Register(1));
        regMap.put("$2", new Register(2));
        regMap.put("$3", new Register(3));
        regMap.put("$4", new Register(4));
        regMap.put("$5", new Register(5));
        regMap.put("$6", new Register(6));
        regMap.put("$7", new Register(7));
        regMap.put("$8", new Register(8));
        regMap.put("$9", new Register(9));
        regMap.put("$10", new Register(10));
        regMap.put("$11", new Register(11));
        regMap.put("$12", new Register(12));
        regMap.put("$13", new Register(13));
        regMap.put("$14", new Register(14));
        regMap.put("$15", new Register(15));
        regMap.put("$16", new Register(16));
        regMap.put("$17", new Register(17));
        regMap.put("$18", new Register(18));
        regMap.put("$19", new Register(19));
        regMap.put("$20", new Register(20));
        regMap.put("$21", new Register(21));
        regMap.put("$22", new Register(22));
        regMap.put("$23", new Register(23));
        regMap.put("$24", new Register(24));
        regMap.put("$25", new Register(25));
        regMap.put("$26", new Register(26));
        regMap.put("$27", new Register(27));
        regMap.put("$28", new Register(28));
        regMap.put("$29", new Register(29));
        regMap.put("$30", new Register(30));
        regMap.put("$31", new Register(31));
    }

    @Override
    protected Object parseVariable(String token) {
        if(regMap.containsKey(token)){
            return regMap.get(token);
        }else{
            return super.parseVariable(token);
        }
    }
}
