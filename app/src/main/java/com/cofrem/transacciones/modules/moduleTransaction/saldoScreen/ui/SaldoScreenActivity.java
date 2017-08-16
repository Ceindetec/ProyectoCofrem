package com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.SaldoScreenPresenter;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.SaldoScreenPresenterImpl;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.TransactionScreenActivity_;
import com.cofrem.transacciones.MainScreenActivity_;
import com.cofrem.transacciones.lib.KeyBoard;
import com.cofrem.transacciones.lib.MagneticHandler;
import com.cofrem.transacciones.models.Transaccion;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.view.KeyEvent.KEYCODE_ENTER;

@EActivity(R.layout.activity_transaction_saldo_screen)
public class SaldoScreenActivity extends Activity implements SaldoScreenView {

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

    // Contents del modulo
    @ViewById
    RelativeLayout bodyContentSaldoDesliceTarjeta;
    @ViewById
    RelativeLayout bodyContentSaldoPassUsuario;
    @ViewById
    RelativeLayout bodyContentSaldoTransaccionExitosa;
    @ViewById
    FrameLayout frlPgbHldTransactionSaldo;

    //Paso transaction_saldo_paso_clave_usuario
    @ViewById
    Button btnSaldoTransactionClaveUsuarioBotonCancelar;
    @ViewById
    EditText edtSaldoTransactionClaveUsuarioContenidoClave;

    //Paso transaction_saldo_paso_transaccion_exitosa
    @ViewById
    Button btnSaldoTransactionExitosaBotonImprimir;
    @ViewById
    Button btnSaldoTransactionExitosaBotonSalir;

    /**
     * Model que almacena la transaccion actual
     */
    Transaccion modelSaldo = new Transaccion();

    /**
     * Pasos definidos
     */
    int pasoCreditoTransaction = 0; // Define el paso actual

    final static int PASO_DESLIZAR_TARJETA = 0;
    final static int PASO_CLAVE_USUARIO = 1;
    final static int PASO_TRANSACCION_EXITOSA = 2;

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

        // Instanciamiento e inicializacion del presentador
        saldoScreenPresenter = new SaldoScreenPresenterImpl(this);

        // Llamada al metodo onCreate del presentador para el registro del bus de datos
        saldoScreenPresenter.onCreate();

        // Metodo para colocar la orientacion de la app
        setOrientation();

        // Metodo que oculta por defecto los include de la vista
        inicializarOcultamientoVistas();

        // Metodo que llena el header de la App
        setInfoHeader();

        //Inicializa el paso del registro de la configuracion
        pasoCreditoTransaction = PASO_DESLIZAR_TARJETA;

        //Primera ventana visible
        bodyContentSaldoDesliceTarjeta.setVisibility(View.VISIBLE);

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
        saldoScreenPresenter.onDestroy();
        super.onDestroy();
    }


    /**
     * Metodo que intercepta las pulsaciones de las teclas del teclado fisico
     *
     * @param keyCode
     * @param event
     * @return
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

                // Ocula el soft keyboard al presionar la tecla enter
                hideKeyBoard();

                switch (pasoCreditoTransaction) {

                    case PASO_DESLIZAR_TARJETA:
                        //Metodo para mostrar la orden de deslizar la tarjeta
                        deslizarTarjeta();
                        break;

                    case PASO_CLAVE_USUARIO:
                        //Metodo para registrar la contraseña del usuario
                        registrarClaveUsuario();
                        break;

                    case PASO_TRANSACCION_EXITOSA:
                        //Metodo para finalizar la transaccion
                        finalizarTransaccion();
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

        switch (pasoCreditoTransaction) {


            case PASO_DESLIZAR_TARJETA:

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
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo para manejar la verificacion exitosa
     */
    public void handleVerifySuccess() {

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
        //TODO: VERIFICAR QUE ESTA MOSTRANDO LA BARRA DE PROGRESO
        // Muestra la barra  de progreso
        frlPgbHldTransactionSaldo.setVisibility(View.VISIBLE);
        frlPgbHldTransactionSaldo.bringToFront();
        frlPgbHldTransactionSaldo.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Oculta la barra de progreso
                frlPgbHldTransactionSaldo.setVisibility(View.GONE);
            }
        }, 1000);
    }

    /**
     * Metodo que coloca la orientacion de la App de forma predeterminada
     */
    private void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {

        bodyContentSaldoDesliceTarjeta.setVisibility(View.GONE);
        bodyContentSaldoPassUsuario.setVisibility(View.GONE);
        bodyContentSaldoTransaccionExitosa.setVisibility(View.GONE);

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
    @Click({R.id.edtSaldoTransactionClaveUsuarioContenidoClave
    })
    public void hideKeyBoard() {

        //TODO:VERIFICAR QUE EL TECLADO SE ESTA OCULTANDO
        //Oculta el teclado
        KeyBoard.hide(this);

    }

    /**
     * Metodo para regresar a la ventana de transaccion
     */
    @Click({R.id.btnSaldoTransactionClaveUsuarioBotonCancelar})
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
        }, 1000);
    }

    /**
     * Metodo para mostrar la orden de deslizar la tarjeta
     */
    public void deslizarTarjeta() {

        String[] magneticHandler = new MagneticHandler().readMagnetic();

        //Registra el valor del host en el modelo de la configuracion
        modelSaldo.setNumero_tarjeta(magneticHandler[1]);

        //Oculta la vista de deslizar la tarjeta
        bodyContentSaldoDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de clave de usuario
        bodyContentSaldoPassUsuario.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoCreditoTransaction++;

    }

    /**
     * Metodo para registrar la contraseña del usuario
     */
    @Click(R.id.btnCreditoTransactionClaveUsuarioBotonAceptar)
    public void registrarClaveUsuario() {

        // Se obtiene el texto de la contraseña
        String passwordUser = edtSaldoTransactionClaveUsuarioContenidoClave.getText().toString();

        if (passwordUser.length() == 4) {

            //Mostrar la barra de progreso
            showProgress();

            //Registra la transaccion
            saldoScreenPresenter.registrarTransaccion(this, modelSaldo);


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
    @Click(R.id.btnSaldoTransactionExitosaBotonSalir)
    public void finalizarTransaccion() {

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
        }, 1000);
    }

    /**
     * Metodo para finalizar la transaccion
     */
    @Click(R.id.btnSaldoTransactionExitosaBotonImprimir)
    public void imprimiRecibo() {

        //TODO: imprimir recibo

    }

}
