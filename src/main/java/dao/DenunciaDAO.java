package dao;


import java.sql.*;

import model.Denuncia;
import java.util.ArrayList;
import java.util.List;

public class DenunciaDAO {
	private Connection conexao;
	private int maxId = 5;

	public int getMaxId() {
		Statement st = null;
	    ResultSet rs = null;
	    
	    try {
	        st = conexao.createStatement();
	        String sql = "SELECT max(id) FROM denuncias"; // Consulta para obter o máximo ID de denúncias
	        rs = st.executeQuery(sql);
	        
	        if (rs.next()) {
	            maxId = rs.getInt(1); // Recupere o valor máximo do resultado
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
	

	public DenunciaDAO() {
		conexao = null;
	}
	
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
	
	public boolean inserirDenuncia(Denuncia denuncia) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO denuncias (id, cidade, bairro, estado, rua, numero, descricao, categoria, \"idUsuario\", data )"
						   + "VALUES (" + denuncia.getId() + ", '" + denuncia.getCidade() + "', '"  
						   + denuncia.getBairro() + "', '" + denuncia.getEstado() + "', '" + denuncia.getRua() + "', '"
						   + denuncia.getNumero() + "', '" + denuncia.getDescricao() + "', '" + denuncia.getCategoria()
						   + "', '" + denuncia.getIdUsuario() + "', '" + denuncia.getData()  + "');");
			st.close();
			status = true;
			this.maxId = (denuncia.getId() > this.maxId) ? denuncia.getId() : this.maxId;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public Denuncia get(int id) {
		Denuncia denuncia = null;
	
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM denuncias WHERE id=" + id;
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				denuncia = new Denuncia(rs.getInt("id"),
						rs.getString("cidade"),
						rs.getString("bairro"),
						rs.getString("estado"),
						rs.getString("rua"),
						rs.getString("numero"),
						rs.getString("descricao"),
						rs.getString("categoria"),
						rs.getString("data"),
						rs.getInt("idUsuario"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return denuncia;
	}
	
	
	public List<Denuncia> getAll(){
		return getAll("");
	}
	
	private List<Denuncia> getAll(String orderBy) {
		List<Denuncia> denuncias = new ArrayList<>();
	
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM denuncias" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Denuncia denuncia = new Denuncia(rs.getInt("id"),
						rs.getString("cidade"),
						rs.getString("bairro"),
						rs.getString("estado"),
						rs.getString("rua"),
						rs.getString("numero"),
						rs.getString("descricao"),
						rs.getString("categoria"),
						rs.getString("data"),
						rs.getInt("idUsuario"));
				denuncias.add(denuncia);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return denuncias;
	}

	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM denuncias WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
}