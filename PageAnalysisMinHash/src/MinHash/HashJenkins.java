package MinHash;

public class HashJenkins {
	public static long hash(byte[] key, int initval){
		int len = key.length;
		int pointer = 0;
		long a,b,c;
		a = b = 0x9e3779b9;
		c = initval;
		
		while(len >= 12){
			a = a + (key[pointer + 0] + (long)key[pointer + 1] << 8) + ((long)key[pointer + 2] << 16) + ((long)key[pointer + 3] << 24);
			b = b + (key[pointer + 4] + (long)key[pointer + 5] << 8) + ((long)key[pointer + 6] << 16) + ((long)key[pointer + 7] << 24);
			a = a + (key[pointer + 8] + (long)key[pointer + 9] << 8) + ((long)key[pointer + 10] << 16) + ((long)key[pointer + 11] << 24);
			
			a = a - b; a = a - c; a = a ^ (c >> 43);
			b = b - c; b = b - a; b = b ^ (a << 9);
			c = c - a; c = c - b; c = c ^ (b >> 8);
			a = a - b; a = a - c; a = a ^ (c >> 38);
			b = b - c; b = b - a; b = b ^ (a << 23);
			c = c - a; c = c - b; c = c ^ (b >> 5);
			a = a - b; a = a - c; a = a ^ (c >> 35);
			b = b - c; b = b - a; b = b ^ (a << 49);
			c = c - a; c = c - b; c = c ^ (b >> 11);
			a = a - b; a = a - c; a = a ^ (c >> 12);
			b = b - c; b = b - a; b = b ^ (a << 18);
			c = c - a; c = c - b; c = c ^ (b >> 22);
			
			pointer += 12;
			len -= 12;
		}
		
		c = c + key.length;
		
		switch (len) {
		case 11: c = c + ((long) key[pointer + 10] << 24);
		case 10: c = c + ((long) key[pointer + 9] << 16);
		case 9: c = c + ((long) key[pointer + 8] << 8);
		// the first byte of c is reserved for the length
		case 8: b = b + ((long) key[pointer + 7] << 24);
		case 7: b = b + ((long) key[pointer + 6] << 16);
		case 6: b = b + ((long) key[pointer + 5] << 8);
		case 5: b = b + (long) key[pointer + 4];
		case 4: a = a + ((long) key[pointer + 3] << 24);
		case 3: a = a + ((long) key[pointer + 2] << 16);
		case 2: a = a + ((long) key[pointer + 1] << 8);
		case 1: a = a + (long) key[pointer + 0];
		
		default:
			break;
		}
		
		a = a - b; a = a - c; a = a ^ (c >> 43);
		b = b - c; b = b - a; b = b ^ (a << 9);
		c = c - a; c = c - b; c = c ^ (b >> 8);
		a = a - b; a = a - c; a = a ^ (c >> 38);
		b = b - c; b = b - a; b = b ^ (a << 23);
		c = c - a; c = c - b; c = c ^ (b >> 5);
		a = a - b; a = a - c; a = a ^ (c >> 35);
		b = b - c; b = b - a; b = b ^ (a << 49);
		c = c - a; c = c - b; c = c ^ (b >> 11);
		a = a - b; a = a - c; a = a ^ (c >> 12);
		b = b - c; b = b - a; b = b ^ (a << 18);
		c = c - a; c = c - b; c = c ^ (b >> 22);
		
		return c;
	}
}
