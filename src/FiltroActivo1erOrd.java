public class FiltroActivo1erOrd implements Calculador {
    private final TipoFiltroActivo tipo;
    private final double           frecCort;
    private final double           gan;
    private Double R1;
    private Double C;
    private Double R2;
    
    public FiltroActivo1erOrd() {
    	// LP @1kHz, gan=2, R1=10kΩ por defecto
    	this(TipoFiltroActivo.L_P, 1000.0, 2.0, 10000.0, null);
    }
    
    public FiltroActivo1erOrd(TipoFiltroActivo tipo, double frecCort, double gan, Double R1, Double C) {
        if (frecCort <= 0) throw new IllegalArgumentException("frecCort > 0");
        if (gan < 1.0)     throw new IllegalArgumentException("gan ≥ 1");
        if (R1 == null && C == null)
            throw new IllegalArgumentException("Especifique R1 o C");
        this.tipo     = tipo;
        this.frecCort = frecCort;
        this.gan      = gan;
        this.R1       = R1;
        this.C        = C;
    }

    @Override
    public void calcular() throws ExcepcionCalculo {
        // frecCort = 1/(2π·R1·C)   y   gan = 1 + R2/R1 ⇒ R2 = (gan–1)·R1
        if (R1 != null) {
            C = 1.0 / (2 * Math.PI * R1 * frecCort);
        } else {
            R1 = 1.0 / (2 * Math.PI * C * frecCort);
        }
        R2 = (gan - 1) * R1;
        if (R1 <= 0 || C <= 0 || R2 <= 0)
            throw new ExcepcionCalculo("Parámetros inválidos");
    }

    public TipoFiltroActivo getTipo()    { return tipo; }
    public double           getFrecCort(){ return frecCort; }
    public double           getGan()     { return gan; }
    public double           getR1()      { return R1; }
    public double           getR2()      { return R2; }
    public double           getC()       { return C; }
}
