package ross.chip8;
//Ross Meikleham 2013

public class Chip8 {

    int opcode; //current operation

    int mem[]; //4k of memory
    int V[]; //16 registers 0-15 general purpose, 16 for carry flag
    int I; //Index register
    int pc; //Program Counter
    int stack[]; //16 levels of stack only stores return addresses
    int HP48_Flags[]; //8 Flag registers for SCHIP
    int sp; //stack pointer

    //Timer registers decrement after every op when set above 0:
    int delay_timer;
    int sound_timer; //makes sound when larger than 0

    Integer gfx[][]; //SCHIP 128*64 pixels, CHIP8 64*32


    final boolean keys_pressed[]; //stores current keys pressed
    long speed; //instructions per second
    boolean debug = false; //debug mode on/off

    boolean SCHIP_GRAPHICS; //Extended graphics mode for SCHIP-8
    boolean graphicsUpdate = false;

    int gfx_width = 64;
    int gfx_height = 32;
    int end = 0;

    int chip8_fontset[] = { //5x16 bytes
        0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
        0x20, 0x60, 0x20, 0x20, 0x70, // 1
        0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
        0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
        0x90, 0x90, 0xF0, 0x10, 0x10, // 4
        0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
        0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
        0xF0, 0x10, 0x20, 0x40, 0x40, // 7
        0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
        0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
        0xF0, 0x90, 0xF0, 0x90, 0x90, // A
        0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
        0xF0, 0x80, 0x80, 0x80, 0xF0, // C
        0xE0, 0x90, 0x90, 0x90, 0xE0, // D
        0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
        0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    int schip8_fontset[] = { //10x16
        0x00, 0x3C, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x3C, 0x00, //0
        0x00, 0x08, 0x38, 0x08, 0x08, 0x08, 0x08, 0x08, 0x3E, 0x00, //1
        0x00, 0x38, 0x44, 0x04, 0x08, 0x10, 0x20, 0x44, 0x7C, 0x00, //2
        0x00, 0x38, 0x44, 0x04, 0x18, 0x04, 0x04, 0x44, 0x38, 0x00, //3
        0x00, 0x0C, 0x14, 0x24, 0x24, 0x7E, 0x04, 0x04, 0x0E, 0x00, //4
        0x00, 0x3E, 0x20, 0x20, 0x3C, 0x02, 0x02, 0x42, 0x3C, 0x00, //5
        0x00, 0x0E, 0x10, 0x20, 0x3C, 0x22, 0x22, 0x22, 0x1C, 0x00, //6
        0x00, 0x7E, 0x42, 0x02, 0x04, 0x04, 0x08, 0x08, 0x08, 0x00, //7
        0x00, 0x3C, 0x42, 0x42, 0x3C, 0x42, 0x42, 0x42, 0x3C, 0x00, //8
        0x00, 0x3C, 0x42, 0x42, 0x42, 0x3E, 0x02, 0x04, 0x78, 0x00, //9
        0x00, 0x18, 0x08, 0x14, 0x14, 0x14, 0x1C, 0x22, 0x77, 0x00, //A
        0x00, 0x7C, 0x22, 0x22, 0x3C, 0x22, 0x22, 0x22, 0x7C, 0x00, //B
        0x00, 0x1E, 0x22, 0x40, 0x40, 0x40, 0x40, 0x22, 0x1C, 0x00, //C
        0x00, 0x78, 0x24, 0x22, 0x22, 0x22, 0x22, 0x24, 0x78, 0x00, //D
        0x00, 0x7E, 0x22, 0x28, 0x38, 0x28, 0x20, 0x22, 0x7E, 0x00, //E
        0x00, 0x7E, 0x22, 0x28, 0x38, 0x28, 0x20, 0x20, 0x70, 0x00  //F
    };


    private static void setZero(Integer mem[][]){
    	for(int i = 0; i < 128; i++)
	    	for(int j = 0; j< 64; j++)
		    	mem[i][j] = 0;
    }

    public Chip8() {
	    this.V = new int[16];
	    this.stack = new int[16];
	    this.HP48_Flags = new int[8];
	    this.gfx = new Integer[128][64];
	    setZero(this.gfx);
	    this.keys_pressed = new boolean[16];
	    this.mem = new int[4096];
    }


    boolean get_key_pressed(int key) {
        synchronized(this.keys_pressed) {
            return keys_pressed[key];
        }
    }

    void set_key_pressed(int key, boolean b) {
        synchronized(this.keys_pressed) {
            this.keys_pressed[key] = b;
        }
    }


}
