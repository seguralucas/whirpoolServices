package exit.services.singletons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConstantesGenerales;
import exit.services.principal.peticiones.ConvertidorJson;

public class RecuperadorMapeoCsv {

	private static RecuperadorMapeoCsv instancia=null;

	private String cabecera;
	private String cuerpo;
	
	public static RecuperadorMapeoCsv getInstancia(){
		if(instancia==null)
			instancia= new RecuperadorMapeoCsv();
		return instancia;
	}

	
	public String getCabecera() {
		return cabecera;
	}


	public void setCabecera(String cabecera) {
		this.cabecera = cabecera;
	}


	public String getCuerpo() {
		return cuerpo;
	}


	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}


	private RecuperadorMapeoCsv(){
		File f= new File(ConstantesGenerales.PATH_CONFIGURACION_ENTIDADES+"/"+ApuntadorDeEntidad.getInstance().getEntidadActual()+"/mapeoCSV.csv");
		CSVHandler csv= new CSVHandler();
		try(BufferedReader br= new BufferedReader(new FileReader(f))){
			String line;
			int nroLine=0;
			while((line=br.readLine()) != null){
				if(nroLine==0)
					this.cabecera=line;
				else if(nroLine==1)
					this.cuerpo=line;
				else
					csv.escribirErrorException("El fichero mapeo.csv posee mas de 2 lineas. Se ignorara lineas siguientes");
				nroLine++;
			}
		}
		catch(Exception e){
			csv.escribirErrorException(e.getStackTrace());
		}
	}
	public void reiniciar(){
		this.instancia=null;
	}
}
