# com.inditex.shiwanzext

Shiwan's TFG project utilizes a RAG (Retrieval-Augmented Generation) model to develop a backend service designed to assist Product Owners in efficiently creating user stories. 
By using vector search with Weaviate, the project combines similar user stories, enabling the LLM (Language Model) to generate comprehensive and complete responses. 
Additionally, the system can integrate relevant search results from Google about the user story's topic, enhancing the precision of the generated responses.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

To get started just run the following commands (allways from the `root` of the project):

### Prerequisites

```
docker compose up
maven install
run DemoApplication

Descomentar linea en application.properties to use the ollama moondream
docker exec -it ollama /bin/bash
ollama run moondream

```

## cookies

JiraApiClient/cookies fetched in https://axinic.central.inditex.grp/jira/rest/api/2/search?jql=project=ICPRSITDEP%20AND%20issuetype%20=%20Historia&startAt=0&fields=key,description,summary,creator,project,updated,created&maxResults=1000
ModelApiService/cookies fetched in https://iop-isai-producto-dqa-dev.cloud.inditex.com/docs#/LLM%20%2B%20RAG/ask_api_ask_post

## example of user story
COMO usuario de SITDEP,
QUIERO poder ver el peso de las ventas de mi departamento (nivel jerárquico) que tienen promoción o no.
PARA poder analizar el volumen de ventas de mi departamento (u otro nivel jerárquico) de ventas con promoción y sin promoción.

AS a SITDEP user
I WANT to be able to see the weight of the sales of my department (hierarchical level) that have promotion or not.
TO to be able to analyze the sales volume of my department (or other hierarchical level) of promo and non-promo sales

http://localhost:9090/api/v1/USHelper/generate?userStory=AS a SITDEP user
I WANT to be able to see the weight of the sales of my department (hierarchical level) that have promotion or not.
TO to be able to analyze the sales volume of my department (or other hierarchical level) of promo and non-promo sales