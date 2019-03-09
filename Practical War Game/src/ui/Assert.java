package ui;

public class Assert {
	public static void test(boolean condition, String msg){
		if(condition){
			try {
				throw new Exception(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
