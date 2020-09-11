;define inline defines a small bit of code that can be represented by one line (using the identifier of the defined section)
;this is NOT a function call it simply places the code defined within its body where ever it is used
;you can also define a list of 'variables' that will be replaced with whatever you define them as later

;this examples shows that using definline can create its own unique memory labes

#definline printStringAt memLable, printTimes ;prints the string at memoryLable, printTimes times

;changes register 1, 10, 5, 4

add $10, $0, $0 ;zeros register 10

addi $5, $0, 1;loads byte step for string

llo $4, memLable:LH
lhi $4, memLable:HH

loop:
trap 4;prints string
addi $10, $10, 1 ;incs counter
slti $1, $10, printTimes ;jumps if counter is less than printTimes
bgtz $1, loop

#endinline

#define loopTimes 3;defines the ammount of times the program will loop

main: ;start of main program

add $11, $0, $0 ;zeros register 11
loop:

printStringAt string1, 4 ;prints string1 4 times
printStringAt string2, 2;prints string2 2 times

addi $11, $11, 1 ;incs counter
slti $1, $11, loopTimes ;jumps if counter is less than loopTimes
bgtz $1, loop

trap 0;stops the program



string1:
.ascii "This is bruh"
.byte 10, 0

string2:
.ascii "LOL maybe not"
.byte 10, 0