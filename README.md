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
위 코드를 실행시킬 시, testRepeat()함수가 5번 실행된다.

#### 2. @PrintTime
@PrintTime Annotation을 테스트 메서드에 부여하면, 테스트 수행시간이 로그에 출력되는 기능입니다.
##### 예시)
```java
    @PrintTime
    public void testPrintTime() {
        ...
    }
``` 
위 코드를 실행 시킬 시, 콘솔창에 tsetPrintTime을 실행시키는데 소요된 시간이 ms단위로 출력된다.


#### 3. @EstimateTime(time [ms])
@EstimateTime Annotaion을 테스트 메서드에 부여하면, 주어진 value값을 제한 시간 (ms단위) 을 넘어간 테스트 케이스는 Fail로 처리합니다.
##### 예시)
```java
    @EstimateTime(1000)  
    public void testEstimateTime() {
        ...
    }
``` 
위 코드를 실행 시킬 시, testEstimateTime를 실행시키는 데에 1000ms가 넘어가면 테스트가 실패처리된다.

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
위 코드를 실행시킬 시, testMethod1~3 메서드가 오름차순 순서로 실행됩니다.


#### 5. @Group, @RunGroup
테스트 메서드에 `@Group(그룹명)` 을 지정하고, 테스트 클래스에 `@RunGroup(그룹명)` 를 지정하면 
테스트 클래스를 실행시킬 때 Rungroup에 지정된 그룹명과 Group에 지정된 그룹명이 일치하는 테스트 클래스만 실행하게 됩니다.

##### 예시)
```java
    
    @RunGroup("grp1")
    public class TestCustomRunner {
        @Test
        @Group("grp1")
        public testMethod1() {
            ...
        }
        @Test
        @Group("grp1")
        public testMethod2() {
            ...
        }
        @Test
        @Group("grp2")
        public testMethod3() {
            ...
        }
   }
``` 
위 코드를 실행시킬 시, grp1에 해당하는 testMethod1과 testMethod2는 실행되고 testMethod3는 실행되지 않습니다.

#### 6. @AfterMethod, @BeforeMethod
특정 테스트 메서드가 실행되기 전, 실행되고 난 후에 실행시킬 메서드를 정의 기능입니다.
메서드의 지정은 Annotation의 파라미터에 스트링 배열 형태로 지정이 가능합니다.

##### 예시)
```java
    public class TestCustomRunner {
        @Test
        public test1() {
            ...
        }
        @Test
        public test2() {
            ...
        }
        @Test
        public test3() {
            ...
        }

        @BeforeMethod({"test1","test2"})
        public beforeMethod() {
            ...
        }
        @AfterMethod({"test2","test3"})
        public afterMethod() {
            ...
        }
   }
``` 

위 코드를 실행시킬 경우, test1과 test2 메서드는 테스트 실행 전에 beforeMethod가 먼저 호출되고, 
test2와 test3메서드는 테스트 종료 후 afterMethod가 실행됩니다.