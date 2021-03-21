#include <SoftwareSerial.h>

SoftwareSerial mySerial(2, 3); //블루투스의 Tx, Rx핀을 2번 3번핀으로 설정
const int analogInPin = A0;
int sensorValue = 0;
int outputValue = 0;

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
  sensorValue = analogRead(analogInPin);

  //outputValue = map(sensorValue, 0, 1023, -9, 9);

  if (sensorValue >= 681){
    outputValue = 3;
  }else if(sensorValue >= 599 && sensorValue <= 620){
    outputValue = 2;
  }else if(sensorValue >= 530 && sensorValue <= 550){
    outputValue = 1;
  }else if(sensorValue >= 470 && sensorValue <= 495){
    outputValue = 0;
  }else if(sensorValue >= 420 && sensorValue <= 440){
    outputValue = -1;
  }else if(sensorValue >= 375 && sensorValue <=390){
    outputValue = -2;
  }
  
  //Serial.println(sensorValue);
  Serial.println(outputValue);
  mySerial.write(outputValue);  //블루투스를 통해 입력된 데이터 전달
 
}
