
#define width 33
#define height 36
#define frameDelay 100

#definline drawFrame frameLable
llo $7, frameLable
;lhi $7, frameLable ;loads the starting address of the frame into register 7
jal draw ;calls the drawing finction
#endinline

#definline delay ms
addi $4, $0, ms
trap 105
#endinline

main:
addi $4, $0, width
addi $5, $0, height
trap 150

loop:
drawFrame frame0
delay frameDelay
drawFrame frame1
delay frameDelay
drawFrame frame2
delay frameDelay
drawFrame frame3
delay frameDelay
drawFrame frame4
delay frameDelay
drawFrame frame5
delay frameDelay
drawFrame frame6
delay frameDelay
drawFrame frame7
delay frameDelay
j main

draw:;register 7 index offset

addi $4, $0, width
addi $5, $0, height
mult $4, $5
mflo $6 ;calculates the max offset

add $9, $0, $0 ;places zero in offset
add $10, $0, $0; zeros offset

addi $4, $0, 3 ;multiplys max index by 3
mult $4, $6
mflo $6

drawLoop:

add $4, $9, $7;places the index offset + index in register 4

lbu $11, 2($4)
add $5, $0, $11
lbu $11, 1($4)
sll $11, $11, 8
or $5, $11, $5
lbu $11, 0($4)
sll $11, $11, 16
or $5, $11, $5
;places the color value into register 5

add $4, $10, $0;places index in register 4
trap 152;places pixel

addi $9, $9, 3 ;incs offset
addi $10, $10, 1;incs coubter
bne $9, $6, drawLoop
trap 153;updates screen
jr $31;returns 


frame0:
#include frame_0.bin
frame1:
#include frame_1.bin
frame2:
#include frame_2.bin
frame3:
#include frame_3.bin
frame4:
#include frame_4.bin
frame5:
#include frame_5.bin
frame6:
#include frame_6.bin
frame7:
#include frame_7.bin
