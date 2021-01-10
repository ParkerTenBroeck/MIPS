/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.compiler;

/**
 *
 * @author parke
 */
public class InstructionToString {

    public static String instructionToString(int opCode) {
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

    private static String registerEncoding(int opCode) {
        int s = (opCode >> 21) & 0B11111;
        int t = (opCode >> 16) & 0B11111;
        int d = (opCode >> 11) & 0B11111;
        int a = (opCode >> 6) & 0B11111;
        int f = opCode & 0B111111;

        switch (f) {
            //arithmatic
            case 0B100000: //add
                return "add   $" + d + ", $" + s + ", $" + t;

            case 0B100001: //addu
                return "addu  $" + d + ", $" + s + ", $" + t;

            case 0B100100: //and
                return "and   $" + d + ", $" + s + ", $" + t;

            case 0B011010: //div
                return "div   $" + s + ", $" + t;

            case 0B011011: //divu
                return "divu  $" + s + ", $" + t;

            case 0B011000: //mult
                return "mult  $" + s + ", $" + t;

            case 0B011001: //multu
                return "multu $" + s + ", $" + t;

            case 0B100111: //nor
                return "nor   $" + d + ", $" + s + ", $" + t;

            case 0B100101: //or
                return "or    $" + d + ", $" + s + ", $" + t;

            case 0B000000: //sll
                return "sll   $" + d + ", $" + t + ", " + a;

            case 0B000100: //sllv
                return "sllv  $" + d + ", $" + t + ", $" + s;

            case 0B000011: //sra
                return "sra   $" + d + ", $" + t + ", " + a;

            case 0B000111: //srav
                return "srav  $" + d + ", $" + t + ", $" + s;

            case 0B000010: //srl
                return "srl   $" + d + ", $" + t + ", " + a;

            case 0B000110: //srlv
                return "srlv  $" + d + ", $" + t + ", $" + s;

            case 0B100010: //sub
                return "sub   $" + d + ", $" + s + ", $" + t;

            case 0B100011: //subu
                return "subu  $" + d + ", $" + s + ", $" + t;

            case 0B100110: //xor
                return "xor   $" + d + ", $" + s + ", $" + t;

            //comparasin
            case 0B101010: //slt
                return "slt   $" + d + ", $" + s + ", $" + t;

            case 0B101001: //sltu
                return "sltu  $" + d + ", $" + s + ", $" + t;

            //jump
            case 0B001001: //jalr
                return "jalr  $" + s;

            case 0B001000: //jr
                return "jr    $" + s;

            //dataMovement
            case 0B010000: //mfhi
                return "mfhi  $" + d;

            case 0B010010: //mflo
                return "mflo  $" + d;

            case 0B010001: //mthi
                return "mthi  $" + s;

            case 0B010011: //mtlo
                return "mtlo  $" + s;

        }
        return "NA";
    }

    private static String immediateEncoding(int opCode) {
        int o = (opCode >>> 26) & 0B111111;
        int s = (opCode >>> 21) & 0B11111;
        int t = (opCode >>> 16) & 0B11111;
        int SEi = ((opCode) << 16) >> 16;
        int ZEi = (opCode & 0xFFFF);

        switch (o) {

            //arthmetic
            case 0B001000: //addi
                return "addi  $" + t + ", $" + s + ", " + SEi;

            case 0B001001: //addiu
                return "addiu $" + t + ", $" + s + ", " + SEi;

            case 0B001100: //andi
                return "andi  $" + t + ", $" + s + ", " + ZEi;

            case 0B001101: //ori
                return "ori   $" + t + ", $" + s + ", " + ZEi;

            case 0B001110: //xori
                return "xori  $" + t + ", $" + s + ", " + ZEi;

            //constant manupulating inctructions
            case 0B011001: //lhi
                return "lhi   $" + t + ", " + ZEi;

            case 0B011000: //llo
                return "llo   $" + t + ", " + ZEi;

            //comparison instructions
            case 0B001010: //slti
                return "slti  $" + t + ", $" + s + ", " + SEi;

            //branch instructions 
            case 0B000100: //beq
                return "beq   $" + t + ", $" + s + ", " + SEi;

            case 0B000111: //bgtz
                return "bgtz  $" + t + ", $" + s + ", " + SEi;

            case 0B000110: //blez
                return "blez  $" + t + ", $" + s + ", " + SEi;

            case 0B000101: //bne
                return "bne   $" + t + ", $" + s + ", " + SEi;

            //load instrictions
            case 0B100000: //lb
                return "lb    $" + t + ", " + SEi + "($" + s + ")";

            case 0B100100: //lbu
                return "lbu   $" + t + ", " + SEi + "($" + s + ")";

            case 0B100001: //lh
                return "lh    $" + t + ", " + SEi + "($" + s + ")";

            case 0B100101: //lhu
                return "lhu   $" + t + ", " + SEi + "($" + s + ")";

            case 0B100011: //lw
                return "lw    $" + t + ", " + SEi + "($" + s + ")";

            //store instrictions
            case 0B101000: //sb
                return "sb    $" + t + ", " + SEi + "($" + s + ")";

            case 0B101001: //sh
                return "sh    $" + t + ", " + SEi + "($" + s + ")";

            case 0B101011: //sw
                return "sw    $" + t + ", " + SEi + "($" + s + ")";

        }
        return "NA";
    }

    private static String jumpEncoding(int opCode) {
        int o = (opCode >>> 26) & 0B111111;
        int i = (opCode << 6) >> 6;

        switch (o) {
            case 0B000010: //j
                return "j     " + i;

            case 0B000011: //jal
                return "jal   " + i;

            case 0B011010: //trap
                return "trap  " + i;

        }
        return "NA";
    }
}
