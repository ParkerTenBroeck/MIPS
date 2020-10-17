;sllv $d = $t << $s
;sllv preforms a bitwise shift to the left on the value in register 4 by register s and places the result in register d

addi $4, $0, 15;places the value 15 into register 4
addi $5, $0, 6 ;places the value 6 into register 5

sllv $6, $4, $5; shifts the value in register 4 to by the value in register 5 places and places the result in register 6

trap 0;stops the program  