# Casos de teste — user-service

## Escopo e situação

Inventário baseado no código de src/test. **Os testes não foram executados nesta atualização de documentação**; nenhum caso está declarado aprovado. A existência de uma asserção não comprova sua aprovação no ambiente atual.

Os testes unitários usam Mockito. AuthIntegrationTest usa MockMvc, contexto Spring, perfil test e H2 em memória. UserServiceApplicationTests não declara perfil; requer configuração válida para carregar o contexto.

## Casos automatizados existentes (5)

| ID | Origem | Pré-condição | Ação | Resultado verificado pelo código |
| --- | --- | --- | --- | --- |
| 1 | [AuthIntegrationTest.shouldRegisterNewUserSuccessfully](https://github.com/grupo-do-alef/user-service/blob/main/src/test/java/com/github/ggarabs/user_service/adapter/in/AuthIntegrationTest.java) | Contexto Spring com perfil test e banco H2. | POST /api/register com conta nova. | HTTP 201 e nome/e-mail corretos no JSON. |
| 2 | [AuthIntegrationTest.shouldReturnConflictWhenRegisteringDuplicateEmail](https://github.com/grupo-do-alef/user-service/blob/main/src/test/java/com/github/ggarabs/user_service/adapter/in/AuthIntegrationTest.java) | Contexto Spring com perfil test e banco H2. | Enviar duas vezes o mesmo cadastro. | Primeiro HTTP 201; segundo HTTP 409 e mensagem E-mail já cadastrado. |
| 3 | [RegisterUserUseCaseTest.shouldRegisterUserSuccessfullyWhenEmailIsUnique](https://github.com/grupo-do-alef/user-service/blob/main/src/test/java/com/github/ggarabs/user_service/application/usecase/RegisterUserUseCaseTest.java) | E-mail ainda não cadastrado; repositório e encoder simulados. | Executar cadastro com nome, e-mail, senha e papéis. | Retorna nome/e-mail informados e chama save. |
| 4 | [RegisterUserUseCaseTest.shouldThrowExceptionWhenEmailAlreadyExists](https://github.com/grupo-do-alef/user-service/blob/main/src/test/java/com/github/ggarabs/user_service/application/usecase/RegisterUserUseCaseTest.java) | Repositório simula e-mail existente. | Repetir cadastro com o mesmo e-mail. | Lança EmailAlreadyExistsException e não chama save. |
| 5 | [UserServiceApplicationTests.contextLoads](https://github.com/grupo-do-alef/user-service/blob/main/src/test/java/com/github/ggarabs/user_service/UserServiceApplicationTests.java) | Configuração necessária ao contexto disponível. | Carregar o contexto Spring. | Inicialização sem exceção; não valida um fluxo de negócio. |

## Como executar

Use JDK 21 e o Maven Wrapper. No PowerShell, na raiz do repositorio:

~~~powershell
.\mvnw.cmd '-Dspring.profiles.active=test' test
~~~

O comando ativa o perfil test com H2 e chave JWT de teste; não utiliza o PostgreSQL de produção. Os relatórios Maven ficam em target/surefire-reports. Em Linux/macOS, use ./mvnw.

## Casos manuais complementares — não executados

Estes cenários são propostas de validação e não testes automatizados existentes.

| ID | Pré-condição | Passos | Resultado esperado |
| --- | --- | --- | --- |
| M1 | Conta cadastrada. | Fazer login com senha correta e depois incorreta. | Login válido retorna token; credenciais incorretas são recusadas. |
| M2 | Token válido. | Salvar currículo completo e consultar novamente. | Dados persistidos e retornados para o próprio usuário. |
| M3 | Token válido. | Salvar currículo sem habilidades ou com itens obrigatórios incompletos. | HTTP 400 e ausência de gravação do currículo inválido. |
| M4 | Token ausente, inválido ou expirado. | Consultar /users/me/profile. | Acesso recusado. |


