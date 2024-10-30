# יש להחליף את שם הקבוצה הרצויה כאן
$groupName = "YourGroupName"

# מקבל את ה-Distinguished Name של הקבוצה
$group = Get-ADGroup -Identity $groupName
$groupDN = $group.DistinguishedName

# מקבל את רשימת ה-ACL (Access Control List) על הקבוצה
$acl = Get-ACL -Path "AD:\$groupDN"

# מציג משתמשים וקבוצות עם הרשאת Full Control
$acl.Access | Where-Object { $_.ActiveDirectoryRights -eq 'GenericAll' } | ForEach-Object {
    [PSCustomObject]@{
        Identity   = $_.IdentityReference
        Rights     = $_.ActiveDirectoryRights
        AccessType = $_.AccessControlType
    }
}





from scapy.all import *
import time

# Replace with the destination MAC address
dest_mac = "aa:bb:cc:dd:ee:ff"
# Replace with the source MAC address
source_mac = get_if_hwaddr(conf.iface)  # gets the MAC address of your interface

interface = "eth0"  # Replace with your network interface

# Send a custom message
def send_message(message):
    frame = Ether(src=source_mac, dst=dest_mac) / Raw(load=message)
    sendp(frame, iface=interface)

# Wait for the response
def wait_for_response():
    def handle_packet(packet):
        if packet[Ether].src == dest_mac and packet.haslayer(Raw):
            print(f"Received: {packet[Raw].load.decode()}")
            return True
        return False

    sniff(iface=interface, prn=handle_packet, timeout=10, stop_filter=handle_packet)

if __name__ == "__main__":
    while True:
        message = input("Enter your message to send: ")
        print(f"Sending: {message}")
        send_message(message.encode())  # Send the message
        print("Waiting for a response...")
        wait_for_response()
        time.sleep(2)



from scapy.all import *
import time

# Replace with the source MAC address
ping_mac = "aa:bb:cc:dd:ee:ff"
# Replace with the destination MAC address
pong_mac = get_if_hwaddr(conf.iface)  # gets the MAC address of your interface

interface = "eth0"  # Replace with your network interface

# Send a response back
def send_response(response):
    frame = Ether(src=pong_mac, dst=ping_mac) / Raw(load=response)
    sendp(frame, iface=interface)

# Wait for an incoming message
def wait_for_message():
    def handle_packet(packet):
        if packet[Ether].src == ping_mac and packet.haslayer(Raw):
            message = packet[Raw].load.decode()
            print(f"Received: {message}")
            return message
        return None

    packet = sniff(iface=interface, prn=handle_packet, timeout=10, stop_filter=handle_packet)
    return packet[0].load.decode() if packet else None

if __name__ == "__main__":
    while True:
        print("Waiting for a message...")
        message = wait_for_message()
        if message:
            print(f"Message received: {message}")
            response = input("Enter your response: ")
            send_response(response.encode())  # Send a response back
        time.sleep(2)
        


        

433 project
I tried to do the project in JAVA,
that's why I couldn't really finish,
I hope what I've done so far is good,
I wasn't able to upload all the MODELS of the server,
to connect to mongodb u need to change the connection string in mongoDBconnection.java file
#the next rext is not connected to the project

Certainly! Here's a more detailed breakdown of the UULoader malware and its operation:

### 1. **What is UULoader?**
UULoader is a newly discovered malware delivery system. It's not a standalone piece of malware but rather a loader, meaning its primary function is to deliver and execute other malicious software (referred to as "payloads") on a victim's machine. It's specifically designed to target users in East Asia, particularly those who speak Korean and Chinese.

### 2. **Distribution Method:**
UULoader is distributed through malicious installers that pretend to be legitimate applications. These installers might appear to be regular software that users in the targeted regions would typically download and install. However, these installers have been tampered with to include UULoader.

### 3. **Structure and Components:**
The core components of UULoader are packed into a Microsoft Cabinet archive file (.cab). A .cab file is a compressed archive format used by Microsoft to distribute software.

Inside the .cab file, there are two primary components:
- **An executable file (.exe):** This is a legitimate application or a program that the user expects to run.
- **A dynamic link library file (.dll):** This is the malicious part of the package. DLL files are used to contain code and data that can be used by multiple programs at the same time.

### 4. **Execution Process (DLL Side-Loading):**
UULoader uses a technique known as **DLL side-loading** to execute the malware. Here's how it works:

- **Legitimate Executable:** When the user runs the executable file from the installer, it operates as expected. However, this executable is designed or modified to load a specific DLL file from its directory.

- **Malicious DLL:** The DLL file in the package is not legitimate but malicious. The legitimate executable inadvertently loads this malicious DLL instead of a safe one, executing the code contained within it.

- **Final Payload Execution:** Once the DLL is loaded, it executes further steps to deliver the final malware payload. In the case of UULoader, the final stage is an obfuscated file named **XamlHost.sys**. This file is executed, and it is at this point that the actual malware (like Gh0st RAT or Mimikatz) is loaded onto the system.

