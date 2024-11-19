If you're only seeing **DHCP Request** and **DHCP ACK** packets in Wireshark, and they are not sent via broadcast, it likely means that your device already has a valid IP address and is renewing its existing DHCP lease. Here's why this happens:

---

### **Why You See Only DHCP Request and ACK**
1. **Existing IP Lease**
   - If your computer already has a valid IP address and the lease is not expired, the `ipconfig /renew` command skips the full DHCP discovery process (which includes **Discover** and **Offer**) and goes straight to the **Request** and **ACK** phase. 
   - This is because the device is simply asking to renew its current IP lease rather than obtaining a completely new address.

2. **Unicast Communication**
   - During lease renewal, the DHCP client communicates directly (via unicast) with the DHCP server that issued the current lease. This avoids using broadcast since the client knows the server's IP address.
   - **DHCP Discover** and **Offer** are only broadcasted when the client doesn’t know the DHCP server or when it is requesting a new lease.

---

### **How the Process Works in Renewal**
1. **DHCP Request (Unicast)**  
   The client sends a unicast DHCP Request packet to the DHCP server, asking to extend or renew the lease for its current IP address.

2. **DHCP ACK (Unicast)**  
   The DHCP server responds with a unicast DHCP ACK packet, confirming the lease extension and providing any updated configuration if necessary.

---

### **Why No Broadcast Packets?**
Broadcasts are used only in the initial phase of obtaining a new IP address (when the client doesn’t have server details). In your case:
- The client already knows the server and its current lease.
- Unicast communication reduces network noise and is more efficient for lease renewals.

---

### **How to Confirm This in Wireshark**
1. **Use Filter:** `dhcp` or `bootp`
2. **Look at Packet Details:**
   - **Request Packet:** Check if the client is asking to renew its **current IP address** (you'll see this in the "Requested IP Address" field).
   - **ACK Packet:** Check if the server confirms the lease for the same address.

If you want to observe the full broadcast process (Discover → Offer → Request → ACK), you can force your computer to release its IP lease using `ipconfig /release` before running `ipconfig /renew`.
