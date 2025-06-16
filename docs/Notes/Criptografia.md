# Criptografia


## Conceito

> Criptografia é o processo de transformar dados legíveis (plaintext) em um formato codificado (ciphertext) que só pode ser lido ou decifrado por alguém que possua a chave de criptografia adequada.
>

## Propósito:

A principal função da criptografia é proteger a confidencialidade dos dados. Ela garante que, mesmo se os dados forem interceptados ou acessados por partes não autorizadas, eles não poderão ser compreendidos sem a chave correta.

## Java

Java é uma linguagem bastante adequada para implementar criptografia, pois possui uma estrutura chamada Java Cryptography Architecture (JCA) que oferece suporte a várias operações criptográficas

---

---

# Tipos de Criptografia e Técnicas

**💫 = utilizaremos no projeto**

## 1. **Criptografia Simétrica (ou de chave secreta)💫**

Na criptografia simétrica, a mesma chave é usada tanto para criptografar quanto para descriptografar os dados. A segurança desse tipo de criptografia depende de manter a chave secreta.

### Características:

- **Chave única**: A mesma chave é usada para criptografar e descriptografar.
- **Desempenho**: Normalmente mais rápida, porque envolve apenas uma chave e um algoritmo simples.
- **Desafio**: A maior dificuldade é a troca segura da chave entre as partes. Se a chave for interceptada, a segurança é comprometida.

### Algoritmos populares:

- **AES (Advanced Encryption Standard)**: É o algoritmo simétrico mais amplamente utilizado e considerado muito seguro. AES pode usar diferentes tamanhos de chave (128, 192 e 256 bits).
- **DES (Data Encryption Standard)**: Um algoritmo mais antigo e já considerado vulnerável, principalmente devido ao seu pequeno tamanho de chave (56 bits).
- **3DES (Triple DES)**: Uma versão mais segura do DES, mas ainda assim não é recomendado em sistemas modernos.
- **RC4**: Um algoritmo de fluxo, mais antigo e agora também desaconselhado devido a vulnerabilidades conhecidas.

---

## 2. **Criptografia Assimétrica (ou de chave pública)**

Considerada mais segura do que a simétrica, pois não necessita o compartilhamento da chave que descriptografar com o receptor da mensagem

Na criptografia assimétrica, são usadas duas chaves distintas: uma **chave pública** para criptografar os dados e uma **chave privada** para descriptografar. A chave pública pode ser compartilhada abertamente, enquanto a chave privada deve ser mantida em segredo.

O pulo do 🐱: A chave pública criptografa mensagem, mas não pode descritografá-la. Apenas a chave privada revela a mensagem e ela NÃO PODE ser obtida através da chave pública. Tem uma espécie de relacionamento matemático MUITO doido

### Características:

- **Chave pública e chave privada**: Uma chave pública para criptografar e uma chave privada para descriptografar.
- **Troca segura de chaves**: Permite a comunicação segura sem a necessidade de compartilhar previamente uma chave secreta.
- **Desempenho**: Geralmente *mais lenta* que a criptografia simétrica, pois envolve cálculos mais complexos.
- **Segurança**: Caso uma das chaves privadas seja vazada, apenas as mensagens recebidas pelo usuário seriam vazadas, já que as enviadas usam outro sistema
- **Confiável**: Para quebrar uma criptografia RSA de 2048 bits seriam necessários aprox. 300 trilhões de anos

### Como funciona

![image (3).png](../../assets/image%20%283%29.png)

Segue o funcionamento genérico:

1. São geradas duas chaves (uma pública e outra provada) para cada usuário envolvido na comunicação
2. Um usuário escreve sua mensagem e criptografa ela usando a chave pública do outro usuário (já que ela tá disponível para todo mundo)
3. Essa mensagem é enviada para o outro usuário e só ele pode ler a versão original, pois possui a chave privada
4. Assim, os dois usuários se comunicam de modo provado sem precisarem compartilhar a chave privada

De um jeito mais simples📫:

