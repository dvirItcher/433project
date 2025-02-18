To create a Wazuh rule to detect `Sysmon Event ID 1` (which indicates process creation), you’ll need to follow these steps:

### **1. Verify Sysmon Logs Are Being Forwarded**
Ensure the Sysmon logs are correctly forwarded to Wazuh. Event ID 1 logs should appear in Wazuh if Sysmon is configured properly. Check logs like this:
```bash
grep "EventID\":1" /var/ossec/logs/archives/archives.json
```
Or use Wazuh Kibana/manager interface.

---

### **2. Create a Custom Decoder (if needed)**
If you haven't already, add a custom decoder for Sysmon events:

- Open the custom decoder file (e.g., `/var/ossec/etc/decoders/local_decoder.xml`) on the Wazuh manager.

```xml
<decoder name="sysmon_event">
  <program_name>winlogbeat</program_name>
  <field name="win.system.providerName">Microsoft-Windows-Sysmon</field>
</decoder>

<decoder name="sysmon_event_id_1">
  <parent>sysmon_event</parent>
  <field name="win.system.eventID">1</field>
</decoder>
```

- Save and restart Wazuh Manager:
```bash
systemctl restart wazuh-manager
```

---

### **3. Create a Custom Rule for Event ID 1**
Now, add the rule to detect process creation events.

- Open the custom rules file (e.g., `/var/ossec/etc/rules/local_rules.xml`).

```xml
<group name="sysmon_process_creation" gid="1001">
    <rule id="100100" level="10">
        <decoded_as>json</decoded_as>
        <field name="win.system.providerName">Microsoft-Windows-Sysmon</field>
        <field name="win.system.eventID">1</field>
        <description>Sysmon Event ID 1 - Process creation detected</description>
        <group>sysmon,process_creation</group>
    </rule>
</group>
```

### **Optional Enhancements**
- To monitor specific processes (e.g., `cmd.exe` or `powershell.exe`):
```xml
<rule id="100101" level="12">
    <decoded_as>json</decoded_as>
    <field name="win.system.providerName">Microsoft-Windows-Sysmon</field>
    <field name="win.system.eventID">1</field>
    <field name="win.event_data.Image">.*(powershell.exe|cmd.exe).*</field>
    <description>Sysmon Event ID 1 - Suspicious process created (PowerShell or CMD)</description>
    <group>sysmon,process_creation,suspicious</group>
</rule>
```

---

### **4. Restart Wazuh Manager**
After editing the rule files, restart the Wazuh manager:
```bash
systemctl restart wazuh-manager
```

---

### **5. Test the Rule**
Trigger process creation by running a command like `cmd.exe` or `powershell.exe` on the monitored machine. Check if Wazuh captures and alerts you on Event ID 1.

Let me know if you encounter any issues!
