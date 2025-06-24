# Course Registration Project

## Running Locally with Docker

You can use Docker to quickly start the services.

### 1. Environmental Variables
- Create a `.env` file with the following layout. Then fill in and modify necessary details.
```
# Environment
ENVIRONMENT=PRODUCTION

# Database
DB_USER=register_rus_user
DB_NAME=register_r_us
DB_PASSWORD=lkjasdfoiuLKN98
DB_PORT=3306
DB_URL=jdbc:mysql://rru_database:3306/register_r_us

# Server
SERVER_PORT=8484

# JWT
JWT_SECRET=
JWT_ISSUER=http://localhost:8484

# Cors
CLIENT_URL=http://localhost:5173

# Frontend
FRONTEND_PORT=5173
```
- For JWT Secret generate a random HS256 secret using [this generator](https://ij0c1ykkfk.execute-api.us-east-1.amazonaws.com/default/hs256_generator).
- For production, change `ENVIRONMENT` to `production`

### 2. Start the Containers
- Make sure [Docker](https://www.docker.com/) is installed and running.
- Use the provided `compose.yaml` file:
  ```sh
  docker compose up -d --build
  ```
- This will start the frontend, database and backend containers in the background.
- The backend will connect to the database container automatically (see `compose.yaml` for details).
- To verify the frontend is up and running visit http://localhost:5173 or your configured endpoint.
- To verify the backend is up and running visit the [/api-docs-ui](http://localhost:8484/api-docs-ui)  endpoint in your browser.

### 3. Stopping Services
- To stop all running containers:
  ```sh
  docker compose down
  ```

For more details, see the [backend/README.md](./backend/README.md) and `compose.yaml` files.

