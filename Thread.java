import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;

public class Thread 
{
	public String cmd;
	public long startTime;
	public long endTime;
	BufferedReader input = null;		//to read from Server
	PrintWriter output = null;	    		//to write to Server
	boolean last = false;
	StringBuilder res = new StringBuilder();
	String results;
	
	Thread(BufferedReader input, PrintWriter output, String cmd)
	{
		this.input = input;
		this.output = output;
		this.cmd = cmd;
	}
	long getStartTime()
	{
		return this.startTime;
	}
	long getEndTime()
	{
		return this.endTime;
	}
	void setStartTime(long sT)
	{
		this.startTime = sT;
	}
	void setEndTime(long eT)
	{
		this.endTime = eT;
	}
	
	void start() throws IOException, InterruptedException
	{
		setStartTime(System.nanoTime());
		run();
		setEndTime(System.nanoTime());	
	}
	void run() throws IOException, InterruptedException
	{
		output.println(this.cmd);								//run command	    
		while(!(results = input.readLine()).equals("end"))		//while there are more results
				res.append(results+"\n");							//else store results                  		

	}
}

