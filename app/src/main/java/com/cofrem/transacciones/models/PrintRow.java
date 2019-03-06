package com.cofrem.transacciones.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.lib.StyleConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by luispineda on 12/07/17.
 */

public class PrintRow {

    private String msg1, msg2;
    private Bitmap logo;
    private StyleConfig styleConfig;
    private StyleConfig.Align align;
    private int lineSpace;
    private int gray;


    public PrintRow() {
    }

    public PrintRow(Bitmap logo, StyleConfig.Align align, int gray, int lineSpace) {
        this.logo = logo;
        this.align = align;
        this.lineSpace = lineSpace;
        this.gray = gray;
    }

    public PrintRow(String msg1, StyleConfig styleConfig) {
        this.msg1 = msg1;
        this.styleConfig = styleConfig;
    }

    public PrintRow(String msg1, String msg2, StyleConfig styleConfig) {
        this.msg1 = msg1;
        this.msg2 = msg2;
        this.styleConfig = styleConfig;

    }

    public static PrintRow printLogo(Context context, int gray) {

        //logo de COFREM que se imprime al inicio del recibo
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);

        return new PrintRow(logo, StyleConfig.Align.CENTER, gray, 3);
    }

    public static void printCofrem(Context context, ArrayList<PrintRow> printRows, int gray, int lineSpace) {

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_nit, context.getResources().getString(
                        R.string.recibo_nit_cofrem)), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_direccion_cofrem), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ciudad_cofrem), new StyleConfig(StyleConfig.Align.CENTER, gray, 20)));

    }

    public static void printOperador(Context context, ArrayList<PrintRow> printRows, int gray, int lineSpace) {

        Establishment modelEstablishment = AppDatabase.getInstance(context).getEstablecimiento();

        String terminal = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_nit, modelEstablishment.getNit()), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(modelEstablishment.getCodigo() + " - " + modelEstablishment.getNombre(), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(modelEstablishment.getDireccion(), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(modelEstablishment.getCiudad(), new StyleConfig(StyleConfig.Align.CENTER, gray, lineSpace)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_transaccion), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_terminal, terminal), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F3)));

    }

    public static void printFirma(Context context, ArrayList<PrintRow> printRows, int gray) {

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ingresa_firma), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ingresa_cc), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ingresa_tel), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

    }

    public static String numberFormat(int number) {

        DecimalFormat formateador = new DecimalFormat("###,###.##");

        return formateador.format(number);

    }

    public String getMsg1() {
        return msg1;
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public String getMsg2() {
        return msg2;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public StyleConfig getStyleConfig() {
        return styleConfig;
    }

    public void setStyleConfig(StyleConfig styleConfig) {
        this.styleConfig = styleConfig;
    }

    public StyleConfig.Align getAlign() {
        return align;
    }

    public void setAlign(StyleConfig.Align align) {
        this.align = align;
    }

    public int getLineSpace() {
        return lineSpace;
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
    }

    public int getGray() {
        return gray;
    }

    public void setGray(int gray) {
        this.gray = gray;
    }


}
