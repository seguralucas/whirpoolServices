package exit.services.principal.peticiones.vtex;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.fileHandler.CSVHandler;
import exit.services.principal.peticiones.AbstractHTTP;
import exit.services.principal.peticiones.EPeticiones;
import exit.services.singletons.RecEntAct;

public class FuncionesVTEX {
	private static final String SPLITEAR_ORDEN="splitearOrden";
	private static final String PROCESAR_FECHA_VTEX="procesarFechaVTEX";
	private static final String DESENCRIPTAR_EMAIL_VTEX="descriptarEmailVtex";
	
	

	


	


	public String procesarPorFunciones(Object o, String cabecera){
		String salida=o.toString();
		JSONArray jsonArr= RecEntAct.getInstance().getCep().getRecuperadorEspecificacionesCSV().getFuncion(cabecera);	
		if(jsonArr==null)
			return o.toString();
		for(int j=0;j<jsonArr.size();j++){
			JSONObject json= (JSONObject)jsonArr.get(j);
			String name= (String)json.get("name");
				if(name.equalsIgnoreCase(SPLITEAR_ORDEN)){
					salida=this.splitearOrden(salida);
				}
				else if(name.equalsIgnoreCase(PROCESAR_FECHA_VTEX)){
					salida=this.procesarFechaVTEX(salida);
				}
				else if(name.equalsIgnoreCase(DESENCRIPTAR_EMAIL_VTEX)){
					salida=this.descriptarEmailVtex(salida,(JSONObject)json.get("params"));
				}
			}
		return salida;
	}
	
	public String splitearOrden(String cadena) {
		char[] chars=cadena.toCharArray();
		String salida="";
		boolean bandera=false;
		for(int i=0;i<chars.length;i++){
			if(chars[i] >='0' && chars[i]<='9'){
				bandera=true;
				salida+=chars[i];
			}
			else if(bandera)
				return salida;
		}
		return salida;
	}
	
	public String procesarFechaVTEX(String  dateOMS){
		/*Busca transformar la fecha del formato 2017-04-28T10:25:22.0000000+00:00
		al formato 2017-04-28T10 10:25:22*/
		String[] aux=dateOMS.split("T");
		String fecha=aux[0];
		String[] time=aux[1].split(":");
		String seconds=time[2].split("\\.")[0];
		return fecha+" "+time[0]+":"+time[1]+":"+seconds;
	}
	
	/*public Object ejecutorServicioACsvVTEXOMS(String cantidadDias) throws UnsupportedEncodingException{
		AbstractHTTP getVTEXGenerico= new GetVTEXOMS();
		String identificadorAtr=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIdentificadorAtributo();
		String cabeceraUrl=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getFiltros().replaceAll(identificadorAtr+"NRO_PAG"+identificadorAtr, String.valueOf(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPaginaActual()));
		Integer cantDias=Integer.parseInt(cantidadDias);
		String parametroDate=getParametroDateOMG(cantDias);
		String parametrosFianl;
		if(cantDias==-1)
			parametrosFianl=cabeceraUrl;
		else
			parametrosFianl=cabeceraUrl+"&"+parametroDate;
	
		System.out.println(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl()+"?"+parametrosFianl);		
		return getVTEXGenerico.realizarPeticion(EPeticiones.GET,RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl()+"?"+parametrosFianl);
	}*/
	
	public String descriptarEmailVtex(String emailEncriptado, JSONObject params){
		try{
		String instancia=(String)params.get("instancia");
		String url=(String)params.get("url");
		AbstractHTTP getEmailDescriptado= new GetVTEXEmailDesencriptado();
		Object result=getEmailDescriptado.realizarPeticion(EPeticiones.GET,url+"?alias="+emailEncriptado+"&an="+instancia,RecEntAct.getInstance().getCep().getCabecera());
		if(result==null)
			throw new Exception();
		return (String)result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Se guarda el valor del email encriptado");
			CSVHandler csv= new CSVHandler();
			try {
				csv.escribirCSV(CSVHandler.PATH_LOG_GENERICO, "No se pudo descriptar el email: "+ emailEncriptado+". Se proceder� a guardarlo encriptado");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return emailEncriptado;
		}
	}
	
	

	
}