########################
### SERIAL INTERFACE ###
########################

import serial
import sys


class SerialReader:

    # class instantiation:
    def __init__(self, port, speed):
        self.port = port
        self.speed = speed  # speed = baud rate
        self.serial = None

    # METHODS:
    def open(self):
        try:
            self.serial = serial.Serial(self.port, self.speed)
            print("Successfully opened Port %s" % self.port)
        except serial.SerialException as se:
            print("Failed to open Port %s" % self.port)
            print(se)
            sys.exit(1)  # remember non-zero exit code is bad....

    def read(self):
        if self.serial:
            return self.serial.readline().decode().strip()
        else:
            print("Port %s isn't open!" % self.port)
            return None

    def write(self, data):
        if self.serial:
            self.serial.write(data.encode())
            print("Wrote to Port.")
        else:
            print("Cannot write to closed port.")

    def close(self):
        if self.serial:
            self.serial.close()
            print("Closed Port: %s" % self.port)
        else:
            print("Can't closed something already closed.")


####################


if __name__ == "__main__":
    port_name = "COM5"
    baud_rate = "9600"
    serialPort = SerialReader(port_name, baud_rate)
    serialPort.open()

    ##############

    try:
        while 1:
            line = serialPort.read()
            if line:
                print("received:", line)
    except KeyboardInterrupt:
        serialPort.close()
