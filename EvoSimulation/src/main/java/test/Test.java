package test;


class Father<T>{
	public Father() {
		
	}
	public void print(T e1) {
		System.out.println(e1);
	}
}
class Test<E> extends Father<E>{
	public Test() {
		super();
	}
    public static void main(String args[]) {
    	Class<?> clazz = new Test<AAA>().getClass();
    	Father  t = null;
    	try {
			t = (Father)Class.forName("test.Test").getConstructors()[0].newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	t.print(AAA.A1);
    	t.print("as");
    	System.out.println(t.getClass().toGenericString());
    }
  }
 