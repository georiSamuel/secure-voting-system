# secure-voting-system

Este projeto é um sistema de votação desenvolvido na linguagem **Java**, visando criar eleições com garantia de segurança, integridade e sigilo dos votos pela aplicação técnicas de criptografia avançadas.


## Objetivos

- Votação segura e simples
- Proteção criptográfica completa dos votos e senha dos usuários
- Simulação de urna eletrônica com funcionalidades parecidas
- Auditabilidade e transparência no processo eleitoral


## Tecnologias


### **Backend & Core**
| Tecnologia | Versão | Propósito                                       |
|------------|-------|-------------------------------------------------|
| **Java** | 21 | Linguagem principal                             |
| **Spring Boot** | 3.x | Backend e injeção de dependência                |
| **Lombok** | Latest | Redução de boilerplate (código repetitivo) code |

### **Banco de Dados**
| Tecnologia | Propósito |
|------------|-----------|
| **MySQL** | Armazenamento de dados |

### **Interface Gráfica**
| Tecnologia        | Versão | Propósito                    |
|-------------------|--------|------------------------------|
| **JavaFX**        | Latest | Interface desktop nativa     |
| **JPA/Hibernate** | 21.0.2 | ORM (Object-Relational Mapping) |

### **Segurança & Criptografia**

| Algoritmo/Biblioteca | Aplicação |
| :------------------- | :--------------------------------- |
| **AES-256 (GCM)** | Criptografia da Contagem de Votos |
| **HMAC-SHA256** | Integridade do Registro de Voto |
| **BCrypt** | Hash de Senhas |

### **Ferramentas de Desenvolvimento**
| Ferramenta | Propósito |
|------------|-----------|
| **Maven** | Gerenciamento de dependências |
| **Git** | Controle de versão |
| **JUnit 5** | Testes unitários |



## Funcionalidades previstas

- Cadastro de candidatos
- Identificação do eleitor/admin
- Registro, armazenamento seguro de senhas e criptografia dos votos
- Apuração automática com verificação de integridade


## Estratégia de Segurança

### Camadas de Proteção:

1.  **Confidencialidade da Apuração (AES-256):** Protege o sigilo dos resultados parciais, criptografando a contagem de votos de cada candidato/opção diretamente no banco de dados.
2.  **Integridade e Autenticidade do Voto (HMAC-SHA256):** Garante que cada registro de voto é autêntico e não foi modificado, através de um selo criptográfico único.
3.  **Hash Seguro de Senhas (BCrypt):** Protege as credenciais de acesso dos usuários contra acesso não autorizado.


### Fluxo de Votação Segura:
**Eleitor → Autenticação → Registro do Voto com Selo HMAC → Atualização Criptografada da Contagem → Banco de Dados**


1.  **Autenticação:** O usuário (eleitor/admin) insere suas credenciais. A senha fornecida é transformada em um hash com **BCrypt** e comparada com o hash seguro armazenado no banco de dados para validar o acesso.
2.  **Registro do Voto:** Ao registrar um voto, o sistema cria um registro (`VotoModel`) contendo os detalhes da votação. Imediatamente, ele gera um "selo" de integridade para esse registro usando **HMAC-SHA256**. Esse selo é um hash que combina os dados do voto (IDs de usuário, votação, opção e o timestamp) com uma chave secreta do servidor.
3.  **Armazenamento do Voto:** O registro do voto, junto com seu selo HMAC, é salvo na tabela `tb_voto`. O selo permite verificar posteriormente se algum dado daquele registro foi alterado.
4.  **Atualização da Contagem:** Simultaneamente, o sistema incrementa a contagem de votos da `OpcaoVoto` escolhida. Antes de salvar essa nova contagem no banco de dados, o valor é criptografado usando **AES-256**. Isso impede que alguém com acesso ao banco de dados possa ver a contagem de votos em tempo real ou alterá-la facilmente.


## Passo-a-Passo para execução

0. Carregar o projeto para a IDE
1. Gerar chaves usando a classe `KeyGenerator` presente no package `Util`
2. Copiar chaves para seus respectivos campos em `application.properties`
2. Configurar o MySQL (veja em [SETUP.MD](SETUP.md))
3. Configurar JavaFX (veja em [SETUP.MD](SETUP.md))
4. Executar classe `SistemaVotacaoApplication`

- Obs: Para configurações de segurança, veja [SETUP.MD](SETUP.md)

## Autores:

- [@georiSamuel](https://github.com/georiSamuel)
- [@Horlanlacerda](https://github.com/Horlanlacerda)
- [@lethy-while](https://github.com/lethy-while)
- [@SuelleMaciel](https://github.com/SuelleMaciel)
