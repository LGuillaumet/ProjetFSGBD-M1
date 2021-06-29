package fr.miage.fsgbd;

public class TestString implements Executable<String> {
	public boolean execute(String str1, String str2) {
		return (str1.length() > str2.length());
	}
}