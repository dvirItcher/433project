Good morning! I can walk you through setting up a Velociraptor server and configuring it for remote GUI access. Here's a step-by-step guide:

### **1. Install Velociraptor on the Server**
1. Download the latest Velociraptor binary:
   - [https://github.com/Velocidex/velociraptor/releases](https://github.com/Velocidex/velociraptor/releases)

2. Make the binary executable:
   ```bash
   chmod +x velociraptor-vX.X.X-linux-amd64
   ```

3. Move it to a directory in your `PATH`:
   ```bash
   sudo mv velociraptor-vX.X.X-linux-amd64 /usr/local/bin/velociraptor
   ```

---

### **2. Generate a Configuration File**
1. Create a Velociraptor server configuration:
   ```bash
   velociraptor config generate -i
   ```
   - **Frontend URL**: Use the server's IP or DNS name (`https://<server-ip>:8000`)
   - **GUI bind address**: Set it to `0.0.0.0:8889` to allow connections from any address.
   - Generate SSL certificates when prompted (self-signed is fine for local use).

2. Save the generated configuration (e.g., `/etc/velociraptor.config.yaml`).

---

### **3. Start the Velociraptor Server**
1. Start the server using the config file:
   ```bash
   sudo velociraptor -c /etc/velociraptor.config.yaml frontend
   ```

2. (Optional) Set up a systemd service to keep it running:
   Create a service file:
   ```bash
   sudo nano /etc/systemd/system/velociraptor.service
   ```
   Add the following:
   ```ini
   [Unit]
   Description=Velociraptor Server
   After=network.target

   [Service]
   ExecStart=/usr/local/bin/velociraptor -c /etc/velociraptor.config.yaml frontend
   Restart=always
   User=root

   [Install]
   WantedBy=multi-user.target
   ```

   Enable and start the service:
   ```bash
   sudo systemctl enable velociraptor
   sudo systemctl start velociraptor
   ```

---

### **4. Open Firewall Ports**
- Open the GUI port (e.g., 8889) and frontend port (e.g., 8000) in the firewall:
   ```bash
   sudo ufw allow 8000/tcp
   sudo ufw allow 8889/tcp
   sudo ufw reload
   ```

---

### **5. Connect to the GUI from Another Machine**
1. Open a browser on another machine and navigate to:
   ```
   https://<server-ip>:8889
   ```
2. Log in using the credentials from the setup process.

---

### ✅ **Next Steps**
- If you want to enroll endpoints, you can generate a client configuration file and install it on the target machine.
- Let me know if you need help setting up the agents!
