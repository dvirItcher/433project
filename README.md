Since you're using **Trellix ePO** (formerly McAfee ePO), the steps are similar but adapted for Trellix:  

### **Fixing Database Connection Issue After Password Change**  

#### **Method 1: Use Trellix ePO Configuration Tool (If Available)**  
1. **Log into the ePO Server** with admin privileges.  
2. Navigate to:  
   ```
   C:\Program Files (x86)\Trellix\ePolicy Orchestrator\DB\
   ```
   - If `Config.exe` exists, **run it as Administrator**.  
   - Update the **database credentials** and save.  
   - Restart **Trellix ePO services**.  

#### **Method 2: Modify the Database Configuration File**  
1. Navigate to:  
   ```
   C:\Program Files (x86)\Trellix\ePolicy Orchestrator\Server\conf\orion\
   ```
2. Open **db.properties** in Notepad++.  
3. Look for these lines:  
   ```
   db.user=DOMAIN\YourUser
   db.password=ENCRYPTED_PASSWORD
   ```
   - Update the **username** if needed.  
   - If the password is encrypted, use the next method to reset it.  

#### **Method 3: Reset SQL Password via ePO Web Console (If Accessible)**  
1. Log into the **Trellix ePO Web Console**.  
2. Go to **Menu > Configuration > Server Settings**.  
3. Find **Database Settings** and update the **username and password**.  
4. Save changes and restart the **ePO services**.  

#### **Method 4: Manually Update SQL Credentials in SQL Server**  
If the ePO server uses **SQL authentication**:  
1. Open **SQL Server Management Studio (SSMS)** on the DB server.  
2. Go to **Security > Logins**.  
3. Right-click the **Trellix ePO database user** → **Properties**.  
4. Update the **password** and apply changes.  
5. Restart **Trellix ePO services**.  

#### **Final Step: Restart Trellix ePO Services**  
1. Open **Services.msc** on the ePO server.  
2. Restart these services:  
   - **Trellix ePolicy Orchestrator Server**  
   - **Trellix ePolicy Orchestrator Application Server**  

After these steps, try logging into the **ePO Web Console** to verify if the issue is resolved. Let me know if you need further troubleshooting!
