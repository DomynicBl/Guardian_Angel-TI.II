package dao; //Define Pacote

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import java.math.BigInteger;
import java.security.MessageDigest;
import model.Usuario;


public class UsuarioDAO {

	private Connection conexao;
	public UsuarioDAO() { 
		conexao = null;
	}
	
	
	//Conectar ao Banco de Dados
		public boolean conectar() {
			String driverName = "org.postgresql.Driver";                    
			String serverName = "localhost"; 
			String mydatabase = "WAusers";
			int porta = 5432;
			
			String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
			String username = "ti2cc";
			String password = "ti@cc";
			boolean status = false;

			try {
				Class.forName(driverName);
				conexao = DriverManager.getConnection(url, username, password);
				status = (conexao == null);
				System.out.println("Conexão efetuada com o postgres!");
			} catch (ClassNotFoundException e) { 
				System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
			} catch (SQLException e) {
				System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
			}
			return status;
		}
	
		
	//Fecha Conexão com o Banco de dados
	public boolean close() {
		boolean status = false;
			
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	
		
	//ID Usuarios	
	private int maxId = 0; 
		
		public int getmaxId() {
		    Statement st = null;
		    ResultSet rs = null;
		    
		    try {
		        st = conexao.createStatement();
		        String sql = "SELECT max(id) FROM users"; 
		        rs = st.executeQuery(sql);
		        
		        if (rs.next()) {
		            maxId = rs.getInt(1);
		        }
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    } finally {
		        try {
		            if (rs != null) {
		                rs.close();
		            }
		            if (st != null) {
		                st.close();
		            }
		        } catch (SQLException e) {
		            System.err.println(e.getMessage());
		        }
		    }
		    
		    return maxId;
		}

	
	//Criptografia	
		public static String criptografar (String senha) {
			String retorno = "";
			MessageDigest md;
			
			try {
				md = MessageDigest.getInstance("MD5");
				BigInteger hash = new BigInteger(1,md.digest(senha.getBytes()));
				retorno = hash.toString(16);
				
			}catch(Exception e) {}
			
			return retorno;
		}
		
	
	//Cadastro de Usuarios
	public boolean inserirUsuario(Usuario usuario) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			String senhaCriptografada = criptografar(usuario.getSenha()); // Criptografe a senha antes de usá-la
			st.executeUpdate("INSERT INTO users (id, login, senha, nivel_acesso, nome)"
							+ "VALUES (" + usuario.getId() + ", '" + usuario.getLogin() + "', '"
							+ senhaCriptografada + "', '" + usuario.getNivel_acesso() + "', '" + usuario.getNome() + "');");
	        
			st.close();
			status = true;
			this.maxId = (usuario.getId() > this.maxId) ? usuario.getId() : this.maxId;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		
		System.out.println("Usuario Cadastrado com Sucesso!");
		
		return status;
	}
	
	
	//Login de Usuarios
	public Usuario get(String login, String senha) {
	    Usuario usuario = null;
	    
	    try {
	        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        String senhaCriptografada = criptografar(senha); // Criptografe a senha fornecida no login
	        String sql = "SELECT * FROM users WHERE login = '" + login + "' AND senha = '" + senhaCriptografada + "'";
	        ResultSet rs = st.executeQuery(sql);
	        if (rs.next()) {
	            usuario = new Usuario(rs.getInt("id"), rs.getString("login"), rs.getString("senha"), rs.getString("nome"));
	            // JOptionPane.showMessageDialog(null, "Usuario encontrado!");
	            System.out.println("Usuario encontrado!");
	        } else {
	            JOptionPane.showMessageDialog(null, "Usuario não cadastrado");
	            System.out.println("Usuario não cadastrado!");
	        }
	        st.close();
	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	    }
	    return usuario;
	}
	
	
	
	/*get
	public Usuario get(int id) {
		Usuario usuario = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM users WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);
	        if(rs.next()){
	            usuario = new Usuario(rs.getInt("id"), rs.getString("login"), rs.getString("senha"), rs.getString("nome"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuario;
	}

    public List<Usuario> getAll(){
		return getAll("");
	}

    private List<Usuario> getAll(String orderBy) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM WAusers" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Usuario p = new Usuario(rs.getInt("id"), rs.getString("login"), rs.getString("senha"), rs.getString("nome"));
	        	usuarios.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuarios;
	}*/
}