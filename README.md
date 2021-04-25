# AI photoclip

인공지능 사진 자동보정 프로그램 'AI PhotoClip'



<img src = "https://user-images.githubusercontent.com/73948775/115994162-caf9fb00-a610-11eb-8229-4d1aec27160f.jpg" width="250px">


```sh
[개발동기]
사진을 3년 이상 공부해왔고 많은 보정 작업을 해보았습니다.
앱을 개발하면서 제가 잘 알고있는 사진과 엮어보고 싶었습니다.
저만의 보정 노하우를 적용한 앱을 개발하여,
보정에 대한 지식이 없는 사람들도 쉽게 사용할 수 있도록 합니다.
```

<br/>


## 메인화면

<img src = "https://user-images.githubusercontent.com/73948775/115990664-68e4ca00-a5ff-11eb-98f4-7ca4d48f807f.jpg" width="250px">

일반적인 사진 보정 애플리케이션의 UI처럼 구성하였다.
화면에는 사진을 띄울 수 있으며, 하단 RecyclerView를 이용해 필터를 넘기고 선택할 수 있다.
사진은 '직접촬영' 및 '저장된 사진 불러오기' 두가지 기능을 지원한다.

<br/>

<br/>


## 기능

### 인공지능 사진 자동보정

<img src = "https://user-images.githubusercontent.com/73948775/115992446-c3ceef00-a608-11eb-8b41-6b9c1dfb3218.gif" width="250px">

<img src = "https://user-images.githubusercontent.com/73948775/115993808-6c804d00-a60f-11eb-96ad-58edb6557d0c.png" width="100px">

- 네이버 클라우드 플랫폼의 AI Service 'Object Detection API'를 활용하여 개발하였다.
- 해당 객체인식 서비스는 MS COCO 80개 클래스를 JSON으로 반환해준다.
아래는 MS COCO 80개 클래스이다.


```sh
사람, 자전거, 자동차, 오토바이, 비행기, 버스, 기차, 트럭, 보트,교통 신호등,
소화전, 정지 신호, 주차 미터, 벤치, 새, 고양이, 개, 말, 양, 암소, 코끼리,
곰, 얼룩말, 기린, 책가방, 우산, 핸드백, 넥타이, 가방, 프리스비, 스키, 스노보드,
스포츠 볼, 연, 야구 방망이, 야구 글러브, 스케이트 보드, 서핑 보드, 테니스 라켓,
병, 와인 글라스, 컵, 포크, 나이프, 숟가락, 그릇, 바나나, 애플, 샌드위치, 주황색,
브로콜리, 당근, 핫도그, 피자, 도넛, 케이크, 의자, 소파, 화분, 침대, 식사테이블,
화장실, TV 모니터, 노트북, 마우스, 원격, 키보드, 휴대 전화, 전자 레인지, 오븐,
토스터기, 싱크대, 냉장고, 도서, 시계, 꽃병, 가위, 테디베어, 헤어드라이어, 칫솔
```

<br/>

### 알고리즘
1. 사진 업로드
2. NAVER CLOUD PLATFORM 서버로 사진 전송
3. Object Detection(객체인식)후 객체값 JSON으로 반환
4. 반환된 JSON 값에 따라 사진 필터 적용

위의 알고리즘에 따라 사진이 자동보정된다.
JSON과 필터 매칭은 나의 사진보정 노하우에 따라 지정하였다.

<br/>

### 객체 우선순위
- 여러개의 다른 객체가 인식되었을 때 필터간 충돌이 일어나는 것을 방지하기 위해 객체별 우선순위를 두었다.
- 예를들어 사람과 자전거가 인식되면 인물을 찍는다고 인식할 수 있도록 사람을 우선순위로 두었다.
- 객체 우선순위는 LankManager 클래스에서 확인할 수 있다.
- 객체 우선순위는 나의 사진보정 노하우에 따라 지정하였다.

#### 예시
```sh
    private void setLankGroup1() { //객체인식 운선순위 지정
        mCoCoMap.put("person", new MsCoCo("person", "사람", 1,4));
        mCoCoMap.put("bicycle", new MsCoCo("bicycle", "자전거", 2,3));
        mCoCoMap.put("car", new MsCoCo("car", "자동차", 3,3));
        mCoCoMap.put("motorcycle", new MsCoCo("motorcycle", "오토바이", 4,3));
        mCoCoMap.put("bird", new MsCoCo("bird", "새", 15,8));
        mCoCoMap.put("cat", new MsCoCo("cat", "고양이", 16,8));
        mCoCoMap.put("dog", new MsCoCo("dog", "개", 17,8));
        .....
    }
```



<br/>
<br/>





### 문의

  - 메일 : haruple97@naver.com
  - 카카오톡 : 946837
  - 블로그 : https://haruple.tistory.com/
  - 유튜브 : https://www.youtube.com/channel/UCI9vrgDbeFdsrQEk-4RWoiA


