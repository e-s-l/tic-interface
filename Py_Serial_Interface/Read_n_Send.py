########################
# READ FROM SERIAL PORT
# SEND AS UDP DATAGRAM
########################

import serial
import sys
import socket


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
            print(f"Successfully opened Serial Port {self.port}")
        except serial.SerialException as se:
            print(f"Failed to open Port {self.port}")
            print(se)
            sys.exit(1)  # remember non-zero exit code is bad....

    def read(self):
        print("reading")
        if self.serial:
            print("is open")
            data = self.serial.readline().decode().strip()
            print(f"Received from serial port {self.port}: {data}")
            return data
        else:
            print(f"Port {self.port} isn't open!")
            return None

    def write(self, data):
        if self.serial:
            if type(data) == str:
                    data = data.encode()
            elif type(data) == float:
                data = data.pack()
            self.serial.write(data)
            print(f"Wrote {data} to Port {self.port}.")
        else:
            print("Cannot write to closed port.")

    def close(self):
        if self.serial:
            self.serial.close()
            print(f"Closed Port: {self.port}")
        else:
            print("Can't closed something already closed.")


####################

class UDPClient:

    def __init__(self, address, port):
        self.server_address = address
        self.server_port = port
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        print(f"UDP set-up to send to Server at {server_address} on port {self.server_port}")

    def send_message(self, message):
        self.client_socket.sendto(message.encode(), (self.server_address, self.server_port))
        print("Sent: %s" % message)

    def close(self):
        self.client_socket.close()


####################

if __name__ == "__main__":

    ##############

    serial_port = "/dev/ttyV1"
    baud_rate = 9600
    serialPort = SerialReader(serial_port, baud_rate)
    serialPort.open()

    ##############

    server_address = "10.0.107.147"
    server_port = 1234
    client = UDPClient(server_address, server_port)

    ##############

    try:
        print("trying!")
        while 1:
            print("loopy")
            line = serialPort.read()
            if line:
                print(line)
                client.send_message(line)
    except KeyboardInterrupt:
        serialPort.close()
    finally:
        client.close()
