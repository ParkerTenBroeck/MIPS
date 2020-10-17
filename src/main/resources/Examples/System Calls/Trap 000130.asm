;trap 130 gets the lower half of system milis
;this can be used for timing 


loop:

addi $4, $0, 0    ;creates a random delay between 0 and 800 ms
addi $5, $0, 800
trap 99
add $4, $0, $2
trap 105

trap 130		;gets the current system half time
add $4, $0, $2  ;prints out value for time
trap 1

addi $4, $0, string1 ;loads the address of the string into register 4
addi $5, $0, 1 ;sets the step ammount to 1
trap 4 ;prints the ascii string to userIO

j loop ;jumps back to loop

string1:
.ascii " is current half time"
.byte 10,0 ;new line and termination