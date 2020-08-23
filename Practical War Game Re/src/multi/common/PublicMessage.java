package multi.common;


import java.io.DataInputStream;
import java.io.IOException;

import ui.util.Util;


public class PublicMessage extends AProtocol{
	@Override
	public String getCommand() {
		return "MSG";
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		String [] arr = readStrs(dis);
		return Util.map("sender,msg", arr);
	}
}
