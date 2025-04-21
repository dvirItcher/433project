# ⚠️ Run as a normal (non-admin) user
# Set up paths
$sysmonDir = "C:\ProgramData\Sysmon"
$targetDir = "$env:USERPROFILE\Desktop\SysmonSymlinkTest"
$testFile = "$targetDir\test.txt"

# Clean up any previous test
Remove-Item $targetDir -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $targetDir | Out-Null
Remove-Item $sysmonDir -Recurse -Force -ErrorAction SilentlyContinue

# Create the symlink
cmd /c "mklink /D `"$sysmonDir`" `"$targetDir`""

# Wait a bit
Start-Sleep -Seconds 2

# Trigger a sysmon config reload (must be installed)
try {
    & "$env:windir\Sysmon64.exe" -c "$env:windir\Sysmon.xml"
} catch {
    Write-Host "[!] Could not reload Sysmon config. Try running this manually." -ForegroundColor Red
}

# Wait for Sysmon to write
Start-Sleep -Seconds 3

# Check if Sysmon wrote anything in the symlink target
if (Test-Path $testFile -or (Get-ChildItem $targetDir | Measure-Object).Count -gt 0) {
    Write-Host "`n🚨 Your Sysmon installation appears to be vulnerable to CVE-2023-29343!" -ForegroundColor Red
    Write-Host "→ It followed the symlink and wrote into: $targetDir`n"
} else {
    Write-Host "`n✅ Sysmon did NOT follow the symlink. Likely not vulnerable." -ForegroundColor Green
}

# Clean up
Remove-Item $sysmonDir -Recurse -Force -ErrorAction SilentlyContinue
