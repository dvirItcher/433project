Certainly! Here’s a basic example of sending a TCP packet using Python's `socket` library. This code will allow you to connect to a server and send data over a TCP connection.

### Steps
1. First, import the `socket` module.
2. Create a socket using `socket.socket()`.
3. Connect the socket to the server's IP address and port.
4. Send data using `sendall()` or `send()`.
5. Close the connection.

Here's a simple example:

```python
import socket

# Define server address and port
server_address = ('<SERVER_IP>', <PORT_NUMBER>)

# Create a TCP socket
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
    # Connect to the server
    sock.connect(server_address)
    try:
        # Send data
        message = b"Hello, server!"
        sock.sendall(message)
        
        # Optional: Receive response
        response = sock.recv(1024)
        print("Received:", response.decode())
    
    finally:
        print("Closing connection")
```

### Explanation of the Code
- Replace `'<SERVER_IP>'` and `<PORT_NUMBER>` with the actual IP address and port of the server you want to connect to.
- `sendall()` ensures that all data in `message` is sent. Use `send()` for lower-level control if needed.
- You can optionally use `recv()` to get a response from the server (if the server sends one).

Let me know if you need additional customization for specific packet formats or protocols!
