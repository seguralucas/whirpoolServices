package exit.services.convertidos.csvAJson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import exit.services.singletons.ConfiguracionEntidadParticular;

public class JSONHandler2{
//	private JSONObject json;
//	private String line;
//	private String cabecera;
//	private ConfiguracionEntidadParticular entidad;
//	
//	public JSONHandler(){}
//	public JSONHandler(String line, String data) throws ParseException{
//		setLine(line);
//		crearteJson(data);
//	}
//	
//	public JSONHandler(String line, JSONObject json) throws ParseException{
//		setLine(line);
//		this.json=json;
//	}
//	
//	public JSONHandler(JSONObject json) throws ParseException{
//		this.json=json;
//	}
//	
//	public String getLine() {
//		return line;
//	}
//
//	public void setLine(String line) {
//		this.line = line;
//	}	
//	
//	
//	
//	public String getCabecera() {
//		return cabecera;
//	}
//	public void setCabecera(String cabecera) {
//		this.cabecera = cabecera;
//	}
//	/**
//	 * Método Exclusivo para debuguear
//	 * @param data
//	 * @throws ParseException
//	 */
//	public void crearteJson(String data) throws ParseException{
//		JSONParser parser = new JSONParser();
//		this.json= (JSONObject)parser.parse(data);
//	}
//	
//	public JSONObject getJson() {
//		return json;
//	}
//
//	public void setJson(JSONObject json) {
//		this.json = json;
//	}
//	
//	
//
//	public ConfiguracionEntidadParticular getEntidad() {
//		return entidad;
//	}
//	public void setEntidad(ConfiguracionEntidadParticular entidad) {
//		this.entidad = entidad;
//	}
//	@Override
//	public String toString() {
//		return json.toString().replace("\\", "").replace(",", ",\n");
//	}
//	
//	public String toStringSinEnter(){
//		return json.toString().replace("\\", "");
//	}
//	
//	public String toStringNormal(){
//		return json.toString();
//	}
//}
