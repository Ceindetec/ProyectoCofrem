package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.ReportScreenActivity_;
import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.lib.KeyBoard;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.Reports;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.ReimpresionScreenPresenter;
import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.ReimpresionScreenPresenterImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_report_reimpresion_screen)
public class ReimpresionScreenActivity extends Activity implements ReimpresionScreenView {

    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */

    private static final int PASO_ULTIMO_RECIBO = 1;
    private static final int PASO_NUMERO_CARGO = 2;
    private static final int PASO_DETALLE = 3;
    private static final int PASO_GENERAL = 4;
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
    RelativeLayout bodyContentReimpresionRecibo;
    @ViewById
    RelativeLayout bodyContentReimpresionReciboUltimo;
    @ViewById
    RelativeLayout bodyContentReimpresionReciboNumeroCargo;
    @ViewById
    RelativeLayout bodyContentReimpresionReciboImpresion;
    @ViewById
    RelativeLayout bodyContentReporteDetallesImpresion;
    @ViewById
    RelativeLayout bodyContentReporteGeneralImpresion;
    @ViewById
    RelativeLayout bodyContentCierreLoteClaveDispositivo;
    @ViewById
    RelativeLayout bodyContentCierreLoteVerificacion;
    @ViewById
    RelativeLayout bodyContentCierreLoteImpresion;
    @ViewById
    RelativeLayout bodyContentReimpresionReciboClaveAdministrador;
    @ViewById
    EditText edtReportReimprimeonReciboNummeroCargoContenidoClave;
    @ViewById
    TextView txvReportReimprimeonReciboImpresionSaldoCantidad;
    @ViewById
    EditText edtReportReimpresionReciboClaveAdministradorContenidoClave;
    @ViewById
    Button btnReportReimpresionReciboImprimirRecibo;
    @ViewById
    Button btnReportCierreLoteClaveDispositivoBotonAceptar;
    @ViewById
    FrameLayout frlPgbHldReimpresionRecibo;
    Transaccion modelTransaccion;
    String passwordAdmin = "";
    String numeroCargo = "";
    /**
     * Pasos definidos
     */
    private int pasoReporte;
    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SaldoScreenPresenter
    private ReimpresionScreenPresenter reimpresionScreenPresenter;

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
        reimpresionScreenPresenter = new ReimpresionScreenPresenterImpl(this);

        /**
         * Llamada al metodo onCreate del presentador para el registro del bus de datos
         */
        reimpresionScreenPresenter.onCreate();

        /**
         * metodo verificar acceso
         */
        //TODO: crear metodos
        reimpresionScreenPresenter.VerifySuccess();

        // Metodo que oculta por defecto los include de la vista
        inicializarOcultamientoVistas();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Coloca la informacion del encabezado
        setInfoHeader();

        Bundle args = getIntent().getExtras();

