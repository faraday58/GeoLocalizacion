package com.temas.selectos.geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION = 100;
    private LocationManager ubicacion;
    private EditText edtLatitud;
    private EditText edtLongitud;
    private Button btnLocalizar;
    private Spinner spnProveedores;
    private PersonaLocaliza personaLocaliza;
    private TextView txtvPais;
    private TextView txtvDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtLatitud = findViewById(R.id.edtLatitud);
        edtLongitud = findViewById(R.id.edtLongitud);
        btnLocalizar = findViewById(R.id.btnLocalizar);
        spnProveedores = findViewById(R.id.spnProveedores);
        btnLocalizar.setOnClickListener(onClickLocalizar);
        txtvDireccion= findViewById(R.id.txtvDirec);
        txtvPais= findViewById(R.id.txtvPais);
        validaGPS();
        listaProveedores();
        registrarLocalizacion();


    }

    private void validaGPS() {

        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ubicacion == null) {
            Toast.makeText(this, "Su equipo no cuenta con ninguún proveedor de Localización", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Se requiere que el GPS se encuentre encendido", Toast.LENGTH_SHORT).show();
            if (!ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent intentGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intentGPS);


            }
        }


    }


    View.OnClickListener onClickLocalizar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            localizacion();
        }
    };


    private void localizacion() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_CODE_LOCATION);
        }
        Location location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            //edtLongitud.setText(String.valueOf(location.getLongitude()));
            //edtLatitud.setText(String.valueOf(location.getLatitude()));
            edtLatitud.setText(personaLocaliza.getLatitud());
            edtLongitud.setText(personaLocaliza.getLongitud());
            txtvPais.setText(personaLocaliza.getPais());
            txtvDireccion.setText(personaLocaliza.getLocalidad());
        }
    }

    private void registrarLocalizacion() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        personaLocaliza = new PersonaLocaliza(this);
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, personaLocaliza);

    }


    private void listaProveedores()
    {
        ubicacion= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String>listaProveedores= ubicacion.getAllProviders();
        String [] proveedores= new String[listaProveedores.size()];
        for(int i=0; i< proveedores.length; i++ )
        {
            LocationProvider proveedor = ubicacion.getProvider(listaProveedores.get(i));
            proveedores[i]=proveedor.getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,proveedores);
        spnProveedores.setAdapter(adapter);


    }
}
