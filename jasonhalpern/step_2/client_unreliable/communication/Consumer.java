package communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Consumer implements Runnable{
	Socket threadSocket;
	SemBoundedBuffer buffer;
	Semaphore sem;
	
	public Consumer(SemBoundedBuffer buffer, Socket newSocket, Semaphore sem) {
		this.buffer = buffer;
		this.threadSocket = newSocket;
		this.sem = sem;
	}
	
	public void run(){
		String incomingMessage;
	
		while(true)
		{
			try {
				System.out.println("IN OUTER WHILE Lock: "+ SemBoundedBuffer.getBroadcastLock() + " Waiting Message: "+SemBoundedBuffer.getWaitingMessage());
				incomingMessage = (String) buffer.get();
				DataOutputStream outToUserInterface = new DataOutputStream(threadSocket.getOutputStream());
				int portNum = this.threadSocket.getPort();
				System.out.println(portNum);
				if(portNum == 1453 && incomingMessage != null){
					System.out.println("Sent to Broadcast Buffer: " + incomingMessage + '\n');
					SemBoundedBuffer.setBroadcastLock(true);
					SemBoundedBuffer.setWaitingMessage(incomingMessage);
					outToUserInterface.writeBytes(incomingMessage + '\n');
					Thread.sleep(3000);
					while(SemBoundedBuffer.getBroadcastLock()){
						System.out.println("IN INNER WHILE Lock: "+ SemBoundedBuffer.getBroadcastLock() + " Waiting Message: "+SemBoundedBuffer.getWaitingMessage());
						this.sem.release();
						Thread.sleep(3000);
						outToUserInterface.writeBytes(incomingMessage + '\n');
						this.sem.acquire();
					}
				}
				else{
					outToUserInterface.writeBytes(incomingMessage + '\n');
					System.out.println("Sent to Our Buffer: " + incomingMessage + '\n');
				}
			
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