        switch (args.getInt(Reports.keyReport)) {
            case Reports.reportReimpresionRecibo:
                bodyContentReimpresionRecibo.setVisibility(View.VISIBLE);
                break;
            case Reports.reportReporteDetalle:
                pasoReporte = PASO_DETALLE;
                reimpresionScreenPresenter.validarExistenciaDetalleRecibos(this);
                break;
            case Reports.reportReporteGeneral:
                pasoReporte = PASO_GENERAL;
                bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.VISIBLE);
                break;
            case Reports.reportCierreLote:
                bodyContentCierreLoteClaveDispositivo.setVisibility(View.VISIBLE);
                break;
        }

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
        reimpresionScreenPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {
        String mensajeRegresar = getString(R.string.general_message_press_back) + getString(R.string.general_text_button_regresar);
        Toast.makeText(this, mensajeRegresar, Toast.LENGTH_SHORT).show();
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


    /**
     * Metodo para manejar la existencia de un Ultimo recibo para reimprimir
     */
    @Override
    public void handleVerifyExistenceUltimoReciboSuccess(Transaccion modelTransaccion) {
        this.modelTransaccion = modelTransaccion;
        bodyContentReimpresionRecibo.setVisibility(View.GONE);
        bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para manejar la NO existencia de un Ultimo recibo para reimprimir
     */
    @Override
    public void handleVerifyExistenceUltimoReciboError() {
// texto quemado hay que pitarlo
        hideProgress();
        Toast.makeText(this, "no existen registros", Toast.LENGTH_LONG).show();

    }


    /**
     * Metodo para manejar la existencia de un recibo por numero de cargo
     */
    @Override
    public void handleVerifyExistenceReciboPorNumCargoSuccess(Transaccion modelTransaccion) {
        hideProgress();
        this.modelTransaccion = modelTransaccion;
        bodyContentReimpresionReciboNumeroCargo.setVisibility(View.GONE);
        bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.VISIBLE);

    }


    /**
     * Metodo para manejar la NO existencia de un recibo por numero de cargo
     */
    @Override
    public void handleVerifyExistenceReciboPorNumCargoError() {
        hideProgress();
        edtReportReimpresionReciboClaveAdministradorContenidoClave.setText("");
        Toast.makeText(this, this.getString(R.string.report_text_message_No_existen_recibo_num_cargo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleVerifyClaveAdministradorSuccess() {

        hideProgress();

        bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.GONE);
        switch (pasoReporte) {
            case PASO_ULTIMO_RECIBO:
                bodyContentReimpresionReciboUltimo.setVisibility(View.VISIBLE);
                break;
            case PASO_NUMERO_CARGO:
                bodyContentReimpresionReciboImpresion.setVisibility(View.VISIBLE);
                txvReportReimprimeonReciboImpresionSaldoCantidad.setText(modelTransaccion.getNumero_cargo());
                break;
            case PASO_DETALLE:
                bodyContentReporteDetallesImpresion.setVisibility(View.VISIBLE);
                break;
            case PASO_GENERAL:
                bodyContentReporteGeneralImpresion.setVisibility(View.VISIBLE);
                break;
        }
        edtReportReimpresionReciboClaveAdministradorContenidoClave.setText("");
    }

    @Override
    public void handleVerifyClaveAdministradorError() {
        hideProgress();
        edtReportReimpresionReciboClaveAdministradorContenidoClave.setText("");
        Toast.makeText(this, this.getString(R.string.report_text_message_clave_admin_incorrecta), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleVerifyExistenceReporteDetalleSuccess() {
        hideProgress();
        bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.VISIBLE);
    }


    @Override
    public void handleVerifyExistenceReporteDetalleError() {
        hideProgress();
        Toast.makeText(this, this.getString(R.string.report_text_message_No_existen_recibos), Toast.LENGTH_LONG).show();
        regresarDesdeReimpimirRecibo();
    }

    @Override
    public void handleImprimirUltimoReciboSuccess() {

        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                regresarDesdeReimpimirRecibo();
            }
        }, 5000);

    }

    @Override
    public void handleImprimirUltimoReciboError(String error) {
        hideProgress();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleImprimirReciboPorNumCargoSuccess() {
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                regresarDesdeReimpimirRecibo();
            }
        }, 5000);
    }

    @Override
    public void handleImprimirReciboPorNumCargoError(String error) {
        hideProgress();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleImprimirReporteDetalleSuccess() {
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                regresarDesdeReimpimirRecibo();
            }
        }, 5000);
    }

    @Override
    public void handleImprimirReporteDetalleError(String error) {
        hideProgress();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleImprimirReporteGeneralSuccess() {
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                regresarDesdeReimpimirRecibo();
            }
        }, 5000);
    }

    @Override
    public void handleImprimirReporteGeneralError(String error) {
        hideProgress();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleCierreDeLoteSuccess() {
        hideProgress();
        bodyContentCierreLoteClaveDispositivo.setVisibility(View.GONE);
        bodyContentCierreLoteImpresion.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleCierreDeLoteError(String error) {
        hideProgress();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleTransaccionWSConexionError(String error) {
        hideProgress();
        Toast.makeText(this, R.string.configuration_text_informacion_dispositivo_error_conexion, Toast.LENGTH_LONG).show();
    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */


    /**
     * Metodo que oculta el teclado al presionar el EditText
     */
    @LongClick({R.id.edtReportReimprimeonReciboNummeroCargoContenidoClave,
            R.id.edtReportReimpresionReciboClaveAdministradorContenidoClave
    })
    @Click({R.id.edtReportReimprimeonReciboNummeroCargoContenidoClave,
            R.id.edtReportReimpresionReciboClaveAdministradorContenidoClave
    })
    @Touch({R.id.edtReportReimprimeonReciboNummeroCargoContenidoClave,
            R.id.edtReportReimpresionReciboClaveAdministradorContenidoClave
    })
    public void hideKeyBoard() {

        //TODO:VERIFICAR QUE EL TECLADO SE ESTA OCULTANDO
        //Oculta el teclado
        KeyBoard.hide(this);

    }

    /**
     * Metodo para mostrar la barra de progreso
     */
    private void showProgress() {
        //TODO: VERIFICAR QUE ESTA MOSTRANDO LA BARRA DE PROGRESO
        //Muestra la barra  de progreso
        frlPgbHldReimpresionRecibo.setVisibility(View.VISIBLE);
        frlPgbHldReimpresionRecibo.bringToFront();
        frlPgbHldReimpresionRecibo.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        //Oculta la barra de progreso
        frlPgbHldReimpresionRecibo.setVisibility(View.GONE);
    }

    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {

        bodyContentReimpresionRecibo.setVisibility(View.GONE);
        bodyContentReimpresionReciboUltimo.setVisibility(View.GONE);
        bodyContentReimpresionReciboNumeroCargo.setVisibility(View.GONE);
        bodyContentReimpresionReciboImpresion.setVisibility(View.GONE);
        bodyContentReporteDetallesImpresion.setVisibility(View.GONE);
        bodyContentReporteGeneralImpresion.setVisibility(View.GONE);
        bodyContentCierreLoteClaveDispositivo.setVisibility(View.GONE);
        bodyContentCierreLoteVerificacion.setVisibility(View.GONE);
        bodyContentCierreLoteImpresion.setVisibility(View.GONE);

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

    /**
     * Metodo para validar la existencia de un ultimo recibo para Reimprimir
     */
    @Click(R.id.btnReportReimpresionReciboUltimoRecibo)
    public void validarExistenciaUltimoRecibo() {
        pasoReporte = PASO_ULTIMO_RECIBO;
        reimpresionScreenPresenter.validarExistenciaUltimoRecibo(this);
    }

    /**
     * Metodo que se encargara de validar la clave del administrador
     */
    @Click(R.id.btnReportReimpresionReciboClaveAdministradorBotonAceptar)
    public void validarClaveAdministrador() {

        //Se obtiene el texto de la contraseña
        passwordAdmin = edtReportReimpresionReciboClaveAdministradorContenidoClave.getText().toString();

        //Vacia la caja de contraseña
        edtReportReimpresionReciboClaveAdministradorContenidoClave.setText("");

        //La contraseña debe ser de exactamente 4 caracteres
        if (passwordAdmin.length() == 4) {

            //Muestra la barra de progreso
            showProgress();

            //Valida la contraseña
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reimpresionScreenPresenter.validarClaveAdministrador(ReimpresionScreenActivity.this, passwordAdmin);
                }
            }, 100);

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_administrador, Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Metodo que se encargara de Reimprimir el recibo
     */
    @Click(R.id.btnReportReimpresionReciboImprimirRecibo)
    public void imprimirUltimoRecibo() {
        btnReportReimpresionReciboImprimirRecibo.setEnabled(false);
        reimpresionScreenPresenter.imprimirUltimoRecibo(this);
    }

    /**
     * Metodo para salir de la vista de Reimpresion Ultimo
     */
    @Click(R.id.btnReportReimpresionReciboSalir)
    public void salirDelContentReimprimirUltimo() {
        bodyContentReimpresionReciboUltimo.setVisibility(View.GONE);
        bodyContentReimpresionRecibo.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para iniciar el proceso de Reimprimir recibo por numero de cargo
     */
    @Click(R.id.btnReportReimpresionReciboNumeroDeCargo)
    public void navigateToContentReimprimirNumCargo() {
        bodyContentReimpresionRecibo.setVisibility(View.GONE);
        bodyContentReimpresionReciboNumeroCargo.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para validad si el nuero de cargo existe para imprimir el recibo
     */
    @Click(R.id.btnReportReimprimeonReciboNummeroCargoBotonAceptar)
    public void acceptReimprimirNumCargo() {

        //Se obtiene el texto de la contraseña
        numeroCargo = edtReportReimprimeonReciboNummeroCargoContenidoClave.getText().toString();

        //Vacia la caja de contraseña
        edtReportReimprimeonReciboNummeroCargoContenidoClave.setText("");

        //La contraseña debe ser de exactamente 4 caracteres
        if (numeroCargo.length() == 7) {

            //Muestra la barra de progreso
            showProgress();

            pasoReporte = PASO_NUMERO_CARGO;

            //Valida la contraseña
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reimpresionScreenPresenter.validarExistenciaReciboConNumCargo(ReimpresionScreenActivity.this, numeroCargo);
                }
            }, 100);

        } else {

            //Muestra el mensaje de error de formato del numero de cargo
            Toast.makeText(this, R.string.transaction_error_format_numero_cargo, Toast.LENGTH_SHORT).show();

        }
    }


    @Click(R.id.btnReportReimprimeonReciboImpresionBotonImprimir)
    public void imprimirReimprimirNumCargo() {

        reimpresionScreenPresenter.imprimirReciboConNumCargo(this);
        edtReportReimprimeonReciboNummeroCargoContenidoClave.setText("");

    }

    @Click(R.id.btnReportReimprimeonReciboImpresionBotonSalir)
    public void cancelReimprimirNumCargo() {
        bodyContentReimpresionReciboImpresion.setVisibility(View.GONE);
        bodyContentReimpresionRecibo.setVisibility(View.VISIBLE);

    }


    @Click(R.id.btnReportReporteDetallesImpresionBotonImprimir)
    public void imprimirReporteDetalle() {
        reimpresionScreenPresenter.imprimirReporteDetalle(this);
    }

    @Click(R.id.btnReportReporteGeneralImpresionBotonImprimir)
    public void imprimirReporteGeneral() {
        reimpresionScreenPresenter.imprimirReporteGeneral(this);
    }

    @Click(R.id.btnReportCierreLoteClaveDispositivoBotonAceptar)
    public void cierreDeLote() {
        showProgress();
        btnReportCierreLoteClaveDispositivoBotonAceptar.setEnabled(false);
        reimpresionScreenPresenter.cierreDeLote(this);
    }

    @Click(R.id.btnReportCierreLoteImpresionBotonImprimir)
    public void imprimirCierreLote() {
        reimpresionScreenPresenter.imprimirCierreLote(this);
    }


    @Click({R.id.btnTransactionScreenBack
            , R.id.btnReportReporteDetallesImpresionBotonSalir
            , R.id.btnReportReporteGeneralImpresionBotonSalir
            , R.id.btnReportCierreLoteClaveDispositivoBotonCancelar
            , R.id.btnReportCierreLoteImpresionBotonSalir
            , R.id.btnReportReimprimeonReciboNummeroCargoBotonCancelar
            , R.id.btnReportReimpresionReciboClaveAdministradorBotonCancelar})
    public void regresarDesdeReimpimirRecibo() {
        Intent intent = new Intent(this, ReportScreenActivity_.class);
        startActivity(intent);
    }

    public void regresarDesdeReimpimirRecibo(int timer) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                regresarDesdeReimpimirRecibo();
            }
        }, timer);
    }


}
