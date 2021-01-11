;sw MEM [$s + i]:4 = $t
;this saves register t into memory at [$s + i] 
;where register s is some register and i is the immediate value
;

lhi $4, 0x1122
llo $4, 0x3344 ;loads 0x11223344 into register 4

sw $4, memLable($0) ;saves register 4 in memory at an index of memoryLable
;offset by register 0 (always zero)

trap 0;stops program

memLable: ;allocates 4 bytes in memory
.space 4


;NOTE this platform is BIG Endian as 

;0x11223344

;will be stored as

;0x00 0x11
;0x01 0x22
;0x02 0x33
;0x03 0x44
