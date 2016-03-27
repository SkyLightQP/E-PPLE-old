package me.efe.skilltree;

public class PitchUtils {
	
	public static float getPitch(int note) {
		float pitch = (float) Math.pow(2.0D, (double) (note - 12) / 12.0);
		
		return pitch;
	}
	
	public static class PitchNote {
		public static int F3s = 0;
		public static int G3 = 1;
		public static int G3s = 2;
		public static int A3 = 3;
		public static int A3s = 4;
		public static int B3 = 5;
		public static int C4 = 6;
		public static int C4s = 7;
		public static int D4 = 8;
		public static int D4s = 9;
		public static int E4 = 10;
		public static int F4 = 11;
		public static int F4s = 12;
		public static int G4 = 13;
		public static int G4s = 14;
		public static int A4 = 15;
		public static int A4s = 16;
		public static int B4 = 17;
		public static int C5 = 18;
		public static int C5s = 19;
		public static int D5 = 20;
		public static int D5s = 21;
		public static int E5 = 22;
		public static int F5 = 23;
		public static int F5s = 24;
	}
}