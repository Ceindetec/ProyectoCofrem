package com.cofrem.transacciones.SplashScreen.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cofrem.transacciones.MainScreenActivity_;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.SplashScreen.SplashScreenPresenter;
import com.cofrem.transacciones.SplashScreen.SplashScreenPresenterImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends Activity implements SplashScreenView {

    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    //Declaracion de los Contoles
    @ViewById
    TextView txvSplashScreenInfo;
    @ViewById
    ProgressBar pgbLoadingSplashScreen;

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SplashScreenPresenter
    private SplashScreenPresenter splashScreenPresenter;

    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void SplashInit() {

        // Metodo para colocar la orientacion de la app
        setOrientation();

        /**
         * Instanciamiento e inicializacion del presentador
         */
        splashScreenPresenter = new SplashScreenPresenterImpl(this);

        /**
         * Llamada al metodo onCreate del presentador para el registro del bus de datos
         */
        splashScreenPresenter.onCreate();

        /**
         * Llamada al metodo validateAcces del presentador que valida:
         *  - Conexion a internet
         *  - Existencia datos en DB interna
         *  - Coherencia de datos con el servidor
         */
        splashScreenPresenter.validateAccess(this);

    }

    /**
     * #############################################################################################
     * Metodos sobrecargados del sistema
     * #############################################################################################
     */

    /**
     * Metodo sobrecargado de la vista para la destruccion de la activity
     */
    @Override
    public void onDestroy() {
        splashScreenPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo para navegar a la ventana inicial
     */
    @Override
    public void navigateToMainScreen() {

        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n Aqui se muestra la pantalla principal"
        );

        hideProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainScreenActivity_.class);
                //Agregadas  ventanas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 3000);
    }

    /**
     * Metodo para mostrar la barra de progreso
     */
    @Override
    public void showProgress() {
        // Muesra la barra  de progreso
        pgbLoadingSplashScreen.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    @Override
    public void hideProgress() {
        //Oculta la barra de progreso
        pgbLoadingSplashScreen.setVisibility(View.GONE);
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    @Override
    public void handleVerifySuccess() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_verify_success)
        );

        navigateToMainScreen();

    }

    /**
     * Metodo para manejar la verificacion erronea
     */
    @Override
    public void handleVerifyError() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_verify_error)
        );

        hideProgress();
    }

    /**
     * Metodo para manejar la conexion a internet exitosa
     */
    @Override
    public void handleInternetConnectionSuccess() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_internet_info)
        );
    }

    /**
     * Metodo para manejar la conexion a internet erronea
     */
    @Override
    public void handleInternetConnectionError() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_internet_error)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo lector de banda magnetica exitosa
     */
    @Override
    public void handleMagneticReaderDeviceSuccess() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_magnetic_device_info)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo lector de banda magnetica erronea
     */
    @Override
    public void handleMagneticReaderDeviceError() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_magnetic_device_error)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo NFC exitosa
     */
    @Override
    public void handleNFCDeviceSuccess() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_nfc_device_info)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo NFC erronea
     */
    @Override
    public void handleNFCDeviceError() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_nfc_device_error)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion exitosa
     */
    @Override
    public void handlePrinterDeviceSuccess() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_printer_device_info)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion erronea
     */
    @Override
    public void handlePrinterDeviceError() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_printer_device_error)
        );
    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que coloca la orientacion de la App de forma predeterminada
     */
    private void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
