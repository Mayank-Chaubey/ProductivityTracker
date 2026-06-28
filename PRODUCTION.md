# Production Notes

## Required environment

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `APP_BASEURL`
- `MAIL_ENABLED=true`
- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `MAIL_FROM`

## Health check

Use `GET /ProductivityTracker/health`. It returns JSON and `503` when the database is unavailable.

## Backups

Back up the MySQL database daily with `mysqldump` or managed snapshots. Verify restore into a staging database before relying on backups.

## Monitoring

Ship Tomcat logs and application logs to your preferred log collector. Alert on `/health` failures, elevated 5xx rates, and reminder scheduler errors.
