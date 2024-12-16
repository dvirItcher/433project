To extract a `.rar` file in a CentOS virtual machine, follow these steps:

### Step 1: Install `unrar`
1. Open your terminal.
2. Install the `unrar` package using the following command:
   ```bash
   sudo yum install -y epel-release
   sudo yum install -y unrar
   ```
   If `yum` doesn’t find `unrar`, it may be in the EPEL (Extra Packages for Enterprise Linux) repository.

### Step 2: Extract the RAR File
1. Navigate to the directory containing the `.rar` file:
   ```bash
   cd /path/to/your/rarfile
   ```
2. Use `unrar` to extract the file:
   - To extract the contents to the current directory:
     ```bash
     unrar x filename.rar
     ```
   - To extract to a specific directory:
     ```bash
     unrar x filename.rar /path/to/extract/to
     ```

### Step 3: Verify the Extraction
Check the directory to ensure the files were extracted successfully:
```bash
ls /path/to/extract/to
```

### Troubleshooting
- If `unrar` is unavailable, you can install the `rar` package or use `7zip` as an alternative:
  ```bash
  sudo yum install -y p7zip
  ```
  Then, extract the file with:
  ```bash
  7z x filename.rar
  ```
