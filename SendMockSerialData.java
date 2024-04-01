/*
 * 
 */

import com.fazecast.jSerialComm.SerialPort;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class SendMockSerialData {

	public static void main(String[] args) {

		//just for fun: find and list available serial ports:
		SerialPort[] ports = SerialPort.getCommPorts();		
		////
		if (ports == null) {
			  System.out.println("no serial ports.");  
		} else {
			for (SerialPort port: ports) {
				System.out.println("port = " + port);	//debug
				System.out.println("Port Name = " + port.getSystemPortName());
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////
		
	
		//we will send data through the (virtual) COM1 (aka 'Port CNCA0')/the first available COM port
		SerialPort COM1 = ports[0];
		//open the port
		if (COM1.openPort()) {	//this method opens the port and returns true if successful
			System.out.println("port opened");
		} else {
			System.out.println("Failed to open port!");
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////
		try {
			System.out.println("Sending data down the stream..."); 
			
			String hello = "hello, can you hear me???";
			byte[] byteString = hello.getBytes();
			
			COM1.getOutputStream().flush();
			Thread.sleep(100); 		
			COM1.getOutputStream().write(byteString);		//write method takes ints or bytes. //type casting int to byte
			COM1.getOutputStream().flush();			//clean the pipes
			
			//more random data.
			for(int i = 0; i < 100; i++) {
				COM1.getOutputStream().write((byte) i);		//write method takes ints or bytes. //type casting int to byte
				COM1.getOutputStream().flush();			//clean the pipes
				Thread.sleep(100); 						//wait "half" a second.
			}
			
	//		boolean sendIt;
//			sendIt = true;
			
		//	Random randNum = new Random(); 
	//		Scanner scannerInput = new Scanner(System.in);
			
	//		while (sendIt) {
	//			int randInt = randNum.nextInt(10);
	//			System.out.println(randInt);
	//			COM1.getOutputStream().write(randInt);		//write method takes ints or bytes. //type casting int to byte
	//			COM1.getOutputStream().flush();				//clean the pipes
	//			Thread.sleep(100); 							//wait "half" a second.
				
	//			if (scannerInput.next() != null) {
	//				sendIt = false;
	//			}
				
	//		}
			
			//when finished sending the data, close the stream
			COM1.getOutputStream().close();
			System.out.println(":)");  
		} catch (IOException | InterruptedException ioe ) {
			System.out.println(":(");  
		} finally {
			System.out.println("well...");  
		}
		
		////////////////////////////////////////////////////////////////////////////////////
		
		
		//now collect the data from the otherside
		SerialPort COM2 = ports[1];
		int[] readIn = new int[102];
		//open the port
		if (COM2.openPort()) {	//this method opens the port and returns true if successful
			System.out.println("port opened");
		} else {
			System.out.println("Failed to open port!");
			return;
		}
		try {
			for(int i = 0; i < 100; i++) {
				readIn[i] = COM2.getInputStream().read();
			}
			
			
			COM2.getInputStream().close();
		} catch (IOException ioe ) {
			System.out.println(":(");  
		}
		
		System.out.println(readIn);
		
		////////////////////////////////////////////////////////////////////////////////////

		
		
		////////////////////////////////////////////////////////////////////////////////////

		if (COM1.closePort()) {	//this method closes the port and returns true if successful
			System.out.println("port closed");
		} else {
			System.out.println("Failed to close port!");
		}

		if (COM2.closePort()) {	//this method closes the port and returns true if successful
			System.out.println("port closed");
		} else {
			System.out.println("Failed to close port!");
		}
		
		
	}

}
