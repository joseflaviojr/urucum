# Urucum

Classes e métodos frequentemente utilizados no desenvolvimento com linguagem Java.

Classes and methods frequently used in Java language development.

## Versão Atual

1.0-A16 (Fase de Nascimento)

Padrão de versionamento: [JFV](http://joseflavio.com/jfv)

## Uso

A Urucum está disponível como biblioteca Java no repositório [Maven](http://search.maven.org/#artifactdetails%7Ccom.joseflavio%7Curucum%7C1.0-A16%7Cjar).

Gradle:

```groovy
implementation 'com.joseflavio:urucum:1.0-A16'
```

Maven:

```xml
<dependency>
    <groupId>com.joseflavio</groupId>
    <artifactId>urucum</artifactId>
    <version>1.0-A16</version>
</dependency>
```

### Requisitos para uso

* Java >= 1.8

## Documentação

A documentação da Urucum, no formato **Javadoc**, está disponível em:

[http://joseflavio.com/urucum/javadoc](http://joseflavio.com/urucum/javadoc)

## Desenvolvimento

Configurar projeto para Eclipse IDE e IntelliJ IDEA:

```sh
gradle cleanEclipse eclipse
gradle cleanIdea idea
```

### Requisitos para desenvolvimento

* Git >= 2.8
* Java >= 1.8
* Gradle >= 4.7

## Teste

Executar os testes:

```sh
gradle clean test
```

## Distribuição

Compilar e gerar os arquivos de distribuição (JAR):

```sh
gradle clean build
```

## Publicação

Publicar os arquivos finais do projeto no repositório [Maven](http://search.maven.org/#artifactdetails%7Ccom.joseflavio%7Curucum%7C1.0-A16%7Cjar):

```sh
gradle clean publish
```
