/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor;

import static org.parker.mips.Processor.Memory.getByte;
import static org.parker.mips.Processor.Memory.getHalfWord;
import static org.parker.mips.Processor.Memory.getWord;
import static org.parker.mips.Processor.Registers.getHigh;
import static org.parker.mips.Processor.Registers.getLow;
import static org.parker.mips.Processor.Registers.getPc;
import static org.parker.mips.Processor.Registers.getRegister;
import static org.parker.mips.Processor.Registers.setHigh;
import static org.parker.mips.Processor.Registers.setLow;
import static org.parker.mips.Processor.Registers.setPc;
import static org.parker.mips.Processor.Registers.setRegister;
import static org.parker.mips.Processor.SystemCallHandler.SystemCall;

/**
 *
 * @author parke
 */
public class InstructionDecode {

    private static final instruction[] instructionLookUp = new instruction[64];
    private static final instruction[] regInstructionLookUp = new instruction[64];

    static {
        instructionLookUp[0B000000] = new instruction() { //Register
            @Override
            void runInstruction(int opCode) {
                int f = opCode & 0B111111;
                regInstructionLookUp[f].runInstruction(opCode);
            }
        };

        //Jump Encoding
        instructionLookUp[0B000010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int i = (opCode << 6) >> 6;
                //Jump
                setPc(getPc() + (i << 2));
            }
        };
        instructionLookUp[0B000011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int i = (opCode << 6) >> 6;
                //jal
                setRegister(31, getPc());
                setPc(getPc() + (i << 2));
            }
        };
        instructionLookUp[0B011010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int i = (opCode << 6) >> 6;
                //trap
                SystemCall(i);
            }
        };
        //Immediate Encoding

        //arthmetic
        instructionLookUp[0B001000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //addi
                setRegister(t, getRegister(s) + SEi);
            }
        };

        instructionLookUp[0B001001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //addiu
                setRegister(t, (int) (getUnsignedInt(getRegister(s)) + getUnsignedInt(SEi)));
            }
        };

        instructionLookUp[0B001100] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //andi
                setRegister(t, getRegister(s) & ZEi);
            }
        };

        instructionLookUp[0B001101] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //ori
                setRegister(t, getRegister(s) | ZEi);
            }
        };

        instructionLookUp[0B001110] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //xori
                setRegister(t, getRegister(s) ^ ZEi);
            }
        };

        //constant manupulating inctructions
        instructionLookUp[0B011001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //lhi
                setRegister(t, (getRegister(t) & 0xFFFF) | ((ZEi << 16) & 0xFFFF0000));
            }
        };

        instructionLookUp[0B011000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //llo
                setRegister(t, (getRegister(t) & 0xFFFF0000) | (ZEi & 0xFFFF));
            }
        };

        //comparison instructions
        instructionLookUp[0B001010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //slti
                setRegister(t, SEi - getRegister(s));
            }
        };

