import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;
import java.util.ArrayList;

public class SerialReadTest {

	public static void main(String[] args) {
		
	/*	
		//////////////////////////////////////////////////////////
		String OS = System.getProperty("os.name").toLowerCase();
		System.out.println(OS);
		if (OS.indexOf("win") >= 0) {
			//search for com ports
			// Create an instance of SerialPortsWindows
	        SerialPortsWindows serialPorts = new SerialPortsWindows();
	        ArrayList<String> ports = serialPorts.availablePorts;
	        // Print the available ports
	        System.out.println("Available Ports:");
	        for (String port : ports) {
	            System.out.println(port);
	        }
	        
		} else if (OS.indexOf("nix") >= 0 || (OS.indexOf("nuw")) >= 0) {
			//search for dev/ttys
//			getSerialPortsLinux()
		} else {
			System.out.println(":(");
		}
		
		System.exit(0);
	*/	
		/////////////////////////////////////////////////////
		
		//just for fun: find and list available serial ports:
		SerialPort[] ports = SerialPort.getCommPorts();		
		if (ports == null) {
			  System.out.println("no serial ports.");  
		} else {
			for (SerialPort port: ports) {
				System.out.println("port = " + port + ", w name: " +  port.getSystemPortName());	
			}
		}

			/*//////////////////////
			* OPEN PORT
			//////////////////////*/
		
		SerialPort CNCB0 = ports[1];
		//open the port
		if (CNCB0.openPort()) {	//this method opens the port and returns true if successful
			System.out.println("port " + CNCB0.getSystemPortName() + " opened");
			Thread inputStreamThread2 = new Thread(new readInputStreamThread(CNCB0));
			inputStreamThread2.start();
		} else {
			System.out.println("Failed to open port!");
			return;
		}

		
		SerialPort comPort = ports[3];
		//open the port
		if (comPort.openPort()) {	//this method opens the port and returns true if successful
			System.out.println("port " + comPort.getSystemPortName() + " opened");
			
			Thread inputStreamThread = new Thread(new readInputStreamThread(comPort));
			inputStreamThread.start();
		} else {
			System.out.println("Failed to open port!");
			return;
		}
		
	
		
		
			/*//////////////////////
			* READ PORT
			//////////////////////*/

//		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
			
//			Scanner scannerInput = new Scanner(System.in);
/*		
		try
		{
			System.out.println(":)");
			while (true) {
				 while (comPort.bytesAvailable() == 0) {
					 Thread.sleep(10); 
				 }
			   System.out.print((char)comPort.getInputStream().read()); 
//					if (scannerInput.next() != null) {
//							throw new IOException();
//					}
		}
	 
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println(":(");
		}
*/
		
	//	try {
			
			
			
			
	//	}
	//	catch(Exception e) {}
		
		/*//////////////////////
		* CLOSE PORT
		//////////////////////*/
		/*
		finally {
//				scannerInput.close();
			try {
				System.out.println("Closing input stream.");
				comPort.getInputStream().close();	
			} catch (IOException ioe ) {
				System.out.println(":(");  
			}
			//
			if (comPort.closePort()) {	//this method closes the port and returns true if successful
				System.out.println("port "  + comPort.getSystemPortName() + " closed");
			} else {
				System.out.println("Failed to close port!");
			}
		}
		*/
		
					
	}

}


class readInputStreamThread implements Runnable {
	//multithreading

	static SerialPort port;
	
	//constructor
		public readInputStreamThread (SerialPort comPort) {
			port = comPort;
		}
		

		@Override
		public void run() {
			
			try
			{
				System.out.println("Running Thread"+ Thread.currentThread().threadId());
				
				while (true) {
					 while (readInputStreamThread.port.bytesAvailable() == 0) {
						 Thread.sleep(10); 
					 }
				   System.out.print((char)readInputStreamThread.port.getInputStream().read()); 
//						if (scannerInput.next() != null) {
//								throw new IOException();
//						}
			}
		 
			} catch (Exception e) { 
				e.printStackTrace();
				System.out.println(":(");
			}
			finally {
//				scannerInput.close();
				try {
					System.out.println("Closing input stream.");
					readInputStreamThread.port.getInputStream().close();	
				} catch (IOException ioe ) {
					System.out.println(":(");  
				}
				//
				if (readInputStreamThread.port.closePort()) {	//this method closes the port and returns true if successful
					System.out.println("port "  + readInputStreamThread.port.getSystemPortName() + " closed");
				} else {
					System.out.println("Failed to close port!");
				}
			}

			
		}
	}
