import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		run();
	}
	
	public static void run() throws IOException, InterruptedException
	{
		try
		{
			    @SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(9990);
			    System.out.println("Server socket created");
			    Socket clientSocket = serverSocket.accept();
			    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);							//to write to Client
			    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	//to read from Client
			    
			    String inputLine = null, outputLine = null;
	            while ((inputLine = input.readLine()) != null) 									//while there are more commands
	            {	
	            	System.out.println("Running " + inputLine);									           	
	            	Process p = Runtime.getRuntime().exec(inputLine);							//run the command
	            	BufferedReader readOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));	 //to read what is printed by the command
	            	
	            	while((outputLine = readOutput.readLine()) != null)							//while there is more output
	            		output.println(outputLine);												//send it to the client
	            	
	            	output.println("end");
	            	p.waitFor();
	            	System.out.println("Finished executing " + inputLine);
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
