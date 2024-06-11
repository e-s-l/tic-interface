########################
# RECEIVE UDP DATAGRAM
# SAVE TO DATABASE
########################

import socket

####################


class UDPServer:

    # class instantiation:
    def __init__(self, address, port):
        self.address = address
        self.port = port
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.server_socket.bind((self.address, self.port))

    def receive(self):
        message, client_address = self.server_socket.recvfrom(1024)  # number of bytes, will have to change
        print(f"Received from {client_address}: {message.decode()}")

    def close(self):
        self.server_socket.close()

####################


if __name__ == "__main__":

    ##############
    server_address = "10.0.107.147"  # "0.0.0.0" # = all available network interfaces
    server_port = 1234  # what's the default UDP port again?
    server = UDPServer(server_address, server_port)

    #
    print(f"Starting server at IP: {server_address} Port: {server_port}")
    #
    try:
        while 1:
            server.receive()
    except KeyboardInterrupt:
        server.close()

####################


