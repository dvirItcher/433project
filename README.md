If you can't use **Windows.Forensics.ChromeDownloads**, you can still find the downloaded PS1 script manually using other methods. Here's an alternative approach:  

---

### **1. Find the PS1 Script in Chrome’s Download History**  
Since you can’t use **Windows.Forensics.ChromeDownloads**, try these options:  

#### **Option 1: Check Chrome’s Local Download History File**
Chrome stores download history in an SQLite database:  
- Navigate to:  
  ```
  C:\Users\USERNAME\AppData\Local\Google\Chrome\User Data\Default\History
  ```
- Open the **"History"** file with an SQLite viewer and run:  
  ```sql
  SELECT target_path FROM downloads;
  ```
  This will show the file path of all downloaded files, including the PS1 script.  

#### **Option 2: Use Windows.USNJournal for Recently Created PS1 Files**  
- Collect **Windows.UsnJrnl** and filter for `.ps1` files.  
- This will list all recently created/modified scripts, including the one downloaded via Chrome.  

---

### **2. Analyze the PS1 Script to Find the Lock Screen Image Path**  
Once you find the PS1 file, open it and look for:  
- **File path assignments**, such as:  
  ```powershell
  $ImagePath = "C:\Windows\Web\Screen\new_lock_screen.jpg"
  ```
- **Registry modifications**, such as:  
  ```powershell
  Set-ItemProperty -Path "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\PersonalizationCSP" -Name "LockScreenImagePath" -Value "C:\Windows\Web\Screen\hacked.jpg"
  ```
  If you see a registry modification, retrieve the image path using **Windows.Registry.Search** (search for `LockScreenImagePath`).  

---

### **3. Retrieve the Modified Lock Screen Image**  
Once you know the image path, use **Windows.Forensics.FileFinder** to collect it.  

Common locations:  
- `C:\Windows\Web\Screen\`  
- `C:\Windows\Web\Wallpaper\`  
- `C:\Users\Public\Pictures\`  

If the file is missing, check:  
- **Windows.RecycleBin** (in case it was deleted).  
- **Windows.UsnJrnl** (to find old file references).  

---

### **Final Step: Check If the PS1 Script is Still Running**  
- Use **Windows.Sysmon.ProcessCreate** to see if PowerShell executed recently.  
- Check **Windows.EventLogs.Security** for PowerShell execution logs.  

---

Try these steps and let me know what you find!
