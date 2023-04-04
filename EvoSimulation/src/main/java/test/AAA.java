package test;

import java.lang.annotation.Documented;

@ClassPreamble(hola = "aaaa")
public class AAA{
	
}

@Documented
@interface ClassPreamble {

   String hola();
}
