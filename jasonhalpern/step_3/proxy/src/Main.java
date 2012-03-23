
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import communication.Consumer;
import communication.NewProducer;
import communication.Producer;
import communication.SemBoundedBuffer;

public class Main {
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		//Create a bounded buffer of size 5
        SemBoundedBuffer buffer = new SemBoundedBuffer(3);
        SemBoundedBuffer secondBuffer = new SemBoundedBuffer(3);
        
        Socket broadcastSocket = new Socket("csee4119.cs.columbia.edu", 1452);

        //Create a producer and a consumer that will interact with the buffer
        Thread c1 = new Thread(new Consumer(buffer, broadcastSocket));
        Thread p1 = new Thread(new Producer(secondBuffer, broadcastSocket));


        //Start the threads
        c1.start();
        p1.start();
        
		
		String sentence;
		try {
			ServerSocket welcomeSocket = new ServerSocket(1337);
			Socket connectionSocket = welcomeSocket.accept();
			Thread c2 = new Thread(new Consumer(secondBuffer, connectionSocket));
			c2.start();
			while(true){
				System.out.println("IN SERVER LOOP");
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				SocketAddress portNum = connectionSocket.getRemoteSocketAddress();
				sentence = inFromClient.readLine();
				System.out.println("PROXY RECEIVED: "+ sentence + " from PORT#: " + portNum.toString() + " and sent to  buffer");
				buffer.put(sentence);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
