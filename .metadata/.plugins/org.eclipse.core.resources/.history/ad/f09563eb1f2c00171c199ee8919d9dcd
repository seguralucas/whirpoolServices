package exit.services.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHandler{
	private JSONObject json;
	private String line;
	
	public JSONHandler(){}
	public JSONHandler(String line, String data) throws ParseException{
		setLine(line);
		crearteJson(data);
	}
	
	public JSONHandler(String line, JSONObject json) throws ParseException{
		setLine(line);
		this.json=json;
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}	
	
	/**
	 * Método Exclusivo para debuguear
	 * @param data
	 * @throws ParseException
	 */
	public void crearteJson(String data) throws ParseException{
		JSONParser parser = new JSONParser();
		this.json= (JSONObject)parser.parse(data);
	}
	
	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return json.toString().replace("\\", "").replace(",", ",\n");
	}
	
	public String toStringSinEnter(){
		return json.toString().replace("\\", "");
	}
	
	public String toStringNormal(){
		return json.toString();
	}
}
