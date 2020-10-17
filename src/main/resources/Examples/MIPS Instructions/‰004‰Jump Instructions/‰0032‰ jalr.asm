;$31 = pc; pc = $s
;jalr sets register 31 to the current pc then the pc to the value in register s (must be alligned with 4)
;this is usful for calling a method with its absolute memory position
;(this is how jump vectoring works in interrupts)

#define printJV 4

.org 0
j main ;goto main program

.org printJV ;sets up vector table
llo $1, printReg4
;lhi $1, printReg4
jr $1


main:
addi $4, $0, 0

loop:
addi $5, $0, printJV
jalr $5 ;goto print method defined in the vector table register 4
addi $4, $4, 1 ;incs register 4
j loop


.org 0x06F8

printReg4:
trap 1 ;prints int in register 4
add $8, $0, $4
addi $4, $0, 10
trap 101      	;swaps register 4 into register 8 and prints char 10(new line)
add $4, $0, $8  ;swaps register 8 back into register 4
jr $31 ;returns;