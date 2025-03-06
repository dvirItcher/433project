Good morning! Since you set up a **self-signed** Velociraptor server, you need to configure it to allow external access to the GUI from another machine. Follow these steps:

### 1. **Modify Velociraptor Configuration**  
You need to edit the server's configuration file (typically `/etc/velociraptor.config.yaml` or the file you generated during setup). Open it with a text editor:
```bash
sudo nano /etc/velociraptor.config.yaml
```
Find the section under `GUI:` and `frontend:` and update the `bind_address` to `0.0.0.0` so that it listens on all interfaces:
```yaml
GUI:
  bind_address: 0.0.0.0
  bind_port: 8889  # Change this if needed

frontend:
  bind_address: 0.0.0.0
  bind_port: 8000  # Change this if needed
```
Save and exit (`CTRL + X`, then `Y`, then `Enter`).

### 2. **Restart Velociraptor**  
Apply the changes by restarting the Velociraptor service:
```bash
sudo systemctl restart velociraptor
```

### 3. **Allow Firewall Rules (If Enabled)**  
If you are using a firewall (e.g., `ufw` or `iptables`), allow access to the Velociraptor GUI port (default is `8889`):
```bash
sudo ufw allow 8889/tcp
sudo ufw allow 8000/tcp
```
For `iptables`:
```bash
sudo iptables -A INPUT -p tcp --dport 8889 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 8000 -j ACCEPT
```

### 4. **Access from Another Machine**  
On your browser, enter the following, replacing `<server-ip>` with your Velociraptor server’s IP address:
```
https://<server-ip>:8889
```
Since it is self-signed, your browser will warn about an insecure connection. Click **Advanced** and proceed.

Let me know if you run into any issues!
