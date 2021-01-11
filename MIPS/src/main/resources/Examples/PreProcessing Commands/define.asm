;define can be used for many things including using defined values for pre processor if/ifelse statements
#define gold 7

#define reg $2
#define zero $0

addi reg, zero, gold

#define reg $5 ;this will give you a warning because reg is apready defined use undef reg for proper convention

addi reg, zero, gold

#define NAME EXAMPLE_DEFINE ;this can used in if and ifelse statements

