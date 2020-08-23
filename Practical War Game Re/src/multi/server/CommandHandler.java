package multi.server;

public interface CommandHandler {
	public void handle(ClientHandler client, String cmd, Object data);	
}