
#define stackRegister $29

#define screenSizeX 29

#define push($a)[
add $4, $0, $a
sw $a, main($stackRegister)
add $29, $29, 1
]

#define pushAndJump(address)[
push($4)
j address
]

main:
trap -1

pushAndJump(main)
addi $4, $0, screenSizeX

trap -1
