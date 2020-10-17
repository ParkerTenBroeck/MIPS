;pc = $s
;jr sets the pc to the value in register s (must be alligned with 4)
;this is usful for returning from method calls

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
