/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.processor;

import GUI.Main_GUI;
import static mips.processor.Memory.getByte;
import static mips.processor.Memory.getHalfWord;
import static mips.processor.Memory.getWord;
import static mips.processor.Memory.setByte;
import static mips.processor.Memory.setHalfWord;
import static mips.processor.Memory.setWord;
import static mips.processor.Registers.getHigh;
import static mips.processor.Registers.getLow;
import static mips.processor.Registers.getPc;
import static mips.processor.Registers.getRegister;
import static mips.processor.Registers.setHigh;
import static mips.processor.Registers.setLow;
import static mips.processor.Registers.setPc;
import static mips.processor.Registers.setRegister;
import static mips.processor.SystemCall.SystemCall;

/**
 *
 * @author parke
 */
public class InstructionDecode {

    private static void incPc() {
        setPc(getPc() + 4);
    }

    public static boolean runInstruction(int opCode) {
        int o = (opCode >>> 26) & 0B111111;

        switch (o) {

            case 0B000000: //Register
                return registerEncoding(opCode);

            case 0B000010: //jump
            case 0B000011:
            case 0B011010:
                return jumpEncoding(opCode);

            default: //Immediate
                return immediateEncoding(opCode);

        }
    }

    private static boolean registerEncoding(int opCode) {
        int s = (opCode >> 21) & 0B11111;
        int t = (opCode >> 16) & 0B11111;
        int d = (opCode >> 11) & 0B11111;
        int a = (opCode >> 6) & 0B11111;
        int f = opCode & 0B111111;

        incPc();

        switch (f) {
            //arithmatic
            case 0B100000: //add
                setRegister(d, getRegister(s) + getRegister(t));
                break;

            case 0B100001: //addu
                setRegister(d, (int)(0xFFFFFFFF & ((long)getRegister(s)) + 0xFFFFFFFF & (long)getRegister(t)));
                break;

            case 0B100100: //and
                setRegister(d, getRegister(s) & getRegister(t));
                break;

            case 0B011010: //div
                setLow(getRegister(s) / getRegister(t));
                setHigh(getRegister(s) % getRegister(t));
                break;

            case 0B011011: //divu
                setLow(getRegister(s) / getRegister(t));
                setHigh(getRegister(s) % getRegister(t));
                break;

            case 0B011000: //mult
                setLow(getRegister(s) * getRegister(t));
                setHigh((int) ((long) getRegister(s) * (long) getRegister(t) >> 32));
                break;

            case 0B011001: //multu
                setLow(getRegister(s) * getRegister(t));
                setHigh((int) ((long) getRegister(s) * (long) getRegister(t) >> 32));
                break;

            case 0B100111: //nor
                setRegister(d, ~(getRegister(s) | getRegister(t)));
                break;

            case 0B100101: //or
                setRegister(d, (getRegister(s) | getRegister(t)));
                break;

            case 0B000000: //sll
                setRegister(d, getRegister(t) << a);
                break;

            case 0B000100: //sllv
                setRegister(d, getRegister(t) << getRegister(s));
                break;

            case 0B000011: //sra
                setRegister(d, getRegister(t) >> a);
                break;

            case 0B000111: //srav
                setRegister(d, getRegister(t) >> getRegister(s));
                break;

            case 0B000010: //srl
                setRegister(d, getRegister(t) >>> a);
                break;

            case 0B000110: //srlv
                setRegister(d, getRegister(t) >>> getRegister(s));
                break;

            case 0B100010: //sub
                setRegister(d, getRegister(s) - getRegister(t));
                break;

            case 0B100011: //subu
                setRegister(d, (int)(0xFFFFFFFF & ((long)getRegister(s)) + 0xFFFFFFFF & (long)getRegister(t)));
                break;

            case 0B100110: //xor
                setRegister(d, getRegister(s) ^ getRegister(t));
                break;

            //comparasin
            case 0B101010: //slt
                setRegister(d, getRegister(t) - getRegister(s));
                break;

            case 0B101001: //sltu 
                setRegister(d, (int)(0xFFFFFFFF & ((long)getRegister(t)) - 0xFFFFFFFF & (long)getRegister(s)));
                break;

            //jump
            case 0B001001: //jalr
                setRegister(31, getPc());
                setPc(getRegister(s));
                break;

            case 0B001000: //jr
                setPc(getRegister(s));
                break;

            //dataMovement
            case 0B010000: //mfhi
                setRegister(d, getHigh());
                break;

            case 0B010010: //mflo
                setRegister(d, getLow());
                break;

            case 0B010001: //mthi
                setHigh(getRegister(s));
                break;

            case 0B010011: //mtlo
                setLow(getRegister(s));
                break;

            default:
                return false;
        }
        return true;
    }

