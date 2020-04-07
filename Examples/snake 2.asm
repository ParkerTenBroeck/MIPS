main:  
;add $1, $31, $0
;jal pushAll
;jal function
;jal popAll
;add $31, $1, $0
;use this when calling functions outside of main loop 

#define lol 5
#define loll 7
trap lol
trap loll
#undef lol
#define lol 10
trap lol

lw $4, screenSizeX($0) ; setScreenSize
lw $5, screenSizeY($0)
trap 150

add $29, $0, $0;sets stack pointer to 0	

addi $4, $0, 2
sw $4, snakeSize($0) ;sets starting size and direction
addi $4, $0, 1
sw $4, snakeCurrentSize($0)
addi $4, $0, 0
sw $4, snakeDirection($0)

addi $4, $0, 0   ;head

addi $8, $0, 2
lw $5, screenSizeX($0)
div $5, $8
mflo $5    ;middle of screen

addi $8, $0, 2
lw $6, screenSizeY($0)
div $6, $8
mflo $6  ;middle of screen

lw $7, snakeSize($0)

jal writeSnakeArray ;calls write snake array

jal newFoodPos ;set food position
	 
jal drawScreen ;draws the first instance of the screen	
	 
loop:

jal userInput

jal collision ;checks snake collision with self and wall
lb $3, snakeRainbow($0)
bgtz $3, 1
bgtz $4, gameOver ;breaks if collision returns 1

jal snakeStep ;updates snake

jal checkFood ;checks if you ate food

jal drawScreen; draws screen 

addi $4, $0, 40
trap 106

j loop

gameOver:

jal clearScreen;clears screen
jal printGameOverMessage ;prints game over message 
jal userInput
j main 

trap 0 ; end of program


printGameOverMessage:

lw $8, snakeSize($0) ;gets size
addi $8, $8, -2 ;subtracts one (the head)
lw $9, foodWorth($0)
div $8, $9 ;divides length by food worth to get points scored
mflo $4
trap 1
addi $4, $0, 10
trap 101

jr $31;returns 

collision: ;checks collision all collision for snake 
           ;returns 1 in register 4 if snake has hit something
		   
add $1, $31, $0
jal pushAll
jal wallCollision ;reads snake head
jal popAll
add $31, $1, $0

bgtz $4, collisionReturnOne

add $1, $31, $0
jal pushAll
jal selfColision ;reads snake head
jal popAll
add $31, $1, $0
		   
jr $31;returns

collisionReturnOne:

addi $4, $0, 1
jr $31 ;returns 

selfColision: ;checks collision with self

add $4, $0, $0

add $1, $31, $0
jal pushAll
jal readSnakeArray ;reads snake head
jal popAll
add $31, $1, $0

add $10, $4, $0;puts head x and y in register 10 and 11
add $11, $5, $0

lw $8, snakeCurrentSize($0)
addi $8, $8, -1

beq $8, $0, selfColisionReturnZero

selfCollisionLoop:

add $4, $8, $0;puts loop index in register 4

add $1, $31, $0
jal pushAll
jal readSnakeArray ;reads snake head
jal popAll
add $31, $1, $0

sub $4, $4, $10 ; is zero when both x x position and y y positions are the same
sub $5, $5, $11
or $4, $4, $5 

beq $4, $0, selfColisionReturnOne

addi $8, $8, -1
bgtz $8, selfCollisionLoop

selfColisionReturnZero:
add $4, $0, $0
jr $31 ;returns 

selfColisionReturnOne:
addi $4, $0, 1
jr $31 ;returns

wallCollision: ;checks if the snake has hit the wall
               ;returns 1 in register 4 if snake has hit something
			   
add $4, $0, $0

add $1, $31, $0
jal pushAll
jal readSnakeArray ;reads snake head
jal popAll
add $31, $1, $0

lw $8, screenSizeX($0)
addi $8, $8, -1
lw $9, screenSizeY($0)
addi $9, $9, -1

addi $10, $0, 1
beq $6, $10, wallCollisionUp
addi $10, $0, 0
beq $6, $10, wallCollisionRight
addi $10, $0, 3
beq $6, $10, wallCollisionDown
addi $10, $0, 2
beq $6, $10, wallCollisionLeft

wallCollisionUp:
bne $5, $0, wallCollisionReturnFalse
j wallCollisionReturnTrue

