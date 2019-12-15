package com.example.jszx.itimeapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListViewMainActivity extends AppCompatActivity {

    public static final int CONTENT_MENU_DELETE = 1;
    public static final int CONTENT_MENU_NEW = 2;
    public static final int CONTENT_MENU_DETAIL = 3;
    public static final int REQUEST_CODE_NEW_EVENT = 901;
    public static final int REQUEST_CODE_DETAIL_EVENT = 902;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    private FloatingActionButton buttonAdd;
  /*  int yearDiff=0;
    int montheDiffer=0;
    int dayDiff=0;
    int hourDiff=0;
    int minuteDiff=0;*/
    ListView listviewevents;
    EventAdapter eventAdapter;
    EventSaver eventsaver;
    // String []eventNames={"生日","四六级考试"};
    private List<Event> listEvents=new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventsaver.saver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd= (FloatingActionButton) this.findViewById(R.id.floating_action_button_add);
        eventsaver=new EventSaver(this);
        listEvents=eventsaver.load();
        if(listEvents.size()==0)
            init();
        listviewevents= (ListView) this.findViewById(R.id.list_view_events);
        eventAdapter =
                new  EventAdapter(ListViewMainActivity.this, R.layout.list_view_event, listEvents);

        listviewevents.setAdapter(eventAdapter);

        this.registerForContextMenu(listviewevents);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewMainActivity.this, NewEventActivity.class);
                intent.putExtra("title", "concert");
                intent.putExtra("date", "2019.11.30");
                //intent.putExtra("insert_position", ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_NEW_EVENT);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==listviewevents) {
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(listEvents.get(info.position).getTitle());
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, CONTENT_MENU_DELETE, 0, "删除");
           // menu.add(0, CONTENT_MENU_NEW, 0, "新增");
            menu.add(0, CONTENT_MENU_DETAIL, 0, "编辑");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW_EVENT:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("title");
                    int insertPosition = data.getIntExtra("insert_position", 0);
                    String date = data.getStringExtra("date");

                   // int i=Integer.parseInt(date.substring(8,10))-day;

                    int i=0;
                    int yearDif=Integer.parseInt(date.substring(0,4));
                    int monthDif=Integer.parseInt(date.substring(5,7));
                    int dayDif=Integer.parseInt(date.substring(8,10));
                    int hourDif=Integer.parseInt(date.substring(11,12));
                    int minDif=Integer.parseInt(date.substring(14,15));
                    if(yearDif>year){
                        i=(monthDif-1)*30+dayDif+(12-month)*30-day;
                    }
                    else if(yearDif==year){
                        i=(monthDif-month-1)*30+dayDif-day;
                    }
                    listEvents.add(insertPosition,new Event(title,i+"天",date));
                    eventAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_DETAIL_EVENT:
                if (resultCode == RESULT_OK) {
                    //String title = data.getStringExtra("title");
                    int insertPosition = data.getIntExtra("insert_position", 0);
                    Event eventAtPosition=listEvents.get(insertPosition);

                    //String date = data.getStringExtra("date");
                    // listEvents.add(insertPosition,new Event(title,R.drawable.concert,date));
                    eventAtPosition.setTitle(data.getStringExtra("title"));
                    eventAtPosition.setDate(data.getStringExtra("date"));
                    String s=data.getStringExtra("date");
                    int i=0;
                    int yearDif=Integer.parseInt(s.substring(0,4));
                    int monthDif=Integer.parseInt(s.substring(5,7));
                    int dayDif=Integer.parseInt(s.substring(8,10));
                    int hourDif=Integer.parseInt(s.substring(11,12));
                    int minDif=Integer.parseInt(s.substring(14,15));
                    if(yearDif>year){
                        i=(monthDif-1)*30+dayDif+(12-month)*30-day;
                    }
                    else if(yearDif==year){
                        i=(monthDif-month-1)*30+dayDif-day;
                    }
                    eventAtPosition.setCount(i+"天");
                    //eventAtPosition.setTitle(data.getStringExtra("title"));
                    eventAdapter.notifyDataSetChanged();
                }
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTENT_MENU_DELETE:
                final int deleteposition=((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listEvents.remove(deleteposition);
                                eventAdapter.notifyDataSetChanged();
                                Toast.makeText(ListViewMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();

                break;
           /* case CONTENT_MENU_NEW:
                Intent intent = new Intent(this, NewEventActivity.class);
                intent.putExtra("title", "concert");
                intent.putExtra("date", "2019.11.30");
                intent.putExtra("insert_position", ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_NEW_EVENT);

               break;
              */
            case CONTENT_MENU_DETAIL:
                int position=((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                Intent intent1 = new Intent(this, UpdateEventActivity.class);
                intent1.putExtra("title",listEvents.get(position).getTitle());
                intent1.putExtra("date", listEvents.get(position).getDate());
                intent1.putExtra("insert_position", position);
                startActivityForResult(intent1, REQUEST_CODE_DETAIL_EVENT);
                // Toast.makeText(ListViewMainActivity.this,"倒计时详情",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void init() {
        String time1="2020-01-31 00:00";
        int i=0;
        int yearDif=Integer.parseInt(time1.substring(0,4));
        int monthDif=Integer.parseInt(time1.substring(5,7));
        int dayDif=Integer.parseInt(time1.substring(8,10));
        int hourDif=Integer.parseInt(time1.substring(11,12));
        int minDif=Integer.parseInt(time1.substring(14,15));
        if(yearDif>year){
            i=(monthDif-1)*30+dayDif+(12-month)*30-day;
        }
        else if(yearDif==year){
            i=(monthDif-month-1)*30+dayDif-day;
        }
        String time2="2020-03-25 00:00";
        int j=0;
        int yearDif1=Integer.parseInt(time2.substring(0,4));
        int monthDif1=Integer.parseInt(time2.substring(5,7));
        int dayDif1=Integer.parseInt(time2.substring(8,10));
        int hourDif1=Integer.parseInt(time2.substring(11,12));
        int minDif1=Integer.parseInt(time2.substring(14,15));
        if(yearDif1>year){
            j=(monthDif1-1)*30+dayDif1+(12-month)*30-day;
        }
        else if(yearDif1==year){
            j=(monthDif1-month-1)*30+dayDif1-day;
        }
        listEvents.add(new Event("Birthday",i+"天",time1));
        listEvents.add(new Event("Exam",j+"天",time2));
    }

    class EventAdapter extends ArrayAdapter<Event> {

        private int resourceId;

        public EventAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            //((ImageView) view.findViewById(R.id.image_view_enent)).setImageResource(event.getCoverResourceId());
            ((TextView) view.findViewById(R.id.text_view_event_title)).setText(event.getTitle());
            ((TextView) view.findViewById(R.id.text_view_event_date)).setText(event.getDate());
            ((TextView) view.findViewById(R.id.text_view_event_count)).setText(event.getCount());
            if(event.getTitle()=="Birthday")
                ((TextView) view.findViewById(R.id.text_view_event_count)).setBackgroundResource(R.drawable.birthday);
            else if(event.getTitle()=="Exam")
                ((TextView) view.findViewById(R.id.text_view_event_count)).setBackgroundResource(R.drawable.exam);
            else if(event.getTitle()=="concert")
                ((TextView) view.findViewById(R.id.text_view_event_count)).setBackgroundResource(R.drawable.concert);
            return view;
        }
    }
}
