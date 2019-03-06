package com.cofrem.transacciones.lib;

import android.os.AsyncTask;

import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import com.telpo.tps550.api.magnetic.MagneticCard;

public class MagneticAsync extends AsyncTask<Void, Integer, String[]> {

    //Call back interface
    public ResponseMagneticAsync delegate = null;

    /**
     * Constructor vacio de la clase
     */
    public MagneticAsync() {
    }


    /**
     * Constructor de la clase que recibe una instancia de la interface ResponseMagneticAsync
     * para la devolucion del proceso
     *
     * @param response
     */
    public MagneticAsync(ResponseMagneticAsync response) {
        //Asignacion de retorno atraves del contructor
        delegate = response;
    }

    @Override
    protected String[] doInBackground(Void... params) {

        String[] TracData = null;

        MagneticCard.startReading();

        long strat = System.currentTimeMillis();

        while (System.currentTimeMillis() - strat < 15000) {

            try {

                TracData = MagneticCard.check(500);

                if (TracData != null) {

                    break;

                }

            } catch (TimeoutException e) {

                e.printStackTrace();

            } catch (TelpoException e) {

                e.printStackTrace();

                break;

            }

        }

        return TracData;

    }

    @Override
    protected void onPreExecute() {

        try {
            MagneticCard.open();
        } catch (TelpoException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(String[] result) {

        MagneticCard.close();

        //Delegado que retorna
        delegate.processFinish(result);
    }

    public boolean testMagneticDevice() {

        boolean resultTestMagneticDevice = false;

        try {

            MagneticCard.open();

            MagneticCard.close();

            resultTestMagneticDevice = true;

        } catch (TelpoException e) {

            e.printStackTrace();

        } catch (Error e) {

            e.printStackTrace();

        }

        return resultTestMagneticDevice;

    }

    /**
     * Interface para el callback de datos
     */
    public interface ResponseMagneticAsync {
        String[] processFinish(String[] magneticResponse);
    }

}
