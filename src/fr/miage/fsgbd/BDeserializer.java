package fr.miage.fsgbd;

import java.io.*;

public class BDeserializer<Type> 
{	
	  public  BTreePlus<Type> getArbre(String path) 
	  {
		BTreePlus<Type> arbre = null; 
	    try {
	      
	      FileInputStream fichier = new FileInputStream(path);
	      ObjectInputStream ois = new ObjectInputStream(fichier);
	      arbre = (BTreePlus<Type>) ois.readObject();
	      
	    } 
	    catch (java.io.IOException e) {
	      e.printStackTrace();
	    }
	    catch (ClassNotFoundException e) {
	      e.printStackTrace();
	    }
	    return arbre;
	   }
	
}

