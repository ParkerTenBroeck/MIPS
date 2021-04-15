/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.emulator.mips;

import org.parker.mips.emulator.InvalidOpCodeException;

import static org.parker.mips.plugin.syscall.SystemCallPluginHandler.SystemCall;
import static org.parker.mips.emulator.mips.Memory.*;
import static org.parker.mips.emulator.mips.Registers.*;

/**
 *
 * @author parke
 */

public class InstructionDecode {

	protected static final Instruction[] immediateInstructionLookUp = new Instruction[64];
	protected static final Instruction[] registerInstructionLookUp = new Instruction[64];

	static {
		immediateInstructionLookUp[0B000000] = new Instruction() { // Register
			@Override
			final void runInstruction(int opCode) {
				int f = opCode & 0B111111;
				try {
					registerInstructionLookUp[f].runInstruction(opCode);
				}catch(ArrayIndexOutOfBoundsException | NullPointerException e){
					throw new InvalidOpCodeException("Invalid (Register) OpCode: " + Integer.toHexString(f));
				}
			}
		};

		// Jump Encoding
		immediateInstructionLookUp[0B000010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int i = (opCode << 6) >> 6;
				// Jump
				setPc(getPc() + (i << 2));
			}
		};
		immediateInstructionLookUp[0B000011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int i = (opCode << 6) >> 6;
				// jal
				setRegister(31, getPc());
				setPc(getPc() + (i << 2));
			}
		};
		immediateInstructionLookUp[0B011010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int i = (opCode << 6) >> 6;
				// trap
				SystemCall(i);
			}
		};
		// Immediate Encoding

		// arthmetic
		immediateInstructionLookUp[0B001000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// addi
				setRegister(t, getRegister(s) + SEi);
			}
		};

		immediateInstructionLookUp[0B001001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// addiu
				setRegister(t, (int) (getUnsignedInt(getRegister(s)) + getUnsignedInt(SEi)));
			}
		};

		immediateInstructionLookUp[0B001100] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// andi
				setRegister(t, getRegister(s) & ZEi);
			}
		};

		immediateInstructionLookUp[0B001101] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// ori
				setRegister(t, getRegister(s) | ZEi);
			}
		};

		immediateInstructionLookUp[0B001110] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// xori
				setRegister(t, getRegister(s) ^ ZEi);
			}
		};

		// constant manupulating inctructions
		immediateInstructionLookUp[0B011001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// lhi
				setRegister(t, (getRegister(t) & 0xFFFF) | ((ZEi << 16) & 0xFFFF0000));
			}
		};

		immediateInstructionLookUp[0B011000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// llo
				setRegister(t, (getRegister(t) & 0xFFFF0000) | (ZEi & 0xFFFF));
			}
		};

		// comparison Instructions
		immediateInstructionLookUp[0B001010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// slti
				setRegister(t, SEi - getRegister(s));
			}
		};

