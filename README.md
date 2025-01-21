$disk = "\\.\PhysicalDrive0"
$stream = [System.IO.File]::Open($disk, 'Open', 'Read', 'None')
$buffer = New-Object byte[] 512
$stream.Read($buffer, 0, 512)
$stream.Close()
[System.Text.Encoding]::ASCII.GetString($buffer)
