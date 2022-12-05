package aliahmed.info.customcalender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerActivity extends AppCompatActivity {
    // MemoActivity 에서 MainActivity 의 메소드 사용을 위함
    public static Context DatePickerContext;

    Button btn;
    public int mYear =0, mMonth=0, mDay=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date_picker);
        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);
    }

    public void mOnClick(View v){
        Intent intent = new Intent(DatePickerActivity.this, PopupActivity.class);
        intent.putExtra("mYear",mYear);
        intent.putExtra("mMonth", mMonth);
        intent.putExtra("mDay", mDay);
        setResult(RESULT_OK, intent);
        startActivityForResult(intent, 1);
        finish();

    }

    DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener(){
        @Override
        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm;
            mDay = dd;
        }
    };

}