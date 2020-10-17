;slt $d = ($s < $t)
;slt will put the result of this boolean operation in register d

main:

llo $4, string1:LH ;prints string 1
addi $5, $0, 1
trap 4

trap 5
add $4, $0, $2 ;loads number A into register 4 and number B into register 5
trap 5
add $5, $0, $2

slt $3, $4, $5     ;places boolean result into register 3
bgtz $3, statementTrue ;if boolean is greater than zero the statement is true

llo $4, string3:LH
addi $5, $0, 1
trap 4

j end
statementTrue:

llo $4, string2:LH
addi $5, $0, 1
trap 4

end:

j main

string1:
.ascii "enter two numbers"
.byte 10,0

string2:
.ascii "number A is less than B"
.byte 10,0

string3:
.ascii "number A is greater than or equal to B"
.byte 10,0