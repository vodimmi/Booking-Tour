# Backup the mysql-data named volume to a tar.gz in the current folder
# Usage: .\backup-mysql-volume.ps1 [output-file]
# Example: .\backup-mysql-volume.ps1 bt_mysql_backup_$(Get-Date -Format yyyyMMddHHmm).tar.gz

param(
    [string]$OutFile = "bt_mysql_data_backup.tar.gz"
)

Write-Host "Backing up Docker volume 'mysql-data' to $OutFile"

docker run --rm -v mysql-data:/volume -v ${PWD}:/backup --entrypoint sh busybox -c "cd /volume && tar czf /backup/$OutFile ."

if ($LASTEXITCODE -eq 0) {
    Write-Host "Backup completed: $OutFile"
} else {
    Write-Error "Backup failed"
}
