package exit.services.procesadorJson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;
import exit.services.singletons.RecuperadorPropierdadesJson;

public class JsonProcesarReemplazo implements IProcesadorJson {
	private Object valorPorReemplazaR;
	private String cabeceraPorReemplazar;
	public  JsonProcesarReemplazo(Object valorPorReemplazaR, String cabeceraPorReemplazar) {
		this.valorPorReemplazaR=valorPorReemplazaR;
		this.cabeceraPorReemplazar=cabeceraPorReemplazar;
	}
	
	@Override
	public void procesarJson(JSONObject json, String keyValue) {
		Object o= json.get(keyValue);
		if(o instanceof String){
			String identificador=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIdentificadorAtributo();
			String valor=(String)o;
			if(RecuperadorPropierdadesJson.getInstancia().getTipo(cabeceraPorReemplazar).equalsIgnoreCase(RecuperadorPropierdadesJson.TIPO_CADENA)){
				if(valor.contains(identificador+cabeceraPorReemplazar+identificador))
					if(valorPorReemplazaR==null)
						json.put(keyValue,null);
					else
						json.put(keyValue, valor.replaceAll(identificador+cabeceraPorReemplazar+identificador, insertarStringNoNull((String)valorPorReemplazaR)));				
			}
			else if(valor.equalsIgnoreCase(identificador+cabeceraPorReemplazar+identificador))
				json.put(keyValue, valorPorReemplazaR);
		}
	}
	
	@Override
	public void procesarJson(JSONArray json, Integer index) {
		Object o= json.get(index);
		if(o instanceof String){
			String identificador=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIdentificadorAtributo();
			String valor=(String)json.get(index);
			if(RecuperadorPropierdadesJson.getInstancia().getTipo(cabeceraPorReemplazar).equalsIgnoreCase(RecuperadorPropierdadesJson.TIPO_CADENA)){
				if(valor.matches(identificador+cabeceraPorReemplazar+identificador)){
					json.remove(index);
					if(valorPorReemplazaR==null)
						json.add(index,null);
					else
					json.add(index, valor.replaceAll(identificador+cabeceraPorReemplazar+identificador, insertarStringNoNull((String)valorPorReemplazaR)));				
				}
			}
			else if(valor.equalsIgnoreCase(identificador+cabeceraPorReemplazar+identificador)){
				json.remove(index);
				json.add(valorPorReemplazaR);	
			}
		}
	}
}