Basta pensaruma caixa de correio. Qualquer pessoa sabe onde é a caixa de correio e pode colocar lá a mensagem que quiser (chave publica), porém, só o dono da caixa tem a chave (chave privada) pra abrí-la e ler as mensagens recebidas

**O mesmo se aplica ao seu endereço de email e sua senha**

### Algoritmos populares:

- **RSA**: O algoritmo de criptografia assimétrica mais conhecido e utilizado. Ele pode ser usado tanto para criptografar dados quanto para gerar assinaturas digitais.
- **ECC (Elliptic Curve Cryptography)**: Utiliza curvas elípticas para gerar chaves mais curtas, mas com o mesmo nível de segurança que o RSA. É mais eficiente em termos de processamento e uso de recursos.
- **DSA (Digital Signature Algorithm)**: Usado principalmente para assinatura digital, mas não para criptografia de dados.
- **ElGamal**: Usado em sistemas de chave pública e para criptografia de dados, bem como para assinaturas digitais.

### Usos

- HTTPS sites
- Bitcoin
- SSH pra se conectar no Linux

---

## **Hashing💫**

*Hash: Chop && Mix*

É o que você armazena no banco de dados ao invés da senha

O hashing não é uma forma de criptografia no sentido tradicional, pois o processo é **irreversível**. Ao invés de criptografar e descriptografar, o hashing gera um valor fixo (geralmente chamado de "**digest**") a partir dos dados de entrada. É utilizado para verificar a integridade dos dados ou para armazenar senhas de forma segura.
⚠️ Na criptografia padrão, com a chave a cifra (mesmo que o texto original e a chave sejam a mesmas sempre) é diferente

### Características:

- **Irreversível**: Não é possível "descriptografar" o valor gerado. O objetivo é verificar se o conteúdo original corresponde ao hash gerado.
- **Funções de hash**: É uma função que recebe uma string e converte (faz uma mistureba) num valor. O pulo do gato é que a mesma string SEMPRE retorna no mesmo valor e não da pra reverter o processo usando a hash

### Algoritmos populares:

- **SHA (Secure Hash Algorithm)**: Uma família de algoritmos de hashing, com versões mais populares como SHA-256 e SHA-512. Usado para garantir a integridade dos dados.
- **MD5 (Message Digest Algorithm 5)**: Antigo e vulnerável, não é mais recomendado devido a colisões (duas entradas gerando o mesmo hash).
- **Bcrypt💫 e Argon2**: Algoritmos de hash projetados especificamente para armazenar senhas de forma segura, com técnicas de "salting" e "key stretching" que dificultam ataques de força bruta.

---

## HMAC (Hash-based Message Authentication Code)💫

O HMAC é uma técnica que **combina hashing com uma chave secreta** para garantir tanto a **integridade** quanto a **autenticidade** dos dados. Diferente do hash simples, o HMAC usa uma chave compartilhada entre remetente e destinatário.

**Em resumo**: HMAC é uma forma de ter certeza que a mensagem veio da pessoa certa e não foi alterada por ninguém.

### Características:

- **Autenticação**: Verifica se a mensagem veio realmente de quem diz ser (possui a chave)
- **Integridade**: Detecta se os dados foram alterados durante a transmissão
- **Baseado em chave**: Sem a chave secreta, não é possível gerar ou verificar o HMAC

### Como funciona:

O HMAC aplica a função de hash duas vezes: uma com a chave misturada aos dados, e outra com a chave misturada ao resultado da primeira operação.

### Casos de uso comuns:

- **APIs REST**: Para autenticar requisições (AWS, por exemplo, usa HMAC)
- **Tokens JWT**: Muitas vezes assinados com HMAC
- **Comunicação entre sistemas**: Garantir que mensagens não foram alteradas
- **Webhooks**: Verificar se a notificação veio realmente do serviço esperado

⚠️ **Importante**: A chave secreta deve ser mantida em segredo absoluto. Se comprometida, a segurança de todo o sistema fica vulnerável.

### HMAC x Hashing

Hash comum (sem chave):

```
Mensagem → Hash → "ABC123"

```

