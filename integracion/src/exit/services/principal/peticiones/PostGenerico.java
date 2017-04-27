package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.DirectorioManager;
import exit.services.fileHandler.ConstantesGenerales;
import exit.services.json.JSONHandler;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public class PostGenerico extends AbstractHTTP{

	@Override
	protected Object procesarPeticionOK(BufferedReader in, JSONHandler json, int responseCode) throws Exception {
		CSVHandler csv= new CSVHandler();
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(CSVHandler.PATH_INSERTADOS_OK);
        String inputLine;
        boolean marca = true; //Recuperamos el ID
        String id=null;
        while ((inputLine = in.readLine()) != null) {
        	//out.println(inputLine);
        	if(marca && inputLine.contains("id")){
        		id=inputLine.replaceAll("\"id\": ", "").replaceAll(",", "");
        		marca=false;
        	}
        }
        csv.escribirCSV(fichero, id+RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSV()+json.getLine(), "ID"+RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSV()+CSVHandler.cabeceraFichero,true);        
        return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception{
		String path=("error_insercion_servidor_codigo_"+responseCode+".txt");
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        out.println(json.toString());
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
         	out.println(inputLine);
        }
        out.println(ConstantesGenerales.SEPARADOR_ERROR_JSON);
        CSVHandler csvHandler = new CSVHandler();
        csvHandler.escribirCSV("error_insercion_servidor_codigo_"+responseCode+".csv",json);            
        out.println(ConstantesGenerales.SEPARADOR_ERROR_PETICION);
        out.close();
        return null;
	 }

	@Override
	protected Object procesarPeticionOK(BufferedReader in, String id, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, String id, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object procesarPeticionError(BufferedReader in, int responseCode) throws Exception {
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
		// TODO Auto-generated method stub
		return null;
	}
}