### 5. **Delivered Payloads:**
- **Gh0st RAT:** This is a Remote Access Trojan (RAT) that allows attackers to take control of the infected computer, enabling them to spy on the victim, steal data, and perform various malicious activities remotely.

- **Mimikatz:** This tool is widely used by attackers to extract passwords, hashes, and other credentials from the system, making it easier for them to move laterally across a network.

### 6. **Evidence of Origin:**
The investigation by the Cyberint Research Team found Chinese language strings in the Program Database (PDB) files embedded within the DLL. PDB files are used for debugging and typically contain metadata about the software's development, including comments and variable names left by developers. The presence of Chinese text in these files strongly suggests that the creators of UULoader are Chinese-speaking.

### 7. **Implications:**
The use of UULoader is a sophisticated method of delivering malware. By using legitimate software to hide its activities, UULoader can evade detection by traditional security systems that might not recognize it as a threat. This makes it particularly dangerous, as users might install the software believing it to be safe, only to have their systems compromised.

##################################

To understand how the flaw in the Windows Ancillary Function Driver (AFD.sys) works, let's break down the technical aspects of privilege escalation vulnerabilities, particularly in the context of kernel drivers like AFD.sys.

### 1. **Kernel Mode vs. User Mode:**
- **User Mode**: Regular applications (like web browsers or word processors) run in user mode, where they have limited privileges and can't directly interact with critical system resources.
- **Kernel Mode**: The Windows kernel, including drivers like AFD.sys, runs in kernel mode, where it has full access to the hardware and system memory. Any code running in kernel mode has the highest level of privilege.

### 2. **AFD.sys and Its Role:**
AFD.sys is part of the Windows networking stack and interacts with the Windows Sockets (WinSock) API to manage network connections. Since it operates at a low level, it runs in kernel mode and interacts closely with system hardware and memory.

### 3. **How Privilege Escalation Bugs Typically Work:**
A privilege escalation vulnerability usually involves a flaw in how a driver handles data or requests from user-mode applications. There are several common ways such bugs can manifest:
- **Improper Input Validation**: The driver might not correctly validate input from a user-mode application. If a user-mode process can send malformed or specially crafted data to the driver, it might cause the driver to perform unintended operations.
- **Buffer Overflows**: If the driver fails to check the size of incoming data, it might write beyond the allocated memory space, corrupting memory and potentially allowing the attacker to execute arbitrary code in kernel mode.
- **Race Conditions**: If the driver handles multiple requests simultaneously and doesn't properly synchronize access to shared resources, an attacker might exploit the timing of these operations to gain control over the system.

### 4. **The Flaw in AFD.sys (CVE-2024-38193):**
While the exact technical details of this specific flaw haven't been fully disclosed (as it’s common with vulnerabilities still being actively patched and studied), we can infer the following based on the general nature of such vulnerabilities and the information provided:

- **Privilege Escalation through AFD.sys**: The flaw in AFD.sys likely involves improper handling of certain network-related requests. This could mean that when a user-mode application sends a specially crafted request or data packet to AFD.sys, the driver does not correctly validate this input. As a result, it may perform an unintended operation—such as accessing or modifying memory it shouldn’t.

- **Gaining SYSTEM Privileges**: By exploiting this flaw, an attacker could trick AFD.sys into executing code or operations on their behalf. Since AFD.sys runs in kernel mode, this could allow the attacker’s code to run with SYSTEM privileges, the highest level of privilege in Windows. This would let the attacker do anything on the system, from installing malware to disabling security features.

### 5. **Real-World Exploitation by Lazarus Group:**
- **Exploitation Vector**: The Lazarus Group likely crafted a payload that could be delivered to a target system, possibly via a malicious file or network packet. Once this payload was processed by AFD.sys, the vulnerability would be triggered, granting them SYSTEM-level access without needing to bypass other security measures directly.
  
- **Post-Exploitation**: After gaining SYSTEM privileges, the attacker could move laterally across the network, exfiltrate sensitive data, or establish persistent access to the compromised systems.

### 6. **Patching the Flaw:**
To fix this issue, Microsoft would have updated AFD.sys to properly validate all inputs, handle memory securely, and ensure no race conditions could be exploited. This reduces the risk of unintended behavior that could be leveraged by an attacker.

### Conclusion:
In essence, the flaw in AFD.sys (CVE-2024-38193) is a classic example of a privilege escalation vulnerability within a critical system driver. By exploiting a weakness in how the driver handles network-related requests, an attacker could elevate their privileges to SYSTEM level, gaining complete control over the affected system. This flaw has been addressed by Microsoft through a security update, closing off this dangerous attack vector.

### 8. **Conclusion:**
UULoader represents a significant threat, particularly in East Asia, due to its ability to stealthily deliver powerful malware like Gh0st RAT and Mimikatz. The use of DLL side-loading, legitimate-looking installers, and obfuscation techniques makes it a formidable tool in the hands of cybercriminals. As always, users should be cautious when downloading and installing software, especially from unofficial or unfamiliar sources.
