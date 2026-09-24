# DevHub API

API REST personnelle pour enregistrer, rechercher et organiser des snippets de code et des configurations.

## Prérequis

- Java 21
- Docker et Docker Compose

## Lancer en développement

Créer la configuration locale à partir du modèle, puis remplacer les valeurs d'exemple, en particulier `JWT_SECRET` :

```bash
cp .env.example .env
docker compose up -d
./mvnw spring-boot:run
```

L'application démarre sur `http://localhost:8080`.

| URL | Usage |
| --- | --- |
| `GET /actuator/health` | État de l'application et de PostgreSQL |
| `/swagger-ui.html` | Documentation OpenAPI en développement |
| `POST /api/auth/login` | Obtention d'un JWT |
| `GET /api/demo/snippets` | Snippets de démonstration publics |

Arrêter PostgreSQL local :

```bash
docker compose down
```

Ajouter `-v` à cette commande supprime aussi les données locales.

## Variables d'environnement

L'application charge automatiquement un fichier `.env` local. Il est ignoré par Git.

| Variable | Requise | Description |
| --- | --- | --- |
| `JWT_SECRET` | Oui | Secret HS256 d'au moins 32 caractères. Générer avec `openssl rand -base64 48`. |
| `JWT_TOKEN_LIFETIME` | Non | Durée de validité des JWT. Défaut : `PT12H`. |
| `ADMIN_EMAIL` | Non | Email du compte administrateur créé au démarrage. |
| `ADMIN_PASSWORD` | Non | Mot de passe du compte administrateur initial. À définir avec `ADMIN_EMAIL`. |
| `DB_URL` | Non en local | URL JDBC. Défaut : `jdbc:postgresql://localhost:5432/devhub`. |
| `DB_USERNAME` | Non en local | Utilisateur PostgreSQL. Défaut : `devhub`. |
| `DB_PASSWORD` | Non en local | Mot de passe PostgreSQL. Défaut : `devhub`. |
| `CORS_ALLOWED_ORIGINS` | Non en local | Origines frontend autorisées. Défaut : `http://localhost:5173`. |

Les variables `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` et `POSTGRES_PORT` sont lues par le Compose de développement. Si les identifiants PostgreSQL sont modifiés, définir aussi `DB_USERNAME` et `DB_PASSWORD` pour l'application locale.

## Lancer avec la configuration de production

Le profil `prod` désactive Swagger, limite Actuator à l'état global et exige les variables de base de données et CORS.

```bash
cp .env.production.example .env.production
# Modifier toutes les valeurs d'exemple avant de continuer.
docker compose --env-file .env.production -f compose.production.yaml up -d --build
```

Vérifier l'état des conteneurs et de l'API :

```bash
docker compose --env-file .env.production -f compose.production.yaml ps
curl http://localhost:8080/actuator/health
```

Suivre les logs ou arrêter les services :

```bash
docker compose --env-file .env.production -f compose.production.yaml logs -f api
docker compose --env-file .env.production -f compose.production.yaml down
```

Ne jamais versionner `.env` ou `.env.production`. En hébergement, fournir ces valeurs par le gestionnaire de secrets de la plateforme.

## Tests

```bash
./mvnw test
```

La GitHub Action exécute ces tests à chaque push et pull request avec une base PostgreSQL éphémère.
