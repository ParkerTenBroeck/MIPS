;sll $d = $t << a
;sll preforms a bitwise shift to the left on the value in register 4 by the immediate value and places the result in register d

addi $4, $0, 15;places the value 15 into register 4

sll $5, $4, 4; shifts the value in register 4 to the left by 4 places and places the result in register 5

trap 0;stops the program  