package com.threeblocks.android.threeblocks;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;
import com.threeblocks.android.threeblocks.SharedPreference.QueryPreference;
import com.threeblocks.android.threeblocks.SharedPreference.SearchAdapter;
import com.threeblocks.android.threeblocks.SharedPreference.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    static final String TAG = "MainActivity";

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

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
    private TMapData tmapdata;

    private ImageButton btn_research;

    TextView myAddress;

    private TextView searchText;

    private ArrayList<String> mSearchlist;


    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    //구글 커스텀 서치 URL 형식 - 커스텀 서치 스레드 올리겠음
    //String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMyLocation();

        //각종 객체 선언
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.map_view);
        tMapView = new TMapView(this);
        linearLayout.addView(tMapView);
        tmapdata = new TMapData();

        myAddress = (TextView)findViewById(R.id.myAddress);

        //버튼 선언 및 리스너 등록
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;

        btn_research = (ImageButton) findViewById(R.id.btn_research);
        btn_research.setOnClickListener(onClickListener);

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

        tmapgps = new TMapGpsManager(MainActivity.this); // 단말 위치탐색 클래스
        tmapgps.setMinTime(1000); // 위치변경 인식 최소시간설정
        tmapgps.setMinDistance(5); // 위치 변경 인식 최소거리 설정
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); // 네트워크 기반의 위치탐색


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

        searchText = (TextView) findViewById(R.id.search);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadtoolbarSearch();
            }
        });
    }

    private void loadtoolbarSearch() {

        ArrayList<String> searchStored = QueryPreference.getStringArrayPref(MainActivity.this,
                Utils.PREFS_NAME,Utils.KEY_COUNTRIES);
        mSearchlist = searchStored;


        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);


        Utils.setListViewHeightBasedOnChildren(listSearch);

        edtToolSearch.setHint("Search Please");

        final Dialog toolbarSearchDialog = new Dialog(MainActivity.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(false);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        searchStored = (searchStored != null && searchStored.size() > 0) ? searchStored : new ArrayList<String>();
        final SearchAdapter searchAdapter = new SearchAdapter(MainActivity.this, searchStored, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);

        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String country = String.valueOf(adapterView.getItemAtPosition(position));
                //QueryPreference.addList(MainActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES, country);

                edtToolSearch.setText(country);
                listSearch.setVisibility(View.GONE);


            }
        });


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

        edtToolSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(MainActivity.this,"Onclicked",Toast.LENGTH_LONG).show();

            }
        });


        edtToolSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(textView.getId()==R.id.edt_tool_search && actionId== EditorInfo.IME_ACTION_DONE){ // 뷰의 id를 식별, 키보드의 완료 키 입력 검출


                    String query = String.valueOf(textView.getText());
                    QueryPreference.addList(MainActivity.this,Utils.PREFS_NAME, Utils.KEY_COUNTRIES,query);
                    //QueryPreference.setStringArrayPref(MainActivity.this, Utils.PREFS_NAME,Utils.KEY_COUNTRIES,mSearchlist);


                }
                return false;
            }
        });


    }

    private void showAddress() {

        MapPoint mp = new MapPoint();

        mp = UserLocation.getInstance(mContext).getMapPoint();
        double lat = mp.getLatitude();
        double lon = mp.getLogitude();


        tmapdata.convertGpsToAddress(lat,lon, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String strAddress) {
                TextView myAddress = (TextView)findViewById(R.id.myAddress);
                myAddress.setText(strAddress);
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
            TMapPoint tpoint = tMapView.getLocationPoint();

            double Latitude = tpoint.getLatitude();
            double Longitude = tpoint.getLongitude();

            MapPoint myPoint = new MapPoint();
            myPoint.setLatitude(Latitude);
            myPoint.setLogitude(Longitude);

            UserLocation.getInstance(mContext).setsUserLocation(myPoint);
            showAddress();
        }
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //------------------------------------------------------------------------------
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();
                    getMyLocation();

                } else {
                    Toast.makeText(MainActivity.this,
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            Message msg;
            switch (view.getId())
            {
                case R.id.btn_research:
                  tmapgps.OpenGps();
                    break;
            }
        }
    }
}
