;bne if ($s != $t) pc += i <<2
;this instruction branches if the condition of $s != $t is true
;else the next instruction is ran

main:

addi $5, $0, 19 ;loads reigster 5 with 19 and register 6 with 0
addi $6, $0, 0

loop:
add $4, $0, $6
trap 1
addi $4, $0, 10 ;prints out number in register 6 and prints a new line
trap 101

addi $6, $6, 1 ;increments counter

bne $6, $5, loop ;jumps if counter is not equal to 19

trap 0 ;ends program


