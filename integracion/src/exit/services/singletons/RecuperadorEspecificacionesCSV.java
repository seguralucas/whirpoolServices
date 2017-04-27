package exit.services.singletons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConstantesGenerales;
import exit.services.principal.peticiones.ConvertidorJson;

public class RecuperadorEspecificacionesCSV {
	private String formato;
	private JSONObject jsonFormato;
	private static RecuperadorEspecificacionesCSV instancia=null;
	
	public static RecuperadorEspecificacionesCSV getInstancia(){
		if(instancia==null)
			instancia= new RecuperadorEspecificacionesCSV();
		return instancia;
	}
	
	private RecuperadorEspecificacionesCSV(){
	File f= new File(ConstantesGenerales.PATH_CONFIGURACION_ENTIDADES+"/"+ApuntadorDeEntidad.getInstance().getEntidadActual()+"/especificacionesCSV.json");
	String line;
	StringBuilder sb= new StringBuilder();
	try(BufferedReader br= new BufferedReader(new FileReader(f))){
		while((line=br.readLine()) != null){
			sb.append(line);
		}
		this.formato=sb.toString();		
		jsonFormato = ConvertidorJson.convertir(this.formato);
	}
	catch(Exception e){
		CSVHandler csv= new CSVHandler();
		csv.escribirErrorException(e.getStackTrace());
	}
	}
	
	public JSONArray getFuncion(String cabecera){
		Object o= jsonFormato.get(cabecera);
		if (o==null)
			return null;
		JSONObject json=(JSONObject)o;
		Object arr= json.get("functions");
		if(arr==null)
			return null;
		return (JSONArray)arr;
		
	}
	
	void reiniciar(){
		instancia=null;
	}
}
