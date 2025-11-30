Volume-Based Data Persistence Pattern (MySQL)

Overview
--------
This repository now uses a named Docker volume `mysql-data` to persist MySQL data. The volume is mounted to the container's data directory (`/var/lib/mysql`) so database files are kept on the host and survive container recreation.

Why this helps
---------------
- Containers are ephemeral; data inside the container filesystem is lost when the container is removed.
- Named volumes store data on the Docker host and survive `docker rm` or container image upgrades.
- You can back up or restore the entire volume (not just SQL dumps) for full safety.

Compose configuration
---------------------
See `docker-compose.yml` — the `mysql` service mounts `mysql-data:/var/lib/mysql` and `./mysql-init:/docker-entrypoint-initdb.d:ro`.

Important notes & best practices
-------------------------------
- Avoid running `docker compose down -v` because `-v` removes named volumes. Use `docker compose down` (without `-v`) or `docker rm` for containers only.
- If you want to fully protect the volume from accidental removal, create it externally and mark `external: true` in `docker-compose.yml`:

  volumes:
    mysql-data:
      external: true

  Then create it once on the host:

  docker volume create mysql-data

- Use the included scripts in `scripts/` to backup/restore the volume if needed:
  - `scripts/backup-mysql-volume.ps1`
  - `scripts/restore-mysql-volume.ps1`

How to take a snapshot (PowerShell)
----------------------------------
# Backup (run from repository root)
.\scripts\backup-mysql-volume.ps1 bt_mysql_backup_20251123.tar.gz

# Restore (run from repository root)
.\scripts\restore-mysql-volume.ps1 bt_mysql_backup_20251123.tar.gz

Advanced: Backing up on Linux/macOS
-----------------------------------
Linux/macOS command equivalent (from repo root):

docker run --rm -v mysql-data:/volume -v $(pwd):/backup busybox tar czf /backup/db_data_backup.tar.gz -C /volume .

to restore:

docker run --rm -v mysql-data:/volume -v $(pwd):/backup busybox sh -c "cd /volume && tar xzf /backup/db_data_backup.tar.gz --strip 1"

Wrap-up
-------
- The repo now declares a named `mysql-data` volume and includes backup/restore helpers.
- Next steps: decide whether you want the compose file to treat the volume as `external: true` (prevents accidental deletion via `docker compose down -v`) or keep it local (easier for CI/dev workflow).