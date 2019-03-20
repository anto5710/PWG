package ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;



public class FileExplorer {		
	static String HOME = Util.APPDATA();
	private File cur_dir;
	
	public FileExplorer(String start_dir) {
		this(new File(start_dir));
	}
	
	public FileExplorer(File cur) {
		System.out.println(cur.getPath());
		
		if(!cur.exists()) cur.mkdirs();
		
		cur_dir = cur;
	}

	public File curDir(){
		return cur_dir;
	}
	
	public FileExplorer goParent(){
		return new FileExplorer(cur_dir.getParentFile());
	}
	
	public boolean contains(String fileName){
		return new File(cur_dir+"/"+fileName).exists();
	}
	
	public File getFile(String fileName){
		return new File(curDir()+"/"+fileName);
	}
	
	public List<FileExplorer> listSubDirs(){
		List<FileExplorer> children = new ArrayList<>();
		for(File ch: cur_dir.listFiles()){
			if(ch.isDirectory()) children.add(new FileExplorer(ch));
		}
		return children;
	}
	
	public FileExplorer goChild(String dir){
		return new FileExplorer(cur_dir.getPath()+"/"+dir);
	}
	
	private String []imgExtenders = {".png", ".jpg", ".jpeg"};
	public BufferedImage laodUnivImage(String nameWithOutExtender){
		for(String extender: imgExtenders){
			if(contains(nameWithOutExtender+extender)){
				
				return laodImage(nameWithOutExtender+extender);
			}
		}
		return null;
	}
	
	public BufferedImage laodImage(String name){
		File toRead = new File(cur_dir.getPath() +"/"+ name);
		
		if(!toRead.exists()) System.err.println("No Such Image: "+ toRead.getPath());
		try {
			return ImageIO.read(toRead);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public File loadUnivWAV(String name) {
		File toRead = new File(cur_dir.getPath() +"/"+ name+".wav");
		return toRead.exists() ? toRead : null;
	}


}
