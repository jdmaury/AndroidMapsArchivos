package com.example.user.androidmapnoparse;



import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.IOException;


import java.util.List;
import java.util.Locale;


public class MainActivity  extends FragmentActivity implements LocationListener{


    //TextView tv4,tv5;


    private LocationManager locationManager;
    private String provider;
    private GoogleMap map;
    boolean gpsenabled = false;
    private Polyline ruta;
    private PolylineOptions coordenadas;
    Button boton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //  tv4 = (TextView)findViewById(R.id.textView4);
//        tv5 = (TextView)findViewById(R.id.textView5);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.969544, -74.803157), 18));
        Marker prueba = map.addMarker(new MarkerOptions()
                .position(new LatLng(10.969544, -74.803157)));

        coordenadas = new PolylineOptions();
        String contenido="hola";


        boton = (Button)findViewById(R.id.buttonn);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean enabled= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled)
        {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,false);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null)
        {
            onLocationChanged(location);
        }

  //      tv4.setText("Obteniendo latitud...");
//        tv5.setText("Obteniendo longitud...");



    }



    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider,200,0,this);



    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the
        // action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    public void onLocationChanged(Location location) {

        if(gpsenabled==true) {
            float lat = (float) location.getLatitude();
            float lng = (float) (location.getLongitude());
    /*        tv4 =  (TextView)findViewById(R.id.textView4);
            tv5 =  (TextView)findViewById(R.id.textView5);
            tv4.setText("Latitud : "+String.valueOf(lat));
            tv4.setTextSize(15);

            tv5.setText("Longitud : "+String.valueOf(lng));
            tv5.setTextSize(15);*/

            //Marker prueba = map.addMarker(new MarkerOptions()
              //      .position(new LatLng(lat, lng)));
            coordenadas.add(new LatLng(lat,lng)).width(8).color(Color.BLUE);


            Toast.makeText(this, "La posición ha cambiado", Toast.LENGTH_SHORT).show();

            String cityName = "";
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> direccion;
            try {
                direccion = gcd.getFromLocation(lat, lng, 1);
                if (direccion.size() > 0) {
                    cityName = "Usted está en la ciudad de " + direccion.get(0).getAddressLine(1);
                    //                tv5.setText(tv5.getText()+"\n"+cityName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }


    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


    public void detener(View v)
    {
        if(gpsenabled==false)
        {
            boton.setText("Detener");
            gpsenabled=true;
            onResume();


        }
        else
        {
            boton.setText("Capturar datos");
            onPause();
            gpsenabled=false;

        }

    }

    public void pintar(View v)
    {
        ruta = map.addPolyline(coordenadas);
    }



}


