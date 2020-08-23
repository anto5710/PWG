package multi.common;

import java.io.DataInputStream;
import java.io.IOException;

public class Move extends AProtocol {

	@Override
	public String getCommand() {
		return "MOVE";
	}

	@Override
	public Object read(DataInputStream dis) throws IOException {
		return readStr(dis);
	}

}
