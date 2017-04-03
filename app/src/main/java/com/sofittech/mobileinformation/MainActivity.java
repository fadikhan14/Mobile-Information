package com.sofittech.mobileinformation;

import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;
import java.lang.System;
import java.lang.String;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {



    TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9;
    TelephonyManager telephonyManager;
    myPhoneStateListener psListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        psListener = new myPhoneStateListener();
        telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(psListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


        textView9=(TextView)findViewById(R.id.tv9);


        textView1=(TextView)findViewById(R.id.tv1);
        textView1.setText(getMacAddr(MainActivity.this));

        textView2=(TextView)findViewById(R.id.tv2);
        textView2.setText(getIpAddr(MainActivity.this));

        textView3=(TextView)findViewById(R.id.tv3);
        textView3.setText(Build.MODEL);// device model

        textView4=(TextView)findViewById(R.id.tv4);
        textView4.setText(Build.MANUFACTURER); // Device name
//
        textView5=(TextView)findViewById(R.id.tv5);
        textView5.setText(Build.VERSION.RELEASE); // os version
//
        textView6=(TextView)findViewById(R.id.tv6);
        textView6.setText(System.getProperty("os.version")); // kernal version

        textView7=(TextView)findViewById(R.id.tv7);
        textView7.setText(getDeviceScreenResolution());

        textView8=(TextView)findViewById(R.id.tv8);
        textView8.setText(Build.SERIAL); // device serial


    }

    public String getSignalStrength(){

        String result;
        TelephonyManager telephonyManager =    (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfoGsm cellinfogsm = (CellInfoGsm)telephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
        result= String.valueOf(cellSignalStrengthGsm.getDbm());

        Log.e("Test",result);
        return result;
    }
    public static String getMacAddr(Context appContext) //This will give MAC Address
    {
        WifiManager manager=(WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info=manager.getConnectionInfo();
        String m_macAddr=info.getMacAddress();
        return m_macAddr;
    }

    public static String getIpAddr(Context appContext) {
        WifiManager manager=(WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=manager.getConnectionInfo();
        String result;
        int ip = wifiInfo.getIpAddress();

        result = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return result;

    }
    public static String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }
        return load;
    }
    public String getDeviceScreenResolution() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x; //device width
        int height = size.y; //device height

        return "" + width + " x " + height; //example "480 * 800"
    }


    public class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            textView9.setText("Signal Strength : " + signalStrengthValue + "dbm");
        }
    }


}
