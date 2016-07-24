package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.changfeng.tcpdemo.socketclient.helper.SocketResponsePacket;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class MainActivity extends Activity implements Serializable {
    private static final String TAG = "MainActivity";
    private Toast mToast = null;//信息提示

    //网络部分所用的参数
    SocketClient socketClient;
    private SocketClient.SocketDelegate socketDelegate;
    private Handler handler;
    private byte[] inputByte;    //接收到的数据
    private byte[] outputByte;    //发送的数据
    private HashSet<String> busInfoSet;//存放车辆线路信息，数据无重复
    private LinkedList<CustomItemView> itemViewList;//存放item的list
    private HashMap<String, CustomItemView> busLineToItemMap;//将路线和item的view绑定
    private HashMap<String, LinkedList<InputDataParse>> busLineToDataMap;//线路和该线路的数据绑定
    private ArrayList<String> totalBusLine;
    private boolean isNewInputFromServer;//监控是否有服务器数据发来
    private boolean isUrgetToShow;//监控是否需要立刻显示
    private LinkedList<InputDataParse> inputParsedList;//接收到的数据原本

    //解析数据时候所用到的变量
//    private long timeStamp;//时间戳
//    private int userID = 871997;//设备ID
    InputDataParse inputDataParse;
    //接收到的自上一个act发过来的数据
    private Intent fromPreIntent;
    private String advertiseContent;//广告内容
    private int adFontSize;//广告字体大小
    private int itemNum;//每屏幕item个数
    private int itemCircle;//滚动周期
    private int busInfoFontSize;//发车信息字体大小
    private String serviceIP;//服务器地址
    private int portNumber;//端口号
    private String deviceIDNum;//设备ID
    private int timeFontSize;//公司信息和时间字体大小

    //sharedPreference默认的一些数据
    private static final String AD_CONTENT = "服务社会，奉献大爱！";
    private static final int AD_FONT_SIZE = 60;
    private static final int TIME_FONT_SIZE = 30;
    private static final int ITEM_NUM = 3;
    private static final int ITEM_CIRCLE = 6;
    private static final int BUS_INFO_FONT_SIZE = 50;
    private static final String DEVICE_ID_NUMBER = "871997";
    private static final int PORT_NUMBER = 50000;
    private static final String SERVICE_IP = "100.20.176.13";
    //动态实现布局所用参数
    private LinearLayout itemLayout;
    private static final float WEIGHT = 2.0f;//用来设置item以及表头的比例，使其能够占满整个布局
    private TextView currentTimeText, curWeather, companyInfoText;
    private String currentTime;
    private int colorSelect;
    //循环显示数据
    private Timer updateTextLoopTimer;
    private TimerTask updateTextLoopTimerTask;
    //循环查询数据
    private Timer inquiryInputTimer;
    private TimerTask inquiryInputTimerTask;
    //定时发送测试数据
//    private  Timer  sendVirtualDataTimer;
//    private TimerTask  SendVirtualDataTask;
//    private  ArrayList<byte[]> dataVertual;

    @Override
    protected void onCreate(Bundle savedInstancetate) {
        super.onCreate(savedInstancetate);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.mainActivityBg);
        setContentView(R.layout.activity_main);

        initValues();
        initMainLayout();
        initUI();
        initUIParameter();
//        initVirtualData();
        startInquiryInputTimer();
        startUpdateTextLoopTimer();
//        startSendDataTimer();

        Timer updateTimeTimer = new Timer();
        TimerTask updateTimeTask = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat startBus = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = startBus.format(new Date(System.currentTimeMillis()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTimeText.setText(currentTime);
                    }
                });
            }
        };
        updateTimeTimer.schedule(updateTimeTask, 500, 500);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
//                getWeather();
                if (socketClient == null) {
//                    socketClient = new SocketClient("100.20.176.13", 50000);//移动
                    socketClient = new SocketClient(serviceIP, portNumber);
                }
                socketClient.getHeartBeatHelper().setRemoteNoReplyAliveTimeout(1000 * 60);
                if (socketDelegate == null) {
                    socketDelegate = new SocketClient.SocketDelegate() {
                        @Override
                        public void onConnected(SocketClient client) {
                            if (socketClient.isConnected()) {
                                Log.i(TAG, "连接状态：" + socketClient.isConnected());
                                Log.i(TAG, "IP地址" + socketClient.getRemoteIP() + "端口：" + socketClient
                                        .getRemotePort());
                                showToast("已经连接上服务器！");
                            }
                            //向服务器发送第一条请求，根据返回结果判断
                            outputByte = dataToSend();
                            socketClient.send(outputByte);
                            Log.i(TAG, "客户端发送的数据：" + toHex(outputByte));
                        }

                        @Override
                        public void onDisconnected(SocketClient client) {
                            Log.i(TAG, "IP地址：" + socketClient.getRemoteIP() + " 端口：" + socketClient
                                    .getRemotePort());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (socketClient.isDisconnected() || socketClient.isConnecting()) {
                                        socketClient.connect();
                                        showToast("正在连接服务器！……");
                                    }
                                }
                            }, 1000 * 60);
                        }

                        @Override
                        public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
//                        //获取传送过来的数据
                            inputByte = responsePacket.getData();
                            if (inputByte == null) {
                                showToast("接收到的数据为空！");
                                Log.w(TAG, "接收到数据为空！");
                                return;
                            }
                            Log.i(TAG, "从服务器接收到的数据： " + toHex(inputByte));

                            inputDataParse = new InputDataParse(inputByte);

                            //如果数据可以被解析，也就是说明，是发车排队包
                            if (inputDataParse.inputParse()) {
                                if (inputDataParse.busNum == null || inputDataParse.busNum.isEmpty()) {
                                    Log.i(TAG, "onResponse: 自编号为空，不处理");
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //将数据加入数组以及暂存数组，
//                                            inputParsedList.addLast(inputDataParse);
                                        //判断有没有对应的item，应该将数据加入到那个线路的list中
//                                            showToast("接收到的数据可以被解析！");
                                        allocateItem(inputDataParse);
                                        //标志位置位
                                        isNewInputFromServer = true;
//                                            isUrgetToShow = true;
//
//                                            Log.i(TAG,"最新收到的数据信息是；车次：" + inputDataParse
//                                                    .busLineNum + "编号：" + inputDataParse
//                                                    .busNum + " 发车时间：" + inputDataParse
//                                                    .timeOfStartBus + "  " + inputDataParse
//                                                    .timeOfStartBusLong + "   " +
//                                                    inputDataParse.timeOfStartBusTotal);
                                    }
                                });
                            }
                        }
                    };
                }
                socketClient.registerSocketDelegate(socketDelegate);
