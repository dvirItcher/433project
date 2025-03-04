You can achieve all three without running commands manually by using **Velociraptor’s GUI and built-in hunts/collections**. Here’s how:

---

### **1. Find the Server's Name/IP Address**  
- Open **Velociraptor’s Web Interface**.  
- Go to **"Collections" → "New Collection"**.  
- Use the **NTFS.Network.Connections** artifact to see **active and historical connections**.  
  - This will list all network connections, including the **server’s IP address** on port 80.  
  - If you need the hostname, you can check **DNS queries** using the **Windows.DNSCache** artifact.  

---

### **2. Find the Lock Screen Image Changed by the Server**  
- Go to **"New Collection"** and select **Windows.Indicators.Autoruns**.  
  - This may show if a script or malware changed the lock screen.  
- Then, collect **Windows.Artifacts.ChangedFiles** and filter it to check:  
  - **C:\Windows\Web\Screen**  
  - **C:\Windows\Web\Wallpaper**  
  - **C:\Users\Public\Pictures**  
- If an unauthorized file was added, check its timestamp and hash for further analysis.  

---

### **3. Check if the Computer is Still Communicating with the Server**  
- Use **NTFS.Network.Connections** again, but now filter results to see **active connections**.  
- Alternatively, run **Windows.System.NetworkEvents**, which captures real-time traffic logs.  
- If you see the same server’s IP in active connections, the communication is still happening.  

---

### **Next Steps**  
- If you confirm malicious activity, **block the IP** in your firewall.  
- Check **Windows.EventLogs.Security** to see if a process or user triggered these changes.  
- You can also run **Windows.Sysmon.ProcessNetworkConnections** if you suspect malware is initiating the connection.  

Let me know if you need help setting up any of these collections!
