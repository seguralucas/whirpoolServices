package exit.services.fileHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import exit.services.singletons.AlmacenadorFechaYHora;
import exit.services.singletons.ApuntadorDeEntidad;
import exit.services.singletons.EOutputs;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public class DirectorioManager {
	
	private static final String NOMBRE_TEMP="temp";
	public static void SepararFicheros(File archivo) throws IOException{
    				CSVHandler csv= new CSVHandler();
		    		String line="";
			    		try(BufferedReader br = new BufferedReader(
			    		         new InputStreamReader(
			    		                 new FileInputStream(archivo), "UTF-8"))){
			    		boolean esPrimeraVez=true;
			    		int i=0;
			    		while ((line = br.readLine()) != null) {
			    			if(esPrimeraVez){
			    				esPrimeraVez=false;
			    				CSVHandler.cabeceraFichero=line;			    									    		
			    			}
			    			else{
				    			csv.escribirCSV(DirectorioManager.getDirectorioFechaYHoraInicioDivision(NOMBRE_TEMP+i+".csv"),line,true);
			    				if(i>=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getNivelParalelismo()-1)
			    					i=0;
			    				else
			    					i++;
			    			}
			    		}
		    		}
			        catch(Exception e){
			        	FileWriter fw = new FileWriter(getDirectorioFechaYHoraInicio("errorLote.txt"));
			        	fw.write(e.getMessage());
			        	fw.close();
			        	throw e;
			        }
	}
	

	private static String getEntidadFecha(){
		String outputPath=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getOutPutPath();
		if(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getOutput()!=EOutputs.DIRECTORIO)
			outputPath=ConstantesGenerales.PATH_EJECUCION;
		return outputPath+"/"+ApuntadorDeEntidad.getInstance().getEntidadActual()+"/"+AlmacenadorFechaYHora.getFechaYHoraInicio();
	}
	public static File getDirectorioFechaYHoraInicio(String nombreFichero) throws IOException{
		File file = new File(getEntidadFecha());
		if(!file.exists())
			Files.createDirectories(Paths.get(getEntidadFecha()));
		return new File(getEntidadFecha()+"/"+nombreFichero);
	}
	public static String getPathFechaYHoraInicioDivision() throws IOException{
		File file = new File(getEntidadFecha());
		if(!file.exists())
			Files.createDirectories(Paths.get(getEntidadFecha()));
		file = new File(getEntidadFecha()+"/division");
		if(!file.exists())
			Files.createDirectories(Paths.get(getEntidadFecha()+"/division"));
		return getEntidadFecha()+"/division";
	}
	private static File getDirectorioFechaYHoraInicioDivision(String nombreFichero) throws IOException{
		return new File(getPathFechaYHoraInicioDivision()+"/"+nombreFichero);
	}
	
	/*	public static void SepararFicherosSinSacsRepetidos(File archivo) throws IOException{
	CSVHandler csv= new CSVHandler();
	String line="";
		try(BufferedReader br = new BufferedReader(
		         new InputStreamReader(
		                 new FileInputStream(archivo), "UTF-8"))){
		boolean esPrimeraVez=true;
		int i=0;
		int columnaNroSac=-1;
		ArrayList<String> listaSacs= new ArrayList<String>();
		boolean agregarLinea=false;
		while ((line = br.readLine()) != null) {
			if(esPrimeraVez){
				esPrimeraVez=false;
				CSVHandler.cabeceraFichero=line;
				String[] elementos=line.split(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSVREGEX());
				for(int j=0;j<elementos.length;j++){
					if(elementos[j].equalsIgnoreCase("NRO_SAC"))
						columnaNroSac=j;
				}
			}
			else if(line.trim().length()!=0){
				agregarLinea=false;
				if(columnaNroSac!=-1){
				String nroSacActual=line.split(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSVREGEX())[columnaNroSac];
    				if(listaSacs.contains(nroSacActual)){
    	    			csv.escribirCSV(DirectorioManager.getDirectorioFechaYHoraInicio(CSVHandler.NRO_SAC_REPETIDO_EN_EL_CSV_EJECUTADO),line,true);    				
    				}
    				else{
    					agregarLinea=true;
    					listaSacs.add(nroSacActual);
    				}
				}
				else
					agregarLinea=true;
				if(agregarLinea){
	    			csv.escribirCSV(DirectorioManager.getDirectorioFechaYHoraInicioDivision(NOMBRE_TEMP+i+".csv"),line);
    				if(i>=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getNivelParalelismo()-1)
    					i=0;
    				else
    					i++;
				}
			}
		}
	}
    catch(Exception e){
    	FileWriter fw = new FileWriter(getDirectorioFechaYHoraInicio("errorLote.txt"));
    	fw.write(e.getMessage());
    	fw.close();
    	throw e;
    }
}*/

}