wallCollisionRight:
bne $4, $8, wallCollisionReturnFalse
j wallCollisionReturnTrue

wallCollisionDown:
bne $5, $9, wallCollisionReturnFalse
j wallCollisionReturnTrue

wallCollisionLeft:
bne $4, $0, wallCollisionReturnFalse
j wallCollisionReturnTrue

wallCollisionReturnFalse:
add $4, $0, $0
jr $31;returns 0

wallCollisionReturnTrue:
addi $4, $0, 1   
jr $31; returns 1



delay:

addi $8, $0, 300
sll $8, $8, 10

delayLoop:
addi $8, $8, -1
bgtz $8, delayLoop

jr $31 ;returns 

userInput:

addi $8, $0, 10

lw $7, snakeDirection($0)


addi $4, $0, 114 ;r for rainbow
trap 104 ;gets user input
addi $4, $0, 1
blez $2, 1
sb $4, snakeRainbow($0)

addi $4, $0, 110 ;n for noraml
trap 104 ;gets user input
blez $2, 1
sb $0, snakeRainbow($0)


addi $4, $0, 100 ;right (0) aka d
trap 104 ;gets user input
addi $6, $0, 2
beq $7, $6, 1 ;skips if left
bgtz $2, userInputRight

addi $4, $0, 119 ;up (1) aka w
trap 104 ;gets user input
addi $6, $0, 3
beq $7, $6, 1 ;skips if down
bgtz $2, userInputUp

addi $4, $0, 97 ;left (2) aka a
trap 104 ;gets user input
addi $6, $0, 0
beq $7, $6, 1 ;skips if right
bgtz $2, userInputLeft

addi $4, $0, 115 ;down (3) aka s
trap 104 ;gets user input
addi $6, $0, 1
beq $7, $6, 1 ;skips if up
bgtz $2, userInputDown

j endUserInput 

userInputRight:
addi $4, $0, 0
sw $4, snakeDirection($0) ;saves key code
j endUserInput 

userInputUp:
addi $4, $0, 1
sw $4, snakeDirection($0) ;saves key code
j endUserInput 

userInputLeft:
addi $4, $0, 2
sw $4, snakeDirection($0) ;saves key code
j endUserInput  

userInputDown:
addi $4, $0, 3
sw $4, snakeDirection($0) ;saves key code
j endUserInput 

endUserInput:

add $4, $0, $0

add $1, $31, $0
jal pushAll
jal readSnakeArray ;reads snake head
jal popAll
add $31, $1, $0

lw $7, snakeDirection($0) ;loads new direction into register 6
add $6, $5, $0 ;puts y in register 6
add $5, $4, $0 ;puts x in register 5
add $4, $0, $0 ;sets index to 0

add $1, $31, $0
jal pushAll
jal writeSnakeArray ;writes new snake head
jal popAll
add $31, $1, $0

jr $31 ;returns

checkFood:

add $4, $0, $0 ;sets index to zero

add $1, $31, $0
jal pushAll
jal readSnakeArray ;calls read snake array
jal popAll
add $31, $1, $0

add $8, $4, $0;sets register 8 to x
add $9, $5, $0;sets register 9 to y

add $1, $31, $0
jal pushAll
jal getFoodPos ;calls read snake array
jal popAll
add $31, $1, $0

bne $8, $4, checkFoodReturn ;returns x if not equal
bne $9, $5, checkFoodReturn ;returns y if not equal

lw $10, snakeSize($0);loads size
lw $11, foodWorth($0);loads how much to grow
add $10, $10, $11;adds one
sw $10, snakeSize($0)

add $1, $31, $0
jal pushAll
jal newFoodPos ;calls read snake array
jal popAll
add $31, $1, $0

checkFoodReturn:
jr $31;returns

newFoodPos: ;sets food pos to a random location

lw $8, screenSizeX($0)
addi $8, $8, -1
lw $9, screenSizeY($0)
addi $9, $9, -1

add $4, $0, $0
add $5, $8, $0
trap 99
add $10, $2, $0 ;sets register 10 to random x location

add $4, $0, $0
add $5, $9, $0
trap 99
add $11, $2, $0  ;sets register 11 to random y location

add $4, $10, $0  ;puts x and y in register 4 and 5
add $5, $11, $0

