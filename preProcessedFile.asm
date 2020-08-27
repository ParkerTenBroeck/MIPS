main:
jal 3
trap 5
trap 7
trap 10
lw $4,screenSizeX($0)
lw $5,screenSizeY($0)
trap 150
add $29,$0,$0
addi $4,$0,2
sw $4,snakeSize($0)
addi $4,$0,1
sw $4,snakeCurrentSize($0)
addi $4,$0,0
sw $4,snakeDirection($0)
addi $4,$0,0
addi $8,$0,2
lw $5,screenSizeX($0)
div $5,$8
mflo $5
addi $8,$0,2
lw $6,screenSizeY($0)
div $6,$8
mflo $6
lw $7,snakeSize($0)
jal writeSnakeArray
jal newFoodPos
jal drawScreen
loop:
jal userInput
jal collision
lb $3,snakeRainbow($0)
bgtz $3,1
bgtz $4,gameOver
jal snakeStep
jal checkFood
jal drawScreen
addi $4,$0,40
trap 106
j loop
gameOver:
jal clearScreen
jal printGameOverMessage
jal userInput
j main
trap 0
printGameOverMessage:
lw $8,snakeSize($0)
addi $8,$8,-2
lw $9,foodWorth($0)
div $8,$9
mflo $4
trap 1
addi $4,$0,10
trap 101
jr $31
collision:
add $1,$31,$0
jal pushAll
jal wallCollision
jal popAll
add $31,$1,$0
bgtz $4,collisionReturnOne
add $1,$31,$0
jal pushAll
jal selfColision
jal popAll
add $31,$1,$0
jr $31
collisionReturnOne:
addi $4,$0,1
jr $31
selfColision:
add $4,$0,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
add $10,$4,$0
add $11,$5,$0
lw $8,snakeCurrentSize($0)
addi $8,$8,-1
beq $8,$0,selfColisionReturnZero
selfCollisionLoop:
add $4,$8,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
sub $4,$4,$10
sub $5,$5,$11
or $4,$4,$5
beq $4,$0,selfColisionReturnOne
addi $8,$8,-1
bgtz $8,selfCollisionLoop
selfColisionReturnZero:
add $4,$0,$0
jr $31
selfColisionReturnOne:
addi $4,$0,1
jr $31
wallCollision:
add $4,$0,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
lw $8,screenSizeX($0)
addi $8,$8,-1
lw $9,screenSizeY($0)
addi $9,$9,-1
addi $10,$0,1
beq $6,$10,wallCollisionUp
addi $10,$0,0
beq $6,$10,wallCollisionRight
addi $10,$0,3
beq $6,$10,wallCollisionDown
addi $10,$0,2
beq $6,$10,wallCollisionLeft
wallCollisionUp:
bne $5,$0,wallCollisionReturnFalse
j wallCollisionReturnTrue
wallCollisionRight:
bne $4,$8,wallCollisionReturnFalse
j wallCollisionReturnTrue
wallCollisionDown:
bne $5,$9,wallCollisionReturnFalse
j wallCollisionReturnTrue
wallCollisionLeft:
bne $4,$0,wallCollisionReturnFalse
j wallCollisionReturnTrue
wallCollisionReturnFalse:
add $4,$0,$0
jr $31
wallCollisionReturnTrue:
addi $4,$0,1
jr $31
userInput:
addi $8,$0,10
lw $7,snakeDirection($0)
addi $4,$0,114
trap 104
addi $4,$0,1
blez $2,1
sb $4,snakeRainbow($0)
addi $4,$0,110
trap 104
blez $2,1
sb $0,snakeRainbow($0)
addi $4,$0,100
trap 104
addi $6,$0,2
beq $7,$6,1
bgtz $2,userInputRight
addi $4,$0,119
trap 104
addi $6,$0,3
beq $7,$6,1
bgtz $2,userInputUp
addi $4,$0,97
trap 104
addi $6,$0,0
beq $7,$6,1
bgtz $2,userInputLeft
addi $4,$0,115
trap 104
addi $6,$0,1
beq $7,$6,1
bgtz $2,userInputDown
j endUserInput
userInputRight:
addi $4,$0,0
sw $4,snakeDirection($0)
j endUserInput
userInputUp:
addi $4,$0,1
sw $4,snakeDirection($0)
j endUserInput
userInputLeft:
addi $4,$0,2
sw $4,snakeDirection($0)
j endUserInput
userInputDown:
addi $4,$0,3
sw $4,snakeDirection($0)
j endUserInput
endUserInput:
add $4,$0,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
lw $7,snakeDirection($0)
add $6,$5,$0
add $5,$4,$0
add $4,$0,$0
add $1,$31,$0
jal pushAll
jal writeSnakeArray
jal popAll
add $31,$1,$0
jr $31
checkFood:
add $4,$0,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
add $8,$4,$0
add $9,$5,$0
add $1,$31,$0
jal pushAll
jal getFoodPos
jal popAll
add $31,$1,$0
bne $8,$4,checkFoodReturn
bne $9,$5,checkFoodReturn
lw $10,snakeSize($0)
lw $11,foodWorth($0)
add $10,$10,$11
sw $10,snakeSize($0)
add $1,$31,$0
jal pushAll
jal newFoodPos
jal popAll
add $31,$1,$0
checkFoodReturn:
jr $31
newFoodPos:
lw $8,screenSizeX($0)
addi $8,$8,-1
lw $9,screenSizeY($0)
addi $9,$9,-1
add $4,$0,$0
add $5,$8,$0
trap 99
add $10,$2,$0
add $4,$0,$0
add $5,$9,$0
trap 99
add $11,$2,$0
add $4,$10,$0
add $5,$11,$0
add $1,$31,$0
jal pushAll
jal setFoodPos
jal popAll
add $31,$1,$0
jr $31
drawFood:
add $1,$31,$0
jal pushAll
jal getFoodPos
jal popAll
add $31,$1,$0
add $11,$4,$0
add $12,$5,$0
lb $10,snakeRainbow($0)
bgtz $10,2
addi $6,$0,255
sll $6,$6,16
blez $10,8
trap 130
sra $4,$2,3
addi $5,$0,255
addi $6,$0,255
trap 155
add $6,$4,$0
add $4,$11,$0
add $5,$12,$0
trap 151
jr $31
getFoodPos:
lw $4,foodPos($0)
sra $5,$4,8
andi $4,$4,255
andi $5,$5,255
jr $31
setFoodPos:
sll $5,$5,8
or $4,$4,$5
sw $4,foodPos($0)
jr $31
snakeStep:
lw $8,snakeCurrentSize($0)
addi $8,$8,-1
add $1,$31,$0
jal pushAll
jal grow
jal popAll
add $31,$1,$0
snakeStepLoop:
add $4,$8,$0
add $1,$31,$0
jal pushAll
jal snakeSingleStep
jal popAll
add $31,$1,$0
addi $8,$8,-1
addi $9,$8,1
bgtz $9,snakeStepLoop
jr $31
grow:
lw $8,snakeCurrentSize($0)
lw $9,snakeSize($0)
beq $8,$9,growReturn
addi $4,$8,-1
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
add $7,$6,$0
add $6,$5,$0
add $5,$4,$0
add $4,$8,$0
add $1,$31,$0
jal pushAll
jal writeSnakeArray
jal popAll
add $31,$1,$0
addi $4,$8,1
sw $4,snakeCurrentSize($0)
growReturn:
jr $31
snakeSingleStep:
add $16,$4,$0
blez $16,snakeSingleStepDirectionIndexZero
addi $4,$4,-1
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
add $7,$6,$0
add $6,$5,$0
add $5,$4,$0
addi $4,$16,0
add $1,$31,$0
jal pushAll
jal writeSnakeArray
jal popAll
add $31,$1,$0
jr $31
snakeSingleStepDirectionIndexZero:
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
lw $10,screenSizeX($0)
addi $10,$10,-1
lw $11,screenSizeY($0)
addi $11,$11,-1
addi $7,$0,0
beq $6,$7,snakeSingleStepRight
addi $7,$0,1
beq $6,$7,snakeSingleStepUp
addi $7,$0,2
beq $6,$7,snakeSingleStepLeft
addi $7,$0,3
beq $6,$7,snakeSingleStepDown
snakeSingleStepRight:
bne $4,$10,2
add $4,$0,$0
j snakeSingleStepDirection
beq $4,$10,1
addi $4,$4,1
j snakeSingleStepDirection
snakeSingleStepUp:
bne $5,$0,2
add $5,$11,$0
j snakeSingleStepDirection
beq $5,$0,1
addi $5,$5,-1
j snakeSingleStepDirection
snakeSingleStepLeft:
bne $4,$0,2
add $4,$10,$0
j snakeSingleStepDirection
beq $4,$0,1
addi $4,$4,-1
j snakeSingleStepDirection
snakeSingleStepDown:
bne $5,$11,2
add $5,$0,$0
j snakeSingleStepDirection
beq $5,$11,1
addi $5,$5,1
j snakeSingleStepDirection
snakeSingleStepDirection:
add $17,$4,$0
add $18,$5,$0
add $4,$16,$0
add $5,$17,$0
add $6,$18,$0
lw $7,snakeDirection($0)
add $1,$31,$0
jal pushAll
jal writeSnakeArray
jal popAll
add $31,$1,$0
jr $31
writeSnakeArray:
sll $6,$6,8
sll $7,$7,16
or $5,$5,$6
or $5,$5,$7
addi $8,$0,4
mult $4,$8
mflo $4
sw $5,snake($4)
jr $31
readSnakeArray:
addi $8,$0,4
mult $4,$8
mflo $4
lw $8,snake($4)
andi $4,$8,255
sra $5,$8,8
andi $5,$5,255
sra $6,$8,16
andi $6,$6,255
jr $31
drawScreen:
add $1,$31,$0
jal pushAll
jal clearScreen
jal popAll
add $31,$1,$0
add $1,$31,$0
jal pushAll
jal drawSnake
jal popAll
add $31,$1,$0
add $1,$31,$0
jal pushAll
jal drawFood
jal popAll
add $31,$1,$0
trap 153
jr $31
drawSnake:
lw $8,snakeCurrentSize($0)
addi $8,$8,-1
blez $8,drawSnakeReturn
drawSnakeLoop:
add $4,$8,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
lw $10,snakeCurrentSize($0)
addi $11,$0,2
div $10,$11
mflo $11
add $10,$11,$10
lb $12,snakeRainbow($0)
addi $6,$0,255
blez $12,1
sub $11,$8,$10
bgtz $12,1
sub $11,$10,$8
mult $6,$11
mflo $6
div $6,$10
mflo $6
lb $12,snakeRainbow($0)
blez $12,noRainbow
add $10,$4,$0
add $11,$5,$0
addi $4,$6,0
addi $5,$0,255
addi $6,$0,255
trap 155
add $6,$4,$0
add $4,$10,$0
add $5,$11,$0
noRainbow:
lb $12,snakeRainbow($0)
bgtz $12,1
sll $6,$6,8
trap 151
addi $8,$8,-1
addi $9,$8,0
bgtz $9,drawSnakeLoop
drawSnakeReturn:
add $4,$0,$0
add $1,$31,$0
jal pushAll
jal readSnakeArray
jal popAll
add $31,$1,$0
addi $6,$0,255
lb $12,snakeRainbow($0)
blez $12,1
sll $6,$6,16
bgtz $12,1
sll $6,$6,8
trap 151
jr $31
clearScreen:
lw $9,screenSizeY($0)
clearScreenYLoop:
lw $8,screenSizeX($0)
clearScreenXLoop:
add $6,$0,$0
addi $4,$8,-1
addi $5,$9,-1
trap 151
addi $8,$8,-1
bgtz $8,clearScreenXLoop
addi $9,$9,-1
bgtz $9,clearScreenYLoop
jr $31
if:
addi $8,$0,0
beq $7,$8,ifEqual
addi $8,$0,1
beq $7,$8,ifNotEqual
addi $8,$0,2
beq $7,$8,ifLessThan
addi $8,$0,3
beq $7,$8,ifGreaterThan
addi $8,$0,4
beq $7,$8,ifLessThanOrEqual
addi $8,$0,5
beq $7,$8,ifGreaterThanOrEqual
ifEqual:
beq $5,$6,ifReturnOne
j ifReturnZero
ifNotEqual:
bne $5,$6,ifReturnOne
j ifReturnZero
ifLessThan:
slt $8,$5,$6
bgtz $8,ifReturnOne
j ifReturnZero
ifGreaterThan:
slt $8,$6,$5
bgtz $8,ifReturnOne
j ifReturnZero
ifLessThanOrEqual:
addi $6,$6,1
slt $8,$5,$6
bgtz $8,ifReturnOne
j ifReturnZero
ifGreaterThanOrEqual:
addi $5,$5,1
slt $8,$6,$5
bgtz $8,ifReturnOne
j ifReturnZero
ifReturnZero:
addi $7,$0,0
jr $31
ifReturnOne:
addi $7,$0,1
jr $31
pushAll:
addi $29,$29,4
sw $1,stack($29)
addi $29,$29,4
sw $8,stack($29)
addi $29,$29,4
sw $9,stack($29)
addi $29,$29,4
sw $10,stack($29)
addi $29,$29,4
sw $11,stack($29)
jr $31
popAll:
lw $11,stack($29)
addi $29,$29,-4
lw $10,stack($29)
addi $29,$29,-4
lw $9,stack($29)
addi $29,$29,-4
lw $8,stack($29)
addi $29,$29,-4
lw $1,stack($29)
addi $29,$29,-4
jr $31
data:
screenSizeX:
.word 62
screenSizeY:
.word 40
snakeSize:
.word 1
snakeCurrentSize:
.word 1
snakeDirection:
.word 0
foodPos:
.word 0
foodWorth:
.word 4
stack:
.space 400
snake:
.space 5000
.space 3
snakeRainbow:
.byte 0
