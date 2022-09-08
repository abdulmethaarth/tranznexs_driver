package com.webnexs.tranznexsdriver.pojoClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Souvik Hazra on 25-02-2017.
 */

public class Response {
	public boolean error = false;
	int status = 0;
	JSONObject resp = null;

	public Response(String data_) throws JSONException {
		resp = new JSONObject(data_);
		status = resp.getInt("status");
		/*if (status == ReturnCodes.SYSTEM_ERROR) {
			error = true;
		}*/
	}

	public String getStringData() throws JSONException {
		return resp.getString("data");
	}

	public int getIntData() throws JSONException {
		return resp.getInt("data");
	}

	public boolean getBoolData() throws JSONException {
		return resp.getBoolean("data");
	}

	public double getDoubleData() throws JSONException {
		return resp.getDouble("data");
	}

	public JSONObject getJObjData() throws JSONException {
		return resp.getJSONObject("data");
	}

	public JSONArray getJArrayData() throws JSONException {
		return resp.getJSONArray("data");
	}

	@Override
	public String toString() {
		return resp.toString();
	}
}
