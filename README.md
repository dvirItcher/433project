To search for all **CLI logs that contain an IP address** in Elasticsearch, you can use a query that matches logs from command-line activity and filters for valid IP patterns. Here's how:

---

### **Query Explanation**
1. **Command-Line Activity Logs**:
   - Look for logs where the `process.name`, `event_data.CommandLine`, or similar fields contain command-line activity.
   - Common fields include `message`, `process.command_line`, or `process.args`.

2. **IP Address Detection**:
   - Use a regular expression to match IP address patterns (`\b(?:\d{1,3}\.){3}\d{1,3}\b`).

---

### **Elasticsearch Query**
#### CLI Logs with IP Address Regex Match
```json
{
  "query": {
    "bool": {
      "must": [
        {
          "exists": {
            "field": "process.command_line"
          }
        },
        {
          "regexp": {
            "process.command_line": {
              "value": "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b"
            }
          }
        }
      ]
    }
  }
}
```

---

### **Kibana Query**
In Kibana, you can use the following:
```
process.command_line: /\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b/
```

---

### **Adjust for Your Log Fields**
If your CLI logs are stored in a different field (e.g., `message` or `event_data.CommandLine`), replace `process.command_line` with the appropriate field:
- For logs in `message`:
  ```json
  {
    "query": {
      "bool": {
        "must": [
          {
            "exists": {
              "field": "message"
            }
          },
          {
            "regexp": {
              "message": {
                "value": "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b"
              }
            }
          }
        ]
      }
    }
  }
  ```

---

### **Include Specific Hosts or Processes (Optional)**
To narrow it down to specific hosts or CLI tools:
```json
{
  "query": {
    "bool": {
      "must": [
        {
          "exists": {
            "field": "process.command_line"
          }
        },
        {
          "regexp": {
            "process.command_line": {
              "value": "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b"
            }
          }
        },
        {
          "term": {
            "host.name": "your-hostname"
          }
        }
      ]
    }
  }
}
```

---

### **IP Pattern Note**
This query matches IPv4 addresses. If you need to include IPv6, adjust the regex:
```
(?:\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b|\\b[a-fA-F0-9:]+\\b)
```

Let me know if you need further assistance tailoring this to your logs!
