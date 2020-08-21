
addi $1, $0, 0

addi $2, $0, -1
sb $2, data($1)
sb $0, data($1)

sh $2, data($1)
sh $0, data($1)

sw $2, data($1)
sw $0, data($1)

addi $1, $0, 1

addi $2, $0, -1
sb $2, data($1)
sb $0, data($1)

sh $2, data($1)
sh $0, data($1)

sw $2, data($1)
sw $0, data($1)


data:
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0