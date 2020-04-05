# test-junit-custom-runner

## 1. Introduction

####  IDE에서 제공되는 기본 JUnit 러너를 임의로 구현하여 JUnit테스트를 코드 레벨에서 임의로 수행 및 제어할 수 있음을 테스트 하기 위한 프로젝트 입니다.

## 2. Targets

#### Annotation을 통해 테스트 케이스에 기능을 부여하고,  JUnit Runner를 extend한 커스텀 러너가 메서드에 부여된 Annotation을 인식하여 해당 기능을 테스트시 반영하도록 러너 클래스 구현하는것을 목표로 합니다.

## 2. Environment
JDK Version : 1.8.0_161

JUnit version : 4.12

Gradle : 5.2.1

IDE : IntelliJ

## 3. Implemented Features

#### 1. @Repeat(count)
@Repeat Annotation을 테스트 메세드에 부여하면, 주어진 숫자 만큼 테스트를 반복하는 기능입니다.
##### 예시)
```java
    @Test
    @Repeat(5)
    public void testRepeat() {
        ...
    }
```