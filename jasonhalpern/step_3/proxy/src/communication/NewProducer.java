package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NewProducer extends Producer{
	SemBoundedBuffer secondBuffer;
	
	public NewProducer(SemBoundedBuffer buffer, SemBoundedBuffer secondBuffer, Socket newSocket) {
		super(buffer, newSocket);
		this.secondBuffer = secondBuffer;
	}
	
	public void run(){
		String outgoingMessage;
		String broadcastMessage;

		while(true)
		{
			try {
				BufferedReader inFromUserInterface = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
				outgoingMessage = inFromUserInterface.readLine();
				buffer.put(outgoingMessage);
				broadcastMessage = this.modifyMessage(outgoingMessage);
				secondBuffer.put(broadcastMessage);
				System.out.println("PUT TO OUR BUFFER: " + outgoingMessage + '\n');
				System.out.println("PUT TO BROADCAST BUFFER: " + broadcastMessage + '\n');
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String modifyMessage(String outgoingMessage){
		String[] message = outgoingMessage.split("\\s+");
		String newMessage = "UNI:jrh2170 Shape:" + message[0] + " X:" + message [1] + " Y:" + message[2] + " Color:" + message[3];
		return newMessage;
	}
}
