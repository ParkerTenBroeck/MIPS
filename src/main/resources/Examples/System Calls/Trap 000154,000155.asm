;trap 154 and 155 both take in the same data  hue -> $4, saturation -> $5, brightness -> $6
;but trap 154 returns red -> $4 (0-255), green -> $5 (0-255), blue -> $6 (0-255)
;and trap 155 returns RGB -> $4


main:
addi $4, $0, 480
addi $5, $0, 320
trap 150 ; set screen size

mult $4, $5
mflo $6
addi $7, $0, 255
div $6, $7
mflo $7

mult $4, $5
mflo $8
addi $9, $0, 0
addi $5, $0, 255
addi $5, $5, 1

loop:
addi $4, $9, 0
add $5, $0, $7
div $4, $5
mflo $4
jal colorWheel
addi $4, $9, 0
trap 152

addi $9, $9, 1
beq $9, $8, end
j loop

end:

trap 153
trap 0

colorWheel:
addi $5, $0, 255 ;get a colorWheel value with bright ness and saturation 255 (max) and a hue from register 4
addi $6, $5, 0
trap 155
addi $5, $4, 0
jr $31