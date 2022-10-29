package test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Father{
	Son son1 = new Son();
	public Son getSon1() {
		return son1;
	}
	public class Son{
		Son2 son2 = new Son2();
		public Son2 getSon2() {
			return son2;
		}
		class Son2{
			
		}
	}
}
class Test{
	public Test() {
		super();
	}
	int att;
	public void setAtt(int a) {
		att = a;
	}
	public Object searchVar(String name) {
		
		Map<String, Object>variables = new HashMap<>();
		variables.put("fathher", new Father());
		Object base = null;
		try {
			String[] path = name.split("\\.");
			base = variables.get(path[0]);
			for(int i=1;i<path.length;i++) {
				String mName = "get"+(char)(path[i].charAt(0)-32)+path[i].substring(1, path[i].length());
				base = base.getClass().getMethod(mName, null).invoke(base,null);
				
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base;
	}
    public static void main(String args[]) {
    	System.out.println(simulator.Constants.NODE_TYPE.class);
    	try {
			Class<?> a = Class.forName("simulator.Constants$NODE_TYPE");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  }
 