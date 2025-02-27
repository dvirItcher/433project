In **Velociraptor**, you can conduct investigations in two ways:  

1. **Active Investigation** – Directly interact with endpoints in real-time to collect forensic data, hunt threats, or respond to incidents.  
2. **Quiet (Stealthy) Investigation** – Passively collect data without alerting attackers or users, useful for covert monitoring.  

---

### **1. Active Investigation in Velociraptor**  
Active investigations involve **real-time queries, live monitoring, and direct action** on endpoints.  

#### **Steps for Active Investigation:**  

#### ✅ **Step 1: Identify the Target Endpoint**  
- Go to **"Clients"** in the Velociraptor GUI.  
- Select the machine you want to investigate.  

#### ✅ **Step 2: Run Live Queries on the Target Machine**  
Use **Velociraptor Query Language (VQL)** to collect forensic data:  

- **List Running Processes:**  
  ```sql
  SELECT * FROM pslist()
  ```
- **Check Active Network Connections:**  
  ```sql
  SELECT * FROM netstat()
  ```
- **Search for Suspicious Files in Temp Folder:**  
  ```sql
  SELECT * FROM glob(globs="C:\Users\*\AppData\Local\Temp\*")
  ```
- **Monitor Active Users and Logins:**  
  ```sql
  SELECT * FROM windows.System.Users()
  ```

#### ✅ **Step 3: Collect and Analyze Artifacts**  
- Navigate to **"Collections" > "New Collection"**.  
- Select forensic artifacts like:  
  - **Windows.Sys.Processes** (Running processes)  
  - **Windows.Forensics.UsnJournal** (Deleted/modified files)  
  - **Windows.System.Registry.HiveList** (Registry keys)  
  - **Windows.Forensics.PrefetchFiles** (Recently executed programs)  
  - **Linux.Sys.Processes** (For Linux process analysis)  
- Click **"Collect"** to fetch the data.  

#### ✅ **Step 4: Take Action if Needed**  
If a threat is confirmed:  
- **Kill a Malicious Process:**  
  ```sql
  SELECT kill(pid=1234)
  ```
- **Delete a Malicious File:**  
  ```sql
  SELECT remove(filename="C:\path\to\malware.exe")
  ```
- **Collect a Memory Dump for Analysis:**  
  ```sql
  SELECT * FROM Windows.DFIR.FullMemoryDump()
  ```

---

### **2. Quiet (Stealth) Investigation in Velociraptor**  
A quiet investigation is useful when you don’t want to alert attackers (e.g., malware, insider threats). This method **avoids live commands** and instead **automates data collection in the background**.  

#### **How to Perform a Quiet Investigation:**  

#### ✅ **Step 1: Schedule Background Collection**  
- Go to **"Server Artifacts" > "Hunt Manager"**.  
- Create a **new hunt** targeting multiple endpoints.  
- Choose **artifacts that passively collect data** without triggering alarms:  
  - **Windows.EventLogs.Security** – Logs security events.  
  - **Windows.KapeFiles.Targets** – Collects forensic files silently.  
  - **Windows.Detection.PsExecExecution** – Detects remote execution tools.  

#### ✅ **Step 2: Monitor Anomalous Behavior Without Interaction**  
Instead of running live commands, use **pre-configured detections**:  
- **Monitor File Changes in Sensitive Folders:**  
  ```sql
  SELECT * FROM glob(globs="C:\Windows\System32\drivers\*.sys")
  ```
- **Detect Unauthorized Remote Access:**  
  ```sql
  SELECT * FROM Windows.EventLogs.Security WHERE EventID = 4624 AND LogonType = 3
  ```
- **Track Execution of Suspicious Scripts (PowerShell, BAT, etc.):**  
  ```sql
  SELECT * FROM Windows.Detection.PowershellScriptBlockLogging()
  ```

#### ✅ **Step 3: Analyze Collected Data Later**  
- Let Velociraptor **quietly collect logs and forensic data**.  
- Review **collected artifacts** without triggering alerts.  
- If you find suspicious activity, escalate to an **active investigation**.  

---

### **Choosing Between Active & Quiet Investigations**  
| **Method**        | **Best Use Case** | **Risk** |
|------------------|-----------------|---------|
| **Active Investigation** | Real-time forensics, incident response, threat hunting. | Higher risk of detection by attackers. |
| **Quiet Investigation** | Covert monitoring, insider threat detection, stealth forensics. | Slower, delayed response to threats. |

---

### **Final Tips**  
- Use **active investigation** for **immediate threat response**.  
- Use **quiet investigation** when dealing with **insider threats or persistent attackers**.  
- Always **encrypt communication and logs** to prevent leaks.  

Would you like help setting up a **specific query or hunt** in Velociraptor?
