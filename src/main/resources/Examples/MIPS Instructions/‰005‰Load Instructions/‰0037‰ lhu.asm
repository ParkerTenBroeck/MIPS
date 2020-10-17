#definline newLine
addi $4, $0, 10
trap 101
#endinline

;lhu $t = SE (MEM [$s + i]:2)
;lhu loads a half word at $s + i where register s is any register and i is an immediate value
;lhu does not keeps twos complement value
;MEM must be alligned with 2

add $5, $0, $0

lhu $4, space($5) ;prints all four half words in order
trap 1
newLine
addi $5, $5, 2

lhu $4, space($5)
trap 1
newLine
addi $5, $5, 2

lhu $4, space($5)
trap 1
newLine
addi $5, $5, 2

lhu $4, space($5)
trap 1
newLine
addi $5, $5, 2

trap 0


space:
.hword 0x0F00
.hword 0x0F01
.hword 0x8F00
.hword 0x8F01
