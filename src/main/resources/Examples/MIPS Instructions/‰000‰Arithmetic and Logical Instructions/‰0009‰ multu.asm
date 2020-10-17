;mult hi:lo = $s * $t
;mult multiplys registers s and t together and places the lower 32 bits of the multiplication in the lo register
;and places the upper 32 bits of the result into the hi register
;this operation is unsigned

addi $4, $0, 600 ;places the value 600 into register 4
addi $5, $0, 14  ;places the value 14 into register 5

multu $4, $5 ;divides register 4 by 5

mflo $4 ;places the lower 32 bits of the multiplication into register 4
mfhi $5 ;places the upper 32 bits of the multiplication into register 5

trap 0;stops the program