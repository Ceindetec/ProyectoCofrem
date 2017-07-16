package com.cofrem.transacciones.lib;

/**
 * Created by luispineda on 13/07/17.
 */

public class StyleConfig{
    public FontFamily fontFamily;
    public FontSize fontSize;
    public FontStyle fontStyle;
    public Align align;
    public int gray;
    public int lineSpace;
    public boolean newLine;

    public StyleConfig() {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = StyleConfig.FontSize.F2;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.align = Align.RIGHT;
        this.gray = 11;
        this.lineSpace = 1;
        this.newLine = true;
    }

    public StyleConfig(StyleConfig.Align align, boolean newLine) {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = StyleConfig.FontSize.F2;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.gray = 11;
        this.lineSpace = 1;
        this.align = align;
        this.newLine = newLine;
    }

    public StyleConfig(StyleConfig.FontSize fontSize, StyleConfig.FontStyle fontStyle, StyleConfig.Align align, int gray, boolean newLine) {
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.align = align;
        this.gray = gray;
        this.newLine = newLine;
    }



    public static enum Align {
        LEFT,
        CENTER,
        RIGHT;

        private Align() {
        }
    }

    public static enum FontFamily {
        DEFAULT;

        private FontFamily() {
        }
    }

    public static enum FontSize {
        F1,
        F2,
        F3,
        F4;

        private FontSize() {
        }
    }

    public static enum FontStyle {
        NORMAL,
        BOLD;

        private FontStyle() {
        }
    }
}

