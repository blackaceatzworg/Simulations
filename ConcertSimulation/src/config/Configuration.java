package config;

public class Configuration {
	public static class Movement{
	}
	
	public static class Simulation{
		public static final int standardFPS = 30;
		
		
	}
	
	
	public static class World{
		private static float CELL_UNITS = 5;
		
		public static float getCellUnits(){
			return World.CELL_UNITS;
		}
		
		public static final int X_WIDTH = 20;
		public static final int Z_WIDTH = 20;
		public static final int Y_WIDTH = 20;
		
		
	}
	
	public static class Physics{
		
		private static float GRAVITY;
		
		static{
			Physics.GRAVITY = -9.8f;
		}
		
		public static float getGravity(){
			return GRAVITY;
		}
		
		
	}
	
	public static class Human{
		public static final float SIZE_MEN_MEAN = 180;
		public static final float SIZE_MEN_SD=7.5f;
		public static final float SIZE_WOMEN_MEAN = 166;
		public static final float SIZE_WOMEN_SD =6.39f;
		
		
		public static final float BMI_MEN_MEAN = 25;
		public static final float BMI_MEN_SD = 1.5f;
		public static final float BMI_WOMEN_MEAN = 25;
		public static final float BMI_WOMEN_SD = 1.5f;
		
		
		
		
		
		
		
		
	}
	
}