//        instructionLookUp[0B001001] = new instruction() {
//            @Override
//            void runInstruction(int opCode) {
//                int s = (opCode >>> 21) & 0B11111;
//                int t = (opCode >>> 16) & 0B11111;
//                int SEi = ((opCode) << 16) >> 16;
//                //sltiu
//                setRegister(t, (int) (getUnsignedInt(SEi) - getUnsignedInt(getRegister(s))));   //conflict with addiu
//            }
//        };
        //branch instructions 
        instructionLookUp[0B000100] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //beq
                if (getRegister(s) == getRegister(t)) {
                    setPc(getPc() + (SEi << 2));
                }
            }
        };

        instructionLookUp[0B000111] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //bgtz
                if (getRegister(s) > 0) {
                    setPc(getPc() + (SEi << 2));
                }
            }
        };

        instructionLookUp[0B000110] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //blez
                if (getRegister(s) <= 0) {
                    setPc(getPc() + (SEi << 2));
                }
            }
        };

        instructionLookUp[0B000101] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int SEi = ((opCode) << 16) >> 16;
                //bne
                if (getRegister(s) != getRegister(t)) {
                    setPc(getPc() + (SEi << 2));
                }
            }
        };

        //load instrictions
        instructionLookUp[0B100000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //lb
                setRegister(t, (int) getByte(getRegister(s) + ZEi));
            }
        };

        instructionLookUp[0B100100] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //lbu
                setRegister(t, getByte(getRegister(s) + ZEi) & 0xFF);
            }
        };

        instructionLookUp[0B100001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //lh
                setRegister(t, (int) getHalfWord(getRegister(s) + ZEi));
            }
        };

        instructionLookUp[0B100101] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //lhu
                setRegister(t, getHalfWord(getRegister(s) + ZEi) & 0xFFFF);
            }
        };

        instructionLookUp[0B100011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //lw
                setRegister(t, getWord(getRegister(s) + ZEi));
            }
        };

        //store instrictions
        instructionLookUp[0B101000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //sb
                Memory.setByte(getRegister(s) + ZEi, getRegister(t));
            }
        };

        instructionLookUp[0B101001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //sh
                Memory.setHalfWord(getRegister(s) + ZEi, getRegister(t));
            }
        };

        instructionLookUp[0B101011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >>> 21) & 0B11111;
                int t = (opCode >>> 16) & 0B11111;
                int ZEi = (opCode & 0xFFFF);
                //sw
                Memory.setWord(getRegister(s) + ZEi, getRegister(t));
            }
        };

        //registerEncoding
        //arithmatic
        regInstructionLookUp[0B100000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                //add
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                setRegister(d, getRegister(s) + getRegister(t));
            }
        };
        regInstructionLookUp[0B100000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //addu
                setRegister(d, (int) (getUnsignedInt(getRegister(s)) + getUnsignedInt(getRegister(t))));
            }
        };
        regInstructionLookUp[0B100100] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //and
                setRegister(d, getRegister(s) & getRegister(t));
            }
        };

        regInstructionLookUp[0B011010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                //div
                setLow(getRegister(s) / getRegister(t));
                setHigh(getRegister(s) % getRegister(t));
            }
        };

        regInstructionLookUp[0B011011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                //divu
                setLow((int) (getUnsignedInt(getRegister(s)) / getUnsignedInt(getRegister(t))));
                setHigh((int) (getUnsignedInt(getRegister(s)) % getUnsignedInt(getRegister(t))));
            }
        };

        regInstructionLookUp[0B011000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                //mult
                setLow(getRegister(s) * getRegister(t));
                setHigh((int) ((long) getRegister(s) * (long) getRegister(t) >> 32));
            }
        };

        regInstructionLookUp[0B011001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                //multu
                long result = (getUnsignedInt(getRegister(s)) * getUnsignedInt(getRegister(t)));
                setLow((int) (result & 0xFFFFFFFF));
                setHigh((int) (result >> 32));
            }
        };

        regInstructionLookUp[0B100111] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //nor
                setRegister(d, ~(getRegister(s) | getRegister(t)));
            }
        };

        regInstructionLookUp[0B100101] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //or
                setRegister(d, (getRegister(s) | getRegister(t)));
            }
        };

        regInstructionLookUp[0B000000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                int a = (opCode >> 6) & 0B11111;
                //sll
                setRegister(d, getRegister(t) << a);
            }
        };

        regInstructionLookUp[0B000100] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //sllv
                setRegister(d, getRegister(t) << getRegister(s));
            }
        };

        regInstructionLookUp[0B000011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                int a = (opCode >> 6) & 0B11111;
                //sra
                setRegister(d, getRegister(t) >> a);
            }
        };

        regInstructionLookUp[0B000111] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                int a = (opCode >> 6) & 0B11111;
                //srav
                setRegister(d, getRegister(t) >> getRegister(s));
            }
        };

        regInstructionLookUp[0B000010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                int a = (opCode >> 6) & 0B11111;
                //srl
                setRegister(d, getRegister(t) >>> a);
            }
        };

        regInstructionLookUp[0B000110] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //srlv
                setRegister(d, getRegister(t) >>> getRegister(s));
            }
        };

        regInstructionLookUp[0B100010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //sub
                setRegister(d, getRegister(s) - getRegister(t));
            }
        };

        regInstructionLookUp[0B100011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //subu
                setRegister(d, (int) (getUnsignedInt(getRegister(s)) - getUnsignedInt(getRegister(t))));
            }
        };

        regInstructionLookUp[0B100110] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //xor
                setRegister(d, getRegister(s) ^ getRegister(t));
            }
        };

        //comparasin
        regInstructionLookUp[0B101010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //slt
                setRegister(d, getRegister(t) - getRegister(s));
            }
        };

        regInstructionLookUp[0B101001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                int t = (opCode >> 16) & 0B11111;
                int d = (opCode >> 11) & 0B11111;
                //sltu 
                setRegister(d, (int) (getUnsignedInt(getRegister(t)) - getUnsignedInt(getRegister(s))));
            }
        };

        //jump
        regInstructionLookUp[0B001001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                //jalr
                setRegister(31, getPc());
                setPc(getRegister(s));
            }
        };

        regInstructionLookUp[0B001000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                //jr
                setPc(getRegister(s));
            }
        };

        //dataMovement
        regInstructionLookUp[0B010000] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int d = (opCode >> 11) & 0B11111;
                //mfhi
                setRegister(d, getHigh());
            }
        };

        regInstructionLookUp[0B010010] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int d = (opCode >> 11) & 0B11111;
                //mflo
                setRegister(d, getLow());
            }
        };

        regInstructionLookUp[0B010001] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                //mthi
                setHigh(getRegister(s));
            }
        };

        regInstructionLookUp[0B010011] = new instruction() {
            @Override
            void runInstruction(int opCode) {
                int s = (opCode >> 21) & 0B11111;
                //mtlo
                setLow(getRegister(s));
            }
        };

    }

    private static void incPc() {
        setPc(getPc() + 4);
    }

    public static boolean runInstruction(int opCode) {
        int o = (opCode >>> 26) & 0B111111;
        incPc();
        try {
            instructionLookUp[o].runInstruction(opCode);
        } catch (Exception e) {
            return false;
        }
        return true;
//        int o = (opCode >>> 26) & 0B111111;
//
//        switch (o) {
//
//            case 0B000000: //Register
//                return registerEncoding(opCode);
//
//            case 0B000010: //jump
//            case 0B000011:
//            case 0B011010:
//                return jumpEncoding(opCode);
//
//            default: //Immediate
//                return immediateEncoding(opCode);
//
//        }
    }

