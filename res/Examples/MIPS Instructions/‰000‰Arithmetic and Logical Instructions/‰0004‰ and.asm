;and $d = $s + $t
;preforms a bitwise and between register s and t and places the result in register d

addi $4, $0, 358;places the value 9 into register 4
addi $5, $0, 0xFF; places the value 13 into register 5

and $6, $5, $4; preforms a bitwise and between register 5 and 4 and places the result int register 6

trap 0;stops the program