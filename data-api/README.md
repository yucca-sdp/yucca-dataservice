# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
La componente **data-api** si occupa di esporre i servizi web per le interrogazioni sui dati del prodotto [Yucca Data Service](https://github.com/yucca-sdp/yucca-dataservice).
# Prerequisites
Si rimanda ai file [README.md](https://github.com/yucca-sdp/yucca-dataservice/blob/master/README.md) del prodotto per i dettagli specifici.
# Configurations
Nel codice sorgente sono stati inseriti dei segnaposto per identificare le variabili della configurazione appplicativa, ad esempio `@@variabile@@`: questa notazione consente di riconoscerle facilmente all'inderno del progetto.

La tabella seguente contiene l'elenco delle variabili della configurazione applicativa, la loro posizione all'interno del progetto e una breve descrizione o un valore di esempio.
|Percorso|Variabile|Descrizione o esempio|
|-:|-|-|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L3)|SDP_WEB_FILTER_PATTERN|`/OdataService.svc/`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L4)|SDP_WEB_SERVLET_URL|`/OdataServlet.svc/`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L5)|SDP_WEB_BASE_URL|`http://api.example.com/api/`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L7)|SDP_MONGO_CFG_DATASET_HOST|`speed1.example.com;speed2.example.com`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L8)|SDP_MONGO_CFG_DATASET_PORT|27017|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L9)|SDP_MONGO_CFG_DATASET_DB|DB_SUPPORT|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L10)|SDP_MONGO_CFG_DATASET_COLLECTION|metadata|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L12)|SDP_MONGO_CFG_API_HOST|`speed1.example.com;speed2.example.com`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L13)|SDP_MONGO_CFG_API_PORT|27017|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L14)|SDP_MONGO_CFG_API_DB|DB_SUPPORT|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L15)|SDP_MONGO_CFG_API_COLLECTION|api|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L17)|SDP_MONGO_CFG_STREAM_HOST|`speed1.example.com;speed2.example.com`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L18)|SDP_MONGO_CFG_STREAM_PORT|27017|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L19)|SDP_MONGO_CFG_STREAM_DB|DB_SUPPORT|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L20)|SDP_MONGO_CFG_STREAM_COLLECTION|stream|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L22)|SDP_MONGO_CFG_TENANT_HOST|`speed1.example.com;speed2.example.com`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L23)|SDP_MONGO_CFG_TENANT_PORT|27017|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L24)|SDP_MONGO_CFG_TENANT_DB|DB_SUPPORT|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L25)|SDP_MONGO_CFG_TENANT_COLLECTION|tenant|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L27)|SDP_MONGO_CFG_DEFAULT_HOST|`speed1.example.com;speed2.example.com`|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L28)|SDP_MONGO_CFG_DEFAULT_PORT|27017|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L30)|SDP_MONGO_CFG_DEFAULT_USER||
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L31)|SDP_MONGO_CFG_DEFAULT_PWD||
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L33)|SDP_MAX_DOCS_PER_PAGE|1000|
|[resources/dev/SDPDataApiConfig.properties](resources/dev/SDPDataApiConfig.properties#L34)|SDP_MAX_SKIP_PAGE|300000|
# Installing
Si rimanda ai file [README.md](https://github.com/yucca-sdp/yucca-dataservice/blob/master/README.md) del prodotto per i dettagli specifici.
# Versioning
Per la gestione del codice sorgente viene utilizzata la metodologia [Semantic Versioning](https://semver.org/).
# Authors
Gli autori della piattaforma Yucca sono:
- [Alessandro Franceschetti](mailto:alessandro.franceschetti@csi.it)
- [Claudio Parodi](mailto:claudio.parodi@csi.it)
# Copyrights
(C) Copyright 2019 Regione Piemonte
# License
Questo software è distribuito con licenza [EUPL-1.2-or-later](https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12)

Consulta il file [LICENSE.txt](../LICENSE.txt) per i dettagli sulla licenza.

Questa componente utilizza librerie la cui licenza prevede un'integrazione: sono state inserite le informazioni necessarie nel file THIRD_PARTY_NOTE.
