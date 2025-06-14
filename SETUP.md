# Configuração MySQL

## Pré-requisitos

- MySQL Server instalado
- MySQL Workbench instalado

## Passo 1: Conectar ao MySQL

Abra o terminal do sistema (pode ser pela própria IDE) e conecte-se ao MySQL como root:

```bash
mysql -u root -p
```

- Digite a senha que você definiu durante a instalação do MySQL
- Após conectar, você verá o prompt mudar para `mysql>`, indicando que está dentro do MySQL

## Passo 2: Criar o Banco de Dados

Execute o comando para criar o banco de dados com codificação UTF-8:

```sql
CREATE DATABASE sistemadevotacaodb_novo 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

**Explicação:**

- `CHARACTER SET utf8mb4`: Suporte completo a Unicode (incluindo emojis)
- `COLLATE utf8mb4_unicode_ci`: Ordenação padrão Unicode, insensível a maiúsculas

## Passo 3: Criar Usuário da Aplicação

Crie um usuário específico para a aplicação (sem senha, conforme application.properties):

```sql
CREATE USER 'votacao_user'@'localhost' IDENTIFIED BY '';
```

**Explicação:**

- `'votacao_user'`: Nome do usuário (deve coincidir com o application.properties)
- `@'localhost'`: Usuário só pode conectar localmente
- `IDENTIFIED BY ''`: Senha vazia (como configurado no projeto)

## Passo 4: Conceder Privilégios

Dê todas as permissões ao usuário no banco criado:

```sql
GRANT ALL PRIVILEGES ON sistemadevotacaodb_novo.* TO 'votacao_user'@'localhost';
```

**Explicação:**

- `ALL PRIVILEGES`: Todas as permissões (SELECT, INSERT, UPDATE, DELETE, CREATE, etc.)
- `sistemadevotacaodb_novo.*`: Todas as tabelas do banco específico
- Necessário para que a aplicação Spring Boot funcione corretamente

## Passo 5: Aplicar Mudanças

Execute o comando para aplicar as alterações de privilégios:

```sql
FLUSH PRIVILEGES;
```

**Explicação:**

- Força o MySQL a recarregar as tabelas de privilégios
- Garante que as permissões sejam aplicadas imediatamente

## Passo 6: Sair do MySQL

```sql
EXIT;
```

---

## Comandos Úteis (Opcionais)

### Alterar senha do usuário (se necessário):

```sql
ALTER USER 'votacao_user'@'localhost' IDENTIFIED BY 'nova_senha';
```

### Verificar usuários criados:

```sql
SELECT User, Host FROM mysql.user WHERE User = 'votacao_user';
```

### Verificar privilégios do usuário:

```sql
SHOW GRANTS FOR 'votacao_user'@'localhost';
```

---

## Verificação Final

1. **MySQL Workbench**: Abra o MySQL Workbench
2. **Nova Conexão**: Crie uma conexão com:
- Connection Name: `Sistema Votação`
- Hostname: `localhost`
- Port: `3306`
- Username: `votacao_user`
- Password: (deixe em branco)
3. **Teste**: Conecte-se e verifique se o banco `sistemadevotacaodb_novo` aparece
4. **Spring Boot**: Execute a aplicação - as tabelas serão criadas automaticamente pelo JPA/Hibernate

---

## Solução de Problemas Comuns

- **Erro de conexão**: Verifique se o MySQL Server está rodando
- **Erro de privilégio**: Execute novamente os comandos GRANT e FLUSH PRIVILEGES
- **Tabelas não aparecem**: Execute a aplicação Spring Boot uma vez para que o JPA crie as tabelas automaticamente

---

---

# Configuração JavaFX

## Visão Geral do Problema

Ao trabalhar com JavaFX em projetos Maven em qualquer IDE, você pode enfrentar dificuldades mesmo tendo as dependências e plugins corretos no `pom.xml`. Por isso, para o funcionamento básico da aplicação é necessário configurar manualmente as opções de VM e baixar o JavaFX SDK

## Por que isso acontece?

1. **JavaFX removido do JDK**: Desde o Java 11, JavaFX não está mais incluído no JDK padrão (projeto utiliza Java 21 LTS)
2. **Dependências Maven limitadas**: Apenas fornecem as classes, mas não configuram o módulo necessário para execução
3. **Conflito arquitetural**: Spring Boot não foi projetado para aplicações JavaFX clássica

---

## Soluções Disponíveis

### Opção 1: Configurar VM Options no IntelliJ

**Status**: Funciona, mas não é a solução ideal

1. Baixe o [Java FX 21.0.2](http://gluonhq.com/products/javafx/)(ou qualquer versão patch superior)
2. Configure as VM options na configuração de execução (Run/Debug configurations):

```bash
--module-path "caminho/para/seu/openjfx/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics
```

**Exemplo de caminho Windows**:

```bash
--module-path "C:\Users\SeuUsuario\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics
```

### Opção 2: Plugin JavaFX Maven

**Status**: Testado, não funcionou adequadament

Configuração no `pom.xml`:

```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>SistemaVotacaoApplication</mainClass>
        <options>
            <option>--add-modules</option>
            <option>javafx.controls,javafx.fxml,javafx.graphics</option>
        </options>
    </configuration>
</plugin>
```

**Problemas identificados**:

- As dependências continuam a não serem configuradas automaticamente

### Opção 3: JDK com JavaFX Incluso

**Status**: Solução recomendada

Utilize um JDK que já inclua JavaFX:

- **[ZuluFX](https://www.azul.com/downloads/?package=jdk-fx)** - Azul Systems
- **[LibericaFX](https://bell-sw.com/pages/downloads/#/java-21-lts)** - BellSoft

**Vantagens**:

- Elimina a necessidade de configuração manual
- Compatibilidade garantida
- Configuração simplificada

---

## Configuração Recomendada

### Para Testar a aplicação

Utilize a primeira opção por ser mais direta ao ponto

### Para Desenvolvimento Local

1. **Instale ZuluFX ou LibericaFX** (Java 21 LTS com JavaFX)
2. **Configure o projeto** para usar este JDK
3. **Mantenha as dependências** Maven normalmente: