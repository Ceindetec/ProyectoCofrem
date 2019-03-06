package com.cofrem.transacciones.splashScreen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettings;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.MagneticHandler;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.splashScreen.events.SplashScreenEvent;

import java.util.Arrays;

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
     * - La existencia de la configuración inicial
     * - En caso de no existir mostrará la vista de configuración
     * - En caso de existir validara el acceso
     *
     * @param context
     */
    @Override
    public void validateInitialConfig(Context context) {

        //Variable que almacena el estado de la conexión a internet
        boolean internetConnection = verifyInternetConnection(context);

        //Valida la existencia de conexion a internet
        if (internetConnection) {

            // Registra el evento de existencia de conexion a internet
            postEvent(SplashScreenEvent.onInternetConnectionSuccess);

            //Consulta la existencia del registro de configuracion
            int conteoRegistrosConfiguracionInicial = AppDatabase.getInstance(context).conteoConfiguracionConexion();

            switch (conteoRegistrosConfiguracionInicial) {

                /**
                 * En caso de que no exista la configuracion
                 * - Registra el evento de no existencia de configuracion
                 * - Inserta el registro del valor de acceso al dispositivo
                 */
                case 0:

                    postEvent(SplashScreenEvent.onVerifyInitialConfigNoExiste);

                    //Consulta la existencia del registro de contraseña tecnica por defecto en el dispositivo
                    int conteoRegistroConfiguracionAcceso = AppDatabase.getInstance(context).conteoConfiguracionAcceso();

                    /**
                     * En caso de que no exista el registro de configuracion de acceso intenta registrarlo
                     */
                    if (conteoRegistroConfiguracionAcceso == 0) {

                        postEvent(SplashScreenEvent.onRegistroConfiguracionAccesoNoExiste);

                        //Registra el valor de acceso
                        if (AppDatabase.getInstance(context).insertRegistroInicialConfiguracionAcceso()) {

                            postEvent(SplashScreenEvent.onInsertRegistroConfiguracionAccesoSuccess);

                        } else {

                            postEvent(SplashScreenEvent.onInsertRegistroConfiguracionAccesoError);
                        }

                    } else {

                        postEvent(SplashScreenEvent.onRegistroConfiguracionAccesoExiste);

                    }

                    break;

                case 1:

                    postEvent(SplashScreenEvent.onVerifyInitialConfigExiste);

                    break;

                default:

                    postEvent(SplashScreenEvent.onVerifyInitialConfigNoValida);

                    break;

            }
        } else {
            postEvent(SplashScreenEvent.onInternetConnectionError);
        }
    }

    /**
     * Metodo que verifica:
     * - Conexion a internet
     * - Existencia datos en DB interna
     * - Coherencia de datos con el servidor
     *
     * @param context
     */
    @Override
    public void validateAcces(final Context context) {

        boolean deviceMagneticReader = verifyDeviceMagneticReader();
        boolean internetConnection = verifyInternetConnection(context);
        boolean devicePrinter = verifyDevicePrinter();
        boolean deviceNFC = verifyDeviceNFC();
        boolean device = verifyDevice();

        if ((deviceMagneticReader || deviceNFC) && device && devicePrinter && internetConnection) {

            if (verifyInitialRegister(context))
                postEvent(SplashScreenEvent.onVerifySuccess);
            else
                postEvent(SplashScreenEvent.onVerifyError);

        } else {
            if (!deviceMagneticReader) {

                postEvent(SplashScreenEvent.onMagneticReaderDeviceError);

            }
            if (!deviceNFC) {

                postEvent(SplashScreenEvent.onNFCDeviceError);

            }
            if (!devicePrinter) {

                postEvent(SplashScreenEvent.onPrinterDeviceError);

            }
            if (!internetConnection) {

                postEvent(SplashScreenEvent.onInternetConnectionError);

            }
            if (!device) {

                postEvent(SplashScreenEvent.onDeviceError);

            }
        }


    }

    /**
     * Metodo que consulta la informacion del header
     *
     * @param context
     */
    @Override
    public void setInfoHeader(Context context) {

        if (AppDatabase.getInstance(context).obtenerInfoHeader()) {

            postEvent(SplashScreenEvent.onGetInfoHeaderSuccess);

        } else {

            postEvent(SplashScreenEvent.onGetInfoHeaderError);

        }

    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que verifica la existencia del registro inicial
     *
     * @param context
     * @return
     */
    private boolean verifyInitialRegister(Context context) {

        boolean resultVerifyInitialRegisterProducto = false;
        boolean resultVerifyInitialRegisterConfigPrinter = false;

        // Validacion en caso de que no existan productos registrados en el sistema
        if (AppDatabase.getInstance(context).obtenerConteoRegistroProductos() == 0) {

            if (AppDatabase.getInstance(context).handlerRegistroInicialProductos()) {

                resultVerifyInitialRegisterProducto = true;

            }

        } else {

            resultVerifyInitialRegisterProducto = true;

        }

        // Validacion en caso de que no exista configuracion inicial para la impresora
        if (AppDatabase.getInstance(context).conteoConfigurationPrinter() == 0) {

            if (AppDatabase.getInstance(context).insertRegistroInicialConfiguracionPrinter()) {

                resultVerifyInitialRegisterConfigPrinter = true;

            }

        } else {

            resultVerifyInitialRegisterConfigPrinter = true;

        }

        return (resultVerifyInitialRegisterProducto && resultVerifyInitialRegisterConfigPrinter);

    }

    /**
     * Metodo que verifica la existencia del registro inicial
     *
     * @param context
     * @return
     */
    private boolean verifyInitialRegisterPrinter(Context context) {

        boolean resultVerifyInitialRegister = false;

        // Validacion en caso de que no exista configuracion inicial para la impresora
        if (AppDatabase.getInstance(context).conteoConfigurationPrinter() == 0) {

            if (AppDatabase.getInstance(context).insertRegistroInicialConfiguracionPrinter()) {

                resultVerifyInitialRegister = true;

            }

        } else {

            resultVerifyInitialRegister = true;

        }

        return resultVerifyInitialRegister;

    }

    /**
     * Metodo que verifica la existencia de conexion a internet en el dispositivo
     *
     * @param context
     * @return
     */
    private boolean verifyInternetConnection(Context context) {

        boolean resultVerifyInternetConnection = false;

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            resultVerifyInternetConnection = true;
        }

        return resultVerifyInternetConnection;

    }

    /**
     * Metodo que verifica la existencia de un lector NFC en el dispositivo
     *
     * @return
     */
    private boolean verifyDeviceNFC() {

        // TODO: Realizar proceso de comprobacion de dispositivo NFC / FASE 1
        return true;
    }

    /**
     * Metodo que verifica la existencia de un lector de banda ma en el dispositivo
     *
     * @return
     */
    private boolean verifyDeviceMagneticReader() {

        // Solicitud de prueba de dispositivo de lectura de banda magnetica
        return new MagneticHandler().testMagneticDevice();

    }

    /**
     * Metodo que verifica la existencia de una impresora en el dispositivo
     *
     * @return
     */
    private boolean verifyDevicePrinter() {

        // TODO: Realizar proceso de comprobacion de dispositivo de impresion / FASE
        return new PrinterHandler().testPrinterDevice();
    }

    /**
     * Metodo que verifica la existencia de una impresora en el dispositivo
     *
     * @return
     */
    private boolean verifyDevice() {

        boolean verifyDevice = false;

        if (Arrays.asList(InfoGlobalSettings.supportedDevices).contains(Build.MODEL)) {
            verifyDevice = true;
        }

        return verifyDevice;
    }


    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage, InfoHeaderApp infoHeaderApp) {

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

        postEvent(type, null, null);

    }

}
