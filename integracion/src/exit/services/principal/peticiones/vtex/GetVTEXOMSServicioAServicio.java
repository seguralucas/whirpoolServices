package exit.services.principal.peticiones.vtex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.convertidor.jsonAJson.ConvertirJsonAJson;
import exit.services.convertidos.csvAJson.JSONHandler;
import exit.services.fileHandler.DirectorioManager;
import exit.services.principal.peticiones.AbstractHTTP;
import exit.services.principal.peticiones.EPeticiones;
import exit.services.principal.peticiones.PostGenerico;
import exit.services.singletons.ApuntadorSubEntidad;
import exit.services.singletons.ConfiguracionEntidadParticular;
import exit.services.singletons.RecEntAct;
import exit.services.util.ConvertidorJson;

public class GetVTEXOMSServicioAServicio  extends AbstractHTTP {

	@Override
	protected Object procesarPeticionOK(BufferedReader in, String id, int responseCode) throws Exception {
		JSONObject jsonObject = ConvertidorJson.convertir(in);
		ConfiguracionEntidadParticular conf=RecEntAct.getInstance().getCep().getSubEntidad("compra");
		ConvertirJsonAJson conv= new ConvertirJsonAJson(conf,jsonObject);

		JSONObject resultadoContacto=conv.convertir();
		PostVTEXGenericoContactoOrdenProducto postGenerico= new PostVTEXGenericoContactoOrdenProducto();
		JSONHandler jsonH= new JSONHandler(conv.getJsonAbstract().getLineaDesdeFormatoJson(),resultadoContacto);
		jsonH.setEntidad(conf);
		jsonH.setCabecera(conv.getJsonAbstract().getCabeceraDesdeFormatoJson());
		JSONHandler result= (JSONHandler)postGenerico.realizarPeticion(EPeticiones.POST, conf.getUrl(),jsonH, RecEntAct.getInstance().getCep().getSubEntidad("contacto").getCabecera(),conf);
		System.out.println("Resultado: "+((JSONObject)result.getJson().get(PostVTEXGenericoContactoOrdenProducto.PROPIEDADES_EXTRA)).get("id"+conf.getEntidadNombre()));
		System.out.println("id"+conf.getEntidadNombre());
		return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, String id, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception {
		JSONArray jsonArrayItems;
		if(RecEntAct.getInstance().getCep().getIterarSobre()!=null){
			JSONObject jsonObject = ConvertidorJson.convertir(in);
			jsonArrayItems= (JSONArray) jsonObject.get(RecEntAct.getInstance().getCep().getIterarSobre());
		}
		else
			jsonArrayItems=ConvertidorJson.convertirArray(in); 
		for(int i=0;i<jsonArrayItems.size();i++){
			GetVTEXOMSServicioAServicio getVTEX= new GetVTEXOMSServicioAServicio();
			JSONObject jsonItem=(JSONObject)jsonArrayItems.get(i);
			String id=(String)jsonItem.get(RecEntAct.getInstance().getCep().getIdIteracion());
			getVTEX.realizarPeticion(EPeticiones.GET,RecEntAct.getInstance().getCep().getUrl(), id,RecEntAct.getInstance().getCep().getCabecera());
		}
		return jsonArrayItems.size(); //Devuelve la cantidad de registros encontradas		return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, int responseCode) throws Exception {
		String path=("error_get_lista_"+responseCode+".txt");
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
         	out.println(inputLine);
        }
        out.close();
		return 0;
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, JSONHandler json, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, JSONHandler json, String id, int responseCode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, JSONHandler json, String id, int responseCode)
			throws Exception {

        return null;
	}

}
