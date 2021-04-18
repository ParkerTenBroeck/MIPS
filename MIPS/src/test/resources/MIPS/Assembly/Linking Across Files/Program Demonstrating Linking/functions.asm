.org 0x20

.define screenWidth 480
.define screenHeight 360

.global "outputText"
.global "getInput"
.global "calculateScreen"
.global "drawScreen"

outputText:
addi $4, $0, question.byteAddress
add $5, $0, $0
trap 4
jr $31

getInput:
jr $31

calculateScreen:

csLoop:

jr $31

drawScreen:
jr $31


choice:
.word 0

question:
.asciiz "enter 1,2,3,4 for each option" + (char)0x0a

screenData:
.space screenWidth * screenHeight