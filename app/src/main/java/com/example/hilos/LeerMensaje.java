package com.example.hilos;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import java.time.LocalDate;

public class LeerMensaje extends Service {
    private Thread hilo;
    private boolean bandera = true;
    public LeerMensaje() {
    }

    @Override
    public void onCreate() {
        verMensaje();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        bandera = false;
        super.onDestroy();
    }

    public void verMensaje(){
    ContentResolver cr = this.getContentResolver();

        StringBuilder resultado = new StringBuilder();
        hilo = new Thread(new Runnable() {
            private String TextoMensaje = null;
            private String FechaMensaje = null;
            private String ContactoMensaje = null;
            @Override
            public void run() {
                try {

                    while (bandera) {

                        Uri uri = Uri.parse("content://sms/inbox");
                        Cursor cursor = cr.query(uri, null, null, null, "date DESC LIMIT 5");


                        if(cursor.getCount() > 0)
                        {
                            while (cursor.moveToNext()) {
                                int fecha = cursor.getColumnIndex(Telephony.Sms.DATE);
                                int contacto = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                                int mensaje = cursor.getColumnIndex(Telephony.Sms.BODY);
                                TextoMensaje = cursor.getString(mensaje);
                                FechaMensaje = cursor.getString(fecha);
                                ContactoMensaje = cursor.getString(contacto);
                                resultado.append("fecha: "+ LocalDate.parse(FechaMensaje)+" cont:"+ContactoMensaje+" msg:"+TextoMensaje+"\n");
                            }
                            cursor.close();
                        };
                        Log.d("Mensaje",resultado.toString());
                    }
                } catch (Exception ex) {

                }
            }
        });
        hilo.start();

    }

}