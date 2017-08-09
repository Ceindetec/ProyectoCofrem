package com.cofrem.transacciones;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.ui.AnulacionScreenActivity_;
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui.CreditoScreenActivity_;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.ui.SaldoScreenActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_transaction_screen)
public class TransactionScreenActivity extends Activity {

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
    void TransactionInit() {
        setInfoHeader();
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
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnTransactionScreenBack)
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainScreenActivity_.class);
        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana Credito
     */
    @Click(R.id.btnTransactionScreenModuleCredito)
    public void navigateToCreditoScreen() {
        Intent intent = new Intent(this, CreditoScreenActivity_.class);
        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana Saldo
     */
    @Click(R.id.btnTransactionScreenModuleSaldo)
    public void navigateToSaldoScreen() {
        Intent intent = new Intent(this, SaldoScreenActivity_.class);
        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana Anulacion
     */
    @Click(R.id.btnTransactionScreenModuleAnulacion)
    public void navigateToAnulacionScreen() {
        Intent intent = new Intent(this, AnulacionScreenActivity_.class);
        startActivity(intent);
    }

}