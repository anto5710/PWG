package multi.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.Coord;
import multi.client.IServerListener;
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
 * 서버에서 전달되는 메시지를 처리합니다.
 * @author anto5710
 *
 */
@SuppressWarnings("unchecked")
public class ServerHandler implements Runnable{

	private Thread t;
	private DataInputStream dis ;
	private DataOutputStream dos ;
	
	private boolean running = true;
	private final Socket SERVER;
	private final String IP, NICKNAME;
	private final int PORT;

	private List<IServerListener> listeners = new ArrayList<>();
	
	public void addListener ( IServerListener listener) {
		Logger.log("registering listenr : " + listener);
		this.listeners.add(listener);
	}
	
	public void removeListener ( IServerListener listener) {
		this.listeners.remove(listener);
	}

	private Map<String, IProtocol> protocolMap =new HashMap<String, IProtocol>();
	{
		registerProtocols(Login.class, Logout.class, ChatterList.class, Move.class, PublicMessage.class);
	}
	
	private void registerProtocols(Class <? extends IProtocol>...classes) {
		Arrays.stream(classes).forEach(cls -> {
			IProtocol prt = Util.instance(cls);
			protocolMap.put(prt.getCommand(), prt);			
		});
	}
	
	public ServerHandler( String ip, int port, String nickName ) throws IOException{
		this.IP=ip;
		this.PORT=port;
		this.NICKNAME=nickName;
		this.SERVER=new Socket(ip,port);
		
		dis = new DataInputStream(SERVER.getInputStream());
		dos = new DataOutputStream(SERVER.getOutputStream());
		t = new Thread(this);
		t.setName("T-SH");
		t.start();
	}
	
	public void sendMove(Coord c1, Coord c2) throws IOException{
//		System.out.println("sending c1: "+ c1.toString() + " -> c2: " + c2.toString());
		String code = c1.toString()+"-"+c2.toString();
//		
//		protocolMap.get("MOVE").write(dos, new Object[]{code, ""});
//		System.out.println("sent Move: "+ code);
		sendPublicMSG(code);
		
	}
	
	public void sendLogin(String nickName) throws IOException {
		System.out.println("[" + Thread.currentThread().getName() + "]sending login name " + nickName);
		t.setName("T-" + nickName);
		protocolMap.get("LOGIN").write(dos, new Object[]{nickName});
		System.out.println("[" + Thread.currentThread().getName() + "sent nickname");
	}
	
	public void sendLogout() throws IOException{ 
		protocolMap.get("LOGOUT").write(dos);
	}
	
	@Override
	public void run(){
		System.out.println("[" + Thread.currentThread().getName() + "] starting server handler");
		while ( running ) {
			String cmd = null;
			try {
				System.out.println("[" + Thread.currentThread().getName() + "] waiting from server .." );
				cmd = dis.readUTF();
				System.out.println("[" + Thread.currentThread().getName() + "] comd from server : " + cmd);
				
				/* TODO 이와 같이 프로토콜 해석 구현체를 따로 분리해서 사용합니다. */
				IProtocol protocol = protocolMap.get(cmd);
				if ( protocol != null ) {
					Object data = protocol.read(dis); // String []
					notifyResponse(cmd, data);
					
				} else throw new UnknownCommandException("invalid CMD? " + cmd );
			} catch (IOException e) {
				e.printStackTrace();
				running=false;
			}
		}
		System.out.println("Client Finished");
		Util.tri(()->{
			try {
				dos.close();
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void notifyResponse(String cmd, Object data) {
		Logger.log(" at notifyResponse : " + cmd + ", " + data.toString());
		Logger.log("num of listeners "  + listeners.size());
		listeners.forEach(listner->listner.onDataReceived(cmd, data));
	}
	
	public void sendPublicMSG(String msg) throws IOException {
		protocolMap.get("MSG").write(dos, NICKNAME, msg);
	}

	public void sendPrivateMSG(String msg, String []nickNames) throws IOException {
		protocolMap.get("PRV_MSG").write(dos, NICKNAME, msg, nickNames);
	}
	
	public String getIP() {
		return IP;
	}
	
	public String getNICKNAME() {
		return NICKNAME;
	}
	
	public int getPORT() {
		return PORT;
	}
}
