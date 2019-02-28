package ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class ImageLoader {	
	private File dataFolder;

	public ImageLoader(String dirName) {
		File mainframe = new File(ImageLoader.class.getResource("").getPath());
		System.out.println(mainframe.getPath());
		File resource = new File(mainframe.getParentFile().getPath()+ "/"+dirName+"/");
		if(!resource.exists()) resource.mkdirs();
		
		
		dataFolder = resource;
	}
	

	public BufferedImage laodImage(String name){
		try {
			return ImageIO.read(new File(dataFolder.getPath() + name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

}
