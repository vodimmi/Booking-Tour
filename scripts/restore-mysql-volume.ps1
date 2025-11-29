# Restore a tar.gz backup into the mysql-data named volume
# Usage: .\restore-mysql-volume.ps1 backup-file.tar.gz
# Example: .\restore-mysql-volume.ps1 bt_mysql_data_backup.tar.gz

param(
    [Parameter(Mandatory=$true)]
    [string]$BackupFile
)

if (-not (Test-Path $BackupFile)) {
    Write-Error "Backup file not found: $BackupFile"
    exit 1
}

Write-Host "Restoring $BackupFile into Docker volume 'mysql-data'"

docker run --rm -v mysql-data:/volume -v ${PWD}:/backup --entrypoint sh busybox -c "cd /volume && tar xzf /backup/$(Split-Path -Leaf $BackupFile) --strip 1"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Restore completed"
} else {
    Write-Error "Restore failed"
}
