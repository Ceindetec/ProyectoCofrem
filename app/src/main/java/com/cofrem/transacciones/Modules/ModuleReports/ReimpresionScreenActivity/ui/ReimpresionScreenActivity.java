package com.cofrem.transacciones.Modules.ModuleReports.ReimpresionScreenActivity.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.ReportScreenActivity_;
import com.cofrem.transacciones.lib.PrintHandler;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Reports;
import com.cofrem.transacciones.Modules.ModuleReports.ReimpresionScreenActivity.ReimpresionScreenPresenter;
import com.cofrem.transacciones.Modules.ModuleReports.ReimpresionScreenActivity.ReimpresionScreenPresenterImpl;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.models.Transaccion;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@EActivity(R.layout.activity_report_reimpresion_screen)
public class ReimpresionScreenActivity extends Activity implements ReimpresionScreenView {

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SaldoScreenPresenter
    private ReimpresionScreenPresenter reimpresionScreenPresenter;

    private static final int PASO_ULTIMO_RECIBO = 1;
    private static final int PASO_NUMERO_CARGO = 2;

    private int paso;

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


    Transaccion modelTransaccion;

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

        inicializarOcultamientoVistas();

        Bundle args = getIntent().getExtras();

        switch (args.getInt(Reports.keyReport)) {
            case Reports.reportReimpresionRecibo:
                bodyContentReimpresionRecibo.setVisibility(View.VISIBLE);
                break;
            case Reports.reportReporteDetalle:
                bodyContentReporteDetallesImpresion.setVisibility(View.VISIBLE);
                break;
            case Reports.reportReporteGeneral:
                bodyContentReporteGeneralImpresion.setVisibility(View.VISIBLE);
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
        Toast.makeText(this, "no existen registros", Toast.LENGTH_LONG).show();

    }


    /**
     * Metodo para manejar la existencia de un recibo por numero de cargo
     */
    @Override
    public void handleVerifyExistenceReciboPorNumCargoSuccess(Transaccion modelTransaccion) {
        this.modelTransaccion = modelTransaccion;
        bodyContentReimpresionReciboNumeroCargo.setVisibility(View.GONE);
        bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.VISIBLE);

    }


    /**
     * Metodo para manejar la NO existencia de un recibo por numero de cargo
     */
    @Override
    public void handleVerifyExistenceReciboPorNumCargoError() {
        Toast.makeText(this, this.getString(R.string.report_text_message_No_existen_recibo_num_cargo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleVerifyClaveAdministradorSuccess() {
        bodyContentReimpresionReciboClaveAdministrador.setVisibility(View.GONE);
       switch (paso){
           case PASO_ULTIMO_RECIBO:
               bodyContentReimpresionReciboUltimo.setVisibility(View.VISIBLE);
               break;
           case PASO_NUMERO_CARGO:
               bodyContentReimpresionReciboImpresion.setVisibility(View.VISIBLE);
               txvReportReimprimeonReciboImpresionSaldoCantidad.setText(String.valueOf(modelTransaccion.getNumero_cargo()));
               break;
       }
        edtReportReimpresionReciboClaveAdministradorContenidoClave.setText("");
    }

// texto quemado hay que pitarlo
    @Override
    public void handleVerifyClaveAdministradorError() {
        Toast.makeText(this, this.getString(R.string.report_text_message_No_existen_recibo_num_cargo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleVerifyExistenceReporteDetalleSuccess() {

    }

    @Override
    public void handleVerifyExistenceReporteDetalleError() {

    }
    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para Obtener el String de fecha y hora
     *
     * @return String fecha
     */
    private String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());

        Date date = new Date();

        return dateFormat.format(date);
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
     * Metodo para validar la existencia de un ultimo recibo para Reimprimir
     */
    @Click(R.id.btnReportReimpresionReciboUltimoRecibo)
    public void validarExistenciaUltimoRecibo() {
        paso = PASO_ULTIMO_RECIBO;
        reimpresionScreenPresenter.validarExistenciaUltimoRecibo(this);
    }


    @Click(R.id.btnReportReimpresionReciboClaveAdministradorBotonAceptar)
    public void validarClaveAdministrador(){
        reimpresionScreenPresenter.validarClaveAdministrador(this,
                edtReportReimpresionReciboClaveAdministradorContenidoClave.getText().toString());
    }

    /**
     * Metodo que se encargara de Reimprimir el recibo
     */
    @Click(R.id.btnReportReimpresionReciboImprimirRecibo)
    public void imprimirUltimoRecibo() {

        Bitmap logo = BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo);
        String mensaje = this.getResources().getString(
                R.string.reimprimir_recibo,
                getDateTime(),
                modelTransaccion.getNumero_tarjeta(),
                String.valueOf(modelTransaccion.getValor()),
                String.valueOf(modelTransaccion.getNumero_cargo())
        );
//        PrintHandler.getInstance(this).printMessage(mensaje);
//        PrintHandler.getInstance(this).printPinture(logo);
//        PrintHandler.getInstance(this).printMessage(mensaje);


        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();
        printRows.add(new PrintRow("hola mundo",2,0,"nose",2,0,0));
        printRows.add(new PrintRow("hola mundo2",2,1,100));

        boolean respuesta = new PrinterHandler().imprimerTexto(printRows);

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
        paso = PASO_NUMERO_CARGO;
        reimpresionScreenPresenter.validarExistenciaReciboConNumCargo(this, edtReportReimprimeonReciboNummeroCargoContenidoClave.getText().toString());
    }


    @Click(R.id.btnReportReimprimeonReciboImpresionBotonImprimir)
    public void imprimirReimprimirNumCargo(){
        Bitmap logo = BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo);

        String mensaje = this.getResources().getString(
                R.string.reimprimir_recibo,
                getDateTime(),
                modelTransaccion.getNumero_tarjeta(),
                String.valueOf(modelTransaccion.getValor()),
                String.valueOf(modelTransaccion.getNumero_cargo())
        );

        PrintHandler.getInstance(this).printRecibo(logo,mensaje);
        edtReportReimprimeonReciboNummeroCargoContenidoClave.setText("");

    }

    @Click(R.id.btnReportReimprimeonReciboImpresionBotonSalir)
    public void cancelReimprimirNumCargo(){
        bodyContentReimpresionReciboImpresion.setVisibility(View.GONE);
        bodyContentReimpresionRecibo.setVisibility(View.VISIBLE);

    }


    @Click(R.id.btnReportReporteDetallesImpresionBotonImprimir)
    public  void validarExistenciaDestalleRecibos(){
        reimpresionScreenPresenter.validarExistenciaDetalleRecibos(this);
    }


















    @Click({R.id.btnTransactionScreenBack
            ,R.id.btnReportReporteDetallesImpresionBotonSalir
            ,R.id.btnReportReporteGeneralImpresionBotonSalir
            ,R.id.btnReportCierreLoteClaveDispositivoBotonCancelar
            ,R.id.btnReportCierreLoteImpresionBotonSalir
            ,R.id.btnReportReimprimeonReciboNummeroCargoBotonCancelar})
    public void regresarDesdeReimpimirRecibo(){
        Intent intent = new Intent(this, ReportScreenActivity_.class);
        startActivity(intent);
    }





}
