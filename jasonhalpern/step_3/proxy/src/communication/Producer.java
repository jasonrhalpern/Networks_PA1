package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Producer implements Runnable{
	Socket threadSocket;
	SemBoundedBuffer buffer;
	
	public Producer(SemBoundedBuffer buffer, Socket newSocket) {
		this.buffer = buffer;
		this.threadSocket = newSocket;
	}
	
	public void run(){
		String outgoingMessage;

		while(true)
		{
			try {
				BufferedReader inFromUserInterface = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
				outgoingMessage = inFromUserInterface.readLine();
				buffer.put(outgoingMessage);
				int portNum = this.threadSocket.getPort();
				System.out.println("PUT TO OUR PROXY BUFFER: " + outgoingMessage + " from incoming PORT NUM: " + portNum + '\n');
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String trimMessage(String message){
		String[] tokens = message.split(":");
		String[] shape = tokens[2].split("\\s+"); //shape is zero index
		String[] x = tokens[3].split("\\s+"); //x-coord is zero index
		String[] y = tokens[4].split("\\s+"); //y-coord is zero index
		String[] color = tokens[5].split("\\s+"); //color is zero index
		return shape[0] + " " + x[0] + " " + y[0] + " " + color[0];
	}
}
