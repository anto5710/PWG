package multi.client;


public interface IServerListener {
	public void onDataReceived(String cmd, Object data);
}

