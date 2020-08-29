;blez if ($s >0) pc += i <<2
;this instruction will branch if the condition $s > 0
;the next instruction is ran if false

main:

llo $4, string0 ;prints string 0
addi $5, $0, 1
trap 4

trap 5 ;user input in register 5

bgtz $2, greaterThan ;checks if $2 is greater than zero or less than / equal

llo $4, string2 ;prints string for greater than
addi $5, $0, 1
trap 4
j end

greaterThan:
llo $4, string1 ;prints string for lessThanOrEqual
addi $5, $0, 1
trap 4

end:
j main


string1:
.ascii "This number is greater than 0"
.byte 10,0

string2:
.ascii "This number is less than or equal to zero"
.byte 10,0

string0:
.ascii "enter a number"
.byte 10,0