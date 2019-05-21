# Importez le projet dans votre IDE

URL pour cloner le projet: <http://gvipers.imt-lille-douai.fr/fa21-a43/exam-filiere-telecom.git>

- IntelliJ:
    1. Check out from version control
    2. Import from external model (Gradle)
    3. Clic-droit sur le projet → Run "All Tests"

- Eclipse:
    1. File/Import… → Projects from git → Clone URI
    2. Saisir l'URL ci-dessus, puis vos identifiants sur gvipers.imt-lille-douai.fr
    3. Wizard for project import → Import as general project
    4. Clic-droit sur le projet qui vient d'apparaître → Configure → Add gradle nature
    5. Clic-droit sur le projet → Run as… → JUnit test

# Vérifiez que le projet a été correctement importé

Vous devriez:
- voir un JDK et les libraries externes `junit-jupiter` et `junit-platform`,
  entre autres,
- voir le panneau Gradle avec un groupe `Examen java` contenant les tâches
  `test` et `zip`,
- être capables de lancer les tests.

Dans IntelliJ, si vous ne pouvez pas taper les accolades ou les crochets `{}[]`,
la marche à suivre est: ctrl+shift+a → taper `registry` → cocher
`actionSystem.fix.alt.gr` ou `actionSystem.force.alt.gr`.

# Résolvez les tests fournis dans l'ordre

Activez les tests un par un, afin de les résoudre dans l'ordre du fichier.

Pour activer un test:
1. Supprimez ou commentez l'annotation `@Disabled`.
2. Le cas échéant, supprimez ou commentez l'instruction `fail(...)` et
   décommentez le corps du test.

Il est possible que la solution d'un test nécessite vous force à modifier le
code introduit pour un test précédent.

**Attention:** Tout test ne compilant pas bloque l'exécution de la suite de
tests. C'est le cas des tests dont le corps est fourni en commentaire, car ils
requièrent la création de classes ou méthodes. Il est donc inutile voire
contre-productif d'activer plus d'un test à la fois.

# Règles d'évaluation / délivrable en fin de séance

**Seuls les tests verts vous accordent des points.** À part pour les activer ou
désactiver, **ne modifiez aucun test**. Votre code sera évalué par les tests
originaux.

En fin de séance :
 1. Éditez la variable `ext.studentName` du fichier `build.gradle` pour y mettre votre `prenom.nom`.
 2. Lancez la tâche `zip` depuis le panneau Gradle.
    Cela produit un fichier `prenom_nom.zip` à la racine du projet.
 3. [Déposez ce fichier sur Whippet](https://whippet.telecom-lille.fr/mod/assign/view.php?id=11660)
