
.org 0x40

.global "anotherSubFunc"

start:

addi $1, $1, 10

.ref "label"

main:

j label
j main

addi $2, $1, 10


anotherSubFunc:
sub $4, $4, $5
sub $4, $4, $7
jr $31