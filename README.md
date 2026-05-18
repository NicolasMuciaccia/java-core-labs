# Java Core Labs

Um laboratório prático focado em testes, estudos e experimentações das funcionalidades nativas da linguagem Java.

## 🎯 Objetivo

O objetivo principal deste projeto é fornecer uma base de exemplos executáveis sobre as ferramentas e APIs embutidas no Java. Ele é construído em formato de testes automatizados para validar, de forma didática, o funcionamento esperado de diferentes recursos da linguagem. A ideia é sempre evoluir ele trazendo novos funcionamentos que serão estudados por mim.

## 📚 Tópicos Abordados

A estrutura do projeto é dividida por áreas de conhecimento do Java Core. Os testes exploram os seguintes tópicos:

*   **Collections Framework:** Listas (`List`), Mapas (`Map`), Conjuntos (`Set`), Filas (`Queue`/`Deque`), Ordenação (`Sorting`) e Iteração.
*   **Concorrência (Concurrency):** Sincronização de memória e fundamentos do funcionamento de Threads.
*   **Date & Time API:** Formatação de datas e manipulação de tempo utilizando o moderno pacote `java.time`.
*   **Programação Funcional:** Interfaces Funcionais, uso da classe `Optional` e a API de `Streams`.
*   **Fundamentos da Linguagem:** Mecânica da JVM, Orientação a Objetos, métodos padrão (Default Methods), palavras-chave e classes Wrapper.
*   **I/O (Input/Output):** Operações com sistemas de arquivos e fluxos de entrada/saída (Stream I/O).
*   **Metadados:** Criação e uso de Anotações (Annotations).
*   **Strings:** Manipulação de texto e análise de performance na concatenação/processamento de Strings.

## 🛠️ Tecnologias Utilizadas

*   **Java 21**
*   **JUnit 5 (Jupiter):** Framework utilizado para estruturar as experimentações como testes de unidade.
*   **Maven:** Gerenciador de dependências e build do projeto.

## 🚀 Como Executar

Para executar todos os testes e verificar o funcionamento das APIs:

1. Certifique-se de ter o **Java 21** e o **Maven** configurados em sua máquina.
2. Navegue até a pasta raiz do projeto pelo seu terminal.
3. Execute o seguinte comando:

```bash
mvn clean test
```

Você também pode importar o projeto em sua IDE favorita (IntelliJ, Eclipse, VS Code) e executar os testes individualmente por classe para estudar cada funcionalidade isoladamente.
