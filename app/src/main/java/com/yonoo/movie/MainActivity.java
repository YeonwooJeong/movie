package com.yonoo.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    // 웹사이트 주소를 저장할 변수
    String text = "";
    Document event = null;
    int start, end, list_cnt, i;
    ListView listview;
    ListViewAdapter adapter;
    public static MainActivity main = new MainActivity();
    //    String[] getDescription = null;
//    String[] getLink = null;
//    String[] getImageUrl = null;
    ArrayList<String> getDescription = new ArrayList<String>();
    ArrayList<String> getLink = new ArrayList<String>();
    ArrayList<String> getImageUrl = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView1);
        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadHtml();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Elements title = main.event.select("#evtList article a strong.tit");

                System.out.println("사이즈" + title.size());
                for (Element e : title) {
                    getDescription.add(e.text());

                }
                for (i = 0; i < title.size(); i++)
                    System.out.println(getDescription.get(i));

                Elements href = main.event.select("#evtList article a");
                for (Element e : href) {
                    getLink.add(e.attr("href"));

                }
                for (i = 0; i < title.size(); i++)
                    System.out.println(getLink.get(i));

                // Adapter 생성
                adapter = new ListViewAdapter();
                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.listview1);
                adapter.addItem(getDescription, getLink);
                listview.setAdapter(adapter);
                // 첫 번째 아이템 추가.

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        // get item
                        ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                        int start =  item.getLink().indexOf("('");
                        int end =  item.getLink().indexOf("',");
                        System.out.println("item.getLink()"+item.getLink());
                        System.out.println("start " +start+ "end "+end);
                        System.out.println("subString===:"+item.getLink().substring(start+2,end));
                        String link = item.getLink().substring(start+2,end);
                        if(link.startsWith("./")){
                            link = link.replaceFirst("\\.","");
                            System.out.println("마지막 주소" +link);
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cgv.co.kr/WebApp/EventNotiV4" + link));
                            startActivity(i);
                        }else{
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cgv.co.kr" + link));
                            startActivity(i);
                        }
                    }
                });
            }
        });


    } // end of onCreate

    Document loadHtml() throws InterruptedException { // 웹에서 html 읽어오기==
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Document doc = Jsoup.connect("http://www.cgv.co.kr/culture-event/event/?menu=2#1").get(); //웹에서 내용을 가져온다.
//                    Element script = doc.select("body").first();
//                    text = script.html(); //원하는 부분은 Elements형태로 되어 있으므로 이를 String 형태로 바꾸어 준다.
//                    start = text.indexOf("var jsonData");
//                    end = text.indexOf("$(\".evt");
//
//                    main.event = text.substring(start + 15, end - 16);
                    Document doc = Jsoup.connect("http://m.cgv.co.kr/WebApp/EventNotiV4/EventList.aspx?mCode=004&logoIndex=0").get(); //웹에서 내용을 가져온다.
//                    Elements script = doc.select("#evtList");
//                    text = script.html(); //원하는 부분은 Elements형태로 되어 있으므로 이를 String 형태로 바꾸어 준다.
//                    start = text.indexOf("var jsonData");
//                    end = text.indexOf("$(\".evt");

//                    for(Element e: script){
//                        System.out.println("script : "+e);
//                    }

                    main.event = doc;


                } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작
        t.join(); //쓰레드 완료까지 대기
        return main.event;
    }

}
