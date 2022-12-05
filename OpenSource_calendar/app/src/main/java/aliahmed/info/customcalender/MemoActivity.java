package aliahmed.info.customcalender;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity{
    ListView listView;
    public static Context memoContext;
    public ArrayList<String> data = Data.data;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        memoContext = this;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, data);

        // 뷰에 어댑터를 설정.
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    public void addMemo(ArrayList<String> data, int year, int month, int day, String memo){
        data.add(month +"월 "+ day +"일: " + memo);
        ((MainActivity)MainActivity.mainContext).check(year, month+1, day);
    }

}
