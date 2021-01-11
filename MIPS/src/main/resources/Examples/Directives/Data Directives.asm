;these directives store some sort of data or leave space for it

word:
.word 1352255 ;creates a 32 bit integer in memory

wordList:
.word 1,0x2,3,0b100,5 ;creates a list of 32 bit integers in memory

hword:
.hword 0xFF ;creates a 16 bit integer in memory

hwordList:
.hword 4,3,2,1 ;creates a list of 16 bit integers in memory

byte:
.byte 0b10100110 ; creates a 8 bit integer in memory

byteList:
.byte 255,254,2,1 ;creates a list of 8 bit integers in memory

space:
.space 16 ;allocates N bytes of space in memory 

string
.ascii "Hello World!" ;places the string Hello World! into memory as an array of chars(bytes)
.byte 0 ;normally used to terminate a string 