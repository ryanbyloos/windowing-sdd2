# Format de fichiers
* La premiere ligne contient 4 nombres reels : $x_0 x_0' y_0 y_0'$ qui decrivent une fenetre $[x_0 : x_0'] X [y_0 : y_0']$ contenant l'ensemble  de tous les segments.
* Pour chaque segment, une ligne du fichier. $x y x' y'$ sont les coordonnees $(x, y), (x', y') des deux points qui definissent le segment.

# TODO
## Etape 1
Charger les segments dans un arbre de recherche a priorite.