;beq if ($s == $t) pc += i <<2
;this instruction branches if the condition $s == $t is true
;if false the next instruction will be ran

main:

llo $4, string0 ;prints string 0
addi $5, $0, 1
trap 4

addi $4, $0, 12 ;loads 12 into register 4 and a user number into register 2
trap 5

beq $2, $4, equal ;checks if $2 and $4 are equal

llo $4, string1 ;prints string for not equal
addi $5, $0, 1
trap 4
j end

equal:
llo $4, string2 ;prints string for equal
addi $5, $0, 1
trap 4

end:
j main


string1:
.ascii "The number you entered is NOT equal to 12"
.byte 10,0

string2:
.ascii "The number you entered IS equal to 12"
.byte 10,0

string0:
.ascii "enter a number"
.byte 10,0