<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
<head>

<meta charset=UTF-8">
<title>En-Dolphin WebSocket</title>

<style>  
table { 
	border-collapse: collapse; 
	border-spacing: 1px;
	text-align: center;
	line-height: 1.5;

}  
th { 

	font-weight: bold;
    vertical-align: top;
    color: #fff;
    background: #1451A8 ;

}

td{
	vertical-align: top;
    border-bottom: 1px solid #ccc;
    background: #eee;
}

</style>

</head>
<body>
	

<!-- 테이블의 헤더 -->

	<table border="1">
	
		<thead>
			<tr>
			    <th width="400" >시간</th>
			    <th width="200" >타각</th>
			    <th width="200" >속도</th>
			    <th width="210" >위도</th>
			    <th width="210" >경도</th>
		    </tr>
		</thead>
		
		<tbody id="orca" style="align-content: center;">
		
		</tbody> 
		
	</table>
        
<!--     <table>
    	<tbody id="orca" style="align-content: center;">
    </table>    --> 
     
    	<!-- sendWindow 서버쪽으로 보낸 데이터가 출력되는 창  -->
    	<textarea id="sendWindow" rows="5" cols="30" hidden="true"></textarea>
        <br>
        
        <!-- dataInput 안드로이드로 부터 들어오는 값이 위치하는 곳 -->
        <input id="dataInput"  type="text" hidden="true"/>
        
        <!-- 사라질 버튼 현재는 버튼을 누르면 서버쪽으로 데이터를 전송한다  -->
        <input type="submit" value=""  hidden="true"/>
        <br><br>

  
    
</body>

	<!-- 자바스크립트 영역  -->
    <script type="text/javascript">
        
        var sendWindow = document.getElementById("sendWindow"); // sendWinodw 변수화
       /* var receiveWindow = document.getElementById("receiveWindow"); // receiveWindow 변수화 */
        var dataInput = document.getElementById('dataInput'); // dataInput변수화
        
 /*     var gpsWindow = document.getElementById("gpsWindow"); // gpsWindow 변수화 19.10.09 */
       
        
        // 현재 JSP로 접속할 시 실행될 java 클래스를 포함하여 웹 소켓 생성
        var webSocket = new WebSocket('ws://endolphin.mooo.com:8080/endolphin/communication'); 
                
        //19.11.14
        var bfrAng ="Midships"; //이전 각도
        var bfrSpd ="Stop"; //이전 속도
        var bfrLat ="37.578052451662025"; //이전 위도
        var bfrLon ="127.06953557733618"; //이전 경도
         
        webSocket.onerror = function(event) {
            onError(event)
          };
          
          
          webSocket.onopen = function(event) {
            onOpen(event)
          };
          
          
          webSocket.onmessage = function(event) {
          		onMessage(event)
          	   
          };    
        
        
    /* webSocket.onmessage를 호출하면 실행될 onMessage가 정의되는 부분
    ** 웹뷰의 값을 안드로이드로 전달할 수 있는 sendMessage 함수가 존재 */
    function onMessage(event) {
    	   
		var orca = document.getElementById('orca');
    	var rowlen = orca.rows.length;
    	var row = orca.insertRow(rowlen); //하단으로 추가
  
        var cmp = new String(event.data)//19.10.09 추가 들어온 값  현재 들어온 값
        var res = cmp.charAt(0);//19.10.09 추가
        

    	if(res >= '0' && res <='9'){//19.10.09 추가
    		var gpsData = cmp.split(',');
    		if (bfrLat != gpsData[0] || bfrLon != gpsData[1]){
        		bfrLat = gpsData[0];
        		bfrLon = gpsData[1];
    		}

    	} // GPS 데이터 처리
        
        
    	if(cmp.indexOf("Port") != -1 || cmp.indexOf("Star") != -1 || cmp == "Midships"){
    		if(cmp != bfrAng){
    			bfrAng = cmp;
    		}
	
    	}
    		
    	
    	if(cmp.indexOf("Full") != -1 ||cmp.indexOf("Half") != -1 ||cmp.indexOf("Slow") != -1 || cmp == "EXIT"|| cmp == "STOP"){
    		if(cmp != bfrSpd){
    			bfrSpd = cmp;
    		}
    		
    	}
 		
       	row.insertCell(0).innerHTML = "<center>"+ new Date()+ "</center>"; //값이 들어온 시간 나타내기
    	row.insertCell(1).innerHTML = "<center>"+bfrAng+ "</center>";   	
    	row.insertCell(2).innerHTML = "<center>"+bfrSpd+ "</center>";
    	row.insertCell(3).innerHTML = "<center>"+bfrLat+ "</center>";
    	row.insertCell(4).innerHTML = "<center>"+ bfrLon+ "</center>";
    	
    	orca.scrollTop = orca.scrollHeight;
    	
    	sendMessage(event.data);
    	
    }// onMessage 괄호
    
    
    /**안드로이드로 부터 전송된 값을 받아서 화면에 출력하게끔 하는 함수*/
    function setMessage(args){
    	if(args != "" || args != null){
    		webSocket.send(args);
    	}
    	 	
    }
    

  	/* 안드로이드 스튜디오에 AndroidBridge 클래스 밑에 setMessage함수로 값을 전달하는 함수 */
  	function sendMessage(msg){
  			window.endolphin.setMessage(msg);
   }
  	

  	/* 안드로이드에 설정한 mWebView 인스턴스에 AndroidBridge()클래스를 자바스크립트를 통해 접근한다고 선언 해주는 부분
  	** 자바스크립트 내에서 AndroidBridge 클래스에 접근하기 위해서 endolphin이란 네임을 써서 접근한다. 
  	** 위의 sendMessage에서 denolphin.setMessage의 경우 androidBridge 클래스 밑에 setMessage를 호출 하는 것이다*/
  	mWebView.addJavascriptInterface(new AndroidBridge(), "endolphin");
  	
  	
  	
    /* webSocket.onopen을 호출하면 실행될 onOpen이 정의되는 부분*/
    function onOpen(event) {
    	sendWindow.value += s;
    }
    
    
    /* webSocket.onerror을 호출하면 실행될 onError가 정의되는 부분*/
    function onError(event) {
      alert(event.data);
    }
  	
  	 /** 다음은 웹소켓 관련 함수 onerror, onopen, onmessage를 function(event){함수}의 형식으로 매핑 시켜
     *** 해당되는 함수가 실행 되면 {}안의 내용이 실행되게끔 한다.
     */
     
 
     
  	
    
  </script>