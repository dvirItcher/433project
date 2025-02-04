Yes! You can use `auditctl` to add the rule dynamically without modifying rule files directly. However, `auditctl` changes **do not persist** after a reboot, so you’ll need to add them permanently later in `/etc/audit/rules.d/`.

---

# **🔹 Step 1: Add Audit Rule Using auditctl**
> **On the agent machine**, run:  
```bash
sudo auditctl -a always,exit -F arch=b64 -F uid=0 -S execve -k root_command
```
Explanation:  
- `-a always,exit` → Log events when a syscall **exits**.  
- `-F arch=b64` → Apply to 64-bit architecture.  
- `-F uid=0` → Filter for **UID 0** (root user).  
- `-S execve` → Monitor execution of any command.  
- `-k root_command` → Tag logs with the key `root_command`.  

For **32-bit systems**, also add:  
```bash
sudo auditctl -a always,exit -F arch=b32 -F uid=0 -S execve -k root_command
```

---

# **🔹 Step 2: Test the Audit Rule**
Run any root command:
```bash
sudo ls /root
```
Check if it was logged:
```bash
sudo ausearch -k root_command --start today
```
If it appears, the rule works! ✅

---

# **🔹 Step 3: Make the Rule Persistent**
> `auditctl` rules are lost after a reboot. To make them permanent:
```bash
sudo nano /etc/audit/rules.d/root_commands.rules
```
Add:
```
-a always,exit -F arch=b64 -F uid=0 -S execve -k root_command
-a always,exit -F arch=b32 -F uid=0 -S execve -k root_command
```
Save & exit.

Reload audit rules:
```bash
sudo augenrules --load
```
Check active rules:
```bash
sudo auditctl -l
```

---

# **🔹 Step 4: Configure Wazuh to Collect Audit Logs**
> Edit Wazuh config on the **agent**:
```bash
sudo nano /var/ossec/etc/ossec.conf
```
Add:
```xml
<localfile>
  <log_format>audit</log_format>
  <location>/var/log/audit/audit.log</location>
</localfile>
```
Save & restart the agent:
```bash
sudo systemctl restart wazuh-agent
```

---

# **🔹 Step 5: Create a High-Severity Wazuh Rule (Level 15)**
> **On the Wazuh manager**, edit local rules:
```bash
sudo nano /var/ossec/etc/rules/local_rules.xml
```
Add:
```xml
<group name="audit, root">
  <rule id="100002" level="15">
    <decoded_as>audit</decoded_as>
    <match>root_command</match>
    <description>Root user executed a command</description>
  </rule>
</group>
```
Save & restart the manager:
```bash
sudo systemctl restart wazuh-manager
```

---

# **🔹 Step 6: Test & Verify**
### **6.1 Run a Root Command**
```bash
sudo ls /root
```

### **6.2 Check Audit Logs**
```bash
sudo ausearch -k root_command --start today
```

### **6.3 Check Wazuh Alerts**
On the **manager**:
```bash
tail -f /var/ossec/logs/alerts/alerts.json
```
✅ If successful, you'll see a **Level 15 alert** when root executes a command!

---

# **🎯 Summary**
- Used `auditctl` for live testing.  
- Made it persistent in `/etc/audit/rules.d/`.  
- Configured Wazuh to capture audit logs.  
- Created a **Level 15** Wazuh rule for **root command execution alerts**.  

Now Wazuh will **detect and alert** every time a root user runs a command! 🚀  

Let me know if you need modifications!
