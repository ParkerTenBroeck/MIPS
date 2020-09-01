;srl $d = $t >>> a
;srl preforms a bitwise shift to the right on the value in register 4 by the immediate value and places the result in register d
;this does not keep the sig bit

addi $4, $0, -304;places the value -304 into register 4

srl $5, $4, 4; shifts the value in register 4 to the right by 4 places and places the result in register 5

trap 0;stops the program  