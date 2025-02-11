You can use the following `iptables` rule to log incoming TCP packets with the `SYN` flag set:  

```sh
iptables -A INPUT -p tcp --syn -j LOG --log-prefix "SYN Packet: " --log-level 4
```

### Explanation:
- `-A INPUT` → Append the rule to the INPUT chain (for incoming traffic).
- `-p tcp` → Match TCP packets.
- `--syn` → Match packets with the SYN flag set (indicating new connection attempts).
- `-j LOG` → Log the matching packets.
- `--log-prefix "SYN Packet: "` → Prefix log entries for easy identification.
- `--log-level 4` → Set log level (4 = warning, which is a good balance between visibility and verbosity).  

### Where to Find Logs:
The logged packets will appear in `/var/log/kern.log`, `/var/log/messages`, or `/var/log/syslog`, depending on your system configuration.

Let me know if you need any modifications!
