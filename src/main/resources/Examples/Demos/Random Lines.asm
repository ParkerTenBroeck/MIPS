
#define width 480
#define height 720

addi $4, $0, width
addi $5, $0, height
trap 150

main:

addi $4, $0, 0

llo $5, 0xFFFF
lhi $5, 0xFF
trap 99
sw $2, color($0) ;saves a random color

addi $5, $0, height
trap 99
add $8, $0, $2
trap 99
add $7, $0, $2 ;loads random cords

addi $5, $0, width
trap 99
add $6, $0, $2
trap 99
add $9, $0, $2

add $5, $8, $0
add $4, $9, $0


jal drawLine
trap 153

j main

trap 0

;line drawing section

#define x1 $4
#define y1 $5

#define x2 $6
#define y2 $7


#define dx $8
#define dy $9

#define dx2 $10
#define dy2 $11

#define ix $12
#define iy $13

#define x $14
#define y $15

#define d $24

;register 4,5 p1 x,y 6,7 p2 x,y
drawLine:

;trap 111

add d, $0, $0 ;d = 0

add $16, $0, x1 ;saves x1,y1 ,x2,y2
add $17, $0, y1
add $18, $0, x2
add $19, $0, y2

sub dx, x2, x1; dx = abs(x2 - x1)
bgtz dx, dxABSEnd
nor dx, dx, $0
addi dx, dx, 1
dxABSEnd:

sub dy, y2, y1; dy = abs(y2 - y1)
bgtz dy, dyABSEnd
nor dy, dy, $0
addi dy, dy, 1
dyABSEnd:

add dx2, dx, dx ;dx2 = 2 * dx
add dy2, dy, dy	;dy2 = 2 * dy

slt $1, x1, x2 ; ix = x1 < x2 ? 1 : -1;
bgtz $1, 2
addi ix, $0, -1
j 1
addi ix, $0, 1

slt $1, y1, y2 ;iy = y1 < y2 ? 1 : -1;
bgtz $1, 2
addi iy, $0, -1
j 1
addi iy, $0, 1

add x, $0, x1 ; x = x1
add y, $0, y1 ; y = y1

;trap 111

slt $1, dx, dy 		;if(dx >= dy){}else{lineDrawSegIf}
;nor $1, $1, $0
bgtz $1, lineDrawSegIf 

drawLineLoop1:;while(true)

add $4, $0, x      ;plot x,y with color and save register 4,5,6
add $5, $0, y
lw $6, color($0)
trap 151
add $4, $0, $16
add $5, $0, $17
add $6, $0, $18

beq x, x2, endDrawLine; if(x == x2)break;

add x, x, ix; x += ix;
add d, d, dy2;d += dy2;

slt $1, dx, d ;if(d > dx)
nor $1, $1, $0
bgtz $1, 2
add y, y, iy ;y += iy;
sub d, d, dx2;d -= dx2;

j drawLineLoop1

lineDrawSegIf:

drawLineLoop2: ;while(true)

add $4, $0, x      ;plot x,y with color and save register 4,5,6
add $5, $0, y
lw $6, color($0)
trap 151
add $4, $0, $16
add $5, $0, $17
add $6, $0, $18

beq y, y2, endDrawLine; if(y == y2)break;

add y, y, iy; y += iy;
add d, d, dx2;d += dx2;

slt $1, dy, d ;if(d > dy)
nor $1, $1, $0
bgtz $1, 2
add x, x, ix ;x += ix;
sub d, d, dy2;d -= dy2;

j drawLineLoop2

endDrawLine:
jr $31;return


color:
.word 0xFF0000;
