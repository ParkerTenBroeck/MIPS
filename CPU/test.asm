add $1, $0, $0
lb $2, data($1)
lh $3, data($1)

addi $1, $1, 1
lb $4, data($1)
lh $5, data($1)

addi $1, $1, 1
lb $6, data($1)
lh $7, data($1)

addi $1, $1, 1
lb $8, data($1)
lh $9, data($1)

lw $10, word($0)
addi $1, $0, 1
lw $11, data($1)



data:
.byte 5
.byte 6
.byte 7
.byte 8
.byte 0
word:
.word -923