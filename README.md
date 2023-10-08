Équipe du Projet :
- Membre 1 : Charles-Antoine Tremblay
- Membre 2 : Hassan Abass

Lien vers le Dépôt GitHub :
[Insérez ici le lien vers votre dépôt GitHub]

Instructions d'utilisation :

1. Compilation :
   
  Pour chaque .jar, l'exécution devrait ce faire en compilant le code avec javac

2. Exécution :
   2.1 TLOC: il faut passer en argument le nom du fichier qu'on souhaite vérifier.
   ```bash
   java Main ./FichierTest.java
   ```

   2.2 TASSERT: C'est la même chose

   2.3 TLS: il faut passer en argument le nom du fichier qu'on souhaite vérifier. On peut aussi creer un fichier CSV en utilisant le flag -o 
   ```bash
   java Main ./FichierTest.java -o chemin-à-la-sortie.csv
   ```

   2.4 TROPCOMP: il faut passer en argument le nom du fichier qu'on souhaite vérifier et le seuil. On peut aussi creer un fichier CSV en utilisant le flag -o 
   ```bash
   java Main -o chemin-à-la-sortie.csv ./FichierTest.java 5
   ```
