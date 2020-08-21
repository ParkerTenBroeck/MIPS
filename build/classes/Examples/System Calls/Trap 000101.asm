;trap 101 prints the character in register 4 to user IO

addi $4, $0, 67 ;moves 44 into reigster 4 67 is the number used for C in ascii
trap 101;prints character in register 4 to userIO
trap 0;stops the program