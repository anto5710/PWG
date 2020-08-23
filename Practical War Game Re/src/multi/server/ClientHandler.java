package multi.server;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import multi.common.ChatProtocolException;
import multi.common.ChatterList;
import multi.common.IProtocol;
import multi.common.Login;
import multi.common.Logout;
import multi.common.Move;
import multi.common.PublicMessage;
import multi.common.UnknownCommandException;
import ui.util.Logger;
import ui.util.Util;

/**
 * 특정 클라이언트가 보낸 메세지를 처리합니다.
 */
public class ClientHandler implements Runnable {

	private Thread t ;
	private Socket sock ;
	private boolean running = true;
	private DataInputStream dis = null; 
	private DataOutputStream dos = null;
	private String nickName ; 
	
	private Map<String, IProtocol> protocolMap = new HashMap<String, IProtocol>();
	{
		try {
			registerProtocols(Login.class, Logout.class, ChatterList.class, Move.class, PublicMessage.class);
		} catch (Exception e) {
			throw new RuntimeException("Error while registering protocols");
		}
	}
	
	@SafeVarargs
	final private void registerProtocols( Class <? extends IProtocol>...classes) {
		Arrays.stream(classes).forEach(cls -> {
			IProtocol prt = Util.instance(cls);
			protocolMap.put(prt.getCommand(), prt);			
		});
	}
	
	private Set<CommandHandler> handlers = new HashSet<>(); 
	
	public void registerHandler ( CommandHandler handler) {
		this.handlers.add ( handler );
	}
	
	public void unregisterHandler ( CommandHandler handler) {
		this.handlers.remove(handler);
	}
	
	public ClientHandler ( Socket client) throws IOException {
		sock = client ;
		//init
		dis = new DataInputStream(sock.getInputStream());
		dos = new DataOutputStream(sock.getOutputStream());
		t = new Thread(this);
	}
	
	public void readNickName() {
		try {
			// read nickname
			String cmd = dis.readUTF(); // "LOGIN"
			Logger.log(cmd);
			if( "LOGIN".equals(cmd) ) {
				 Object name = protocolMap.get("LOGIN").read(dis);
				 this.nickName = (String) name;
				 start();
			} else throw new ChatProtocolException ( "expected LOGIN but " + cmd );
		} catch (IOException e1) {
			throw new RuntimeException("fail to create stream");
		}
	}

	private void start(){
		if(!t.isAlive()){
			t.setName("T-" + this.nickName);
			t.start();
		}
	}
	
	public void stop(){
		running = false;
		this.t.interrupt();
	}
	
	@Override
	public void run() {
		while ( running ) {
			try {
				String cmd = dis.readUTF(); // MSG, LOGOUT, PRV_MSG
				IProtocol protocol = protocolMap.get(cmd);
				
				if ( protocol != null) {
					Object data = protocol.read(dis); // msg, [a, b, c]
					notifyNewData ( cmd, data );					
				} else {
					throw new UnknownCommandException("invalid CMD? " + "["+cmd+"]" );
				}
			} catch (IOException e) {
				e.printStackTrace();
				stop();
			} 
		}
		/**
		 * TODO
		 * ChatMain.handlers에서 현재 ClientHandler를 제거해야 합니다.
		 * 원래는 아래처럼 제거하기 전에 다른 참여자들에게 로그아웃했다는 메세지를 보내줘야 합니다.
		 */
		Server.unregisterClient(nickName);
	}	
		
	private void notifyNewData(String cmd, Object data) {
		handlers.forEach(ch->ch.handle(this, cmd, data));
	}
	
	public void sendChatterList (String[]nickNames) throws IOException{
		protocolMap.get("CHATTER_LIST").write(dos, nickNames);
	}
	
	public void sendPrvMSG(String sender, String msg, String[] receivers) throws IOException{
		protocolMap.get("PRV_MSG").write(dos, sender, msg, receivers);
	}
	
	public void sendPublicMSG (String sender, String msg ) throws IOException{
		protocolMap.get("MSG").write(dos, sender, msg);
		System.out.println("sent to " + this.nickName + " => " + msg);
	}

	public String getNickname() {
		return nickName;
	}

	public void notifyLogin(String nicknam) throws IOException{
		protocolMap.get("LOGIN").write(dos, nicknam);
	}
	
	public void notifyLogout(String nicknam) throws IOException {
		protocolMap.get("LOGOUT").write(dos, nicknam);
	}

	public void move(String data) throws IOException {
		System.out.println("Server received movement and notifies it to other players");
		System.out.println("Move: "+data);
		protocolMap.get("MOVE").write(dos, data);
	}
}

