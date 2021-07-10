# 키친포스

## 2단계 - 서비스 리팩터링 (기능요구사항)

- [x] entity 클래스와 dto 클래스 분리
- [x] 서비스 레이어의 비즈니스 로직 domain으로 이동


## 3단계 - 의존성 리팩터링 (기능요구사항)

- [x] 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.
- [x] 함께 생성되고 함께 삭제되는 객체들을 함께 묶어라
- [x] 불변식을 지켜야 하는 객체들을 함께 묶어라
- [x] 가능하면 분리하라

## 요구 사항

1. 메뉴그룹
    - 메뉴그룹 등록
        - 메뉴그룹을 등록할수 있다.
    - 메뉴그룩 목록 출력
        - 메뉴그룹의 리스트를 확인할수 한다.
2. 메뉴
    - 메뉴생성
        - 메뉴의 가격은 필수이며 0보다 작아서는 안된다.
        - 메뉴는 하나의 메뉴 그룹에 속해 있으며 메뉴그룹이 먼저 등록되어 있어야한다.
        - 메뉴는 상품과 수량으로 구성되며 상품이 먼저 등록되어 있어야한다.
    - 메뉴출력
        - 등록한 메뉴의 리스트를 확인할수 있다.
3. 주문
    - 주문생성
        - 주문목록은 비어있으면 안된다.
        - 주문 받은 메뉴는 등록이 되어있어야한다.
        - 주문하려면 테이블의 번호가 있어야한다.
    - 주문목록출력
        - 주문한 리스트를 확인할수 있다.
    - 주문상태변경
        - 주문완료된 상태는 상태를 변경할수 없다.
        - 주문 변경시 주문에 포함된 모든 주문의 상태가 변경된다.
4. 상품
    - 상품등록
        - 상품의 가격은 필수이며 0 보다 작아서는 안된다.
    - 상품목록 출력
        - 상품 목록을 확인할수 있다.
5. 테이블그룹
    - 테이블 그룹 등록
        - 단체테이블 지정시 테이블 정보는 필수이며, 최소 2개 이상이여야 그룹화가 가능하다.
    - 테이블 그룹 해제
        - 테이블의 주문상태가 요리중이나 식사시에는 단체테이블 지정을 해제할수 없다.
6. 테이블
    - 테이블 생성
        - 빈 테이블을 생성할수 있다.
    - 테이블 정리
        - 단체지정 테이블은 정리할수 없다.
        - 요리중이거나 식사중인 테이블은 정리할수 없다.
    - 테이블 손님수 변경
        - 테이블에 착석가능한 손님은 0명이상이다.
        - 빈테이블은 인원수 변경이 불가능하다.
    
## DB 테이블 정의서
1. orders

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|id|BIGINT| | |PK|
|order_table_id|BIGINT|20| |FK|
|order_status|VARCHAR|255| | |
|ordered_time|DATETIME| | |

2. order_line_item

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|seq|BIGINT|20| |PK|
|order_id|BIGINT|20| |FK|
|menu_id|BIGINT|20| |FK|
|quantity|BIGINT|20| | |

3. menu

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|id BIGINT|BIGINT|20| |PK|
|name|VARCHAR|255| | |
|price|DECIMAL|19, 2| | |
|menu_group_id|BIGINT|20| |FK|

4. menu_group

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|id BIGINT|BIGINT|20| |PK|
|name|VARCHAR|255| | |

5. menu_product

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|seq|BIGINT|20| |PK|
|menu_id|BIGINT|20| |FK|
|product_id|BIGINT|20| |FK|
|quantity|BIGINT|20| | |

6. order_table

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|id|BIGINT|20| |PK|
|table_group_id|BIGINT|20| |FK|
|number_of_guests|INT|11| | |
|empty|BIT|1| | |

7. table_group

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|id|BIGINT|20| |PK|
|created_date|DATETIME| | | |

8. product

|컬렴명|타입|크기|Null허용|KEY|
|---|---|---|---|---|
|id|BIGINT|20| |PK|
|name|BIGINT|255| | |
|price|DECIMAL|19, 2| | |


## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
