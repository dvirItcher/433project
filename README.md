If the **server is changing the lock screen image**, but you don’t want to run a direct command, you can still retrieve the image **passively** using Velociraptor by monitoring file changes.  

### **Approach: Use Velociraptor's NTFS Analysis to Find New Lock Screen Images**  
Instead of running a command, you can query Velociraptor’s **NTFS MFT (Master File Table)** records to detect **new or modified images** without executing anything on the endpoint.

---

### **Step 1: Search for Recently Modified Lock Screen Images**  
Since Windows stores lock screen images in known locations, you can retrieve **recently modified files**.

#### **VQL Query to Identify New Lock Screen Images**
```vql
SELECT FullPath, 
       Size, 
       ModificationTime 
FROM artifact.Windows.NTFS.MFT
WHERE FullPath LIKE 'C:\\Windows\\Web\\Screen\\%'
   OR FullPath LIKE 'C:\\Users\\%\\AppData\\Local\\Packages\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets\\%'
ORDER BY ModificationTime DESC
LIMIT 5
```
##### **What This Does:**
- Scans the **MFT (Master File Table)** to find **new or modified files** in the lock screen image folders.
- Doesn't execute any commands on the endpoint.
- Returns the **latest modified images**.

---

### **Step 2: Download the Image (Without Running a Command)**
1. **Run the query** in Velociraptor.
2. Find the **most recently modified file**.
3. Use Velociraptor’s **File Finder** to retrieve it:
   - **Navigate to "Collected Data" → "File Finder"**.
   - Enter the **file path** found in the query.
   - Click **"Download File"**.

---

### **Alternative: Monitor for Future Lock Screen Changes (Passive Detection)**
If you want to **automatically detect when the server updates the lock screen**, set up a **hunt** to monitor file modifications.

1. **Go to "Hunts" → Create New Hunt**.
2. Use the **Windows.NTFS.MFT** artifact.
3. Filter results for:
   ```vql
   WHERE FullPath LIKE 'C:\\Windows\\Web\\Screen\\%' 
      OR FullPath LIKE 'C:\\Users\\%\\AppData\\Local\\Packages\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets\\%'
   ```
4. Set it to **run periodically** (e.g., every hour).
5. Whenever a new image is detected, it will appear in Velociraptor.

---

### **Next Steps**
- Do you want to **automate image collection** whenever it changes?  
- Would you like to **extract metadata** from the image (e.g., timestamps, creation details)?  

Let me know how you’d like to refine this further!