    private static boolean immediateEncoding(int opCode) {
        int o = (opCode >>> 26) & 0B111111;
        int s = (opCode >>> 21) & 0B11111;
        int t = (opCode >>> 16) & 0B11111;
        int SEi = ((opCode) << 16) >> 16;
        int ZEi = (opCode & 0xFFFF);

        incPc();

        switch (o) {

            //arthmetic
            case 0B001000: //addi
                setRegister(t, getRegister(s) + SEi);
                break;

            case 0B001001: //addiu
                setRegister(t, (int)(0xFFFFFFFF & ((long)getRegister(s)) + 0xFFFFFFFF & (long)SEi));
                break;

            case 0B001100: //andi
                setRegister(t, getRegister(s) & ZEi);
                break;

            case 0B001101: //ori
                setRegister(t, getRegister(s) | ZEi);
                break;

            case 0B001110: //xori
                setRegister(t, getRegister(s) ^ ZEi);
                break;

            //constant manupulating inctructions
            case 0B011001: //lhi
                setRegister(t, (getRegister(t) & 0xFFFF) | ((ZEi << 16) & 0xFFFF0000));
                break;

            case 0B011000: //llo
                setRegister(t, (getRegister(t) & 0xFFFF0000) | (ZEi & 0xFFFF));
                break;

            //comparison instructions
            case 0B001010: //slti
                setRegister(t, SEi - getRegister(s));
                break;

            //branch instructions 
            case 0B000100: //beq
                if (getRegister(s) == getRegister(t)) {
                    setPc(getPc() + (SEi << 2));
                }
                break;
            case 0B000111: //bgtz

                if (getRegister(s) > 0) {
                    setPc(getPc() + (SEi << 2));
                }
                break;
            case 0B000110: //blez
                if (getRegister(s) <= 0) {
                    setPc(getPc() + (SEi << 2));
                }
                break;
            case 0B000101: //bne
                if (getRegister(s) != getRegister(t)) {
                    setPc(getPc() + (SEi << 2));
                }
                break;

            //load instrictions
            case 0B100000: //lb
                setRegister(t, (int) getByte(getRegister(s) + ZEi));
                break;

            case 0B100100: //lbu
                setRegister(t, getByte(getRegister(s) + ZEi) & 0xFF);
                break;

            case 0B100001: //lh
                setRegister(t, (int) getHalfWord(getRegister(s) + ZEi));
                break;

            case 0B100101: //lhu
                setRegister(t, getHalfWord(getRegister(s) + ZEi) & 0xFFFF);
                break;

            case 0B100011: //lw
                setRegister(t, getWord(getRegister(s) + ZEi));
                break;

            //store instrictions
            case 0B101000: //sb
                Memory.setByte(getRegister(s) + ZEi, getRegister(t));
                break;

            case 0B101001: //sh
                Memory.setHalfWord(getRegister(s) + ZEi, getRegister(t));
                break;

            case 0B101011: //sw
                Memory.setWord(getRegister(s) + ZEi, getRegister(t));
                break;

            default:
                return false;
        }
        return true;
    }

    private static boolean jumpEncoding(int opCode) {
        int o = (opCode >>> 26) & 0B111111;
        int i = (opCode << 6) >> 6;

        incPc();

        switch (o) {
            case 0B000010: //j
                setPc(getPc() + (i << 2));
                break;

            case 0B000011: //jal
                setRegister(31, getPc());
                setPc(getPc() + (i << 2));
                break;

            case 0B011010: //trap
                SystemCall(i);
                break;

            default:
                return false;
        }
        return true;
    }
}
