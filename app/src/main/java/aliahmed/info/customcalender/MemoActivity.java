package aliahmed.info.customcalender;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, Data.data);

        // 뷰에 어댑터를 설정.
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

}
