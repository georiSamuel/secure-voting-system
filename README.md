# secure-voting-system

Este projeto é um sistema de votação desenvolvido na linguagem **Java**, visando criar eleições com garantia de segurança, integridade e sigilo dos votos pela aplicação técnicas de criptografia avançadas.

---
## Objetivos

- Votação segura e simples para eleitores 
- Proteção criptográfica completa dos dados 
- Simulação de urna eletrônica com funcionalidades oficiais 
- Auditabilidade e transparência no processo eleitoral

---
## Tecnologias


### **Backend & Core**
| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| **Java** | 21+ | Linguagem principal |
| **Spring Boot** | 3.x | Framework web e injeção de dependência |
| **Lombok** | Latest | Redução de boilerplate code |

### **Banco de Dados**
| Tecnologia | Propósito |
|------------|-----------|
| **MySQL** | Armazenamento de dados |

### **Interface Gráfica**
| Tecnologia | Propósito |
|------------|-----------|
| **JavaFX** | Interface desktop nativa |

### **Segurança & Criptografia**
| Algoritmo/Biblioteca | Aplicação | Características                              |
|-----------------------|-----------|----------------------------------------------|
| **RSA** | Criptografia assimétrica | Criptografia dos votos, assinaturas digitais |
| **AES-256** | Criptografia simétrica | Criptografia da chave privada                |
| **SHA-256** | Hash criptográfico | Assinaturas digitais                         |
| **BCrypt** | Hash de senhas | Armazenamento seguro de credenciais          |
| **PBKDF2** | Derivação de chaves | Geração de chaves a partir de senhas         |

### **Ferramentas de Desenvolvimento**
| Ferramenta | Propósito |
|------------|-----------|
| **Maven** | Gerenciamento de dependências |
| **Git** | Controle de versão |
| **JUnit 5** | Testes unitários |


---
## Funcionalidades previstas

- Cadastro de candidatos
- Identificação do eleitor
- Registro, armazenamento seguro de senhas e criptografia dos votos
- Apuração automática com verificação de integridade
- Exportação de resultados

---
## Estratégia de Segurança

### Camadas de Proteção:

1. Criptografia Híbrida (RSA + AES)
2. Assinatura digital (SHA-256)
3. Hash Seguro de Senhas (BCrypt)
4. Integridade de Dados (SHA-256)

### Fluxo de Votação Segura:
Eleitor → Autenticação (BCrypt) → Voto (RSA) → Assinatura (SHA-256) → Banco de dados


---
## Autores:

- [@georiSamuel](https://github.com/georiSamuel)
- [@Horlanlacerda](https://github.com/Horlanlacerda)
- [@lethy-while](https://github.com/lethy-while)
- [@SuelleMaciel](https://github.com/SuelleMaciel)

