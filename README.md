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

### 5. **Check Beats Configurations**
If you're using tools like Filebeat or Winlogbeat to collect logs:
- Ensure your `filebeat.yml` or `winlogbeat.yml` is configured to capture relevant event types or keywords related to RDP activity.
- Verify which fields are being indexed in Elasticsearch to refine your queries.

Let me know if you'd like help customizing these queries further!
