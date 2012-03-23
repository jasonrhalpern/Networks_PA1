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
				int portNum = this.threadSocket.getPort();
				System.out.println("PORT NUM: " + portNum);
				if(portNum != 1337){
					buffer.put(outgoingMessage);
					System.out.println("PUT TO OUR SERVER BUFFER FROM US: " + outgoingMessage + '\n');
				}else{
					try{
						String newMessage = this.trimMessage(outgoingMessage);
						buffer.put(newMessage);	
						System.out.println("PUT FROM BROADCAST SERVER TO OUR SERVER BUFFER: " + newMessage + '\n');
					}
					catch(Exception e){
						
					}
				}
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
