Yes, using Velociraptor to analyze registry changes, file modifications, event logs, and execution artifacts can help determine **who or what changed the lock screen image**. Here’s how you can pinpoint the responsible action:

---

### **1. Identifying the Process or User Responsible**
#### **Check Security Event Logs (Who Made the Change?)**
- **Event ID 4624 (Login Success):** Helps identify who was logged in at the time of change.
- **Event ID 4672 (Admin Privileges Used):** Checks if an admin account was involved.
- **Event ID 4688 (New Process Created):** Reveals if a script, PowerShell, or external program changed the lock screen.

#### **VQL Query to Find Suspicious Logins and Actions**
```sql
SELECT * FROM artifacts.windows.events.EventLogs
WHERE EventID IN (4624, 4672, 4688)
ORDER BY TimeGenerated DESC
```
- This helps track the user or process that may have changed the settings.

---

### **2. Checking Which Program Made the Change**
#### **Look for Execution Artifacts**
If **regedit.exe**, **powershell.exe**, or another process changed the lock screen settings, you can detect it using Prefetch or process logs.

##### **Velociraptor Prefetch Query**
```sql
SELECT * FROM artifacts.windows.prefetch.PrefetchFiles
WHERE Executable LIKE '%regedit%' OR Executable LIKE '%powershell%' OR Executable LIKE '%gpedit%'
ORDER BY LastExecuted DESC
```
- If you see **regedit.exe** or **powershell.exe** running at the time of the change, it likely means a script or manual edit was used.

##### **Check PowerShell Logs for Suspicious Commands**
```sql
SELECT * FROM artifacts.windows.events.EventLogs
WHERE Source = 'Microsoft-Windows-PowerShell'
AND EventID = 4104
```
- This will show PowerShell commands that might have been used to modify lock screen settings.

---

### **3. Checking File Changes for the Lock Screen Image**
If the lock screen image was **manually changed** by replacing a file, Velociraptor can track **when and how** it was modified.

##### **Find Recent Changes in Lock Screen Image Folders**
```sql
SELECT * FROM artifacts.filesystem.Glob
WHERE Path LIKE 'C:\ProgramData\Microsoft\Windows\SystemData\%'
OR Path LIKE 'C:\Users\%\AppData\Local\Microsoft\Windows\Themes\%'
ORDER BY mtime DESC
```
- This helps identify when the image file was replaced and possibly by which process.

---

### **4. Investigating Group Policy (If Enforced by GPO)**
If the lock screen was changed via **Group Policy**, the change may have been made by an administrator.

##### **Check GPO Lock Screen Settings**
```sql
SELECT * FROM artifacts.windows.registry.SysmonRegistry
WHERE KeyPath LIKE 'HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\Personalization'
```
- If `LockScreenImage` is set here, it means Group Policy forced the change.

---

### **5. Correlating Evidence**
Once you gather data from:
1. **Event logs (Who was logged in, what ran?)**
2. **Prefetch files (Was a tool like regedit or PowerShell used?)**
3. **File modifications (Was the lock screen image manually changed?)**
4. **Registry changes (Was the lock screen setting modified programmatically?)**
5. **Group Policy settings (Was it an enforced admin change?)**

You can **cross-reference timestamps** and determine **how** the lock screen image was changed and by **whom**.

---

### **Conclusion**
- If **PowerShell or regedit** was used → A user or script changed it.
- If the **file was replaced** → A manual file modification occurred.
- If **Group Policy was modified** → An admin enforced the change.
- If a **malware process modified it** → Prefetch and security logs might reveal an unknown process.

Would you like help refining the VQL queries based on your findings?