- **Qualquer pessoa** pode pegar a mesma mensagem e gerar o mesmo hash
- Se eu sei que o hash de "Oi" é "ABC123", eu posso alterar sua mensagem para "Oi" e gerar o mesmo "ABC123"

HMAC (com chave secreta):

```
Mensagem + Chave Secreta → HMAC → "XYZ789"

```

- **Só quem tem a chave** consegue gerar o HMAC correto
- Mesmo sabendo que "Oi" + chave = "XYZ789", sem a chave eu não consigo criar novos HMAC válidos

### Analogia fácil

**Situação**: Você quer mandar um bilhete para seu amigo, mas tem medo que alguém mude o que você escreveu no caminho.

### O que você faz:

1. **Escreve a mensagem**: "Vamos jogar bola às 3h"
2. **Usa o código secreto**: Você pega sua mensagem + o código secreto e faz uma "mistureba mágica" que sempre dá o mesmo resultado
    - Mensagem + Código Secreto = **"XYZ123"** (este é o HMAC)
3. **Manda os dois**: Você envia a mensagem E o resultado da mistureba

### Do lado do seu amigo:

1. **Recebe**: "Vamos jogar bola às 3h" + "XYZ123"
2. **Faz a mesma mistureba**: Ele pega a mensagem + o código secreto que vocês combinaram
3. **Compara**: Se der "XYZ123" também, ele sabe que:
    - ✅ A mensagem veio de você (só você tem o código)
    - ✅ Ninguém mudou nada (senão daria resultado diferente)


---

## Salt & IV💫

### SALT?

**Salt** é como "tempero" para sua senha - um valor aleatório que é misturado com sua senha antes de fazer o hash.

Por que usar Salt?

**Sem Salt:**

```
Senha: "123456"
Hash: "e10adc3949ba59abbe56e057f20f883e"

```

**Problema:** Se duas pessoas usam a mesma senha "123456", elas terão o mesmo hash! Um atacante pode usar "rainbow tables" (tabelas com hashes pré-calculados) para descobrir senhas comuns.

**Com Salt:**

```
Usuário 1: "123456" + salt "abc123" = Hash diferente
Usuário 2: "123456" + salt "xyz789" = Hash totalmente diferente

```

Como funciona na prática:

```jsx
// Exemplo conceitual
senha = "minhasenha"
salt = gerarSaltAleatorio() // Ex: "k3n7x9m2"
senhaComSalt = senha + salt  // "minhasenhak3n7x9m2"
hashFinal = hash(senhaComSalt)

// Salvar no banco:
// usuario_id | salt     | hash_senha
// 1         | k3n7x9m2 | a7b8c9d1e2f3...

```

### O que é IV (Initialization Vector)?

**IV** é um valor aleatório usado para "embaralhar" o início da criptografia, mesmo quando você criptografa o mesmo texto várias vezes.

Por que usar IV?

**Sem IV:**

```
Texto: "Olá mundo"
Chave: "minhaChaveSecreta"
Resultado: Sempre o mesmo texto criptografado

```

**Com IV:**

```
Texto: "Olá mundo"
Chave: "minhaChaveSecreta"
IV 1: "abc123" → Resultado criptografado X
IV 2: "xyz789" → Resultado criptografado Y (diferente!)

```

### Como Salt e IV são usados no AES?

1. 1. SALT no AES

O salt não é usado diretamente no AES, mas sim para **derivar a chave** a partir de uma senha:

```jsx
// Processo típico:
senha = "minhasenha"
salt = gerarSaltAleatorio()
chave = PBKDF2(senha, salt, iterações) // Deriva uma chave forte
// Agora usa essa chave no AES

```

2. IV no AES

O IV é usado diretamente na criptografia AES:

```jsx
// Criptografia
texto = "Dados secretos"
chave = "chave256bits..."
iv = gerarIVAleatorio() // 16 bytes para AES
textoCriptografado = AES_encrypt(texto, chave, iv)

// Para descriptografar, você precisa do mesmo IV
textoOriginal = AES_decrypt(textoCriptografado, chave, iv)

```

### Exemplo Prático Completo

Imagine um sistema de login:

1. Cadastro do usuário:

