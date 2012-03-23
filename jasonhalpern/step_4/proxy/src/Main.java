import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import communication.Consumer;
import communication.Producer;
import communication.SemBoundedBuffer;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
		
		
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
		boolean checkOne;
		boolean checkTwo;
		String message;
		
		try {
			ServerSocket welcomeSocket = new ServerSocket(1337);
			Socket connectionSocket = welcomeSocket.accept();
			Thread c2 = new Thread(new Consumer(secondBuffer, connectionSocket));
			c2.start();
			while(true){
				System.out.println("IN SERVER LOOP");
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				sentence = inFromClient.readLine();
				System.out.println("PROXY RECEIVED: "+ sentence);
				
				message = sentence;
				
				checkOne = message.contains("/");
				checkTwo = message.contains("-");
				
				
					if(checkOne && checkTwo){
						String[] tokens = message.split("/");
						String unisToAdd = tokens[0].substring(3);
						System.out.println("UNIS TO ADD: "+ unisToAdd);
						Producer.addUnisToList(unisToAdd);
						String unisToRemove = tokens[1].substring(3);
						Producer.removeUnisFromList(unisToRemove);
					}
					else if(!checkOne && checkTwo){
						if(message.charAt(1) == 'r'){
							String unisToRemove = message.substring(3);
							System.out.println("UNIS TO REMOVE: "+ unisToRemove);
							Producer.removeUnisFromList(unisToRemove);
						}
						if(message.charAt(1) == 'a'){
							String unisToAdd = message.substring(3);
							Producer.addUnisToList(unisToAdd);
						}
					}
					else{
						buffer.put(sentence);
					}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
}
