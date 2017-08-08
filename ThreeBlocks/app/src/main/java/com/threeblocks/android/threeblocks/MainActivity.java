package com.threeblocks.android.threeblocks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{



    static final String T_MAP_API ="f559eb18-a660-3ae4-8068-ee47c87c0984";

    static final String Google_API_KEY = "AIzaSyCjRpNuxMPk3KFAt9Vv8REt4MTaN_lOR0o";
    static final String Google_CX_KEY = "003008065443619207290:dsnyfqx4xdi";

    static final String Client_ID = "HA6q2eBbMBukCxpgJzhm";
    static final String Client_Secret = "Yoecs3HtyN";

    private Context mContext = null;

    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;
    private static int mMarkerID;
    private boolean m_bTrackingMode = true;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    //구글 커스텀 서치 URL 형식 - 커스텀 서치 스레드 올리겠음
    //String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //선언
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.map_view);
        tMapView = new TMapView(this);
        constraintLayout.addView(tMapView);

        //키값
        tMapView.setSKPMapApiKey(T_MAP_API);

        /*현재 보는 방향*/
        tMapView.setCompassMode(true);

        /* 현 위치 아이콘 표시 */
        tMapView.setIconVisibility(true);

        /* 줌레벨 */
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        /*GPS 설정*/
        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        //연결된 인터넷으로 현 위치를 받음 실내일때 유용
        tmapgps.OpenGps();

        /* 화면 중심을 단말의 현재 위치로 이동*/
        tMapView.setTrackingMode(true); // 화면 중심을 단말로이동
        tMapView.setSightVisible(true); // 시야 표출 여부를 설정

        /*풍선 우클릭 리스너*/
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void addPoint() {
        // 예시 포인트 //
        m_mapPoint.add(new MapPoint("강남",37.510350,127.066847));
    }

    public void showMarkerPoint(){
        //마커는 빨간색
        for(int i = 0;i <m_mapPoint.size();i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLogitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.poi_dot);
            //poi_dot 지도에 꼽는 빨간 마커

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);
        }
    }

    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(),location.getLatitude());
        }
    }
}
