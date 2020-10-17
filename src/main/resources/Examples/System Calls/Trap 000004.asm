;trap 4 Print the ACIIZ string to User IO starting at the address in $4 and setpping by the number of bytes in $5 always ending with a zero termination

addi $4, $0, string ;loads the memory pointer for the starting byte of string
addi $5, $0, 1 ;sets the stepping ammount to 1 byte per character 
trap 4 ;prints the string
trap 0;halts the program

string:
.ascii "Hello World!"
.byte 0