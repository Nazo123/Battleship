package Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHolder {
	public Socket s;
	public DataInputStream is;
	public DataOutputStream os;
	public String name = null;
	public ClientHolder(Socket s) {
		this.s = s;
		try {
		is = new DataInputStream(s.getInputStream());
		os = new DataOutputStream(s.getOutputStream());
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
