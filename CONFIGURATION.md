# Environment Variables Configuration

This application requires the following environment variables to be set for database connectivity and server configuration.

## Required Environment Variables

### Database Configuration

- `DB_URL`: PostgreSQL database connection URL
  - Format: `jdbc:postgresql://<host>:<port>/<database>`
  - Example: `jdbc:postgresql://localhost:5432/portfolio_tracker`

- `DB_USERNAME`: Database username
  - Example: `postgres`

- `DB_PASSWORD`: Database password
  - **Important**: Never commit this value to version control

### Server Configuration

- `PORT` (Optional): Server port number
  - Default: `8081`
  - Example: `8080`

## How to Set Environment Variables

### Linux/macOS

```bash
export DB_URL=jdbc:postgresql://your-host:5432/your-database
export DB_USERNAME=your-username
export DB_PASSWORD=your-password
export PORT=8081
```

### Windows (Command Prompt)

```cmd
set DB_URL=jdbc:postgresql://your-host:5432/your-database
set DB_USERNAME=your-username
set DB_PASSWORD=your-password
set PORT=8081
```

### Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:postgresql://your-host:5432/your-database"
$env:DB_USERNAME="your-username"
$env:DB_PASSWORD="your-password"
$env:PORT="8081"
```

### Using .env file (for local development)

You can create a `.env` file in the project root (this file is gitignored):

```
DB_URL=jdbc:postgresql://localhost:5432/portfolio_tracker
DB_USERNAME=postgres
DB_PASSWORD=your-password
PORT=8081
```

**Note**: Make sure to add `.env` to your `.gitignore` file to prevent committing sensitive data.

### Using IDE (IntelliJ IDEA / Eclipse)

1. Open Run/Debug Configurations
2. Add environment variables in the Environment Variables section
3. Set each variable with its corresponding value

## Running the Application

After setting the environment variables, run the application:

```bash
./mvnw spring-boot:run
```

## Production Deployment

For production environments (e.g., cloud platforms like GCP, AWS, Heroku, Render):

1. Set environment variables in your platform's configuration dashboard
2. Never include production credentials in your code or version control
3. Use your platform's secret management system when available

## Security Best Practices

- ✅ **DO**: Use environment variables for sensitive data
- ✅ **DO**: Use strong, unique passwords for your database
- ✅ **DO**: Rotate credentials regularly
- ✅ **DO**: Use secret management tools in production
- ❌ **DON'T**: Commit credentials to version control
- ❌ **DON'T**: Share credentials via insecure channels
- ❌ **DON'T**: Use default or weak passwords
