Équipe du Projet :
- Membre 1 : Charles-Antoine Tremblay
- Membre 2 : Hassan Abass

Lien vers le Dépôt GitHub :
[[IFT-3913-TP1]](https://github.com/Charles-ATremblay/IFT-3913-TP1)

Instructions d'utilisation :

Pour exécuter les jars, ils suffit d'écrire la commande :
```bash
java -jar tloc.jar
```

Note: Vous avez besoin d'une version récente de Java Runtime pour l'exécuter

1. Compilation: 
Si les jars ne fonctionnent pas, pour rouler le code en ligne de commande on doit compiler les fichiers en faisant: 
```bash
javac ./src./*.java
```
Lorsque vous êtes rendu dans le fichier ./src.

2. Exécution :
3. Pour toutes les applications, la classa Main est celle qui exécute le code.
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
