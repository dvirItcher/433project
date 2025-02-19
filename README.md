The Wazuh API allows you to interact with and manage your Wazuh installation programmatically. Here’s how you can work with it:

---

### **1. Enable and Access Wazuh API**
By default, the Wazuh API runs on port `55000`. To check if it’s running, use:

```bash
curl -k -X GET "https://<WAZUH_MANAGER_IP>:55000/"
```

If it’s not running, start it with:

```bash
systemctl start wazuh-api
```

---

### **2. Authenticate with the API**
You need an API user with proper roles. By default, the `wazuh` user exists. To authenticate, you’ll need the username and password:

```bash
curl -k -u wazuh:YourPassword -X GET "https://<WAZUH_MANAGER_IP>:55000/security/user/authenticate"
```

This returns a `JWT token`, which you’ll use in further requests.

Example response:
```json
{
  "data": {
    "token": "your-jwt-token"
  }
}
```

---

### **3. Making API Requests**
For all further API requests, pass the token in the `Authorization` header.

#### **List all active agents:**
```bash
curl -k -X GET "https://<WAZUH_MANAGER_IP>:55000/agents" -H "Authorization: Bearer your-jwt-token"
```

#### **Get agent information by ID:**
```bash
curl -k -X GET "https://<WAZUH_MANAGER_IP>:55000/agents/001" -H "Authorization: Bearer your-jwt-token"
```

#### **Restart an agent:**
```bash
curl -k -X PUT "https://<WAZUH_MANAGER_IP>:55000/agents/001/restart" -H "Authorization: Bearer your-jwt-token"
```

---

### **4. Custom API Queries**
If you want to query specific logs, for example, check security alerts:

```bash
curl -k -X GET "https://<WAZUH_MANAGER_IP>:55000/alerts?search=root" -H "Authorization: Bearer your-jwt-token"
```

To fetch logs related to your audit rule:

```bash
curl -k -X GET "https://<WAZUH_MANAGER_IP>:55000/alerts?rule_ids=100001" -H "Authorization: Bearer your-jwt-token"
```

*(Replace `100001` with your custom rule ID.)*

---

### **5. Automate API Calls with Python**
You can use Python to interact with the Wazuh API.

```python
import requests

WAZUH_API = "https://<WAZUH_MANAGER_IP>:55000"
USERNAME = "wazuh"
PASSWORD = "YourPassword"

# Authenticate
response = requests.get(f"{WAZUH_API}/security/user/authenticate", auth=(USERNAME, PASSWORD), verify=False)
token = response.json()['data']['token']

# Get list of agents
headers = {"Authorization": f"Bearer {token}"}
response = requests.get(f"{WAZUH_API}/agents", headers=headers, verify=False)

print(response.json())
```

---

### **6. Debugging API Issues**
- **Check API logs**: `/var/ossec/logs/api.log`
- **Verify service status**: `systemctl status wazuh-api`
- **Test authentication**: Ensure you’re using the correct username/password.

---

Would you like help automating specific Wazuh tasks using the API?
