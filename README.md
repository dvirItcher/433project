$disk = "\\.\PhysicalDrive0"
$stream = [System.IO.File]::Open($disk, 'Open', 'Read', 'None')
$buffer = New-Object byte[] 512
$stream.Read($buffer, 0, 512)
$stream.Close()
[System.Text.Encoding]::ASCII.GetString($buffer)









Sysmon (System Monitor), a part of the Microsoft Sysinternals suite, provides detailed logs of system activities for monitoring and security purposes. Logs 19, 20, and 21 are related to **WMI (Windows Management Instrumentation) activities**. Here's a breakdown of each:

---

### **Event ID 19: WmiEventFilter activity detected**
- **Purpose**: Logs when a new WMI event filter is registered.
- **What it Tracks**:  
  - WMI event filters are conditions that trigger WMI events.
  - Example: A filter might specify "log an event when a file is deleted."
- **Fields**:
  - **RuleName**: Custom name for the Sysmon rule that matched.
  - **EventNamespace**: The WMI namespace where the filter was registered.
  - **Name**: Name of the WMI filter.
  - **Query**: WQL (WMI Query Language) used to define the filter.

---

### **Event ID 20: WmiEventConsumer activity detected**
- **Purpose**: Logs when a new WMI event consumer is registered.
- **What it Tracks**:
  - Consumers act on WMI events, such as executing scripts or commands when an event occurs.
  - Example: Run a script when a file is modified.
- **Fields**:
  - **RuleName**: Custom name for the Sysmon rule that matched.
  - **ConsumerName**: Name of the WMI consumer.
  - **ConsumerType**: Type of the consumer (e.g., CommandLineEventConsumer, ActiveScriptEventConsumer).
  - **Destination**: Action performed by the consumer (e.g., running a script).

---

### **Event ID 21: WmiEventConsumerToFilter activity detected**
- **Purpose**: Logs when a filter-to-consumer binding is created.
- **What it Tracks**:
  - A filter-to-consumer binding links a WMI event filter with an event consumer.
  - Example: A filter detects when a file is deleted, and the consumer runs a script; this binding links them together.
- **Fields**:
  - **RuleName**: Custom name for the Sysmon rule that matched.
  - **Consumer**: Name of the consumer involved in the binding.
  - **Filter**: Name of the filter involved in the binding.

---

### **Key Differences**
1. **Event ID 19**: Focuses on the **creation of WMI event filters** (conditions).
2. **Event ID 20**: Focuses on the **creation of WMI event consumers** (actions).
3. **Event ID 21**: Tracks **bindings between filters and consumers** (links conditions to actions).

---

### **Why This Matters**
- **Security Implications**:
  - Attackers often abuse WMI to maintain persistence or execute malicious activities (e.g., living-off-the-land attacks).
  - These events help detect suspicious WMI-based behavior.
- **Example Use Case**:
  - Event ID 19 shows a filter querying for file deletions.
  - Event ID 20 logs a consumer that runs a script.
  - Event ID 21 shows that the filter and consumer are linked, indicating potential malicious automation.

Let me know if you'd like examples or help interpreting specific logs!
