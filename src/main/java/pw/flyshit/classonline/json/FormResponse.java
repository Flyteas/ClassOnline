package pw.flyshit.classonline.json;

import java.util.HashMap;
import java.util.Map;

public class FormResponse {
	private boolean success=true;
	private String msg;
	private Map<String,String> errors=new HashMap<String,String>();
	public void addError(String field,String msg)
	{
		errors.put(field, msg);
		if(success)
			success=false;
	}
	public boolean hasError()
	{
		return !errors.isEmpty();
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
}
