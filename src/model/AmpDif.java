package model;

import util.Calculador;
import util.ExcepcionCalculo;

public class AmpDif implements Calculador {
    private final double ganDif;
    private Double r1;
    private Double r2;

    public AmpDif() {
        this(1.0, 10000.0, null);
    }

    public AmpDif(double ganDif, Double r1, Double r2) {
        if (ganDif < 0)
            throw new IllegalArgumentException("ganDif ≥ 0");
        if (r1 == null && r2 == null)
            throw new IllegalArgumentException("Especifique R1 o R2");
        this.ganDif = ganDif;
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public void calcular() throws ExcepcionCalculo {
        if (r1 != null) {
            r2 = ganDif * r1;
        } else {
            r1 = r2 / ganDif;
        }
        if (r1 <= 0 || r2 <= 0)
            throw new ExcepcionCalculo("Valores resultantes inválidos");
    }

    public double getGanDif() {
        return ganDif;
    }

    public double getR1() {
        return r1;
    }

    public double getR2() {
        return r2;
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
}