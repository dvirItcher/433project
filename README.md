If Chrome is the process communicating with the suspicious server, let's focus on **browser-specific forensics**. Here's how to proceed:  

---

### **1. Find the Server's IP/Domain via Chrome’s History & Cache**  
- Go to **"New Collection" → Windows.Forensics.BrowserHistory**  
- Select **Google Chrome** and collect:  
  - **History** (Check for suspicious URLs)  
  - **Cookies** (See if any cookies are set by the bad server)  
  - **Cache** (Might contain references to downloaded images or scripts)  

If you already have the **IP address**, use **Windows.Forensics.ChromeCache** and search for that IP.  

---

### **2. Find the Lock Screen Image Downloaded via Chrome**  
If Chrome downloaded the image, it may be in:  
- **Downloads Folder** (`C:\Users\USERNAME\Downloads`)  
- **Temporary Internet Files** (`C:\Users\USERNAME\AppData\Local\Google\Chrome\User Data\Default\Cache`)  

To find the exact file:  
- Use **Windows.UsnJrnl** to list **recently created/modified files**.  
- Use **Windows.Forensics.ChromeDownloads** to check **Chrome's download history**.  

---

### **3. Check if Chrome is Still Communicating with the Server**  
Use **Windows.Sysmon.ProcessNetworkConnections** and filter for **chrome.exe**:  
- This will show **active and recent** connections.  
- Look for connections to the **suspicious IP** on port 80.  

Alternatively, use **Windows.Network.Connections** with `ProcessName = chrome.exe` to get real-time activity.  

---

### **Next Steps**  
- If the server is still active, block the IP in **Windows Firewall**.  
- Check for **browser extensions** in Chrome that may be responsible.  
- If a script or malware modified Chrome’s settings, check **Windows.Registry.Search** for `Chrome\Policies` to see if a policy forces Chrome to connect.  

Try these steps and let me know what you find!
