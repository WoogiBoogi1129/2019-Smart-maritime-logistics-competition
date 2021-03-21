#include <SoftwareSerial.h>

//SoftwareSerial mySerial(0, 1); //블루투스의 Tx, Rx핀을 2번 3번핀으로 설정
SoftwareSerial mySerial(2, 3);
char rdData='a';

void setup() {
  // 시리얼 통신의 속도를 9600으로 설정
  Serial.begin(9600);
  while (!Serial) {
    ; //시리얼통신이 연결되지 않았다면 코드 실행을 멈추고 무한 반복
  }

  //블루투스와 아두이노의 통신속도를 9600으로 설정
  mySerial.begin(9600);
 
}

void loop() { //코드를 무한반복합니다.
  if (mySerial.available()) { //블루투스에서 넘어온 데이터가 있다면
    rdData=mySerial.read(); //시리얼모니터에 데이터를 출력
  }

 // if (Serial.available() >0){
    Serial.println((int)rdData);
    delay(10);
   // Serial.end();
   // Serial.begin(1152500);
  //}

}