add $1, $31, $0
jal pushAll
jal setFoodPos ;calls read snake array
jal popAll
add $31, $1, $0

jr $31;returns

drawFood: ;draws food on screen

add $1, $31, $0
jal pushAll
jal getFoodPos ;calls read snake array
jal popAll
add $31, $1, $0

add $11, $4, $0
add $12, $5, $0
lb $10, snakeRainbow($0)

bgtz $10, 2 ; red food
addi $6, $0, 255
sll $6, $6, 16

blez $10, 8 ; ranbow food
trap 130
sra $4, $2, 3
;addi $8, $0, 1023
;div $2, $8
;mfhi $4
addi $5, $0, 255
addi $6, $0, 255
trap 155
add $6, $4, $0
add $4, $11, $0
add $5, $12, $0

trap 151

jr $31;returns 

getFoodPos:;returns food x pos in register 4
           ;returns foox y pos in register 5
		   
lw $4, foodPos($0)
sra $5, $4, 8
andi $4, $4, 255
andi $5, $5, 255

jr $31;returns 

setFoodPos: ;register 4 is x
            ;register 5 is y
sll $5, $5, 8
or $4, $4, $5
sw $4, foodPos($0) ;saves food pos

jr $31;returns

snakeStep:    ;moves snake one step

lw $8, snakeCurrentSize($0)

addi $8, $8, -1 ;size includes head at index 0

add $1, $31, $0
jal pushAll
jal grow ;calls read snake array
jal popAll
add $31, $1, $0

snakeStepLoop:

add $4, $8, $0 ;puts register 8 in register 4

add $1, $31, $0
jal pushAll
jal snakeSingleStep ;calls read snake array
jal popAll
add $31, $1, $0

addi $8, $8, -1  ;subtracts one from index
addi $9, $8, 1  
bgtz $9, snakeStepLoop ;jump if greater or equal to zero

jr $31 ;returns

grow: ;checks if the snake has to grow and do so if true

lw $8, snakeCurrentSize($0)
lw $9, snakeSize($0)
beq $8, $9, growReturn ;returns if snake deosnt have to grow

addi $4, $8, -1 ;puts last snake index into register 4

add $1, $31, $0
jal pushAll
jal readSnakeArray ;calls read snake array
jal popAll
add $31, $1, $0

add $7, $6, $0  ;puts direction in reg 7
add $6, $5, $0  ;puts y in reg 6
add $5, $4, $0 ;puts x in reg 5
add $4, $8, $0  ;puts index in reg 4

add $1, $31, $0
jal pushAll
jal writeSnakeArray ;writes old tail into further index
jal popAll
add $31, $1, $0

addi $4, $8, 1
sw $4, snakeCurrentSize($0)

growReturn:
jr $31

snakeSingleStep: ;register 4 is index to step


add $16, $4, $0 ;saves index for later

blez $16, snakeSingleStepDirectionIndexZero ;special case for head aka zero index

addi $4, $4, -1 ;next value in snake array (closer to head)

add $1, $31, $0
jal pushAll
jal readSnakeArray  ;gets direction from previouse sname position
jal popAll
add $31, $1, $0


add $7, $6, $0  ;puts next snake direction in register 7
add $6, $5, $0 ;puts x in register 5
add $5, $4, $0 ;puts y in register 6
addi $4, $16, 0

add $1, $31, $0
jal pushAll
jal writeSnakeArray
jal popAll
add $31, $1, $0

jr $31 ;returns

snakeSingleStepDirectionIndexZero:

add $1, $31, $0
jal pushAll
jal readSnakeArray ;calls read snake array
jal popAll
add $31, $1, $0

lw $10, screenSizeX($0)
addi $10, $10, -1
lw $11, screenSizeY($0)
addi $11, $11, -1

addi $7, $0, 0
beq $6, $7, snakeSingleStepRight
addi $7, $0, 1
beq $6, $7, snakeSingleStepUp
addi $7, $0, 2
beq $6, $7, snakeSingleStepLeft
addi $7, $0, 3
beq $6, $7, snakeSingleStepDown

snakeSingleStepRight:
bne $4, $10, 2
add $4, $0, $0
j snakeSingleStepDirection
beq $4, $10, 1
addi $4, $4, 1 ;adds one to x
j snakeSingleStepDirection

