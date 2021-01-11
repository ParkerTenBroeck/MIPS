;define inline defines a small bit of code that can be represented by one line (using the identifier of the defined section)
;this is NOT a function call it simply places the code defined within its body where ever it is used
;you can also define a list of 'variables' that will be replaced with whatever you define them as later


#definline return  
jr $31
#endinline

#definline push $a
addi $29, $29, 4
sw $a, stack, $29 
#endinline

#definline pop $a 
lw $a, stack, $29 
addi $29, $29, -4
#endinline

#definline pushAll  
push $31 
push $30 
push $28 
push $23 
push $22 
push $21 
push $20 
push $19 
push $18 
push $17 
push $16 
push $7 
push $6 
push $5 
push $4 
#endinline

#definline popAll  
pop $4 
pop $5 
pop $6 
pop $7 
pop $16 
pop $17 
pop $18 
pop $19 
pop $20 
pop $21 
pop $22 
pop $23 
pop $28 
pop $30 
pop $31 
#endinline

#definline call location 
pushAll  
jal location
popAll  
#endinline

.org 0 ;start of program

Main:
addi $4, $0, 6
call factorial 
;add $4, $2, $0
;call factorial 

trap 111
trap 0; end program




factorial:
addi $8, $4, -1
blez $8, factorialEndCase
addi $16, $4, 0 ;saves register 4 
addi $4, $4, -1
call factorial   ;decrements value and calls factorial
addi $4, $16, 0
mult $2, $4
mflo $2
return  

factorialEndCase:
addi $2, $0, 1
return  

stack:
.space 100000