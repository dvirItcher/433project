Good morning! Since you only want audit logs from the Wazuh agent and need to log commands executed by `root`, follow these steps:

---

### **1. Configure Auditd to Log Root Commands on the Agent**  
Ensure `auditd` is installed and running:  
```bash
sudo apt update
sudo apt install auditd -y
sudo systemctl enable --now auditd
```

#### **Add an Audit Rule to Log Root Commands**
Edit the audit rules file:
```bash
sudo nano /etc/audit/rules.d/audit.rules
```
Add the following line at the bottom:
```bash
-a always,exit -F arch=b64 -F euid=0 -S execve -k root_commands
```
This logs every command executed by `root`.

Apply the rule:
```bash
sudo auditctl -R /etc/audit/rules.d/audit.rules
```
Verify:
```bash
sudo auditctl -l | grep root_commands
```

---

### **2. Configure Wazuh Agent to Send Only Audit Logs**
Edit the Wazuh agent configuration file:
```bash
sudo nano /var/ossec/etc/ossec.conf
```
Modify the `<localfile>` section to only send audit logs:
```xml
<localfile>
  <log_format>audit</log_format>
  <location>/var/log/audit/audit.log</location>
</localfile>
```
Save and restart the agent:
```bash
sudo systemctl restart wazuh-agent
```

---

### **3. Create a Wazuh Rule for Root Commands in the Manager**
On the Wazuh Manager, create a custom rule:
```bash
sudo nano /var/ossec/rules/root_commands.xml
```
Add:
```xml
<group name="audit_root_command">
  <rule id="100100" level="15">
    <decoded_as>audit</decoded_as>
    <field name="audit.key">root_commands</field>
    <description>Root executed a command: $(audit.exe)</description>
  </rule>
</group>
```
Save and restart Wazuh Manager:
```bash
sudo systemctl restart wazuh-manager
```

---

### **4. Verify Alerts**
Check if logs are received:
```bash
sudo cat /var/log/audit/audit.log | grep root_commands
```
Check if Wazuh processed the logs:
```bash
sudo cat /var/ossec/logs/alerts/alerts.json | jq '. | select(.rule.id == 100100)'
```

---

Let me know if you need further debugging!






You're right to ask about the decoder! Wazuh already includes a built-in decoder for `auditd` logs (`/var/ossec/etc/decoders/audit-keys.xml`), so in most cases, you **do not** need to create a custom decoder.  

However, if your logs are not being decoded correctly, you may need to define a custom decoder.

---

### **1. Check if Audit Logs Are Being Decoded**  
Run the following on the **manager** to see if audit logs are being processed:  
```bash
sudo cat /var/ossec/logs/alerts/alerts.json | jq '. | select(.decoder.name == "audit")'
```
If audit logs are missing, Wazuh may not be decoding them properly.

---

### **2. Create a Custom Decoder (If Needed)**  
If Wazuh is not decoding audit logs properly, create a custom decoder on the **manager**:  
```bash
sudo nano /var/ossec/etc/decoders/custom-audit-decoder.xml
```
Add:
```xml
<decoder name="custom_audit">
  <prematch>type=EXECVE</prematch>
  <regex>.*audit\(\d+\.\d+:\d+\): .* key="(\w+)"</regex>
  <order>audit.key</order>
</decoder>
```
Save and restart Wazuh Manager:
```bash
sudo systemctl restart wazuh-manager
```

---

### **3. Test the Decoder**  
Run this command to test if your custom decoder is working:  
```bash
echo 'type=EXECVE audit(1710000000.123:456): argc=3 a0="ls" a1="-l" a2="/root" key="root_commands"' | /var/ossec/bin/ossec-logtest
```
If it works, you should see `custom_audit` as the decoder.

Let me know if you need further debugging!
