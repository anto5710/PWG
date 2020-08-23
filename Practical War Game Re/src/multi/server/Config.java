package multi.server;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
	
	private static final Object KEY_CONFIG_PORT = "port";
	
	public static Config readConfig(String path){
		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, String> config = new HashMap<>();
		
		while(sc.hasNext()){
			String line = sc.nextLine();
			System.out.println(line);
			String [] param = line.split("=");
			String key = param[0];
			String val = param[1];
			config.put(key, val);
		}
		return new Config(config);
	}
	
	private final Map<String, String> CONFIG;
	private Config(Map<String, String> config){
		this.CONFIG = config;
	}

	public int getPort() {
		return Integer.parseInt(CONFIG.get(KEY_CONFIG_PORT));
	}
}
