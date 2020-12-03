addi $7, $0, 60
addi $8, $0, 24

addi $5, $0, 0
addi $6, $0, 22
add $4, $0, $5
trap 201
add $4, $0, $6
trap 200

main:
addi $4, $5, 0
trap 201
addi $5, $5, 1

addi $4, $0, 100
trap 105

beq $5, $7, nextHour
j main

nextHour:
addi $5, $0, 0

addi $6, $6, 1
addi $4, $6, 0
trap 200
beq $6, $8, 1
j main
addi $6, $0, 0
j main