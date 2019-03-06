package com.cofrem.transacciones;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.modules.moduleConfiguration.configurationPrinter.ui.ConfigurationPrinterScreenActivity_;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.ui.RegisterConfigurationScreenActivity_;
import com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen.ui.TestCommunicationScreenActivity_;
import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.ui.UltimaTransaccionScreenActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_configuration_screen)
public class ConfigurationScreenActivity extends Activity {

    /**
     * #############################################################################################
     * Definición de variables
     * #############################################################################################
     */
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;

    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void ConfigurationInit() {
        setInfoHeader();
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados del sistema
     * #############################################################################################
     */

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {
        String mensajeRegresar = getString(R.string.general_message_press_back) + getString(R.string.general_text_button_regresar);
        Toast.makeText(this, mensajeRegresar, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metodo sobrecargado de la vista cuando pierde el foco
     *
     * @param hasFocus regresa el parametro del foco del sistema
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            //Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
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
     * Metodo propios de la clase
     * #############################################################################################
     */
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

    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnConfigurationScreenModuleRegisterConfiguration)
    public void navigateToRegisterConfiguration() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Inicializacion del Bundle de argumentos
                Bundle args = new Bundle();

                args.putInt(Configurations.keyConfiguration, Configurations.configuracionRegistrar);

                Intent intent = new Intent(ConfigurationScreenActivity.this, RegisterConfigurationScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtras(args);

                startActivity(intent);
            }
        }, 100);

    }


    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnConfigurationScreenModuleRegisterConfigurationPrinter)
    public void navigateToConfigurationPrinter() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConfigurationScreenActivity.this, ConfigurationPrinterScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);

    }

    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnConfigurationScreenModuleTestCommunication)
    public void navigateToTestCommunication() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConfigurationScreenActivity.this, TestCommunicationScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);

    }

    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnConfigurationScreenModuleUltimaTransaccion)
    public void navigateToUltimaTransaccion() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConfigurationScreenActivity.this, UltimaTransaccionScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);

    }

    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnConfigurationScreenBack)
    public void navigateToMainScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConfigurationScreenActivity.this, MainScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);

    }


}