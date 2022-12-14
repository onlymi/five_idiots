package aliahmed.info.customcalender;

import static aliahmed.info.customcalender.Data.eventObjects;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    // Channel에 대한 id 생성
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;

    // Notification에 대한 ID 생성
    private int NOTIFICATION_ID = 0;
    private int count_notification = 0;
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
                count_notification = 0;
                readStr = "";
                readStr += str;
                StringTokenizer t = new StringTokenizer(readStr, ",");
                int year = Integer.parseInt(t.nextToken());
                int month = Integer.parseInt(t.nextToken());
                int day = Integer.parseInt(t.nextToken());

                String memo;

                try{
                    memo = t.nextToken();
                }
                catch (Exception e){
                    memo = " ";
                }

                addMemo(Data.data, year, month, day, memo);
                int current_year;
                int current_month;
                int current_day;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    current_year = LocalDate.now().getYear();
                    current_month = LocalDate.now().getMonthValue();
                    current_day = LocalDate.now().getDayOfMonth();
                    System.out.println("test:"+current_year+","+current_month+","+current_day);
                }
                else{
                    current_year = 0;
                    current_month = 0;
                    current_day = 0;
                }

                if(current_year == year && current_month == month+1 && current_day == day){
                    NOTIFICATION_ID += 1;
                    count_notification += 1;
                    createNotificationChannel();
                    CharSequence cs = memo;
                    sendNotification(cs);

                }

            }
            //updateIconBadgeCount(mainContext, int count);
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

    //채널을 만드는 메소드
    public void createNotificationChannel()
    {
        //notification manager 생성
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if(android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O){
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID
                    ,"Test Notification",mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }

    // Notification Builder를 만드는 메소드
    private NotificationCompat.Builder getNotificationBuilder(CharSequence memo) {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Today's Memo:")
                .setContentText(memo)
                .setNumber(count_notification)
                .setSmallIcon(R.drawable.white_next_icon);
        return notifyBuilder;
    }

    // Notification을 보내는 메소드
    public void sendNotification(CharSequence memo){
        // Builder 생성
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(memo);
        // Manager를 통해 notification 디바이스로 전달
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());

    }

    public void updateIconBadgeCount(Context context, int count) {

        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        // Component를 정의
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", getLauncherClassName(context));

        // 카운트를 넣어준다.
        intent.putExtra("badge_count", count);

        // Version이 3.1이상일 경우에는 Flags를 설정하여 준다.
        // send
        context.getApplicationContext().sendBroadcast(intent);
    }

    private String getLauncherClassName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(context.getApplicationContext().getPackageName());

        List<ResolveInfo> resolveInfoList = context.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
        if(resolveInfoList != null && resolveInfoList.size() > 0) {
            return resolveInfoList.get(0).activityInfo.name;
        }
        return "";
    }

}
