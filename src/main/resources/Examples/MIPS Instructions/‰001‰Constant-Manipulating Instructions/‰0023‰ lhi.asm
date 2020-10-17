;lhi HH ($t) = i
;loads the immediate value into the upper 16 bits of register t

add $4, $0, $0
lhi $4, 0x87 ;sets register 4 to 0x00870000

lhi $4, memPointer:HH;use :HH or :LH for high half and low half of the memory pointer
llo $4, memPointer:LH

.org 0x00F000F ;origin in memory farther than max 16 bit int (0xFFFF)
memPointer:

trap 0;stops program