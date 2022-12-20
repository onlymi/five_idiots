package aliahmed.info.customcalender;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MemoActivity extends AppCompatActivity{
    ListView listView;
    public static Context memoContext;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);

        /* 액션바 설정 */
        Toolbar appActionBar = findViewById(R.id.appActionBar);
        setSupportActionBar(appActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memoContext = this;

        ArrayList<String> date_data = new ArrayList<>();

        // 메모 로드
        int i = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"/memo_data.txt"));
            String readStr;
            String str;

            while(((str = br.readLine()) != null)){
                readStr = "";
                readStr += str;
                StringTokenizer t = new StringTokenizer(readStr, ",");
                int year = Integer.parseInt(t.nextToken());
                int month = Integer.parseInt(t.nextToken())+1;
                int day = Integer.parseInt(t.nextToken());
                String memo;

                try{
                    memo = t.nextToken();
                }
                catch (Exception e){
                    memo = " ";
                }

                if(((MainActivity)MainActivity.mainContext).selected_month == month && ((MainActivity)MainActivity.mainContext).selected_day == day){
                    date_data.add(month +"월 "+ day +"일: " + memo);
                }

                i++;
            }
            br.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "memo_data File not Found", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //메모 로드 끝

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, date_data);



        // 원본
        //        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Data.data);

        // 뷰에 어댑터를 설정.
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

}
