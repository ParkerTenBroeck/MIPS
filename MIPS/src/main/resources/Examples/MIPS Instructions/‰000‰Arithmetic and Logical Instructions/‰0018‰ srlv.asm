;srav $d = $t >>> $s
;srav preforms a bitwise shift to the right on the value in register 4 by register s value and places the result in register d
;this does not keep the sig bit

addi $4, $0, 304;places the value 304 into register 4
addi $5, $0, 5  ;places the value 5 into register 5

srlv $6, $4, $5; shifts the value in register 4 to the right by register 5 and places and places the result in register 6

trap 0;stops the program  