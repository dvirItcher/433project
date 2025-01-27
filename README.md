The **UAC (User Account Control) bypass using registry techniques** is a method attackers use to escalate privileges on a Windows system without triggering the UAC prompt. It involves manipulating the Windows Registry to exploit legitimate processes that run with higher privileges. Here’s a detailed explanation:

---

### **UAC Bypass Using Registry Technique**

1. **Understanding UAC and Privilege Escalation**:
   - **UAC** is a security feature in Windows that prevents unauthorized changes by prompting for administrator approval when an application requires elevated privileges.
   - Attackers bypass UAC by exploiting trusted processes that are whitelisted by UAC to execute malicious code without a prompt.

2. **Registry-Based UAC Bypass**:
   - Some legitimate processes in Windows (e.g., `fodhelper.exe`, `eventvwr.exe`) automatically elevate privileges when launched. These processes look for specific registry keys to load configurations or commands.
   - Attackers manipulate these registry keys to execute malicious payloads with elevated privileges.
   - Example:
     - An attacker sets malicious commands in a registry key like `HKCU\Software\Classes\ms-settings\Shell\Open\command`.
     - When a trusted process such as `fodhelper.exe` is launched, it reads the modified registry key and executes the attacker's command with elevated privileges.

3. **Commonly Exploited Registry Paths**:
   - `HKCU\Software\Classes\ms-settings\Shell\Open\command`
   - `HKCU\Software\Classes\exefile\shell\open\command`
   - `HKCU\Software\Microsoft\Windows\CurrentVersion\Run`

4. **Why It Works**:
   - The trusted processes do not validate the integrity of the registry values they read.
   - UAC whitelists these processes, so the system automatically grants them elevated privileges.

---

### **Sysmon Event IDs and Logging UAC Bypass**

**Sysmon** (System Monitor) is a Windows system monitoring tool from Microsoft’s Sysinternals Suite. It provides detailed logging for security monitoring. The relevant Sysmon Event IDs for detecting registry-based UAC bypass are:

1. **Event ID 12: Registry Object Created or Deleted**
   - Logs the creation or deletion of registry keys.
   - This can detect the addition of malicious registry keys like `HKCU\Software\Classes\ms-settings\Shell\Open\command`.

2. **Event ID 13: Registry Value Set**
   - Logs changes to registry values.
   - Attackers modify specific registry keys/values to insert malicious commands, and this event captures such changes.

3. **Event ID 14: Registry Object Renamed**
   - Logs when registry keys are renamed.
   - Some bypass techniques involve renaming registry keys to activate payloads.

---

### **Why These Events Are Useful**

- **Visibility**: Sysmon provides granular visibility into registry changes, including:
  - The exact key or value being modified.
  - The process responsible for the modification.
  - The timestamp of the activity.

- **Correlation with Legitimate Processes**: Security analysts can correlate registry changes (e.g., changes to `ms-settings`) with the execution of processes like `fodhelper.exe`.

- **Indicators of Compromise (IOCs)**:
  - Sudden creation or modification of keys linked to trusted processes.
  - Malicious command strings in registry values.

---

### **Mitigation and Detection**

1. **Monitoring with Sysmon**:
   - Enable Sysmon with a well-configured XML configuration file to log registry changes.
   - Focus on sensitive keys like `HKCU\Software\Classes\ms-settings\Shell\Open\command`.

2. **Analyzing Logs**:
   - Use a SIEM (e.g., Splunk, ELK) to aggregate and analyze Sysmon logs.
   - Set alerts for suspicious registry changes.

3. **Preventive Measures**:
   - Restrict write access to sensitive registry keys.
   - Enforce application whitelisting using tools like Windows Defender
   - Application Control (WDAC).











COM hijacking via the Windows registry is a persistence and privilege escalation technique used by attackers to hijack legitimate processes. Here's a breakdown:

---

### **What is COM Hijacking?**
- **COM (Component Object Model):** A Windows framework that allows software components to interact.
- **Hijacking Technique:**
  - Windows uses the registry to locate COM objects (CLSID keys).
  - Attackers can replace the legitimate COM object registration in the registry with malicious code.
  - When a legitimate application or service calls the hijacked COM object, it executes the attacker's code.

#### **Steps in COM Hijacking:**
1. **Locate CLSID Registry Keys:**
   - COM objects are registered under specific CLSID keys in the Windows Registry, such as:
     - `HKEY_CLASSES_ROOT\CLSID`
     - `HKEY_LOCAL_MACHINE\SOFTWARE\Classes\CLSID`

2. **Modify Registry Entries:**
   - Replace the path of the legitimate COM server (DLL or EXE) with a malicious one.
   - The system executes the attacker’s payload when the COM object is invoked.

3. **Persistence:**
   - Since the hijacked registry key persists across reboots, the attacker maintains a foothold on the system.

---

### **Sysmon Event IDs for Detecting COM Hijacking**

#### **Sysmon Overview:**
Sysinternals Sysmon is a system monitoring tool that logs various events. Event IDs 12, 13, and 14 are particularly useful for detecting suspicious registry modifications associated with COM hijacking.

#### **Relevant Sysmon Event IDs:**
1. **Event ID 12: Registry Object Added/Created**
   - Triggers when a new registry key or value is created.
   - Detects when attackers create new CLSID registry entries or hijack existing ones.

2. **Event ID 13: Registry Value Set**
   - Triggers when a registry value is modified.
   - Logs when attackers overwrite legitimate CLSID paths with malicious ones.

3. **Event ID 14: Registry Object Deleted**
   - Triggers when a registry key or value is deleted.
   - Can reveal cleanup activities or deletion of evidence by attackers.

#### **Why Sysmon Helps Detect COM Hijacking:**
- **Detailed Logging:** Tracks precise registry changes, including the process responsible for the modification.
- **Visibility:** Highlights suspicious changes to CLSID keys, allowing analysts to identify and investigate potential hijacks.
- **Correlation:** Combining Event IDs 12-14 with process creation logs (e.g., Event ID 1) helps trace the execution of malicious code linked to a hijacked COM object.

---

### **Detection and Mitigation Tips**
1. **Monitor Critical Registry Paths:**
   - Focus on `HKCR\CLSID`, `HKLM\SOFTWARE\Classes\CLSID`, and other relevant COM-related keys.

2. **Use Sysmon Configurations:**
   - Deploy Sysmon with a well-crafted configuration file to capture and analyze registry events efficiently.

3. **Correlate with Process Logs:**
   - Investigate processes invoked after registry modifications (e.g., DLL or EXE loads).

4. **Harden COM Registrations:**
   - Restrict write access to sensitive registry locations using Group Policy or ACLs.

5. **Endpoint Protection:**
   - Use endpoint detection and response (EDR) solutions to complement Sysmon monitoring.

---

Would you like an example Sysmon configuration to detect COM hijacking more effectively?
4. **Regular Updates**:
   - Keep Windows and Sysinternals tools updated to mitigate newly discovered bypass techniques.

---

By monitoring **Sysmon Event IDs 12-14**, you can effectively detect and analyze UAC bypass attempts that use registry manipulation techniques.
