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

```
