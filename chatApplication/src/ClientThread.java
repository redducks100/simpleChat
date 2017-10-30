import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread implements Runnable {
	
	private Boolean closed = false;
	private BufferedReader inputStream = null;

	public ClientThread(Boolean closed, BufferedReader inputStream)
	{
		this.closed = closed;
		this.inputStream = inputStream;
	}
	
	@Override
	public void run() {
		String responseLine;
		    try {
		      while ((responseLine = inputStream.readLine()) != null) {
		         if(responseLine.indexOf("@SERVER: /exit") != -1)
			          break;
		    	 System.out.println(responseLine);
		      }
		      closed  = true;
		    } catch (IOException e) {
		      System.err.println("IOException:  " + e);
		    }	
	}
}	
