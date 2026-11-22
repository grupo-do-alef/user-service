# TrampoRápido — User Service

Serviço responsável por cadastro, autenticação e perfis dos usuários do TrampoRápido.

## Contexto e papel no projeto

O TrampoRápido conecta profissionais a oportunidades de trabalho. O projeto está dividido em três repositórios:

| Repositório | Papel |
| --- | --- |
| `tramporapido-ui` | Interface web em Next.js, React e TypeScript. |
| `user-service` | Identidade dos usuários, autenticação por JWT e armazenamento de perfis e currículos. |
| `task-service` | Vagas, candidaturas e dúvidas sobre oportunidades. |

Este serviço é a origem dos dados de identidade usados pela interface e esperados pelo serviço de tarefas. Possui seu próprio PostgreSQL; os dados de vagas ficam no banco do `task-service`.

## Tecnologias

- Java 21 como versão alvo no Maven e Spring Boot 4.1.1.
- Spring MVC, Spring Security e Spring Data JPA/Hibernate.
- PostgreSQL 17 no Docker Compose.
- BCrypt para senhas e JJWT 0.12.5 para tokens.
- Lombok, Springdoc/OpenAPI e HATEOAS nas dependências.
- Maven Wrapper, Docker, Jenkins e GitHub Actions.


## Organização do código

A estrutura segue uma proposta de arquitetura hexagonal:

```text
Controller → porta de entrada → caso de uso → porta de saída → adaptador
```

- `domain/model`: usuário, papéis e currículo.
- `port/in`: contratos dos casos de uso.
- `port/out`: contratos de persistência, codificação de senha e token.
- `application/usecase`: cadastro, autenticação e consulta de perfil.
- `adapter/in`: controllers HTTP.
- `adapter/out`: implementações de persistência, BCrypt e JWT.
- `infra`: segurança, configuração, entidades JPA e tratamento de erros.

A separação não é uniforme: o controller de currículo acessa diretamente o repositório de persistência.

## API e fluxo de autenticação

| Método | Rota | Uso |
| --- | --- | --- |
| POST | `/api/register` | Cadastro público. |
| POST | `/api/auth/login` | Login público e emissão do JWT. |
| GET | `/users/{id}` | Consulta autenticada de dados do usuário. |
| GET | `/users/me/profile` | Consulta do próprio currículo. |
| PUT | `/users/me/profile` | Gravação do próprio currículo. |

O cadastro verifica duplicidade de e-mail, codifica a senha com BCrypt e persiste o usuário. Os papéis do domínio são `WORKER`, `EMPLOYER`, `AGENCY` e `ADMIN`.

O login retorna `accessToken`, `email`, `name` e `roles`. O token contém o e-mail como assunto, papéis e expiração de 24 horas. As chamadas protegidas usam `Authorization: Bearer <token>`.

A autenticação é stateless: não há sessão HTTP persistida no servidor nesse fluxo. O frontend guarda o token em cookie acessível por JavaScript.

## Persistência

- `users_tb`: dados pessoais, senha codificada e papéis em array.
- `resume_profiles`: currículo serializado como JSON em uma coluna de texto, identificado pelo UUID do usuário.
- Há também uma entidade `RoleEntity`; os papéis de `UserEntity` são armazenados diretamente no usuário.

O currículo contém escolaridade, habilidades e experiências. A gravação exige escolaridade e experiência completas, além de pelo menos uma habilidade. O Hibernate usa `ddl-auto=update` para atualizar o esquema.

## Limitações atuais
- O projeto contempla apenas 3 casos de uso que estabelecemos anteriormente em outra entrega, o que torna a navegação funcional incompleta.