```
1. Usuário digita senha: "minhaSenha123"
2. Sistema gera salt aleatório: "k8x9m2n7"
3. Combina: "minhaSenha123k8x9m2n7"
4. Faz hash: SHA256(senha+salt)
5. Salva no banco: {salt: "k8x9m2n7", hash: "a1b2c3d4..."}

```

2. Login do usuário:

```
1. Usuário digita: "minhaSenha123"
2. Sistema pega o salt do banco: "k8x9m2n7"
3. Combina: "minhaSenha123k8x9m2n7"
4. Faz hash e compara com o hash salvo
5. Se igual = senha correta!

```

3. Criptografar dados sensíveis:

```
1. Dados: "Número do cartão: 1234-5678"
2. Deriva chave da senha do usuário + salt
3. Gera IV aleatório: "p9q8r7s6..."
4. Criptografa com AES: AES(dados, chave, iv)
5. Salva: {dados_criptografados, iv}

```

### Resumo Simples

- **SALT**: Evita que senhas iguais tenham hashes iguais
- **IV**: Evita que textos iguais tenham criptografia igual
- **Ambos são aleatórios** e tornam tudo mais seguro
- **Salt** vai com hash de senhas
- **IV** vai com criptografia AES
- **Ambos devem ser únicos** para cada operação

**Analogia:**

- Salt é como usar temperos diferentes na mesma receita
- IV é como usar ingredientes em ordens diferentes - mesmo prato, sabores únicos!

---

---

# Como escolhi prosseguir

O nosso projeto emprega uma abordagem de defesa em camadas, utilizando diferentes técnicas criptográficas para proteger componentes distintos do sistema, desde os dados armazenados no banco de dados até as credenciais dos usuários.
Com base nas classes **AES, HMAC, VoteCountEncryptor e SecurityConfig**, exploramos aqui os três principais mecanismos de segurança implementados.

---

## 1.Protegendo os Dados em Repouso: Criptografia Simétrica com AES-GCM

Para garantir que informações sensíveis, como a contagem de votos, permaneçam confidenciais mesmo que o banco de dados seja comprometido, utilizamos a criptografia simétrica.
A classe `VoteCountEncryptor` atua como um ‘conversor’ JPA que intercepta os dados antes de serem salvos e depois de serem lidos do banco. Ela utiliza a classe utilitária AES para realizar a criptografia e descriptografia, seguindo um padrão moderno e seguro:

- Algoritmo: AES (Advanced Encryption Standard), um padrão global para criptografia de dados.
- Modo de Operação: GCM (Galois/Counter Mode). Este modo é crucial porque não apenas cifra os dados (garantindo confidencialidade), mas também gera uma tag de autenticação. Essa tag assegura que os dados não foram alterados ou corrompidos (integridade).
- Vetor de Inicialização (IV): Para cada operação de criptografia, um IV aleatório de 12 bytes é gerado. Isso garante que a criptografia do mesmo dado (por exemplo, o número 100) resulte em um texto cifrado diferente a cada vez, tornando ataques mais difíceis. O IV é então prefixado ao dado cifrado para ser usado na descriptografia.
  Em resumo, a contagem de votos nunca é armazenada como um número simples no banco de dados. Em vez disso, é guardada como um bloco de bytes criptografados que só pode ser lido pela aplicação com a posse da chave mestra (voting.app.aes-key).

---

## 2.Garantindo a Integridade do Voto: HMAC-SHA256

Além de proteger os dados em repouso, é vital garantir que cada "evento de voto" seja autêntico e não tenha sido forjado ou alterado. Para isso, utilizamos um Código de Autenticação de Mensagem baseado em Hash (HMAC).
A classe `HMAC` implementa essa funcionalidade:

- Algoritmo: HmacSHA256. Ele combina o algoritmo de hash SHA-256 com uma chave secreta (voting.app.hmac-secret).
- Funcionamento: Um HMAC pode ser visto como uma "impressão digital" ou um "selo de autenticidade" para um conjunto de dados (como ID do eleitor, ID da pauta e o horário do voto). Ao contrário de um hash simples, um HMAC só pode ser gerado ou verificado por quem possui a chave secreta.
  Isso cria um registro de integridade para cada voto, permitindo verificar posteriormente que os dados daquele voto são exatamente os mesmos que foram registrados no momento da sua criação, garantindo sua autenticidade.

