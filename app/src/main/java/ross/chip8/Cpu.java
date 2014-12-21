package ross.chip8;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;



public class Cpu {
/* memory map :
 *
 * 0x000-0x1FF - Chip 8 interpreter (font set)
 * 0x050-0x0A0 - Built in 4x5 pixel font set (0-F)
 * 0x200-0xFFF - Program Rom and Ram */

Chip8 c8System;
C8opcodes c8Op;
Context context;
	
public Cpu(Context c, String fileName, Chip8 c8, C8opcodes op) {
	c8System = c8;
	c8Op = op;
	this.context = c;
	load_program(fileName);
}
	
//Load program into memory starting at address 0x200
void load_program(String filename)
{
    int startMem =  0x200;
    int count,fileLen = 0;

    AssetManager am = context.getAssets();
	 
    InputStream f = null;
	try {
		f = am.open("INVADERS");
		fileLen = f.available();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    
    byte[] fileData = new byte[fileLen];
    DataInputStream dis = null;
	
	dis = new DataInputStream(f);
	
    try {
		dis.readFully(fileData);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		dis.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    //Read into memory
    for(int i = startMem; i < fileLen + startMem; i++) {
    	c8System.mem[i] = fileData[i-startMem]&0xFF;
    	//System.out.printf("%d\n",(fileData[i-startMem]));
    	//System.out.printf("%X %X\n",i,(c8System.mem[i]));
    }
    
    //romDump();
    System.out.println("FILE LENGTH "+fileLen);
}


//Obtain current time in microseconds
long get_time_ns()
{
    return System.nanoTime();
}


void reset_system() 
{
    int i;
	// Clear stack
	for(i = 0; i < 16; ++i)
		c8System.stack[i] = 0;

	// Clear memory
	//for(i = 0; i < 4096; ++i)
	//	c8System.mem[i] = 0;

	// Load fontset
	for(i = 0; i < 80; ++i)
		c8System.mem[i] = c8System.chip8_fontset[i];
        
    for(i = 80; i < 240; ++i) {
    	c8System.mem[i] = c8System.schip8_fontset[i-80];
    }	

	// Reset timers
    c8System.delay_timer = 0;
    c8System.sound_timer = 0;
    
    c8System.sp = -1; //Set stack pointer to bottom of stack
    c8System.I = 0;
    c8System.pc = 0x0;
    c8System.opcode = 0;

}

//Set no of instructions to execute per second
void set_speed(long hz)
{
	c8System.speed = hz;
}

/** Initialise CPU **/
void init() {

    reset_system();
    set_speed(100);
    //init_gfx(c8System.key_pressed);
    c8System.pc = 0x200; //Program starts from 200h
}

/** Perform an instruction
 * return whether or not the program has finished**/
boolean step() {

    long wait_time, t_difference, tpi,
            start_time, current_time;

    start_time = get_time_ns();
    fetch();

    c8System.graphicsUpdate = false;

    c8Op.decode_exec();
    c8System.pc+=2; // PC points to location of next instruction

    //System.out.println("PC"+c8System.pc);

    //ensure PC stays within range 0x000-0xFFF
    if(c8System.pc > 0xFFF)
        c8System.pc &= 0xFFF;
        
    if(c8System.delay_timer > 0) c8System.delay_timer--;
    if(c8System.sound_timer > 0) c8System.sound_timer--;

    // Use time it took to execute instruction to determine
    // waiting time
    current_time = get_time_ns();
    t_difference = (current_time - start_time);
    tpi = 1000000/c8System.speed;
    wait_time = tpi - t_difference;
      
    //wait, used to control emulator speed
    if(wait_time > 0) {
        try {
            Thread.sleep(wait_time / 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    return c8System.end == 1;
}

void romDump() {
	for(int i = 0x200; i < 0x300; i++) {
		System.out.printf("%X %X\n",i,c8System.mem[i]);
	}
}


 /*fetches next instruction in memory
  *returns 1 if overflow, 0 otherwise*/
int fetch() 
{   /* 2 byte op-code*/
	c8System.opcode = c8System.mem[c8System.pc];  //Read pc byte into opcode
	c8System.opcode <<= 8; // Shift opcode left by 1 byte

    //load 2nd byte:
    if((c8System.pc + 1) > 0x0FFF) { //Overflow
    	c8System.opcode |= c8System.mem[(c8System.pc + 1) % 0x0FFF];
       //return 1;
    }
    else {
    	c8System.opcode |= c8System.mem[c8System.pc+1];
       //return 0;
    }
    //c8System.opcode %= 0xFFFF;
    //System.out.printf("opcode = %X\n",c8System.opcode);
    return 0;
}






}


