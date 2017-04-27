package exit.services.singletons;



public class ApuntadorDeEntidad {
	private String[] entidades;
	private int puntero;
	private static ApuntadorDeEntidad instance;
	private ApuntadorDeEntidad(){
		entidades= RecuperadorPropiedadesConfiguracionGenerales.getInstance().getEntidadesAIntegrar().split(",");
		puntero=-1;
	}
    public static synchronized ApuntadorDeEntidad getInstance() {
    	if(instance==null)
    		instance=new ApuntadorDeEntidad();
    	return instance;
    }
    
    public String getEntidadActual(){
    	if(puntero>=entidades.length)
    		return null;
    	return entidades[puntero].trim();
    }
    
    public boolean hasNext(){
    	return (puntero+1)<entidades.length;
    }
    
    public boolean siguienteEntidad(){
    	try{
    	if((puntero+1)>=entidades.length)
    		return false;
    	puntero++;
    	RecuperadorPropiedadedConfiguracionEntidad.getInstance().reiniciar();
    	switch(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getAction().toUpperCase()){
		case RecuperadorPropiedadedConfiguracionEntidad.ACCION_CSVASERVICIO:
			RecuperadorFormato.getInstancia().reiniciar();
	    	RecuperadorPropierdadesJson.getInstancia().reiniciar();
	    	AlmacenadorFechaYHora.reiniciar();
	    	;break;
		case RecuperadorPropiedadedConfiguracionEntidad.ACCION_SERVICIOAACSV:
			RecuperadorMapeoCsv.getInstancia().reiniciar();
			RecuperadorEspecificacionesCSV.getInstancia().reiniciar();
			break;
	}    	
    	return true;
    	
    }
    	catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	}
}
