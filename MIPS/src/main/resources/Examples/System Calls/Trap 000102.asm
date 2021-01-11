;trap 102 reads a character from the user console and places the ascii value into register 2

addi $6, $0, 97 ;moves the value for ascii a into register 6
loop: ;memory lable loop 
trap 102 ;inputs the next character from the UserIO
add $4, $0, $2 ;moves the character to register 4
trap 101 ;prints the character just read 
bne $4,$6, loop ;branches to loop if the character read is NOT a lowercase a
trap 0;stops the brogram