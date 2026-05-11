# Bot Agendamento Inteligente (WhatsApp + Google Calendar)

Base completa de projeto com backend Java/Spring Boot e frontend Angular para evolução em produção.

## Stack
- Backend: Java 21, Spring Boot 3, Spring Security + JWT, JPA, PostgreSQL, Redis, Flyway, Swagger
- Frontend: Angular 18, Angular Material, TailwindCSS
- Infra: Docker, Docker Compose

## Estrutura
- `backend/`: API REST, segurança, entidades de negócio, migrações e integrações base
- `frontend/admin-dashboard/`: painel administrativo inicial responsivo
- `postman/`: collection inicial
- `.env.example`: variáveis de ambiente

## Entidades modeladas
- `User`, `Role`, `Client`, `Appointment`, `ServiceType`, `BusinessHours`, `Holiday`, `Notification`, `ConversationContext`, `AuditLog`

## Execução local com Docker
```bash
cp .env.example .env
docker compose up --build
```

- Frontend: http://localhost:4200
- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html

## Backend local
```bash
cd backend
mvn spring-boot:run
```

## Frontend local
```bash
cd frontend/admin-dashboard
npm install
npm start
```

## Segurança
- JWT com expiração configurável
- RBAC (`ADMIN`, `STAFF`)
- Rotas protegidas por perfil
- Estrutura pronta para validação de webhook/rate limiting
- **Obrigatório em produção**: sobrescrever `JWT_SECRET` e `WEBHOOK_TOKEN` no ambiente

## Banco e seeds
- Migrations Flyway em `backend/src/main/resources/db/migration`
- Seed complementar em `backend/scripts/seeds.sql`

## Endpoints principais
- `POST /api/auth/register-admin`
- `POST /api/auth/login`
- `POST /api/staff/appointments`
- `GET /api/staff/appointments?dayStart=...`
- `GET/POST /api/webhooks/whatsapp`

## Próximos passos recomendados
1. Conectar Google Calendar API real com OAuth2
2. Conectar WhatsApp Cloud API/Twilio real
3. Adicionar camada de multiempresa (tenant)
4. Implementar automações de lembrete e resumo diário
5. Cobrir fluxo com testes de integração end-to-end
