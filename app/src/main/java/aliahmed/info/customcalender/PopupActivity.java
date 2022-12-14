package aliahmed.info.customcalender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        ArrayList<String> data = Data.data;
        EditText memo = findViewById(R.id.memo);
        String memo_convert = memo.getText().toString();
        ((MainActivity)MainActivity.mainContext).addMemo(data, mYear, mMonth, mDay, memo_convert);
        ((MainActivity)MainActivity.mainContext).onResume();
        try{
            saveData(memo_convert);
        }
        catch (IOException e){
            System.out.println("File IOException");
        }
        finish();

    }

    public void saveData(String memo) throws IOException{
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "/memo_data.txt", true));
            bw.write(mYear + "," + mMonth + "," + mDay + "," + memo + "\n");
            bw.close();
            Toast.makeText(this,"저장완료", Toast.LENGTH_SHORT).show();
            System.out.println("파일경로: " + getFilesDir());
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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