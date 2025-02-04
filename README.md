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







Since you've followed all troubleshooting steps and the agent is still **disconnected**, let's go deeper to diagnose and fix the issue.  

---

## **🔍 Step 1: Check Wazuh Manager Logs**
On the **manager machine**, run:
```bash
sudo tail -f /var/ossec/logs/ossec.log
```
Look for any errors related to agent connections.  

If you see **authentication errors** or **connection refused**, it may be a registration issue.  

---

## **🔍 Step 2: Check Agent Logs**
On the **agent machine**, run:
```bash
sudo tail -f /var/ossec/logs/ossec.log
```
If you see something like:
```
ERROR: No connection could be established with manager
```
Then the agent is unable to communicate with the manager.  

---

## **🔍 Step 3: Verify That the Manager is Listening on Port 1514**
On the **manager machine**, run:
```bash
sudo netstat -tulnp | grep 1514
```
You should see an output like:
```
tcp  0  0 0.0.0.0:1514  0.0.0.0:*  LISTEN  1234/ossec-analysisd
```
If **nothing appears**, restart the manager:
```bash
sudo systemctl restart wazuh-manager
```
Then check again.  

---

## **🔍 Step 4: Verify Network Connectivity**
### **4.1 Test if the Agent Can Ping the Manager**
On the **agent machine**, run:
```bash
ping -c 4 WAZUH_MANAGER_IP
```
If no response, check your network settings.  

### **4.2 Test if the Agent Can Reach Port 1514**
Run this from the **agent machine**:
```bash
nc -zv WAZUH_MANAGER_IP 1514
```
- If connection **fails**, the port might be blocked.  
- If **successful**, the connection is open, and the issue might be elsewhere.  

---

## **🔍 Step 5: Verify Firewall Settings**
### **5.1 On the Manager Machine**
Run:
```bash
sudo ufw allow 1514/tcp   # Ubuntu/Debian
sudo firewall-cmd --add-port=1514/tcp --permanent  # RHEL/CentOS
sudo firewall-cmd --reload
```

### **5.2 On the Agent Machine**
Ensure the firewall allows outgoing traffic:
```bash
sudo ufw allow out 1514/tcp
```
Restart both the agent and manager after changing firewall rules.

---

## **🔍 Step 6: Re-register the Agent**
If everything seems correct but the agent is still disconnected, re-register it:  

### **6.1 Remove Agent from Manager**
On the **manager**, run:
```bash
sudo /var/ossec/bin/manage_agents
```
1. Choose **R (Remove an agent)** and remove the disconnected agent.  
2. Choose **A (Add an agent)** to re-add it.  
3. Extract the new agent key (**E (Extract key for an agent)**).  

### **6.2 Remove Old Agent Configuration**
On the **agent machine**, delete the current agent data:
```bash
sudo rm -rf /var/ossec/queue/rids/*
sudo rm -rf /var/ossec/var/run/*
sudo rm -rf /var/ossec/etc/client.keys
```

### **6.3 Re-register the Agent with a New Key**
```bash
sudo /var/ossec/bin/agent-auth -m WAZUH_MANAGER_IP -p 1514 -k "PASTE_NEW_KEY_HERE"
sudo systemctl restart wazuh-agent
```

---

## **🔍 Step 7: Check Again**
On the **manager**, run:
```bash
sudo /var/ossec/bin/agent_control -l
```
If the agent **is still disconnected**, please share:  
- The **error messages** from `/var/ossec/logs/ossec.log` (agent and manager).  
- The **output** of `netstat -tulnp | grep 1514` on the manager.  
- The **result** of `nc -zv WAZUH_MANAGER_IP 1514` on the agent.  

I’ll help you debug further based on these logs!