snakeSingleStepUp:
bne $5, $0, 2
add $5, $11, $0
j snakeSingleStepDirection
beq $5, $0, 1
addi $5, $5, -1 ;adds one to x
j snakeSingleStepDirection

snakeSingleStepLeft:
bne $4, $0, 2
add $4, $10, $0
j snakeSingleStepDirection
beq $4, $0, 1
addi $4, $4, -1 ;adds one to x
j snakeSingleStepDirection

snakeSingleStepDown:
bne $5, $11, 2
add $5, $0, $0
j snakeSingleStepDirection
beq $5, $11, 1
addi $5, $5, 1 ;adds one to x
j snakeSingleStepDirection


snakeSingleStepDirection:

add $17, $4, $0 ;saves x for later
add $18, $5, $0 ;saves y for later

add $4, $16, $0 ;puts index in register 4 
add $5, $17, $0 ;puts x in register 5
add $6, $18, $0 ;puts y in register 6
lw $7, snakeDirection($0)  ;puts user snake direction in register 7

add $1, $31, $0
jal pushAll
jal writeSnakeArray
jal popAll
add $31, $1, $0

jr $31 ;returns



writeSnakeArray: ;index in register 4
				 ;x pos in register 5
				 ;y pos in register 6
				 ;direction in register 7				
sll $6, $6, 8   
sll $7, $7, 16
or $5, $5, $6
or $5, $5, $7

addi $8, $0, 4 ;multiplys index by 4 because its a word 32 bits 4 bytes
mult $4, $8
mflo $4
sw $5, snake($4)

jr $31 ; returns 

readSnakeArray:  ; register 4 is the index head starts at index 0
                   ;returns x in register 4
				   ;returns y in register 5
                   ;returns direction in register 6 
				   ;
				   
addi $8, $0, 4 ;multiplys index by 4 because its a word 32 bits 4 bytes
mult $4, $8
mflo $4
lw $8, snake($4) ;loads value into register 8

andi $4, $8, 255
sra $5, $8, 8
andi $5, $5, 255
sra $6, $8, 16
andi $6, $6, 255

jr $31 ;returns

drawScreen:

add $1, $31, $0
jal pushAll
jal clearScreen ;clears screen
jal popAll
add $31, $1, $0

add $1, $31, $0
jal pushAll
jal drawSnake ;clears screen
jal popAll
add $31, $1, $0

add $1, $31, $0
jal pushAll
jal drawFood ;clears screen
jal popAll
add $31, $1, $0

trap 153

jr $31 ;returns

drawSnake:

lw $8, snakeCurrentSize($0)

addi $8, $8, -1 ;size includes head at index 0

blez $8, drawSnakeReturn



drawSnakeLoop:

add $4, $8, $0 ;puts register 8 in register 4

add $1, $31, $0
jal pushAll
jal readSnakeArray ;calls read snake array
jal popAll
add $31, $1, $0

lw $10, snakeCurrentSize($0) ;; color fading code
;sub $12, $10, $8
addi $11, $0, 2
div $10, $11
mflo $11
add $10, $11, $10


lb $12, snakeRainbow($0)
addi $6, $0, 255
blez $12, 1
sub $11, $8, $10
bgtz $12, 1
sub $11, $10, $8
mult $6, $11
mflo $6
div $6, $10 ;divice index by snake size
mflo $6

lb $12, snakeRainbow($0)
blez $12, noRainbow

add $10, $4, $0
add $11, $5, $0
addi $4, $6, 0
addi $5, $0, 255
addi $6, $0, 255
trap 155
add $6, $4, $0
add $4, $10, $0
add $5, $11, $0


noRainbow:

lb $12, snakeRainbow($0)
bgtz $12, 1
sll $6, $6, 8
trap 151

addi $8, $8, -1  ;subtracts one from index
addi $9, $8, 0  
bgtz $9, drawSnakeLoop ;jump if greater or equal to zero

drawSnakeReturn:

add $4, $0, $0 ;putsindex  0 into register 4

add $1, $31, $0
jal pushAll
jal readSnakeArray ;calls read snake array
jal popAll
add $31, $1, $0

addi $6, $0, 255
lb $12, snakeRainbow($0)
blez $12, 1
sll $6, $6, 16
bgtz $12, 1
sll $6, $6, 8
trap 151