//    private static boolean registerEncoding(int opCode) {
//        int s = (opCode >> 21) & 0B11111;
//        int t = (opCode >> 16) & 0B11111;
//        int d = (opCode >> 11) & 0B11111;
//        int a = (opCode >> 6) & 0B11111;
//        int f = opCode & 0B111111;
//
//        incPc();
//
//        switch (f) {
//            //arithmatic
//            case 0B100000: //add
//                setRegister(d, getRegister(s) + getRegister(t));
//                break;
//
//            case 0B100001: //addu
//                setRegister(d, (int) (getUnsignedInt(getRegister(s)) + getUnsignedInt(getRegister(t))));
//                break;
//
//            case 0B100100: //and
//                setRegister(d, getRegister(s) & getRegister(t));
//                break;
//
//            case 0B011010: //div
//                setLow(getRegister(s) / getRegister(t));
//                setHigh(getRegister(s) % getRegister(t));
//                break;
//
//            case 0B011011: //divu
//                setLow((int) (getUnsignedInt(getRegister(s)) / getUnsignedInt(getRegister(t))));
//                setHigh((int) (getUnsignedInt(getRegister(s)) % getUnsignedInt(getRegister(t))));
//                break;
//
//            case 0B011000: //mult
//                setLow(getRegister(s) * getRegister(t));
//                setHigh((int) ((long) getRegister(s) * (long) getRegister(t) >> 32));
//                break;
//
//            case 0B011001: //multu
//            {
//                //System.out.println(getUnsignedInt(getRegister(s)));
//                long result = (getUnsignedInt(getRegister(s)) * getUnsignedInt(getRegister(t)));
//                setLow((int) (result & 0xFFFFFFFF));
//                setHigh((int) (result >> 32));
//                break;
//            }
//
//            case 0B100111: //nor
//                setRegister(d, ~(getRegister(s) | getRegister(t)));
//                break;
//
//            case 0B100101: //or
//                setRegister(d, (getRegister(s) | getRegister(t)));
//                break;
//
//            case 0B000000: //sll
//                setRegister(d, getRegister(t) << a);
//                break;
//
//            case 0B000100: //sllv
//                setRegister(d, getRegister(t) << getRegister(s));
//                break;
//
//            case 0B000011: //sra
//                setRegister(d, getRegister(t) >> a);
//                break;
//
//            case 0B000111: //srav
//                setRegister(d, getRegister(t) >> getRegister(s));
//                break;
//
//            case 0B000010: //srl
//                setRegister(d, getRegister(t) >>> a);
//                break;
//
//            case 0B000110: //srlv
//                setRegister(d, getRegister(t) >>> getRegister(s));
//                break;
//
//            case 0B100010: //sub
//                setRegister(d, getRegister(s) - getRegister(t));
//                break;
//
//            case 0B100011: //subu
//                setRegister(d, (int) (getUnsignedInt(getRegister(s)) - getUnsignedInt(getRegister(t))));
//                break;
//
//            case 0B100110: //xor
//                setRegister(d, getRegister(s) ^ getRegister(t));
//                break;
//
//            //comparasin
//            case 0B101010: //slt
//                setRegister(d, getRegister(t) - getRegister(s));
//                break;
//
//            case 0B101001: //sltu 
//                setRegister(d, (int) (getUnsignedInt(getRegister(t)) - getUnsignedInt(getRegister(s))));
//                break;
//
//            //jump
//            case 0B001001: //jalr
//                setRegister(31, getPc());
//                setPc(getRegister(s));
//                break;
//
//            case 0B001000: //jr
//                setPc(getRegister(s));
//                break;
//
//            //dataMovement
//            case 0B010000: //mfhi
//                setRegister(d, getHigh());
//                break;
//
//            case 0B010010: //mflo
//                setRegister(d, getLow());
//                break;
//
//            case 0B010001: //mthi
//                setHigh(getRegister(s));
//                break;
//
//            case 0B010011: //mtlo
//                setLow(getRegister(s));
//                break;
//
//            default:
//                return false;
//        }
//        return true;
//    }
//
//    private static boolean immediateEncoding(int opCode) {
//        int o = (opCode >>> 26) & 0B111111;
//        int s = (opCode >>> 21) & 0B11111;
//        int t = (opCode >>> 16) & 0B11111;
//        int SEi = ((opCode) << 16) >> 16;
//        int ZEi = (opCode & 0xFFFF);
//
//        incPc();
//
//        switch (o) {
//
//            //arthmetic
//            case 0B001000: //addi
//                setRegister(t, getRegister(s) + SEi);
//                break;
//
//            case 0B001001: //addiu
//                setRegister(t, (int) (getUnsignedInt(getRegister(s)) + getUnsignedInt(SEi)));
//                break;
//
//            case 0B001100: //andi
//                setRegister(t, getRegister(s) & ZEi);
//                break;
//
//            case 0B001101: //ori
//                setRegister(t, getRegister(s) | ZEi);
//                break;
//
//            case 0B001110: //xori
//                setRegister(t, getRegister(s) ^ ZEi);
//                break;
//
//            //constant manupulating inctructions
//            case 0B011001: //lhi
//                setRegister(t, (getRegister(t) & 0xFFFF) | ((ZEi << 16) & 0xFFFF0000));
//                break;
//
//            case 0B011000: //llo
//                setRegister(t, (getRegister(t) & 0xFFFF0000) | (ZEi & 0xFFFF));
//                break;
//
//            //comparison instructions
//            case 0B001010: //slti
//                setRegister(t, SEi - getRegister(s));
//                break;
//
//            //case 0B001001: //sltiu
//            //setRegister(t, (int)(getUnsignedInt(SEi) - getUnsignedInt(getRegister(s))));   //conflict with addiu
//            //break;
//            //branch instructions 
//            case 0B000100: //beq
//                if (getRegister(s) == getRegister(t)) {
//                    setPc(getPc() + (SEi << 2));
//                }
//                break;
//            case 0B000111: //bgtz
//
//                if (getRegister(s) > 0) {
//                    setPc(getPc() + (SEi << 2));
//                }
//                break;
//            case 0B000110: //blez
//                if (getRegister(s) <= 0) {
//                    setPc(getPc() + (SEi << 2));
//                }
//                break;
//            case 0B000101: //bne
//                if (getRegister(s) != getRegister(t)) {
//                    setPc(getPc() + (SEi << 2));
//                }
//                break;
//
//            //load instrictions
//            case 0B100000: //lb
//                setRegister(t, (int) getByte(getRegister(s) + ZEi));
//                break;
//
//            case 0B100100: //lbu
//                setRegister(t, getByte(getRegister(s) + ZEi) & 0xFF);
//                break;
//
//            case 0B100001: //lh
//                setRegister(t, (int) getHalfWord(getRegister(s) + ZEi));
//                break;
//
//            case 0B100101: //lhu
//                setRegister(t, getHalfWord(getRegister(s) + ZEi) & 0xFFFF);
//                break;
//
//            case 0B100011: //lw
//                setRegister(t, getWord(getRegister(s) + ZEi));
//                break;
//
//            //store instrictions
//            case 0B101000: //sb
//                Memory.setByte(getRegister(s) + ZEi, getRegister(t));
//                break;
//
//            case 0B101001: //sh
//                Memory.setHalfWord(getRegister(s) + ZEi, getRegister(t));
//                break;
//
//            case 0B101011: //sw
//                Memory.setWord(getRegister(s) + ZEi, getRegister(t));
//                break;
//
//            default:
//                return false;
//        }
//        return true;
//    }
//
//    private static boolean jumpEncoding(int opCode) {
//        int o = (opCode >>> 26) & 0B111111;
//        int i = (opCode << 6) >> 6;
//
//        incPc();
//
//        switch (o) {
//            case 0B000010: //j
//                setPc(getPc() + (i << 2));
//                break;
//
//            case 0B000011: //jal
//                setRegister(31, getPc());
//                setPc(getPc() + (i << 2));
//                break;
//
//            case 0B011010: //trap
//                SystemCall(i);
//                break;
//
//            default:
//                return false;
//        }
//        return true;
//    }
//
    public static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }
}

abstract class instruction {

    abstract void runInstruction(int opCode);
}
