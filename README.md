If querying the **MFT (Master File Table)** didn’t return results, we can try other methods to find the lock screen image without executing a command. Here are a few alternative approaches:

---

### **1. Use Velociraptor's Raw NTFS Query**
Instead of the **Windows.NTFS.MFT** artifact, try using **raw NTFS file enumeration** to list all files in the expected folders.

#### **VQL Query to List Lock Screen Images (Without Running a Command)**
```vql
SELECT FullPath, 
       Size, 
       ModificationTime 
FROM artifact.Windows.NTFS.EnumerateFiles(globs=[
  'C:\\Windows\\Web\\Screen\\*',
  'C:\\Users\\*\\AppData\\Local\\Packages\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets\\*'
])
ORDER BY ModificationTime DESC
LIMIT 5
```

##### **Why This Might Work**
- This directly **lists files** from disk without relying on Windows APIs.
- If the images are not appearing in the MFT query, they may still be accessible via direct **file enumeration**.

---

### **2. Retrieve Previous File Versions (If Changed or Deleted)**
If the **server replaced** the lock screen image, you might still find it in **Windows Shadow Copies** or **journal logs**.

#### **Check NTFS Journal for Recent Changes**
```vql
SELECT FullPath, 
       Action, 
       ChangeTime 
FROM artifact.Windows.NTFS.UsnJournal()
WHERE FullPath LIKE 'C:\\Windows\\Web\\Screen\\%'
   OR FullPath LIKE 'C:\\Users\\%\\AppData\\Local\\Packages\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets\\%'
ORDER BY ChangeTime DESC
LIMIT 10
```

##### **This Helps If:**
- The server **replaced or deleted** the image.
- The file’s history is still in the **NTFS Change Journal**.

---

### **3. Check Windows Registry for Lock Screen Image**
If the image has been **set manually**, the path might be stored in the **Windows Registry**.

#### **VQL Query to Get Lock Screen Path from Registry**
```vql
SELECT ValueName, ValueData 
FROM artifact.Windows.Registry.ListValues(
  keys=['HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\PersonalizationCSP']
)
```

##### **Why This Might Work**
- If the server applied a **custom lock screen**, the image’s path might be **stored in the registry**.
- This lets you **locate the exact image file**.

---

### **4. Recover the Image from Memory or Pagefile**
If the **lock screen was recently displayed**, it may still exist in memory.

#### **Extract Memory Artifacts (If Enabled)**
```vql
SELECT * 
FROM artifact.Windows.Memory.Acquisition()
```
You can then **search the memory dump** for **image file signatures**.

---

### **Next Steps**
- Did **any of these methods return results**?
- Do you suspect the image was **deleted or overwritten**?
- Should we try a **forensic approach**, like **Shadow Copies** or **$Recycle.Bin** recovery?

Let me know what worked (or didn’t), and I’ll refine the approach further!
