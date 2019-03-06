package com.cofrem.transacciones.splashScreen.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.MainScreenActivity_;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.ui.RegisterConfigurationScreenActivity_;
import com.cofrem.transacciones.splashScreen.SplashScreenPresenter;
import com.cofrem.transacciones.splashScreen.SplashScreenPresenterImpl;

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

        //Instanciamiento e inicializacion del presentador
        splashScreenPresenter = new SplashScreenPresenterImpl(this);

        //Llamada al metodo onCreate del presentador para el registro del bus de datos
        splashScreenPresenter.onCreate();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Previene la apertura del Status Bar
        getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);

        //Metodo para validar la configuracion inicial
        splashScreenPresenter.validateAccess(this);

    }

    /*
      #############################################################################################
      Metodos sobrecargados del sistema
      #############################################################################################
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
     * Metodo que interfiere en la presion del boton Task
     */
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /**
     * Metodo sobrecargado de la vista para la presion de las teclas de volumen
     *
     * @param event evento de la presion de una tecla
     * @return regresa el rechazo de la presion
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (InfoGlobalSettingsBlockButtons.blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /*
      #############################################################################################
      Metodos sobrecargados de la interface
      #############################################################################################
     */

    /**
     * Metodo para manejar la existencia de la configuracion inicial
     */
    @Override
    public void handleVerifyInitialConfigExiste() {

        //Agrega el texto de error del manejador de configuracion inicial encontrada
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_verify_configuration_initial_existe)
        );

        setInfoHeader();

    }

    /**
     * Metodo para manejar la NO existencia de la configuracion inicial
     */
    @Override
    public void handleVerifyInitialConfigNoExiste() {

        //Agrega el texto de error del manejador de configuracion inicial no encontrada
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_verify_configuration_initial_no_existe)
        );

    }

    /**
     * Metodo para manejar la existencia de la configuracion inicial NO valida
     */
    @Override
    public void handleVerifyInitialConfigNoValida() {

        //Oculta la barra de progreso
        hideProgress();

        //Agrega el texto de error del manejador de verificacion inicial no valida
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_verify_configuration_initial_no_valida)
        );

    }

    /**
     * Metodo para manejar la existencia de la configuracion de acceso
     */
    @Override
    public void handleRegistroConfiguracionAccesoExiste() {

        //Agrega el texto de informacion del manejador de registro de configuracion inicial existente
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_configuracion_acceso_existe)
        );

    }

    /**
     * Metodo para manejar la no existencia de la configuracion de acceso
     */
    @Override
    public void handleRegistroConfiguracionAccesoNoExiste() {

        //Agrega el texto de informacion del manejador de registro de configuracion inicial no existente
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_configuracion_acceso_no_existe)
        );

    }

    /**
     * Metodo para manejar el registro de la configuracion de acceso exitosa
     */
    @Override
    public void handleInsertRegistroConfiguracionAccesoSuccess() {

        //Agrega el texto de informacion del manejador de insercion de registro de configuracion inicial satisfactoria
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_insert_configuracion_acceso_success)
        );

        //Navegando a la ventana de configuración
        navigateToRegisterConfiguracionScreen();

    }

    /**
     * Metodo para manejar el registro de la configuracion de acceso erronea
     */
    @Override
    public void handleInsertRegistroConfiguracionAccesoError() {

        //Oculta la barra de progreso
        hideProgress();

        //Agrega el texto de informacion del manejador de insercion de registro de configuracion inicial erronea
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_insert_configuracion_acceso_error)
        );
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

        validateConfig();

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

        //Agrega el texto de informacion del manejador de conexion a internet existente
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

        //Oculta la barra de progreso
        hideProgress();

        //Agrega el texto de error del manejador de conexion a internet no existente
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
     * Metodo para manejar la conexion al dispositivo de impresion exitosa
     */
    @Override
    public void handleDeviceSuccess() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_device_info)
        );
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion erronea
     */
    @Override
    public void handleDeviceError() {
        txvSplashScreenInfo.setText(txvSplashScreenInfo.getText() +
                "\n" +
                getString(R.string.general_message_device_error)
        );
    }

    /**
     * Metodo para manejar la obtencion de la informacion del header exitosa
     */
    @Override
    public void handleGetInfoHeaderSuccess() {

        navigateToMainScreen();

    }

    /**
     * Metodo para manejar la obtencion de la informacion del header erronea
     */
    @Override
    public void handleGetInfoHeaderError() {
        //Muestra el mensaje de error de la informacion del header no disponible
        Toast.makeText(this, R.string.header_error_no_disponible, Toast.LENGTH_SHORT).show();

        navigateToMainScreen();

    }

    /*
      #############################################################################################
      Metodo propios de la clase
      #############################################################################################
     */

    /**
     * Metodo para mostrar la barra de progreso
     */
    private void showProgress() {
        //Muesra la barra  de progreso
        pgbLoadingSplashScreen.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        pgbLoadingSplashScreen.setVisibility(View.GONE);
    }

    /**
     * Metodo para validar la configuración inicial
     */
    private void validateConfig() {

        //Muestra la barra de progreso
        showProgress();

        /*
          Llamada al metodo validateInitialConfig del presentador que valida:
           - La existencia de la configuración inicial
           - En caso de no existir mostrará la vista de configuración
           - En caso de existir validara el acceso
         */
        splashScreenPresenter.validateInitialConfig(SplashScreenActivity.this);

    }

    /**
     * Metodo para llenar la informacion de los encabezados
     */
    private void setInfoHeader() {

        splashScreenPresenter.setInfoHeader(this);

    }

    /**
     * Metodo para navegar a la ventana inicial
     */
    private void navigateToMainScreen() {

        //Oculta la barra de progreso
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
        }, 100);
    }

    /**
     * Metodo para navegar a la ventana de registro de configuracion
     */
    private void navigateToRegisterConfiguracionScreen() {

        //Oculta la barra de progreso
        hideProgress();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Inicializacion del Bundle de argumentos
                Bundle args = new Bundle();

                args.putInt(Configurations.keyConfiguration, Configurations.configuracionRegistrarConfigInicial);

                Intent intent = new Intent(SplashScreenActivity.this, RegisterConfigurationScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtras(args);

                startActivity(intent);
            }
        }, 100);
    }

}
