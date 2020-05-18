package com.temas.selectos.geolocalizacion;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PersonaLocaliza implements LocationListener {

    private String longitud;
    private String latitud;
    private Context context;
    private String pais;

    public PersonaLocaliza(Context context) {
        this.context = context;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    private String localidad;


    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }


    @Override
    public void onLocationChanged(Location location) {
        //System.out.println("La dirección ha cambiado");

        longitud = String.valueOf(location.getLongitude());
        latitud = String.valueOf(location.getLatitude());

        Geocoder geocoder= new Geocoder(context, Locale.getDefault());
        try {
            List<Address> direccion= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            pais= direccion.get(0).getCountryName();
            localidad= direccion.get(0).getLocality();

            //System.out.println(direccion.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
