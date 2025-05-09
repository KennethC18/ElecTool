public class FiltroPasivo implements Calculador {
    private final TipoFiltroPasivo tipo;
    private final double frecCort;
    private Double R;
    private Double C;
    
    public FiltroPasivo() {
        this(TipoFiltroPasivo.L_P, 1000.0, 10000.0, null);
    }

    public FiltroPasivo(TipoFiltroPasivo tipo, double frecCort, Double R, Double C) {
        if (frecCort <= 0) throw new IllegalArgumentException("frecCort > 0");
        if (R == null && C == null)
            throw new IllegalArgumentException("Especifique R o C");
        this.tipo     = tipo;
        this.frecCort = frecCort;
        this.R        = R;
        this.C        = C;
    }

    @Override
    public void calcular() throws ExcepcionCalculo {
        if (R != null) {
            C = 1.0 / (2 * Math.PI * R * frecCort);
        } else {
            R = 1.0 / (2 * Math.PI * C * frecCort);
        }
        if (R <= 0 || C <= 0)
            throw new ExcepcionCalculo("Resultado inválido");
    }

    public TipoFiltroPasivo getTipo()    { return tipo; }
    public double            getFrecCort(){ return frecCort; }
    public double            getR()       { return R; }
    public double            getC()       { return C; }
}