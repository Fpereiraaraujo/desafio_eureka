# Cooperfilme Challenge

Este projeto é uma aplicação fullstack de gerenciamento de submissões de roteiros, com workflow completo de **análise**, **revisão** e **votação/aprovação**, implementado com:

- **Backend:** Spring Boot (Java) com arquitetura hexagonal.
- **Frontend:** React + TypeScript.
- **Banco de Dados:** MySQL.
- **Autenticação:** JWT.
- **Containerização:** Docker + Docker Compose.

---

## Estrutura do Projeto

cooperfilme/
├─ backend/
│ ├─ src/main/java/com/fernandopereira/cooperfilme/...
│ ├─ pom.xml
├─ frontend/
│ ├─ src/
│ │ ├─ pages/
│ │ │ └─ Dashboard.tsx
│ │ └─ api/
│ │ └─ api.ts
│ ├─ package.json
├─ docker-compose.yml
├─ README.md
---

## Funcionalidades

### Backend

- Autenticação JWT para analistas e aprovadores.
- Endpoints REST:
  - `POST /login` → autenticação e geração do token JWT.
  - `GET /v1/public/submissions` → listar todas as submissões.
  - `POST /v1/submissions/:id/claim` → reivindicar análise ou revisão.
  - `POST /v1/submissions/:id/analyze` → aprovar ou rejeitar submissão na análise.
  - `POST /v1/submissions/:id/review` → reivindicar para revisão.
  - `POST /v1/submissions/:id/review/finish` → finalizar revisão com nota.
  - `POST /v1/submissions/:id/vote` → votar `YES` ou `NO` em submissão.

- Workflow de stages:
AWAITING_ANALYSIS → IN_ANALYSIS → AWAITING_REVIEW → IN_REVIEW → AWAITING_APPROVAL → IN_APPROVAL → APPROVED / REJECTED

- Objeto Submission:
- `id`, `title`, `content`
- `clientName`, `clientEmail`, `clientPhone`
- `stage`, `analystNote`
- `approvalsCount`, `rejectionsCount`

### Frontend

- Dashboard com visualização de submissões.
- Botões e inputs exibidos conforme `stage`.
- Atualização automática de stages e notas.
- Tratamento de loading e erros para todas as ações.

### Banco de Dados (MySQL)

```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255),
  role ENUM('ANALYST','APPROVER')
);

CREATE TABLE submissions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  content TEXT,
  client_name VARCHAR(100),
  client_email VARCHAR(100),
  client_phone VARCHAR(20),
  stage VARCHAR(50),
  analyst_note TEXT,
  approvals_count INT DEFAULT 0,
  rejections_count INT DEFAULT 0
);
Docker
yaml
Copiar código
version: "3.8"

services:
  cooper_db:
    image: mysql:8.0
    container_name: cooper_db
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: cooperfilme
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
    networks:
      - cooper-net

  backend:
    build: ./backend
    container_name: cooper_backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://cooper_db:3306/cooperfilme
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      JWT_SECRET: secret_key
    ports:
      - "8080:8080"
    depends_on:
      - cooper_db
    networks:
      - cooper-net

  frontend:
    build: ./frontend
    container_name: cooper_frontend
    ports:
      - "3000:3000"
    depends_on:
      - backend
    networks:
      - cooper-net

networks:
  cooper-net:
    driver: bridge

volumes:
  db_data:
Rodando o Projeto
Subir containers:


docker-compose up --build
Acessar frontend:

Frontend: http://localhost:3000

Backend API: http://localhost:8080/v1/public/submissions

Login com usuários cadastrados no banco.

Workflow:

Analista: Reivindica → Analisa → Aprova/Rejeita

Revisor: Reivindica → Finaliza revisão

Aprovador: Vota → Resultado final

Boas práticas
JWT e controle de roles.

Arquitetura hexagonal no backend.

Loading e tratamento de erros no frontend.

Docker Compose isolado e com volumes persistentes.