jr $31;returns

clearScreen:  ;no arguments and arguments dont change
 
 

lw $9, screenSizeY($0) ;loops throught all of screen code 
clearScreenYLoop:

lw $8, screenSizeX($0)
clearScreenXLoop:

addi $4, $8, -1 ;puts x index in register 4
addi $5, $9, -1  ;puts x index in register 5
add $6, $0, $0 ;puts 0 in register 6
trap 151

addi $8, $8, -1
bgtz $8, clearScreenXLoop

addi $9, $9, -1
bgtz $9, clearScreenYLoop
jr $31  ;returns 

if: ; needs $5, $6, $7 register 5 and 6 are the two values being compaired
    ;register 7 is what type of compairison is being used
	;0 (5 == 6), 1(5 != 6), 2 (5 < 6), 3 (5 > 6), 4 (5 <= 6), 5 (5 >= 6) 
	;returns 1 if true or 0 if not in register 7
	
addi $8, $0, 0	
beq $7, $8, ifEqual ;calls the method the user choose
addi $8, $0, 1	
beq $7, $8, ifNotEqual
addi $8, $0, 2	
beq $7, $8, ifLessThan
addi $8, $0, 3	
beq $7, $8, ifGreaterThan
addi $8, $0, 4	
beq $7, $8, ifLessThanOrEqual
addi $8, $0, 5	
beq $7, $8, ifGreaterThanOrEqual

ifEqual:
beq $5, $6, ifReturnOne
j ifReturnZero

ifNotEqual:
bne $5, $6, ifReturnOne
j ifReturnZero

ifLessThan:
slt $8, $5, $6
bgtz $8, ifReturnOne
j ifReturnZero

ifGreaterThan:
slt $8, $6, $5
bgtz $8, ifReturnOne
j ifReturnZero

ifLessThanOrEqual:
addi $6, $6, 1
slt $8, $5, $6
bgtz $8, ifReturnOne
j ifReturnZero

ifGreaterThanOrEqual:
addi $5, $5, 1
slt $8, $6, $5
bgtz $8, ifReturnOne
j ifReturnZero

ifReturnZero:
addi $7, $0, 0 ; puts 0 in return register
jr $31 ; returns

ifReturnOne:
addi $7, $0, 1 ; puts 1 in return register
jr $31 ; returns

pushAll: ;pushes all data to stack
addi $29, $29, 4 ;increments stack pointer 
sw $1, stack($29) ;puts register 1 (previous return address) into stack
addi $29, $29, 4 ;increments stack pointer 
sw $8, stack($29) ;puts register 1 (previous return address) into stack
addi $29, $29, 4 ;increments stack pointer 
sw $9, stack($29) ;puts register 1 (previous return address) into stack
addi $29, $29, 4 ;increments stack pointer 
sw $10, stack($29) ;puts register 1 (previous return address) into stack
addi $29, $29, 4 ;increments stack pointer 
sw $11, stack($29) ;puts register 1 (previous return address) into stack
jr $31;return

popAll: ;pops all data to stack 
lw $11, stack($29) ;puts (previous return address) into register 1
addi $29, $29, -4 ;decrements stack pointer 
lw $10, stack($29) ;puts (previous return address) into register 1
addi $29, $29, -4 ;decrements stack pointer 
lw $9, stack($29) ;puts (previous return address) into register 1
addi $29, $29, -4 ;decrements stack pointer 
lw $8, stack($29) ;puts (previous return address) into register 1
addi $29, $29, -4 ;decrements stack pointer 
lw $1, stack($29) ;puts (previous return address) into register 1
addi $29, $29, -4 ;decrements stack pointer 
jr $31;return 

data: ;data section

snakeRainbow:
.byte 0

screenSizeX:  ;screen size x
.word 62

screenSizeY:  ;screen size y
.word 40

snakeSize:    ;size of the snake or size the snake must grow to
.word 1

snakeCurrentSize: ; must be 1 or more zero is always a part of the snake
.word 1

snakeDirection: ;right is 0, up is 1, 2 is left and 3 is down
.word 0

foodPos:      ;food position
.word 0

foodWorth:    ;the ammount the snake grows by every time he eats 
.word 4

stack: ;stack space
.space 400 

snake: ;snake data
.space 5000 

.space 3