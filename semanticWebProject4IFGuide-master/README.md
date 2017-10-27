Guide Projet Web Sémantique 4IF
===============================

### Etape 1 - Récupérer quelques URLs de Google Search
Pour cela nous avons fourni le petit script **querygoogle.sh** qui permet de récupérer les **URLs** retournés par **Google Search** en réponse d'une requête **Query**.

Vous pouvez utiliser pour cela le script [queryGoogle.sh](./queryGoogle.sh). Par exemple, la requête ```QUERY=michelle+obama OFFSET=10 ./queryGoogle.sh``` va donner une dizaine d'**URLs** liés à la recherche **"michelle+obama"** à partir du **11ème** résultat selon le classement de **Google**.

### Etape 2 - Faire un déréférencement pour chaque URL obtenu
Pour cela il suffit de récupérer le texte contenu dans la page de l'**URL** en question. Nous fournissons deux manières de faire : 
 - **En utilisant la commande sed :** Pour cela vous pouvez utiliser le petit script améliorable [dereferenceURL.sh](./dereferenceURL.sh). Par exemple, pour faire le déréférencement de la page [https://www.whitehouse.gov/administration/first-lady-michelle-obama](https://www.whitehouse.gov/administration/first-lady-michelle-obama) on utilise ```URL=https://www.whitehouse.gov/administration/first-lady-michelle-obama ./dereferenceURL.sh```.
 - **En utilisant l'API Alchemy :** Une autre manière de faire est d'utiliser l'[API Alchemy](http://www.ibm.com/watson/developercloud/alchemy-language/api/v1/#text_cleaned) mais vous devez avoir une **apikey** de [http://www.alchemyapi.com/api/register.html](http://www.alchemyapi.com/api/register.html). Le petit script [dereferenceURLAlchemy.sh](./dereferenceURLAlchemy.sh) peut être utilisé de la manière suivante : ```APIKEY=your_api_key URL=https://www.whitehouse.gov/administration/first-lady-michelle-obama ./dereferenceURLAlchemy.sh```.

### Etape 3 - Récupérer les URI des entités DBpedia existantes dans le texte
Nous pouvons utiliser l'outil [DBPedia Spotlight](https://github.com/dbpedia-spotlight/dbpedia-spotlight). Le script [spotlightText.sh](./spotlightText.sh) montre une manière d'utiliser cet outil. 
Par exemple la commande ```TEXT="Donald Trump, né le 14 juin 1946 à New York, est un homme d'affaires américain. Magnat milliardaire de l'immobilier, il est également animateur de télévision et homme politique" CONFIDENCE=0.3 SUPPORT=10 ./spotlightText.sh``` retourne les **URI** concernés par le **text** en entrée. 
Le script [spotlightText.sh](./spotlightText.sh) utilise la commande [jq](https://stedolan.github.io/jq/) pour parser les fichier de format ```json```.

### Etape 4 - Explorer le graphe DBPedia à partir des URI obtenus
Les URI capturés à l'issu de l'**étape 3** peuvent être utilisés comme point d'entrée dans le graphe **DBPedia**. Plusieurs manières de faire sont possibles : 
 - **Voir les propriétés d'une seule ressource URI :** Le script [dbpediaExplore.sh](./dbpediaExplore.sh) donne une manière simple de faire. Vous pouvez l'utiliser de la manière suivante ```URI=http://dbpedia.org/resource/Donald_Trump OUTPUT_FORMAT=json ./dbpediaExplore.sh```.
 - **Executer une requête SPARQL en utilisant les URI obtenus :** En utilisant le [point d'entrée SPARQL de DBPedia](http://dbpedia.org/sparql) nous pouvons exécuter une requête SPARQL. Le script [dbpediaSPARQL.sh](./dbpediaSPARQL.sh) montre une manière de faire pour utiliser ce point d'entrée, celui-ci est utilisé de la manière suivante : ```SPARQL_QUERY_PATH=sparqlQuery.txt OUTPUT_FORMAT=json ./dbpediaSPARQL.sh``` où [sparqlQuery.txt](./sparqlQuery.txt) représente la requête SPARQL à exécuter. Vous pouvez bien sûr utiliser ensuite la commande ```jq``` (quand le format de sortie est ```json```) pour découvrir d'autres **entités** ou **propriétés**.
