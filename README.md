If you want to execute a command and process its output using the **wodle** (without relying on `localfile` or external scripts), you can use the `command` wodle. Here’s a step-by-step guide:

---

# 🛠️ **1. Configure the Command Wodle (Agent-Side)**  

Open the agent configuration:  
```bash
sudo nano /var/ossec/etc/ossec.conf
```

Add this `wodle` block:

```xml
<wodle name="command">
  <disabled>no</disabled>
  <schedule>every 1m</schedule>
  <timeout>60</timeout>
  <run_command>whoami; id</run_command>
  <alias>custom_command_output</alias>
  <log_stdout>true</log_stdout>
</wodle>
```

### 🔍 **Explanation:**  
- **`schedule`:** Runs the command every minute.  
- **`timeout`:** Stops execution after 60 seconds if needed.  
- **`run_command`:** Command(s) to execute.  
- **`alias`:** Prefix to identify the log.  
- **`log_stdout`:** Captures command output as log messages.  

**Restart the agent:**  
```bash
sudo systemctl restart wazuh-agent
```

---

# 🔍 **2. Create a Decoder (Manager-Side)**  

Open the custom decoder file:  
```bash
sudo nano /var/ossec/etc/decoders/custom_command_decoder.xml
```

Add this decoder:

```xml
<decoder name="custom-command">
  <prematch>^custom_command_output:</prematch>
</decoder>
```

### 🧠 **How it works:**  
- It matches logs starting with `custom_command_output`, which corresponds to the alias you set in the wodle.  

**Restart the manager:**  
```bash
sudo systemctl restart wazuh-manager
```

---

# 🚨 **3. Create a Rule (Manager-Side)**  

Open the custom rules file:  
```bash
sudo nano /var/ossec/etc/rules/local_rules.xml
```

Add a new rule:

```xml
<group name="custom_command_group" gid="1001">
  <rule id="100200" level="15">
    <decoded_as>custom-command</decoded_as>
    <description>Remote command executed: $(log)</description>
  </rule>
</group>
```

### 🧠 **Explanation:**  
- This rule triggers on any log decoded with the `custom-command` decoder.  
- **Level 15** ensures it appears in `alerts.log`.

**Restart the manager:**  
```bash
sudo systemctl restart wazuh-manager
```

---

# 🧪 **4. Testing & Debugging**  

### **Run the Command Wodle Immediately**  
Wait for the scheduled interval or restart the agent to trigger it manually:

```bash
sudo systemctl restart wazuh-agent
```

### **Check Logs**  
- View the command output in the archives:  
  ```bash
  tail -f /var/ossec/logs/archives/archives.log
  ```
- Check the alerts:  
  ```bash
  tail -f /var/ossec/logs/alerts/alerts.json
  ```

### **Run a Logtest**  
Open the logtest utility:

```bash
/var/ossec/bin/ossec-logtest
```

Paste a sample log from the archives that starts with `custom_command_output`.

### ✅ **Expected Output**  
1. **Decoder match:** `custom-command`  
2. **Rule match:** `100200`  
3. **Alert description:**  
   ```
   Remote command executed: custom_command_output: root - uid=0(root) gid=0(root) groups=0(root)
   ```

---

# 🔍 **5. Troubleshooting Checklist**  

1. **Decoder Not Matching:**  
   - Run `ossec-logtest` to verify the decoder works.  
   - Check the exact log format in `archives.log`.  

2. **No Alerts:**  
   - Make sure the rule `level` is ≥ 3.  
   - Confirm that the custom rules file is properly loaded:  
     ```bash
     grep "custom_command_group" /var/ossec/logs/ossec.log
     ```

3. **Missing Logs:**  
   - Check permissions on `/var/ossec/logs/archives/archives.log`.  

---

This method uses Wazuh’s built-in `command` wodle to execute remote commands, decode their output, and generate alerts based on custom rules. Let me know if you hit any roadblocks! 🚀
