# Banco de Dados

## Introdução

### Conceito

Um **repositório de dados**, ou simplesmente banco de dados, é um sistema estruturado utilizado para armazenar, organizar, gerenciar e recuperar informações digitais de forma eficiente e segura.

Quando se fala em banco de dados, existem dois tipos principais:

- **Banco de Dados Relacional**: dados bem organizados em tabelas e com regras rígidas
- **Banco de Dados não Relacional**: dados mais flexíveis escalados horizontalmente

## MySQL

No projeto em questão, será utilizado o **MySQL**, um banco de dados relacional que organiza as informações em tabelas compostas por linhas e colunas. A linguagem utilizada para manipulação desses dados é a **SQL (Structured Query Language)**, amplamente empregada para realizar operações como:

- Inserção
- Consulta
- Atualização
- Exclusão de registros

## Ferramentas de Desenvolvimento

### XAMPP e MySQL Workbench

**XAMPP** é um pacote que instala um ambiente de desenvolvimento completo no computador, incluindo:
- Servidor web Apache
- Banco de dados MySQL
- Outras ferramentas

Ele permite rodar o MySQL localmente, ou seja, no próprio computador, sem precisar de um servidor externo.

**MySQL Workbench** é uma ferramenta gráfica usada para:
- Modelar bancos de dados
- Gerenciar bancos de dados
- Executar instruções SQL

Para que o Workbench funcione, é necessário que o servidor MySQL esteja em execução. O XAMPP fornece esse servidor já configurado de forma simples, bastando apenas iniciar o módulo MySQL pelo painel XAMPP.

### MySQL x Spring Boot

Visando uma maior praticidade, automação e integração entre as ferramentas, os comandos SQL serão executados dentro do Spring Boot, por meio da **JPA (Java Persistence API)** e do **Hibernate**, sem a necessidade de escrever SQL manualmente.

## Configuração e Integração

### MySQL com Spring Boot

Para que o MySQL funcione corretamente dentro do Spring Boot, é necessário:

1. **Adicionar a dependência do MySQL Driver** no projeto
    - Pode ser feito diretamente pelo Spring Initializr
    - Ou manualmente no arquivo `pom.xml` (projetos Maven)


![image (4).png](../../assets/image%20%284%29.png)


2. **Configurar os dados de conexão** no arquivo `application.properties` (localizado em `src/main/resources`)

### Parâmetros de Configuração do Banco de Dados

```properties
# URL do banco
spring.datasource.url

# Usuário
spring.datasource.username

# Senha
spring.datasource.password

# Driver JDBC (tradutor entre Java e BD)
spring.datasource.driver-class-name

# Exibir comandos SQL no console
spring.jpa.show-sql
```

### JPA e Hibernate

Além das configurações do MySQL, outra ferramenta essencial na integração com o banco de dados é a **JPA (Java Persistence API)**, utilizada em conjunto com o **Hibernate**.

#### JPA (Java Persistence API)
- Define um conjunto de regras e padrões para o mapeamento objeto-relacional (ORM)
- Especifica como os objetos Java devem ser armazenados em tabelas de bancos relacionais
- Por si só não executa nada — é apenas a especificação

#### Hibernate
- Responsável por executar as regras da JPA
- Utiliza as anotações definidas pela JPA (`@Entity`, `@Id`, etc.)
- Converte objetos Java em registros de banco de dados (e vice-versa)
- Gera automaticamente os comandos SQL
- Gerencia a conexão com o banco


![image (5).png](../../assets/image%20%285%29.png)



### Configurações JPA-Hibernate no Spring Boot

```properties
# Criação automática das tabelas do BD
spring.jpa.hibernate.ddl-auto=create

# Formatação visual do SQL
spring.jpa.properties.hibernate.format_sql=true
```

## Arquitetura do Projeto

### Estrutura das Classes Spring Boot

No projeto, cada módulo funcional foi estruturado como uma pasta, contendo os principais pacotes da arquitetura em camadas:

- **controller**
- **dto**
- **model**
- **repository**
- **service**

Para integração com o banco de dados, as classes mais fundamentais são:

- **model**: representa as entidades mapeadas para as tabelas do banco
- **repository**: responsável pelo acesso e manipulação dos dados via JPA/Hibernate

### 1. Pacote Model

O pacote **model** contém as entidades do banco de dados - classes Java que representam tabelas no banco relacional.

![image (6).png](../../assets/image%20%286%29.png)

#### Anotações Principais

- **`@Entity`**: informa ao JPA/Hibernate que a classe deve ser mapeada como entidade persistente
- **`@Table(name = "nome_da_tabela")`**: define explicitamente o nome da tabela (opcional)
- **`@Id`**: indica o campo que será a chave primária
- **`@GeneratedValue`**: define a estratégia de geração automática da chave primária
- **`@Column`**: personaliza propriedades da coluna (nome, tipo, tamanho, restrições)

Essas anotações permitem ao Hibernate mapear automaticamente os atributos da classe Java para colunas da tabela no banco de dados.

### 2. Pacote Repository

O pacote **repository** contém as interfaces responsáveis por intermediar o acesso ao banco de dados, conectando o Spring Data JPA às entidades da aplicação.

#### Características

- **Estendem a interface `JpaRepository`**
- **Disponibilizam automaticamente métodos CRUD**:
    - **Create** (criar)
    - **Read** (ler)
    - **Update** (atualizar)
    - **Delete** (excluir)
- **Não requerem comandos SQL manuais**

#### Funcionamento

Em segundo plano, o **Hibernate** atua como implementação do JPA:

- Traduz operações em comandos SQL compatíveis com o banco
- Gerencia o mapeamento objeto-relacional (ORM)
- Converte classes Java em tabelas do banco
- Identifica classes anotadas com `@Entity`
- Mapeia entidades para tabelas
- Fornece métodos eficientes para manipulação de dados

Com essa integração, o Spring Boot permite que os repositórios forneçam de forma simples e eficiente os métodos para salvar, buscar, atualizar e deletar dados diretamente no banco.

![image (7).png](../../assets/image%20%287%29.png)