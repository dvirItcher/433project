To run a remote command using a Wazuh **wodle** (specifically, the `command` wodle), and then decode and create rules for the command’s output, follow these steps:

---

## 1. **Configure the Command Wodle (Agent-Side)**  
The **wodle** can execute commands at specified intervals and send the output to the Wazuh manager.

### **Edit the Agent Configuration**  
Open `/var/ossec/etc/ossec.conf` on the **agent** and add a `command` wodle:

```xml
<wodle name="command">
  <disabled>no</disabled>
  <schedule>every 1m</schedule>
  <timeout>60</timeout>
  <run_command>whoami; id</run_command>
  <alias>custom_command_output</alias>
</wodle>
```

**Explanation:**  
- `schedule`: Executes the command every 1 minute.  
- `timeout`: Maximum execution time.  
- `run_command`: Command to execute remotely.  
- `alias`: A label to track the command's output.

**Restart the agent:**  
```bash
sudo systemctl restart wazuh-agent
```

---

## 2. **Create a Custom Decoder (Manager-Side)**  

Wazuh uses decoders to parse logs from commands like the one above.

### **Add a Decoder**  
Create a new decoder file:  
```bash
sudo nano /var/ossec/etc/decoders/custom_command_decoder.xml
```

Add the following decoder:

```xml
<decoder name="custom-command">
  <prematch>^custom_command_output</prematch>
</decoder>
```

### **Explanation:**  
- `prematch`: Matches the alias specified in the wodle output.  

**Restart the manager:**  
```bash
sudo systemctl restart wazuh-manager
```

---

## 3. **Create a Custom Rule (Manager-Side)**  

Rules determine which logs trigger alerts.

### **Add a Rule**  
Open the custom rules file:  
```bash
sudo nano /var/ossec/etc/rules/local_rules.xml
```

Add a new rule:

```xml
<group name="custom_command_group" gid="1001">
  <rule id="100100" level="15">
    <decoded_as>custom-command</decoded_as>
    <description>Command executed remotely: $(log)</description>
  </rule>
</group>
```

**Explanation:**  
- `decoded_as`: Matches the decoder name we defined.  
- `log`: Displays the command output in the alert.

**Restart the manager:**  
```bash
sudo systemctl restart wazuh-manager
```

---

## 4. **Testing & Debugging**

### **Force Command Execution:**  
Wait for the scheduled execution or force it by restarting the agent.

### **Check Logs:**  
On the agent:  
```bash
tail -f /var/ossec/logs/ossec.log
```

On the manager:  
```bash
tail -f /var/ossec/logs/alerts/alerts.json
```

Look for logs with your command output.

---

This setup should allow Wazuh to:  
1. **Run commands remotely** with the `command` wodle.  
2. **Decode the output** with a custom decoder.  
3. **Generate alerts** based on the command’s output using a custom rule.

Let me know if you encounter any issues!
