package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import dao.DenunciaDAO;
import model.Denuncia;
import spark.Request;
import spark.Response;
import spark.Session;


public class DenunciaService{

	private DenunciaDAO denunciaDAO;

	private String processarDenuncias(List<Denuncia> denuncias) {
		// Construa um JSON contendo as denúncias
		StringBuilder jsonDenuncias = new StringBuilder("[");
		for (Denuncia denuncia : denuncias) {
			jsonDenuncias.append("{");
			jsonDenuncias.append("\"categoria\":\"").append(denuncia.getCategoria()).append("\",");
			jsonDenuncias.append("\"cidade\":\"").append(denuncia.getCidade()).append("\",");
			jsonDenuncias.append("\"bairro\":\"").append(denuncia.getBairro()).append("\",");
			jsonDenuncias.append("\"estado\":\"").append(denuncia.getEstado()).append("\",");
			jsonDenuncias.append("\"rua\":\"").append(denuncia.getRua()).append("\",");
			jsonDenuncias.append("\"numero\":\"").append(denuncia.getNumero()).append("\",");
			jsonDenuncias.append("\"descricao\":\"").append(denuncia.getDescricao()).append("\"");
			jsonDenuncias.append("},");
		}
		// Remova a vírgula extra se houver denúncias
		if (denuncias.size() > 0) {
			jsonDenuncias.deleteCharAt(jsonDenuncias.length() - 1);
		}
		jsonDenuncias.append("]");
	
		// Retorna o JSON gerado como uma string
		return jsonDenuncias.toString();
	}
	

	public DenunciaService() {
		denunciaDAO = new DenunciaDAO();

	}

	public Object add(Request request, Response response) {
		denunciaDAO.conectar();
		Session session  = request.session(true);                    
		 
		session.attribute("user", 1);
		int id = denunciaDAO.getMaxId() + 1;
		String cidade = request.queryParams("cidade");
		String bairro = request.queryParams("bairro");
		String estado = request.queryParams("estado");
		String rua = request.queryParams("rua");
		String numero = request.queryParams("numero");
		String descricao = request.queryParams("descricao");
		String categoria = request.queryParams("categoria");
		String data = "28/10/2023";
		int idUsuario = session.attribute("user");
	
		Denuncia denuncia = new Denuncia(id, cidade, bairro, estado, rua, numero, descricao, categoria, data, idUsuario);
		denunciaDAO.inserirDenuncia(denuncia);
	    
		response.status(201); // 201 Created
		response.redirect("./index.html");
	
		denunciaDAO.close();
		return id;
	}
	
	 
    public Object get(Request request, Response response) {
        denunciaDAO.conectar();
        int id = Integer.parseInt(request.params(":id"));		
		Denuncia denuncia =  denunciaDAO.get(id);
        return denuncia;
    }
    
    public String getAll(Request request, Response response) {
		denunciaDAO.conectar();
		List<Denuncia> denuncias = denunciaDAO.getAll();
		
		// Faz o processamento necessário para gerar o JSON
		String json = processarDenuncias(denuncias);
		response.header("Access-Control-Allow-Origin", "*");
	
		// Configura o tipo de conteúdo da resposta como JSON
		response.type("application/json");

		
		// Configura o corpo da resposta com o JSON gerado
		response.body(json);
		

	
		return json;
	}

	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Denuncia denuncia = denunciaDAO.get(id);
        String resp = "";       

        if (denuncia != null) {
            denunciaDAO.delete(id);
            response.status(200); // success
            resp = "Produto (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (" + id + ") não encontrado!";
        }
		return resp;
	}
	
}