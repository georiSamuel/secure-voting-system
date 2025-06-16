# Back-end

#  Linha de Pensamento Geral

No desenvolvimento do backend, segmentei o sistema em três áreas: **Usuário**, **Votação** e **Voto**. Cada uma com suas responsabilidades, garantindo clareza, segurança e escalabilidade.

---

##  Ferramentas e Como Usei

###  Usuário

- **JPA / Hibernate**: Para mapear a entidade Usuário no banco de dados, definindo campos como id, nome, email, senha (com hash), tipo de usuário e flags de controle.
- **Validações (javax.validation)**: Assegurei que dados obrigatórios fossem validados (ex: email único, CPF válido).
- **Segurança (BCryptPasswordEncoder)**: As senhas nunca são armazenadas em texto puro — sempre protegidas com hash forte.
- **Controle de acesso**: Defini tipos de usuário (ADMIN, COMUM) com enum para controlar permissões.
- **Auditoria**: Registro automático de data de criação para acompanhar o histórico.

###  Votação

- **Modelagem clara**: A entidade Votação guarda informações do processo eleitoral, como tipo, status e datas.
- **Relacionamentos JPA**: Associei usuários às votações para controlar quem pode votar em cada eleição.
- **Regras de negócio**: Implementei lógica para abertura, fechamento e validação da votação (ex: só pode votar se estiver ativa).

###  Voto

- **Persistência dos votos**: Cada voto é uma entidade que referencia o usuário e a votação, garantindo integridade.
- **Controle de unicidade**: Evito votos duplicados com flags e validações no banco.
- **Auditoria e segurança**: Registro data/hora do voto para garantir transparência.

---

## ⚙ Tecnologias de Apoio

- **Spring Data JPA**: Facilita CRUD e consultas complexas com métodos declarativos.
- **Spring Security**: Para autenticação, autorização e proteção de endpoints.
- **Banco de Dados Relacional (MySQL/PostgreSQL)**: Com índices nos campos usados nas buscas para performance.
- **Maven**: Gerenciamento de dependências e build do projeto.
- **Lombok (opcional)**: Para reduzir boilerplate(trecho de código repetitivo), embora no projeto atual não tenha sido utilizado por preferência.

---

##  Segurança e Práticas

- Senhas armazenadas com hash bcrypt
- Validações rigorosas para evitar dados inválidos ou duplicados
- Uso de `Optional` para tratamento seguro de dados que podem não existir
- Camada de serviço isolando lógica de negócio da persistência
- Tratamento de exceções personalizadas para feedback claro
- Transações para garantir atomicidade em operações críticas

---

##  Explicando as pastas e a importância de cada

### 🔹 Votação

**Função:**

Ela representa um processo de votação no sistema.

**Responsabilidades:**

- Determinar quando a votação está ativa ou encerrada
- Relacionar com as opções (`OpcaoVoto`) disponíveis
- Relacionar com os votos registrados (`Voto`)
- Servir como base para validações (ex: só deixar votar se a votação estiver ativa)

**Importância:**

Sem essa entidade, não há evento de votação. É o ponto de partida pra todo o processo.

### 🔹 Voto

**Função:**

Registra a escolha de um usuário em uma opção dentro de uma determinada votação.

**Responsabilidades:**

- Garantir que o voto foi feito por um usuário
- Indicar em qual opção e em qual votação
- Armazenar de forma sigilosa (com criptografia)
- Impedir que o mesmo usuário vote mais de uma vez

**Importância:**

Sem isso, não dá pra rastrear ou contar os votos corretamente. Essa classe representa a ação principal do sistema: votar.

### 🔹 Usuário

**Função:**

Representa a pessoa que interage com o sistema, geralmente quem vai votar ou administrar votações.

**Responsabilidades:**

- Identificação/autenticação de quem acessa o sistema
- Limitar quem pode votar e onde
- (Se for admin) permitir criação de votações, opções, etc.

**Importância:**

É o vínculo entre o mundo real e o sistema. Controla quem tem acesso, quem já votou, e quem pode fazer o quê.

**🔹 Separação por Responsabilidade (SRP - Princípio da Responsabilidade Única)**
- O pacote `votacao` trata da estrutura e regras da eleição ou processo de votação (ex: título, período, tipo de votação, cargo, segundo turno).
- O pacote `voto` trata do ato de votar em si (quem votou, em quem votou, se foi branco, qual opção, etc.).

Mesmo que estejam relacionados, são responsabilidades diferentes:
- `Votacao` existe sem precisar saber como os votos são armazenados internamente.
- `Voto` depende da votação, mas pode ser reutilizado em diversos contextos (eleição, enquete, votação personalizada...).

**🔹 Analogias reais e estruturais**

Votação é como um evento ou urna.
Voto é o que as pessoas colocam dentro de uma urna.