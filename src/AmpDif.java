public class AmpDif implements Calculador {
    private final double ganDif;
    private Double R1;
    private Double R2;

    public AmpDif() {
        this(1.0, 10000.0, null);
    }
    
    public AmpDif(double ganDif, Double R1, Double R2) {
        if (ganDif < 0)
            throw new IllegalArgumentException("ganDif ≥ 0");
        if (R1 == null && R2 == null)
            throw new IllegalArgumentException("Especifique R1 o R2");
        this.ganDif = ganDif;
        this.R1     = R1;
        this.R2     = R2;
    }

    @Override
    public void calcular() throws ExcepcionCalculo {
        if (R1 != null) {
            R2 = ganDif * R1;
        } else {
            R1 = R2 / ganDif;
        }
        if (R1 <= 0 || R2 <= 0)
            throw new ExcepcionCalculo("Valores resultantes inválidos");
    }

    public double getGanDif() { return ganDif; }
    public double getR1()     { return R1; }
    public double getR2()     { return R2; }
}