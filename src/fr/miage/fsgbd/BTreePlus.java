package fr.miage.fsgbd;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import static fr.miage.fsgbd.Noeud.mapPointeurs;


/**
 * @author Galli Gregory, Mopolo Moke Gabriel
 * @param <Type>
 */
public class BTreePlus<Type> implements java.io.Serializable {
    private Noeud<Type> racine;

    public BTreePlus(int u, Executable e) {
        racine = new Noeud<Type>(u, e, null);
    }

    public void afficheArbre() {
        racine.afficheNoeud(true, 0);
    }

    /**
     * Méthode récursive permettant de récupérer tous les noeuds
     *
     * @return DefaultMutableTreeNode
     */
    public DefaultMutableTreeNode bArbreToJTree() {
        return bArbreToJTree(racine);
    }

    private DefaultMutableTreeNode bArbreToJTree(Noeud<Type> root) {
        StringBuilder txt = new StringBuilder();
        for (Type key : root.keys)
            txt.append(key.toString()).append(" ");

        DefaultMutableTreeNode racine2 = new DefaultMutableTreeNode(txt.toString(), true);
        for (Noeud<Type> fil : root.fils) {
            root.refLeafs =  root.fils;// on assigne à l'objet refLeafs du Noeud ses fils

            racine2.add(bArbreToJTree(fil));
        }

        return racine2;
    }



    public boolean addValeur(Type valeur) {
        System.out.println("Ajout de la valeur POPOPO: " + valeur.toString());
        if (racine.contient(valeur) == null) {
            Noeud<Type> newRacine = racine.addValeur(valeur);
            if (racine != newRacine)
                racine = newRacine;
            return true;
        }
        return false;
    }


    public boolean addValeur(Type valeur, int index) {
        System.out.println("Ajout de la valeur : " + valeur.toString() + " index : "+ index);
        mapPointeurs.put((Integer) valeur, index);
        if (racine.contient(valeur) == null) {
            Noeud<Type> newRacine = racine.addValeur(valeur, index);
            if (racine != newRacine)
                racine = newRacine;
            return true;
        }
        return false;
    }


    public void removeValeur(Type valeur) {
        System.out.println("Retrait de la valeur : " + valeur.toString());
        if (racine.contient(valeur) != null) {
            Noeud<Type> newRacine = racine.removeValeur(valeur, false);
            if (racine != newRacine)
                racine = newRacine;
        }
    }

