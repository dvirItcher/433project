Yes! You need a **decoder** to extract `audit.key` from the raw audit logs before Wazuh can match your custom rule. Let’s go through **all steps, including decoding**.

---

## **🔹 Step 1: Enable Audit Logging on the Agent**
> **On the agent**, install `auditd` (if not already installed):
```bash
sudo apt install auditd -y    # Ubuntu/Debian
sudo yum install audit -y     # CentOS/RHEL
```
> **Add an Audit Rule to Log Root Commands:**
```bash
sudo auditctl -a always,exit -F arch=b64 -F uid=0 -S execve -k root_command
sudo auditctl -a always,exit -F arch=b32 -F uid=0 -S execve -k root_command
```
> **Verify Rule is Active:**
```bash
sudo auditctl -l | grep root_command
```
✅ Expected output:
```
-a always,exit -F arch=b64 -F uid=0 -S execve -k root_command
-a always,exit -F arch=b32 -F uid=0 -S execve -k root_command
```

---

## **🔹 Step 2: Test Audit Logging**
> **Run a root command to generate logs:**
```bash
sudo ls /root
```
> **Check if `auditd` Captured It:**
```bash
sudo ausearch -k root_command --start today
```
✅ Expected output:
```
type=SYSCALL msg=audit(1672523674.934:1234): key="root_command"
```
🔴 **If no logs appear:**  
- Check if `auditd` is running:
  ```bash
  sudo systemctl status auditd
  ```
- If inactive, start it:
  ```bash
  sudo systemctl start auditd
  sudo systemctl enable auditd
  ```

---

## **🔹 Step 3: Configure Wazuh Agent to Monitor Audit Logs**
> **On the agent, edit Wazuh config file:**
```bash
sudo nano /var/ossec/etc/ossec.conf
```
📌 **Add this inside `<localfile>`**:
```xml
<localfile>
  <log_format>audit</log_format>
  <location>/var/log/audit/audit.log</location>
</localfile>
```
✅ **Restart the agent**:
```bash
sudo systemctl restart wazuh-agent
```

> **Check if Wazuh Reads Audit Logs:**
```bash
sudo tail -f /var/log/audit/audit.log
```
- If logs are **missing**, go back to **Step 2**.

---

## **🔹 Step 4: Add a Custom Decoder on the Manager**
> **On the manager, create a new decoder file:**
```bash
sudo nano /var/ossec/etc/decoders/local_decoder.xml
```
📌 **Paste this decoder:**
```xml
<decoder name="auditd">
  <prematch>{"type":"SYSCALL"</prematch>
  <regex>"audit.key":"([^"]+)"</regex>
  <order>audit.key</order>
</decoder>
```
✅ **Restart the manager:**
```bash
sudo systemctl restart wazuh-manager
```

---

## **🔹 Step 5: Create a Custom Rule on the Manager**
> **On the manager, edit the rules file:**
```bash
sudo nano /var/ossec/etc/rules/local_rules.xml
```
📌 **Add this rule:**
```xml
<group name="audit, root">
  <rule id="100002" level="15">
    <decoded_as>json</decoded_as>
    <field name="audit.key">root_command</field>
    <description>Root user executed a command</description>
  </rule>
</group>
```
✅ **Restart the manager:**
```bash
sudo systemctl restart wazuh-manager
```

---

## **🔹 Step 6: Test Everything**
### **6.1 Run a Root Command on the Agent**
```bash
sudo ls /root
```

### **6.2 Check if Wazuh Captures It**
> **On the manager, check for alerts:**
```bash
sudo tail -f /var/ossec/logs/alerts/alerts.json
```
✅ If you see a **Level 15 alert**, it works! 🎉  

---

# **🎯 Final Checklist**
✅ `auditd` logs root commands (`ausearch -k root_command --start today`)  
✅ Wazuh agent reads `/var/log/audit/audit.log`  
✅ Wazuh **decoder extracts** `audit.key=root_command`  
✅ Wazuh **rule triggers** a Level 15 alert  

---

### **Need Help?**
If it **still doesn’t work**, send me:
1. Output of:
   ```bash
   sudo auditctl -l | grep root_command
   ```
2. Output of:
   ```bash
   sudo ausearch -k root_command --start today
   ```
3. Output of:
   ```bash
   sudo tail -f /var/log/audit/audit.log
   ```
4. Output of:
   ```bash
   sudo tail -f /var/ossec/logs/alerts/alerts.json
   ```
5. Output of:
   ```bash
   sudo /var/ossec/bin/ossec-logtest
   ```

I’ll debug it with you! 🚀
