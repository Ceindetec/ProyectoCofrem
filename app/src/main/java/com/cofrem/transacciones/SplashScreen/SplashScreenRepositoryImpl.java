package com.cofrem.transacciones.SplashScreen;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.SplashScreen.events.SplashScreenEvent;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;

public class SplashScreenRepositoryImpl implements SplashScreenRepository {

    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */


    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public SplashScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo que verifica:
     * - Existencia de datos
     * - Validez de datos
     *
     * @param context
     */
    @Override
    public void validateAcces(final Context context) {

        boolean deviceMagneticReader = verifyDeviceMagneticReader(context);
        boolean internetConnection = verifyInternetConnection(context);
        boolean devicePrinter = verifyDevicePrinter(context);
        boolean deviceNFC = verifyDeviceNFC(context);

        if ((deviceMagneticReader || deviceNFC) && devicePrinter && internetConnection) {
            postEvent(SplashScreenEvent.onVerifySuccess);
        } else {
            if (!deviceMagneticReader) {

                postEvent(SplashScreenEvent.onMagneticReaderDeviceError, Resources.getSystem().getString(R.string.general_message_magnetic_device_error));

            }
            if (!deviceNFC) {

                postEvent(SplashScreenEvent.onNFCDeviceError, Resources.getSystem().getString(R.string.general_message_nfc_device_error));

            }
            if (!devicePrinter) {

                postEvent(SplashScreenEvent.onPrinterDeviceError, Resources.getSystem().getString(R.string.general_message_printer_device_error));

            }
            if (!internetConnection) {

                postEvent(SplashScreenEvent.onInternetConnectionError, Resources.getSystem().getString(R.string.general_message_internet_error));

            }
        }


    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que verifica la existencia de conexion a internet en el dispositivo
     *
     * @param context
     * @return
     */
    private boolean verifyInternetConnection(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Metodo que verifica la existencia de un lector NFC en el dispositivo
     *
     * @param context
     * @return
     */
    private boolean verifyDeviceNFC(Context context) {
        return true;
    }

    /**
     * Metodo que verifica la existencia de un lector de banda ma en el dispositivo
     *
     * @param context
     * @return
     */
    private boolean verifyDeviceMagneticReader(Context context) {
        return true;
    }

    /**
     * Metodo que verifica la existencia de una impresora en el dispositivo
     *
     * @param context
     * @return
     */
    private boolean verifyDevicePrinter(Context context) {
        return true;
    }


    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage) {
        SplashScreenEvent splashScreenEvent = new SplashScreenEvent();
        splashScreenEvent.setEventType(type);
        if (errorMessage != null) {
            splashScreenEvent.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(splashScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null);

    }

}
