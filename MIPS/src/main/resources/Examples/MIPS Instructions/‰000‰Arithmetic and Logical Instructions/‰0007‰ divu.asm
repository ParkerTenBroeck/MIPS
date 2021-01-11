;divu lo = $s / $t; hi = $s % $t
;divu preforms division between registers s and t ($s/$t)
;then places the quotient in the lo register and the remainder in the hi register
;this operation is unsigned

addi $4, $0, 100 ;places the value 100 into register 4
addi $5, $0, 14  ;places the value 14 into register 5

divu $4, $5 ;divides register 4 by 5

mflo $4 ;places the quotient into register 4
mfhi $5 ;places the remainder into register 5

trap 0;stops the program