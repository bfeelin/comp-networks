import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Server {	
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException 
	{
		run();
	}
	
	public static void run() throws IOException, InterruptedException, ClassNotFoundException
	{
		StringBuilder data;
		try
		{
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(9991);
			    System.out.println("Server socket created");
			    Socket clientSocket = serverSocket.accept();
			    String inputLine = null;
	            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	//to read from Client

		        while ((inputLine = input.readLine()) != null) 									//while there are more commands
		        {
		        	if(inputLine.equals("done"))
		        		continue;
		        	ServerThread newST = new ServerThread(clientSocket, inputLine);			//create thread and run
		        	newST.run();
			    }
		        
		       
		}
		catch (IOException e) 
		{
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }
	}
}
