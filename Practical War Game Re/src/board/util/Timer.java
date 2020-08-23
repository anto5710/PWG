package board.util;

public class Timer {
	private long last;
	
	/**
	 * The timer automatically starts running once its constructor is called
	 */
	public Timer() {
		init(); 
	}
	
	/**
	 * initiates(or reset and start) the timer
	 */
	public void init(){
		last = System.currentTimeMillis();
	}
	
	/**
	 * returns the time interval between the current time and the initiated point.
	 * @return time interval in milliseconds
	 */
	public long lap(){
		return System.currentTimeMillis() - last;
	}
	
	/**
	 * laps the point but reinits at the same time
	 * @return time interval in milliseconds.
	 */
	public long stopNreset(){
		long interval = lap();
		init();
		return interval;
	}
}
