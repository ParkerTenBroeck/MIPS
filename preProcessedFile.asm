addi $6,$0,97
loop:
trap 102
add $4,$0,$2
trap 101
bne $4,$6,loop
trap 0
