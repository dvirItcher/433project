To determine if a computer is vulnerable to PrintNightmare (CVE-2021-34527), you can check the system's patch status, configuration, and exposure of the Print Spooler service. Here's a detailed explanation:

---

### **If You Have Access to the Computer**

#### 1. **Check for Security Updates**
   - Verify if the system has installed the patch for CVE-2021-34527.
   - Use PowerShell to list installed updates:
     ```powershell
     Get-HotFix | Where-Object { $_.Description -like '*Security*' -and $_.HotFixID -like '*KB5004945*' }
     ```
   - The relevant patches include KB5004945, KB5005010, and others depending on the Windows version. Cross-reference with Microsoft's security advisories.

#### 2. **Inspect Print Spooler Configuration**
   - Check if the Print Spooler service is running:
     ```powershell
     Get-Service -Name Spooler
     ```
   - Disable the service if not needed:
     ```powershell
     Stop-Service -Name Spooler -Force
     Set-Service -Name Spooler -StartupType Disabled
     ```

#### 3. **Check Group Policy Settings**
   - Ensure that **Point and Print Restrictions** are configured:
     - Run `gpedit.msc` and navigate to:
       `Computer Configuration > Administrative Templates > Printers > Point and Print Restrictions`.
     - Verify settings like **"When installing drivers for a new connection"** to ensure elevation prompts are required.

#### 4. **Run Vulnerability Scanners**
   - Use tools like **Nessus**, **Qualys**, or **Microsoft Baseline Security Analyzer** to scan for known vulnerabilities, including PrintNightmare.

---

### **If You Don’t Have Access to the Computer**

#### 1. **Network Scanning**
   - **Identify Open RPC or SMB Ports**:
     - PrintNightmare relies on network services like RPC (Port 135) and SMB (Ports 445, 139).
     - Use a tool like **Nmap**:
       ```bash
       nmap -p 135,445,139 --script smb-enum-shares,smb-enum-users <IP>
       ```
     - Look for exposed SMB shares related to printing (`\\<hostname>\print$`).

   - **Detect Print Spooler Exposure**:
     - Use a script or tool to detect if the spooler service is exposed:
       ```bash
       rpcdump.py <IP>
       ```
     - Look for RPC bindings related to the Print Spooler.

#### 2. **Exploit Testing (With Permission)**
   - If you have legal permission (e.g., internal pentesting), you can use tools like:
     - **Metasploit Framework**:
       - Search for the module:
         ```bash
         search printnightmare
         ```
       - Configure and test the exploit against the target.
     - **Impacket**:
       - Use tools like `AddPrinterDriverEx()` to test.

#### 3. **Check Patch Management Reports**
   - If you manage the network but don’t have direct access to the system, check centralized patch management systems (e.g., WSUS, SCCM, or other tools) for unpatched systems.

#### 4. **Public Information**
   - Check if the system’s information is exposed online (e.g., Shodan or Censys.io searches):
     - Query for exposed SMB shares or systems running vulnerable Print Spooler services:
       ```plaintext
       port:445 service:smb
       ```

---

### **Proactive Steps**

If you're concerned about the security of external systems:

1. **Request Information**:
   - Ask the administrator or organization to confirm patch status or configurations.

2. **Educate the Owners**:
   - Share awareness about PrintNightmare and its risks, urging them to check for vulnerabilities.

3. **Avoid Unauthorized Scanning**:
   - Only scan systems you own or have explicit permission to test. Unauthorized testing can violate laws and agreements.

---

Let me know if you need assistance with specific tools or commands!
