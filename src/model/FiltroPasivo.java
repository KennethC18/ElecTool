package model;

import util.Calculador;
import util.ExcepcionCalculo;

public class FiltroPasivo implements Calculador {
    private final TipoFiltroPasivo tipo;
    private final double frecCort;
    private Double r;
    private Double c;

    public FiltroPasivo() {
        this(TipoFiltroPasivo.L_P, 1000.0, 10000.0, null);
    }

    public FiltroPasivo(TipoFiltroPasivo tipo, double frecCort, Double r, Double c) {
        if (frecCort <= 0) throw new IllegalArgumentException("frecCort > 0");
        if (r == null && c == null)
            throw new IllegalArgumentException("Especifique R o C");
        this.tipo = tipo;
        this.frecCort = frecCort;
        this.r = r;
        this.c = c;
    }

    @Override
    public void calcular() throws ExcepcionCalculo {
        if (r != null) {
            c = 1.0 / (2 * Math.PI * r * frecCort);
        } else {
            r = 1.0 / (2 * Math.PI * c * frecCort);
        }
        if (r <= 0 || c <= 0)
            throw new ExcepcionCalculo("Resultado inválido");
    }

    public TipoFiltroPasivo getTipo() {
        return tipo;
    }

    public double getFrecCort() {
        return frecCort;
    }

    public double getR() {
        return r;
    }

    public double getC() {
        return c;
    }

    public void setR(Double r) {
        if (r != null && r <= 0)
            throw new IllegalArgumentException("R debe ser positivo");
        this.r = r;
    }

    public void setC(Double c) {
        if (c != null && c <= 0)
            throw new IllegalArgumentException("C debe ser positivo");
        this.c = c;
    }
}