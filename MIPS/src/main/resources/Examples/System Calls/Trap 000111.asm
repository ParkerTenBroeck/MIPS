;trap 111 is used for debugging

;when trap 111 is called it will stop the program and NOT reset it only when breakpoints are enabled

;after the system has been stopped you can look to see the states of the internal registers
;and also look at the memory of the system at any point
;and if nessisary you can step throught the program one instruction at a time


;you can see it in use from the example code for trap 104


;press w,a,s,d and see each key light up on screen


addi $4, $0, 3
addi $5, $0, 2
trap 150 ;inits the screen to 2x3

loop: ;each block loads the char for w,a,s,d and checks weather it is pressed then does a logical or for a boolean into register 8

addi $8, $0, 0 
addi $4, $0, 97 ; char for a
trap 104
beq $2, $0, 1
ori $8, $8, 2x1 

addi $4, $0, 115 ; char for s
trap 104
beq $2, $0, 1
ori $8, $8, 2x10 

trap 111;breakpoint

addi $4, $0, 100 ; char for d
trap 104
beq $2, $0, 1
ori $8, $8, 2x100

addi $4, $0, 119 ; char for w
trap 104
beq $2, $0, 1
ori $8, $8, 2x1000 

addi $4, $0, 0 ;fills screen with black(clears screen)
trap 156

andi $7, $8, 1 ;each block checks weather its letter boolean is set and fills in the specified spot on the screen if it is set 
beq $7, $0, 4
addi $4, $0, 0
addi $5, $0, 1
addiu $6, $0, 16xFF00
trap 151

andi $7, $8, 2
beq $7, $0, 4
addi $4, $0, 1
addi $5, $0, 1
addiu $6, $0, 16xFFFF
trap 151

trap 111;breakpoint

andi $7, $8, 4
beq $7, $0, 4
addi $4, $0, 2
addi $5, $0, 1
addiu $6, $0, 16x00FF
trap 151

andi $7, $8, 8
beq $7, $0, 4
addi $4, $0, 1
addi $5, $0, 0
addiu $6, $0, 16xFF99
trap 151

trap 153 ;updates screen
j loop   ;loops