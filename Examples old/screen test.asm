
#define stackRegister $29

#define screenSizeX 29

#definline push $a
add $4, $0, $a
sw $a, main(stackRegister)
addi $29, $29, 1
#endinline

#definline pushAndJump address
push $4 
j address
#endinline

main:
trap -1

pushAndJump main 
addi $4, $0, screenSizeX

trap -1
