package model;

public class Usuario {
	private int id;
	private String login;
	private String senha;
	private String nome;
	private int nivel_acesso;
	
	public Usuario(int id, String login, String senha, String nome ) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.nivel_acesso = 0;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public String getSenha() {
		return this.senha;
	}
	
	public int getNivel_acesso() {
		return this.nivel_acesso;
	}

	public String getNome() {
		return this.nome;
	}
	
	public void setId ( int id ) {
		this.id = id;
	}
	
	public void setLogin( String login) {
		this.login = login;
	}
	
	public void setSenha( String senha ) {
		this.senha = senha;
	}

	public void setNome( String nome ) {
		this.nome = nome;
	}
}
