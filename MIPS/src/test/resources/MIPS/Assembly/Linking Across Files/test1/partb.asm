.org 0

start:
add $4, $2, $4

.global "label"

.ref "anotherSubFunc"

main:
add $4, $4, $4
jal anotherSubFunc

.org 0x20

label:
;some function
add $4, $4, $5
j main