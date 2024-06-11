## TO RUN LOCALLY:

I used socat to create a virtual port pair

sudo socat -d -d -v pty,rawer,echo=0,link=/dev/ttyV0,rawer,b9600 pty,rawer,echo=0,link=/dev/ttyV1,rawer,b9600

sudo python3 TIC_Emulator.py

sudo python3 Read_n_Send.py

(the sudo is only necessary since a work comp.)

Then on the otherside run:

sudo python3 Receive_n_Send.py

(Also, to kill the server on the windows machine used: 'Fn Ctrl B')
