public class VooAtrasado {
    public String voo, modelo, origem, destino, status, atraso;

    public VooAtrasado(String voo, String modelo, String origem, String destino, String status, String atraso) {
        this.voo = voo;
        this.modelo = traduzirModelo(modelo);
        this.origem = origem;
        this.destino = destino;
        this.status = status;
        this.atraso = atraso;
    }

    private String traduzirModelo(String m) {
        if (m == null || m.equals("N/D")) return "Aeronave";
        String mod = m.toUpperCase();
        
        if (mod.contains("A35")) return "Airbus A350";
        if (mod.contains("A33") || mod.contains("A333") || mod.contains("A339")) return "Airbus A330";
        if (mod.contains("B77")) return "Boeing 777";
        if (mod.contains("B74") || mod.contains("748")) return "Boeing 747-8";
        if (mod.contains("B38") || mod.contains("MAX")) return "Boeing 737 MAX"; // CORREÇÃO AQUI
        if (mod.contains("B73") || mod.contains("737")) return "Boeing 737";
        if (mod.contains("A32") || mod.contains("320")) return "Airbus A320";
        
        return m;
    }

    public int getPax() {
        if (modelo.contains("747")) return 467;
        if (modelo.contains("777")) return 410;
        if (modelo.contains("350")) return 350;
        if (modelo.contains("330")) return 298;
        return 174;
    }
}