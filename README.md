Scapy is a powerful Python library for packet crafting and manipulation. If you want to send a message over UDP to a server using Scapy, you can do so by crafting a `UDP` packet. Here's a simple example:

```python
from scapy.all import IP, UDP, send

# Define server details
server_ip = "192.168.1.10"  # Replace with your server's IP
server_port = 12345  # Replace with your server's port

# Create a UDP packet with payload
message = "Hello, Server!"  # Message to send
udp_packet = IP(dst=server_ip) / UDP(dport=server_port) / message

# Send the packet
send(udp_packet)
```

### Explanation
1. **`IP(dst=server_ip)`**: Sets the destination IP address.
2. **`UDP(dport=server_port)`**: Specifies the destination port.
3. **`/ message`**: Appends the payload (message) to the UDP packet.
4. **`send(udp_packet)`**: Sends the crafted packet to the specified destination.

### Notes
- Scapy works best for testing and crafting packets but is not designed for heavy-duty networking like `socket`.
- Ensure you have proper permissions (e.g., running as root or administrator) since Scapy often requires elevated privileges to send crafted packets.
- The server should be ready to receive and parse the UDP message correctly.
