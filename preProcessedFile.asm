addi $4,$0,0xFFFF
addi $5,$0,0xFFFF
multu $4,$5
mfhi $4
trap 1
trap 0
