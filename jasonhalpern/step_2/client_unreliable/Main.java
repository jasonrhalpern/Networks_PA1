import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import communication.Consumer;
import communication.NewProducer;
import communication.Producer;
import communication.SemBoundedBuffer;
import communication.Semaphore;


public class Main {
	
    public static void main(String[] args) throws UnknownHostException, IOException {

        //Create a bounded buffer of size 5
        SemBoundedBuffer buffer = new SemBoundedBuffer(3);
        SemBoundedBuffer secondBuffer = new SemBoundedBuffer(3);
        Semaphore sem = new Semaphore(1);

        String hostname;
       
        // Get hostname by textual representation of IP address
        InetAddress addr = InetAddress.getByName("128.59.15.27");
            
        // Get the host name
        hostname = addr.getHostName();
        
        Socket inSocket = new Socket("localhost", 1479);
        Socket outSocket = new Socket("localhost", 1480);
        Socket broadcastSocket = new Socket("csee4119.cs.columbia.edu", 1453);

        //Create a producer and a consumer that will interact with the buffer
        Thread p1 = new Thread(new NewProducer(buffer, secondBuffer, inSocket, sem));
        Thread p2 = new Thread(new Producer(buffer, broadcastSocket, sem));
        Thread c1 = new Thread(new Consumer(buffer, outSocket, sem));
        Thread c2 = new Thread(new Consumer(secondBuffer, broadcastSocket, sem));

        //Start the threads
        p1.start();
        c1.start();
        c2.start();
        p2.start();

        //in.close();
        //out.close();
    }
}

