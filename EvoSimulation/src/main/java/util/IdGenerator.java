package util;

public class IdGenerator {
	int cont;
	public IdGenerator() {
		this.cont = 0;
	}
	public String getNextId() {
		cont++;
		return String.valueOf(cont);
	}
	public String getCurrentId() {
		return String.valueOf(cont);
	}
}
