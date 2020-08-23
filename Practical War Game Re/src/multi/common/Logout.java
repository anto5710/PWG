package multi.common;

import java.io.DataInputStream;
import java.io.IOException;

public class Logout extends AProtocol{
	@Override
	public String getCommand() {
		return "LOGOUT";
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		return readStr(dis);
	}
}

