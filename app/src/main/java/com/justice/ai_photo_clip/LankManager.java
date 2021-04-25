package com.justice.ai_photo_clip;

import java.util.HashMap;

import com.justice.ai_photo_clip.coco.MsCoCo;

public class LankManager {
    private HashMap<String, MsCoCo> mCoCoMap;

    public LankManager() {
        mCoCoMap = new HashMap<>();
        setLankGroup1();
        setLankGroup2();
    }

    public HashMap<String, MsCoCo> getCoCoMap() { //우선순위를 정해줍니다.
        return mCoCoMap;
    }

    private void setLankGroup1() { //너무 복잡해서 두개로 나눔
        mCoCoMap.put("person", new MsCoCo("person", "사람", 1,4));
        mCoCoMap.put("bicycle", new MsCoCo("bicycle", "자전거", 2,3));
        mCoCoMap.put("car", new MsCoCo("car", "자동차", 3,3));
        mCoCoMap.put("motorcycle", new MsCoCo("motorcycle", "오토바이", 4,3));
        mCoCoMap.put("airplane", new MsCoCo("airplane", "비행기", 5,3));
        mCoCoMap.put("bus", new MsCoCo("bus", "버스", 6,3));
        mCoCoMap.put("train", new MsCoCo("train", "기차", 7,3));
        mCoCoMap.put("truck", new MsCoCo("truck", "트럭", 8,3));
        mCoCoMap.put("boat", new MsCoCo("boat", "보트", 9,3));
        mCoCoMap.put("traffic light", new MsCoCo("traffic light", "교통 신호등", 10,3));
        mCoCoMap.put("fire hydrant", new MsCoCo("fire hydrant", "소화전", 11,3));
        mCoCoMap.put("stop sign", new MsCoCo("stop sign", "정지 신호", 12,3));
        mCoCoMap.put("parking meter", new MsCoCo("parking meter", "주차 미터", 13,3));
        mCoCoMap.put("bench", new MsCoCo("bench", "벤치", 14,-1));
        mCoCoMap.put("bird", new MsCoCo("bird", "새", 15,8));
        mCoCoMap.put("cat", new MsCoCo("cat", "고양이", 16,8));
        mCoCoMap.put("dog", new MsCoCo("dog", "개", 17,8));
        mCoCoMap.put("horse", new MsCoCo("horse", "말", 18,8));
        mCoCoMap.put("sheep", new MsCoCo("sheep", "양", 19,8));
        mCoCoMap.put("cow", new MsCoCo("cow", "암소", 20,8));
        mCoCoMap.put("elephant", new MsCoCo("elephant", "코끼리", 21,8));
        mCoCoMap.put("bear", new MsCoCo("bear", "곰", 22,8));
    }

