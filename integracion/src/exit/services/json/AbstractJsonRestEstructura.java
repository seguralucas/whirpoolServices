package exit.services.json;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConstantesGenerales;
import exit.services.procesadorJson.IProcesadorJson;
import exit.services.procesadorJson.JsonProcesarReemplazo;
import exit.services.singletons.ApuntadorDeEntidad;
import exit.services.singletons.RecuperadorFormato;
import exit.services.singletons.RecuperadorPropierdadesJson;

public abstract class AbstractJsonRestEstructura {
	protected String line;
	protected String dataJson;
	protected JSONObject jsonFormato;
	protected HashMap<String, Object> mapCabeceraValor;

	abstract public void agregarCampo(String cabecera, String valor);
	abstract public boolean validarCampos();
	abstract public JSONHandler createJson() throws Exception;
	abstract protected Object alterarValor(String cabecera, String valor);
	

	
	public AbstractJsonRestEstructura() throws Exception {
		super();
		this.dataJson=RecuperadorFormato.getInstancia().getFormato();
		this.jsonFormato=RecuperadorFormato.getInstancia().getJsonFormato();
		mapCabeceraValor= new HashMap<String, Object>();
	}
	
	public HashMap<String, Object> getMapCabeceraValor(){
		return mapCabeceraValor;
	}
	
	protected void insertarValorMap(String cabecera, String valor){
		getMapCabeceraValor().put(cabecera, alterarValor(cabecera,valor));
	} 
	
	protected void insertarValorJson(String cabecera, String valor){ 
		Object valorAlterado=alterarValor(cabecera,valor);
		//this.dataJson=this.dataJson.replaceAll("#"+cabecera+"#", alterarValor(cabecera,valor));
		if(valorAlterado==null && RecuperadorPropierdadesJson.getInstancia().isBorrarSiEsNull(cabecera))
			borrarKey(cabecera);
		else{
		JsonProcesarReemplazo jpr= new JsonProcesarReemplazo(valorAlterado,cabecera);
		recorrerJSON(this.jsonFormato,jpr);
		}
	}
	
	protected void recorrerJSON(JSONObject jsonObj, IProcesadorJson procesadorJson ) {
	    for (Object key : jsonObj.keySet()) {
	        String keyStr = (String)key;
	        Object keyvalue = jsonObj.get(keyStr);
	        if (keyvalue instanceof JSONObject)
	        	recorrerJSON((JSONObject)keyvalue,procesadorJson);
	        else if(keyvalue instanceof JSONArray)
	        	recorrerJSON((JSONArray)keyvalue,procesadorJson);
	        else
	        	procesadorJson.procesarJson(jsonObj, keyStr);
	        }
	}
	
	protected void recorrerJSON(JSONArray jsonArr, IProcesadorJson procesadorJson ){
		for(int i=0;i<jsonArr.size();i++){
			Object value=jsonArr.get(i);
	        if (value instanceof JSONObject)
	        	recorrerJSON((JSONObject)value,procesadorJson);
	        else if(value instanceof JSONArray)
	        	recorrerJSON((JSONArray)value,procesadorJson);
		    else
	        	procesadorJson.procesarJson(jsonArr, i);
		}
	}
	
	protected Boolean insertarTrueOFalse(String valor){
		if(valor == null)
			return null;
		if (valor.equalsIgnoreCase("si") || valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("verdadero"))
			return true;
		else if(valor.equalsIgnoreCase("no") || valor.equalsIgnoreCase("false") || valor.equalsIgnoreCase("falso"))
			return false;
		else 
			return null;
	}
	

