import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	  private String hostName = null;
	  private int portNumber = 0;
	  private Socket clientSocket = null;
	  private PrintStream outputStream = null;
	  private BufferedReader inputStream = null;
	  private BufferedReader inputLine = null;
	  private Boolean closed = false;
	  
	  public Client(String hostName, int portNumber)
	  {
		  this.hostName = hostName;
		  this.portNumber = portNumber;
	  }
	  
	  public void start()
	  {
	    try {
	      clientSocket = new Socket(hostName, portNumber);
	      inputLine = new BufferedReader(new InputStreamReader(System.in));
	      outputStream = new PrintStream(clientSocket.getOutputStream());
	      inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    } catch (UnknownHostException e) {
	      System.err.println("Couldn't find host: " + hostName);
	    } catch (IOException e) {
	      System.err.println("Couldn't get I/O stream for the connection to the host: "+ hostName);
	    }

	    if (isInitialised()) {
	      try {
	        Thread clientThread = new Thread(new ClientThread(closed, inputStream));
	        clientThread.start();
	        while (!closed) {
	          outputStream.println(inputLine.readLine().trim());
	        }
	        outputStream.close();
	        inputStream.close();
	        clientSocket.close();
	      } catch (IOException e) {
	        System.err.println("IOException:  " + e);
	      }
	    }
	  }
	  
	  private boolean isInitialised()
	  {
		  return (clientSocket != null && outputStream != null && inputStream != null);
	  }
}
