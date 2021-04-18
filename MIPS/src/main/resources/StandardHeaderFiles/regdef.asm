.define INCLUDED_STANDARD_REGISTER_HEADER

.define at $1 ;idrk what but used for the assembler

.define v0 $2 ;Used for expression evaluations and to hold the integer type function results. Also used to pass the static link when calling nested procedures.
.define v1 $3

.define a0 $4 ;Used to pass the first 4 words of integer type actual arguments, their values are not preserved across procedure calls.
.define a1 $5
.define a2 $6
.define a3 $7

.define t0 $8 ;Temporary registers used for expression evaluations; their values aren’t preserved across procedure calls.
.define t1 $9
.define t2 $10
.define t3 $11
.define t4 $12
.define t5 $13
.define t6 $14
.define t7 $15

.define s0 $16 ;Saved registers. Their values must be preserved across procedure calls.
.define s1 $17
.define s2 $18
.define s3 $19
.define s4 $20
.define s5 $21
.define s6 $22
.define s7 $23

.define t8 $24 ;Temporary registers used for expression evaluations; their values aren’t preserved across procedure calls.
.define t9 $25

.define k0 $26 ;Reserved for the operating system kernel. used for interrupts.
.define kt0 $26
.define k1 $27
.define kt1 $27

.define gp $28 ;Contains the global pointer.

.define sp $29 ;Contains the stack pointer.

.define fp $30 ;Contains the frame pointer (if needed); otherwise a saved register (like s0-s7).

.define ra $31 ;Contains the return address and is used for expression evaluation.