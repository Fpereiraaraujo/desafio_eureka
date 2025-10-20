# Cooperfilme Challenge

Aplicação **fullstack** para gerenciamento de submissões de roteiros, implementando um **workflow completo de análise, revisão e votação/aprovação**, com foco em boas práticas de arquitetura, autenticação, UX e containerização.

---

## Tecnologias Utilizadas

- **Backend:**
  - Java 17
  - Spring Boot (Arquitetura Hexagonal)
  - Spring Security + JWT (autenticação e controle de roles)
  - Swagger (documentação da API)
  - Maven
- **Frontend:**
  - React 18
  - TypeScript
  - TailwindCSS (estilização moderna e responsiva)
  - React Hooks (gerenciamento de estado)
- **Banco de Dados:**
  - MySQL 8
  - Docker volume persistente
- **Containerização:**
  - Docker + Docker Compose
- **Outras práticas:**
  - Loading e tratamento de erros
  - Atualização reativa do estado após ações
  - Workflow configurável de submissões
  - Roles: `ANALYST` e `APPROVER`

---

## Estrutura do Projeto

cooperfilme/
├─ backend/
│ ├─ src/main/java/com/fernandopereira/cooperfilme/...
│ ├─ pom.xml
│ └─ Dockerfile
├─ frontend/
│ ├─ src/
│ │ ├─ pages/
│ │ │ └─ Dashboard.tsx
│ │ └─ api/
│ │ └─ api.ts
│ ├─ package.json
│ └─ Dockerfile
├─ docker-compose.yml
├─ README.md


---

## Funcionalidades

### Backend (Spring Boot + Java)

- **Autenticação JWT** e controle de roles:
  - `ANALYST`: pode analisar submissões
  - `APPROVER`: pode votar submissões
- **Endpoints REST principais:**
  - `POST /login` → Gera token JWT
  - `GET /v1/public/submissions` → Lista todas as submissões
  - `POST /v1/submissions/:id/claim` → Reivindicar submissão
  - `POST /v1/submissions/:id/analyze` → Aprovar ou rejeitar submissão
  - `POST /v1/submissions/:id/review` → Reivindicar para revisão
  - `POST /v1/submissions/:id/review/finish` → Finalizar revisão com nota
  - `POST /v1/submissions/:id/vote` → Votar `YES` ou `NO`
- **Swagger:** documentação disponível em `/swagger-ui.html`
- **Workflow de stages:**
AWAITING_ANALYSIS → IN_ANALYSIS → AWAITING_REVIEW → IN_REVIEW → AWAITING_APPROVAL → IN_APPROVAL → APPROVED / REJECTED

- **Objeto Submission:**
  - `id`, `title`, `content`
  - `clientName`, `clientEmail`, `clientPhone`
  - `stage`, `analystNote`
  - `approvalsCount`, `rejectionsCount`
- **Boas práticas:**
  - Arquitetura hexagonal
  - DTOs para requests/responses
  - Regras de negócio separadas da camada de infraestrutura
  - Controle de roles e validações

---

### Frontend (React + TypeScript + TailwindCSS)

- Dashboard completo com submissões:
  - Exibição condicional de botões e inputs conforme `stage`
  - Inserção de notas de análise e revisão
  - Aprovação/Rejeição de submissões
  - Finalização de revisão
  - Votação YES/NO
- **Reatividade:** atualização automática das submissões após qualquer ação
- **Tratamento de estado:**
  - Loading por submissão
  - Mensagens de erro
- **Estilização:** TailwindCSS para layout moderno e responsivo

---

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
Docker & Docker Compose

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
Subir todos os containers:


docker-compose up --build
Acessar aplicações:

Frontend: http://localhost:3000

Backend API: http://localhost:8080/v1/public/submissions

Swagger: http://localhost:8080/swagger-ui.html

Login: usar usuários cadastrados no banco (ANALYST ou APPROVER)

Workflow de uso
Role	Ações
Analista	Reivindica → Analisa → Aprova/Rejeita
Revisor	Reivindica → Finaliza revisão
Aprovador	Vota → Sistema calcula aprovação final após todos votarem

Boas Práticas Implementadas
JWT e controle de roles

Arquitetura hexagonal no backend

Swagger para documentação da API

Loading e tratamento de erros no frontend

Atualização reativa de submissões

Docker Compose com containers isolados e volumes persistentes

TailwindCSS para layout moderno e responsivo

Observações
Backend retorna submissão completa atualizada após cada ação para sincronização correta com o frontend

Frontend utiliza stages e contadores de aprovação/rejeição para definir o estado atual da submissão

Workflow adaptável para múltiplos aprovadores (configurado para 3 por padrão, mas pode ser alterado no backend)
