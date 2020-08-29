;sltu $d = ($s < $t)
;sltu will put the result of this boolean operation in register d
;this operation is unsigned

main:

llo $4, string1:LH
addi $5, $0, 1
trap 4

trap 5
add $4, $0, $2
trap 5
add $5, $0, $2

sltu $3, $4, $5 
bgtz $3, statementTrue

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