package org.parker.mips.assembler2.util;

import org.parker.mips.assembler2.operand.OpImmediate;
import org.parker.mips.assembler2.operand.OpLabel;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

import java.util.HashMap;
import java.util.Map;

public class DataOperandExpressionParser extends ExpressionParser{

    private final HashMap<String, Label> labelMap = new HashMap<>();

    public DataOperandExpressionParser(Map<String, Label> labelMap){
        this.labelMap.putAll(labelMap);
    }

    @Override
    public Object parse(String str) {

        Object x = super.parse(str);
        Object[] y;
        if(!(x instanceof Object[])){
            y = new Object[]{x};
        }else{
            y = (Object[]) x;
        }
        Operand[] ops = new Operand[y.length];
        for(int i = 0; i < ops.length; i ++){
            if(y[i] instanceof Register){
                ops[i] = new OpRegister(((Register) y[i]).regNumber);
            }else if(y[i] instanceof Integer || y[i] instanceof Long || y[i] instanceof Byte || y[i] instanceof Short){
                ops[i] = new OpImmediate(((Number) y[i]).longValue());
            }else if(y[i] instanceof Label){
                ops[i] = new OpLabel(((Label) y[i]).mnemonic);
            }
            //ops[i] = (Operand) y[i];
        }
        return ops;
    }


    private static HashMap<String, Register> regMap = new HashMap<>();

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

    protected Object parseVariable(String token) {
        if(regMap.containsKey(token)){
            return regMap.get(token);
        }else if(labelMap.containsKey(token)){
            return labelMap.get(token);
        }
        throw new IllegalArgumentException("Variable: " + token + " not found");
    }
}
