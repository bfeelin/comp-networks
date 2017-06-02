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
				ServerSocket serverSocket = new ServerSocket(9991);
			    System.out.println("Server socket created");
			    Socket clientSocket = serverSocket.accept();
			    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);							//to write to Client
			    String inputLine = null;
	            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	//to read from Client
	            String outputLine;
	            
	            
		        while ((inputLine = input.readLine()) != null) 									//while there are more commands
		        {
		        	if(inputLine.equals("last"))										//flag when done
		        	{	
		        		output.println("last");
		        		continue;
		        	}
		        	System.out.println("Running " + inputLine);
		        	Process p = Runtime.getRuntime().exec(inputLine);									//run the command
		        	BufferedReader readOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        	
					while((outputLine = readOutput.readLine()) != null)							//while there is more output
		        		output.println(outputLine); 
					output.println("end");
					
		        	p.waitFor();
		        	p.destroy();
			    }
		        
		       
		}
		catch (IOException e) 
		{
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }
	}
}