//                socketClient.registerSocketDelegate(new SocketClient.SocketDelegate() {
//                    @Override
//                    public void onConnected(SocketClient client) {
//                        if(socketClient.isConnected()){
//                            Log.i(TAG,"连接状态：" + socketClient.isConnected());
//                            Log.i(TAG, "IP地址" + socketClient.getRemoteIP() + "端口：" + socketClient
//                                    .getRemotePort());
//                            showToast("已经连接上服务器！");
//                        }
//                        //向服务器发送第一条请求，根据返回结果判断
////                        socketClient.send(data());
//                        outputByte = dataToSend();
//                        socketClient.send(outputByte);
//                        Log.i(TAG,"客户端发送的数据" + toHex(outputByte));
//                    }
//                    @Override
//                    public void onDisconnected(SocketClient client) {
//                        if(socketClient.isDisconnected()){
//                            Log.i(TAG,"ondisconnected函数调用次数：" + MainActivity.this.count++);
//                            showToast("正在连接服务器！……");
//                        }
//                        Log.i(TAG,  socketClient.getState().toString());
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                socketClient.connect();
//                            }
//                        },10000);
//                    }
//                    @Override
//                    public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
////                        //获取传送过来的数据
//                         inputByte = responsePacket.getData();
//                        Log.i(TAG, toHex(inputByte));
//
//                        //首先判断接收到数据，然后再判断
//                        if(inputByte != null) {
////                            Toast.makeText(MainActivity.this,"接收到服务器发送的数据",Toast.LENGTH_SHORT)
////                                    .show();
//                            Log.i("接收数据不为空","判断后开始执行");
//                            //判断1，
//                            if (inputDataParse == null) {
//                                inputDataParse = new InputDataParse(inputByte);
//
//                            } else {
//                                inputDataParse = null;
//                                inputDataParse = new InputDataParse(inputByte);
//                            }
//                            if(inputDataParse.inputParse()){
//                                setText(inputDataParse);
//                            }
//
//                            try {
//                                if(file == null){
//                                    file = new File("dataFromServer");
//                                    file.createNewFile();
//                                }
//                                if (fileOutputStream == null) {
//                                    fileOutputStream = new FileOutputStream(file);
//                                    fileOutputStream = getApplication().openFileOutput(file
//                                            .getName(), Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
//                                }
//                                fileOutputStream.write(inputByte);
//                                Log.i(TAG,"文件路径为：" + file.getAbsolutePath() + ":" + file.getName
//                                        () + file.getPath());
//                                fileOutputStream.close();
//                            }catch(IOException e){
//                                e.printStackTrace();
//                            }
//
//                        }else{
//                            Toast.makeText(MainActivity.this,"没有接收到数据！",Toast.LENGTH_SHORT).show();
//                        }
//
//                        /*if(inputByte != null){
//                            if(inputDataParse ==null){
//                                inputDataParse = new InputDataParse(inputByte);
//                                inputDataParse.inputParse();
//                            }else{
//                                inputDataParse =null;
//                                inputDataParse = new InputDataParse(inputByte);
//                                inputDataParse.inputParse();
//                            }
//                            Log.i(TAG, "onResponse: set text:" + System.currentTimeMillis());
////                            receiveDataText.setText(""+System.currentTimeMillis());
//                            receiveDataText.setText("数据包类型：" + inputDataParse.packetType + "\n"
//                                    + "发送数据时间：" + inputDataParse.timeOfSendData +"\n"
//                                    + "序号：" + inputDataParse.num + "\n"
//                                    + "线路号：" + inputDataParse.roadLineNum + "\n"
//                                    + "车辆车次：" + inputDataParse.busLineNum + "\n"
//                                    + "车辆自编号：" + inputDataParse.busNum + "\n"
//                                    + "车辆发车时间：" + inputDataParse.timeOfStartBus + "\n"
//                                    + "司机姓名：" + inputDataParse.nameOfDriver  + "\n"
//                                    + "车次类型：" + inputDataParse.busType + "\n"
//                                    +  "接收数据个数：" + MainActivity.COUNT++);
//                        }else{
//                            receiveDataText.setText("没有接受到任何数据，检查你的连接！");
//                        }*/
//                    }
//                    });
                socketClient.connect();

            }
        }, 1000);
    }

    private void getWeather() {
        Parameters parameters = new Parameters();
        parameters.put("city", "kunming");
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free", ApiStoreSDK.GET,
                parameters, new ApiCallBack() {
                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i(TAG, "onSuccess调用，开始返回天气数据");
                        if (responseString != null) {
                            String weather = "暂无数据";


//                            Gson gson = new Gson();
//                            JsonParser jsonParser = new JsonParser();
//
//                            JsonObject jsonObject = jsonParser.parse(responseString)
//                                    .getAsJsonObject();
//                            JsonArray obj = jsonObject.get("daily_forecast").getAsJsonArray();
//
//                            for (int i =0;i< obj.size();i++){
//                            }
//


//                            for (int i = 0; i < jsonArray.size();i++){
//
//
//                            }

                            Log.i(TAG, "天气返回的数据：  " + responseString);

//
//                            try{
//                                JSONObject first = new JSONObject(responseString);
////                                JSONArray jsonArray = first.get
////                                JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//
////                                JSONArray array = jsonObject.getJSONArray("daily_forecast");
////                                JSONObject obj = array.getJSONObject(0);
////                               JSONObject subObj = obj.getJSONObject("cond");
////                                 weather = subObj.getString("txt_d");
//
//
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }


                        }

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete执行");
                    }

                    @Override
                    public void onError(int status, String responsingString, Exception e) {
                        Log.e(TAG, "onError: ", e);
//
//                        Log.e(TAG, "onError    " + (e == null ? " 没有错误" : e.getMessage()));
////                        curWeather.setText("接收到数据有误");

                    }
                });
    }

    /**
     * 主要用于描述个人思路
     * 创建时间：/2016/6/18  19:00
     *
     * @description :
     */
    private void initMainLayout() {
        itemLayout = (LinearLayout) findViewById(R.id.item_layout_ID);
        currentTimeText = (TextView) findViewById(R.id.currentTime_ID);
        companyInfoText = (TextView) findViewById(R.id.companyInfo_ID);

        curWeather = (TextView) findViewById(R.id.weather_ID);
//        itemLayout.setWeightSum(WEIGHT*itemNum);
//        mainLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        mainLayout.setDividerDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dividershape, null));
    }

    private void addItem() {
        //设置每个item的布局参数
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lp.weight = WEIGHT;
        //初始化item的view
        CustomItemView newCustomItem = new CustomItemView(MainActivity.this);
        if (colorSelect % 2 == 0) {
            newCustomItem.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.itemBgColor));
            newCustomItem.setNormalFontColor(ContextCompat.getColor(MainActivity.this, R.color.itemFontColor));
        } else {
            newCustomItem.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color
                    .itemAnotherBgColor));
            newCustomItem.setNormalFontColor(ContextCompat.getColor(MainActivity.this, R.color
                    .itemAnotherFontColor));
        }
        colorSelect++;
        itemViewList.add(newCustomItem);
        itemLayout.addView(newCustomItem, lp);
    }

    private void initUI() {
        for (int i = 0; i < itemNum; i++) {
            addItem();
        }
    }

    private void initUIParameter() {
        for (int i = 0; i < itemViewList.size(); i++) {
            CustomItemView item = itemViewList.get(i);
            item.setAdvertiseFont(adFontSize);
            item.setNormalFontSize(busInfoFontSize);
        }

        companyInfoText.setTextSize(timeFontSize);
        currentTimeText.setTextSize(timeFontSize * 0.7f);
    }

    private void initValues() {
        busInfoSet = new HashSet<>();
        itemViewList = new LinkedList<>();
        busLineToItemMap = new HashMap<>();
        busLineToDataMap = new HashMap<>();
        totalBusLine = new ArrayList<>();

        isNewInputFromServer = false;
        isUrgetToShow = false;
        colorSelect = 0;
        inputParsedList = new LinkedList<>();
        if (fromPreIntent == null) {
            fromPreIntent = getIntent();
        }
        Log.i(TAG, "intent的值" + fromPreIntent.toString() + "intent里面有：" + fromPreIntent.hasExtra
                ("发车信息字体大小"));
        handler = new Handler();

        //获得到的上一个activity发送过来的配置数据
//        dataResource = fromPreIntent.getStringExtra(getString(R.string.data_resource));
        //如果数据是通过点击设置完成按钮发送过来的，则使用发送过老来的数据，否则使用保存的数据

//        if (dataResource.equals(getString(R.string.change))){
//            itemNum = Integer.parseInt(fromPreIntent.getStringExtra(getString( R.string
//                    .item_number)));
//            itemCircle = Integer.parseInt(fromPreIntent.getStringExtra(getString(R.string
//                    .item_circle)));
//            busInfoFontSize = Integer.parseInt(fromPreIntent.getStringExtra(getString(R
//                    .string.bus_info_font_size)));
//            advertiseContent = fromPreIntent.getStringExtra(getString(R.string.ad_content));
//            adFontSize = Integer.parseInt(fromPreIntent.getStringExtra(getString(R.string
//                    .ad_font_size)));
//            //后期添加服务器服务器相关部分
//            deviceIDNum = Integer.parseInt(fromPreIntent.getStringExtra(getString(R.string
//                    .device_ID)));
//            serviceIP = fromPreIntent.getStringExtra(getString(R.string.service_IP));
//            portNumber = Integer.parseInt(fromPreIntent.getStringExtra(getString(R.string
//                    .port_number)));
//
//        }else if(dataResource.equals(getString(R.string.non_change))){
//            //
//            SharedPreferences  sharedPreferences  = getSharedPreferences("parameterSaved",Activity
//                    .MODE_PRIVATE);
//            itemNum = sharedPreferences.getInt(getString(R.string.item_number),ITEM_NUM);
//            busInfoFontSize = sharedPreferences.getInt(getString(R.string.bus_info_font_size),
//                    BUS_INFO_FONT_SIZE);
//            itemCircle  = sharedPreferences.getInt(getString(R.string.item_circle),ITEM_CIRCLE);
//            adFontSize = sharedPreferences.getInt(getString(R.string.ad_font_size),AD_FONT_SIZE);
//            advertiseContent = sharedPreferences.getString(getString(R.string.ad_content),AD_CONTENT);
//
//            deviceIDNum = sharedPreferences.getInt(getString(R.string.device_ID),DEVICE_ID_NUMBER);
//            portNumber  =  sharedPreferences.getInt(getString(R.string.port_number),PORT_NUMBER);
//            serviceIP  = sharedPreferences.getString(getString(R.string.service_IP),SERVICE_IP);
//
//        }
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.parameterSaved), Activity
                .MODE_PRIVATE);
        itemNum = sharedPreferences.getInt(getString(R.string.item_number), ITEM_NUM);
        busInfoFontSize = sharedPreferences.getInt(getString(R.string.bus_info_font_size),
                BUS_INFO_FONT_SIZE);
        itemCircle = sharedPreferences.getInt(getString(R.string.item_circle), ITEM_CIRCLE);
        adFontSize = sharedPreferences.getInt(getString(R.string.ad_font_size), AD_FONT_SIZE);
        advertiseContent = sharedPreferences.getString(getString(R.string.ad_content), AD_CONTENT);

        deviceIDNum = sharedPreferences.getString(getString(R.string.device_ID),
                DEVICE_ID_NUMBER);
        portNumber = sharedPreferences.getInt(getString(R.string.port_number), PORT_NUMBER);
        serviceIP = sharedPreferences.getString(getString(R.string.service_IP), SERVICE_IP);

        timeFontSize = sharedPreferences.getInt(getString(R.string.time_font_size), TIME_FONT_SIZE);


    }

    /**
     * 根据车辆路线对其进行分配item
     */
    private void allocateItem(InputDataParse input) {
        //遍历所有item，如果能够把路线加入则生成一个新的list维护该路线的数据.
        //思考，如果线路多于item个数怎么办？
        String line = input.busLineNum;
        for (int i = 0; i < itemViewList.size(); i++) {
            CustomItemView currentItem = itemViewList.get(i);
            //首先判断是否已经有item和该路线绑定
            if (line.equals(currentItem.getBusLine())) {
                busLineToDataMap.get(line).addFirst(input);
                return;
            }
            if (currentItem.setBusLine(line)) {
                Log.i(TAG, "第" + (i + 1) + "个item，没有绑定路线，开始绑定");

                LinkedList<InputDataParse> inputList = new LinkedList<>();
                inputList.add(input);
                busLineToItemMap.put(line, currentItem);
                busLineToDataMap.put(line, inputList);
                totalBusLine.add(input.busLineNum);
                Log.i(TAG, "能够添加路线，路线名;" + line + "该路线对应的第" + (i + 1) + "个item：");
                for (int j = 0; j < busLineToDataMap.get(line).size(); j++) {
                    Log.i(TAG, "该路线对应的数据有：" + busLineToDataMap.get(line).get(j).busLineNum);
                }
                break;
            }
        }
    }

    /**
     *用来测试数据
     */
