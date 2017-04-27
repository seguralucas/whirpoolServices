package exit.services.singletons.entidadesARecuperar;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import exit.services.fileHandler.ConstantesGenerales;
import exit.services.fileHandler.DirectorioManager;
import exit.services.singletons.RecuperadorPropiedadesConfiguracionGenerales;


public class RecuperadorPeticiones {
	HashMap<String, Peticion> mapPeticiones;
	private static RecuperadorPeticiones instance;
	private Peticion get;
	private Peticion post;
	private Peticion update;
	private Peticion delete;
    private RecuperadorPeticiones(){
        Properties props = new Properties();
        try{
    		props.load(new FileReader(ConstantesGenerales.PATH_PETICIONES_GET));
    		get=new Peticion(props.getProperty("metodo"), Integer.parseInt(props.getProperty("codigoResponseEsperado")), props.getProperty("cabecera"));
    		props.load(new FileReader(ConstantesGenerales.PATH_PETICIONES_POST));
    		post=new Peticion(props.getProperty("metodo"), Integer.parseInt(props.getProperty("codigoResponseEsperado")), props.getProperty("cabecera"));
    		props.load(new FileReader(ConstantesGenerales.PATH_PETICIONES_UPDATE));
    		update=new Peticion(props.getProperty("metodo"), Integer.parseInt(props.getProperty("codigoResponseEsperado")), props.getProperty("cabecera"));
    		props.load(new FileReader(ConstantesGenerales.PATH_PETICIONES_DELETE));
    		delete=new Peticion(props.getProperty("metodo"), Integer.parseInt(props.getProperty("codigoResponseEsperado")), props.getProperty("cabecera"));
        }
        catch(Exception e){
        	e.printStackTrace();
        	try(FileWriter fw= new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("error.txt"))){
				fw.write("Error al recuperar las propierdades del fichero: ConfiguracionGeneral.properties");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }
    
    void reiniciar(){
    	instance=null;
    }

	public synchronized static RecuperadorPeticiones getInstance() {
    	if(instance==null)
    		instance=new RecuperadorPeticiones();
    	return instance;	}


	public Peticion getGet() {
		return get;
	}

	public Peticion getPost() {
		return post;
	}

	public Peticion getUpdate() {
		return update;
	}

	public Peticion getDelete() {
		return delete;
	}

    
    
    
}
