Here is a more comprehensive list of **common Sysmon registry values** and their purposes, which are found under the **Sysmon service registry key**:

### **Registry Path**
```
HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Sysmon
```

---

### **Subkeys and Values**
1. **`Config` Subkey**
   This is the most critical subkey that contains configuration details for Sysmon.

   **Values:**
   - **`Rules`** (Binary)  
     Stores the active Sysmon configuration in binary format. This is the content of the XML configuration file uploaded to Sysmon.  
     - Updated whenever you reload the configuration using `sysmon.exe -c`.
     - Cannot be directly edited without causing issues—always use the Sysmon CLI.

   - **`SchemaVersion`** (DWORD)  
     Specifies the Sysmon configuration schema version.  
     - Example: `13` for Sysmon schema version 13.

   - **`ConfigurationHash`** (Binary)  
     A hash of the currently loaded configuration file to detect changes or tampering.

---

2. **Root Values**
   These values directly under the `Sysmon` key determine service and operational settings.

   **Common Values:**
   - **`DisplayName`** (REG_SZ)  
     The display name of the Sysmon service.  
     - Default: "Sysmon - System Monitor".

   - **`ErrorControl`** (DWORD)  
     Determines the error handling if the Sysmon service fails to start:  
     - `0x0`: Ignore.  
     - `0x1`: Display a warning.  
     - `0x3`: Fail critical system startup.

   - **`ImagePath`** (REG_EXPAND_SZ)  
     The file path to the Sysmon executable.  
     - Default: `%SystemRoot%\Sysmon64.exe`.

   - **`ObjectName`** (REG_SZ)  
     The name of the account Sysmon runs under.  
     - Default: `LocalSystem`.

   - **`Start`** (DWORD)  
     The startup type of the Sysmon service:  
     - `0x2`: Automatic start.  
     - `0x3`: Manual start.

   - **`Type`** (DWORD)  
     The service type for Sysmon. Default: `0x10` (indicates a file system driver).

   - **`DriverState`** (DWORD)  
     Represents the current state of the Sysmon driver:  
     - `0`: Stopped.  
     - `1`: Running.

---

3. **Event Log Configuration**
   These settings configure the Sysmon logs in the Windows Event Viewer.

   **Registry Path:**
   ```
   HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\WINEVT\Channels\Microsoft-Windows-Sysmon/Operational
   ```

   **Values:**
   - **`Enabled`** (DWORD)  
     Specifies if the Sysmon log channel is enabled:  
     - `1`: Enabled.  
     - `0`: Disabled.

   - **`MaxSize`** (DWORD)  
     The maximum size (in bytes) of the event log file.

   - **`Retention`** (DWORD)  
     Defines the retention policy:  
     - `0`: Overwrite events as needed.  
     - `1`: Retain events until explicitly cleared.

---

4. **Performance and Debugging**
   Sysmon also stores performance-related values under the `Sysmon` registry key.

   **Common Values:**
   - **`DebugFlags`** (DWORD)  
     Used for debugging Sysmon (usually disabled).  
     - Default: `0x0`.

   - **`StateFlags`** (DWORD)  
     Controls state-specific features of Sysmon.  
     - Example: `0x1` for active state.

---

### Notes:
- Modifying the values under the `Sysmon` registry key manually is risky. Always use the official Sysmon CLI (`sysmon.exe`) to ensure consistent and valid configurations.
- Regularly monitor the `Config` subkey to detect unauthorized changes (e.g., configuration tampering).
- Use tools like `Regedit`, `reg query`, or PowerShell to inspect the registry settings.

Let me know if you’d like assistance extracting or interpreting any specific value!
