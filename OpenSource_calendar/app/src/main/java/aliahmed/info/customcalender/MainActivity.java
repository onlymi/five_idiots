package aliahmed.info.customcalender;

import static aliahmed.info.customcalender.Data.eventObjects;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import android.content.Intent; // 화면전환 용도

public class MainActivity extends AppCompatActivity {
    LinearLayout layoutCalender;
    View custom_view;
    Date lastDate;

    // 메모추가 버튼
    Button btn;

    // MemoActivity 에서 MainActivity 의 메소드 사용을 위함
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        mainContext = this;
        setInitializations();
        setCalenderView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        mainContext = this;
        setInitializations();
        setCalenderView();
    }

    public void loadData(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"/memo_data.txt"));
            String readStr;
            String str;
            while(((str = br.readLine()) != null)){
                readStr = "";
                readStr += str;
                StringTokenizer t = new StringTokenizer(readStr, ",");
                int year = Integer.parseInt(t.nextToken());
                int month = Integer.parseInt(t.nextToken());
                int day = Integer.parseInt(t.nextToken());
                String memo = t.nextToken();
                addMemo(Data.data, year, month, day, memo);
            }
            br.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "memo_data File not Found", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setInitializations() {
        custom_view = findViewById(R.id.custom_view);
        layoutCalender = findViewById(R.id.layoutCalender);
    }

    public void setCalenderView() {

        //Custom Events
        eventObjects.setColor(R.color.colorPrimary);
        Data.mEvents.add(eventObjects);

        ViewGroup parent = (ViewGroup) custom_view.getParent();
        parent.removeView(custom_view);
        layoutCalender.removeAllViews();
        layoutCalender.setOrientation(LinearLayout.VERTICAL);

        final CalendarCustomView calendarCustomView = new CalendarCustomView(this, Data.mEvents);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        calendarCustomView.setLayoutParams(layoutParams);
        layoutCalender.addView(calendarCustomView);

        calendarCustomView.calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getAdapter().getView((int) l, null, null).getAlpha() == 0.4f) {
                    Log.d("", "hello");
                } else {
                    Calendar today = Calendar.getInstance();
                    today.setTime(new java.util.Date());

                    Calendar tapedDay = Calendar.getInstance();
                    tapedDay.setTime((Date) adapterView.getAdapter().getItem((int) l));
                            lastDate = (Date) adapterView.getAdapter().getItem((int) l);
                }
                try {
                    // 날짜 클릭 시 Memo 화면으로 화면전환
                    Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // 메모추가 버튼 누를 시 날짜 선택기
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, DatePickerActivity.class);
                startActivityForResult(intent, 1);
            }
        };
        btn = (Button)findViewById(R.id.button_dialog);
        btn.setOnClickListener(listener);

    }

    public void check(int year, int month, int day){
        EventObjects eventObjects = new EventObjects(Data.event_count, "Memo", new Date(year, month-1, day));
        eventObjects.setColor(R.color.colorPrimary);
        Data.mEvents.add(eventObjects);
        Data.event_count++;
    }

    public void addMemo(ArrayList<String> data, int year, int month, int day, String memo){
        data.add(month +"월 "+ day +"일: " + memo);
        check(year, month+1, day);
    }

}
