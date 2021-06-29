package fr.miage.fsgbd;

public interface Executable<T> extends java.io.Serializable
{
	public  boolean execute(T arg1, T arg2) ;	
}
