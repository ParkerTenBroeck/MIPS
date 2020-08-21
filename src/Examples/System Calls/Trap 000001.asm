;trap 1 prints the integer in $4 to the console
addi $4, $0, 33 ;puts the number 33 into register 4
trap 1 ;prints the number in register 4
trap 0 ;stops the program