package ross.chip8;

public class C8opcodes {

	Chip8 c8System;
	
	public C8opcodes(Chip8 c) {
		c8System = c;
	}
	


/*Push item onto stack,
 * fails if stack is full*/
void push(int item)
{
    if(14 < c8System.sp)
        System.err.print("Stack Overflow\n");
    else{
    	c8System.stack[++c8System.sp] = item;
    }
    
}

/*Pops the byte on top of the stack
 *fails if stack is empty*/ 
int pop()
{
    if(c8System.sp < 0) {
        System.err.println("Empty Stack");
    }
    return c8System.stack[c8System.sp--];
}


//Get bottom 3 nibbles of opcode
int get_NNN()
{
    return (c8System.opcode & 0x0FFF);
}

//Get bottom byte of opcode
int get_NN()
{
    return (c8System.opcode & 0x00FF);
}

//Get bottom nibble of opcode
int get_N()
{
    return (c8System.opcode & 0x000F);
}

//Get 2nd nibble of opcode
int get_X()
{
    return ((c8System.opcode & 0x0F00) >> 8);
}

//Get 3rd nibble of opcode
int get_Y()
{
    return ((c8System.opcode & 0x00F0) >> 4);
}



//Execute op which first nibble is 0
void op_get_0()
{
    if(c8System.opcode == 0x00E0)
        op_00E0();
    else if(c8System.opcode == 0x00EE)
        op_00EE();
    else if((c8System.opcode >> 4) == 0x000C)
        op_00CN();
    else if(c8System.opcode == 0x00FB)
        op_00FB();
    else if(c8System.opcode == 0x00FC)
        op_00FC();
    else if(c8System.opcode == 0x00FD)
        op_00FD(); 
    else if(c8System.opcode == 0x00FE)
        op_00FE();
    else if(c8System.opcode == 0x00FF){
        op_00FF();
    }
    else op_0NNN();
}

//Execute op which first nibble is 8
void op_get_8() {
 
	int opcode = c8System.opcode;
	switch (opcode & 0xF) {
	case 0: op_8XY0(); break;
	case 1: op_8XY1(); break;
	case 2: op_8XY2(); break;
	case 3: op_8XY3(); break;
	case 4: op_8XY4(); break;
	case 5: op_8XY5(); break;
	case 6: op_8XY6(); break;
	case 7: op_8XY7(); break;
	case 8: op_8XYE(); break;
	default: System.err.printf("Error invalid opcode %X\n",opcode);
	}
 
}


//Execute op which first nibble is E
void op_get_E()
{
	int opcode = c8System.opcode;
	switch(opcode & 0xFF) {
		case 0x9E: op_EX9E(); break;
		case 0xA1: op_EXA1(); break;
		default: System.err.printf("Error invalid opcode %X\n",opcode); break;
	}
}
    


//Execute op which first nibble is F
void op_get_F()
{
	int opcode = c8System.opcode;
	
	switch(opcode & 0xFF) {
		case 0x29: op_FX29(); break; 
		case 0x15: op_FX15(); break;
		case 0x07: op_FX07(); break; 
		case 0x55: op_FX55(); break;
		case 0x65: op_FX65(); break;
		case 0x75: op_FX75(); break;
		case 0x85: op_FX85(); break;
		case 0x33: op_FX33(); break;
		case 0x1E: op_FX1E(); break; 
		case 0x18: op_FX18(); break;
		case 0x0A: op_FX0A(); break;
		default: System.err.printf("Error invalid opcode %X\n",opcode); break;
	}
}

void opLookup() {

	int opcode = c8System.opcode;
	switch((opcode & 0xF000) >> 12) {
	case 0x0: op_get_0(); break;
	case 0x1: op_1NNN(); break;
	case 0x2: op_2NNN(); break;
	case 0x3: op_3XNN(); break;
	case 0x4: op_4XNN(); break;
	case 0x5: op_5XY0(); break;
	case 0x6: op_6XNN(); break;
	case 0x7: op_7XNN(); break;
	case 0x8: op_get_8(); break;
	case 0x9: op_9XY0(); break;
	case 0xA: op_ANNN(); break;
	case 0xB: op_BNNN(); break;
	case 0xC: op_CXNN(); break;
	case 0xD: op_DXYN(); break;
	case 0xE: op_get_E(); break;
	case 0xF: op_get_F(); break;
	default: System.err.printf("Error invalid opcode %X\n",opcode); break;
	}
}





/* 0NNN runs RCA 1802 program at address NNN*/
void op_0NNN()
{
    //unused
}

/*Clears the screen*/
void op_00E0()
{
    int i,j;
    for(i = 0; i < 128; i++)
        for(j = 0; j < 64; j++)
        	c8System.gfx[i][j] = 0;

    c8System.graphicsUpdate = true;
}

/*Returns from a subroutine*/
void op_00EE()
{
	c8System.pc = pop();
}

/*Scroll down N lines SCHIP8*/
void op_00CN()
{
    int N = get_N();
    int i,j,k;
    
    for(j = 0; j < 128; j++) {
        for(i = 63; i >= N; i--) {
        	c8System.gfx[j][i] = c8System.gfx[j][i-N];      //Shift down N pixels
        }
        for(k = N-1; k >= 0; k--) {
        	c8System.gfx[j][k] = 0; //Pad top N pixels with blank
        }
    }
    c8System.graphicsUpdate = true;
}



/*Scroll 4 pixels right SCHIP8*/
void op_00FB()
{
    int i,j,k;
    for(i = 0; i < 64; i++) {
        for(j = 128-1; j >= 4; j--) { //Shift pixels 4 pixels right
        	c8System.gfx[j][i] = c8System.gfx[j-4][i]; //

        }
        for(k = 3; k >=0 ; k--) {
        	c8System.gfx[k][i] = 0;    //Pad left 4 pixels with blank

        }
   }
   c8System.graphicsUpdate = true;
}


/*Scroll 4 pixels left SCHIP8*/
void op_00FC()
{
    int i,j,k;
    for(i = 0; i < 64; i++) {
        for(j = 0; j < 128-4; j++) { //Shift pixels 4 pixels left
        	c8System.gfx[j][i] = c8System.gfx[j+4][i];
        }
        for(k = 128 -4; k < 128; k++) {
        	c8System.gfx[k][i] = 0;    //Pad right 4 pixels with blank
        }
    }
    c8System.graphicsUpdate = true;
}


/*Exit*/
void op_00FD()
{
	c8System.end = 1;
}


/*CHIP8 Graphics*/
void op_00FE()
{
	c8System.SCHIP_GRAPHICS = false;
	c8System.gfx_width = 64;
	c8System.gfx_height = 32;
}


/*SCHIP8 Graphics*/
void op_00FF()
{
	c8System.SCHIP_GRAPHICS = true;
	c8System.gfx_width = 128;
	c8System.gfx_height = 64;
}




/*jumps to address NNN*/
void op_1NNN()
{
	c8System.pc = get_NNN();
	c8System.pc -=2; //Decrement PC so increment at end of cycle cancels out
}

/*Calls subroutine at NNN*/
void op_2NNN()
{
	push(c8System.pc);
	c8System.pc = get_NNN(); /*jump*/
	c8System.pc -=2;//Decrement PC so increment at end of cycle cancels out
}

/*Skips the next instruction if VX equals NN*/
void op_3XNN()
{
  if(c8System.V[get_X()] == get_NN()) {
	  c8System.pc+=2;
      c8System.pc &= 0x0FFF;
   }
}

/*Skips the next instruction if VX doesn't equal NN*/
void op_4XNN()
{
    if(c8System.V[get_X()] != get_NN()) {
    	c8System.pc+=2;
    	c8System.pc&= 0x0FFF;
    }
}

/*Skips the next instruction if VX equals VY */
void op_5XY0()
{
    if(c8System.V[get_X()] == c8System.V[get_Y()]) {
    	c8System.pc+=2;
    	c8System.pc&= 0x0FFF;
    }
}

/*Sets VX to NN */
void op_6XNN()
{
	c8System.V[get_X()] = get_NN();
}

/*Adds NN to VX */
void op_7XNN()
{
	c8System.V[get_X()] += get_NN();
	c8System.V[get_X()]&=0xFF;
}

/*Sets VX to the value of VY*/
void op_8XY0()
{
	c8System.V[get_X()] = c8System.V[get_Y()];
}

/*Sets VX to VX OR VY*/
void op_8XY1()
{
	c8System.V[get_X()] |= c8System.V[get_Y()];
}

/*Sets VX to VX AND VY*/
void op_8XY2()
{
	c8System.V[get_X()] &= c8System.V[get_Y()];
}

/*Sets VX to VX XOR VY*/ 
void op_8XY3()
{
	c8System.V[get_X()] ^= c8System.V[get_Y()];
	c8System.V[get_X()]&=0xFF;
}

/*Adds VY to VX. VF set to 1 if carry
 * 0 otherwise*/
void op_8XY4()
{
    int X = get_X();
    int Y = get_Y();

    c8System.V[0xF] = 0;
    if(0xFF - c8System.V[Y] < c8System.V[X])
    	c8System.V[0xF] = 1; //Overflow
     
    c8System.V[X] += c8System.V[Y];
    c8System.V[X]&=0xFF;
}

/*VY is subtracted from VX
 * VF is set to 0 when there's a borrow
 * set to 1 otherwise*/
void op_8XY5()
{
    int X = get_X();
    int Y = get_Y();
    c8System.V[0xF] = 1;
    if(c8System.V[Y] > c8System.V[X])
    	c8System.V[0xF] = 0; //underflow

    c8System.V[X] -=c8System.V[Y];
    c8System.V[X]&=0xFF;
   

}

/* Shifts VX right, 
 * VF is set to value of lsb of VX before shift */
void op_8XY6()
{
    int X = get_X();
    c8System.V[0xF] = (c8System.V[X] & 1);
    c8System.V[X] >>= 1;
    c8System.V[X]&=0xFF;

    
}

/*Sets VX to VY - VX
 *VF is set to 0 when borrow, 1 otherwise */ 
void op_8XY7()
{
    int X = get_X();
    int Y = get_Y();

    c8System.V[0xF] = 1;
    if(c8System.V[X] > c8System.V[Y]) //Underflow
    	c8System.V[0xF] = 0;
     
    c8System.V[X] = (c8System.V[Y] - c8System.V[X])&0xFF;	
}

/*Shifts VX left by 1
 *VF is set to value of msb of VX before shift*/ 
void op_8XYE() 
{
    int X = get_X();
    c8System.V[0xF] = (c8System.V[X] >> 7)%256;
    c8System.V[X] <<= 1;
    c8System.V[X]&=0xFF;
}

/*Skips the next instruction if V[X] not equal to V[Y] */
void op_9XY0()
{
    if(c8System.V[get_X()] != c8System.V[get_Y()]) {
    	c8System.pc+=2;
    	c8System.pc&=0xFFF;
    }
}

/*Sets I to the address NNN*/
void op_ANNN()
{
	c8System.I = get_NNN();
}

/*Jumps to the address NNN plus V0*/
void op_BNNN()
{
	c8System.pc = (get_NNN() + c8System.V[0]);
	c8System.pc-=2;
	c8System.pc &=0xFFF;
}

/*Sets VX to a random number and NN*/
void op_CXNN()
{
    int temprand = (int)(Math.random()*256);
    c8System.V[get_X()] = (int) (temprand & get_NN());
}

void op_DXY0(int x, int y)
{
    int i,j;
    int sprite_line;

    for(i = 0; i < 16; i++) {
        sprite_line = c8System.mem[c8System.I + (2*i)];
        sprite_line <<= 8;
        sprite_line |= c8System.mem[c8System.I + (2*i)+1];

        for(j = 0; j < 16; j++) {

            if((sprite_line & (0x8000 >> j))!=0) {
                 
                if((j+x) < 128 && (i + y) < 64){
                     
                    if(c8System.gfx[(j+x)][(i+y)] == 1)
                    	c8System.V[0xF] = 1;

                    c8System.gfx[(j+x)][(i+y)]^=1;
                } 
            }
        }
    }
    c8System.graphicsUpdate = true;
}

/*Draws a sprite at coordinate VX, VY
 *has a width of 8 pixels and height of n pixels
 *read from memory location I
 *VF set to 1 if any screen pixels are flipped from set to unset*/

