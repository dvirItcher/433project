You can accomplish these tasks using the Wazuh API. Below are the API calls for each action:

---

### **1. Change a User's Password**
#### **Endpoint:**  
`PUT /security/users/{username}/actions/change_password`

#### **Example Request (Using `curl`):**
```bash
curl -X PUT "https://<WAZUH_MANAGER>:55000/security/users/<USERNAME>/actions/change_password" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
        "new_password": "<NEW_PASSWORD>"
      }' \
  -k
```

---

### **2. Remove Agents from a Group**
#### **Endpoint:**  
`DELETE /groups/{group_id}/agents`

#### **Example Request (Using `curl`):**
```bash
curl -X DELETE "https://<WAZUH_MANAGER>:55000/groups/<GROUP_ID>/agents" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
        "agents": [<AGENT_ID_1>, <AGENT_ID_2>]
      }' \
  -k
```

---

### **3. Add Agents to a Group**
#### **Endpoint:**  
`PUT /groups/{group_id}/configuration`

#### **Example Request (Using `curl`):**
```bash
curl -X PUT "https://<WAZUH_MANAGER>:55000/groups/<GROUP_ID>/configuration" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
        "agents": [<AGENT_ID_1>, <AGENT_ID_2>]
      }' \
  -k
```

---

### **Notes:**
- Replace `<WAZUH_MANAGER>` with your Wazuh manager's IP or hostname.
- Replace `<TOKEN>` with your Wazuh API token.
- Replace placeholders like `<USERNAME>`, `<NEW_PASSWORD>`, `<GROUP_ID>`, and `<AGENT_ID_X>` with actual values.
- The `-k` flag is used to ignore SSL certificate validation (use only if necessary).

Let me know if you need further clarification!
