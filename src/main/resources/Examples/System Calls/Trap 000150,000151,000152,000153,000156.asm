;trap 150 sets the size of the virtual screen where $4 -> x and $5 -> y

;trap 151 sets one pixel of color where $4 -> x, $5 -> y, $6 -> color stored as a RGB int

;trap 152 sets one pixel of color where index is $4, $5 -> color stored as RGB int
;where an index of 0 is the top left and the bottom right index is width*height-1 filled in from left to right top to bottom

;trap 153 updates the screen (without calling this the screen will never change)

;trap 156 fills the screen with the color in register 4 stored as a RGB int

addi $4, $0, 5 ;sets the screen to 5x4
addi $5, $0, 4
trap 150

addi $4, $0, 255 ;fills screen with blue
trap 156

addiu $6, $0, 16xFFFF ;yellow
sll $6, $6, 8

addi $4, $0, 1 ;yellow pixel at 1,0
addi $5, $0, 0
trap 151

addi $4, $0, 3 ;yellow pixel at 3,0
addi $5, $0, 0
trap 151

addi $4, $0, 0 ;yellow pixel at 0,2
addi $5, $0, 2
trap 151

addi $4, $0, 1 ;yellow pixel at 1,3
addi $5, $0, 3
trap 151

addi $4, $0, 2 ;yellow pixel at 2,3
addi $5, $0, 3
trap 151

addi $4, $0, 3 ;yellow pixel at 3,3
addi $5, $0, 3
trap 151

addi $4, $0, 4 ;yellow pixel at 4,4
addi $5, $0, 2
trap 151

trap 153 ;updates screen




addi $4, $0, 1000 ;delays for one second
trap 105





addi $4, $0, 4072 ;sets the screen to 4072x4072
addi $5, $0, 4072
trap 150

llo $8, 16xFFFF
lhi $8, 16xFF ;stores the number 0xFFFFFF in register 8 (white)

addi $4, $0, 0 
addi $5, $0, 0

loop: ;loops throught every pixel on screen setting it to the rgb color of the index
trap 152
addi $4, $4, 1
addi $5, $5, 1
beq $4, $8, end
j loop
end:
trap 153 ;updates screen
trap 0  ;stops program