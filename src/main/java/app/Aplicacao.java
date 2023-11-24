package app;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import model.Denuncia;
import com.google.gson.Gson;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.DenunciaService;
import spark.Route;

import service.UsuarioService;

public class Aplicacao {
	
    private static DenunciaService denunciaService = new DenunciaService();
	private static UsuarioService usuarioService = new UsuarioService();
	
    public static void main(String[] args) {
        port(6793);
        
        Configuration configuration = new Configuration(new Version(2, 3, 31));
        
        configuration.setClassForTemplateLoading(Aplicacao.class, "/templates");
        
        staticFiles.location("/templates");	
        
        post("/produto", (request, response) -> denunciaService.add(request, response));
        	

        // Metodo get pra pegar uma denuncia especifica pelo parametro de pesquisa
        get("/produto/:pesquisa", (request, response) -> denunciaService.get(request, response));
        /*get("/produto/update/:id", (request, response) -> tarefaService.update(request, response));*/

        //get("/produto/delete/:id", (request, response) -> denunciaService.delete(request, response));

        get("/produto", (request, response) -> denunciaService.getAll(request, response));
               

        post("/usuario", (request, response) -> usuarioService.add(request, response));

        get("/usuario", (request, response) -> usuarioService.get(request, response));

               
    }
}