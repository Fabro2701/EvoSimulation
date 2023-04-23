package experiment.models.metro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
/*
General model Gauss3:
     f(x) = 
              a1*exp(-((x-b1)/c1)^2) + a2*exp(-((x-b2)/c2)^2) + 
              a3*exp(-((x-b3)/c3)^2)
Coefficients (with 95% confidence bounds):
       a1 =       157.5  (142.1, 172.9)
       b1 =       8.063  (7.976, 8.15)
       c1 =       1.123  (0.9982, 1.249)
       a2 =       85.02  (67.89, 102.1)
       b2 =       17.83  (17.67, 18)
       c2 =      0.8972  (0.6801, 1.114)
       a3 =       76.14  (67.41, 84.88)
       b3 =       15.85  (15.29, 16.41)
       c3 =       6.386  (5.479, 7.294)
 */
	static double a1 =  157.5d; 
    static double b1 =  8.063d; 
    static double c1 =  1.123d; 
    static double a2 =  85.02d; 
    static double b2 =  17.83d; 
    static double c2 = 0.8972d; 
    static double a3 =  76.14d; 
    static double b3 =  15.85d; 
    static double c3 =  6.386d; 
	public static double getStationDistribution(double x) {
		double r = a1*Math.exp(-Math.pow((x-b1)/c1,2)) + a2*Math.exp(-Math.pow((x-b2)/c2,2)) + 
        a3*Math.exp(-Math.pow((x-b3)/c3,2));
		return r<0d?0d:r;
	}
	public static void main(String args[]) {
		//System.out.println(getStationDistribution(8d));
		Pattern p = Pattern.compile("^[*/%]");
		Matcher m = p.matcher("%");
		System.out.println(m.find());
	}
}
