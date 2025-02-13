It sounds like your custom Wazuh setup isn’t generating alerts from your script output. Let’s troubleshoot step-by-step:

1. **Check the Agent Logs:**  
   Run this on the agent to see if your script output is being logged:  
   ```bash
   cat /var/ossec/logs/ossec.log | grep localfile
   ```
   or  
   ```bash
   cat /var/ossec/logs/ossec.log | grep your_custom_tag
   ```
   Make sure the output from your script appears in the agent logs.

2. **Verify Localfile Configuration:**  
   Confirm that your `<localfile>` block is correctly set up in the agent's `ossec.conf`:
   ```xml
   <localfile>
     <log_format>full_command</log_format>
     <command>your_script.sh</command>
     <alias>custom-script</alias>
   </localfile>
   ```
   Ensure there are no typos and that the script has executable permissions:
   ```bash
   chmod +x /path/to/your_script.sh
   ```

3. **Check Decoder:**  
   Confirm that the decoder is correctly defined and active on the manager. You can test it with:
   ```bash
   /var/ossec/bin/wazuh-logtest
   ```
   Paste the expected log line and see if the decoder extracts the right fields.

4. **Check Rule Matching:**  
   In `wazuh-logtest`, ensure your rule triggers. If it doesn’t, check the `if_sid`, `group`, and `level` settings.

5. **Restart Services Properly:**  
   Restart both agent and manager:
   ```bash
   systemctl restart wazuh-agent
   systemctl restart wazuh-manager
   ```
   Then check for errors:
   ```bash
   journalctl -u wazuh-agent -xe
   journalctl -u wazuh-manager -xe
   ```

6. **Check Manager Logs:**  
   On the manager:
   ```bash
   cat /var/ossec/logs/ossec.log | grep custom-script
   ```
   If nothing appears, there may be a problem with decoding or rule configuration.

Would you like to share your `localfile`, decoder, and rule configurations? I can review them for errors.
