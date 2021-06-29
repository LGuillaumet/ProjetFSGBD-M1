Il s'agit d'une implémentation en Java d'un arbre balancé.

Plus précisément il s'agit d'une classe de gestion d'un BTree+ , ce qui signifie que TOUTES les données sont stockées dans les feuilles de l'arbre, les noeuds intermédiaires permettant uniquement de trier les données.

Les arbres BTree et BTree+ sont fréquemment utilisés, en particulier dans le stockage de données car ils permettent d'avoir toujours un temps de recherche maitrisable.

On les retrouve principalement dans les systèmes de fichier (NTFS par exemple) et dans les SGBD (Oracle...)

Ici sont gérés :
- la création d'un arbre de n'importe quel ordre
- l'ajout de données
- la recherche d'une valeur dans l'arbre

La classe de traitement est générique, c'est à dire que l'on peut stocker n'importe quel type de données dans l'arbre, mais comme il s'agit d'un tri, il faut définir et préciser une procédure de comparaison de ces données.

Grâce à la serialization, on peut aussi sauvegarder et charger un arbre.
