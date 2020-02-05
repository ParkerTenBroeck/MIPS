/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import static Compiler.ASMCompiler.error;
/**
 *
 * @author parke
 */
public class StringToOpcode {

    public static int stringToOpcode(String instruction, int index) {

        String opCodeString = "";
        String[] parameter = null;

        if (instruction == null || instruction.equals("")) {
            error("instruction null");
        } else {
            try {
                opCodeString = instruction.split(" ")[0];
                opCodeString = opCodeString.trim();
                parameter = instruction.substring(opCodeString.length()).split(",");
                for (int i = 0; i < parameter.length; i++) {
                    
                    parameter[i] = parameter[i].trim();
                    parameter[i] = ASMCompiler.findAndReplacePreProcessorValue(parameter[i]);
                    
                }
            } catch (Exception e) {
                error("Invalid parameters");
                return 0;
            }
        }

        switch (opCodeString) {

            //arithmetic and logical instructions
            case "add":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100000);

            case "addu":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100001);

            case "addi":
                return immediateEncoding(0B001000,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            case "addiu":
                return immediateEncoding(0B001001,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            case "and":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100100);

            case "andi":
                return immediateEncoding(0B001100,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            case "div":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        decodeRegister(parameter[1]),
                        0, 0, 0B011010);

            case "divu":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        decodeRegister(parameter[1]),
                        0, 0, 0B011011);

            case "mult":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        decodeRegister(parameter[1]),
                        0, 0, 0B011000);

            case "multu":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        decodeRegister(parameter[1]),
                        0, 0, 0B11001);

            case "nor":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100111);

            case "or":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100101);

            case "ori":
                return immediateEncoding(0B001101,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            case "sll":
                return registerEncoding(0,
                        0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]),
                        0B000000);
            case "sllv":
                return registerEncoding(0,
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        0, 0B000100);

            case "sra":
                return registerEncoding(0,
                        0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]),
                        0B000011);

            case "srav":
                return registerEncoding(0,
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        0, 0B000111);

            case "srl":
                return registerEncoding(0,
                        0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]),
                        0B000010);

            case "srlv":
                return registerEncoding(0,
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        0, 0B000110);

            case "sub":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100010);

            case "subu":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100011);

            case "xor":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B100110);

            case "xori":
                return immediateEncoding(0B001110,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            //constant manipulating instructions
            case "lhi":
                return immediateEncoding(0B011001,
                        decodeRegister(parameter[0]),
                        0,
                        decodeImmediateValue(parameter[1]));

            case "llo":
                return immediateEncoding(0B011000,
                        decodeRegister(parameter[0]),
                        0,
                        decodeImmediateValue(parameter[1]));

            //comparison instruction
            case "slt":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B101010);

            case "sltu":
                return registerEncoding(0,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        0, 0B101001);

            case "slti":
                return immediateEncoding(0B001010,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            case "sltiu":
                return immediateEncoding(0B001001,
                        decodeRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[2]));

            //branch instructions
            case "beq":
                return immediateEncoding(0B000100,
                        decodeRegister(parameter[0]),
                        decodeRegister(parameter[1]),
                        decodeMemoryPointerJump(parameter[2]));

            case "bgtz":
                return immediateEncoding(0B000111,
                        decodeRegister(parameter[0]),
                        0,
                        decodeMemoryPointerJump(parameter[1]));

            case "blez":
                return immediateEncoding(0B000110,
                        decodeRegister(parameter[0]),
                        0,
                        decodeMemoryPointerJump(parameter[1]));

            case "bne":
                return immediateEncoding(0B000101,
                        decodeRegister(parameter[0]),
                        decodeRegister(parameter[1]),
                        decodeMemoryPointerJump(parameter[2]));

            //jump instructions
            case "j":
                return jumpEncoding(0B000010,
                        decodeMemoryPointerJump(parameter[0]));

            case "jal":
                return jumpEncoding(0B000011,
                        decodeMemoryPointerJump(parameter[0]));

            case "jalr":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        0, 0, 0, 0B001001);

            case "jr":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        0, 0, 0, 0B001000);

            //load instruction
            case "lb":
                return immediateEncoding(0B100000,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lbu":
                return immediateEncoding(0B100100,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lh":
                return immediateEncoding(0B100001,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lhu":
                return immediateEncoding(0B100101,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lw":
                return immediateEncoding(0B100011,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            //store instructions
            case "sb":
                return immediateEncoding(0B101000,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "sh":
                return immediateEncoding(0B101001,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "sw":
                return immediateEncoding(0B101011,
                        decodeMemoryOpRegister(parameter[1]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            //data movement instructions
            case "mfhi":
                return registerEncoding(0, 0, 0,
                        decodeRegister(parameter[0]),
                        0, 0B010000);

            case "mflo":
                return registerEncoding(0, 0, 0,
                        decodeRegister(parameter[0]),
                        0, 0B010010);

            case "mthi":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        0, 0, 0, 0B001001);

            case "mtlo":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        0, 0, 0, 0B001011);

            //system calls
            case "trap":
                return jumpEncoding(0B011010, decodeImmediateValue(parameter[0]));

            default:
                error("Invalid opcode");
                return -1;
        }
        //return -1;
    }

    private static int registerEncoding(int o, int s, int t, int d, int a, int f) {
        o = o & 0B111111;
        s = s & 0B11111;
        t = t & 0B11111;
        d = d & 0B11111;
        a = a & 0B11111;
        f = f & 0B111111;

        return (o << 26) | (s << 21) | (t << 16) | (d << 11) | (a << 6) | f;
    }

    private static int immediateEncoding(int o, int s, int t, int i) {
        o = o & 0B111111;
        s = s & 0B11111;
        t = t & 0B11111;
        i = i & 0xFFFF;

        return (o << 26) | (s << 21) | (t << 16) | i;

    }

    private static int jumpEncoding(int o, int i) {
        o = o & 0B111111;
        i = i & 0x3FFFFFF;

        return (o << 26) | i;
    }

    public static int decodeRegister(String parameter) {
        if (!parameter.contains("$")) {
            ASMCompiler.error("Invalid register");
            return 0;
        }
        try {
            return decodeInt(parameter.replace("$", ""));
        } catch (Exception e) {
            ASMCompiler.error("Invalid register number");
        }
        return 0;
    }

    private static int decodeImmediateValue(String parameter) {
        try {
            return decodeInt(parameter);
        } catch (Exception e) {
            ASMCompiler.error("Invalid immediate value");
        }
        return 0;
    }

    private static int decodeMemoryPointerJump(String parameter) {
        
        try{
            int temp = decodeInt(parameter);
            return temp;
        }catch(Exception e){
            
        }
        
        return ASMCompiler.getRelativeIndexOffset(parameter.trim());
    }

    private static int decodeMemoryOpPointerAddress(String parameter) {
        try {
            return ASMCompiler.getIndex(parameter.split("\\(")[0]);
        } catch (Exception e) {
            ASMCompiler.error("Invalid Memory Pointer");
        }
        return 0;
    }

    private static int decodeMemoryOpRegister(String parameter) {
        try {
            return decodeRegister(parameter.split("\\(")[1].split("\\)")[0]);
        } catch (Exception e) {
            ASMCompiler.error("Brackets not closed");
        }
        return 0;
    }

    private static int decodeInt(String string) { //to add functionality later

        if (string.contains("x")) {
            return Integer.parseInt(string.split("x")[1], Integer.parseInt(string.split("x")[0]));
        }

        return Integer.parseInt(string.trim());
    }
}
