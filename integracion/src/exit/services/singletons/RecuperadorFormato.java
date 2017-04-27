package exit.services.singletons;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConstantesGenerales;
import exit.services.principal.peticiones.ConvertidorJson;
import exit.services.procesadorJson.IProcesadorJson;

public class RecuperadorFormato {

	private static RecuperadorFormato instancia=null;
	private String formato;
	private JSONObject jsonFormato;
	
	public static RecuperadorFormato getInstancia(){
		if(instancia==null)
			instancia= new RecuperadorFormato();
		return instancia;
	}
	private RecuperadorFormato(){
		File f= new File(ConstantesGenerales.PATH_CONFIGURACION_ENTIDADES+"/"+ApuntadorDeEntidad.getInstance().getEntidadActual()+"/formatoJson.json");
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
	public String getFormato() {
		return formato;
	}
	
	void reiniciar(){
		instancia=null;
	}
	
	public JSONObject getJsonFormato() throws Exception{
		return ConvertidorJson.convertir(this.formato);
	}
	
/*	public static void main(String[] args) {
		ApuntadorDeEntidad.getInstance().siguienteEntidad();
		RecuperadorFormato.getInstancia().printJsonObject(RecuperadorFormato.getInstancia().jsonFormato);
	}*/
	
}
