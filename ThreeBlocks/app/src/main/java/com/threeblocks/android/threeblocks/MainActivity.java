package com.threeblocks.android.threeblocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    static final String T_MAP_API ="f559eb18-a660-3ae4-8068-ee47c87c0984";

    static final String Google_API_KEY = "AIzaSyCjRpNuxMPk3KFAt9Vv8REt4MTaN_lOR0o";
    static final String Google_CX_KEY = "003008065443619207290:dsnyfqx4xdi";

    static final String Client_ID = "HA6q2eBbMBukCxpgJzhm";
    static final String Client_Secret = "Yoecs3HtyN";

    //구글 커스텀 서치 URL 형식 - 커스텀 서치 스레드 올리겠음
    //String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
