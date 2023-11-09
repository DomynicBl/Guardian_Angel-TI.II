package service;

import spark.Session;

import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService{

	private UsuarioDAO usuarioDAO;

	public UsuarioService() {
		usuarioDAO = new UsuarioDAO();
	}

	public Object add(Request request, Response response) {
		usuarioDAO.conectar();
		int id = usuarioDAO.getmaxId() +1;
		String login = request.queryParams("login");
		String senha = request.queryParams("senha");
		String nome = request.queryParams("nome");
		
		Usuario usuario = new Usuario(id, login, senha, nome);
		usuarioDAO.inserirUsuario(usuario);

		response.status(201); // 201 Created
		
		response.redirect("./login.html"); // redireciona para index
		usuarioDAO.close();
		return id;
	}
	
	 public Object get(Request request, Response response) {
	        usuarioDAO.conectar();
	        
	        Object resposta = null;
	        String pagina = "./login.html";
	        
			try {String login = request.queryParams("login");
			String senha = request.queryParams("senha");		
			Usuario usuario =  usuarioDAO.get(login,senha);
			        
			Session session = request.session(true);
			session.attribute("user",usuario.getId());
			
			resposta = usuario.getLogin();
	        pagina = "./index.html";
			
			response.redirect(pagina);
	 		} catch(Exception a) {
	 			response.redirect(pagina);
	 		}
			
			return resposta;
	 }
}