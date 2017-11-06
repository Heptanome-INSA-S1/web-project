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
	        "content": queryParam
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
                Movie
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
            "http://dbpedia.org/ontology/abstract[@fr]": queryParam
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