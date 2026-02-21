# DemoRestAssuredPosUninassau
Este é um projeto de exemplo de automação de testes de API utilizando o framework RestAssured e o Allure para geração de report. 
A API utlizada para os testes foi a [ServRest](https://serverest.dev/), API pública disponível na internet para fins de estudo e práticas.

## Pre-requisitos
```
IntelliJ
Java 21
RestAssured
Allure
JUnit 5
```

## Execução dos Testes
Os testes foram desenvolvidos em Java e se encontram na pasta test do projeto. Foi desenvolvida também uma validação de schema, o arquivo json do schema se encontra na pasta resources
```
- Execute os testes a partir do menu fazendo click direito em cima do arquivo de teste
- Para Utilização do Allure, siga os seguintes passoÇ
  - 1. instale o nodeJs (https://nodejs.org/)
  - 2. execute o comando: ** npm install -g allure **
  - 3. verifique se a instalação foi bem sucedida: ** allure --version **
- Após executar o teste, abra o terminal na propria IDE e execute o comando ** allure serve **, na pasta principal do projeto, para visualizar o relatorio dos testes
```

## Links importantes
```
- RestAssured -> https://rest-assured.io/
- Allure -> https://allurereport.org/
- Gerador de schema -> https://www.liquid-technologies.com/online-json-to-schema-converter
```
