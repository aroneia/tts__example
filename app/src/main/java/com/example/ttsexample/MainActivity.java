package com.example.ttsexample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.speech.tts.TextToSpeech.ERROR;


public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;              // TTS 변수 선언
    private Button button01;
    private String filelocationpath = "C:/Users/koko111/Desktop/pol_all_news1.csv";
    private static List<String[]> locationList=new ArrayList<String[]>();
    private List<String> plz = new ArrayList<>();
    StringBuilder sb;


    public MainActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button01 = (Button) findViewById(R.id.button01);


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStream is = getBaseContext().getResources().getAssets().open("pol_all_news.xls");
                    Workbook wb = Workbook.getWorkbook(is);

                    if(wb != null) {
                        Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                        if(sheet != null) {
                            int colTotal = sheet.getColumns();    // 전체 컬럼
                            int rowIndexStart = 1;                  // row 인덱스 시작
                            int rowTotal = sheet.getColumn(colTotal-1).length;

                            List<String> plz = new ArrayList<>();
                            for(int row=rowIndexStart;row<rowTotal;row++) {
                                sb = new StringBuilder();
                                int col=1;
                                String contents = sheet.getCell(col, row).getContents();
                                sb.append(contents);
                                plz.add(contents);
                            }
                            for(int i=0;i<plz.size();i++){
                                tts.speak(plz.get(i),TextToSpeech.QUEUE_ADD, null);
                                tts.playSilence(1000, TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BiffException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
