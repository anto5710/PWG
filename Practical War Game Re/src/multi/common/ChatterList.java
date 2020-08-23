package multi.common;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.activation.DataSource;



public class ChatterList extends AProtocol{

	@Override
	public String getCommand() {
		return "CHATTER_LIST";
	}
	
	/*
	@Override
	public void write(DataOutputStream dos, Object...data) throws IOException {
		super.write(dos, data);
		System.out.println(
				String.format("[%s] %d %s", 
								getCommand(), data.length, 
								Arrays.toString(data)));	
	}
	*/
	@Override
	public Object read(DataInputStream dis) throws IOException {
		return readStrs(dis);
	}	
	
}
