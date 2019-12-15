package com.example.jszx.itimeapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class UpdateEventActivity extends AppCompatActivity {

    private Button buttonOk1,buttonCancel1;
    private EditText editTextTitle1,editTextDescription1;
    private int insertPosition;

    private TextView TimeText,recordTimeText;
    private ImageView imageViewClock;

    private TextView textViewShow;

    private ConstraintLayout countDown;
    // 倒计时
    private TextView daysTv, hoursTv, minutesTv, secondsTv;
    private long mDay = 10;
    private long mHour = 10;
    private long mMin = 30;
    private long mSecond = 00;// 天 ,小时,分钟,秒

    private boolean isRun = true;
   private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();
                daysTv.setText(mDay+"");
                hoursTv.setText(mHour+"");
                minutesTv.setText(mMin+"");
                secondsTv.setText(mSecond+"");
                if (mDay==0&&mHour==0&&mMin==0&&mSecond==0) {
                    countDown.setVisibility(View.GONE);
                }
            }
        }
    };



    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }
            }
        }
    }

    //获取Calendar对象，用于获取当前时间
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second=calendar.get(Calendar.SECOND);

    int yearDif=0;
    int monthDif=0;
    int dayDif=0;
    int hourDif=0;
    int minDif=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        countDown = (ConstraintLayout) findViewById(R.id.countdown_layout);
        daysTv = (TextView) findViewById(R.id.days_tv);
        hoursTv = (TextView) findViewById(R.id.hours_tv);
        minutesTv = (TextView) findViewById(R.id.minutes_tv);
        secondsTv = (TextView) findViewById(R.id.seconds_tv);

        String s=getIntent().getStringExtra("date");
        yearDif=Integer.parseInt(s.substring(0,4));
        monthDif=Integer.parseInt(s.substring(5,7));
        dayDif=Integer.parseInt(s.substring(8,10));
        hourDif=Integer.parseInt(s.substring(11,12));
        minDif=Integer.parseInt(s.substring(14,15));
        if(yearDif>year){
            mDay=(monthDif-1)*30+dayDif+(12-month)*30-day;
            mHour=(hourDif+24-hour)%24;
            mMin=(minDif+60-minute)%60;
            mSecond=(60-second);
        }
        else if(yearDif==year){
            mDay=(monthDif-month-1)*30+dayDif-day;
            mHour=(hourDif+24-hour)%24;
            mMin=(minDif+60-minute)%60;
            mSecond=(60-second);
        }

        startRun();

        imageViewClock= (ImageView) this.findViewById(R.id.image_view_clock);
        TimeText= (TextView) this.findViewById(R.id.text_view_choose_date);
       // recordTimeText= (TextView) this.findViewById(R.id.text_view_record_date);

        buttonOk1= (Button) this.findViewById(R.id.button_ok1);
        buttonCancel1=(Button)this.findViewById(R.id.button_cancel1);
        editTextTitle1=(EditText)this.findViewById(R.id.edit_text_title1);
        editTextDescription1=(EditText)this.findViewById(R.id.edit_text_description1);

        editTextTitle1.setText(getIntent().getStringExtra("title"));
        editTextDescription1.setText(getIntent().getStringExtra("date"));
        insertPosition=getIntent().getIntExtra("insert_position",0);

        buttonOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", editTextTitle1.getText().toString());
                intent.putExtra("insert_position", insertPosition);
                intent.putExtra("date", editTextDescription1.getText().toString());

                //获取该日期

                setResult(RESULT_OK, intent);
                UpdateEventActivity.this.finish();
            }
        });
        buttonCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateEventActivity.this.finish();
            }
        });


      /*  recordTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time,time1;
                time1=year+" "+month+"."+day;
                time= (String) TimeText.getText();

                recordTimeText.setText(time);

            }
        });*/

    }
}
