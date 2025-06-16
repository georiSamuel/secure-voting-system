# Front-end

*(Tela de login. -Versão sujeita a mudanças.)*

![image (2).png](../../Assets/image%20%282%29.png)

#  Desenvolvimento:

A Interface Gráfica do projeto será desenvolvido utilizando principalmente o [JavaFX](https://openjfx.io/), uma plataforma de desenvolvimento que permite a criação de aplicações com interface gráfica para desktop, web e dispositivos móveis a partir da linguagem Java, que será a principal utilizada nesse projeto.

### Por que usar JavaFX?

É uma boa opção para desenvolver interfaces modernas e interativas, esses são alguns dos seus pontos positivos:

- Tem suporte a gráficos vetoriais, permitindo a criação de interfaces escaláveis e responsivas, melhorando a resolução.
- A estilização das interfaces é facilitada pelo suporte a CSS, separando a apresentação lógica e visual da aplicação.
- É didática e de fácil aprendizado, a sintaxe é semelhante à do Java e não requer um aprendizado do zero.
- Aplicações JavaFX podem ser executadas em diferentes sistemas operacionais, como Windows, MacOS e Linux

# Estrutura básica de um projeto com JavaFX:

### 1. Arquivos FXML

Os arquivos FXML são documentos XML desenvolvidos especificamente para o JavaFX, com a finalidade de descrever interfaces gráficas (UI) de forma declarativa. Eles têm uma estrutura hierárquica, sintaxe específica e vinculação com Controllers. Ele permite associar elementos a métodos e campos de classes Java através de atributos como fx:id (nome da variável) e onAction (método que será chamado).

![image (8).png](../../Assets/image%20%288%29.png)

### 2. Controllers

Os controllers são os intermediários entre a View (FXML) e o Modelo (dados). O JavaFX injeta elementos declarados com fx:id nos campos anotados com @FXML. Métodos que também são anotados com @FXML são conectados a eventos declarados, como onAction.

Geralmente, cada Controller gerencia apenas uma tela/página, por exemplo LoginController, VotaçãoController e etc.

### 3. Configuração do Maven ([pom.xml](https://maven.apache.org/pom.html))

Nesse projeto, estamos usando o Maven como ferramenta de gerenciamento, e o pom.xml é um arquivo fundamental em projetos Maven, ele controla toda a configuração do projeto. Dentro dele, estão listadas todas as bibliotecas que o projeto utiliza (dependências), define como o código será compilado, configura plugins essenciais (como compilador Java), etc.

# [Scene Builder](https://gluonhq.com/products/scene-builder/):

O Scene Builder é uma ferramenta visual para design de interfaces JavaFX. Com ela, é possível criar telas através do simples arrastar e soltar de componentes (botões, campos de texto e tabelas) em um editor gráfico, que gera automaticamente arquivos FXML integráveis ao projeto Java. A ferramenta elimina a necessidade de escrever código manualmente, oferece integração com IDEs como IntelliJ e permite visualização em tempo real das alterações. Uma vez configurado, todas as modificações realizadas no Scene Builder são automaticamente refletidas no código.

- Suas principais vantagens incluem maior produtividade, visualização em tempo real, clara separação entre interface e lógica, além de suporte a CSS.
- No arquivo FXML, é possível visualizar e editar diretamente pelo Scene Builder na própria IDE (IntelliJ), simplificando o processo de edição visual.

![image (1).png](../../Assets/image%20%281%29.png)