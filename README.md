Detecting a **Kerberoasting attack** involves analyzing authentication logs for specific patterns indicative of suspicious behavior. Kerberoasting is an attack targeting service accounts in Active Directory by requesting service tickets (TGS) and extracting their encrypted passwords for offline cracking.

### Steps to Create Detection Logic

1. **Log Sources**
   - Active Directory (AD) Security Event Logs
   - SIEM (Security Information and Event Management) system
   - Network monitoring tools

2. **Key Indicators to Look For**
   - **Event ID 4769** (An account was granted a Kerberos service ticket) in Windows Security Logs:
     - Look for unusual TGS requests, especially for service accounts.
   - Large number of service ticket requests in a short time frame.
   - Requests from non-standard or unauthorized tools.
   - Requests for high-privilege accounts (e.g., `krbtgt` or administrative accounts).
   - Suspicious source systems or IPs (e.g., non-domain joined systems).
   - Anomalies in the Service Name or SPN (Service Principal Name) being requested.

3. **Detection Logic**
   Use the following logic to create queries or rules in your SIEM:

#### Pseudocode for Detection Logic
```sql
SELECT 
    SourceIPAddress, 
    TargetAccountName, 
    ServiceName, 
    EventTime 
FROM 
    SecurityLogs 
WHERE 
    EventID = 4769
    AND EncryptionType IN ('RC4-HMAC', 'AES256-CTS-HMAC-SHA1-96') -- Common encryption types used
    AND (
        -- High frequency of requests from a single source
        COUNT(*) OVER (PARTITION BY SourceIPAddress, TargetAccountName, ServiceName, DATE_TRUNC('hour', EventTime)) > Threshold
        OR
        -- High-privilege accounts or sensitive service names
        TargetAccountName IN ('krbtgt', 'Administrator', 'DomainAdmin')
        OR
        ServiceName LIKE '%sensitive%'
    )
ORDER BY 
    EventTime DESC;
```

4. **Threshold and Baselines**
   - Define thresholds based on normal network behavior.
   - For example, more than 5 TGS requests per minute from the same source or targeting the same account could trigger an alert.
   - Compare activity against historical baselines.

5. **Advanced Correlation**
   - Correlate with Event ID **4624** (Logon event) to see if the source IP has logged into multiple accounts unusually.
   - Cross-reference with **network logs** for signs of unusual traffic (e.g., tools like `Rubeus` or `Impacket` often generate unique patterns).

6. **Response**
   - Trigger an alert in your SIEM or SOAR system.
   - Perform automated actions, like isolating the machine or blocking the source IP.

### Example in Splunk
```spl
index=security_logs
sourcetype=windows
EventCode=4769
| stats count by Source_Workstation, Target_Account_Name, Service_Name, Encryption_Type
| where count > 5 AND Encryption_Type IN ("0x17", "0x12")  # RC4 or AES encryption
| table Source_Workstation, Target_Account_Name, Service_Name, count
```

### Example in Elastic (EQL)
```eql
sequence by host.id
  [authentication where event.action == "kerberos_request" and event.outcome == "success"]
  [authentication where target.service.name in ("krbtgt", "Administrator")]
```

By implementing this logic, you can effectively detect and respond to potential Kerberoasting attacks.