---

## 3.Armazenamento Seguro de Credenciais: Hashing com BCrypt

Proteger as senhas dos usuários requer uma abordagem diferente da criptografia de dados. Senhas nunca devem ser descriptografáveis, nem mesmo pela aplicação. Para isso, usamos o hashing de senhas.
A classe `SecurityConfig` configura o Spring Security para usar `BCryptPasswordEncoder`. BCrypt é o padrão da indústria para essa finalidade por três motivos principais:

- É um Hashing de Mão Única: Uma senha é transformada em um hash, mas o processo não pode ser revertido. Para verificar uma senha, a aplicação executa o mesmo processo de hash na senha fornecida e compara o resultado com o hash armazenado.
- Usa "Sal" (Salt): BCrypt automaticamente adiciona um valor aleatório (o "sal") a cada senha antes de gerar o hash. Isso significa que mesmo que dois usuários tenham a mesma senha, os hashes armazenados no banco de dados serão completamente diferentes, protegendo contra ataques de rainbow table.
- É Lento Propositalmente: O algoritmo é computacionalmente caro (lento) por design. Isso torna ataques de força bruta, onde um atacante tenta adivinhar senhas gerando bilhões de hashes por segundo, inviáveis.
  Essa configuração, centralizada em SecurityConfig, garante que as credenciais dos usuários estejam protegidas com o mais alto nível de segurança.
  Geração e Gestão das Chaves
  A força de todos esses sistemas criptográficos depende da segurança de suas chaves. A classe KeyGenerator é responsável por criar chaves fortes e aleatórias de 256 bits usando SecureRandom. Essas chaves são então externalizadas para o arquivo application.properties, separando o código-fonte dos segredos da aplicação, uma prática de segurança essencial.

---

## Geração e Gestão de chaves de chaves

A força de todos esses sistemas criptográficos depende da segurança de suas chaves. A classe `KeyGenerator` é responsável por criar chaves fortes e aleatórias de 256 bits usando SecureRandom que, pra maior segurança, serão armazenadas como variáveis de ambiente na IDE ou Terminal em que a aplicação está rodando

---

---

# Bibliotecas utilizadas

Nosso projeto utiliza um conjunto de bibliotecas e APIs robustas para construir uma aplicação segura e funcional.

## 1.Java Cryptography Architecture (JCA/JCE)

A base para todas as operações criptográficas. É a API padrão do Java para segurança.

- Pacotes: javax.crypto.*, java.security.*
- Uso: Implementação de criptografia com AES-GCM (Cipher), geração de códigos de autenticação com HMAC-SHA256 (Mac), criação de chaves (SecretKeySpec) e geração de números aleatórios seguros (SecureRandom).

## 2.Spring Framework & Spring Security

O ecossistema Spring é a espinha dorsal da aplicação, gerenciando componentes, configurações e a segurança web.

- Pacotes: org.springframework.*
- Uso:
    - Spring Security: Para configurar regras de acesso HTTP (HttpSecurity), proteger endpoints e, crucialmente, para o hashing de senhas com BCryptPasswordEncoder.
    - Spring Core: Para injeção de dependências e configuração (@Configuration, @Component, @Bean, @Value).

## 3.Jakarta Persistence API (JPA)

A especificação padrão para mapeamento objeto-relacional (ORM), permitindo a integração transparente da lógica de criptografia com o banco de dados.

- Pacotes: jakarta.persistence.*
- Uso: A interface AttributeConverter é implementada pela classe VoteCountEncryptor para criptografar e descriptografar dados de entidades automaticamente.

## 4.Bibliotecas Padrão do Java

Recursos utilitários essenciais do próprio Java

- Pacotes: java.util.Base64, java.nio.ByteBuffer
- Uso: Base64 para codificar chaves e saídas criptográficas em formato de texto, e ByteBuffer para manipulação eficiente de arrays de bytes.