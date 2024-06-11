**********************************************
## Serial Port -> UDP/TCP -> DB

The original purpoes of this project was to take counter values from a serial port on one computer and load into a database.
Initially, there was to be an intervening UDP or TCP/IP client-server interface.

However, in the process, we:

    Started using a serial-to-ethernet adaptor, which now acts as the server. (This version is found in /s2eChecker/)

    Can access the database through a network connection from another machine. Though we haven't implmeneted this, this means the server stuff isn't really necessary. Though it was still valuable learning experience.

### .py version:

    The two python versions are considered complete.
    The first version used UDP and a either a SQL or Influx database.
    This has been tested between two local machines.

### .java version

    The versions here are in some sense both done and not.
    We explored setting up and using TCP/IP and UDP connections.
    But have not estabilished database connections.


**********************************************


