package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Producer implements Runnable{
	Socket threadSocket;
	SemBoundedBuffer buffer;
	static ArrayList<String> listOfUnis = new ArrayList<String>();
	
	public Producer(SemBoundedBuffer buffer, Socket newSocket) {
		this.buffer = buffer;
		this.threadSocket = newSocket;
	}
	
	public static void addUnisToList(String uniList){
		String[] uniArray = uniList.split(",");
		for(int i = 0; i < uniArray.length; i++){
			if(!listOfUnis.contains(uniArray[i])){
				System.out.println("Add to List: " + uniArray[i]);
				listOfUnis.add(uniArray[i]);
			}
		}
	}
	
	public static void removeUnisFromList(String uniList){
		String[] uniArray = uniList.split(",");
		for(int i = 0; i < uniArray.length; i++){
			if(listOfUnis.contains(uniArray[i])){
				System.out.println("Removee from List: " + uniArray[i]);
				listOfUnis.remove(uniArray[i]);
			}
		}
	}
	
	public void run(){
		String outgoingMessage;

		while(true)
		{
			try {
				BufferedReader inFromUserInterface = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
				outgoingMessage = inFromUserInterface.readLine();
				boolean send = this.checkUni(outgoingMessage);
				System.out.println("check Uni before sending" + outgoingMessage +send);
				if(send){
					buffer.put(outgoingMessage);
					int portNum = this.threadSocket.getPort();
					System.out.println("PUT TO OUR PROXY BUFFER: " + outgoingMessage + " from incoming PORT NUM: " + portNum + '\n');
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
	
	public boolean checkUni(String message){
		String[] tokens = message.split(":");
		System.out.println(tokens[0]);
		String[] uni = tokens[1].split("\\s+"); //uni is zero index
		if(listOfUnis.contains(uni[0])){
			return false;
		}
		else{
			return true;
		}
	}
}
