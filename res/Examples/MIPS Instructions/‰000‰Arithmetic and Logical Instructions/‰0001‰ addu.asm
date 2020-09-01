;add $d = $s + $t
;adds register s and t together and places the result in register d
;this operation is unsigned 

addi $4, $0, 9;places the value 9 into register 4
addi $5, $0, 13; places the value 13 into register 5

addu $6, $5, $4; adds $5 + $4 and places the result int register 6

trap 0;stops the program