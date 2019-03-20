package ui;

public class Assert {
	public static void throwIF(boolean condition, String msg){
		if(condition){
			try {
				throw new Exception(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
