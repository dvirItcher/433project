Let's go step by step again to fix this issue. Since other audit logs are working, the problem is likely with Wazuh's ability to recognize and forward your custom audit logs.

---

## **🔎 Step 1: Verify Audit Logs on the Agent**
Run this on the **agent**:  
```bash
sudo ausearch -k root_command --start today
```
✅ **Expected Output**:  
```
type=SYSCALL msg=audit(1690000000.123:456): key="root_command"
```
🔴 **If missing**, check if the audit rule is loaded:  
```bash
sudo auditctl -l | grep root_command
```
If it's not listed, re-add it:  
```bash
sudo auditctl -a always,exit -F arch=b64 -F uid=0 -S execve -k root_command
sudo auditctl -a always,exit -F arch=b32 -F uid=0 -S execve -k root_command
```
Then restart `auditd`:  
```bash
sudo systemctl restart auditd
```

---

## **🔎 Step 2: Verify Wazuh Agent Reads Audit Logs**
Run this on the **agent**:  
```bash
sudo tail -f /var/log/audit/audit.log | grep root_command
```
Then, in another terminal, run a root command:  
```bash
sudo ls /root
```
✅ **Expected Output**: A log entry containing `"key=root_command"`  

🔴 **If missing**, check if Wazuh is reading `/var/log/audit/audit.log` by running:  
```bash
sudo tail -f /var/ossec/logs/ossec.log | grep audit
```
If Wazuh is **not reading audit logs**, check the agent configuration:  
```bash
sudo nano /var/ossec/etc/ossec.conf
```
Ensure this section exists:  
```xml
<localfile>
  <log_format>audit</log_format>
  <location>/var/log/audit/audit.log</location>
</localfile>
```
Then restart the agent:  
```bash
sudo systemctl restart wazuh-agent
```

---

## **🔎 Step 3: Check If the Manager Receives the Logs**
Run this on the **manager**:  
```bash
sudo tail -f /var/ossec/logs/archives/archives.log | grep root_command
```
Then, **run a root command on the agent**:  
```bash
sudo ls /root
```
✅ **Expected Output**: A log entry containing `"key=root_command"`

🔴 **If missing**, check if the agent is active on the manager:  
```bash
sudo /var/ossec/bin/agent_control -l
```
If the agent is **disconnected**, restart it:  
```bash
sudo systemctl restart wazuh-agent
```

---

## **🔎 Step 4: Test Wazuh's Decoding on the Manager**
Run this on the **manager**:  
```bash
sudo /var/ossec/bin/ossec-logtest
```
Paste a sample audit log:
```json
{"audit.key":"root_command"}
```
✅ **Expected Output**:  
```
Rule ID: 100002 | Level: 15 | Root user executed a command
```
🔴 **If no rule matches, Wazuh is not decoding your logs correctly.**  

---

## **🔎 Step 5: Fix Decoders on the Manager**
Edit the Wazuh decoder file:  
```bash
sudo nano /var/ossec/etc/decoders/local_decoder.xml
```
Add this decoder:  
```xml
<decoder name="auditd">
  <prematch>{"type":"SYSCALL"</prematch>
  <regex>"audit.key":"([^"]+)"</regex>
  <order>audit.key</order>
</decoder>
```
Restart the Wazuh manager:  
```bash
sudo systemctl restart wazuh-manager
```

---

## **🔎 Step 6: Fix Rules on the Manager**
Edit the Wazuh rules file:  
```bash
sudo nano /var/ossec/etc/rules/local_rules.xml
```
Add this rule:  
```xml
<group name="audit, root">
  <rule id="100002" level="15">
    <decoded_as>json</decoded_as>
    <field name="audit.key">root_command</field>
    <description>Root user executed a command</description>
  </rule>
</group>
```
Restart the Wazuh manager:  
```bash
sudo systemctl restart wazuh-manager
```

---

## **🎯 Final Debugging Checklist**
✅ `auditd` logs `root_command` (`ausearch -k root_command --start today`)  
✅ Wazuh **reads** `/var/log/audit/audit.log`  
✅ Wazuh agent **sends logs** to the manager (`archives.log`)  
✅ Wazuh **decodes** `audit.key=root_command` (`ossec-logtest`)  
✅ Wazuh **triggers Level 15 alerts** (`alerts.json`)  

---

### **🔎 Still Not Working?**
Please send me the output of:
1.  
   ```bash
   sudo ausearch -k root_command --start today
   ```
2.  
   ```bash
   sudo tail -f /var/log/audit/audit.log | grep root_command
   ```
3.  
   ```bash
   sudo tail -f /var/ossec/logs/archives/archives.log
   ```
4.  
   ```bash
   sudo /var/ossec/bin/ossec-logtest
   ```

I'll analyze it and help you fix it! 🚀
