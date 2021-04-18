.org 0 ;start of the program

.include "functions.h"

main:

loop:

jal outputText
jal getInput
jal calculateScreen
jal drawScreen

j loop



data:

