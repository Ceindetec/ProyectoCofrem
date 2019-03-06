package com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.MainScreenActivity_;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.TransactionScreenActivity_;
import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.lib.KeyBoard;
import com.cofrem.transacciones.lib.MagneticHandler;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionSaldo;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.SaldoScreenPresenter;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.SaldoScreenPresenterImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import static android.view.KeyEvent.KEYCODE_ENTER;

@EActivity(R.layout.activity_transaction_saldo_screen)
public class SaldoScreenActivity extends Activity implements SaldoScreenView {

    /*
      #############################################################################################
      Declaracion de componentes y variables
      #############################################################################################
     */

    final static int PASO_NUMERO_DOCUMENTO = 0;
    final static int PASO_DESLICE_TARJETA = 1;
    final static int PASO_CLAVE_USUARIO = 2;
    final static int PASO_TRANSACCION_EXITOSA = 3;
    /**
     * Declaracion de los Contoles
     */

    //Controles del header
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;
    //Controles del modulo
    @ViewById
    RelativeLayout bodyContentSaldoNumeroDocumento;
    @ViewById
    RelativeLayout bodyContentSaldoDesliceTarjeta;
    @ViewById
    RelativeLayout bodyContentSaldoLecturaIncorrecta;
    @ViewById
    RelativeLayout bodyContentSaldoPassUsuario;
    @ViewById
    RelativeLayout bodyContentSaldoTransaccionExitosa;

    //Paso transaction_saldo_paso_lectura_incorrecta
    @ViewById
    RelativeLayout bodyContentSaldoTransaccionErronea;
    @ViewById
    FrameLayout frlPgbHldTransactionSaldo;
    //Paso transaction_saldo_paso_numero_documento
    @ViewById
    EditText edtSaldoTransactionNumeroDocumentoValor;
    //Paso transaction_saldo_paso_deslice_tarjeta
    @ViewById
    TextView txvSaldoTransactionDesliceTarjetaNumeroDocumento;
    //Paso content_transaction_saldo_paso_pass_usuario
    @ViewById
    EditText edtSaldoTransactionClaveUsuarioContenidoClave;
    //Paso content_transaction_saldo_paso_transaccion_exitosa
    @ViewById
    TextView txvSaldoTransactionExitosaTarjetaValor;
    @ViewById
    TextView txvSaldoTransactionExitosaCedulaValor;
    @ViewById
    TextView txvSaldoTransactionExitosaSaldoCantidad;
    //Paso content_transaction_saldo_paso_transaccion_error
    @ViewById
    TextView txvSaldoTransactionErrorDetalleTexto;
    /**
     * Model que almacena la transaccion actual
     */
    Transaccion modelTransaccion = new Transaccion();
    /**
     * Pasos definidos
     */
    int pasoTransaccion = 0; //Define el paso actual
    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface AnulacionScreenPresenter
    private SaldoScreenPresenter saldoScreenPresenter;


    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void MainInit() {

        //Instanciamiento e inicializacion del presentador
        saldoScreenPresenter = new SaldoScreenPresenterImpl(this);

        //Llamada al metodo onCreate del presentador para el registro del bus de datos
        saldoScreenPresenter.onCreate();

        //Metodo que oculta por defecto los include de la vista
        inicializarOcultamientoVistas();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Coloca la informacion del encabezado
        setInfoHeader();

        //Inicializa el paso del registro de la configuracion
        pasoTransaccion = PASO_NUMERO_DOCUMENTO;

        //Primera ventana visible
        bodyContentSaldoNumeroDocumento.setVisibility(View.VISIBLE);

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
        saldoScreenPresenter.onDestroy();
        super.onDestroy();
    }


