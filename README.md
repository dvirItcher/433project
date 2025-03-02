To **detect the creation of an admin account** on your machine using **Velociraptor**, you need to monitor **Windows Event Logs, registry changes, and user management commands**. Here’s a structured approach:

---

## **1. Detect Admin Account Creation via Windows Event Logs**
Windows logs user creation events under **Event ID 4720 (User Account Created)** and admin privilege assignments under **Event ID 4732 (User Added to Administrators Group).**

### **VQL Query to Detect Admin Account Creation**
```sql
SELECT * FROM artifacts.windows.events.EventLogs
WHERE EventID IN (4720, 4732)
ORDER BY TimeGenerated DESC
```
- **Event ID 4720** → A new user was created.
- **Event ID 4732** → A user was added to the **Administrators** group.

> **How this helps:** If a new admin account is created (even stealthily), you’ll see the details, including the username and timestamp.

---

## **2. Monitor Registry Changes for New Admin Users**
When a new user is created, their profile information is stored in the **SAM (Security Account Manager) registry**.

### **VQL Query to Detect New User Registry Entries**
```sql
SELECT * FROM artifacts.windows.registry.SysmonRegistry
WHERE KeyPath LIKE 'HKEY_LOCAL_MACHINE\SAM\SAM\Domains\Account\Users'
ORDER BY LastModified DESC
```
> **How this helps:** This detects any new user added at the registry level, even if logs are cleared.

---

## **3. Detect Admin Account Creation Using PowerShell or CMD**
If an attacker uses **PowerShell** or **Command Prompt** to create an admin, it can be detected in **Event ID 4688 (New Process Created).**

### **VQL Query to Detect Suspicious User Creation Commands**
```sql
SELECT * FROM artifacts.windows.events.EventLogs
WHERE EventID = 4688
AND (CommandLine LIKE '%net user%'
OR CommandLine LIKE '%New-LocalUser%'
OR CommandLine LIKE '%Add-LocalGroupMember%'
OR CommandLine LIKE '%net localgroup administrators%')
ORDER BY TimeGenerated DESC
```
> **How this helps:** It identifies if a **"net user"**, **PowerShell**, or **group modification command** was executed.

---

## **4. Track User Creation with WMI (Windows Management Instrumentation)**
Attackers might use **WMI** to add users. The WMI event log records these actions under **Event ID 5857**.

### **VQL Query for WMI-Based User Creation**
```sql
SELECT * FROM artifacts.windows.events.EventLogs
WHERE EventID = 5857
ORDER BY TimeGenerated DESC
```
> **How this helps:** This detects admin creation via **WMI-based attacks**.

---

## **5. Monitor Changes in "Administrators" Group Membership**
The list of admin users is stored in:

```
HKEY_LOCAL_MACHINE\SAM\SAM\Domains\Account\Aliases\00000220
```
This registry key contains all **Administrators Group Members**.

### **VQL Query to Detect New Admin in Local Group**
```sql
SELECT * FROM artifacts.windows.registry.SysmonRegistry
WHERE KeyPath LIKE 'HKEY_LOCAL_MACHINE\SAM\SAM\Domains\Account\Aliases\00000220'
ORDER BY LastModified DESC
```
> **How this helps:** If a user was **added as an admin**, this will detect it.

---

## **6. Enable Continuous Monitoring in Velociraptor**
To **continuously monitor for new admin accounts**, you can set up **a Velociraptor hunt** with **Event ID 4720 & 4732**.

1. Open **Velociraptor Web UI**.
2. Go to **Hunt Manager** → Create a new Hunt.
3. Use the **VQL query for admin account creation (Event 4720 & 4732).**
4. Set it to run **continuously**.
5. Configure alerts to notify when a new admin is created.

---

### **Final Notes**
- If a **stealth admin** is created, logs might be deleted. To counter this:
  - **Enable Sysmon** for better process tracking.
  - **Use registry monitoring** (as attackers rarely modify both logs & registry).
- If an **unknown process** is creating admins, check **Event ID 4688** for command execution.

Would you like help setting up automated alerts for this detection?
