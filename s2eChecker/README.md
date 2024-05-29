
## Clients to connect to s2e server
### And upload received data to DB

    - Since we have now a serial-to-Ethernet (s2e) interface, we can for go the server sider of this.
    - The java client was the first draft, but I stopped at uploading to database (DB).
    - Since already had the python code for a mysql (relational) DB.
    - In operation, though an influx (time-series) DB is used, so we added a very basic functionality.
    - This is the final python version here then. Does the job.
