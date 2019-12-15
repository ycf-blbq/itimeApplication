package com.example.jszx.itimeapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {
    private Button buttonOk,buttonCancel;
    private EditText editTextTitle,editTextDescription;
    private int insertPosition;
    private TextView timeText;
    private ImageView imageViewClock,imageViewPeriod;
    private TextView periodText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        timeText= (TextView) this.findViewById(R.id.text_view_choose_date);
        imageViewClock= (ImageView) this.findViewById(R.id.image_view_clock);
        imageViewPeriod= (ImageView) this.findViewById(R.id.image_view_period);
        periodText= (TextView) this.findViewById(R.id.text_view_choose_period);

        buttonOk= (Button) this.findViewById(R.id.button_ok);
        buttonCancel=(Button)this.findViewById(R.id.button_cancel);
        editTextTitle=(EditText)this.findViewById(R.id.edit_text_title);
        editTextDescription=(EditText)this.findViewById(R.id.edit_text_description);

        editTextTitle.setText(getIntent().getStringExtra("title"));
        editTextDescription.setText(getIntent().getStringExtra("date"));
        //insertPosition=getIntent().getIntExtra("insert_position",0);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", editTextTitle.getText().toString());
               // intent.putExtra("insert_position", insertPosition);
                intent.putExtra("date", timeText.getText().toString());
                setResult(RESULT_OK, intent);
                NewEventActivity.this.finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewEventActivity.this.finish();
            }
        });

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将timeText传入用于显示所选择的时间
                showDialogPick((TextView) v);
            }

            private void showDialogPick(final TextView timeText) {
                final StringBuffer time = new StringBuffer();
                //获取Calendar对象，用于获取当前时间
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                //实例化TimePickerDialog对象
                final TimePickerDialog timePickerDialog = new TimePickerDialog(NewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    //选择完时间后会调用该回调函数
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourString=String.format("%0"+2+"d", hourOfDay);
                        String minString=String.format("%0"+2+"d", minute);
                        time.append(" "  + hourString + ":" + minString);
                        //设置TextView显示最终选择的时间
                        timeText.setText(time);
                    }
                }, hour, minute, true);
                //实例化DatePickerDialog对象
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    //选择完日期后会调用该回调函数
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //因为monthOfYear会比实际月份少一月所以这边要加1
                       // time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                       String monString=String.format("%0"+2+"d", monthOfYear+1);
                        String dayString=String.format("%0"+2+"d", dayOfMonth);
                        time.append(year + "-" + monString + "-" + dayString);
                        //选择完日期后弹出选择时间对话框
                        timePickerDialog.show();
                    }
                }, year, month, day);
                //弹出选择日期对话框
                datePickerDialog.show();
            }
        });

        periodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将timeText传入用于显示所选择的时间
                showDialogChoose((TextView) v);
            }

            private void showDialogChoose(final TextView timeText) {
                final String []subj={"Week","Month","Year","Custom"};
                AlertDialog.Builder builder=new AlertDialog.Builder(NewEventActivity.this);
                builder.setTitle("Period");
                builder.setItems(subj, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NewEventActivity.this, "您选择了"+subj[which], Toast.LENGTH_SHORT).show();
                        periodText.setText(subj[which]);
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }


        });


    }
}
