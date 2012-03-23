package communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Consumer implements Runnable{
	Socket threadSocket;
	SemBoundedBuffer buffer;
	
	public Consumer(SemBoundedBuffer buffer, Socket newSocket) {
		this.buffer = buffer;
		this.threadSocket = newSocket;
	}
	
	public void run(){
		String incomingMessage;
	
		while(true)
		{
			try {
				incomingMessage = (String) buffer.get();
				DataOutputStream outToUserInterface = new DataOutputStream(threadSocket.getOutputStream());
				outToUserInterface.writeBytes(incomingMessage + '\n');
				int portNum = this.threadSocket.getPort();
				System.out.println("GET FROM BUFFER THIS MESSAGE: " + incomingMessage + " and SEND TO PORT #: " + portNum + '\n');
			}catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