    private void setLankGroup2() {
        mCoCoMap.put("spoon", new MsCoCo("spoon", "숟가락", 23,1));
        mCoCoMap.put("bowl", new MsCoCo("bowl", "그릇", 24,1));
        mCoCoMap.put("banana", new MsCoCo("banana", "바나나", 25,1));
        mCoCoMap.put("apple", new MsCoCo("apple", "사과", 26,1));
        mCoCoMap.put("sandwich", new MsCoCo("sandwich", "샌드위치", 27,1));
        mCoCoMap.put("orange", new MsCoCo("orange", "주황색", 28,1));
        mCoCoMap.put("broccoli", new MsCoCo("broccoli", "브로콜리", 29,1));
        mCoCoMap.put("carrot", new MsCoCo("carrot", "당근", 30,1));
        mCoCoMap.put("hot dog", new MsCoCo("hot dog", "핫도그", 31,1));
        mCoCoMap.put("pizza", new MsCoCo("pizza", "피자", 32,1));
        mCoCoMap.put("donut", new MsCoCo("donut", "도넛", 33,1));
        mCoCoMap.put("cake", new MsCoCo("cake", "케이크", 34,1));
        mCoCoMap.put("chair", new MsCoCo("chair", "의자", 35,-1));
        mCoCoMap.put("sofa", new MsCoCo("sofa", "소파", 36,-1));
        mCoCoMap.put("potted plant", new MsCoCo("potted plant", "화분", 37,-1));
        mCoCoMap.put("bed", new MsCoCo("bed", "침대", 38,-1));
        mCoCoMap.put("dining table", new MsCoCo("dining table", "식사테이블", 39,-1));
        mCoCoMap.put("toilet", new MsCoCo("toilet", "화장실", 40,-1));
        mCoCoMap.put("tv", new MsCoCo("tv", "TV 모니터", 41,8));
        mCoCoMap.put("laptop", new MsCoCo("laptop", "노트북", 42,-1));
        mCoCoMap.put("mouse", new MsCoCo("mouse", "마우스", 43,-1));
        mCoCoMap.put("remote", new MsCoCo("remote", "원격", 44,-1));

        //내가 한다
        mCoCoMap.put("zebra", new MsCoCo("zebra", "얼룩말", 45,8));
        mCoCoMap.put("giraffe", new MsCoCo("giraffe", "기린", 46,8));
        mCoCoMap.put("backpack", new MsCoCo("backpack", "책가방", 47,0));
        mCoCoMap.put("umbrella", new MsCoCo("umbrella", "우산", 48,0));
        mCoCoMap.put("handbag", new MsCoCo("handbag", "핸드백", 49,0));
        mCoCoMap.put("tie", new MsCoCo("tie", "넥타이", 50,0));
        mCoCoMap.put("suitcase", new MsCoCo("suitcase", "가방", 51,0));
        mCoCoMap.put("frisbee", new MsCoCo("frisbee", "프리스비", 52,9));
        mCoCoMap.put("skis", new MsCoCo("skis", "스키", 53,9));
        mCoCoMap.put("snowboard", new MsCoCo("snowboard", "스노보드", 54,9));
        mCoCoMap.put("sports ball", new MsCoCo("sports ball", "스포츠 볼", 55,9));
        mCoCoMap.put("kite", new MsCoCo("kite", "연", 56,9));
        mCoCoMap.put("baseball", new MsCoCo("baseball", "야구방망이", 57,9));
        mCoCoMap.put("baseball glove", new MsCoCo("baseball glove", "야구글러브", 58,9));
        mCoCoMap.put("skate board", new MsCoCo("skate board", "스케이트 보드", 59,9));
        mCoCoMap.put("surfboard", new MsCoCo("surfboard", "서핑 보드", 60,9));
        mCoCoMap.put("tennis racket", new MsCoCo("tennis racket", "테니스 라켓", 61,9));
        mCoCoMap.put("bottle", new MsCoCo("bottle", "병", 62,10));
        mCoCoMap.put("wine glass", new MsCoCo("wine glass", "와인 글라스", 63,10));
        mCoCoMap.put("cup", new MsCoCo("cup", "컵", 64,10));
        mCoCoMap.put("fork", new MsCoCo("fork", "포크", 65,10));
        mCoCoMap.put("knife", new MsCoCo("knife", "나이프", 66,10));
        //여기부터 필터 설정해야함
        mCoCoMap.put("keyboard", new MsCoCo("keyboard", "키보드", 67,0));
        mCoCoMap.put("call phone", new MsCoCo("call phone", "휴대전화", 68,0));
        mCoCoMap.put("microwave", new MsCoCo("microwave", "전자 레인지", 69,0));
        mCoCoMap.put("oven", new MsCoCo("oven", "오븐", 70,0));
        mCoCoMap.put("toaster", new MsCoCo("toaster", "토스터기", 71,0));
        mCoCoMap.put("sink", new MsCoCo("sink", "싱크대", 72,0));
        mCoCoMap.put("refrigerator", new MsCoCo("refrigerator", "냉장고", 73,0));
        mCoCoMap.put("book", new MsCoCo("book", "도서", 74,0));
        mCoCoMap.put("clock", new MsCoCo("clock", "시계", 75,0));
        mCoCoMap.put("vase", new MsCoCo("vase", "꽃병", 76,0));
        mCoCoMap.put("scissors", new MsCoCo("scissors", "가위", 77,0));
        mCoCoMap.put("teddy bear", new MsCoCo("teddy bear", "테디베어", 78,0));
        mCoCoMap.put("hair drier", new MsCoCo("hair drier", "헤어드라이어", 79,0));
        mCoCoMap.put("toothbrush", new MsCoCo("toothbrush", "칫솔", 80,0));

    }
}
