# Portfolio Backend

Spring Boot service that records visits to [my portfolio](https://github.com/Sohith-reddy/Portfolio), so I can see when a link I shared (for example in a job application) gets opened.

**Stack:** Java 17 · Spring Boot 3.5 · Spring Data JPA · PostgreSQL · Docker · Google Cloud Run

## API

### `POST /api/track`

Records one visit. The client IP is taken from `X-Forwarded-For` (set by the hosting proxy) and the user agent from the request headers.

```json
{
  "city": "Hyderabad",
  "region": "Telangana",
  "country": "India",
  "ref": "acme-application",
  "page": "/",
  "referrer": "https://www.linkedin.com/"
}
```

All fields are optional; `ref` defaults to `unknown`. Returns `201 Created`:

```json
{ "id": 42, "message": "Visit tracked successfully" }
```

### `GET /actuator/health`

Health check used by the hosting platform.

## Configuration

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_URL` | no | built from `DB_HOST`/`DB_PORT`/`DB_NAME` | Full JDBC URL, e.g. `jdbc:postgresql://host:5432/portfolio_tracker` |
| `DB_HOST` / `DB_PORT` / `DB_NAME` | no | `localhost` / `5432` / `portfolio_tracker` | Used when `DB_URL` is not set |
| `DB_USERNAME` | no | `postgres` | Database user |
| `DB_PASSWORD` | yes (prod) | empty | Database password — never commit it |
| `DB_POOL_SIZE` | no | `5` | Max Hikari connections |
| `CORS_ALLOWED_ORIGINS` | yes (prod) | `http://localhost:3000` | Comma-separated origins, wildcards allowed (e.g. `https://*.vercel.app`) |
| `PORT` | no | `8081` | HTTP port (Cloud Run / Render set this automatically) |

The `visits` table is created/updated automatically on startup.

## Run locally

```bash
export DB_PASSWORD=your-password      # plus DB_URL etc. if not using the defaults
./mvnw spring-boot:run
```

Run the tests (uses an in-memory H2 database, no Postgres needed):

```bash
./mvnw verify
```

## Deploy

### Google Cloud Run (via GitHub Actions)

`.github/workflows/ci-deploy.yml` runs the tests on every push/PR and deploys `main` to Cloud Run once these are configured in **Settings → Secrets and variables → Actions**:

| Kind | Name | Value |
|---|---|---|
| Secret | `GCP_SA_KEY` | JSON key of a service account with *Cloud Run Admin*, *Cloud Build Editor*, *Artifact Registry Writer*, *Service Account User* and *Secret Manager Secret Accessor* |
| Variable | `GCP_PROJECT_ID` | Your GCP project ID (deploy is skipped while unset) |
| Variable | `GCP_REGION` | e.g. `asia-south1` (default) |
| Variable | `DB_URL` | `jdbc:postgresql://<host>:5432/portfolio_tracker` |
| Variable | `DB_USERNAME` | Database user |
| Variable | `CORS_ALLOWED_ORIGINS` | e.g. `https://sohith-portfolio.vercel.app,https://*.vercel.app` |
| Variable | `DB_PASSWORD_SECRET` | Name of the Secret Manager secret holding the DB password (default `portfolio-db-password`) |

Create the password secret once:

```bash
printf '%s' 'your-db-password' | gcloud secrets create portfolio-db-password --data-file=-
```

Or deploy manually from your machine with the same flags:

```bash
gcloud run deploy portfolio-backend --source . --region asia-south1 --allow-unauthenticated \
  --set-env-vars DB_URL=...,DB_USERNAME=...,CORS_ALLOWED_ORIGINS=... \
  --set-secrets DB_PASSWORD=portfolio-db-password:latest
```

### Any Docker host

The `Dockerfile` is a self-contained multi-stage build, so the same image runs on Render, Railway, Fly.io etc.:

```bash
docker build -t portfolio-backend .
docker run -p 8080:8080 -e DB_URL=... -e DB_USERNAME=... -e DB_PASSWORD=... \
  -e CORS_ALLOWED_ORIGINS=http://localhost:3000 portfolio-backend
```

## Connect the frontend

Set `NEXT_PUBLIC_API_URL` in the portfolio's Vercel project to the Cloud Run service URL (printed at the end of the deploy job) and redeploy the frontend.
