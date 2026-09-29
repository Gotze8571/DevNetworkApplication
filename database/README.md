# Database

PostgreSQL 16, run through `docker-compose.yml` at the repo root.

## Connection (local defaults)

| Setting | Value |
|---|---|
| Host | `localhost:5432` |
| Database | `devnetwork` |
| User / password | `devnetwork` / `devnetwork` |

Override these with `POSTGRES_DB`, `POSTGRES_USER` and `POSTGRES_PASSWORD` in a root `.env` file.

Start only the database:

```bash
docker compose up database
```

## Schema migrations

The schema is owned by the backend and managed with **Flyway**. Migrations live in
[`backend/src/main/resources/db/migration`](../backend/src/main/resources/db/migration) and run automatically when the backend starts.

- Name new files `V<next number>__<description>.sql`, e.g. `V2__create_connections_table.sql`.
- Never edit a migration that has already been applied; add a new one instead.
- Hibernate is set to `validate`, so the app refuses to start if entities and schema disagree.

## Reset local data

```bash
docker compose down -v   # removes the db-data volume
```