    public void getDataFromIndex(int key) {
        afficheArbre();
        if(mapPointeurs.isEmpty()){
            System.out.println("Veuillez d'abord charger le fichier");
        }
        else {
            if(mapPointeurs.get(key) == null){
                System.out.println("Echec de la recherche");
            }
            else{
                System.out.println("On recherche les donnees de la key "+ key);
                System.out.println("Les donnees se trouve a la ligne "+ mapPointeurs.get(key)+" du fichier");
                int lineIndex = mapPointeurs.get(key);
                String lineData;
                try (Stream<String> lines = Files.lines(Paths.get("sample.csv"))) {
                    lineData = lines.skip(lineIndex-1).findFirst().get();
                    String[] splitted = lineData.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    System.out.println("index: "+splitted[0]+", | key: "+splitted[1]+" | Prenom: "+splitted[2]+" | Nom: "+splitted[3]+" | Email: "+splitted[4]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void benchmark(BTreePlus<Integer> bInt) throws IOException, InterruptedException {

        if(mapPointeurs.isEmpty()){
            System.out.println("Veuillez d'abord charger le fichier");
        }

        Long tempsMinSeq= 100000000L;//on augmente la taille car le temps est très court
        Long tempsMinIdx= 100000000L;
        Long tempsMaxSeq=1000L;
        Long tempsMaxIdx=1000L;

        ArrayList<Integer> keys = new ArrayList<>();
        ArrayList<Long> tempsSeq = new ArrayList<>();
        ArrayList<Long> tempsIdx = new ArrayList<>();

        //on pourrait également utiliser bInt et récuréper les clés. Ici on utilise le fichier 100keys qui cointient 100 keys du fichier custom

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("100keys.txt"));
            String currentLine = reader.readLine();
            while (currentLine != null) {
                Integer key = Integer.valueOf(currentLine);
                // read next line
                currentLine = reader.readLine();
                keys.add(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n");
        System.out.println("100 recherches indexees");
        Thread.sleep(1000);
        System.out.println("\n\n");
        String result;

        for( Integer key : keys ) {
            try (Stream<String> lines = Files.lines(Paths.get("sample.csv"))) {
                Long start = System.nanoTime();
                result = lines.skip(mapPointeurs.get(key)-1).findFirst().get();
                Long end = System.nanoTime();
                Long tempsRecherche = (end-start);
                System.out.println(result +" "+tempsRecherche/ 1_000_000.0+" milliseconds");

                if (tempsRecherche < tempsMinIdx)
                    tempsMinIdx = tempsRecherche;
                else if (tempsRecherche > tempsMaxIdx)
                    tempsMaxIdx = tempsRecherche;

                tempsIdx.add(tempsRecherche);//on ajoute le temps de la recherche à notre liste
            }


        }
        System.out.println("\n\n");
        System.out.println("100 recherches sequentielles");
        Thread.sleep(1000);
        System.out.println("\n\n");


        for( Integer key : keys ) {
            Long start = System.nanoTime();
            try {
                BufferedReader reader2 = new BufferedReader(new FileReader("sample.csv"));
                String currentLine = reader2.readLine();
                while (currentLine != null) {
                    String[] splitted = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    Integer getKey = Integer.valueOf(splitted[1]);// correspond à la valeur key de la ligne
                    if(getKey == (int)key){
                        Long end = System.nanoTime();
                        Long tempsRecherche = (end-start);
                        System.out.println(currentLine +" "+tempsRecherche/ 1_000_000.0+" milliseconds");

                        if (tempsRecherche < tempsMinSeq)
                            tempsMinSeq = tempsRecherche;
                        else if (tempsRecherche < tempsMaxSeq)
                            tempsMaxSeq = tempsRecherche;
                        tempsSeq.add(tempsRecherche);
                        break;//on sort du while
                    }
                    else {
                        // read next line
                        currentLine = reader2.readLine();
                    }
                }
                // line is not visible here.
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        Double averageSeq = tempsSeq.stream().mapToInt(val -> Math.toIntExact(val)).average().orElse(0.0);
        Double averageIdx = tempsIdx.stream().mapToInt(val -> Math.toIntExact(val)).average().orElse(0.0);

        Long tempsTotalSeq = tempsSeq.stream()// On fait la somme des temps
                .mapToLong(a -> a)
                .sum();

        Long tempsTotalIdx=tempsIdx.stream()// On fait la somme des temps
                .mapToLong(a -> a)
                .sum();

        System.out.println("\n");

        System.out.println("\nTotal des 100 recherches indexees : "+tempsTotalIdx/ 1e9+ " sec, "+
                "temps moyen une recherche " + averageSeq/ 1_000_000.0+" milliseconds, "+
                "temps recherche plus longue "+ tempsMaxSeq/ 1_000_000.0+" milliseconds, "+
                "temps recherche plus courte "+ tempsMinSeq/ 1_000_000.0+" milliseconds ou "+ tempsMinSeq +" nanoseconds.");

        System.out.println("\nTotal des 100 recherches sequentielles : "+tempsTotalSeq/ 1e9+ " sec, "+
                "temps moyen une recherche " + averageIdx/ 1_000_000.0+" milliseconds, "+
                "temps recherche plus longue "+ tempsMaxIdx/ 1_000_000.0+" milliseconds, "+
                "temps recherche plus courte "+ tempsMinIdx/ 1_000_000.0+" milliseconds " + tempsMinIdx +" nanoseconds.");



    }
}
