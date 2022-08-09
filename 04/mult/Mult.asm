// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

    @product
    M=0        // product = 0

    @R0
    D=M
    @RESULT
    D; JEQ     // goto RESULT if R0 = 0
    @operand0
    M=D        // operand0 = R0

    @R1
    D=M
    @RESULT
    D; JEQ     // goto RESULT if R1 = 0
    @operand1
    M=D        // operand1 = R1

    @i
    M=D        // i = operand1

(LOOP)
    @operand0
    D=M
    @product
    M=D+M      // product = product + operand0

    @i
    M=M-1      // i = i - 1
    D=M

    @LOOP
    D; JGT     // if (i > 0), goto LOOP

(RESULT)
    @product
    D=M
    @R2
    M=D        // R2 = product

(END)
    @END
    0; JMP     // infinite loop
