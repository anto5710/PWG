package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

public class Util {
	
	public static boolean between(double min, double num, double max){
		
		return Math.min(min, max) <= num && num <= Math.max(min, max);
	}
	
	public static Color setAlpha(Color color, int alpha){
		if(color==null) return null;
		return new Color(color.getRed(), color.getBlue(), color.getGreen(), alpha);
	}
	
	public static void runAfter(Runnable task, double delay){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				task.run();
			}
		}, (long)(delay*1000));
	}
	
	public static String APPDATA(){
		String appdata;
		String OS = (System.getProperty("os.name")).toUpperCase();
		
		if (OS.contains("WIN")){
		    appdata = System.getenv("AppData");
		}else{
			appdata = System.getProperty("user.home");
		    appdata += "/Library/Application Support";
		}
		return appdata;
	}
	
	public static File getJARdir(Class<?> aclass){
		try {
			return getJarContainingFolder(aclass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static File getJarContainingFolder(Class<?> aclass) throws Exception {
		  CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();
		  URL codeLoc = codeSource.getLocation();
		  
		  File jarFile;
		  
		  if (codeLoc != null) {
		    jarFile = new File(codeLoc.toURI());
		    
		  } else {
		    String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
		    String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
		    // C:'dsd'!
		    
		    jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
		    jarFile = new File(jarFilePath);
		  }
		  return jarFile.getParentFile();
	}
	
	
	public static Color blend(Color... c) {
	    if (c == null || c.length <= 0) {
	        return null;
	    }
	    float ratio = 1f / ((float) c.length);

	    int a = 0;
	    int r = 0;
	    int g = 0;
	    int b = 0;

	    for (int i = 0; i < c.length; i++) {
	        int rgb = c[i].getRGB();
	        int a1 = (rgb >> 24 & 0xff);
	        int r1 = ((rgb & 0xff0000) >> 16);
	        int g1 = ((rgb & 0xff00) >> 8);
	        int b1 = (rgb & 0xff);
	        a += ((int) a1 * ratio);
	        r += ((int) r1 * ratio);
	        g += ((int) g1 * ratio);
	        b += ((int) b1 * ratio);
	    }

	    return new Color(a << 24 | r << 16 | g << 8 | b);
	}
	
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

	    
//	    for(String word: words){
//	    	
//	    	int wordWidth = fm.stringWidth(word);
//	    	LineMetrics lm = fm.getLineMetrics(s, g);
//	    	
//	    	float wordHeight = lm.getStrikethroughOffset();
//	    	
//	    	if(curX + wordWidth > px + width){
//	    		curY += lineHeight;
//	    		curX = px; // 가로 허용 범위를 넘어가면 한줄 띄움
//	    		System.out.println("linneline\n\n");
//	    	}
////	    	System.out.println(width+"<-exP  " + wordWidth+" <-act HH->" + wordHeight);
//	    	g.drawLine(curX-width/2, curY, curX+width/2, curY);
//	    	g.drawLine(curX-width/2, (int) (curY+wordHeight/2), curX+width/2, (int) (curY+wordHeight/2));
//	    	
//	        g.drawString(word, curX-width/2, (int) (curY-wordHeight/2)); //정중앙에 조정
//	        curX += wordWidth;
//	    }
	    
	    for (String word : words) {
	    	int wordWidth = fm.stringWidth(word + " ");
//	    	int wordWidth = fm.stringWidth(word);
	    	int wordHeight = fm.getAscent()+fm.getDescent();
	    	
	        if (curX + wordWidth > px + width){
	            curY += lineHeight;
	            curX = px; // 가로 허용 범위를 넘어가면 한줄 띄움
	        }
//	        System.out.println(width+"<-exP  " + wordWidth+" <-act");
	        g.drawString(word, curX-width/2, (int) (curY-wordHeight*0.8)); //정중앙에 조정
	        curX += wordWidth;
	    }
	}
	
	public static Polygon getNPolygon(double x, double y, int N, double xr, double yr){
		Assert.throwIF(N<=0, N+" sides? It's not a polygon you know?");
		
		int[]xs = new int[N];
		int[]ys = new int[N];
		
		double npie = N%2==0? 1D/N: -0.5;
		
		for(int i=0; i<N; i++){
			xs[i] = (int) Math.round(x+(xr*Math.cos(npie*Math.PI))); 
			ys[i] = (int) Math.round(y+(yr*Math.sin(npie*Math.PI))); 
			npie+= 2D/N;
		}
		return new Polygon(xs, ys, N);
	}

	public static void fillCircleAtCenter(Graphics g, int x, int y, int r){
		g.fillOval(x-r, y-r, 2*r, 2*r);
	}

	public static void fillCircleAtCenter(Graphics g, int[] pixel, int r) {
		fillCircleAtCenter(g, pixel[0],  pixel[1], r);
	}
	
	
	private static HashMap<Font, Float> fonts = new HashMap<>();
	public static Font getFont(String name, int style, float size){
		Stream<Font> same_family = fonts.keySet().stream().filter(f->{
			return f.getFontName().equals(name);});
		
		if(same_family.count()>0){
			Optional<Font> same_size = same_family.filter(f->fonts.get(f)==size).findAny();
			if(same_size.isPresent()) return same_size.get();
			
			return same_family.findAny().get().deriveFont(style, size);
		}
		Font f=  new Font(name, style, (int)size);
		fonts.put(f, f.getSize2D());
		return f;
	}
		
}