//        immediateInstructionLookUp[0B001001] = new Instruction() {
//            @Override
//            final void runInstruction(int opCode) {
//                int s = (opCode >>> 21) & 0B11111;
//                int t = (opCode >>> 16) & 0B11111;
//                int SEi = ((opCode) << 16) >> 16;
//                //sltiu
//                setRegister(t, (int) (getUnsignedInt(SEi) - getUnsignedInt(getRegister(s))));   //conflict with addiu
//            }
//        };
		// branch Instructions
		immediateInstructionLookUp[0B000100] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// beq
				if (getRegister(s) == getRegister(t)) {
					setPc(getPc() + (SEi << 2));
				}
			}
		};

		immediateInstructionLookUp[0B000111] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// bgtz
				if (getRegister(s) > 0) {
					setPc(getPc() + (SEi << 2));
				}
			}
		};

		immediateInstructionLookUp[0B000110] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// blez
				if (getRegister(s) <= 0) {
					setPc(getPc() + (SEi << 2));
				}
			}
		};

		immediateInstructionLookUp[0B000101] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int SEi = ((opCode) << 16) >> 16;
				// bne
				if (getRegister(s) != getRegister(t)) {
					setPc(getPc() + (SEi << 2));
				}
			}
		};

		// load instrictions
		immediateInstructionLookUp[0B100000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// lb
				setRegister(t, (int) getByte(getRegister(s) + ZEi));
			}
		};

		immediateInstructionLookUp[0B100100] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// lbu
				setRegister(t, getByte(getRegister(s) + ZEi) & 0xFF);
			}
		};

		immediateInstructionLookUp[0B100001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// lh
				setRegister(t, (int) getHalfWord(getRegister(s) + ZEi));
			}
		};

		immediateInstructionLookUp[0B100101] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// lhu
				setRegister(t, getHalfWord(getRegister(s) + ZEi) & 0xFFFF);
			}
		};

		immediateInstructionLookUp[0B100011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// lw
				setRegister(t, getWord(getRegister(s) + ZEi));
			}
		};

		// store instrictions
		immediateInstructionLookUp[0B101000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// sb
				Memory.setByte(getRegister(s) + ZEi, getRegister(t));
			}
		};

		immediateInstructionLookUp[0B101001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// sh
				Memory.setHalfWord(getRegister(s) + ZEi, getRegister(t));
			}
		};

		immediateInstructionLookUp[0B101011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >>> 21) & 0B11111;
				int t = (opCode >>> 16) & 0B11111;
				int ZEi = (opCode & 0xFFFF);
				// sw
				Memory.setWord(getRegister(s) + ZEi, getRegister(t));
			}
		};

		// registerEncoding
		// arithmatic
		registerInstructionLookUp[0B100000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				// add
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				setRegister(d, getRegister(s) + getRegister(t));
			}
		};
		registerInstructionLookUp[0B100000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// addu
				setRegister(d, (int) (getUnsignedInt(getRegister(s)) + getUnsignedInt(getRegister(t))));
			}
		};
		registerInstructionLookUp[0B100100] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// and
				setRegister(d, getRegister(s) & getRegister(t));
			}
		};

		registerInstructionLookUp[0B011010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				// div
				setLow(getRegister(s) / getRegister(t));
				setHigh(getRegister(s) % getRegister(t));
			}
		};

		registerInstructionLookUp[0B011011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				// divu
				setLow((int) (getUnsignedInt(getRegister(s)) / getUnsignedInt(getRegister(t))));
				setHigh((int) (getUnsignedInt(getRegister(s)) % getUnsignedInt(getRegister(t))));
			}
		};

		registerInstructionLookUp[0B011000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				// mult
				setLow(getRegister(s) * getRegister(t));
				setHigh((int) ((long) getRegister(s) * (long) getRegister(t) >> 32));
			}
		};

		registerInstructionLookUp[0B011001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				// multu
				long result = (getUnsignedInt(getRegister(s)) * getUnsignedInt(getRegister(t)));
				setLow((int) (result & 0xFFFFFFFF));
				setHigh((int) (result >> 32));
			}
		};

		registerInstructionLookUp[0B100111] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// nor
				setRegister(d, ~(getRegister(s) | getRegister(t)));
			}
		};

		registerInstructionLookUp[0B100101] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// or
				setRegister(d, (getRegister(s) | getRegister(t)));
			}
		};

		registerInstructionLookUp[0B000000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				int a = (opCode >> 6) & 0B11111;
				// sll
				setRegister(d, getRegister(t) << a);
			}
		};

		registerInstructionLookUp[0B000100] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// sllv
				setRegister(d, getRegister(t) << getRegister(s));
			}
		};

		registerInstructionLookUp[0B000011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				int a = (opCode >> 6) & 0B11111;
				// sra
				setRegister(d, getRegister(t) >> a);
			}
		};

		registerInstructionLookUp[0B000111] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// srav
				setRegister(d, getRegister(t) >> getRegister(s));
			}
		};

		registerInstructionLookUp[0B000010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				int a = (opCode >> 6) & 0B11111;
				// srl
				setRegister(d, getRegister(t) >>> a);
			}
		};

		registerInstructionLookUp[0B000110] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// srlv
				setRegister(d, getRegister(t) >>> getRegister(s));
			}
		};

		registerInstructionLookUp[0B100010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// sub
				setRegister(d, getRegister(s) - getRegister(t));
			}
		};

		registerInstructionLookUp[0B100011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// subu
				setRegister(d, (int) (getUnsignedInt(getRegister(s)) - getUnsignedInt(getRegister(t))));
			}
		};

		registerInstructionLookUp[0B100110] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// xor
				setRegister(d, getRegister(s) ^ getRegister(t));
			}
		};

		// comparasin
		registerInstructionLookUp[0B101010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// slt
				setRegister(d, getRegister(t) - getRegister(s));
			}
		};

		registerInstructionLookUp[0B101001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				int t = (opCode >> 16) & 0B11111;
				int d = (opCode >> 11) & 0B11111;
				// sltu
				setRegister(d, (int) (getUnsignedInt(getRegister(t)) - getUnsignedInt(getRegister(s))));
			}
		};

		// jump
		registerInstructionLookUp[0B001001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				// jalr
				setRegister(31, getPc());
				setPc(getRegister(s));
			}
		};

		registerInstructionLookUp[0B001000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				// jr
				setPc(getRegister(s));
			}
		};

		// dataMovement
		registerInstructionLookUp[0B010000] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int d = (opCode >> 11) & 0B11111;
				// mfhi
				setRegister(d, getHigh());
			}
		};

		registerInstructionLookUp[0B010010] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int d = (opCode >> 11) & 0B11111;
				// mflo
				setRegister(d, getLow());
			}
		};

		registerInstructionLookUp[0B010001] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				// mthi
				setHigh(getRegister(s));
			}
		};

		registerInstructionLookUp[0B010011] = new Instruction() {
			@Override
			final void runInstruction(int opCode) {
				int s = (opCode >> 21) & 0B11111;
				// mtlo
				setLow(getRegister(s));
			}
		};

	}

	public static void runInstruction(int opCode) throws InvalidOpCodeException {
		int o = (opCode >>> 26) & 0B111111;

		Registers.pc += 4;

		try {
			immediateInstructionLookUp[o].runInstruction(opCode);
		}catch(ArrayIndexOutOfBoundsException | NullPointerException e){
			throw new InvalidOpCodeException("Invalid (Immediate) OpCode: 0x" + Integer.toHexString(o));
		}
	}
	
	public static long getUnsignedInt(int x) {
		return x & 0x00000000ffffffffL;
	}

	public static abstract class Instruction {
		abstract void runInstruction(int opCode);
	}
}
