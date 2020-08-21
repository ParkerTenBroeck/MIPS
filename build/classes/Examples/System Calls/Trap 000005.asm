;trap 5 reads an integer from the user console and places the value into register 2

trap 5 ;reads in user integer
add $4, $0, $2 ;moves the value into register 4
trap 1 ; prints the number the user entered
trap 0 ; stops the program