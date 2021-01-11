;trap 105 sleeps for the numbers of milliseconds in register 4


loop:
addi $4, $0, string ;loads the address of the string into register 4
addi $5, $0, 1 ;sets the step ammount to 1
trap 4 ;prints the ascii string to userIO
addi $4, $0, 1000 ;moves 1000 into register 4
trap 105 ;waits 1000 ms 
j loop ;jumps back to loop

string:
.ascii "delay of 1000 ms"
.byte 10,0 ;new line and termination