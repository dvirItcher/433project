If you're in a domain environment and limited to using a specific DNS server (e.g., a corporate DNS server) that may be manipulated or restricted, you have a few options to verify DNS authenticity:

1. **Check for DNSSEC Validation**:
   See if your domain’s DNS server supports DNSSEC. While this might not be something you can control, DNSSEC can ensure that DNS responses are authenticated and haven’t been tampered with. You can check DNSSEC validation with:

   ```sh
   nslookup -type=any example.com
   ```

   If DNSSEC is active, you’ll typically see records like RRSIG in the response, indicating a signature-based verification.

2. **Request a Temporary Policy Exception**:
   If you have a legitimate security reason to check other DNS servers, you could ask IT for temporary access to an external DNS resolver. Explain that you need to verify DNS records for security or troubleshooting purposes, which may make IT more willing to grant an exception.

3. **Use VPN or Proxy (If Permitted)**:
   Some VPNs or proxies might have their own DNS resolvers, allowing you to bypass your domain’s DNS settings temporarily. Check with IT to see if they allow VPN usage and if the VPN can use a different DNS server. This is only advisable if permitted by your organization’s policies.

4. **Verify IPs Through Other Channels**:
   If you suspect DNS tampering, you could ask a trusted contact outside your network to run `nslookup` for the domain you need and share the IPs they receive. You can then compare these IPs to what you get in your environment.

5. **Examine Host Entries**:
   Sometimes, network policies might involve redirecting specific DNS queries using local host file entries. You can review your local `hosts` file (typically found at `C:\Windows\System32\drivers\etc\hosts` on Windows or `/etc/hosts` on Linux) to ensure there are no unexpected redirections. Changes to this file could indicate an attempt to reroute certain requests without modifying the DNS server.

6. **Look for Network-Specific Anomalies**:
   Monitor for unusual network activity, like unknown IPs responding to your DNS requests or unusual TTL values, which could signal a man-in-the-middle attack within your domain.

If you have no way to access other servers directly, these steps can help identify potential DNS tampering within the constraints of your domain environment.
