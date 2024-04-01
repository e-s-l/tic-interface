import java.io.File;
import java.util.ArrayList;


public class SerialPortsWindows {
	
	public ArrayList<String> availablePorts;
	
	/*
	 * The below approach doesnt work.
	 * try instead, to run from the powershell
	 *  [System.IO.Ports.SerialPort]::getportnames()
	 *  use output of this. 
	 */

	public SerialPortsWindows () {
		//constructor which is a scieal method to initialise the attributes when an object is made form a class
		this.availablePorts = getAvailablePorts();
	}
	
	private ArrayList<String> getAvailablePorts() {
		//method which returns a mutable array of strings. 
		 
		  ArrayList<String> PortList = new  ArrayList<String>();
		 
	        // Define the base directory for serial ports on Windows
	        File baseDir = new File("\\\\.\\COM");

	        // List all files in the base directory
	        File[] files = baseDir.listFiles();

	        if (files != null) {
	            for (File file : files) {
	                // Check if the file represents a COM port
	                if (file.getName().startsWith("COM")) {
	                    System.out.println("Serial Port: " + file.getName());
	                    PortList.add(file.getName());
	                }
	            }
	        } else {
	            System.err.println("Error: Unable to list COM ports in " + baseDir.getAbsolutePath());
	        }
	        //
	        return PortList;
	    }
}
