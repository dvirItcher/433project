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
   - Enforce application whitelisting using tools like Windows Defender Application Control (WDAC).

4. **Regular Updates**:
   - Keep Windows and Sysinternals tools updated to mitigate newly discovered bypass techniques.

---

By monitoring **Sysmon Event IDs 12-14**, you can effectively detect and analyze UAC bypass attempts that use registry manipulation techniques.
