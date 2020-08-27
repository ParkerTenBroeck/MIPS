;$31 = pc; pc += i << 2
;jal jumps and links the inctruction register with register 31
;this is usful for method calls

addi $4, $0, 0

loop:
jal printReg4 ;print register 4
addi $4, $4, 1 ;incs register 4
j loop

printReg4:
trap 1 ;prints int in register 4
add $8, $0, $4
addi $4, $0, 10
trap 101      	;swaps register 4 into register 8 and prints char 10(new line)
add $4, $0, $8  ;swaps register 8 back into register 4
jr $31 ;returns;
