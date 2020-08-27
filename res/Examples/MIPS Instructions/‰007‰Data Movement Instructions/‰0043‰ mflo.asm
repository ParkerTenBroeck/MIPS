;mflo $d = hi moves the value in the lo register into register d

addi $4, $0, 13
addi $5, $0, 47
mult $4, $5 ;multiplys 13 and 47

mflo $4; moves the lo register into register 4
trap 1
trap 0;prints result and stops program