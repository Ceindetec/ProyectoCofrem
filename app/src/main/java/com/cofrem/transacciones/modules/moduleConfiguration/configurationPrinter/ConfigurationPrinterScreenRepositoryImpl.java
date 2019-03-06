package com.cofrem.transacciones.modules.moduleConfiguration.configurationPrinter;

import android.content.Context;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.modules.moduleConfiguration.configurationPrinter.events.ConfigurationPrinterScreenEvent;

import java.util.ArrayList;

public class ConfigurationPrinterScreenRepositoryImpl implements ConfigurationPrinterScreenRepository {
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
    public ConfigurationPrinterScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     *
     */
    @Override
    public void VerifyConfigurationInitialPrinter(Context context) {

        ConfigurationPrinter configuracion = AppDatabase.getInstance(context).getConfigurationPrinter();

        if (configuracion != null) {
            postEvent(ConfigurationPrinterScreenEvent.onVerifyConfigurationInitialSuccess, configuracion);
        } else {
            postEvent(ConfigurationPrinterScreenEvent.onVerifyConfigurationInitialError);
        }

    }

    @Override
    public void saveConfigurationPrinter(Context context, ConfigurationPrinter configuration) {
        if (AppDatabase.getInstance(context).insertConfigurationPrinter(configuration)) {
            postEvent(ConfigurationPrinterScreenEvent.onSaveConfigurationPrinterSuccess);
        } else {
            postEvent(ConfigurationPrinterScreenEvent.onSaveConfigurationPrinterError);
        }
    }

    @Override
    public void imprimirPrueba(Context context, int gray) {

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_prueba_impresora), new StyleConfig(StyleConfig.Align.CENTER, gray, 50)));

        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ConfigurationPrinterScreenEvent.onPrintTestSuccess);
        } else {
            postEvent(ConfigurationPrinterScreenEvent.onPrintTestError, PrinterHandler.stringErrorPrinter(status, context));
        }

    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, ConfigurationPrinter configuracion, String errorMessage) {
        ConfigurationPrinterScreenEvent configurationPrinterScreenEvent = new ConfigurationPrinterScreenEvent();
        configurationPrinterScreenEvent.setEventType(type);
        if (errorMessage != null) {
            configurationPrinterScreenEvent.setErrorMessage(errorMessage);
        }
        if (configuracion != null) {
            configurationPrinterScreenEvent.setConfigurationPrinter(configuracion);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(configurationPrinterScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, ConfigurationPrinter configuration) {

        postEvent(type, configuration, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, String error) {
        postEvent(type, null, error);
    }

}