	protected String insertarFecha(String valor){
		final String PATH_ERROR="error_formato_fecha.csv";
		if(valor==null || valor.length()==0)
			return null;
		CSVHandler csv= new CSVHandler();
		String[] fecha=valor.split("/");
		if(fecha.length==3){
			try{
				return fecha[2]+"-"+fecha[0]+"-"+fecha[1];
			}
			catch(Exception e){
				try {
					csv.escribirCSV(PATH_ERROR, this.getLine());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}
		}
		fecha=valor.split("-");
		if(fecha.length!=3)
			return null;
		else
			return valor;
	}
	

	
	protected void borrarKey(String cabecera){
		try{
		if(RecuperadorPropierdadesJson.getInstancia().isBorrarSiEsNull(cabecera)){
			String[] recorrido=RecuperadorPropierdadesJson.getInstancia().getBorrarSiEsNull(cabecera).split("\\.");
			JSONObject aux= this.jsonFormato;
			JSONArray auxArray;
			for(int i=0;i<recorrido.length;i++){
				if(i+1==recorrido.length){
					if(recorrido[i].matches(".*\\[[0-9]\\]$")){
						auxArray=(JSONArray)aux.get(recorrido[i].substring(0, recorrido[i].length()-3));
						auxArray.remove(Character.getNumericValue(recorrido[i].charAt(recorrido[i].length() - 2)));
					}
					else
						aux.remove(recorrido[i]);
				}
				else{
					aux=(JSONObject)aux.get(recorrido[i]);
					if(recorrido[i].matches(".*\\[[0-9]\\]$")){
						auxArray=(JSONArray)aux.get(recorrido[i].substring(0, recorrido[i].length()-3));
						aux.get(Character.getNumericValue(recorrido[i].charAt(recorrido[i].length() - 2)));
					}
				}
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
			CSVHandler csv= new CSVHandler();
			try{
				csv.escribirCSV("error_borrar_key.txt", "Error al intentar eliminar llave por valor nulo");
				csv.escribirCSV("error_borrar_key.txt", "Elemento causante: "+cabecera);
				csv.escribirCSV("error_borrar_key.txt", "Valor: "+RecuperadorPropierdadesJson.getInstancia().getBorrarSiEsNull(cabecera));
				csv.escribirCSV("error_borrar_key.txt", "Operacion sobre el registro no detenida");
				csv.escribirCSV("error_borrar_key.txt", ConstantesGenerales.SEPARADOR_ERROR_TRYCATCH);
			}
			catch(Exception a){
				a.printStackTrace();
			}
		}
	}
	
	protected String insertarString(String valor){
		if(valor == null || valor.length()==0)
			return null;
		return valor;
	}
	
	protected String procesarFecha(String cabecera, String valor){
		return insertarFecha(valor);
	}
	
	public String remplazarTildes(String cabecera,String valor) {
		if(!RecuperadorPropierdadesJson.getInstancia().isReemplazarCarEspanol(cabecera))
			return valor;
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = valor;
	    for (int i=0; i<original.length(); i++) {
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }
	    return output;
	    
	}
	
	
	public String agregar10Ceros(String cabecera,String valor) {
		if(!RecuperadorPropierdadesJson.getInstancia().isCompletar10Ceros(cabecera))
			return valor;
		for(int i=valor.length();i<10;i++)
			valor="0"+valor;
		return valor;
	}

	protected String procesarCadena(String cabecera, String valor){
		valor=agregar10Ceros(cabecera,valor);
		valor=borrarCaracteresNoNumericos(cabecera,valor);
		valor=remplazarTildes(cabecera,valor);
		if(valor==null || valor.length()==0)
			return null;
		return valor;
	}
	
	protected String borrarCaracteresNoNumericos(String cabecera, String valor){
		if(RecuperadorPropierdadesJson.getInstancia().isBorrarCarNoNumericos(cabecera))
			valor=valor.replaceAll("[^0-9]", "");
		return valor;		
	}
	
	protected Integer procesarEntero(String cabecera, String valor){
		valor=borrarCaracteresNoNumericos(cabecera,valor);
		if(valor==null || valor.length()==0)
			return null;
		return Integer.parseInt(valor);
	}
	
	
	public String getLine(){
		return line;
	}
	public void setLine(String line){
		this.line=line;
	}
	
	public String getDataJson(){
		return dataJson;
	}
	public JSONObject getJsonFormato() {
		return jsonFormato;
	}

	public static void main(String[] args) {
		ApuntadorDeEntidad.getInstance().siguienteEntidad();
		Class c= String.class;
		Object o="ttt";
		String a=(String) c.cast(o);
		System.out.println(a);
		System.out.println(RecuperadorPropierdadesJson.getInstancia().isValoresPermitidosCaseSensitive("BENEFICIO_CATEGORIA"));
		System.out.println(RecuperadorPropierdadesJson.getInstancia().isValoresPermitidosCaseSensitive("BENEFICIO_ESTADO"));
	}
	
	
}
