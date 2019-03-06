package com.cofrem.transacciones.lib;

/**
 * Created by luispineda on 13/07/17.
 */

public class StyleConfig {
    public FontFamily fontFamily;
    public FontSize fontSize;
    public FontStyle fontStyle;
    public Align align;
    public int gray;
    public int lineSpace;
    public boolean newLine;

    public StyleConfig() {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = StyleConfig.FontSize.F3;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.align = Align.LEFT;
        this.gray = 11;
        this.lineSpace = 1;
        this.newLine = true;
    }

    public StyleConfig(StyleConfig.Align align, int gray, StyleConfig.FontSize fontSize) {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = fontSize;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.gray = gray;
        this.lineSpace = 1;
        this.align = align;
        this.newLine = true;
    }

    public StyleConfig(StyleConfig.Align align, int gray, StyleConfig.FontSize fontSize, int lineSpace) {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = fontSize;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.gray = gray;
        this.lineSpace = lineSpace;
        this.align = align;
        this.newLine = true;
    }

    public StyleConfig(StyleConfig.Align align, int gray) {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = StyleConfig.FontSize.F3;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.gray = gray;
        this.lineSpace = 1;
        this.align = align;
        this.newLine = true;
    }

    public StyleConfig(StyleConfig.Align align, int gray, int lineSpace) {
        this.fontFamily = StyleConfig.FontFamily.DEFAULT;
        this.fontSize = StyleConfig.FontSize.F3;
        this.fontStyle = StyleConfig.FontStyle.NORMAL;
        this.gray = gray;
        this.lineSpace = lineSpace;
        this.align = align;
        this.newLine = true;
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

