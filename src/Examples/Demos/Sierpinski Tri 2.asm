addi $15, $0, 256
mainLoop:

addi $4, $15, 0
add $5, $4, $0
trap 150 ; inits screen


add $10, $4, $0; $10 = n
addi $12, $5, -1; dec n by one and put it in y $12 = y
yLoop:
add $11, $0, $0;$11 = x
xLoop:

and $4, $11, $12 ; dont draw if y & x is zero
bne $4, $0, endOfDraw

add $4, $11, $0 ; x -> $4
add $5, $12, $0 ; y -> $5

sub $5, $10, $5  ; invert y
addi $5, $5, -1  ; start at 0
addi $6, $0, 2   
div $12, $6    ; y / 2 -> $6
mflo $6
add $4, $4, $6

addi $6, $0, 255
trap 151
endOfDraw:

addi $11, $11, 1;inc x by 1
add $4, $11, $0 ; 
sub $4, $4, $10 ; subtracts n
addi $4, $4, -1 ; subtracts 1
blez $4, xLoop



addi $12, $12, -1; dec y by one

addi $4, $12, 1 ; jumps if greater or equal to zero
bgtz $4, yLoop

trap 153

addi $4, $0, 256
bne $4, $15, 1
addi $15, $0, 512

addi $4, $0, 100
trap 105
addi $15, $15, -1

j mainLoop

