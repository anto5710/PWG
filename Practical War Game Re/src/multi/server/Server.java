package multi.server;



import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ui.util.Util;



/**
 *  172.30.1.39:3076
 *  
 *  IP  172.30.1.39
 *     특정 기계마다 갖는 고유한 주소를 의미합니다.
 *     
 *  PORT 1 ~ 65535
 *  서버 설정 파일 이름은 - server.config
 *  key=value
 *  key=value 
 */
public class Server {

	private static Map<String, ClientHandler> handlers = new HashMap<>();
	public static String CONFIG_FILE = "server.config";
	private static ClientCleaner cleaner ;
	
	
	public static void main(String[] args) throws IOException {
		init(-1);
	}
	
	public static void init(int customport) throws IOException{
		
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
//				Config config = Config.readConfig(CONFIG_FILE);
				
//				int port = customport<0 ? config.getPort() : customport;
				int port = customport;
				
				ServerSocket serverSock = null;
				try {
					serverSock = new ServerSocket( port );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean running = true;
				cleaner = new ClientCleaner();
				
				
				while ( running ) {
					System.out.println("starting server ... at " +port);
					Socket client = null;
					try {
						client = serverSock.accept();
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // blocking method
					ClientHandler handler = null;
					try {
						handler = new ClientHandler(client);
						handler.sendPublicMSG("Server", Inet4Address.getLocalHost().toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					registerClient(handler);
				}
				try {
					serverSock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
		Thread t = new Thread(r);
		
		t.start();
		
		
	}
	
	public static void registerClient(ClientHandler loginClient){
		loginClient.readNickName();
		loginClient.registerHandler(new DataHandle());

		String name = loginClient.getNickname();
		handlers.put(name, loginClient);
		notifyLogin(name);// handler 제외한 기존의 채팅 참여자들한테 보낼 메세지가 따로 있어야 할 듯?
		sendChatterList(loginClient); // 지금 로그인한 당사자한테
	}
	
	public static void unregisterClient(String name){
		if(handlers.remove(name)!=null)
			notifyLogout(name);
	}
	
	private static void notifyLogin (String name){
		for(ClientHandler handler: getClients()){
			try {
				handler.notifyLogin(name);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	private static void notifyLogout ( String name ) {
		for(ClientHandler handler: getClients()){
			try {
				handler.notifyLogout(name);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	private static void broadcastMove(ClientHandler client, String data) {
		for(ClientHandler handler: getClients()){
			try {
//				if(handler!=client){
					handler.move(data);
//				}
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
				e.printStackTrace();
			}
		}
	}
	
	public static String[] getChatterList(){
		return handlers.keySet().toArray(new String[0]);
	}
	
	private static void sendChatterList(ClientHandler loginUser){
		try {
			loginUser.sendChatterList(getChatterList());
		} catch (IOException e) {
			cleaner.registerDeadClient(loginUser);
		}
	}
	
	public static Collection<ClientHandler> getClients(){
		return handlers.values();
	}
	
	public static void sendPrvMSG (String sender, String msg, String [] receivers){
		Arrays.stream(receivers).map(name -> handlers.get(name)).forEach(hdl -> {
			try {
				hdl.sendPrvMSG(sender, msg, receivers);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void broadcastMSG (String sender, String msg ) {
		for(ClientHandler handler: getClients()){
			try {
				handler.sendPublicMSG(sender, msg);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
				e.printStackTrace();
			}
		}
	}

	
	static class DataHandle implements CommandHandler {
	
		@Override
		public void handle(ClientHandler client, String cmd, Object data) {
			switch ( cmd ) {
			case "MSG" :
				Map<String, Object> map = (Map<String, Object>)data;
				String name = (String) map.get("sender");
				String msg = (String) map.get("msg");
				System.out.printf( "%s: %s\n", name, msg);
				broadcastMSG( name, msg);
				
				break;
			case "MOVE":
				broadcastMove(client, (String)data);
			
			case "LOGOUT" :
				unregisterClient(client.getNickname());
				client.stop();
				
				break;
			}
		}
	}
}
