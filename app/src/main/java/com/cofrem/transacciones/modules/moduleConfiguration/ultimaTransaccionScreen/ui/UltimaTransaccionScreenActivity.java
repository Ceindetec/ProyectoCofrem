package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cofrem.transacciones.ConfigurationScreenActivity_;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.UltimaTransaccionScreenPresenter;
import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.UltimaTransaccionScreenPresenterImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_configuration_ultima_screen)
public class UltimaTransaccionScreenActivity extends Activity implements UltimaTransaccionScreenView {

    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */

    /**
     * Declaracion de los Contoles
     */

    // Controles del header

    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;
    @ViewById
    TextView txvConfiguracionUltimaTransaccionResultado;
    @ViewById
    FrameLayout frlPgbHldUltimaTransaccion;

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SaldoScreenPresenter
    private UltimaTransaccionScreenPresenter ultimaTransaccionScreenPresenter;


    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void MainInit() {

        /**
         * Instanciamiento e inicializacion del presentador
         */
        ultimaTransaccionScreenPresenter = new UltimaTransaccionScreenPresenterImpl(this);

        /**
         * Llamada al metodo onCreate del presentador para el registro del bus de datos
         */
        ultimaTransaccionScreenPresenter.onCreate();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Coloca la informacion del encabezado
        setInfoHeader();

        ultimaTransaccionScreenPresenter.validarUltimaTransaccion(this);

        showProgress();
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
        ultimaTransaccionScreenPresenter.onDestroy();
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

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    @Override
    public void onValidarUltimaTransaccionCorrecta() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txvConfiguracionUltimaTransaccionResultado.setText(R.string.configuration_text_ultima_correcta);
                hideProgress();
            }
        }, 2000);

    }

    @Override
    public void onValidarUltimaTransaccionErronea() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txvConfiguracionUltimaTransaccionResultado.setText(R.string.configuration_text_ultima_erronea);
                hideProgress();
            }
        }, 2000);
    }

    @Override
    public void onValidarUltimaTransaccionError(final String error) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txvConfiguracionUltimaTransaccionResultado.setText(error);
                hideProgress();
            }
        }, 500);
    }

    @Override
    public void handleTransaccionWSConexionError() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txvConfiguracionUltimaTransaccionResultado.setText(R.string.configuration_text_informacion_dispositivo_error_conexion);
                hideProgress();
            }
        }, 500);
    }

    /**
     *
     */
    @Click(R.id.btnConfiguracionUltimaTransaccionBotonSalir)
    void returnConfiguracionScreenActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(UltimaTransaccionScreenActivity.this, ConfigurationScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 500);
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para mostrar la barra de progreso
     */
    private void showProgress() {
        frlPgbHldUltimaTransaccion.setVisibility(View.VISIBLE);
        frlPgbHldUltimaTransaccion.bringToFront();
        frlPgbHldUltimaTransaccion.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        frlPgbHldUltimaTransaccion.setVisibility(View.GONE);
    }

    /**
     * Metodo que llena el header de la App
     */
    private void setInfoHeader() {

        txvHeaderIdDispositivo.setText(
                String.format(
                        getString(R.string.header_text_id_dispositivo_registrado)
                        , InfoHeaderApp.getInstance().getIdDispositivo()
                )
        );

        txvHeaderIdPunto.setText(
                String.format(
                        getString(R.string.header_text_id_punto_registrado)
                        , InfoHeaderApp.getInstance().getIdPunto()
                )
        );

        txvHeaderEstablecimiento.setText(
                String.format(
                        getString(R.string.header_text_nombre_establecimiento_registrado)
                        , InfoHeaderApp.getInstance().getNombreEstablecimiento()
                )
        );

        txvHeaderPunto.setText(
                String.format(
                        getString(R.string.header_text_nombre_punto_registrado)
                        , InfoHeaderApp.getInstance().getNombrePunto()
                )
        );

    }
}
