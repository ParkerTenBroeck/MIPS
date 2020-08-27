#definline newLine
addi $4, $0, 10
trap 101
#endinline

;lbu $t = SE (MEM [$s + i]:1)
;lbu loads the byte at $s + i where register s is any register and i is an immediate value
;lbu does not keeps twos complement value

add $5, $0, $0

lbu $4, space($5) ;prints all four bytes in order
trap 1
newLine
addi $5, $5, 1

lbu $4, space($5)
trap 1
newLine
addi $5, $5, 1

lbu $4, space($5)
trap 1
newLine
addi $5, $5, 1

lbu $4, space($5)
trap 1
newLine
addi $5, $5, 1

trap 0


space:
.byte 1
.byte 2
.byte -1
.byte -2
