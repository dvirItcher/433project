Sysmon logs in Wazuh are treated as standard Windows Event Logs, but they require proper configuration to be collected and decoded correctly. However, there are some specific considerations for Sysmon logs in Wazuh:

### 1. **Sysmon Logs Are Collected via Windows Event Channel (Not Just a File)**
   - Unlike standard log files, Sysmon logs are stored in the **Windows Event Log system** under:
     ```
     Applications and Services Logs → Microsoft → Windows → Sysmon → Operational
     ```
   - Wazuh collects them using the **Windows Event Log (`wodle`) module** instead of `localfile`:
     ```xml
     <wodle name="windows_eventchannel">
       <disabled>no</disabled>
       <log_format>eventchannel</log_format>
       <query>
         <name>Sysmon</name>
         <log_name>Microsoft-Windows-Sysmon/Operational</log_name>
       </query>
     </wodle>
     ```
   - If this is missing, Wazuh won’t collect Sysmon logs.

### 2. **EventChannel Logs Are Pre-Processed Differently**
   - Logs from the Windows Event Channel are received as XML before being processed by Wazuh.
   - The decoder must match **the XML structure** of the event.
   - You can check raw logs from Sysmon with:
     ```bash
     cat /var/ossec/logs/archives/archives.json | grep "Sysmon"
     ```
   - If logs appear but aren’t decoded, adjust your decoder to match the XML format.

### 3. **Ensure Sysmon Decoder Matches the XML Format**
   - Sysmon logs look like this in XML:
     ```xml
     <Event>
       <System>
         <Provider Name="Microsoft-Windows-Sysmon" Guid="{GUID}" />
         <EventID>1</EventID>
       </System>
       <EventData>
         <Data Name="Image">C:\Windows\System32\cmd.exe</Data>
         <Data Name="User">NT AUTHORITY\SYSTEM</Data>
       </EventData>
     </Event>
     ```
   - Your decoder should handle this structure properly:
     ```xml
     <decoder name="sysmon">
       <prematch>.*Microsoft-Windows-Sysmon.*</prematch>
       <regex>.*<EventID>(\d+)</EventID>.*</regex>
       <order>event_id</order>
     </decoder>
     ```

### 4. **Confirm Sysmon Events Are Parsed Correctly**
   - Run:
     ```bash
     tail -f /var/ossec/logs/ossec.log | grep -i "sysmon"
     ```
   - If Wazuh sees Sysmon logs but doesn’t decode them, the decoder might not match the log format.

### 5. **Rules Must Be Created for Sysmon Alerts**
   - Decoders structure logs, but rules generate alerts.
   - Example rule for process creation (Event ID 1):
     ```xml
     <group name="sysmon,">
       <rule id="100200" level="12">
         <decoded_as>sysmon</decoded_as>
         <description>Sysmon Process Creation Detected</description>
         <field name="event_id">1</field>
       </rule>
     </group>
     ```
   - Restart Wazuh after adding rules:
     ```bash
     systemctl restart wazuh-manager
     ```

### 6. **Use `ossec-logtest -v` for Debugging**
   - Run:
     ```bash
     echo '<Event>...</Event>' | /var/ossec/bin/ossec-logtest -v
     ```
   - This helps test if Wazuh is decoding Sysmon logs correctly.

If your Sysmon logs are still not decoded, share your decoder XML and a sample log from `archives.json`.
