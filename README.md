To detect a spreading virus (or any malware) with **Velociraptor**, you can use its powerful **hunting and detection** capabilities. Here’s how you can approach it:

---

### **1. Identify Indicators of Compromise (IOCs)**
- Look for **suspicious processes**, **network connections**, **file changes**, and **registry modifications**.
- Gather **hashes**, **domains**, and **IPs** of known malware.

---

### **2. Hunt for Malicious Activity**
Velociraptor allows you to create and run **VQL (Velociraptor Query Language) queries** to detect suspicious behavior.

#### **A. Detect Suspicious Processes**
Run the following **VQL query** to find processes with unusual names or locations:

```sql
SELECT Pid, Name, Cmdline, Exe, PPid, Username, CreateTime
FROM pslist()
WHERE Exe =~ "C:\\Users\\Public\\*" OR Name =~ "svchost.exe" AND Cmdline NOT LIKE "%-k%"
ORDER BY CreateTime DESC
```
- This looks for processes running from `C:\Users\Public\` (a common hiding place for malware).
- It also detects fake `svchost.exe` processes.

#### **B. Check Active Network Connections**
Look for suspicious **remote connections**:

```sql
SELECT Pid, Name, LocalAddress, LocalPort, RemoteAddress, RemotePort, State
FROM netstat()
WHERE RemoteAddress NOT LIKE "192.168.%"
ORDER BY RemoteAddress
```
- This shows processes communicating with **external IPs**.

#### **C. Find Newly Created Executables**
Use Velociraptor's file hunting capabilities to find **recently dropped executables**:

```sql
SELECT Name, FullPath, MTime, Size
FROM glob(globs=["C:\\Users\\*\\AppData\\Roaming\\*.exe", "C:\\Users\\*\\AppData\\Local\\*.exe"])
WHERE MTime > now() - duration("1d")
```
- This finds executables created **in the last 24 hours**.

#### **D. Scan for Persistence Mechanisms**
Check the **Run** registry keys for malware persistence:

```sql
SELECT Name, Data, Key
FROM artifact.Windows.System.Registry.RunKeys()
WHERE Data LIKE "%.exe%"
```

---

### **3. Set Up Live Monitoring**
Use **Velociraptor Collection & Event Monitoring** to detect malware in real-time:

- **Enable Event Queries**: Monitor for process creation, file modifications, or registry changes.
- **Collect Logs**: Store forensic logs for further investigation.
- **Trigger Alerts**: Set up alerts when malware-like activity is detected.

---

### **4. Automate Remediation (Optional)**
If you detect malware:
- **Kill the Process**: Use `kill -9 <PID>` via Velociraptor remote shell.
- **Delete Malicious Files**: Automate cleanup scripts.
- **Isolate the Infected Machine**: Disable its network connection.

---

### **Final Steps**
- **Correlate with External Threat Intelligence**: Compare findings with known threat feeds.
- **Analyze Executables**: Extract and analyze suspicious binaries using **Velociraptor's file carving**.

Would you like help creating a **custom Velociraptor artifact** for this?
