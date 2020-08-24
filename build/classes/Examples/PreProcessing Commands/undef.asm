;undef is used for undefining anything that has a identifire 

#define ZERO $0

#definline inc
addi $4, ZERO, 1
#endinline

inc 

#define TEN 10

#undef inc
#define inc $6

addi inc, ZERO, TEN
 