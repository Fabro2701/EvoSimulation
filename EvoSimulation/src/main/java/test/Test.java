package test;

import java.lang.reflect.Method;


class Test{
	public Test() {
		
	}
	public static void printlnString(String s) {
		System.out.println(s);
	}
    public static void main(String args[]) {
    	for(Method m:Test.class.getMethods()) {
    		System.out.println(m);
    	}
    }
  }
 