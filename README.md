# Custom JUnit Runner for IntelliJ

## 1. Introduction

####  IntelliJ 에서 제공되는 기본 JUnit 러너를 임의로 구현하여 JUnit테스트를 코드 레벨에서 임의로 수행 및 제어할 수 있음을 테스트 하기 위한 프로젝트 입니다.

## 2. Targets

#### Annotation을 통해 테스트 케이스에 기능을 부여하고,  JUnit Runner를 extend한 커스텀 러너가 메서드에 부여된 Annotation을 인식하여 해당 기능을 테스트시 반영하도록 러너 클래스 구현하는것을 목표로 합니다.

## 2. Environment
JDK Version : 1.8.0_161

JUnit version : 4.12

Gradle : 5.2.1

IDE : IntelliJ

## 3. Implemented Features

#### 1. @Repeat(count)
@Repeat Annotation을 테스트 메서드에 부여하면, 주어진 숫자 만큼 테스트를 반복하는 기능입니다.
##### 예시)
```java
    @Test
    @Repeat(5)
    public void testRepeat() {
        ...
    }
```

#### 2. @PrintTime
@PrintTime Annotation을 테스트 메서드에 부여하면, 테스트 수행시간이 로그에 출력되는 기능입니다.
##### 예시)
```java
    @PrintTime
    public void testPrintTime() {
        ...
    }
``` 


#### 3. @EstimateTime(time [ms])
@EstimateTime Annotaion을 테스트 메서드에 부여하면, 주어진 value값을 제한 시간 (ms단위) 을 넘어간 테스트 케이스는 Fail로 처리합니다.
##### 예시)
```java
    @EstimateTime(1000)  
    public void testEstimateTime() {
        ...
    }
``` 


#### 4. @TestOrder
@TestOrder(OrderType.ASCEND/DESCEND/DEFAULT) 형태로 클래스에 Annotation을 부여하면, 대상 테스트 클래스에 존재하는 메서드는 부여된 정렬 순서에 따라 정렬되어 실행된다. 
부여 가능한 옵션은 아래의 세 가지 입니다.

- OrderType.ASCEND : 오름차순
- OrderType.DESCEND : 내림차순
- OrderType.DEFAULT : 정렬하지 않음  
만일 별도로 지정되지 않을 경우, 디폴트 값으로 OrderType.DEFAULT가 사용됩니다.

##### 예시)
```java
    
    @TestOrder(TestOrder.OrderType.ASCEND)
    public class TestCustomRunner {
        @Test
        public testMethod2() {
            ...
        }
        @Test
        public testMethod3() {
            ...
        }
        @Test
        public testMethod1() {
            ...
        }
   }
``` 


