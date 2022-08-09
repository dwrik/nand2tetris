// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed.
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

(LOOP)
    @KBD
    D=M

    @CLEAR
    D; JEQ    // goto CLEAR if (KBD == 0) i.e. no keys are pressed

(FILL)
    @value
    M=-1      // value = -1 (1111111111111111)

    @WRITE
    0; JMP    // skip CLEAR

(CLEAR)
    @value
    M=0       // value = 0 (0000000000000000)

(WRITE)
    @8191
    D=A
    @i
    M=D       // i = 8191 (total 8192 16-bit words in SCREEN[0, 8191])

(SCREEN_LOOP)
    @SCREEN
    D=A       // base address of SCREEN (memory map in data memory)

    @i
    D=D+M
    @address
    M=D       // address = address of word in SCREEN (base + offset)

    @value
    D=M
    @address
    A=M
    M=D       // SCREEN[address] = value

    @i
    M=M-1     // i = i - 1
    D=M

    @SCREEN_LOOP
    D; JGE    // goto SCREEN_LOOP if (i >= 0)

    @LOOP
    0; JMP    // goto LOOP
