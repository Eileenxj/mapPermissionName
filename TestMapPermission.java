/**
 * 
 */
/** 函数处理一个英文的权限结构
 * @author eileen
 *
 */
package test;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsDateJsonBeanProcessor;



public class TestMapPermission {
	
	public static JSONObject testPermissionFile(String str1, Map permissionMap, Map permissionTypeMap) {
		JSONObject returnJson = new JSONObject();
		try {
			//先将字符串转化为json对象
			JSONObject jsonObject = JSONObject.fromObject(str1);
	        Iterator<?> iterator = jsonObject.keys();
	        String key = null;
	        String value = null;
	        while(iterator.hasNext()) {
	        	key = (String)iterator.next();
	        	value = jsonObject.getString(key);
	        	if(value.startsWith("{")) {
	        		JSONObject jsonValue=(JSONObject) jsonObject.get(key);
	        		JSONObject jsonObject3=new JSONObject();
	        		if(permissionTypeMap.containsKey(key)){
	        			jsonObject3.put("name",permissionTypeMap.get(key));
	        		}
	        		jsonObject3.putAll(jsonValue);
	    			JSONObject jsonObject2=testPermissionFile(jsonObject3.toString(), permissionMap, permissionTypeMap);
	    			returnJson.put(key, jsonObject2);		
	        	}else if(value.startsWith("[")) {
	        		JSONArray arr= (JSONArray) jsonObject.get(key);
	        		JSONArray jsonArray2=new JSONArray();
	        		for (int i = 0; i < arr.size(); i++) {
						if(permissionMap.containsKey(arr.get(i))){
			    			JSONObject returnJson1 = new JSONObject();
			    			returnJson1.put("type", arr.get(i));
			    			returnJson1.put("name", permissionMap.get(arr.get(i)));
			    			jsonArray2.element(returnJson1);
						}
	        		}
	        	returnJson.put(key, jsonArray2);
	        }else {
	        	returnJson.put(key, value);
	        }
	        	
	    }
		}catch(JSONException jExp){
			throw new JSONException("%传入的参数不是JSON格式%"+jExp.getMessage());
		}


		return returnJson;
	}
	
    public static void main(String[] args) {
    	JSONObject jsonObject = new JSONObject();
    	JSONObject jsonObject1 = new JSONObject();
    	JSONObject jsonObject2 = new JSONObject();
    	//input parameters
    	jsonObject1.put("PERMISSIONS", new String[] {"READ_INVENTORY_DASHBOARD","READ_CONNECTION_DASHBOARD"});
    	jsonObject2.put("PERMISSIONS", new String[]{"MANAGE_DEPARTMENT","MANAGE_USER"});
    	jsonObject.put("DASHBOARD", jsonObject1);
		jsonObject.put("USER", jsonObject2);
		//System.out.println(jsonObject.toString());
		
		//name permissionMap
		Map permissionMap =new	HashMap<>();
		permissionMap.put("READ_INVENTORY_DASHBOARD", "商品统计查看");
		permissionMap.put("READ_CONNECTION_DASHBOARD", "供应商统计查看");
		permissionMap.put("MANAGE_DEPARTMENT", "部门管理");
		permissionMap.put("MANAGE_USER", "员工管理");
		
		//type permissionMap
		Map permissionTypeMap = new HashMap<>();
		permissionTypeMap.put("DASHBOARD", "统计面板权限");
		permissionTypeMap.put("USER", "员工权限管理");
		JSONObject returnJson=testPermissionFile(jsonObject.toString(), permissionMap, permissionTypeMap);
		
		System.out.println(returnJson.toString());
		
    }
	
}