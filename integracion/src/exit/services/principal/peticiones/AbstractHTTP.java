package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.singletons.ApuntadorDeEntidad;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;
import exit.services.singletons.entidadesARecuperar.RecuperadorPeticiones;

public abstract class AbstractHTTP {
	public Object realizarPeticion(EPeticiones httpMetodo){
		return realizarPeticion(httpMetodo,RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),null,null);
	}
	
	public Object realizarPeticion(EPeticiones httpMetodo, JSONHandler json){
		return realizarPeticion(httpMetodo,RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),null,json);
	}

	public Object realizarPeticion(EPeticiones httpMetodo,String url){
		return realizarPeticion(httpMetodo,url,null,null);
	}
	

	
	public Object realizarPeticion(EPeticiones httpMetodo, String url, JSONHandler json){
		 return realizarPeticion(httpMetodo,url,null,json);
	 }
	 
	public Object realizarPeticion(EPeticiones httpMetodo, String url,String id){
		 return realizarPeticion(httpMetodo,url,id,null);
	 }
	
	public Object realizarPeticion(EPeticiones httpMetodo, String url,String id,JSONHandler json){
	        try{
	        	WSConector ws;
	        	if(id!=null)
	        		 ws = new WSConector(httpMetodo,url+"/"+id);
	        	else
	        		 ws = new WSConector(httpMetodo,url);
	        	HttpURLConnection conn=ws.getConexion();
	        	if(json!=null){
		        	DataOutputStream wr = new DataOutputStream(
		        			conn.getOutputStream());
		        	wr.write(json.toStringNormal().getBytes("UTF-8"));
		        	wr.flush();
		        	wr.close();
	        	}
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            Object o;
	            if(responseCode == RecuperadorPeticiones.getInstance().getGet().getCodigoResponseEsperado()){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	if(id!=null)
	            		o=procesarPeticionOK(in, id,responseCode);
	            	else
	            		o=procesarPeticionOK(in,responseCode);
	            	
	            }
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	if(id!=null)
	            		o=procesarPeticionError(in,id,responseCode);
	            	else
	            		o=procesarPeticionError(in,responseCode);
	            }
	            return o;	 
	            }	                
          catch (ConnectException e) {
				CSVHandler csv= new CSVHandler();
				try {
					csv.escribirCSV(CSVHandler.PATH_ERROR_SERVER_NO_ALCANZADO, url+"/"+id==null?"":id);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}
          catch (Exception e) {
        	  e.printStackTrace();
				CSVHandler csv= new CSVHandler();
				if(id!=null)
					csv.escribirErrorException("Error al realizar request "+ url+"/"+id==null?"":id+" de la entidad "+ApuntadorDeEntidad.getInstance().getEntidadActual(),e.getStackTrace(),false);
				return null;
			}
      }
	
	abstract protected Object procesarPeticionOK(BufferedReader in, String id,int responseCode) throws Exception;
	abstract protected Object procesarPeticionError(BufferedReader in, String id, int responseCode) throws Exception;
	abstract protected  Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception;
	abstract protected Object procesarPeticionError(BufferedReader in, int responseCode) throws Exception;
	abstract protected  Object procesarPeticionOK(BufferedReader in, JSONHandler json, int responseCode) throws Exception;
	abstract protected Object procesarPeticionError(BufferedReader in, JSONHandler json,int responseCode) throws Exception;
	abstract protected Object procesarPeticionOK(BufferedReader in, JSONHandler json, String id,int responseCode) throws Exception;
	abstract protected Object procesarPeticionError(BufferedReader in, JSONHandler json, String id, int responseCode) throws Exception;

}
