package com.orca.orcaconsole.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

// 서버 클라이언트 통신을 위한 추가 코드 - import
import android.os.Handler;
import android.os.Message;

// USB시리얼 통신을 하기위한 코드
import com.orca.orcaconsole.control.GyroControl;
import com.orca.orcaconsole.R;
import com.orca.orcaconsole.driver.UsbSerialDriver;
import com.orca.orcaconsole.driver.UsbSerialPort;
import com.orca.orcaconsole.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;

//GyroControl
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

//progress 부분
import android.widget.ProgressBar;
import android.widget.Toast;


/***
 * @19.09.06  시점으로 P2P소켓통신 할 수 있는 부분은 다 주석처리로 막아 놓았음
 * 현재는 웹소켓으로만 통신을 할 수 있게끔 되어 있는 코딩으로 이루어져 있음
 */

//안드로이드에서 자바스크립트의 사용승인과, 자바스크립트인터페이스 적용
@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener{



    //19.08.28, webview를 이용한 웹소켓관련
    private WebView mWebView;
    private WebSettings mWebSettings;
    public final Handler handler2 = new Handler();
    TextView fromJSPData;
    EditText websocketDataContainer; // toWebsocketData값이 담겨질 텍스트창이고 이 텍스트창에서 값의 변화가 감지 되면 jsp서버로 값을 전송해준다 (공항느낌)
    String toWebsocketData;// JSP서버로 보내질 값이 저장되는 곳 (여행객들 느낌)
    EditText gpsDataContainer;
    String toWebsocketGpsData;
    boolean webviewFlag = false;
    String toastMsg; // 19.09.29 연결 해제 를 위한 Toast창 매세지



    //19.08.23 imageview
    ImageView shipView;
    ImageView openServerView;


    //19.08.22 progressBar 부분
    private  ProgressBar LeftProgress;
    private  ProgressBar RightProgress;
    int value2 =1;


    //19.11.14 서버레이아웃 속도부분 추가
    private TextView receivedBoxSpd;

    //19.07.26 서버레이아웃 부분
    private TextView receivedBox;
    private String  cliToSerMsg, sendMsg, raspiMsg="1";


    //19.07.27 게임패드레이아웃 부분
    private TextView cliServerIpText;
    private ImageButton serverOpenCloseBtn;
    private boolean isBtnClickStart=false;


    //GyroControl 부분
    private GyroControl gyroCon;
    private SensorManager sensorMngr;
    private Sensor sen;
    private int comInt, pastComInt=0;


    //usb 시리얼 통신 관련
    UsbManager manager;
    List<UsbSerialDriver> availableDrivers;
    UsbSerialDriver driver;
    UsbDeviceConnection connection;
    UsbSerialPort port;


    //P2P소켓통신중 핸들러 사용을 위한 변수 추가 코드
    private static final int SERVER_UPDATE = 100;
    private static final int CLIENT_UPDATE = 200;
    private static final int WEBSOCKET_UPDATE = 300; // 19.08.30 웹소켓을 위해 추가
    private static final int WEB_GPS_UPDATE = 400; // 19.08.30 웹소켓을 위해 추가


    //p2p소켓통신 서버세팅
/*    private ServerSocket serverSocket =null;
    private Socket socket=null;
    private DataInputStream serverIn;
    private DataOutputStream serverOut;*/
    private StringBuilder serverMsg = new StringBuilder();
/*    private Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();*/


    //p2p소켓통신 클라이언트 세팅
/*    private Socket clientSocket = null;
    private DataInputStream clientIn;
    private DataOutputStream clientOut;*/
    private String controllerMsg = null;


    //레버 버튼 관련 변수
    String [] leverNum = {"EXIT", "Half Astern", "Slow Astern", "Stop", "Slow Ahead", "Half Ahead", "Full Ahead"};
    int idx=3;


    //raspi쪽 GPS 관련 변수 추가
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isPermission = false;
    TextView tvGpsLatitude, tvGpsLongitude;


    //게임패드쪽의 GPS 관련 19.08.28 private LocationListener locationlistner 삭제
    TextView tvGpsLatitudePad, tvGpsLongitudePad;
    private LocationManager locationManager;
    double gpsLat=0.0; // 위도
    double gpsLon=0.0;// 경도
    String gpsLocationMsg = "1";
    String[] gpsLocationAry;


    //19.09.22
    BroadcastReceiver mUsbDeviceReceiver;
    UsbManager usbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = "";
    PendingIntent permissionIntent;


    //19.09.24
    TextView tvAngleData;
    TextView tvSpeedData;

    //핸들러 담당 부분
   private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msgg) {
            super.handleMessage(msgg);
            switch (msgg.what) {
                //19.08.06 p2p 소켓통신 서버에서 클라이언트에서 오는 값 확인 후 값 매핑 후 라즈베리파이로 전송
                //19.08.30 웹 소켓통신에서도 p2p 소켓통신과 똑같은 기능을 함
                case SERVER_UPDATE: {
                    serverMsg.append(cliToSerMsg); // 클라이언트에서 보내는 정보는 msg에 담김  추후에 오고 간 내역 확인 가능때문에 살려둔 기능
                    receivedBox.setTextColor(getColor(R.color.setColor));
                    receivedBoxSpd.setTextColor(getColor(R.color.setColor));
                    //receivedBox.setText(cliToSerMsg);
                    switch (cliToSerMsg){
                        case "Port Five": raspiMsg = "a";  shipView.setImageResource(R.drawable.l1); receivedBox.setText(cliToSerMsg); break;
                        case "Port Ten": raspiMsg = "b"; shipView.setImageResource(R.drawable.l2); receivedBox.setText(cliToSerMsg); break;
                        case "Port Fifteen": raspiMsg = "c"; shipView.setImageResource(R.drawable.l3); receivedBox.setText(cliToSerMsg); break;
                        case "Port Twenty": raspiMsg = "d"; shipView.setImageResource(R.drawable.l4); receivedBox.setText(cliToSerMsg); break;
                        case "Port Twenty-Five":  raspiMsg = "e"; shipView.setImageResource(R.drawable.l5); receivedBox.setText(cliToSerMsg); break;
                        case "Port Thirty": raspiMsg = "f"; shipView.setImageResource(R.drawable.l6); receivedBox.setText(cliToSerMsg); break;
                        case "Hard a Port": raspiMsg = "g"; shipView.setImageResource(R.drawable.l7); receivedBox.setText(cliToSerMsg); break;
                        case "Midships": raspiMsg = "o"; shipView.setImageResource(R.drawable.home); receivedBox.setText(cliToSerMsg); break;
                        case "Starboard Five": raspiMsg = "h"; shipView.setImageResource(R.drawable.r1); receivedBox.setText(cliToSerMsg); break;
                        case "Starboard Ten": raspiMsg = "i"; shipView.setImageResource(R.drawable.r2); receivedBox.setText(cliToSerMsg); break;
                        case "Starboard Fifteen": raspiMsg = "j"; shipView.setImageResource(R.drawable.r3); receivedBox.setText(cliToSerMsg); break;
                        case "Starboard Twenty": raspiMsg = "k"; shipView.setImageResource(R.drawable.r4); receivedBox.setText(cliToSerMsg); break;
                        case "Starboard Twenty-Five": raspiMsg = "l"; shipView.setImageResource(R.drawable.r5); receivedBox.setText(cliToSerMsg); break;
                        case "Starboard Thirty": raspiMsg = "m"; shipView.setImageResource(R.drawable.r6); receivedBox.setText(cliToSerMsg); break;
                        case "Hard a Starboard": raspiMsg = "n";shipView.setImageResource(R.drawable.r7); receivedBox.setText(cliToSerMsg); break;
                        case "Full Ahead": raspiMsg = "p"; receivedBoxSpd.setText(cliToSerMsg); break;
                        case "Half Ahead": raspiMsg = "q"; receivedBoxSpd.setText(cliToSerMsg); break;
                        case "Slow Ahead": raspiMsg = "r"; receivedBoxSpd.setText(cliToSerMsg); break;
                        case "Stop": raspiMsg = "s"; receivedBoxSpd.setText(cliToSerMsg); break;
                        case "Slow Astern": raspiMsg = "t"; receivedBoxSpd.setText(cliToSerMsg); break;
                        case "Half Astern": raspiMsg = "u"; receivedBoxSpd.setText(cliToSerMsg); break;
                        case "EXIT": raspiMsg = "v"; receivedBox.setText(cliToSerMsg); receivedBoxSpd.setText(" "); break;
                        default: receivedBox.setText(cliToSerMsg); shipView.setImageResource(R.drawable.home); break;
                    }
                        //드라이버가 사용 가능지 확인 하는 부분
                        if (availableDrivers.isEmpty()) {
                            return;
                        }

                        // 드라이버를 활성화
                        driver = availableDrivers.get(0);
                        connection = manager.openDevice(driver.getDevice());

                        // 연결이 안되었을 경우 리턴
                        if (connection == null) {
                            return;
                        }
                        // 드라이버를 통한 port연결
                    try{
                        port = driver.getPorts().get(0);
                        port.open(connection);
                        port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                        byte buffer[] = raspiMsg.getBytes();
                        port.write(buffer,3500);
                        port.close();

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }break;


                //19.08.06 p2p 소켓통신 클라이언트에서 서버에서 오는 값 확인 후 매핑
                //19.08.30 웹 소켓통신에서 GPS값을 받고 난 후 출력할 때 사용
                case CLIENT_UPDATE:{
                    gpsLocationAry = controllerMsg.split(",");
                    tvGpsLatitudePad.setTextColor(getColor(R.color.gamePadText)); //19.09.25 색 칼라
                    tvGpsLongitudePad.setTextColor(getColor(R.color.gamePadText)); //19.09.25 색 칼라
                    tvGpsLatitudePad.setText(gpsLocationAry[0]);
                    tvGpsLongitudePad.setText(gpsLocationAry[1]);
                    gpsLocationAry = null;
                }break;

                // 웹소켓 서버쪽으로 타각, 속도를 보내는 곳
                case WEBSOCKET_UPDATE:{
                    websocketDataContainer.setText(toWebsocketData.trim());
                }break;

                // 웹소켓 서버쪽으로 GPS 정보를 보내는 곳
                case WEB_GPS_UPDATE:{
                    toWebsocketGpsData = gpsLocationMsg;
                    gpsDataContainer.setText(toWebsocketGpsData);
                }break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //19.08.05 상태바 없애고 풀화면으로 보여주게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main); // 안드로이드 시작시 메인 화면과의 연결

        //19.09.24
        tvAngleData = (TextView)findViewById(R.id.tvAngleData);
        tvSpeedData = (TextView)findViewById(R.id.tvSpeedData);

        //19.08.28 webView 사용을 위한 설정
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.addJavascriptInterface(new AndroidBridge(), "endolphin"); //JSP페이지에서 자바스크립트를 통해 안드로이드를 접근할때 사용 하는 약속
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록

        //19.09.29 제어부 앱을 위한 주석 온/오프 (제어부 => 주석해제, 조종기 => 주석)
        webviewFlag = true;
        if(webviewFlag == true){
            mWebView.loadUrl("http://endolphin.mooo.com:8080/endolphin/WebSocketServer.jsp");
        }



        //19.07.26 새로운 UI의 스크롤뷰 적용
        receivedBox = (TextView) findViewById(R.id.receivedBox);
        receivedBoxSpd = (TextView) findViewById(R.id.receivedBoxSpd);


        //19.07.27 드론 게임패드 관련
        serverOpenCloseBtn = (ImageButton) findViewById(R.id.serverOpenCloseBtn);
        cliServerIpText = (TextView) findViewById(R.id.cliServerIpText);


        //usb시리얼
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);


        //GyroControl
        sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sen = sensorMngr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroCon = (GyroControl) findViewById(R.id.gyro_control);


        //라즈베리파이쪽 GPS
        tvGpsLatitude = (TextView) findViewById(R.id.tvGpsLatitude);
        tvGpsLongitude = (TextView) findViewById(R.id.tvGpsLongitude);


        //컨트롤러쪽 GPS
        tvGpsLatitudePad = (TextView) findViewById(R.id.tvGpsLatitudePad);
        tvGpsLongitudePad = (TextView) findViewById(R.id.tvGpsLongitudePad);


        //imageView 배 각도에 따른 변경
        shipView = (ImageView)findViewById(R.id.shipView);
        openServerView = (ImageView)findViewById(R.id.openServer);


        //웹뷰레이아웃에 같이 있는 전송된 값 받는 창
        fromJSPData = (TextView)findViewById(R.id.fromJSPData);
        websocketDataContainer = (EditText)findViewById(R.id.websocketDataContainer);
        gpsDataContainer = (EditText)findViewById(R.id.gpsDataContainer);
        websocketDataContainer.addTextChangedListener(new TextWatcher() {
            //입력하기 전
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //mWebView.loadUrl("javascript:setMessage('"+ websocketDataContainer.getText() + "')");
            }
            //입력하는 중
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mWebView.loadUrl("javascript:setMessage('"+ websocketDataContainer.getText() + "')");

            }
            //입력 완료
            @Override
            public void afterTextChanged(Editable s) {
                mWebView.loadUrl("javascript:setMessage('"+ websocketDataContainer.getText() + "')");
            }
        });

        //GPS를 웹뷰로 보내는 함수
        gpsDataContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mWebView.loadUrl("javascript:setMessage('"+ gpsDataContainer.getText() + "')");
            }
        });


        //19.09.22
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        mUsbDeviceReceiver=new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String action =  intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        //UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                           // if(device != null){
                                //call method to set up device communication
                            //}
                        }
                        else {
                            //Log.d(TAG, "permission denied for device " + device);
                        }
                    }
                }


                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                   // UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                }


            }
        };

        callPermission();

        //19.09.22
