package test;

public class B extends A{
	static int g=4;
	public static void main(String args[]) {
		System.out.println(A.g);
		System.out.println(B.g);
	}
}