 /*If N !=0 draws 8xN sprite*/
 /*If N = 0 SCHIP Mode draws 16x16 sprite*/
 /*If N = 0 CHIP  Mode draws 8x16 sprite*/
void op_DXYN()
{
    int X = get_X();
    int Y = get_Y();
    int N = get_N();
    
    if(c8System.SCHIP_GRAPHICS && N==0) { //Extended draw
        op_DXY0(c8System.V[X],c8System.V[Y]);
        return;
    }
    if(N == 0)
        N= 16;

    int sprite;
    int xline, yline, xPos, yPos;
    c8System.V[0xF] = 0;
     
    for(yline = 0; yline < N; yline++) {

        sprite = c8System.mem[c8System.I+yline];
        yPos = yline + c8System.V[Y];
        for(xline = 0; xline < 8; xline++) {

            xPos = xline + c8System.V[X]; 
            if((sprite & (0x80 >> xline))!=0) { //check if bit set
                if(c8System.gfx[xPos][yPos] == 1) { 
                	c8System.V[0xF] = 1; 
                }
                c8System.gfx[xPos][yPos]^= 1;
               
                      
           }
        }      
   }

   c8System.graphicsUpdate = true;
}

/*Skips the next instruction if the key stored in VX is pressed*/
void op_EX9E()
{
    if(c8System.key_pressed[c8System.V[get_X()]] != 0) {
    	c8System.pc+=2; 
    	c8System.pc &=0xFFF;
    }
}

/*Skips the next instruction if the key stored in VX isn't pressed*/
void op_EXA1()
{
    if(c8System.key_pressed[c8System.V[get_X()]] == 0) {
    	c8System.pc+=2;
    	c8System.pc &=0xFFF;
    }
      
}

/*Sets VX to the value of the delaytimer*/
void op_FX07()
{
	c8System.V[get_X()] = c8System.delay_timer;
}

/*Wait for keypress and store in VX*/
void op_FX0A()
{
	//wait_for_keypress();
	c8System.V[get_X()] = c8System.key_pressed[16]; //Last key pressed stored in key_pressed index 16
}

/*Sets delay timer to VX*/
void op_FX15()
{
	c8System.delay_timer = c8System.V[get_X()];
}

/*Sets sound timer to VX*/
void op_FX18()
{
	c8System.sound_timer = c8System.V[get_X()];
}

/*Adds VX to I, set VF to 1 if overflow*/
void op_FX1E()
{
    int X = get_X();
    if(c8System.I > 0xFFF - c8System.V[X])
    	c8System.V[0xF] = 1;
    else
    	c8System.V[0xF] = 0;

    c8System.I += c8System.V[X];
    c8System.I = (c8System.I & 0xFFF); //ensure doesn't exceed 255

}

/*Sets I to the location of the sprite for the character in VX
 * characters 0-F are represented by a 4x5 font*/
void op_FX29()
{
    //  if(SCHIP_GRAPHICS) {
    //    I = (80 + (V[get_X()] * 0xA));
    //  }
    //  else
	c8System.I = (c8System.V[get_X()] * 0x5) & 0xFFF;

}

/*stores the Binary-coded decimal representation of VX, with the
 * most significant of three digits at the address in I, the middle digit
 * at I + 1, and the lsg at I + 2.*/
void op_FX33()
{
    int X = get_X();
    c8System.mem[c8System.I] = (c8System.V[X]/100);
    c8System.mem[c8System.I+1] =  ((c8System.V[X]%100)/10);
    c8System.mem[c8System.I+2] =  ((c8System.V[X]%100)%10);
}


/*stores V0 to VX in memory starting at address I*/
void op_FX55()
{
    byte i;
    int X = get_X();
    for(i = 0; i <= X; i++)
    	c8System.mem[c8System.I+i] = c8System.V[i];
}


/*Fills V0 to VX with values from memory starting at address I*/
void op_FX65()
{
    int i;
    int X = get_X();
    for(i = 0; i<= X; i++)
    	c8System.V[i] = c8System.mem[c8System.I+i];
}


/*Save V0 to VX X < 8 in HP48 flags*/
void op_FX75()
{
    byte i;
    int X = get_X();
    if(X > 7)
        X=7;
    for(i = 0; i <= X; i++)
    	c8System.HP48_Flags[X] = c8System.V[X];

}

/*Load V0 to VX, X < 8 from HP48 flags*/
void op_FX85()
{
    byte i;
    int X = get_X();
    if(X > 7)
        X = 7;
    for(i = 0; i <= X; i++)
    	c8System.V[X] = c8System.HP48_Flags[X];
}


/*decodes opcode*/
void decode_exec()
{  
    opLookup();      
}

}

