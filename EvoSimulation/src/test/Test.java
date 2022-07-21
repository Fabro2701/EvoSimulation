package test;

import java.util.ArrayList;
import java.util.Iterator;

public class Test  {  
	  
	public Test() {  
		
	}  
	public void right() {System.out.println("rr");}
	public void left(){}
	public void up(){}
	public void down(){}
	public void neutral(){}
	public boolean posF(int i) {return false;}
	public void prueba() {
		int d_f_d=1;
		int r_f_d=1;
		int l_f_d=1;
		int u_f_d=1;
		
		
	}
	
		
		
	  
	  
	  public static void main(String[] args) {  
		ArrayList<Integer>l1 = new ArrayList<Integer>();
		ArrayList<Integer>l2 = new ArrayList<Integer>();
		
		l1.add(1);
		l1.add(2);
		l1.add(3);
		l1.add(4);
		
		Iterator<Integer>i1=l1.iterator();
		
		i1.next();
		

		Iterator<Integer>i2=l1.iterator();
		System.out.println(i1.next());
		
		
		
		System.out.println(i2.next());
		
		
		
	  }  
	}  