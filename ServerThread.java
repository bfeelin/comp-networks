import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread 
{
	BufferedReader input = null;		//to read from Client
	PrintWriter output = null;	    		//to write to Client
	Socket clientSocket;
	String cmd;
	String outputLine;
	StringBuilder out = new StringBuilder();
	ServerThread(Socket cSocket, String cmd)
	{
		this.clientSocket = cSocket;
		this.cmd = cmd;
	}
	void run() throws IOException, InterruptedException
	{
		this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			//to read from Client thread
		this.output = new PrintWriter(clientSocket.getOutputStream(), true);							//to write to Client thread
    	System.out.println("Running " + cmd);
    	Process p = Runtime.getRuntime().exec(cmd);									//run the command
    	BufferedReader readOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    	
		while((outputLine = readOutput.readLine()) != null)							//while there is more output
    		output.println(outputLine); 
		output.println("end");
    	p.waitFor();
    	p.destroy();
	}
}
