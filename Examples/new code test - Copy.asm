#definline push $a
addi $29, $29, 4
sw $a, stack, $29 
#endinline

.org 0 ;start of program

Main:
addi $4, $0, 3
push $4

something:
.byte 5, 6,7,8,9,10

stack:
.space 100000

