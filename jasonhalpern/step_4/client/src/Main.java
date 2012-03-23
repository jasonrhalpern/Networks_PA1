import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import communication.Consumer;
import communication.NewProducer;
import communication.Producer;
import communication.SemBoundedBuffer;


public class Main {
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		
        //Create a bounded buffer of size 5
        SemBoundedBuffer buffer = new SemBoundedBuffer(3);
        SemBoundedBuffer secondBuffer = new SemBoundedBuffer(3);

        String hostname = args[0];
        String ipAddress = args[1];
        Integer ipAdd = Integer.valueOf(ipAddress);
        
        Socket inSocket = new Socket("localhost", 1479);
        Socket outSocket = new Socket("localhost", 1480);
        Socket proxySocket = new Socket(hostname, ipAdd);

        //Create a producer and a consumer that will interact with the buffer
        Thread p1 = new Thread(new NewProducer(buffer, secondBuffer, inSocket));
        Thread p2 = new Thread(new Producer(buffer, proxySocket));
        Thread c1 = new Thread(new Consumer(buffer, outSocket));
        Thread c2 = new Thread(new Consumer(secondBuffer, proxySocket));

        //Start the threads
        p1.start();
        c1.start();
        c2.start();
        p2.start();
        
		if(args.length == 3){
			//send the string as it is
			String message = args[2];
			DataOutputStream outToUserInterface = new DataOutputStream(proxySocket.getOutputStream());
			outToUserInterface.writeBytes(message + '\n');
		}
		
		if(args.length == 4){
			char protocol = args[2].charAt(1);
			if(protocol == 'a'){
				String message = args[2]+"/"+args[3];
				DataOutputStream outToUserInterface = new DataOutputStream(proxySocket.getOutputStream());
				outToUserInterface.writeBytes(message + '\n');
			}
			else{
				String message = args[3]+"/"+args[2];
				DataOutputStream outToUserInterface = new DataOutputStream(proxySocket.getOutputStream());
				outToUserInterface.writeBytes(message + '\n');
			}
		}
		
		//send that message
	}
}
