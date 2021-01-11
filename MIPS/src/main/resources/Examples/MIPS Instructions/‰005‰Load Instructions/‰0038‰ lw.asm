#definline newLine
addi $4, $0, 10
trap 101
#endinline

;lw $t = SE (MEM [$s + i]:4)
;lw loads a half word at $s + i where register s is any register and i is an immediate value
;lw twos complement value
;MEM must be alligned with 4

add $5, $0, $0

lw $4, space($5) ;prints all four words in order
trap 1
newLine
addi $5, $5, 4

lw $4, space($5)
trap 1
newLine
addi $5, $5, 4

lw $4, space($5)
trap 1
newLine
addi $5, $5, 4

lw $4, space($5)
trap 1
newLine
addi $5, $5, 4

trap 0


space:
.word 0x0F000F00
.word 0x0F000F01
.word 0x8F000F00
.word 0x8F000F01