//        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        /*19.09.29
        * 주석처리 하고 테스트 해본 결과 없어도 통신하는데 문제 없음
        * */
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        registerReceiver(mUsbDeviceReceiver, filter);
//        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
//        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));


        //19.08.06 GPS 관련 퍼미션 셀프 체크 GPS는 다음과 같이 퍼미션 허가를 해줘도 onCreate 안에서 다시 한번 확인을 해줘야 한다.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    } /** onCreate()*/


   // 19.08.28 JSP서버 값을 안드로이드 웹뷰로 자바스크립트를 통해 전달받기 안드로이드 -> JSP 자바스크립트 전달
   // JSP -> 자바 스크립트로 전달 할 때 사용 하는 클래스 이 때 클래스명 메소드명은 마음대로 정할 수 있고
   // 추후 자바스크립트와 안드로이드 클래스, 함수 연결할 때만 잘 맞춰주면 된다.
    public class AndroidBridge {
        //안드로이드 자바스크립트를 사용하려면 다음과 같은 어노테이션을 해줘야 함
        @android.webkit.JavascriptInterface
        // 자바 스크립트에서 AndroidBridge에 접근할때 사용 할 수 있는 함수
        public void setMessage(final String arg) {
            handler2.post(new Runnable() {
                @Override
                public void run() {
                    fromJSPData.setText(arg);
                    /**JSP 서버로 부터 받은 값을 확인하여 각각에 해당되는 곳으로 출력하게끔 하는 코드
                    *** 읽어 들인 값이 0~9사이에서 시작을 한다면 GPS업데이트를 뜻함 이때는 클라이언트 업데이트와 혼용
                     *  숫자로 시작하지 않는 값들은 타각, 속도 업데이트를 뜻함 이때는 기존 P2P의 서버 업데이트와 혼용
                     * */
                    if(arg.charAt(0) >= '0' && arg.charAt(0) <='9' ){
                        controllerMsg = arg;
                        handler.sendEmptyMessage(CLIENT_UPDATE);
                    }else {
                        cliToSerMsg = arg;
                        handler.sendEmptyMessage(SERVER_UPDATE);
                    }
                }
            });
        }
    }


    // P2P소켓통신에서 게임패드(클라이언트) clientIn.readTF()를 통해 서버로 부터 들어오는 값을 받게된다.
    /*public void joinServer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(cliServerIpText.getText().toString(), 7777);
                    clientOut = new DataOutputStream(clientSocket.getOutputStream());
                    clientIn = new DataInputStream(clientSocket.getInputStream());

                    //서버의 GPS값을 보내는 용도
                    while (clientIn != null) {
                        try {
                           controllerMsg = clientIn.readUTF();
                            handler.sendEmptyMessage(CLIENT_UPDATE);

                      } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }*/


    // P2P소켓통신에서 서버에서 서버소켓을 여는 부분
    /*public void serverCreate() {
        Collections.synchronizedMap(clientsMap);
        try {
            serverSocket = new ServerSocket(7777);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        *//** XXX 01. 첫번째. 서버가 할일 분담. 계속 접속받는것. *//*
                        Log.v("", "서버 대기중");
                        try {
                            socket = serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v("", socket.getInetAddress() + "에서 접속했습니다.");

                        //핸들러를 이용한 지연
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 여기는 데이터인풋스트림으로 값을 읽고 서버가 값을 출력 하는 부분
                                listenClientSocket(socket);
                            }
                        }, 2000);

                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    /* 서버가 데이터 가져오는 부분 in.readUTF()가 게임패드(클라이언트)에서 보내는 정보를 읽어 들여서
    *  그 다음 작업을 진행한다.*/
   /* private void listenClientSocket(final Socket socket) {
        if (socket == null) {
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    serverOut = new DataOutputStream(socket.getOutputStream());
                    serverIn = new DataInputStream(socket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {// 계속 듣기만!!
                    while (serverIn != null) {
                        cliToSerMsg = serverIn.readUTF(); // 클라이언트
                        handler.sendEmptyMessage(SERVER_UPDATE);

                        Thread.sleep(800);
                    }
                } catch (IOException e) {
                    // 사용접속종료시 여기서 에러 발생.


                } catch(InterruptedException e){

                }
            }
        }).start();
    }
*/

    //19.07.31 자이로센서 및 버튼을통해 데이터 보내는 부분
    private void sendAngleSpd(){

 /*       if(clientSocket!=null) {
            try { // 클라이언트에서 서버로 데이터를 보내는 부분 보낼 데이터는 sendMsg에 담겨 있음
                clientOut.writeUTF(sendMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            handler.sendEmptyMessage(WEBSOCKET_UPDATE);
        }*/

        // p2p 소켓통신이 복구가 될 시 아래의 코드는 삭제
        handler.sendEmptyMessage(WEBSOCKET_UPDATE);

    }


    //19.07.31 자이로를 통한 sendMsg 할당하는 함수
    public void steeringAngle(int comInt){
            switch(-comInt){
            case -35: sendMsg = "Hard a Port"; break;
            case -30: sendMsg = "Port Thirty"; break;
            case -25: sendMsg = "Port Twenty-Five";break;
            case -20: sendMsg = "Port Twenty";break;
            case -15: sendMsg = "Port Fifteen";break;
            case -10: sendMsg = "Port Ten";break;
            case -5:  sendMsg = "Port Five";break;
            case 0:   sendMsg = "Midships";break;
            case 5:   sendMsg = "Starboard Five";break;
            case 10:  sendMsg = "Starboard Ten";break;
            case 15:  sendMsg = "Starboard Fifteen";break;
            case 20:  sendMsg = "Starboard Twenty";break;
            case 25:  sendMsg = "Starboard Twenty-Five";break;
            case 30:  sendMsg = "Starboard Thirty"; break;
            case 35:  sendMsg = "Hard a Starboard"; break;
        }
        tvAngleData.setTextColor(getColor(R.color.gamePadText)); //19.09.25 색 칼라
        tvAngleData.setText(sendMsg);
        toWebsocketData = sendMsg;
        sendAngleSpd();
    }


    //19.07.31 degrees 통한 comInt 할당하는 함수
    public void setComInt(double d){
        if(d >= 180 ){
            comInt = -35;
        }else if(d >166.5 && d<=169.5 ){
            comInt = -30;
        }else if(d >138.5 && d<141.5 ){
            comInt = -25;
        }else if(d >110.5 && d <=113.5 ){
            comInt = -20;
        }else if(d > 82.5 && d<=85.5 ){
            comInt = -15;
        }else if(d >54.5 && d<=57.5 ){
            comInt = -10;
        }else if(d >26.5 && d<=29.5 ){
            comInt = -5;
        }else if(d <= 1.5 && d > -1.5){
            comInt = 0;
        }else if(d <= -180){
            comInt = 35;
        }else if(d <= -166.5 && d> -169.5){
            comInt = 30;
        }else if(d <= -138.5 && d > -141.5){
            comInt = 25;
        }else if(d <= -110.5 &&  d>-113.5){
            comInt = 20;
        }else if(d <= -82.5 && d>-85.5){
            comInt = 15;
        }else if(d <= -54.5 && d>-57.5){
            comInt = 10;
        }else if(d <= -26.5 && d> -29.5){
            comInt = 5;
        }
    }


    // 07.30 자이로 회전 불러오는 부분
    double gyroRotation;
    public void onSensorChanged(SensorEvent event) {
        double value = event.values[1];
        double degrees = (180 * value)/(9.8);
        // 기울기에 따른 progressBar 값 세팅
        gyroRotation = degrees;
        LeftProgress = (ProgressBar) findViewById(R.id.leftProgress);
        RightProgress = (ProgressBar) findViewById(R.id.rightProgress);
        value2 = (int)Math.round(gyroRotation);
        if (value2<0) {
            LeftProgress.setProgress(-value2);
        }
        else{
            RightProgress.setProgress(value2);
        }

        if(degrees >= 175){
            degrees = 175;
        }else if(degrees <=-175){
            degrees = -175;
        }
        setComInt(degrees);
        //웹소켓일 때 값 전달
        if (pastComInt != comInt) {
            pastComInt = comInt;
            steeringAngle(pastComInt);
        }

        gyroCon.setDegrees((float)degrees);

    }


    //19.07.26 안드로이드내 모든 클릭이벤트
    public void onClick(View v)
    {
        switch (v.getId()){
            // 서버에서 서버 아이피 불러오고 서버를 여는 부분
            case R.id.openServer:{
                if (webviewFlag == true){
                    webviewFlag = false;
                    openServerView.setImageResource(R.drawable.clouddisconnect);
                    mWebView.loadUrl("");
                    return;
                }
                // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작, 우리팀의 경우 DDNS로 매핑 시켜놓은 주소를 통해 서버에 접근함
               // 제어부앱 기능변경을 위한 설정
                mWebView.loadUrl("http://endolphin.mooo.com:8080/endolphin/WebSocketServer.jsp");
                //serverCreate();
                openServerView.setImageResource(R.drawable.cloudconnect);
                webviewFlag = true;

            }break;

            //19.08.23 GPS icon이 위치하는 부분 이고 p2p를 위해서 히든기능이 아래의 기능
            //원래는 없어져야 하는 기능// 클라이언트에서 서버아이피 불러오는 부분
/*
            case R.id.gpsIcon:{
                cliServerIpText.setText(getServerIpAddress());
                //p2pflag 이것이 1이 되면 p2p소켓통신을 하겠다는 것
                p2pFlag = 1;
            }break;
*/

            // 한번 누르면 서버접속 , 두번 누르면 서버연결 해제제
           case R.id.serverOpenCloseBtn:{
                if(isBtnClickStart == true){
                    isBtnClickStart = false;
                    cliServerIpText.setText("");
                    serverOpenCloseBtn.setImageResource(R.drawable.connect);
                    mWebView.loadUrl("");
                    toastMsg = "연결이 해제되었습니다.";
                    Toast.makeText(this,toastMsg,Toast.LENGTH_LONG).show();


                    return;
                }

                // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작, 우리팀의 경우 DDNS로 매핑 시켜놓은 주소를 통해 서버에 접근함
                   mWebView.loadUrl("http://endolphin.mooo.com:8080/endolphin/WebSocketServer.jsp");

                 //  joinServer(); //P2P소켓통신 서버 열기
               toastMsg = "연결되었습니다!";
               Toast.makeText(this,toastMsg,Toast.LENGTH_LONG).show();

                serverOpenCloseBtn.setImageResource(R.drawable.disconnect);
                isBtnClickStart = true;
            }break;

            //왼쪽 레버 버튼을 누를 시, 1씩 감소
            case R.id.leftLever:{
                    idx = idx - 1;
                    if (idx < 0){
                        idx = 0;
                }
                sendMsg=leverNum[idx];
                tvSpeedData.setTextColor(getColor(R.color.gamePadText));//19.09.25 색 칼라
                tvSpeedData.setText(sendMsg); //19.09.24
                toWebsocketData = leverNum[idx];
                sendAngleSpd();
            }break;

            //오른쪽 레버 버튼을 누를 시, 1씩 증가
            case R.id.rightLever:{
                    idx = idx + 1;
                    if (idx > 6){
                        idx = 6;
                    }
                sendMsg=leverNum[idx];
                tvSpeedData.setTextColor(getColor(R.color.gamePadText)); //19.09.25 색 칼라
                tvSpeedData.setText(sendMsg); // 19.09.24
                toWebsocketData = leverNum[idx];
                sendAngleSpd();
            } break;


        }
    }


    //19.07.27 연결종료시 소켓 다 닫는 부분 에러 발생 가능성
  /*  public void closeServer() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (clientIn != null){
                            clientIn = null;
                            clientSocket.close();
                        }

                        if (serverIn != null){
                            serverIn = null;
                            serverSocket.close();
                            socket.close();
                        }

                        Log.v("", "clientIn, clientSocket 모두 닫음");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    // p2p 소켓통신에서 ip주소 가지고 오기
/*
    public String getLocalIpAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = String.format("%d.%d.%d.%d"
                , (ip & 0xff)
                , (ip >> 8 & 0xff)
                , (ip >> 16 & 0xff)
                , (ip >> 24 & 0xff));
        return ipAddress;
    }
*/



    // p2p 소켓통신에서 서버IP가져오게 하는 부분 핫스팟을 해주는 부분의 아이피가 1로 끝난 다는 것을 활용
  /*  public String getServerIpAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = String.format("%d.%d.%d.%d"
                , (ip & 0xff)
                , (ip >> 8 & 0xff)
                , (ip >> 16 & 0xff)
                , 1);
        return ipAddress;
    }
*/






    @Override //19.07.28 뒤로가기 버튼 누르면 나오는 팝업 종료
    public void onBackPressed() {
        AlertDialog.Builder alertWindow = new AlertDialog.Builder(this);
        alertWindow.setMessage("어플을 종료합니까?");

        alertWindow.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertWindow.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertWindow.setTitle("End");
        AlertDialog alert = alertWindow.create();
        alert.show();
    }


    @Override   //GyroControl, locationListener 에서의 오버라이드
    protected void onResume() {
        super.onResume();
        sensorMngr.registerListener(this, sen, SensorManager.SENSOR_DELAY_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);

        //19.09.22 이부분은 onPause에 옮겨 보는 것도 괜찮은거 같음
        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        /*19.09.29
        *  onResume의 작업을 onPause로 이동 하기이전
        *  새로운 인텐트 추가로 띄우는것을 막기 위한 아래의 주석처리
        * */
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        registerReceiver(mUsbDeviceReceiver, filter);
//        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
//        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));


    }


    @Override //GyroControl, locationListener 에서의 오버라이드
    protected void onPause() {
        super.onPause();
        sensorMngr.unregisterListener(this);

        // 19.08.20 에러로 인한 주석 처리
        // locationManager.removeUpdates(this);


        //19.09.29 onResume에서의 동작을 onPause로 옮김
        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
//        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));

    }


    @Override//sensoreventlistner 오버라이딩
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override // 프로세스가 완전히 종료 될시 자동 호출되어 처리 하는 부분
    protected void onDestroy() {
        super.onDestroy();
       // clientIn = null;
    }


    //19.08.01퍼미션 관련 함수
    private void callPermission(){
        if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.ThreadPolicy policy
                    = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
            isPermission = true;

    }


    //19.08.06 GPS 업데이트 하는 부분
    @Override
    public void onLocationChanged(Location location) {

        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            gpsLat = location.getLatitude();
            gpsLon = location.getLongitude();
            tvGpsLatitude.setTextColor(getColor(R.color.setColor));
            tvGpsLongitude.setTextColor(getColor(R.color.setColor));
            tvGpsLatitude.setText(": " + Double.toString(gpsLat));
            tvGpsLongitude.setText((": " + Double.toString(gpsLon)));

            gpsLocationMsg = Double.toString(gpsLat)+","+Double.toString(gpsLon);

            //p2p 소켓통신 과 웹소켓 GPS관련건
/*            if(socket!=null) {
                try {
                    serverOut.writeUTF(gpsLocationMsg);
                } catch (IOException e) {
                }
            }else{
                handler.sendEmptyMessage(WEB_GPS_UPDATE);
            }*/

            // p2p 소켓통신이 복구가 될 시 아래의 코드는 삭제
            handler.sendEmptyMessage(WEB_GPS_UPDATE);

        }

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //19.08.06 GPS PROVIDER 일때 최소 2.1초마다 혹은 0미터 변동 되었을때 마다 리스너를 호출 한다.
        //19.11.14 주석처리
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

    }


    @Override
    public void onProviderDisabled(String provider) {

    }

}

//이하 언젠가 사용하게 될지 모르는 웹뷰 관련 무엇들
//        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
//        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부



