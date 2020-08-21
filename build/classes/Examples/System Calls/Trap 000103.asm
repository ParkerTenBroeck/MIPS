;trap 103 returns the last character the user entered into the console if empty it reutnrs 0 into register 2

loop:
add $4, $6, $0 ;puts the last character the user pressed in register 4
trap 101 ;prints out the last character the user pressed
addi $4, $0, 10 ;puts the character for new line in register 4
trap 101; prints a new line
trap 103 ; gets the last character the user pressed
add $4, $0, $2 ;moves the character into register 4
bne $4, $0, notZero ;jumps to notZero memory lable if resullt is not zero
j loop ;jumps back to main loop
notZero:
add $6, $4, $0;puts register 4 into 6 only if its not zero
j loop ;jumps back to main loop