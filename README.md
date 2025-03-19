If you have access to the Trellix ePO (formerly McAfee ePO) server but have forgotten the admin password, you can reset it or create a new user through the following methods:

### **Method 1: Reset Admin Password Using `cmdagent` Command**
1. **Log into the ePO server** directly using an account with administrative access.
2. Open a **Command Prompt** as an administrator.
3. Navigate to the ePO installation directory, typically:
   ```bash
   C:\Program Files (x86)\McAfee\ePolicy Orchestrator\Server\bin
   ```
4. Run the following command to reset the admin password to "admin":
   ```bash
   pwreset.cmd
   ```
   - This will reset the admin password to **"admin"**.

### **Method 2: Create a New User in SQL Database**
If the password reset script doesn’t work, you can create a new user directly through the ePO database:

1. Open **SQL Server Management Studio** and connect to the database server hosting the ePO database.
2. Find the ePO database (usually named **"ePO_<ServerName>"**).
3. Execute the following SQL query to create a new user with admin rights:
   ```sql
   INSERT INTO OrionUsers (UserName, UserPassword, LastName, FirstName, Email, Description, AuthType, Disabled, PasswordNeverExpires)
   VALUES ('newadmin', HASHBYTES('SHA1', 'password'), 'Admin', 'New', 'admin@example.com', 'New admin account', 1, 0, 1);

   INSERT INTO OrionUserRoles (UserID, RoleID)
   SELECT (SELECT UserID FROM OrionUsers WHERE UserName = 'newadmin'), RoleID FROM OrionRoles WHERE RoleName = 'Administrator';
   ```
   - Replace `'newadmin'` and `'password'` with your preferred username and password.
   - The `AuthType` value of `1` indicates a local user (not LDAP).
4. Restart the ePO services:
   - Open **Services** (`services.msc`) and restart:
     - `McAfee ePolicy Orchestrator Server`
     - `McAfee ePolicy Orchestrator Event Parser`

### **Method 3: Reset Admin Password via SQL**
You can also reset the existing admin password directly through SQL:

1. Open **SQL Server Management Studio**.
2. Connect to the ePO database.
3. Run the following query to reset the admin password to **"admin"**:
   ```sql
   UPDATE OrionUsers
   SET UserPassword = HASHBYTES('SHA1', 'admin')
   WHERE UserName = 'admin';
   ```
4. Restart the ePO services.

Let me know which method you want to try!
