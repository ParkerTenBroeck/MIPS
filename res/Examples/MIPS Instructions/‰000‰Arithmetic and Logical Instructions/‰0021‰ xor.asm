;xor $d = $s ^ $t
;xor preforms a bitwise xor between register s and t and places the result into register d

addi $4, $0, 14 ;places the value 14 into register 4
addi $5, $0, 3  ;places the value 4 into register 5

xor $6, $5, $4 ;preforms a bitwise xor between register 4 and 5 and places the result into register 6

trap 0;stops the program