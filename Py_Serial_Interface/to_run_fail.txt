
sudo socat -d -d -v pty,rawer,echo=0,link=/dev/ttyV0,rawer,b9600 pty,rawer,echo=0,link=/dev/ttyV1,rawer,b9600

sudo python3 TIC_Emulator.py

sudo python3 Read_n_Send.py
