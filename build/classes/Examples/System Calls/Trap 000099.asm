;trap 99 picks a random number between the values in register 4 and register 5 and places it into register 2

addi $4, $0, 2 ;places 2 into register 4
addi $5, $0, 94 ;places 94 into register 5
trap 99 ;generates a random number
add $4, $0, $2;moves the random number to register 4
trap 1 ;prints the random number
trap 0;stops the program