;mfhi $d = hi moves the value in the hi register into register d

addi $4, $0, 0xFFFF
addi $5, $0, 0xFFFF
multu $4, $5 ;multiplys 13 and 47

mfhi $4; moves the lo register into register 4
trap 1
trap 0;prints upper 32 bits of result and stops program