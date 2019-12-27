package urjc.isi.controladores;

import static spark.Spark.*;

import java.sql.SQLException;
import java.util.List;

import spark.Request;
import spark.Response;

import urjc.isi.entidades.Peliculas;
import urjc.isi.service.PeliculasService;

public class PeliculasController {

	private static PeliculasService ps;
	private static String adminkey = "1234";
	
	/**
	 * Constructor por defecto
	 */
	public PeliculasController() {
		ps = new PeliculasService();
	}
	
	/**
	 * Maneja las peticiones que llegan al endpoint /peliculas/uploadTable
	 * @param request
	 * @param response
	 * @return El formulario para subir el fichero con las pseudoqueries o una redireccion al endpoint /welcome
	 */
	public static String uploadTable(Request request, Response response) {
		if(!adminkey.equals(request.queryParams("key"))) {
			response.redirect("/welcome"); //Se necesita pasar un parametro (key) para poder subir la tabla
		}
		return "<form action='/peliculas/upload' method='post' enctype='multipart/form-data'>" 
			    + "    <input type='file' name='uploaded_films_file' accept='.txt'>"
			    + "    <button>Upload file</button>" + "</form>";
	}
	
	/**
	 * Metodo que se encarga de manejar las peticiones a /peliculas/upload
	 * @param request
	 * @param response
	 * @return Mensaje de estado sobre la subida de los registros
	 */
	public static String upload(Request request, Response response) {
		return ps.uploadTable(request);
	}
	
	/**
	 * Metodo encargado de manejar las peticiones a /peliculas/selectAll
	 * @param request
	 * @param response
	 * @return Listado de peliculas que estan en la tabla Peliculas de la base de datos
	 * @throws SQLException
	 */
	public static String selectAllPeliculas(Request request, Response response) throws SQLException {
		List<Peliculas> output;
		String result = "";
		if(request.queryParams("actor")!= null) {
			output = ps.getAllPeliculasByActor(request.queryParams("actor"));
		}else {
			output = ps.getAllPeliculas();
		}
		for(int i = 0; i < output.size(); i++) {
		    result = result + output.get(i).toHTMLString() +"</br>";
		}
		return result;
	}
	
	/**
	 * Metodo que se encarga de manejar todos los endpoints que cuelgan de /peliculasactores
	 */
	public void peliculasHandler() {
		//get("/crearTabla", AdminController::crearTablaPeliculas);
		get("/selectAll", PeliculasController::selectAllPeliculas);
		get("/uploadTable", PeliculasController::uploadTable);
		post("/upload", PeliculasController::upload);
	}
	
	/**
	 * Metodo manejador del endpoint /peliculas/crearTabla 
	 * @param request
	 * @param response
	 * @return 
	 */
	/*
	public static JsonObject crearTablaPeliculas(Request request, Response response) throws SQLException{
		request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
		String output = as.crearTablaPeliculas();
		response.type("application/json");
		JsonObject json = new JsonObject();
		json.addProperty("status", "SUCCESS");
		json.addProperty("serviceMessage", "La peticion se manejó adecuadamente");
		json.addProperty("output", output);
		return json;
	}*/
	/*public static JsonObject selectAllPeliculas(Request request, Response response){

		request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
		response.type("application/json");
		JsonObject json = new JsonObject();
		try {
			List<Peliculas> output = as.getAllPeliculas();
			output.add(new Peliculas());
			json.addProperty("status", "SUCCESS");
			json.addProperty("serviceMessage", "La peticion se manejo adecuadamente");
			JsonArray array = new JsonArray();
			for(int i = 0; i < output.size(); i++) {
				array.add(output.get(i).toString());;
			}
			json.add("output", array);
		}catch(SQLException e) {
			json.addProperty("status", "ERROR");
			json.addProperty("serviceMessage", "Ocurrio un error accediendo a la base de datos");
		}
		return json;
	}*/
}