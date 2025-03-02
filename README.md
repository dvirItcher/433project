To investigate a **desktop wallpaper change** using **Velociraptor**, you can analyze relevant artifacts that store wallpaper change events. Here’s a step-by-step approach:

---

### **1. Collecting Wallpaper Change Artifacts**
Windows stores wallpaper information in several locations:

#### **A. Registry Artifacts**
The registry contains paths to the current wallpaper and recent wallpapers:
- **Current wallpaper path:**
  - `HKEY_CURRENT_USER\Control Panel\Desktop\Wallpaper`
- **Recent wallpapers (Windows 10/11):**
  - `HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Wallpapers`

To collect these registry keys in Velociraptor:
- Use **Windows.Registry.HiveList** to check if the NTUSER.DAT hive is available.
- Query the registry with:
  ```sql
  SELECT * FROM artifacts.windows.registry.KeyValue
  WHERE key LIKE 'HKEY_CURRENT_USER\Control Panel\Desktop\Wallpaper'
  ```
  or
  ```sql
  SELECT * FROM artifacts.windows.registry.KeyValue
  WHERE key LIKE 'HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Wallpapers'
  ```

#### **B. Event Logs**
If a wallpaper change happened recently, event logs might contain clues:
- **User Preference Changes (Event ID 9010, 9027)**
  - Location: `Microsoft-Windows-Shell-Core/Operational`
- **Security Auditing Logs (Event ID 4657)**
  - If auditing is enabled on registry keys, this event logs changes.

To collect logs:
```sql
SELECT * FROM artifacts.windows.eventlogs.EventLog
WHERE Source = "Microsoft-Windows-Shell-Core/Operational"
AND (EventID = 9010 OR EventID = 9027)
```

#### **C. File System Analysis**
- The actual wallpaper file might exist at:
  - `C:\Users\%USERNAME%\AppData\Roaming\Microsoft\Windows\Themes\TranscodedWallpaper`
  - `C:\Windows\Web\Wallpaper\`
- Check timestamps (creation/modification) of these files:
  ```sql
  SELECT * FROM artifacts.windows.NTFS.MFT
  WHERE filename LIKE 'C:\Users\%\AppData\Roaming\Microsoft\Windows\Themes\TranscodedWallpaper'
  ```

#### **D. Prefetch & Jump Lists**
If a third-party app (e.g., browser or image viewer) changed the wallpaper, its prefetch file or jump list might show recent activity.

- Use **Windows.PrefetchFiles** to check:
  ```sql
  SELECT * FROM artifacts.windows.prefetch.PrefetchFiles
  WHERE filename LIKE 'rundll32.exe' OR filename LIKE 'ms-settings:personalization-background'
  ```
- Check **Windows.JumpLists**:
  ```sql
  SELECT * FROM artifacts.windows.JumpLists
  ```

---

### **2. Monitoring Future Wallpaper Changes**
If you suspect ongoing wallpaper modifications, you can set up **live monitoring** using Velociraptor’s event monitoring:
```sql
SELECT * FROM watchdog.FileChange
WHERE Path LIKE 'C:\Users\%\AppData\Roaming\Microsoft\Windows\Themes\%'
```
This will notify you when the wallpaper file is modified.

---

### **Conclusion**
By combining **registry analysis, event logs, file system analysis, and monitoring**, you can determine:
- **When** the wallpaper changed.
- **Who** changed it.
- **What** the new wallpaper is.

Would you like to automate this with a custom Velociraptor hunt?
