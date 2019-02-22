package ui;

import java.awt.FontMetrics;
import java.awt.Graphics;


public class Util {
	
	
	
	public static int round(double num, double center){
		int numI = (int)num;
		if(Math.abs(num-numI) > Math.abs(num-(numI+1))){
			return numI+1;
		}else if(Math.abs(num-numI) == Math.abs(num-(numI+1))){
			return (int) ((num-center)/Math.abs(num-center) + numI);
		}
		return numI;
	}
	
	public static void drawStringJustified(Graphics g, String s, int px, int py, int width)	{
	    FontMetrics fm = g.getFontMetrics();

	    int lineHeight = fm.getHeight();

	    int curX = px;
	    int curY = py;

	    String[] words = s.split(" ");

	    for (String word : words)
	    {
	        int wordWidth = fm.stringWidth(word + " ");

	        if (curX + wordWidth >= px + width){
	            curY += lineHeight;
	            curX = px; // 가로 허용 범위를 넘어가면 한줄 띄움
	        }

	        g.drawString(word, curX-width/2, (int) (curY-lineHeight/1.5)); //정중앙에 조정
	        curX += wordWidth;
	    }
	}
	

	public static void fillCircleAtCenter(Graphics g, int x, int y, int r){
		g.fillOval(x-r, y-r, 2*r, 2*r);
	}

	public static void fillCircleAtCenter(Graphics g, int[] pixel, int r) {
		fillCircleAtCenter(g, pixel[0],  pixel[1], r);
	}
	
	
}