//    private void initVirtualData(){
//        dataVertual  = new ArrayList<>();
//        byte[]  input_1 = {0x7E,0x00,0x6D,0x01,0x59,0x00,0x24,0x02, 0x03,0x0A,
//                0x03,0x01,0x00,0x00,(byte) 0x84,(byte)0xDB,
//                0x04,0x03,0x0E,
//                0x05,0x01,0x00,0x0D,0x4E,0x3D,
//                0x06,0x03, 0x02,
//                0x08,0x03,0x33,
//                0x07,0x03,0x04,
//                0x09,0x01,0x57,0x56,(byte) 0x90,0x6F,
//                0x0A,
//                0x03,0x01, 0x01, 0x02,0x00,0x02,0x02,0x01,0x00,0x00,(byte) 0x9E,(byte) 0xA2,
//                0x03,0x04,0x04,0x36,0x31, (byte) 0xC2, (byte) 0xB7,
//                0x05,0x04,0x04,0x34,0x33,0x30,0x33,
//                0x06,0x01,0x57,0x56,(byte) 0x91,0x68,
//                0x08,0x04,0x06, (byte) 0xBC,(byte) 0xDD,0x34,0x33,0x30,0x33,
//                0x09,0x03,0x01,
//                0x0B,0x04,0x14,0x31,0x36, 0x30, 0x36,0x30,0x37,0x31,0x37,0x31,0x31,0x31,0x39,0x36,0x32,0x35,0x39,0x30,0x31,0x31,0x35,
//                0x7F} ;
//
//        byte []  input_2  = input_1.clone();
//        //修改路线名称,一共五条线路
//        input_2[57] = 0x32;
//        byte []  input_3  = input_1.clone();
//        input_3[57] = 0x33;
//
//        byte []  input_4 = input_1.clone();
//        input_4[57] = 0x34;
//
//        byte []  input_5  = input_1.clone();
//        input_5[57] = 0x35;
//
//        //修改发车时间和编号
//        byte []  input_6 =   input_2.clone();
//        //编号
//        input_6[63] = 0x35;
//        input_6[64] = 0x38;
//        input_6[65] = 0x32;
//        input_6[66] = 0x36;
//        //时间
//        input_6[69] =(byte) 0x97;
//        input_6[70] = (byte) 0xE3;
//        input_6[71] = 0x72;
//        input_6[72] = 0x78;
//
//        byte []  input_7   = input_3.clone();
//        //编号
//        input_7[63] = 0x31;
//        input_7[64] = 0x36;
//        input_7[65] = 0x33;
//        input_7[66] = 0x38;
//        //时间
//        input_7[69] =(byte) 0x97;
//        input_7[70] = (byte) 0xD3;
//        input_7[71] = 0x53;
//        input_7[72] = 0x39;
//
//        byte []  input_8  = input_4.clone();
//        //编号
//        input_8[63] = 0x38;
//        input_8[64] = 0x32;
//        input_8[65] = 0x34;
//        input_8[66] = 0x36;
//        //时间
//        input_8[69] =(byte) 0xE2;
//        input_8[70] = (byte) 0xC3;
//        input_8[71] = (byte) 0x83;
//        input_8[72] = 0x27;
//
//        byte []  input_9 =   input_5.clone();
//        //编号
//        input_9[63] = 0x36;
//        input_9[64] = 0x34;
//        input_9[65] = 0x36;
//        input_9[66] = 0x38;
//        //时间
//        input_9[69] =(byte) 0xE8;
//        input_9[70] = (byte) 0xD1;
//        input_9[71] = 0x48;
//        input_9[72] = 0x29;
//
//        byte []  input_10 = new byte[109];
//        input_10 = input_1.clone();
//        //编号
//        input_10[63] = 0x33;
//        input_10[64] = 0x38;
//        input_10[65] = 0x31;
//        input_10[66] = 0x34;
//        //时间
//        input_10[69] =(byte) 0xB8;
//        input_10[70] = (byte) 0xC3;
//        input_10[71] = 0x53;
//        input_10[72] = 0x41;
//
//        dataVertual.add(input_1);
//        dataVertual.add(input_2);
//        dataVertual.add(input_3);
//        dataVertual.add(input_4);
//        dataVertual.add(input_5);
//        dataVertual.add(input_6);
//        dataVertual.add(input_7);
//        dataVertual.add(input_8);
//        dataVertual.add(input_9);
//        dataVertual.add(input_10);
//
//    }
//    private void startSendDataTimer(){
//        sendVirtualDataTimer = new Timer();
//        SendVirtualDataTask = new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int  index = (int) (Math.random()*10);
//                        Log.i(TAG,"模拟数据的index：  " + index);
//                        if (inputDataParse == null) {
//                            inputDataParse = new InputDataParse(dataVertual.get(index));
//                        } else {
//                            inputDataParse = null;
//                            inputDataParse = new InputDataParse(dataVertual.get(index));
//                        }
//                        if (inputDataParse.inputParse()){
//                            allocateItem(inputDataParse);
//                            isNewInputFromServer = true;
//                            Log.i("模拟数据","最新收到的数据信息是；车次：" + inputDataParse
//                                    .busLineNum + "编号：" + inputDataParse
//                                    .busNum + " 发车时间：" + inputDataParse
//                                    .timeOfStartBus + "  " + inputDataParse
//                                    .timeOfStartBusLong + "   " +
//                                    inputDataParse.timeOfStartBusTotal);
//                        }
//                    }
//                });
//
//            }
//        };
//        sendVirtualDataTimer.schedule(SendVirtualDataTask,100,1000*3);
//
//    }
//    private void stopSendDataTimer(){
//        sendVirtualDataTimer.cancel();
//        sendVirtualDataTimer = null;
//        SendVirtualDataTask.cancel();
//        SendVirtualDataTask = null;
//
//    }

    /**
     * 用于测试
     */
