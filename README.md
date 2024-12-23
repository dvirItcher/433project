Good morning! Yes, I can help you search for RDP-related logs in Elasticsearch. Here’s how you can structure your query, depending on the data fields and logging format you're using. 

### 1. **Filter by Event ID (Windows Logs)**
If you’re ingesting Windows Event Logs (via tools like Winlogbeat), RDP connections are typically associated with the following Event IDs:
- **Event ID 4624**: Successful logon (look for RDP logons: Logon Type 10).
- **Event ID 4648**: A logon was attempted with explicit credentials.
- **Event ID 1024**: RDP session disconnected.

To query for these events:
```json
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "winlog.event_id": 4624
          }
        },
        {
          "term": {
            "logon.type": 10
          }
        }
      ]
    }
  }
}
```

### 2. **Search for "mstsc" or "rdp" in Logs**
If you are unsure about specific fields or want to search for any occurrence of terms like `mstsc` or `rdp` in log messages:
```json
{
  "query": {
    "multi_match": {
      "query": "mstsc rdp",
      "fields": ["message", "*"],
      "type": "phrase"
    }
  }
}
```

### 3. **Filter by Source IP or Target Host**
If you're tracking RDP connection attempts:
```json
{
  "query": {
    "bool": {
      "filter": [
        {
          "match_phrase": {
            "source.ip": "192.168.x.x"
          }
        },
        {
          "match_phrase": {
            "process.name": "mstsc.exe"
          }
        }
      ]
    }
  }
}
```

### 4. **Kibana Query Example**
In Kibana's search bar, you can use a Lucene query like:
```
message: "mstsc" OR message: "rdp"
```




To search for **unsuccessful RDP connection attempts**, you can focus on specific Windows Event IDs related to failed logins. Below are the relevant IDs and how to query them in Elasticsearch.

---

### **Relevant Event IDs for Failed RDP Connections**
1. **Event ID 4625**: Failed login attempt.  
   Look for `Logon Type = 10` (Remote Interactive Logon via RDP).
   
2. **Event ID 1026**: Disconnection from an RDP session due to authentication failure.

---

### **Elasticsearch Query Example**
Here’s a query to find failed RDP logins:

#### Query for `4625` with `Logon Type = 10`
```json
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "winlog.event_id": 4625
          }
        },
        {
          "term": {
            "logon.type": 10
          }
        }
      ]
    }
  }
}
```

---

#### Query for Disconnections (`Event ID 1026`)
```json
{
  "query": {
    "term": {
      "winlog.event_id": 1026
    }
  }
}
```

---

### **Kibana Query for Unsuccessful Attempts**
In the Kibana search bar, you can use:
```
winlog.event_id: 4625 AND log

### 5. **Check Beats Configurations**
If you're using tools like Filebeat or Winlogbeat to collect logs:
- Ensure your `filebeat.yml` or `winlogbeat.yml` is configured to capture relevant event types or keywords related to RDP activity.
- Verify which fields are being indexed in Elasticsearch to refine your queries.

Let me know if you'd like help customizing these queries further!
