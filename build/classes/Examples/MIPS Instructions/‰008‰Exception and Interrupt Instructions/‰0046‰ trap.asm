;traps (system calls) is how the program talks to the rest of the syste
;usally system calls are used for input / output of data but can also be used for 
;calculating values that would be hard normally (like random numbers and HSV color conversion)


addi $4, $0, message1
addi $5, $0, 1;byte string
trap 4 ;prints message 1



trap 5
add $5, $0, $2;keeps first int in registers 5 and 8
add $8, $0, $5

trap 5
add $4, $0, $2;keeps second int in registers 4 and 9
add $9, $0, $4

trap 99
add $10, $0, $2 ;stores the random number in register 10

addi $4, $0, message2
addi $5, $0, 1;byte string
trap 4 ;prints message 2

add $4, $0, $8 ;prints out first integer
trap 1

addi $4, $0, message3
addi $5, $0, 1;byte string
trap 4 ;prints message 3


add $4, $0, $9 ;prints second integer
trap 1

addi $4, $0, message4
addi $5, $0, 1;byte string
trap 4 ;prints message 4


add $4, $0, $10 ;prints out random number
trap 1

trap 0;stops the program


message1:
.ascii "Enter two numbers"
.byte 10,0 

message2:
.ascii "a random number between "
.byte 0 

message3:
.ascii " and "
.byte 0 

message4:
.ascii " is "
.byte 0 