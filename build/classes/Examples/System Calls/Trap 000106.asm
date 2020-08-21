;trap 106 speeps for the number of mills in register 4 minus the time it took since the call was last made

;this can be used when loops take an uncertain ammount of time to complete 
;it was always ensure that the loop takes the same ammount of time IF the loop completes in a SHORTER ammount of time then the given delay


;this loop will be completed in 1000ms even with random delay as long as that delay is not greater than 1000ms

loop:

addi $4, $0, string3 ;loads the address of the string into register 4
addi $5, $0, 1 ;sets the step ammount to 1
trap 4 ;prints the ascii string to userIO

addi $4, $0, 0    ;creates a random delay between 0 and 800 ms
addi $5, $0, 800
trap 99
add $4, $0, $2
trap 105

addi $4, $0, string2 ;loads the address of the string into register 4
addi $5, $0, 1 ;sets the step ammount to 1
trap 4 ;prints the ascii string to userIO

addi $4, $0, 1000 ;moves 1000 into register 4
trap 106 ;waits 1000 ms minus the time its been since the call was last made

addi $4, $0, string ;loads the address of the string into register 4
addi $5, $0, 1 ;sets the step ammount to 1
trap 4 ;prints the ascii string to userIO

j loop ;jumps back to loop

string:
.ascii "delay of 1000 ms even with a random delay"
.byte 10,0 ;new line and termination

string2:
.ascii "random delay done"
.byte 10,0 ;new line and termination

string3:
.ascii "loop started"
.byte 10,0 ;new line and termination
