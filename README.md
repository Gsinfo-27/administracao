# API de Geração e Gerenciamento de Keys

API RESTful desenvolvida para criar, gerenciar e validar **API Keys** e **chaves de acesso** de forma segura, com **autenticação JWT** e **upload de imagens via Cloudinary**.  
Ideal para sistemas que precisam controlar acessos e gerenciar perfis de usuários com imagens na nuvem.

---

## Tecnologias Utilizadas

- **Backend:** Java, Spring Boot  
- **Segurança:** Spring Security, JWT  
- **Banco de Dados:** PostgreSQL (Neon ou Render adapte a sua escolha como dica use arquivos .env)  
- **Uploads de Imagens:** Cloudinary  
- **Ferramentas:** Maven, Git, Postman / Insomnia para testes  

---

## Funcionalidades Principais

### Usuários
- Registrar novo usuário  
- Login com JWT  
- Alterar senha  
- Upload de imagem de perfil via Cloudinary  
- Listar usuários ativos  
- Deletar usuários  

### Chaves de Segurança
- Criar chave para um endpoint específico  
- Ativar ou desativar chaves  
- Validar chave e retornar endpoint associado  

### API Keys
- Criar API Key para integração com sistemas externos  
- Listar API Keys com filtros, ordenação e paginação  
- Consultar API Key específica pelo `keygen`  
- Alterar status de API Key (ativa/inativa)  
- Remover API Key  

---

## Endpoints da API

### Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/api/user/create` | Registrar novo usuário |
| POST   | `/api/user/login` | Login e geração de JWT |
| PUT    | `/api/user/me/password` | Alterar senha |
| POST   | `/api/user/upload-image` | Atualizar imagem de perfil (Cloudinary) |
| GET    | `/api/user/users` | Listar usuários |
| DELETE | `/api/user/users/{id}` | Remover usuário |

### Chaves de Segurança
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/api/security/create?endpoint=` | Criar nova chave |
| POST   | `/api/security/activate/{keygen}` | Ativar chave |
| POST   | `/api/security/deactivate/{keygen}` | Desativar chave |
| GET    | `/api/security/access/{keygen}` | Validar chave e retornar endpoint |

### API Keys
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/api/keys` | Criar nova API Key |
| GET    | `/api/keys` | Listar API Keys com filtros e paginação |
| GET    | `/api/keys/{keygen}` | Consultar API Key específica |
| PUT    | `/api/keys/{keygen}/status/{newStatus}` | Alterar status de API Key |
| DELETE | `/api/keys/{keygen}` | Remover API Key |

---

## Como Rodar a API

1. Clone o repositório:  
```bash
git clone https://github.com/Gsinfo-27/administracao.git
