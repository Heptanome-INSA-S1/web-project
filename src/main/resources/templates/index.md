# Api Documentation

## Methods

- [searchLinks](#user-content-searchlinks)
- [fillContent](#user-content-fillcontent)
- [analyse](#user-content-analyse)
- [getRdf](#user-content-getrdf)

### /searchLinks

```
GET http://localhost:8080/searchLinks?query=QUERY&offset=OFFSET
```

| Argument Name | Kind | Type | Description |
|:--------:|:----:|:----:|:-----------:|
| query | QueryParam | String | The key words to search |
| offset | QueryParam | Optional Integer `default = 0` | The result offset |

Query example:
```
GET http://localhost:8080/searchLinks?query=Game+Of+Thrones&offset=10
```
[Run](http://localhost:8080/searchLinks?query=Game+Of+Thrones&offset=10)

Response example:
```js
// List of SearchLink
[
    {
        "url": "https://www.hbo.com/game-of-thrones",
        "title": "Game of Thrones - Official Website for the HBO Series - HBO.com",
        "description": "The official website for Game of Thrones on HBO, featuring full episodes online, interviews, schedule information and episode guides.",
        "content": null
    },
    {
        "url": "https://fr.wikipedia.org/wiki/Game_of_Thrones",
        "title": "Game of Thrones — Wikipédia",
        "description": "Cet article ou cette section contient des informations sur une série télévisée en cours de production, programmée ou prévue. Le texte est susceptible de contenir ...",
        "content": null
    } //,...
]
```

### /fillContent

```
PUT http://localhost:8080/fillContent
```

| Argument Name | Kind | Type | Description |
|:--------:|:----:|:----:|:-----------:|
| - | BodyParam | Array of [SearchLink](#user-content-searchlink) | The search links to fill |

Query example:
```json
// Schema: Array<SearchLink>
[
    {
        "url": "https://www.hbo.com/game-of-thrones",
        "title": "Game of Thrones - Official Website for the HBO Series - HBO.com",
        "description": "The official website for Game of Thrones on HBO, featuring full episodes online, interviews, schedule information and episode guides.",
        "content": null
    },
    {
        "url": "https://fr.wikipedia.org/wiki/Game_of_Thrones",
        "title": "Game of Thrones — Wikipédia",
        "description": "Cet article ou cette section contient des informations sur une série télévisée en cours de production, programmée ou prévue. Le texte est susceptible de contenir ...",
        "content": null
    }
]
```

Response example:
```json
// Schema: Array<SearchLink> 
[
    {
        "url": "https://www.hbo.com/game-of-thrones",
        "title": "Game of Thrones - Official Website for the HBO Series - HBO.com",
        "description": "The official website for Game of Thrones on HBO, featuring full episodes online, interviews, schedule information and episode guides.",
        "content": "Summers span decades. Winters can last a lifetime. And the struggle for the Iron Throne begins. Based on the bestselling book series by George R.R. Martin and created by David Benioff and D.B. Weiss. Season 7 Looking for the Latest?Head to the Season 7 episode page for the most recent interviews, videos and extras. Spoilers follow.Season..."
    },
    {
        "url": "https://fr.wikipedia.org/wiki/Game_of_Thrones",
        "title": "Game of Thrones — Wikipédia",
        "description": "Cet article ou cette section contient des informations sur une série télévisée en cours de production, programmée ou prévue. Le texte est susceptible de contenir ...",
        "content": "Le texte est susceptible de contenir des informations spéculatives et son contenu peut être nettement modifié au fur et à mesure de l’avancement de la série et des informations disponibles s’y rapportant.  Logo original de la série.modifier Game of Thrones, également désignée par le titre français de l'œuvre romanesque dont elle est adaptée, Le Trône de fer (A Song of Ice and Fire), est une série télévisée américaine médiéval-fantastique[1] créée par David Benioff et D. B. Weiss, diffusée depuis le 17 avril 2011 sur HBO. Il s'agit de l'adaptation de la série de romans écrits par George R. R. Martin depuis 1996, saga réputée pour..."
    }
]
```

### /analyse

```
POST http://localhost:8080/analyse
```

| Argument Name | Kind | Type | Description |
|:--------:|:----:|:----:|:-----------:|
| - | BodyParam | [DBpediaQuery](#user-content-dbpediaquery) | The DBpediaQuery to send |

Query example:
```json
// Schema: DBpediaQuery
{
	"confidence": 0.8,
	"support": 5,
	"resources":
	[
	    {
	        "url": "https://www.hbo.com/game-of-thrones",
	        "title": "Game of Thrones - Official Website for the HBO Series - HBO.com",
	        "description": "The official website for Game of Thrones on HBO, featuring full episodes online, interviews, schedule information and episode guides.",
	        "content": "Summers span decades. Winters can last a lifetime. And the struggle for the Iron Throne begins. Based on the bestselling book series by George R.R. Martin and created by David Benioff and D.B. Weiss.    Season 7  Looking for the Latest? Head to the Season 7 episode page for the most recent interviews, videos and extras.  Spoilers follow. Season 7  Looking for the Latest? Head to the Season 7 episode page for the most recent interviews, videos and extras.  Spoilers follow. WATCH FOR FREE Missing Game of Thrones? The Game Revealed  pulls back the curtain on the biggest scenes of Season 7. Watch the first episode now.    Visit the sites below to discover everything you need to know about  Game of Thrones.      MAKING GAME OF THRONES Visit the Official Blog Stocked with interviews, storyboards, prop photos and facts from seasons past, use  Making Game of Thrones  to go deeper inside the epic series.    GAMEOFTHRONES.COM The Ultimate Viewer?s Guide Seven seasons can be hard to keep straight. Let the Viewer?s Guide help: Tell it where you are in the series and explore recaps, videos and images.    WHISPERS OF WESTEROS Sign Up for the Newsletter Stay up to date with exclusive content, behind-the-scenes looks and more with Whispers of Westeros, the  Game of Thrones  newsletter.  MAKING GAME OF THRONES Visit the Official Blog Stocked with interviews, storyboards, prop photos and facts from seasons past, use  Making Game of Thrones  to go deeper inside the epic series.    GAMEOFTHRONES.COM The Ultimate Viewer?s Guide Seven seasons can be hard to keep straight. Let the Viewer?s Guide help: Tell it where you are in the series and explore recaps, videos and images.    WHISPERS OF WESTEROS Sign Up for the Newsletter Stay up to date with exclusive content, behind-the-scenes looks and more with Whispers of Westeros, the  Game of Thrones  newsletter.  ?Pulls you in ? and doesn?t let go.? ? LOS ANGELES TIMES A Story in Cloth Costume designer Michele Clapton discusses her approach and where she finds inspiration.    A Story in Special Effects Special effects supervisor Sam Conway talks creating ?chaos and mayhem? from the set.    A Story in Score Composer Ramin Djawadi describes his process for creating a defining sound for  Game of Thrones.    A Story in Cloth Costume designer Michele Clapton discusses her approach and where she finds inspiration.    A Story in Special Effects Special effects supervisor Sam Conway talks creating ?chaos and mayhem? from the set.    A Story in Score Composer Ramin Djawadi describes his process for creating a defining sound for  Game of Thrones.    See the Opening Titles in 360 Travel from the North to King?s Landing to Dorne with this 360 degree video.    The Deuce Westworld The Leftovers Rome Enjoy HBO?s original series, hit movies, and more. HBO is available through your TV provider, existing digital subscriptions, or in our stand-alone app.    HBO GO is a free streaming service for those who subscribe to HBO through a TV provider.    \ufffd 2017 Home Box Office, Inc. All Rights Reserved. This website may contain mature content."
	    }
	]
}
```

Response example:
```json
// Schema: URI => Array<Annotation>
{
    "https://www.hbo.com/game-of-thrones": [
        {
            "surface": "WESTEROS",
            "types": [
                ""
            ],
            "support": 981,
            "uri": "http://dbpedia.org/resource/World_of_A_Song_of_Ice_and_Fire"
        },
        {
            "surface": "BD+",
            "types": [
                ""
            ],
            "support": 52,
            "uri": "http://dbpedia.org/resource/Durchmusterung"
        },
        {
            "surface": "HBO",
            "types": [
                "DBpedia:Agent",
                "Schema:Organization",
                "DBpedia:Organisation",
                "DBpedia:Broadcaster",
                "Schema:TelevisionStation",
                "DBpedia:TelevisionStation"
            ],
            "support": 8669,
            "uri": "http://dbpedia.org/resource/HBO"
        },
        {
            "surface": "Westworld",
            "types": [
                "Schema:CreativeWork",
                "DBpedia:Work",
                "Schema:Movie",
                "DBpedia:Film"
            ],
            "support": 84,
            "uri": "http://dbpedia.org/resource/Westworld"
        }
    ]
}
```

### /getRdf

```json
POST http://localhost:8080/getRdf
```

Query example:
```
{
	"url": "http://dbpedia.org/resource/List_of_A_Song_of_Ice_and_Fire_characters"
}
```

Response example: 
```json
// Schema: URI => Array<Tuple<Predicat, Object>>
{
	"http://dbpedia.org/resource/Garlan_Tyrell": [
        {
            "http://dbpedia.org/ontology/wikiPageRedirects": "http://dbpedia.org/resource/List_of_A_Song_of_Ice_and_Fire_characters"
        }
    ],
    "http://dbpedia.org/resource/Clegane": [
        {
            "http://dbpedia.org/ontology/wikiPageRedirects": "http://dbpedia.org/resource/List_of_A_Song_of_Ice_and_Fire_characters"
        }
    ],
    "http://dbpedia.org/resource/Obella_Sand": [
        {
            "http://dbpedia.org/ontology/wikiPageRedirects": "http://dbpedia.org/resource/List_of_A_Song_of_Ice_and_Fire_characters"
        }
    ],
	"http://dbpedia.org/resource/List_of_A_Song_of_Ice_and_Fire_characters": [
        {
            "http://dbpedia.org/ontology/abstract[@fr]": "Cet article présente la liste des personnages du cycle de fantasy Le Trône de fer (A Song of Ice and Fire) écrit par George R. R. Martin. Les romans présentent trois intrigues entrelacées : une guerre de dynastie pour le contrôle de Westeros par plusieurs grandes maisons ; la menace grandissante venue du Nord posée par des créatures appelées « les Autres » (The Others) ; et les ambitions royales de , l'héritière exilée d'une dynastie presque disparue. Chaque chapitre est narré à travers les yeux d'un seul personnage, par un point de vue interne à la troisième personne. Au début de la saga, les romans adoptaient à tour de rôle les points de vue de neuf personnages (1996) ; ce nombre va jusqu'à trente-et-un dans A Dance with Dragons (2011). Réutilisés dans les œuvres dérivées des romans, leurs descriptions peuvent varier légèrement, notamment par rapport à celles de la série télévisée, dont les personnages et leurs interprètes sont détaillés ici. Note : les personnages sont classés par grandes maisons ; au sein des différentes maisons, les membres de la famille sont indiqués en premier tandis que leur maisonnée, leurs alliés et leurs bannerets sont indiqués à la suite. Les noms originaux (en anglais) sont indiqués entre parenthèses.@fr"
        },
        {
            "http://www.w3.org/2002/07/owl#sameAs": "http://de.dbpedia.org/resource/Figuren_im_Lied_von_Eis_und_Feuer"
        }
    ]
}
```

## Data 

### SearchLink

```json
{
    "url": "string | The url of the link",
    "title": "string | The title of the link",
    "description": "string | A short description of the link's content",
    "content": "string | The content of the link"
}	
```

### DBpediaQuery
```json
{
	"confidence": "double | Confidence of dbpedia in the result (from 0 to 1.0)",
	"support": "integer | The minimal require number of in egdes on the resource in DBpedia",
	"resources": "array of <SearchLink> | The search links to analyse"
}
```

### Annotation
```json
{
    "surface": "string | The text which triggers the resource",
    "types": "array of <string> | The list of types of the resource",
    "support": "integer | The index of the surface word in the text",
    "uri": "string | The resource URI"
}
```