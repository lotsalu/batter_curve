package archermind.com.batter_curve;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

public class MainActivity extends Activity {

    String sDCardString = android.os.Environment.getExternalStorageState();
    File SDFile = android.os.Environment.getExternalStorageDirectory();
    File myFile = new File(SDFile.getAbsolutePath() + File.separator + "ST.txt");
    private TextView batteryTV;
    private TextView logView;
    private Button CreateFile;
    int battery;
    String batteryString;
    String timeStr;
    private ScrollView  slview;
    private FileOutputStream outputStream1;
    private FileOutputStream outputStream2;
    float batteryPct;
    String batteryPctString;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd" + "  " + "HH:mm:ss" + "  ");


    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            Log.i("test", "health: " + health);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            Log.i("test", "voltage: " + voltage);
            battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//            batteryString = String.valueOf(battery);
            //当前剩余电量
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//电量最大值
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//电量百分比
            batteryPct = level / (float) scale;
//            batteryPctString = String.valueOf(batteryPct);
            batteryString = String.valueOf(battery) + "%  \n ";
            batteryPctString = String.valueOf((batteryPct) * 100) + "%  \n ";

            Log.i("test", "batteryPct: " + batteryPct);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            Log.e("test", String.valueOf(isCharging));

        }
    };

    Handler mhHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    slview.fullScroll(ScrollView.FOCUS_DOWN);
                    batteryTV.setText(batteryString + "\n");
                    logView.append(timeStr+" ");
                    logView.append(batteryString + "\n");

            }
        }
    };

    //    while (true){
//        battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//        Log.i("test", "batter: " + battery);
//        batteryString = String.valueOf(battery) + "%  ";
//
//        try {
//            outputStream1.write(batteryString.getBytes());
//            Thread.sleep(1000);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        MainActivity.this.mhHandler.sendEmptyMessage(3);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        batteryTV = (TextView) findViewById(R.id.tvBattery);
        MainActivity.this.mhHandler.sendEmptyMessage(1);
        logView = (TextView) findViewById(R.id.logView);
        slview = (ScrollView) findViewById(R.id.slview);
        MainActivity.this.mhHandler.sendEmptyMessage(2);
        new CreateFiles();

        CreateFile = (Button) findViewById(R.id.CreateFile);
        CreateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == CreateFile) {
//                    SDCardTest();
                    new Thread(new MyThread()).start();
                }
            }
        });

    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            CreateDir();

//            try {
//                outputStream1 = new FileOutputStream(myFile);
//                outputStream1.write(szOutText.getBytes());
//                outputStream1.write(batteryPctString.getBytes());
////                outputStream1.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            writeLogFile();
//                    outputStream1.write(batteryPctString.getBytes());
//                    outputStream1.flush();
//                    outputStream1.close();
            Message message = new Message();
            message.what = 3;
            mhHandler.sendMessage(message);// 发送消息
        }
    }


    public void CreateDir() {

        Log.i("TAG", sDCardString);

        if (sDCardString.equals(Environment.MEDIA_MOUNTED)) {
//            File SDFile = android.os.Environment.getExternalStorageDirectory();
//            File myFile = new File(SDFile.getAbsolutePath() + File.separator + "ST.txt");
            if (!myFile.exists()) {
                try {
                    myFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    myFile.delete();
                    myFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void writeLogFile() {
        try {
            outputStream1 = new FileOutputStream(myFile,true);
            String szOutText = "开始测试\n\n";
            String enterText = "\n";
            outputStream1.write(szOutText.getBytes());

            outputStream1.close();

        while (true) {
            float timeMillis = System.currentTimeMillis();
            Date curDate = new Date(System.currentTimeMillis());
            timeStr = formatter.format(curDate);
            Log.i("test", "*************************");

            outputStream2 = new FileOutputStream(myFile,true);
            outputStream2.write(timeStr.getBytes());
            outputStream2.write(batteryString.getBytes());
            outputStream2.flush();
            outputStream2.close();
            mhHandler.sendEmptyMessage(1);
            Thread.sleep(60000);
        }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
        }
//        String sDCardString = android.os.Environment.getExternalStorageState();
//                Environment.MEDIA_MOUNTED_READ_ONLY
//        } else if((android.os.Environment.getExternalStorageState().) {
//            File SDFile = android.os.Environment.getExternalStorageDirectory();
//            File myFile = new File(SDFile.getAbsolutePath() + File.separator + "my.txt");
//            if (myFile.exists()) {
//                try {
//                    FileInputStream inputStream = new FileInputStream(myFile);
//                    byte[] buffer = new byte[1024];
//                    inputStream.read(buffer);
//                    inputStream.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//

//            }
    }
}


