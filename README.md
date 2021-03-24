# 한이음 공모전 _ 2019 스마트 해상물류 프로젝트 경진대회

## [2019 스마트 해상물류 프로젝트 경진대회] 5G를 이용한 울산항 안전운항 시뮬레이션 ( **참고 : 아래 설명은 필자가 개발한 부분 위주로 설명했습니다.** )

## 목차

- [과제 개요](#과제-개요)
- [적용 기술](#적용-기술)
    - [서버와 앱](#서버와-앱)
    - [조종기 앱](#조종기-앱)
    - [수신기 앱](#수신기-앱)
    - [시뮬레이션](#시뮬레이션)
- [최종 시연 영상](#최종-시연-영상)

## 과제 개요

**작품 소개**

1. 시뮬레이션과 시뮬레이션 조종기
    
    ![7](https://user-images.githubusercontent.com/47939832/112260023-90690f80-8cac-11eb-82d6-de04dd84465a.png)

2. 드론과 드론 운항 모습
    
    ![8](https://user-images.githubusercontent.com/47939832/112260029-93640000-8cac-11eb-913a-894af4ea2381.png)
    
    ![9](https://user-images.githubusercontent.com/47939832/112260036-952dc380-8cac-11eb-9832-16f0194d88d5.png)

3. 서버 화면
    
    ![10](https://user-images.githubusercontent.com/47939832/112260044-97901d80-8cac-11eb-8ae3-47d89a73759b.png)

4. 조종기앱, 제어부 앱 화면
    
    ![11](https://user-images.githubusercontent.com/47939832/112260050-9959e100-8cac-11eb-9328-cd7c30687240.png)
    
    ![12](https://user-images.githubusercontent.com/47939832/112260052-99f27780-8cac-11eb-937d-19c8cd72310f.png)
    
    ![13](https://user-images.githubusercontent.com/47939832/112260059-9ced6800-8cac-11eb-84ce-af5149906749.png)

- 5G + 위치센서 + 원격조정 + 수상드론 + 게임의 조합으로 선박 안전운항 시뮬레이션 개발
- 시뮬레이터 구축 후 소형선박 운항자 대상 안전교육 가능(상대적으로 작은 선박의 사고율이 높고 큰 선박은 사고율이 낮음)
- 5G 센서 모듈의 실제선박 부착: 수상드론의 스마트폰 탑재 후 스마트폰에 서 5G 및 GPS를 활용한 초저지연으로 안전관리 가능
- 시뮬레이터와 수상 드론, 관제센터의 구축으로 울산항 내부의 입출항 배의 항로 궤적 확인 파악

**작품 구성도**

    ![1](https://user-images.githubusercontent.com/47939832/111901995-7c3fcb00-8a7e-11eb-843e-fdccf836ce35.png)
    

- 작품의 특징 및 장점
    
    - 시뮬레이션
        
        - 바다와 배의 동작은 기존의 오픈소스를 활용하여 우리 프로젝트에 맞게 수정 및 변경, 함수 추가 등으로 제작
        
        - 시뮬레이션 내부의 일어나는 동작은 마우스의 커서 좌표와 블루프린트를 통한 애셋의 움직임, 레벨단위의 map 제작 을 진행
        
        -  실제 시뮬레이션을 하기 위한 배의 타기와 속도 조절 레버를 실제로 구현 하였는데 가격적인 측면에서 다른 조종기에 비해 저렴
        
        - 가격도 가격이지만 아두이노를 활용하였기에 부가적인 기능의 확장 가능성이 용이함
        
        - 국내의 제한적인 항만 시뮬레이션에서 상대적으로 쉬우면서 저렴한 시뮬레이션의 등장이라고 할 수 있음
        
        - 누구나 쉽게 어렵지 않게 실제 배의 움직임을 간접적으로 체험 할 수 있음
        
        - 자이로 센서를 활용해 실제의 각도와 배의 타각과 매핑 또한 배의 속도의 단수 만큼 레버의 단 수 구현 하여 CPS를 이루어 냈다고 할 수 있음
        
        - 전반적으로 시뮬레이션은 비슷한 기능을 보여주면서 부가기능 + 덧 붙여진 재료의 조합이 다른 시뮬레이션에 비해 저가라는 사실을 어필 할 수 있음
        
    - 수상 드론
      
        - 앱을 통한 조종으로 사용자가 쉽게 수상 드론을 조작 할 수 있음
        
        - LTE에서 인터페이스를 구축 하였기에 추후 5G로 확장 가능성이 존재
        
        - 수상 드론은 실제 배의 타각에 따른 회전을 똑같이 구현 하였기에 CPS를 이루어 냈다고 할 수 있음
        
        - 라즈베리 파이가 주 MCU로 동작하기 때문에 추후 다른 센서, 모듈의 연결로 확장 가능성이 용이하다고 할 수 있음 
        
        - 드론의 외형 소재, 내부에 들어가는 소자들을 생각 했을 때 기존의 출시된 다른 수상 드론에 비해 무게가 가볍고 가격적인 측면에서 저렴함
        
        - 구축된 서버와 통신이 가능하여 사용자가 앱을 통해 조종하는 것이 아닌 관제센터에서 관리가 가능
        
        - 또한 실제 배에 탑재되어 VTS관제 시스템도 있겠지만 안전장치의 기능을 추가 할 수 있음
        
        - 안전장치의 추가라는 것은 수상 드론에 부착 되어있는 GPS 정보가 될 수 있고 비상 연락망이 될 수도 있고(LTE 인터페이스를 구축하기위해 스마트폰이 드론에 탑재된 형태) 더 얼마나 확장 하느냐에 따라 각기 다른 기능을 보여줄 것으로 예상

## 적용 기술

- 5G를 이용한 GPS 정밀 관리 (오차: 1m이내, 경신: ms까지 조절가능)

- 실제 환경과 비슷한 시뮬레이션, 인터넷을 통한 실시간 수상 드론 제어

- 5G / LTE / WiFi 무선 네트워크를 통한 네트워크 제어

- 수상 드론의 기획/개발/운용

- 스마트 폰을 연동한 물리적 도구(타기, 수상 드론) 제어

- Open Source H/W (Arduino, Raspberry Pi)

- Open Source S/W (운항 시뮬레이션, Unreal Engine, Google Map API)

### 서버와 앱

![2](https://user-images.githubusercontent.com/47939832/111902193-7dbdc300-8a7f-11eb-82d2-ef7b8e1eb8b2.png)
  
    초반 앱과 앱을 통신하기 위해 제어부 앱 측에서 핫스팟을 열어줘야 TCP/IP 통신을 통해 드론을 제어했습니다. 하지만 우리 프로젝트는 5G를 이용해 통신을 해야 했었고 고정 IP를 이용해 5G를 구현하기에는 한계가 있었습니다. 5G를 사용하기 위해서는 우선 LTE 통신을 이용해 앱과 앱의 data를 주고받을 필요가 있었습니다. 이를 위한 해결책으로 직접 서버를 만들어 서로 다른 2개의 앱을 서버에 접속시켜 서버를 통해 값을 주고받는 방법을 생각해냈습니다.
    
    드론과 연결될 앱과 드론을 조종할 앱 2개가 존재합니다. 앱과 앱은 LTE 더나아가 5G 환경을 통해 통신합니다. 아래 과정은 APP간의 통신을 위한 과정입니다.
    
    1. 인터넷을 통한 상호 교환을 위해 서버를 구축해야 합니다. 그러기 위해 KT 홈 AP를 이용 하여 서버가 될 노트북의 내부 아이피를 할당
    2. 내부 아이피를 할당받은 노트북에서 WAS를 통해 로컬서버를 오픈하고 그 위에 웹소켓을 실행해줄 서버 페이지를 JSP, 자바스크립트, 자바로 구성
    3. 이후 로컬서버를 실행하여 JSP페이지를 구동
    4. DDNS를 위한 URL을 하나 할당받고 이 URL을 DDNS서버를 통해 외부 아이피와 URL을 매핑
    5. DNS를 위한 URL을 할당 받고 DNS 서버를 통해 내부 아이피와 URL을 매핑
    6. KT 홈 관리자를 통해 외부아이피와 내부 아이피 간의 포트포워딩
    7. 조종기 앱과 드론에 동작할 앱이 DDNS에 등록된 URL에 덧붙여 로컬서버를 통한 JSP페이지까지의 위치를 덧붙여 접속
    8. 앱에서 JSP페이지까지 가게 되면 안에 구현해놓은 자바스크립트 함수 및 자바 함수에 따라 양방향 간의 소켓통신
    9. 각각의 소켓통신으로 얻은 데이터는 각각 해당되는 부분에 맞게 파싱되고 처리

### 조종기 앱

![3](https://user-images.githubusercontent.com/47939832/111902354-7c40ca80-8a80-11eb-8b58-de56caf5bbd2.png)

- background : 선박 조타실 내부의 모습과 비슷한 이미지
- 휠의 기능 : 스마트폰의 기울기 센서 값을 통해 휠의 이미지가 회전하며 회전한 값에 따라 드론의 회전 각도를 구현
- progress bar : 스마트폰의 기울기 값에 따라 게이지 증감소
- GPS : 사용자에게 드론의 현재 위치를 알려주기 위해 GPS View 또한 구성

### 수신기 앱

![4](https://user-images.githubusercontent.com/47939832/111902556-64b61180-8a81-11eb-8d71-b290d8b8a07f.png)

- background : 드론에 실릴 스마트폰으로써 배경 이미지는 실제 드론이 동작될 환경에 맞춰 바다 이미지를 선택
- 인터넷 연결 버튼 : 구름 모양의 이미지는 버튼 기능을 합니다. 이는 인터넷과 연결되어 조종기 앱으로부터 드론의 방향과 속도 값을 받을 수 있게 합니다. 또한, 배경 이미지에 어울리도록 구름 이미지로 디자인했습니다.
- GPS : 수상드론의 위치를 조종기 앱으로 송신 및 가시화

### 시뮬레이션

![6](https://user-images.githubusercontent.com/47939832/111902869-cd51be00-8a82-11eb-86e7-0bb1c6d1c3dd.png)

- 시뮬레이션 전체 흐름도
    
    - 플로우 차트
    
        ![5](https://user-images.githubusercontent.com/47939832/111902780-6207ec00-8a82-11eb-9456-a47cb267cf06.png)
        
        시뮬레이션은 크게 3개의 액션으로 구성되어 있고 위 흐름을 따른다. 
        
        1. 메인 : 시뮬레이션 프로그램 실행 시 화면에 처음 보여지는 화면(figure 2)이다. 바다에 선박이 떠있는 모습이 백그라운드 영상으로 재생되고 우측에는 ‘시작’, ‘종료’ 버튼 UI로 구성된다. 
        2. 시뮬레이션 환경설정 : 시뮬레이션을 플레이 하기 전 사용자가 시뮬레이트 할 환경을 설정하는 화면으로 [ 배 선택 – 부두 선택 – 입,출항 선택 – 자유,자동항해 선택 ] 의 4단계로 구성되어 있다. 메인 화면에서 ‘시작’ 버튼을 누르면 뒤이어 설정화면이 나온다. 설정단계에서 3단계까지 모두 선택했다면 선택한 옵션으로 시뮬레이션을 시작하게 된다.
        3. 플레이 : 환경설정에서 선택한 옵션으로 시뮬레이션이 시작되고 사용자가 조타기와 레버를 이용하여 배를 조종하며 운항해볼 수 있다.
        4. 도착 : 목적지에 도착하게 되면 도착이라는 텍스트와 함께 ‘처음으로’, ‘다시하기’ 버튼 UI가 생성되어 사용자가 시뮬레이션을 계속 진행할 수 있게 한다.
        
        ![14](https://user-images.githubusercontent.com/47939832/112263073-0b80f480-8cb2-11eb-9a25-ffd73564a859.png)
        
        1. 마우스 좌표값에 따라 특정 좌표값을 넘어가면, 원하는 setting 버튼을 선택할 수 있게 알고리즘을 구성
        2. 마우스 좌측 -> 메뉴 선택
        3. 마우스 우측 -> 이전 환경설정 화면

- BluePrint 알고리즘( 환경 Setting menu )

    - MainWidgetVisibility
        
        **Main 화면 UI에 해당하는 버튼의 visibility를 정의하는 함수 블루프린트**
        
        ![19](https://user-images.githubusercontent.com/47939832/112264578-716e7b80-8cb4-11eb-91ac-0476e25ce8d9.png)
        
        - 메인 화면에서 시작이나 종료 버튼을 누르면 OnClicked(시작버튼) 이벤트가 실행되어 MainButtonSelected 변수값을 true로 설정하였다. 이 변수값을 이용하여, true이면 메인 화면의 버튼을 눌렀다는 의미이므로 Main Visibility를 hidden으로 설정한다. MainButtonSelected 변수값이 false 이면 메인 화면의 버튼을 누르지 않았다는 의미이므로 메인 화면 버튼 UI를 표시해야하므로 MainVisibility를 visible로 설정한다. MainVisibility는 MainWidgetVisibility의 estateVisibility 리턴값으로 설정되고 estateVisibility 리턴값에 따라 화면에 MainWidgetVisibility의 영향을 받는 버튼과 이미지(메인화면 버튼 UI)가 보여지게 된다.
        
    - ShipSelectButtonVisibility
        
        **배 선택 화면 UI에 해당하는 버튼의 visibility를 정의하는 함수 블루프린트**
        
        ![20](https://user-images.githubusercontent.com/47939832/112264585-729fa880-8cb4-11eb-9c50-02eb6cfcb8ff.png)
        
        - 화면의 Visibility를 결정하기 위해 ShipSelected 변수와 MainSelected 변수가 사용되는데, 이 두 변수의 배타적 논리합에 따라 배 선택에 해당하는 UI들의 Visibility가 결정된다. 두 변수의 배타적 논리합을 한 결과값이 false인 경우 estateVisibility 변수의 hidden으로 간주하고, 결과값이 true인 경우 visible로 간주하였을 때의 진리표는 다음과 같다. 
        
            ![23](https://user-images.githubusercontent.com/47939832/112264936-f8bbef00-8cb4-11eb-9243-64331cdbf3b6.png)
            
        - 위젯 화면의 구성 순서에 따라서 MainButtonSelected값이 false이며 ShipSelected의 값이 true인 경우는 존재하지 않으므로 고려하지 않는다. 위의 테이블에서 연산의 결과값이 visible인 경우는 두 변수의 값이 다른 경우만 존재한다. 따라서 이를 조건식으로 삼아서 true(두 변수의 값이 다른 경우)이면 ShipSelectVisibility를 visible로 설정하고 false이면 hidden으로 설정한다. 
        
        - 두 변수의 값이 다른 경우 조건식에서 true가 되어 ShipSelectVisibility를 visible로 설정하는데 그 이후 GoBack이라는 bool 변수를 조건식으로 삼는 branch가 수행되게 된다. 이것은 사용자가 환경설정 위젯에서 마우스 오른쪽 버튼을 누르면 이전 화면을 나타나게 되는데 이 기능을 구현한 것이다. 마우스 오른쪽 버튼을 누르면 UlsanMap 레벨 블루프린트에서 UW_SimulSetting의 변수 GoBack을 true로 설정하게 된다. 그리고 UW_SimulSetting의 ShipSelectButtonVisibility 함수에서 ShipSelectVisibility 값이 visible으로 설정되면 GoBack 변수의 값에 따라, true이면 뒤로 가는 동작을 수행하기 위해 MainButtonSelected와 ShipSelected, GoBack의 값을 모두 false로 설정한다. MainButtonSelected의 값이 다시 false로 변경된다면 MainWidgetVisibility 함수에 의해 메인 화면의 visibility가 다시 visible로 설정되기 때문이다.
        
    - PortSelectButtonVisibility
        
        **부두 선택 화면 UI에 해당하는 버튼의 visibility를 정의하는 함수 블루프린트**
        
        ![21](https://user-images.githubusercontent.com/47939832/112264586-73383f00-8cb4-11eb-8834-d8c32f54986e.png)
        
        - PortSelectButtonVisibility 함수도 ShipSelectButtonVisibility 함수의 논리와 동일하다. 단지 다루는 변수가 MainButtonSelected가 아닌 PortSelected로 바뀌었을 뿐이다. 위의 테이블과 동일하게 배타적 논리합의 결과에 따라 visible과 hidden이 결정된다는 로직 또한 같다. ShipSelected가 true이고 PortSelected가 false일 때 PortSelectVisibility 값을 visible로 설정하고 ShipSelected와 PortSelected의 값이 같으면 PortSelectVisibility 값을 hidden으로 설정한다.
        
        - 뒤로가기 로직 또한 같다. 단지, 이전 설정단계 화면과 관련있는 변수 ShipSelected의 값을 false로 설정하고 뒤이어 현재 화면의 버튼이 선택되지 않았음을 설정하기 위해 PortSelected의 값을 false로 설정하고 GoBack의 변수 또한 false로 설정하여, 또 오른쪽 마우스 버튼을 눌렀을 때 GoBack의 값을 true로 설정하여 뒤로가기 기능이 제대로 동작할 수 있게 한다.
        
    - A/DSelectButtonVisibility
        
        **입,출항 선택 화면 UI에 해당하는 버튼의 visibility를 정의하는 함수 블루프린트**
        
        ![22](https://user-images.githubusercontent.com/47939832/112264588-73d0d580-8cb4-11eb-97c1-17ea97d8514f.png)
        
        - A/DSelectButtonVisibility 함수명의 A/D 중 A는 Arrival, D는 Departure를 의미하는 것으로 입출항 선택 화면의 visibility를 결정하는 함수이다. 이 함수 또한 위의 두 개의 화면 visibility 함수와 동일한 로직을 갖는다. SelectedArrivalMode와 SelectedDepartureMode의 논리합의 결과가 PortSelected 변수와 함께 배타적 논리합을 이루게 되는데 이 값이 조건식으로 사용된다.
        
        - SelectedArrivalMode와 SelectedDepartureMode가 OR로 연결된 까닭은, 입출항 모드 선택 화면에서 입항 모드와 출항 모드 둘 중 하나를 선택하여 선택을 결정하기 때문이다. 둘 중 하나가 선택되면 값이 true로 되고 논리합의 결과값이 true가 되어 입,출항 모드 선택이 완료되었음을 의미하게 된다. 위의 visibility 함수의 로직과 동일하게 이전 화면의 PortSelected 변수값과 논리합의 결과값의 배타적 논리합을 조건식으로 한다. 조건식이 true이면 A/DWidgetVisibility 값을 visible로 설정하고 false이면 hidden으로 설정한다.
        
        - 뒤로가기 기능도 마찬가지로 위의 visibility 함수와 동일한 로직을 갖는다. 마우스 오른쪽 버튼 클릭 동작으로 레벨 블루프린트에서 GoBack 변수를 true로 설정하고, 배타적 논리합한 결과값이 true이면 이전 화면의 Visibility를 결정하는 PortSelected 변수를 false로 설정하고 SelectedArrivalMode와 SelectedDepartureMode를 false로 설정하여 PortSelectButtonVisibility 함수에 의해 부두선택 화면이 보여지게 된다. 마지막으로 GoBack 변수를 false로 설정하여 다음 오른쪽 마우스 동작에도 뒤로가기 기능이 실행될 수 있도록 한다.
        
- BluePrint( 시뮬레이션과 아두이노<가변저항> 연결 )
    
    **아두이노 가변저항으로부터 읽어들인 값을 BP_ContainerShip의 변수에 저장을 하는 블루프린트**
    
    ![24](https://user-images.githubusercontent.com/47939832/112266758-d11a5600-8cb7-11eb-976b-ccd171417577.png)
    
    - 아두이노와 언리얼간의 연결을 하는 부분이다. 우선 언리얼과 연결 되기 이전에 아두이노에는 Serial 통신을 할 수 있는 코드가 삽입이 되어 있어야 한다. 오렌지보드 같은 상위 호환 보드에서는 먼저 연결을 하고 코드를 한번 더 업로드 함으로써 안정성을 확실하게 해주는 것이 좋다. 아두이노에 코드를 삽입한 후 언리얼 상 블루프린트에서 다음과 같은 작업을 한다. 기기마다 변화하는 Serial 포트 넘버로 인해 Integer 타입의 portNum이란 변수를 생성하고 그 곳에 현 아두이노와 연결된 포트번호를 숫자만 기입하면 된다. 이후 Open Serial Port 함수를 통해서 언리얼과 아두이노의 Serial 연결을 실행한다. 중간에 포트를 통해 String 타입을 한번 print를 해주는데 이것은 아두이노에서 무작정 값을 주는 것이 아니라 언리얼에서 읽어 들이는 값이 존재하면 아두이노의 Serial의 통신을 실행하는 것이다. 이후 통신이 열리게 되면 가변저항을 통한 값을 언리얼에 보내주게 되고 UE4 Arduino 플러그인 밑의 read 함수를 통해 들어온 값을 읽고 그 값을 float타입으로 캐스팅을 해주는데 이것이 곧 시뮬레이션 상의 속도를 조절하는 역할을 하는 값이다. 이후 이 값은 선택한 환경까지 똑같이 타고가 시뮬레이션 내의 선박의 속도 조절에 사용한다. 그리고 이 모든 과정이 끝난 후 Close Port를 통해 열린 포트를 닫아 준다.
        
- 알고리즘 구성에 따른 화면 구성
    
    ![15](https://user-images.githubusercontent.com/47939832/112263808-3d468b00-8cb3-11eb-97ac-9e5477548284.png)
    
    ![16](https://user-images.githubusercontent.com/47939832/112263820-40417b80-8cb3-11eb-8803-60ddb2061793.png)
    
    ![17](https://user-images.githubusercontent.com/47939832/112263824-42a3d580-8cb3-11eb-802a-9bd69287eb0c.png)
    
    ![18](https://user-images.githubusercontent.com/47939832/112263829-446d9900-8cb3-11eb-86dd-204fb457e77c.png)
        
        
## 최종 시연 영상
    
    [![[2019 스마트 해상물류 프로젝트 경진대회][19-P625]-5G를 이용한 울산항 안전운항 시뮬레이션](http://img.youtube.com/vi/Kb9kTH-7CJw/0.jpg)](https://youtu.be/Kb9kTH-7CJw?t=0s)
    
