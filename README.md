To send a ping from one virtual machine (VM) running Windows 10 to another, follow these steps:

---

### 1. **Ensure Both VMs Are on the Same Network**
   - Check the network configuration of your virtual machines. Depending on the hypervisor (e.g., VirtualBox, VMware, Hyper-V), ensure both VMs are configured to be on the same virtual network.
     - **NAT Network**: Machines share the host’s IP, but may not be directly reachable.
     - **Bridged Network**: VMs are directly connected to the host’s network and can communicate with each other.
     - **Host-Only Network**: VMs can communicate only with each other and the host.

   **For VirtualBox**:
   - Go to the VM settings > `Network`.
   - Select `Adapter 1` > `Attached to: Bridged Adapter` or `Host-Only Adapter`.

   **For VMware**:
   - Go to VM settings > `Network Adapter`.
   - Choose `Bridged` or `Host-Only`.

---

### 2. **Find the IP Address of Each VM**
   - On each VM, open Command Prompt and run:
     ```cmd
     ipconfig
     ```
   - Note the **IPv4 Address** for each machine.

---

### 3. **Enable Ping Requests on Both VMs**
   By default, Windows 10 blocks ICMP (ping) requests via the firewall. You need to allow them:
   1. Open **Windows Defender Firewall with Advanced Security**.
   2. In the left pane, click **Inbound Rules**.
   3. In the right pane, click **New Rule**.
   4. Select **Custom** > Click **Next**.
   5. Select **ICMPv4** > Click **Next**.
   6. Choose when the rule applies (e.g., Domain, Private) > Click **Next**.
   7. Name the rule (e.g., Allow Ping) > Click **Finish**.

---

### 4. **Test Connectivity**
   - On one VM, open Command Prompt and use the `ping` command:
     ```cmd
     ping <IP address of the other VM>
     ```
   - Replace `<IP address of the other VM>` with the IPv4 address obtained earlier.

---

### Troubleshooting Tips:
1. **Ping Fails**: Ensure the firewall rule is correctly applied and that both VMs are on the same subnet.
2. **Network Configuration**: If using NAT, check if the VMs are isolated. Consider switching to a Bridged or Host-Only adapter.
3. **VM Software Settings**: Some hypervisors have additional settings for inter-VM communication; ensure these are enabled.

