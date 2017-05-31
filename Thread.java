import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Thread 
{
	public Socket socket;
	public long respTime;
	Thread(Socket clientSocket)
	{
		this.socket = clientSocket;
	}
	long getRespTime()
	{
		return this.respTime;
	}

	void start(String cmd) throws IOException, InterruptedException
	{
		System.out.println("Running " + cmd);	
		run(cmd);
		System.out.println("Finished executing " + cmd + "Response time : " + this.respTime);
	}
	void run(String cmd) throws IOException, InterruptedException
	{
		String outputLine = null;
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec(cmd);							//run the command
    	BufferedReader readOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));	 //to read what is printed by the command
    	PrintWriter output = new PrintWriter(this.socket.getOutputStream(), true);							//to write to Client
    	while((outputLine = readOutput.readLine()) != null)							//while there is more output
    		output.println(outputLine);												//send it to the client
    	this.respTime = startTime - System.nanoTime();
    														//send "end" to indicate end of output
    	p.waitFor();
    	p.destroy();
	    
	}
}

