Here's a **step-by-step guide** for setting up a **Wazuh manager and agent** using only the terminal.

---

# **🔹 Step 1: Install Wazuh Manager**
### **1.1 Update System & Install Dependencies**
```bash
sudo apt update && sudo apt install curl apt-transport-https unzip -y
```

### **1.2 Add Wazuh Repository & Install Manager**
```bash
curl -sO https://packages.wazuh.com/4.6/wazuh-install.sh
sudo bash wazuh-install.sh --wazuh-manager
```

### **1.3 Start & Enable the Manager**
```bash
sudo systemctl start wazuh-manager
sudo systemctl enable wazuh-manager
```

### **1.4 Check Status**
```bash
sudo systemctl status wazuh-manager
```

---

# **🔹 Step 2: Install & Configure Wazuh Agent**
> Perform these steps on the machine you want to monitor.

### **2.1 Add Wazuh Repository & Install Agent**
```bash
curl -sO https://packages.wazuh.com/4.6/wazuh-install.sh
sudo bash wazuh-install.sh --wazuh-agent
```

### **2.2 Configure Agent**
Edit the configuration file:
```bash
sudo nano /var/ossec/etc/ossec.conf
```
Find the `<client>` section and set the Wazuh manager's IP:
```xml
<client>
  <server>
    <address>WAZUH_MANAGER_IP</address>
    <port>1514</port>
  </server>
</client>
```
Save and exit (`CTRL+X`, `Y`, `Enter`).

### **2.3 Start & Enable Agent**
```bash
sudo systemctl start wazuh-agent
sudo systemctl enable wazuh-agent
```

---

# **🔹 Step 3: Register Agent with the Manager**
> Run these commands on the **Wazuh manager**.

### **3.1 Add the Agent**
```bash
sudo /var/ossec/bin/manage_agents
```
1. Choose **A (Add an agent)**.
2. Enter:
   - **Agent name** (e.g., `agent1`).
   - **Agent IP address**.
3. Exit.

### **3.2 Extract the Agent Key**
```bash
sudo /var/ossec/bin/manage_agents
```
1. Choose **E (Extract key for an agent)**.
2. Copy the key.

### **3.3 Import Key on the Agent**
> On the **agent machine**, run:
```bash
sudo /var/ossec/bin/agent-auth -m WAZUH_MANAGER_IP -p 1514 -k "PASTE_KEY_HERE"
```

### **3.4 Restart Agent**
```bash
sudo systemctl restart wazuh-agent
```

---

# **🔹 Step 4: Verify Connection**
### **4.1 Check Agent Status on Manager**
```bash
sudo /var/ossec/bin/agent_control -l
```
Expected output:
```
ID   Name    IP          Status
001  agent1  x.x.x.x    Active
```

### **4.2 Check Logs on Manager**
```bash
tail -f /var/ossec/logs/alerts/alerts.json
```

---

# **🔹 Step 5: Configure Custom Rules (Optional)**
> Run this on the **manager**.

### **5.1 Edit Local Rules File**
```bash
sudo nano /var/ossec/etc/rules/local_rules.xml
```
Example: Detect SSH failed logins
```xml
<group name="custom, ssh">
  <rule id="100001" level="5">
    <decoded_as>syslog</decoded_as>
    <program>sshd</program>
    <match>Failed password</match>
    <description>SSH failed login attempt detected</description>
  </rule>
</group>
```

### **5.2 Restart Wazuh Manager**
```bash
sudo systemctl restart wazuh-manager
```

### **5.3 Test Rule**
Cause a failed SSH login:
```bash
ssh wronguser@localhost
```
Check alerts:
```bash
tail -f /var/ossec/logs/alerts/alerts.json
```

---

# **🎯 Final Checks**
- Ensure **agent is listed**:  
  ```bash
  sudo /var/ossec/bin/agent_control -l
  ```
- Verify **alerts are being received**:
  ```bash
  tail -f /var/ossec/logs/alerts/alerts.json
  ```
- Restart **manager and agent** if needed:
  ```bash
  sudo systemctl restart wazuh-manager wazuh-agent
  ```

---

### **✅ Setup Complete!**
Now, your **Wazuh manager and agent are connected and sending logs**. Let me know if you need further assistance!
