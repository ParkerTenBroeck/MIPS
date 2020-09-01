;sub $d = $s - $t
;sub subtracts registers s and t and places the result into reigster d
;this operation is unsigned

addi $4, $0, 13;places the value 13 into reigster 4
addi $5, $0, 54;places the value 54 into register 5

subu $6, $4, $5;subtracts reigster 5 from reigster s and places the result int reigster 6

trap 0;stops the program