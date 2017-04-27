package exit.services.principal.peticiones.vtex.funciones;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.singletons.RecuperadorEspecificacionesCSV;

public class FuncionesVTEX {
	private static final String SPLITEAR_ORDEN="splitearOrden";
	private static final String PROCESAR_FECHA_VTEX="procesarFechaVTEX";
	private static final String DESENCRIPTAR_EMAIL_VTEX="descriptarEmailVtex";
	
	
	public String[] getArrayTipos(String accesoAlJson){
		String datos[]=accesoAlJson.split("\\.");
		String[] tipoDatos= new String[datos.length];
		for(int i=0;i<datos.length;i++){
			String dato=datos[i];
			String[] partes=dato.split("\\[");
			if(partes.length>1){
			   	tipoDatos[i]="JSONArray";
			}
			else
			   	tipoDatos[i]="JSONObject";
		}
		return tipoDatos;
	}
	
	public boolean isJsonObject(String valor){
		return valor.equalsIgnoreCase("JSONObject");
	}
	
	public Object getObjectJSONObject(Object json, String key){
		return ((JSONObject)json).get(key);
	}
	
	public Object getObjectJSONArray(Object jsonArr, String datos){
		String[] datosPartidos=datos.split("\\[");
		Integer indice=Integer.parseInt(datosPartidos[1].substring(0, datosPartidos[1].length()-1));
		return ((JSONArray)((JSONObject)jsonArr).get(datosPartidos[0])).get(indice);	
	}
	
	public Object getValue(String accesoAlJson, String[] tiposDatos, JSONObject json){
		try{
		String datos[]=accesoAlJson.split("\\.");
		Object posicionActual=json;
		Object resultado;
		for(int i=0;i<tiposDatos.length-1;i++){
			int siguiente=i+1;
			if(isJsonObject(tiposDatos[i]))/*Es jsonObject*/
					posicionActual=getObjectJSONObject(posicionActual,datos[i]);
			else/*Es un array*/
				posicionActual=getObjectJSONArray(posicionActual,datos[i]);
		}
		if(isJsonObject(tiposDatos[tiposDatos.length-1]))
			resultado=getObjectJSONObject(posicionActual,datos[tiposDatos.length-1]);
		else/*Es un array*/
			resultado=getObjectJSONArray(posicionActual,datos[tiposDatos.length-1]);
		return resultado;
		}
		catch(Exception e){
			return null;
		}
		}

	public String procesarPorFunciones(Object o, String cabecera){
		String salida=o.toString();
		JSONArray jsonArr= RecuperadorEspecificacionesCSV.getInstancia().getFuncion(cabecera);	
		if(jsonArr==null)
			return o.toString();
		for(int j=0;j<jsonArr.size();j++){
			JSONObject json= (JSONObject)jsonArr.get(j);
			String name= (String)json.get("name");
				if(name.equalsIgnoreCase(SPLITEAR_ORDEN)){
					FuncionesWhirpool f= new FuncionesWhirpool();
					salida=f.splitearOrden(salida);
				}
				else if(name.equalsIgnoreCase(PROCESAR_FECHA_VTEX)){
					FuncionesWhirpool f= new FuncionesWhirpool();
					salida=f.procesarFechaVTEX(salida);
				}
				else if(name.equalsIgnoreCase(DESENCRIPTAR_EMAIL_VTEX)){
					FuncionesWhirpool f= new FuncionesWhirpool();
					salida=f.descriptarEmailVtex(salida,(JSONObject)json.get("params"));
				}
				
				
		}
		return salida;
	}
	

	
}