    /**
     * Metodo que intercepta las pulsaciones de las teclas del teclado fisico
     *
     * @param keyCode Tecla del evento
     * @param event   Evento
     * @return retorno de Boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * Keycodes disponibles
         *
         * 4: Back
         * 66: Enter
         * 67: Delete
         *
         */
        switch (keyCode) {

            case KEYCODE_ENTER:

                //Ocula el soft keyboard al presionar la tecla enter
                hideKeyBoard();

                switch (pasoTransaccion) {

                    case PASO_NUMERO_DOCUMENTO:
                        //Metodo para registrar el numero de documento
                        registrarNumeroDocumento();
                        break;

                    case PASO_DESLICE_TARJETA:
                        //Metodo para mostrar la orden de deslizar la tarjeta
                        deslizarTarjeta();
                        break;

                    case PASO_CLAVE_USUARIO:
                        //Metodo para registrar la contraseña del usuario
                        registrarClaveUsuario();
                        break;

                    case PASO_TRANSACCION_EXITOSA:
                        //Metodo para ir a la ventana principal
                        navigateToMainScreen();
                        break;
                }
                break;

            default:
                Log.i("Key Pressed", String.valueOf(keyCode));
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {

        switch (pasoTransaccion) {

            case PASO_NUMERO_DOCUMENTO:
                //Vacia la caja del valor del numero de documento
                edtSaldoTransactionNumeroDocumentoValor.setText("");
                break;

            case PASO_DESLICE_TARJETA:

                break;

            case PASO_CLAVE_USUARIO:
                //Vacia la caja del valor de la clave de usuario
                edtSaldoTransactionClaveUsuarioContenidoClave.setText("");
                break;

            case PASO_TRANSACCION_EXITOSA:

                break;
        }
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
     * Metodo para manejar la verificacion exitosa
     */
    @Override
    public void handleTransaccionSuccess(InformacionSaldo informacionSaldo) {

        //Ocula la barra de progreso
        hideProgress();

        //Oculta la vista de deslizar tarjeta
        bodyContentSaldoPassUsuario.setVisibility(View.GONE);

        //Muestra la vista de contraseña de usuario
        bodyContentSaldoTransaccionExitosa.setVisibility(View.VISIBLE);

        txvSaldoTransactionExitosaTarjetaValor.setText(informacionSaldo.getNumeroTarjeta());

        txvSaldoTransactionExitosaCedulaValor.setText(informacionSaldo.getCedulaUsuario());

        int saldo = Integer.parseInt(informacionSaldo.getValorTotalSaldo().split("\\.")[0]);

        txvSaldoTransactionExitosaSaldoCantidad.setText(PrintRow.numberFormat(saldo));

    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    @Override
    public void handleTransaccionWSRegisterError(String errorMessage) {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

        //Regresa a la vista de transacciones
        navigateToTransactionScreen();
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    @Override
    public void handleTransaccionWSConexionError() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista del Host de conexion
        bodyContentSaldoPassUsuario.setVisibility(View.GONE);

        //Muestra la vista del Port de conexion
        bodyContentSaldoTransaccionErronea.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaccion++;
    }

    /**
     * Metodo para manejar la orden de imprimir recibo exitosa
     */
    @Override
    public void handleImprimirReciboSuccess() {

    }

    /**
     * Metodo para manejar la orden de imprimir recibo Error
     */
    @Override
    public void handleImprimirReciboError(String errorMessage) {

        //Muestra el mensaje de error que paso en la impresora
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

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
        //TODO: VERIFICAR QUE ESTA MOSTRANDO LA BARRA DE PROGRESO
        //Muestra la barra  de progreso
        frlPgbHldTransactionSaldo.setVisibility(View.VISIBLE);
        frlPgbHldTransactionSaldo.bringToFront();
        frlPgbHldTransactionSaldo.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        //Oculta la barra de progreso
        frlPgbHldTransactionSaldo.setVisibility(View.GONE);
    }

    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {
        bodyContentSaldoNumeroDocumento.setVisibility(View.GONE);
        bodyContentSaldoDesliceTarjeta.setVisibility(View.GONE);
        bodyContentSaldoLecturaIncorrecta.setVisibility(View.GONE);
        bodyContentSaldoPassUsuario.setVisibility(View.GONE);
        bodyContentSaldoTransaccionExitosa.setVisibility(View.GONE);
        bodyContentSaldoTransaccionErronea.setVisibility(View.GONE);
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
     * Metodo que oculta el teclado al presionar el EditText
     */
    @LongClick({R.id.edtSaldoTransactionNumeroDocumentoValor,
            R.id.edtSaldoTransactionClaveUsuarioContenidoClave})
    @Click({R.id.edtSaldoTransactionNumeroDocumentoValor,
            R.id.edtSaldoTransactionClaveUsuarioContenidoClave})
    @Touch({R.id.edtSaldoTransactionNumeroDocumentoValor,
            R.id.edtSaldoTransactionClaveUsuarioContenidoClave})
    public void hideKeyBoard() {

        //TODO:VERIFICAR QUE EL TECLADO SE ESTA OCULTANDO
        //Oculta el teclado
        KeyBoard.hide(this);

    }

    /**
     * Metodo para regresar a la ventana de transaccion
     */
    @Click({R.id.btnSaldoTransactionNumeroDocumentoBotonCancelar,
            R.id.btnSaldoTransactionLecturaIncorrectaBotonSalir,
            R.id.btnSaldoTransactionClaveUsuarioBotonCancelar,
            R.id.btnSaldoTransactionErrorBotonSalir
    })
    public void navigateToTransactionScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SaldoScreenActivity.this, TransactionScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);
    }


    /**
     * Metodo para finalizar la transaccion
     */
    @Click(R.id.btnSaldoTransactionExitosaBotonSalir)
    public void navigateToMainScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SaldoScreenActivity.this, MainScreenActivity_.class);

                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);
    }


    /**
     * Metodo para registrar el numero de documento
     */
    @Click(R.id.btnSaldoTransactionNumeroDocumentoBotonAceptar)
    public void registrarNumeroDocumento() {

        //Se obtiene el texto del numero de documento
        String numeroDocumento = edtSaldoTransactionNumeroDocumentoValor.getText().toString();

        //Vacia la caja de contraseña
        edtSaldoTransactionNumeroDocumentoValor.setText("");

        if (numeroDocumento.length() > 0) {

            //Registra el valor del numero de documento en el modelo de transaccion
            modelTransaccion.setNumero_documento(numeroDocumento);

            txvSaldoTransactionDesliceTarjetaNumeroDocumento.setText(
                    String.valueOf(modelTransaccion.getNumero_documento())
            );

            //Oculta la vista del numero de documento
            bodyContentSaldoNumeroDocumento.setVisibility(View.GONE);

            //Muestra la vista de verificacion del valor
            bodyContentSaldoDesliceTarjeta.setVisibility(View.VISIBLE);

            //Actualiza el paso actual
            pasoTransaccion++;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    deslizarTarjeta();
                }
            }, 100);


        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_numero_documento, Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Metodo para mostrar la orden de deslizar la tarjeta
     */

    public void deslizarTarjeta() {

        String[] magneticHandler = new MagneticHandler().readMagnetic();

        if (magneticHandler != null) {

            String numeroTarjeta = magneticHandler[1]
                    .replace(";", "")
                    .replace("!", "")
                    .replace("#", "")
                    .replace("$", "")
                    .replace("&", "")
                    .replace("/", "")
                    .replace("|", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("=", "")
                    .replace("?", "")
                    .replace("¿", "")
                    .replace("¿", "")
                    .replace("¡", "")
                    .replace("*", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(".", "")
                    .replace("-", "")
                    .replace("_", "")
                    .replace("%", "");

            //Registra el valor del numero de tarjeta en el modelo de la transaccion
            modelTransaccion.setNumero_tarjeta(numeroTarjeta);

            //En caso de la lectura correcta se continua el proceso
            lecturaTarjetaCorrecta();

        } else {

            //En caso de la lectura erronea se muestra la pantalla de error
            lecturaTarjetaErronea();

        }

    }

    /**
     * Metodo para mostrar la lectura correcta de tarjeta
     */
    private void lecturaTarjetaCorrecta() {

        //Oculta la vista de deslizar tarjeta
        bodyContentSaldoDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de contraseña de usuario
        bodyContentSaldoPassUsuario.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaccion++;
    }

    /**
     * Metodo para mostrar la lectura erronea de tarjeta
     */
    private void lecturaTarjetaErronea() {

        //Oculta la vista de deslizar tarjeta
        bodyContentSaldoDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de contraseña de usuario
        bodyContentSaldoLecturaIncorrecta.setVisibility(View.VISIBLE);

    }

    /**
     * Metodo para registrar la contraseña del usuario
     */
    @Click(R.id.btnSaldoTransactionClaveUsuarioBotonAceptar)
    public void registrarClaveUsuario() {

        //Se obtiene el texto de la contraseña
        String passwordUser = edtSaldoTransactionClaveUsuarioContenidoClave.getText().toString();

        if (passwordUser.length() == 4) {

            //Vacia la caja contraseña
            edtSaldoTransactionClaveUsuarioContenidoClave.setText("");

            //Mostrar la barra de progreso
            showProgress();

            //Se registra la contraseña en el modelo
            modelTransaccion.setClave(passwordUser);

            //TODO: agregar las diferentes encriptaciones
            //Se registra el tipo de encriptacion en el modelo
            modelTransaccion.setTipo_encriptacion(Transaccion.CODIGO_ENCR_NO_ENCRIPTADO);

            //TODO: agregar los diferentes tipos de productos
            //Se registra el tipo de producto en el modelo
            modelTransaccion.setTipo_servicio(Transaccion.CODIGO_PRODUCTO_CUPO_ROTATIVO);

            //Actualiza el paso actual
            pasoTransaccion++;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    saldoScreenPresenter.registrarTransaccion(SaldoScreenActivity.this, modelTransaccion);
                }
            }, 100);


        } else {

            //Vacia la caja de contraseña
            edtSaldoTransactionClaveUsuarioContenidoClave.setText("");

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_usuario, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Metodo para finalizar la transaccion
     */
    @Click(R.id.btnSaldoTransactionExitosaBotonImprimir)
    public void imprimiRecibo() {

        saldoScreenPresenter.imprimirRecibo(this);

    }

}
