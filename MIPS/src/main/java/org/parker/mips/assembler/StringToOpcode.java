/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler;

import org.parker.mips.assembler.data.AbstractArgumentList;
import org.parker.mips.assembler.data.UserLine;

import java.nio.ByteBuffer;

/**
 *
 * @author parke
 */
public class StringToOpcode {

    //private static int currentOpCodeLine;
    private static UserLine currentLine;
    private static int currentByteIndex;

    public static final String[] INSTRUCTIONS = new String[]{"add", "addu", "addi", "addiu", "and", "andi", "div", "divu", "mult", "multu", "nor", "or", "ori", "sll", "sllv", "sra", "srav", "srl", "srlv", "sub", "subu", "xor", "xori", "lhi", "llo", "slt", "sltu", "slti", "sltiu", "beq", "bgtz", "ble", "bne", "j", "jal", "jalr", "jr", "lb", "lbu", "lh", "lhu", "lw", "sb", "sh", "sw", "mfhi", "mflo", "mthi", "mtlo", "trap"};

    public static byte[] stringToOpcode(AssembleTimeUserLine ctul) throws InvalidOpCodeException, InvalidArgumentsException {

        String instruction = ctul.ul.line;
        currentLine = ctul.ul;
        currentByteIndex = ctul.startingByteAddress;

        String opCodeString = "";
        String[] parameter = null;

        if (instruction == null || instruction.equals("")) {
            throw new InvalidOpCodeException("Instruction null?", ctul.ul);
        } else {
            opCodeString = instruction.split(" ")[0];
            
            if(opCodeString.length() == -1 || instruction.length() == -1){
                throw new InvalidOpCodeException( "opCode/instruction Length is -1? invalid opCode / character", ctul.ul);
            }
            if(opCodeString.length() == instruction.length()){
                throw new InvalidOpCodeException("opCode is same length as instruction? invalid character or no parameters given", ctul.ul);
            }

            if (instruction.endsWith(")")) {
                instruction = instruction.substring(0, instruction.length() - 1);
            }
            AbstractArgumentList aal = new AbstractArgumentList(instruction.substring(opCodeString.length()), new char[]{',', '(', ')'});
            parameter = aal.args;
            opCodeString = opCodeString.trim();
        }
        if (getNumberOfArguments(new UserLine(opCodeString, ctul.ul.realLineNumber)) != parameter.length) {
            throw new org.parker.mips.assembler.InvalidArgumentsException("Wrong number of arguments used for (" + opCodeString + ") needed "
                    + getNumberOfArguments(new UserLine(opCodeString, ctul.ul.realLineNumber))
                    + " found " + parameter.length, currentLine);
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
                        0,
                        decodeRegister(parameter[0]),
                        decodeImmediateValue(parameter[1]));

            case "llo":
                return immediateEncoding(0B011000, 
                        0,
                        decodeRegister(parameter[0]),
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
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lbu":
                return immediateEncoding(0B100100,
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lh":
                return immediateEncoding(0B100001,
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lhu":
                return immediateEncoding(0B100101,
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "lw":
                return immediateEncoding(0B100011,
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            //store instructions
            case "sb":
                return immediateEncoding(0B101000,
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "sh":
                return immediateEncoding(0B101001,
                        decodeMemoryOpRegister(parameter[2]),
                        decodeRegister(parameter[0]),
                        decodeMemoryOpPointerAddress(parameter[1]));

            case "sw":
                return immediateEncoding(0B101011,
                        decodeMemoryOpRegister(parameter[2]),
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
                        0, 0, 0, 0B010001);

            case "mtlo":
                return registerEncoding(0,
                        decodeRegister(parameter[0]),
                        0, 0, 0, 0B010011);

            //system calls
            case "trap":
                return jumpEncoding(0B011010, decodeImmediateValue(parameter[0]));

            default:
                throw new InvalidOpCodeException(ctul.ul);
        }
        //return -1;
    }

    private static byte[] registerEncoding(int o, int s, int t, int d, int a, int f) {
        o = o & 0B111111;
        s = s & 0B11111;
        t = t & 0B11111;
        d = d & 0B11111;
        a = a & 0B11111;
        f = f & 0B111111;

        int temp = (o << 26) | (s << 21) | (t << 16) | (d << 11) | (a << 6) | f;
        return ByteBuffer.allocate(4).putInt(temp).array();
    }

    private static byte[] immediateEncoding(int o, int s, int t, int i) {
        o = o & 0B111111;
        s = s & 0B11111;
        t = t & 0B11111;
        i = i & 0xFFFF;

        int temp = (o << 26) | (s << 21) | (t << 16) | i;
        return ByteBuffer.allocate(4).putInt(temp).array();

    }

    private static byte[] jumpEncoding(int o, int i) {
        o = o & 0B111111;
        i = i & 0x3FFFFFF;

        int temp = (o << 26) | i;
        return ByteBuffer.allocate(4).putInt(temp).array();
    }

    private static int decodeRegister(String parameter) throws InvalidArgumentsException {
        if (!parameter.contains("$")) {
            throw new org.parker.mips.assembler.InvalidArgumentsException("Invalid register does not contain $", currentLine);
        }
        try {
            return Assembler.parseInt(parameter.replace("$", ""));
        } catch (Exception e) {
            throw new org.parker.mips.assembler.InvalidArgumentsException("Invalid register number", currentLine, e);
            //Assembler.ArgumentError("Invalid register number", currentOpCodeLine);
        }
    }

    private static int decodeImmediateValue(String parameter) throws InvalidArgumentsException {
        try {
            return Assembler.parseInt(parameter);
        } catch (Exception e) {

            int num = Assembler.getByteIndexOfMemoryLable(parameter.trim(), currentLine.realLineNumber);
            if (num == -1) {
                throw new org.parker.mips.assembler.InvalidArgumentsException("Invalid register number", currentLine, e);
            }
            return num;
        }
    }

    private static int decodeMemoryPointerJump(String parameter) {

        try {
            int temp = Assembler.parseInt(parameter);
            return temp;
        } catch (Exception e) {
            
        }
        return ((Assembler.getByteIndexOfMemoryLable(parameter.trim(), currentLine.realLineNumber) - currentByteIndex) >> 2) - 1;
    }

    private static int decodeMemoryOpPointerAddress(String parameter) throws InvalidArgumentsException {

        try {
            int temp = Assembler.parseInt(parameter);
            return temp;
        } catch (Exception e) {

        }

        try {
            return (Assembler.getByteIndexOfMemoryLable(parameter, currentLine.realLineNumber));
        } catch (Exception e) {
            throw new org.parker.mips.assembler.InvalidArgumentsException("Invalid Memory Pointer", currentLine, e);
        }
    }

    private static int decodeMemoryOpRegister(String parameter) throws InvalidArgumentsException {
        try {
            return decodeRegister(parameter);
        } catch (Exception e) {
            throw new InvalidArgumentsException("Brackets not closed or Invalid Register", currentLine, e);
        }
    }

    public static int getInstructionSize(UserLine ul) { //to be implemented
        return 4;
    }

    public static int getNumberOfArguments(UserLine ul) throws InvalidOpCodeException {
        switch (ul.line) {

            //arithmetic and logical instructions
            case "add":
                return 3;

            case "addu":
                return 3;

            case "addi":
                return 3;

            case "addiu":
                return 3;

            case "and":
                return 3;

            case "andi":
                return 3;

            case "div":
                return 2;

            case "divu":
                return 2;

            case "mult":
                return 2;

            case "multu":
                return 2;

            case "nor":
                return 3;

            case "or":
                return 3;

            case "ori":
                return 3;

            case "sll":
                return 3;
            case "sllv":
                return 3;

            case "sra":
                return 3;

            case "srav":
                return 3;

            case "srl":
                return 3;

            case "srlv":
                return 3;

            case "sub":
                return 3;

            case "subu":
                return 3;

            case "xor":
                return 3;

            case "xori":
                return 3;

            //constant manipulating instructions
            case "lhi":
                return 2;

            case "llo":
                return 2;

            //comparison instruction
            case "slt":
                return 3;

            case "sltu":
                return 3;

            case "slti":
                return 3;

            case "sltiu":
                return 3;

            //branch instructions
            case "beq":
                return 3;

            case "bgtz":
                return 2;

            case "blez":
                return 2;

            case "bne":
                return 3;

            //jump instructions
            case "j":
                return 1;

            case "jal":
                return 1;

            case "jalr":
                return 1;

            case "jr":
                return 1;

            //load instruction
            case "lb":
                return 3;

            case "lbu":
                return 3;

            case "lh":
                return 3;
            case "lhu":
                return 3;

            case "lw":
                return 3;

            //store instructions
            case "sb":
                return 3;

            case "sh":
                return 3;

            case "sw":
                return 3;

            //data movement instructions
            case "mfhi":
                return 1;

            case "mflo":
                return 1;

            case "mthi":
                return 1;

            case "mtlo":
                return 1;

            //system calls
            case "trap":
                return 1;

            default:
                throw new InvalidOpCodeException(ul);
        }
    }
}
