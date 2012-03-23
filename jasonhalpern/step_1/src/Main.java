import communication.Consumer;
import communication.Producer;
import communication.SemBoundedBuffer;

import java.io.*;
import java.net.*;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		//Create a bounded buffer of size 5
		SemBoundedBuffer buffer = new SemBoundedBuffer(3);
		
		Socket inSocket = new Socket("localhost", 1479);
		Socket outSocket = new Socket("localhost", 1480);

		//Create a producer and a consumer that will interact with the buffer
		Thread p1 = new Thread(new Producer(buffer, inSocket));
		Thread c1 = new Thread(new Consumer(buffer, outSocket));
		
		//Start the threads
		p1.start();
		c1.start();

		//in.close();
		//out.close();
	}
}
