#definline newLine
addi $4, $0, 10
trap 101
#endinline

;lh $t = SE (MEM [$s + i]:2)
;lh loads a half word at $s + i where register s is any register and i is an immediate value
;lh keeps twos complement value
;MEM must be alligned with 2

add $5, $0, $0

lh $4, space($5) ;prints all four half words in order
trap 1
newLine
addi $5, $5, 2

lh $4, space($5)
trap 1
newLine
addi $5, $5, 2

lh $4, space($5)
trap 1
newLine
addi $5, $5, 2

lh $4, space($5)
trap 1
newLine
addi $5, $5, 2

trap 0


space:
.hword 0x0F00
.hword 0x0F01
.hword 0x8F00
.hword 0x8F01
