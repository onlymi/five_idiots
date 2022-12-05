package aliahmed.info.customcalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    Button okBtn, cancleBtn;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 제거 ( 전체화면 모드 )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);

        Intent intent = getIntent();
        this.mYear = intent.getIntExtra("mYear", -1);
        this.mMonth = intent.getIntExtra("mMonth", -1);
        this.mDay = intent.getIntExtra("mDay", -1);

    }

    //동작 버튼 클릭
    public void mOk(View v){
        ArrayList<String> data = ((MemoActivity)MemoActivity.memoContext).data;
//        int month = ((DatePickerActivity)DatePickerActivity.DatePickerContext).mMonth;
//        int day = ((DatePickerActivity)DatePickerActivity.DatePickerContext).mDay;

        EditText memo = findViewById(R.id.memo);
        String memo_convert = memo.getText().toString();
        ((MemoActivity)MemoActivity.memoContext).addMemo(data, mYear, mMonth, mDay, memo_convert);
        finish();
    }


    //취소 버튼 클릭
    public void mCancle(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}