package aliahmed.info.customcalender;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Data {
    // 데이터 저장
    public static ArrayList<String> data = new ArrayList<>();

    public static List<EventObjects> mEvents = new ArrayList<>();

    public static EventObjects eventObjects = new EventObjects(0, "Today", new Date());

    public static int event_count = 1;
}
