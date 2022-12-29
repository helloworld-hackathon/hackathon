package com.example.helloworld

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    // 구글 맵 객체를 불러옴
    private lateinit var mMap: GoogleMap

    // 현재 위치
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val PERM_FLAG = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        startProcess()  // 구글맵 호출
        // 권한 체크
        if (isPermitted()) {
            startProcess()  // 구글맵 호출
        }
        // 권한 요청
        else {
            ActivityCompat.requestPermissions(this, permissions, PERM_FLAG)
        }
    }

    // 권한요청
    fun isPermitted(): Boolean {
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // 구글맵 호출
    fun startProcess() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    // 권한요청 처리 결과 수신
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERM_FLAG -> {
                var check = true

                // 요청한 권한 허용/거부 상태 한번에 체크
                for (grant in grantResults) {
                    if (grant != PERMISSION_GRANTED) {
                        check = false
                        break
                    }
                }
                // 요청한 권한 허용함
                if (check) {
                    startProcess()
                }
                // 권한 허용 안함
                else {
                    Toast.makeText(this, "권한을 승인해야 앱을 사용할 수 있습니다", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        maps_view.visibility = View.INVISIBLE; // 카드뷰 안보이게


        // 현재 위치
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setUpdateLocationListener()


        // 마커 클릭시 카드뷰로 정보 알려줌
        googleMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {

                var name = findViewById<TextView>(R.id.name)
                var field = findViewById<TextView>(R.id.field)
                var location = findViewById<TextView>(R.id.location)

                maps_view.visibility = View.VISIBLE;       // 카드뷰 보이게
                name.text = marker.title                   // 식당 이름
                field.text = marker.snippet                // 음식 종류
                location.text = marker.position.toString() // 식당 위치

                return false
            }
        })

        // 맵 클릭시 카드뷰 안보임
        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                maps_view.visibility = View.GONE
            }
        })
    }

    // 마커 아이콘 설정
    fun getDescriptorFromDrawable(drawableId: Int): BitmapDescriptor {
        var bitmapDrawable: BitmapDrawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bitmapDrawable = getDrawable(drawableId) as BitmapDrawable
        } else {
            bitmapDrawable = resources.getDrawable(drawableId) as BitmapDrawable
        }
        // 마커 크기 변환
        val scaledBitmap = Bitmap.createScaledBitmap(bitmapDrawable.bitmap, 100, 100, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    // 내 위치를 가져오는 코드
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListener() {
        val locationRequest = LocationRequest.create() // 위치정보 요청
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확도 높게
            interval = 1000 * 30 // 30초에 한번씩 요청
        }

        // 실시간 위치 정보 받음
        locationCallback = object : LocationCallback() {
            // 위치정보 변경 시 호출됨
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for ((i, location) in it.locations.withIndex()) {
                        Log.d("로케이션", "$i ${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }

        // 로케이션 요청 함수 호출 (locationRequest, locationCallback)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

    }

    // 위치 표시
    fun setLastLocation(location: Location) {

        // 사용자의 현재 위치
        val myLocation = LatLng(location.latitude, location.longitude)


        // 마커 아이콘 만들기
        val descriptor = getDescriptorFromDrawable(R.drawable.marker2) // 현재 위치 (RED)
        val descriptor2 = getDescriptorFromDrawable(R.drawable.marker3) // 식당 위치 (BROWN)

        // 현재 위치 마커
        val marker = MarkerOptions()
            .position(myLocation)
            .title("현재 위치")
            .icon(descriptor)
        mMap.clear() // 이전 마커 삭제
        mMap.addMarker(marker)  // 현재 위치


        //식당 위치 마커
        var marker1 = LatLng(37.636810098806, 127.066396184056)
        mMap.addMarker(MarkerOptions().position(marker1!!).title("502 세컨즈카페").snippet("양식").icon(descriptor2))

        marker1 = LatLng(37.6340522250578, 127.051491429932)
        mMap.addMarker(MarkerOptions().position(marker1).title("5길반찬").snippet("한식").icon(descriptor2))

        marker1 = LatLng(37.6467024983811, 127.083737077543)
        mMap.addMarker(MarkerOptions().position(marker1).title("고향보리밥쌈밥").snippet("한식").icon(descriptor2))

        marker1 = LatLng(37.624597796799, 127.07649582856)
        mMap.addMarker(MarkerOptions().position(marker1).title("굿피자파스타").snippet("양식").icon(descriptor2))

        marker1 = LatLng(37.6511256659606, 127.062638552294)
        mMap.addMarker(MarkerOptions().position(marker1).title("귀빈반점").snippet("중식").icon(descriptor2))

        marker1 = LatLng(37.6267450093012, 127.087912442601)
        mMap.addMarker(MarkerOptions().position(marker1).title("고향보리밥쌈밥").snippet("한식").icon(descriptor2))

        marker1 = LatLng(37.6558595520912, 127.078489309937)
        mMap.addMarker(MarkerOptions().position(marker1).title("까르보네 (중계은행사거리점)").snippet("양식").icon(descriptor2))

        marker1 = LatLng(37.6227650416546, 127.060063066786)
        mMap.addMarker(MarkerOptions().position(marker1).title("꽃제비칼국수").snippet("한식").icon(descriptor2))

        marker1 = LatLng(37.6577670301141, 127.062338304855)
        mMap.addMarker(MarkerOptions().position(marker1).title("나빈").snippet("인도/중동").icon(descriptor2))

        marker1 = LatLng(37.6524383700379, 127.078501126526)
        mMap.addMarker(MarkerOptions().position(marker1).title("닐리 (중계점)").snippet("양식").icon(descriptor2))


        // 카메라의 위치
        val cameraPosition = CameraPosition.Builder()
            .target(myLocation)
            .zoom(14.0f) // zoom in
            .build()

        // 카메라 이동
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}