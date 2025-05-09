package model;

import util.Calculador;
import util.ExcepcionCalculo;

public class FiltroActivo1erOrd implements Calculador {
    private final TipoFiltroActivo tipo;
    private final double frecCort;
    private final double gan;
    private Double r1;
    private Double c;
    private Double r2;

    public FiltroActivo1erOrd() {
        this(TipoFiltroActivo.L_P, 1000.0, 2.0, 10000.0, null);
    }

    public FiltroActivo1erOrd(TipoFiltroActivo tipo, double frecCort, double gan, Double r1, Double c) {
        if (frecCort <= 0) throw new IllegalArgumentException("frecCort > 0");
        if (gan < 1.0) throw new IllegalArgumentException("gan ≥ 1");
        if (r1 == null && c == null)
            throw new IllegalArgumentException("Especifique R1 o C");
        this.tipo = tipo;
        this.frecCort = frecCort;
        this.gan = gan;
        this.r1 = r1;
        this.c = c;
    }

    @Override
    public void calcular() throws ExcepcionCalculo {
        if (r1 != null) {
            c = 1.0 / (2 * Math.PI * r1 * frecCort);
        } else {
            r1 = 1.0 / (2 * Math.PI * c * frecCort);
        }
        r2 = (gan - 1) * r1;
        if (r1 <= 0 || c <= 0 || r2 <= 0)
            throw new ExcepcionCalculo("Parámetros inválidos");
    }

    public TipoFiltroActivo getTipo() {
        return tipo;
    }

    public double getFrecCort() {
        return frecCort;
    }

    public double getGan() {
        return gan;
    }

    public double getR1() {
        return r1;
    }

    public double getR2() {
        return r2;
    }

    public double getC() {
        return c;
    }

    public void setR1(Double r1) {
        if (r1 != null && r1 <= 0)
            throw new IllegalArgumentException("R1 debe ser positivo");
        this.r1 = r1;
    }

    public void setR2(Double r2) {
        if (r2 != null && r2 <= 0)
            throw new IllegalArgumentException("R2 debe ser positivo");
        this.r2 = r2;
    }

    public void setC(Double c) {
        if (c != null && c <= 0)
            throw new IllegalArgumentException("C debe ser positivo");
        this.c = c;
    }
}