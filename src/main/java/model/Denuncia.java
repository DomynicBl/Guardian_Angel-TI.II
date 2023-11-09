package model;

public class Denuncia {
    private int id;
    private String categoria;
    private String cidade;
    private String bairro;
    private String estado;
    private String rua;
    private String numero;
    private String descricao;
    private String data;
    private int idUsuario;

    public Denuncia(int id, String cidade, String bairro, String estado, String rua, String numero, String descricao, String categoria, String data, int idUsuario) {
        this.id = id;
        this.cidade = cidade;
        this.bairro = bairro;
        this.estado = estado;
        this.rua = rua;
        this.numero = numero;
        this.descricao = descricao;
        this.categoria = categoria;
        this.data = data;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return this.id;
    }

    public String getCidade() {
        return this.cidade;
    }

    public String getBairro() {
        return this.bairro;
    }

    public String getEstado() {
        return this.estado;
    }

    public String getRua() {
        return this.rua;
    }

    public String getNumero() {
        return this.numero;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public String getData() {
        return this.data;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }
}