//开始周期监控是否有数据来，有就刷新
    private void startInquiryInputTimer() {
        inquiryInputTimer = new Timer();
        inquiryInputTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "startInquiryInputTimer开始执行，轮询输入数据");
                        //如果没有检测到新的输入，直接返回
                        if (!isNewInputFromServer) {
//                            showToast("轮询中，没有最新数据");
                            Log.i(TAG, "startInquiryInputTimer执行结束(没有更新UI)");
                            return;
                        } else {
                            /**
                             *首先确认属于那一条线路的数据，将其加入对应的list,如果没有对应的item,则不管
                             */

//                            if (inputDataParse.inputParse()) {
//                                String line = inputDataParse.busLineNum;
//
//                                if (totalBusLine.contains(inputDataParse.busLineNum)) {
//                                    stopUpdateTextLoopTimer();//停止滚动
//                                    LinkedList<InputDataParse> temp = busLineToDataMap.get(line);
//                                    if (temp.contains(inputDataParse)) {
//                                        temp.remove(inputDataParse);
//                                        temp.addFirst(inputDataParse);
//                                    } else {
//                                        temp.addFirst(inputDataParse);
//                                    }
//                                    isUrgetToShow = true;
//                                    startUpdateTextLoopTimer();
//                                }
//                            }
                            for (int k = 0; k < totalBusLine.size(); k++) {
//                            if (inputDataParse.inputParse()) {
//                                Log.i(TAG,"数据可以被解析，轮询开始！");
                                String line = inputDataParse.busLineNum;
                                if (line.equals(totalBusLine.get(k))) {
                                    stopUpdateTextLoopTimer();//停止滚动
                                    LinkedList<InputDataParse> temp = busLineToDataMap.get(line);
                                    if (temp.contains(inputDataParse)) {
                                        temp.remove(inputDataParse);
                                        temp.addFirst(inputDataParse);
                                    } else {
                                        temp.addFirst(inputDataParse);
                                    }

                                    isUrgetToShow = true;
                                    startUpdateTextLoopTimer();

//                            }
                                }
                            }

//                            showToast("轮询结束，更新了数据");
                            isNewInputFromServer = false;
                            Log.i(TAG, "startInquiryInputTimer执行结束(更新UI)");
                        }

                    }
                });
            }
        };
        inquiryInputTimer.schedule(inquiryInputTimerTask, 200, 80);
    }

    private void stopInquiryInputTimer() {
        inquiryInputTimer.cancel();
        inquiryInputTimerTask.cancel();
        inquiryInputTimerTask = null;
        inquiryInputTimer = null;
    }

    //开始循环显示
    private void startUpdateTextLoopTimer() {
        updateTextLoopTimer = new Timer();
        updateTextLoopTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "startUpdateTextLoopTimer开始执行");
                        long startUpdateTime = System.nanoTime();
                        //判断是否需要紧急展示
                        if (isUrgetToShow) {
                            long urgentStart = System.nanoTime();
                            for (int i = 0; i < itemViewList.size(); i++) {
                                CustomItemView currentView = itemViewList.get(i);
                                //首先判断是否有路线占有该item,没有就显示广告
                                if (currentView.getBusLine() != null) {
                                    String line = currentView.getBusLine();
                                    LinkedList<InputDataParse> tempList = busLineToDataMap.get(line);

                                    //如果是当前数据所在的item则当作最新数据显示，否则
                                    if (line.equals(inputDataParse.busLineNum)) {
                                        InputDataParse lastInput = tempList.getFirst();
                                        currentView.setLastText(lastInput.busLineNum, lastInput
                                                .busNum, lastInput.timeOfStartBus);
                                        currentView.setNormalFontSize(busInfoFontSize);
                                    } else {
                                        //如果不是按照最新数据显示则首先判断list中数据个数
                                        if (tempList.size() == 0) {
                                            currentView.setAdvertise(advertiseContent);
                                        } else {
                                            InputDataParse input = (InputDataParse) tempList.getFirst();
//
//                                                  currentView.setNormalText(input.busLineNum,
//                                                          input.busNum,input.timeOfStartBus);
//                                                  如果距离发车时间很近，则
                                            if (input.timeOfStartBusLong >= (System.currentTimeMillis() / 1000 - 120)) {
                                                itemViewList.get(i).setWillStartText(input
                                                        .busLineNum, input.busNum, input.timeOfStartBus);
                                                itemViewList.get(i).setNormalFontSize(busInfoFontSize);
                                            } else {
                                                itemViewList.get(i).setNormalText(input.busLineNum,
                                                        input.busNum, input.timeOfStartBus);
                                                itemViewList.get(i).setNormalFontSize
                                                        (busInfoFontSize);
                                            }
                                            //显示过后将第一个放到最后
                                            if (tempList.size() >= 2) {
                                                InputDataParse in = (InputDataParse) tempList
                                                        .removeFirst();
                                                tempList.addLast(in);
                                            }

                                        }
                                    }
                                } else {
                                    //否则显示广告
                                    currentView.setAdvertise(advertiseContent);
                                    currentView.setAdvertiseFont(adFontSize);
                                }
                            }
                            //显示完后将第一个放到最后
                            Log.i(TAG, "isUrgetoShow为true，显示第一个，然后放到最后");
//                                    for (int i= 0; i< totalBusLine.size();i++){
//                                        String line = totalBusLine.get(i);
//                                        if (busLineToDataMap.get(line).size() >=2){
//                                            InputDataParse input =   busLineToDataMap.get(line).removeFirst();
//                                            busLineToDataMap.get(line).addLast(input);
//                                        }
//                                    }
                            //标志位置为false
                            isUrgetToShow = false;
                            long urgentEnd = System.nanoTime();
                            Log.i(TAG, "紧急显示结束，共用时：（十分之一毫秒）：" + (urgentEnd - urgentStart) / 100000);
                            return;
                        } else {
                            /**
                             如果不是急需要显示数据，则//共有三种显示方式
                             */
                            long nomalStart = System.nanoTime();
//                             showToast("不需要紧急显示数据，正常显示");
                            for (int i = 0; i < itemViewList.size(); i++) {
                                Log.i(TAG, "无需紧急显示时候，正常显示，开始执行！");

                                CustomItemView item = itemViewList.get(i);
                                //如果item对应一个路线则按照几种显示方式进行显示
                                if (item.getBusLine() != null) {
                                    String line = itemViewList.get(i).getBusLine();
                                    LinkedList dataList = busLineToDataMap.get(line);
                                    //该路线的数据不为0个
                                    if (dataList.size() != 0) {
                                        InputDataParse inputData = busLineToDataMap.get(line)
                                                .getFirst();
                                        //如果距离发车时间很近，则
//                                         item.setNormalText(inputData.busLineNum,inputData
//                                                 .busNum,inputData.timeOfStartBus);
                                        if (inputData.timeOfStartBusLong >= (System
                                                .currentTimeMillis() / 1000 - 120)) {
                                            item.setWillStartText(inputData.busLineNum,
                                                    inputData.busNum, inputData.timeOfStartBus);
                                        } else {
                                            item.setNormalText(inputData.busLineNum,
                                                    inputData.busNum, inputData.timeOfStartBus);
                                        }
                                        //移动数据，如果数据个数大于等于2个则把第一个换到最后，否则不动
                                        if (dataList.size() >= 2) {
                                            InputDataParse in = (InputDataParse) dataList.removeFirst();
                                            dataList.addLast(in);
                                        }
                                    } else {
                                        item.setAdvertise(advertiseContent);
                                    }
                                } else {
                                    //否则显示广告
                                    item.setAdvertise(advertiseContent);
                                }
                            }
                            long nomalEnd = System.nanoTime();
                            Log.i(TAG, "正常显示结束，共用时：（十分之一毫秒）" + (nomalEnd - nomalStart) / 100000);
                        }

                        //   删除已经过了发车时间的数据
                        long deleteStart = System.nanoTime();
                        for (int i = 0; i < totalBusLine.size(); i++) {
                            String line = totalBusLine.get(i);
                            LinkedList<InputDataParse> tempInputList = busLineToDataMap.get(line);
                            LinkedList<InputDataParse> outOfTimeData = new
                                    LinkedList<InputDataParse>();
                            ListIterator<InputDataParse> iterator = tempInputList.listIterator();
                            Log.i(TAG, "删除已经发车数据前，" + line + "中数据个数是：" + tempInputList.size() + " " +
                                    "    ");
                            for (InputDataParse in : tempInputList) {
                                Log.i(TAG, line + "对应的数据有" + in.toString() + "   发车时间的时间戳： " + in
                                        .timeOfStartBusTotal);
                            }
//                            for (int j =0;j<tempInputList.size();j++){
//                                InputDataParse  data  = tempInputList.get(j);
//                                if (data.timeOfStartBusLong <= System.currentTimeMillis()/1000 ){
//                                    outOfTimeData.addLast(data);
//                                }
//                            }
//                            tempInputList.removeAll(outOfTimeData);

                            while (iterator.hasNext()) {
                                if (((InputDataParse) iterator.next()).timeOfStartBusLong <=
                                        System.currentTimeMillis() / 1000) {
                                    Log.i(TAG, "该条数据已经过了发车时间,将其删除");
                                    iterator.remove();
                                }
                            }

                            Log.i(TAG, "删除已经发车数据hou，" + line + "中数据：");
                            for (InputDataParse in : tempInputList) {
                                Log.i(TAG, in.toString() + "   " + in.timeOfStartBusTotal);
                            }
                        }
                        long deleteEnd = System.nanoTime();
                        Log.i(TAG, "删除已经发车的数据，共用时：（十分之一毫秒）" + (deleteEnd - deleteStart) / 100000);

                        //查重
                        long doubleStart = System.nanoTime();

                        for (int i = 0; i < totalBusLine.size(); i++) {
                            //得到该路线名字以及对应的数据list
                            String line = totalBusLine.get(i);
                            LinkedList<InputDataParse> tempList = busLineToDataMap.get(line);
                            LinkedList<InputDataParse> temp = new LinkedList<>();
                            Log.i(TAG, "查重前各list中数据：");
                            for (InputDataParse in : tempList) {
                                Log.i(TAG, line + "路线中的数据有：" + in.toString() + "   " + in
                                        .timeOfStartBusTotal);
                            }

                            //如果该路线所对应的数据个数大于2个，对其进行查重
                            if (tempList.size() >= 2) {
                                for (int j = 0; j < tempList.size(); j++) {
                                    for (int k = j + 1; k < tempList.size(); k++) {
                                        if (tempList.get(j).timeOfStartBusLong == tempList.get(k).timeOfStartBusLong) {
                                            temp.add(tempList.get(k));
                                        }
                                    }
                                }
                                tempList.removeAll(temp);
                            }
                            Log.i(TAG, "查重后各list中数据：");
                            for (InputDataParse in : tempList) {
                                Log.i(TAG, line + "路线中的数据有（查重后）：" + in.toString() + "   " + in
                                        .timeOfStartBusTotal);
                            }

                        }
                        long doubleEnd = System.nanoTime();
                        Log.i(TAG, "排序执行结束,（十分之一毫秒）：" + (doubleEnd - doubleStart) / 100000);

                        //排序
                        long sortStart = System.nanoTime();
                        for (int i = 0; i < totalBusLine.size(); i++) {
                            String line = totalBusLine.get(i);
                            LinkedList<InputDataParse> tempList = busLineToDataMap.get(line);
                            if (tempList.size() >= 2) {
                                Collections.sort(tempList, new Comparator<InputDataParse>() {
                                    @Override
                                    public int compare(InputDataParse obj1, InputDataParse obj2) {
                                        if (obj1.timeOfStartBusLong < obj2.timeOfStartBusLong) {
                                            return -1;
                                        } else if (obj1.timeOfStartBusLong == obj2
                                                .timeOfStartBusLong) {
                                            return 0;
                                        } else {
                                            return 1;
                                        }
                                    }
                                });
                            }
                        }
                        long sortEnd = System.nanoTime();
                        Log.i(TAG, "排序执行结束,（十分之一毫秒）：" +
                                (sortEnd - sortStart) / 100000);
                        long endUpdateTime = System.nanoTime();
                        Log.i(TAG, "startUpdateTextLoopTimer执行结束,循环更新一次所用时间：（毫秒）" +
                                (endUpdateTime - startUpdateTime) / 1000000);
                    }
                });
            }
        };
        updateTextLoopTimer.schedule(updateTextLoopTimerTask, 50, itemCircle * 1000);
    }

    private void stopUpdateTextLoopTimer() {
        updateTextLoopTimer.cancel();
        updateTextLoopTimerTask.cancel();
        updateTextLoopTimer = null;
        updateTextLoopTimerTask = null;
    }

    private class InputDataParse implements Cloneable {
        private final String Tag = "InputDataParse内部类，解析数据";
        String packetType;//包类型
        String timeOfSendData;//发送数据时间
        //包体段
        String roadLineNum;//线路号
        String busLineNum;//车辆车次
        String busNum;//车辆自编号
        String timeOfStartBus;//车辆发车时间,只有时分，
        long timeOfStartBusLong;//车辆发车时间的时间戳
        String timeOfStartBusTotal;//发车时间，完全格式
        String nameOfDriver;//司机名字
        String busType;//车次类型
        String numOfBusWillStart;//发车编号
        int index;//负责跳转到每个子包的头一项
        int lenOfSubPacket;//负责记录该子包的长度
        private byte[] input = null;//持有接收到的数据的备份；

        InputDataParse(@NonNull byte[] input) {
            this.input = input;
            this.index = 0;
            this.timeOfStartBusLong = 0L;
        }

        @Override
        public InputDataParse clone() {
            InputDataParse input = null;
            try {
                input = (InputDataParse) super.clone();

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                Log.i(Tag, "对象复制不支持，异常！");
            }
            return input;
        }

        private boolean inputParse() {
//            判断包头和长度
            if (input[0] != 0x7E) {
                showToast("服务器发送数据格式错误！");
                return false;
            }

//            if (input[0] == 0x7E) {
//                int high =  (input[1] & 0xFF)<< 8;
//                int low = (input[2] & 0xFF);
//                int length = (low + high);
//                if (input.length != length){
//                    showToast("数据包长度不正确！");
//                    return false;
//                }

            if (input[4] != 0x59) {
                Log.i(TAG, "inputParse() 不是发车屏数据，不处理");
                return false;
            }

//            if (input[4] == 0x59) {
            //包头段长度
            int highOfPacketHeadLen = (input[5] & 0xFF) << 8;
            int lowOfPacketHeadLen = (input[6] & 0xFF);
            //时间戳,发送数据时间
//                    Long firstStamp = Long.parseLong(String.valueOf((input[36] & 0xFF) << 24));
//                    Long secondStamp = Long.parseLong(String.valueOf((input[37] & 0xFF) << 16));
//                    Long thirdStamp = Long.parseLong(String.valueOf((input[38] & 0xFF) << 8));
//                    Long fourStamp = Long.parseLong(String.valueOf((input[39] & 0xFF)));
//                    Long timeStamp = firstStamp + secondStamp + thirdStamp + fourStamp;
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    timeOfSendData = simpleDateFormat.format(new Date(timeStamp * 1000));
//
////                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            index = 7 + (highOfPacketHeadLen + lowOfPacketHeadLen);//跳转到包体段第一项；
            //包体段第一项--序号
            if (input[index] == 0x01) {
                int first, second, third, four;
                lenOfSubPacket = input[index + 1];//每个子包的第二项是该子包数据项长度，记录下；
//                        num = String.valueOf(input[index + 2]);
                nextSubPacketFirstItem(lenOfSubPacket);
                //包体段第二项--线路号
                if (input[index] == 0x02) {
                    lenOfSubPacket = input[index + 1];

//                            first = (input[index + 2] & 0xFF) << 24;
//                            second = (input[index + 3] & 0xFF) << 16;
//                            third = (input[index + 4] & 0xFF) << 8;
//                            four = input[index + 5] & 0xFF;//如果不和0xFF与的话结果不对，这是因为byte和int的差别
//                            roadLineNum = String.valueOf(first + second + third + four);
                    nextSubPacketFirstItem(lenOfSubPacket);
                }
                //包体段第三项--班次名称
                if (input[index] == 0x03) {
                    Log.i(Tag, "第三项，车辆路线index的值：" + index);
                    lenOfSubPacket = input[index + 1];
                    int valueLength = getLenOfSubPacketValue(lenOfSubPacket);
                    byte[] busLine = new byte[valueLength];
                    System.arraycopy(input, (index + 3), busLine, 0, busLine.length);
                    try {
                        String str = new String(busLine, "GBK");
                        Log.i(Tag, "路线的名字：  " + str + "     " + busLine.length);
                        busLineNum = str;
                    } catch (UnsupportedEncodingException e) {
                        busLineNum = "数据解析错误！";
                    }
                    nextSubPacketFirstItem(lenOfSubPacket);
                }
                //包体段第四项--车辆自编号
                if (input[index] == 0x05) {
                    Log.i(Tag, "第四项，车辆自编号的index值：" + index);

                    lenOfSubPacket = input[index + 1];
                    byte[] bus = new byte[getLenOfSubPacketValue(lenOfSubPacket)];
                    System.arraycopy(input, (index + 3), bus, 0, bus.length);
                    try {
                        String str = new String(bus, "GBK");
                        Log.i(Tag, "车辆自编号:  " + str + "    " + bus.length);
                        busNum = str;
                    } catch (UnsupportedEncodingException e) {
                        busNum = "数据解析错误！";
                    }
                    nextSubPacketFirstItem(lenOfSubPacket);
                }
                //包体段第五项--发车时间
                if (input[index] == 0x06) {
                    lenOfSubPacket = input[index + 1];

                    Long timeStampFirst = Long.parseLong(String.valueOf((input[index + 2] & 0xFF)
                            << 24));
                    Long timeStampSecond = Long.parseLong(String.valueOf((input[index + 3] &
                            0xFF) << 16));
                    Long timeStampThird = Long.parseLong(String.valueOf((input[index + 4] &
                            0xFF)
                            << 8));
                    Long timeStampFour = Long.parseLong(String.valueOf((input[index + 5] &
                            0xFF)));
                    Long busStartTime = timeStampFirst + timeStampSecond + timeStampThird +
                            timeStampFour;
                    timeOfStartBusLong = busStartTime;
                    SimpleDateFormat startBusTotal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timeOfStartBusTotal = startBusTotal.format(new Date(busStartTime * 1000));
                    SimpleDateFormat startBus = new SimpleDateFormat("HH:mm");
                    timeOfStartBus = startBus.format(new Date(busStartTime * 1000));
                    nextSubPacketFirstItem(lenOfSubPacket);
                }
                //包体段第六项--驾驶员姓名
//                        if (input[index] == 0x08) {
////                            lenOfSubPacket = input[index + 1];
////                            byte[] name = new byte[getLenOfSubPacketValue(lenOfSubPacket)];
////                            System.arraycopy(input, (index + 3), name, 0, name.length);
////                            try {
////                                String str = new String(name, "GBK");
////                                nameOfDriver = str;
////                            } catch (UnsupportedEncodingException e) {
////                                nameOfDriver = "数据解析错误！";
////                            }
////                            nextSubPacketFirstItem(lenOfSubPacket);
//                        }
                //包体段第七项--车次类型
//                        if (input[index] == 0x09) {
//                            lenOfSubPacket = input[index + 1];
//                            if (input[index + 2] == 1) {
//                                busType = "营运车辆";
//                            } else {
//                                busType = "非营运车辆";
//                            }
//                        }
            } else {
            }
//            } else {
////                    showToast("不是发车排队数据包！");
//                return false;
//            }
//            }
            return true;
        }

        private void parsed() {
        }

        //组合子包中的数据，然后返回；
        private void nextSubPacketFirstItem(int lenOfPacketValue) {
            switch (lenOfPacketValue) {
                case 1:
                    //四个字节
                    index += 6;
                    break;
                case 2:
                    //两个字节
                    index += 4;
                    break;
                case 3:
                    //一个字节
                    index += 3;
                    break;
                case 4:
                    //未知，需要下一位
                    int subItemLen = this.input[index + 2];
                    index += (2 + subItemLen + 1);
                    break;
                default:
                    index = -1;//表示数据类型有误
                    break;
            }
            Log.i("nextSubPacketFirstItem", "index的值是：" + index);
        }

        private int getLenOfSubPacketValue(int dataType) {
            int lenOfSubPacketValue;
            switch (dataType) {
                case 1:
                    lenOfSubPacketValue = 4;
                    break;
                case 2:
                    lenOfSubPacketValue = 2;
                    break;
                case 3:
                    lenOfSubPacketValue = 1;
                    break;
                case 4:
                    lenOfSubPacketValue = input[index + 2];
                    break;
                default:
                    lenOfSubPacketValue = 0;
                    break;
            }
            return lenOfSubPacketValue;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = getSharedPreferences("parameterSaved", Activity
                .MODE_PRIVATE);
        itemNum = sharedPreferences.getInt(getString(R.string.item_number), ITEM_NUM);
        busInfoFontSize = sharedPreferences.getInt(getString(R.string.bus_info_font_size),
                BUS_INFO_FONT_SIZE);
        itemCircle = sharedPreferences.getInt(getString(R.string.item_circle), ITEM_CIRCLE);
        adFontSize = sharedPreferences.getInt(getString(R.string.ad_font_size), AD_FONT_SIZE);
        advertiseContent = sharedPreferences.getString(getString(R.string.ad_content), AD_CONTENT);

        deviceIDNum = sharedPreferences.getString(getString(R.string.device_ID),
                DEVICE_ID_NUMBER);
        portNumber = sharedPreferences.getInt(getString(R.string.port_number), PORT_NUMBER);
        serviceIP = sharedPreferences.getString(getString(R.string.service_IP), SERVICE_IP);


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, " onstop函数执行");
        fromPreIntent = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, " onPause函数执行");
        if (mToast != null) {
            mToast.cancel();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, " ondestroy函数执行");
        if (socketClient.isConnected()) {
            socketClient.disconnect();
        }
        if (socketClient != null) {
            for (int i = 0; i < socketClient.getSocketDelegates().size(); i++) {
                socketClient.removeSocketDelegate(socketClient.getSocketDelegates().get(i));
            }
            socketDelegate = null;
        }
        socketClient = null;
        if (inputParsedList != null) {
            inputParsedList.clear();
            inputParsedList = null;
        }
        stopInquiryInputTimer();
        stopUpdateTextLoopTimer();

    }

    private String toHex(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            builder.append(String.format(" %02x", b));
        }
        return builder.toString().toUpperCase();
    }

    private byte[] dataToSend() {
        outputByte = new byte[38];
        //1.固定开始部分
        outputByte[0] = 0x7E;
        //总长度
        outputByte[1] = 0x00;
        outputByte[2] = 0x26;
        outputByte[3] = 0x01;
        //消息帧类型
        outputByte[4] = 0x01;
        //包头段长度
        outputByte[5] = 0x00;
        outputByte[6] = 0x15;
        //2.包头段
        //原类型
        outputByte[7] = 0x02;
        outputByte[8] = 0x03;
        outputByte[9] = 0x0E;
        //身份标识，原地址
        outputByte[10] = 0x03;
        outputByte[11] = 0x01;
        int id = Integer.parseInt(deviceIDNum.trim());
        byte firstByteID = (byte) ((id >> 24) & 0xFF);
        byte secondByteID = (byte) ((id >> 16) & 0xFF);
        byte thirdByteID = (byte) ((id >> 8) & 0xFF);
        byte fourByteID = (byte) (id & 0xFF);
        Log.i(TAG, "dataTosend函数中的设备ID：" + deviceIDNum);
        outputByte[12] = firstByteID;
        outputByte[13] = secondByteID;
        outputByte[14] = thirdByteID;
        outputByte[15] = fourByteID;

        outputByte[16] = 0x07;
        outputByte[17] = 0x03;
        outputByte[18] = 0x04;
        //回应字段
        outputByte[19] = 0x06;
        outputByte[20] = 0x03;
        outputByte[21] = 0x04;
        //时间戳
        outputByte[22] = 0x09;
        outputByte[23] = 0x01;
        long timeStamp = System.currentTimeMillis() / 1000;
        Log.i(TAG, "sendTodata中时间戳：" + timeStamp);

        byte firstByte = (byte) ((timeStamp >> 24) & 0xFF);
        byte secondByte = (byte) ((timeStamp >> 16) & 0xFF);
        byte thirdByte = (byte) ((timeStamp >> 8) & 0xFF);
        byte fourByte = (byte) (timeStamp & 0xFF);

        outputByte[24] = firstByte;
        outputByte[25] = secondByte;
        outputByte[26] = thirdByte;
        outputByte[27] = fourByte;
        //3.包体段
        //身份标识
        outputByte[28] = 0x02;
        outputByte[29] = 0x01;
        outputByte[30] = firstByteID;
        outputByte[31] = secondByteID;
        outputByte[32] = thirdByteID;
        outputByte[33] = fourByteID;
        //登录原因
        outputByte[34] = 0x03;
        outputByte[35] = 0x03;
        outputByte[36] = 0x02;
        //4.包尾
        outputByte[37] = 0x7F;
        return outputByte;

    }

    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
