To create a Bash script that extracts the password from the encoded file, we need to reverse the process: repeatedly **decode** the Base64-encoded content until we retrieve the original password. Here's how you can do it:

---

### Bash Script
Save this as `decode_password.sh`:

```bash
#!/bin/bash

# Input file
filename="file"

# Read the content of the file
encoded_password=$(cat "$filename")

# Decoding loop
while true; do
    # Try decoding Base64 content
    decoded=$(echo "$encoded_password" | base64 -d 2>/dev/null)
    
    # Check if decoding was successful
    if [[ $? -ne 0 ]]; then
        # If decoding fails, we have reached the original password
        echo "Password found: $encoded_password"
        exit 0
    else
        # Update the encoded_password for the next iteration
        encoded_password="$decoded"
    fi
done
```

---

### Explanation:
1. **Read the file**: The script reads the file's content into a variable.
2. **Decode repeatedly**: A `while` loop tries to decode the content using `base64 -d` until it fails.
3. **Stop when failure occurs**: When decoding fails, the last valid decoded content is the original password.
4. **Output the password**: It prints the password.

---

### Steps to Use:
1. Save the script as `decode_password.sh`.
2. Make it executable:
   ```bash
   chmod +x decode_password.sh
   ```
3. Run the script:
   ```bash
   ./decode_password.sh
   ```

---

### Example Output:
If your file contains a deeply Base64-encoded password, the script will print:
```
Password found: 123456
```

---

This script works for your described Python encoding logic. It will keep decoding until it retrieves the original ASCII-encoded password.
