package exit.services.convertidos.csvAJson;

import org.json.simple.JSONObject;

import exit.services.principal.peticiones.EPeticiones;
import exit.services.principal.peticiones.vtex.FuncionesVTEX;
import exit.services.principal.peticiones.vtex.GetVTEXEmailDesencriptado;
import exit.services.singletons.ConfiguracionEntidadParticular;

public class JsonVTEX extends JsonGenerico{
	private static final String PROPIEDAD_DESCRIPTAR_MAIL_VTEX="desencriptarMailVtex";
	public JsonVTEX(ConfiguracionEntidadParticular confEntidadPart) throws Exception {
		super(confEntidadPart);
	}

	public String agregar10Ceros(String cabecera,String valor) {
		if(!confEntidadPart.getRecuperadorPropiedadesJson().isCompletar10Ceros(cabecera))
			return valor;
		for(int i=valor.length();i<10;i++)
			valor="0"+valor;
		return valor;
	}
	
	@Override
	protected String procesarCadena(String cabecera, String valor) {
		String resultado= super.procesarCadena(cabecera, valor);
		JSONObject j= confEntidadPart.getRecuperadorPropiedadesJson().getPropiedades(cabecera);
		Object aux=j==null?null:j.get(PROPIEDAD_DESCRIPTAR_MAIL_VTEX);
		if(aux!=null){
			FuncionesVTEX fvtex= new FuncionesVTEX();
			resultado=(fvtex.descriptarEmailVtex(valor,(JSONObject)aux));
		}			
		return resultado;
	}
}