//    private byte[] data(){
//        dataByte = new byte[35];
//        //1.固定开始部分
//        dataByte[0] = 0x7E;
//        //总长度
//        dataByte[1] = 0x00;
//        dataByte[2] = 0x23;
//
//        dataByte[3] = 0x01;
//        //消息帧类型
//        dataByte[4] = 0x01;
//        //包头段长度
//        dataByte[5] = 0x00;
//        dataByte[6] = 0x12;
//        //2.包头段
//        //原类型
//        dataByte[7] = 0x02;
//
//        dataByte[8] = 0x03;
//        dataByte[9] = 0x0E;
//        //身份标识，原地址
//        dataByte[10] = 0x03;
//        dataByte[11] = 0x01;
//        byte firstByteID =(byte) ((userID >> 24) & 0x000000FF) ;
//        byte secondByteID = (byte)((userID >> 16) & 0x000000FF);
//        byte thirdByteID = (byte)((userID >> 8) & 0x000000FF);
//        byte fourByteID = (byte)(userID & 0x000000FF);
//        dataByte[12] = firstByteID;
//        dataByte[13] = secondByteID;
//        dataByte[14] = thirdByteID;
//        dataByte[15] = fourByteID;
//
//       //回应字段
//        dataByte[16] = 0x06;
//        dataByte[17] = 0x03;
//        dataByte[18] = 0x04;
//        //时间戳
//        dataByte[19] = 0x09;
//        dataByte[20] = 0x01;
//        timeStamp = System.currentTimeMillis();
//        Log.i(TAG,"时间戳：" + timeStamp);
//
//        byte firstByte = (byte) ((timeStamp >> 24) & 0X000000FF);
//        byte secondByte =(byte) ((timeStamp >>16) & 0X000000FF);
//        byte thirdByte =(byte)( (timeStamp >> 8 ) & 0x000000FF);
//        byte fourByte = (byte)(timeStamp & 0x000000FF);
//
//        dataByte[21] = firstByte;
//        dataByte[22] = secondByte;
//        dataByte[23] = thirdByte;
//        dataByte[24] = fourByte;
//        //3.包体段
//        //身份标识
//        dataByte[25] = 0x02;
//        dataByte[26] = 0x01;
//        dataByte[27] = firstByteID;
//        dataByte[28] = secondByteID;
//        dataByte[29] = thirdByteID;
//        dataByte[30] = fourByteID;
//        //登录原因
//        dataByte[31] = 0x03;
//        dataByte[32] = 0x03;
//        dataByte[33] = 0x01;
//        //4.包尾
//        dataByte[34] = 0x7F;
//        return dataByte;
//    }
//    private byte[] data{
//        /*dataToSend = new byte[100];
//        //1.固定开始部分
//        dataToSend[0] = 0x7E;
//
//        dataToSend[1] = 0x00;
//        dataToSend[2] = 0x26;
//
//        dataToSend[3] = 0x01;
//        dataToSend[4] = 0x01;
//        //包头段长度
//        dataToSend[5] = 0x00;
//        dataToSend[6] = 0x;
//        //2.包头段
//        //2.1源地址组织
//        dataToSend[]=0x01;
//        dataToSend[]=0x01;
//        dataToSend[]=0x;
//        dataToSend[]=0x;
//        dataToSend[]=0x;
//        dataToSend[]=0x;
//        //2.2原类型
//        dataToSend[]=0x02;
//        dataToSend[]=0x03;
//        dataToSend[]=0x0;
//        //2.3源地址
//        byte firstByteID =(byte) ((userID >> 24) & 0x000000FF) ;
//        byte secondByteID = (byte)((userID >> 16) & 0x000000FF);
//        byte thirdByteID = (byte)((userID >> 8) & 0x000000FF);
//        byte fourByteID = (byte)(userID & 0x000000FF);
//        outputByte[12] = firstByteID;
//        outputByte[13] = secondByteID;
//        outputByte[14] = thirdByteID;
//        outputByte[15] = fourByteID;
//
//        dataToSend[]=0x03;
//        dataToSend[]=0x01;
//        dataToSend[]=firstByteID;
//        dataToSend[]=secondByteID;
//        dataToSend[]=thirdByteID;
//        dataToSend[]=fourByteID;
//        //2.4目标类型
//        dataToSend[]=0x04;
//        dataToSend[]=0x03;
//        dataToSend[]=0x;
//        //2.5目标地址
//        dataToSend[]=0x05;
//        dataToSend[]=0x01;
//        dataToSend[]=0x;
//        dataToSend[]=0x;
//        dataToSend[]=0x;
//        dataToSend[]=0x;
//
//        //2.6回应字段
//        dataToSend[]=0x06;
//        dataToSend[]=0x03;
//        dataToSend[]=0x03;
//        //2.8顺序号
//
//
//        //2.9时间戳包
//        timeStamp = System.currentTimeMillis();
//        Log.i(TAG,"时间戳：" + timeStamp);
//
//        byte firstByte = (byte) ((timeStamp >> 24) & 0X000000FF);
//        byte secondByte =(byte) ((timeStamp >>16) & 0X000000FF);
//        byte thirdByte =(byte)( (timeStamp >> 8 ) & 0x000000FF);
//        byte fourByte = (byte)(timeStamp & 0x000000FF);
//
//        dataToSend[]=0x09;
//        dataToSend[]=0x01;
//        dataToSend[]= firstByte;
//        dataToSend[]=secondByte;
//        dataToSend[]=thirdByte;
//        dataToSend[]=fourByte;
//
//        //3.包体段
//        //3.1
//        //3.2源id
//        dataToSend[] = 0x02;
//        dataToSend[] = 0x01;
//        dataToSend[12] = firstByteID;
//        dataToSend[13] = secondByteID;
//        dataToSend[14] = thirdByteID;
//        dataToSend[15] = fourByteID;
//        //3.3登录原因
//        dataToSend[] = 0x03;
//        dataToSend[] = 0x03;
//        dataToSend[] = 0x0E;
//
//
//        //4.包尾
//        dataToSend[]=0x0;*/
////    }
}

