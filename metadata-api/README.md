# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
La componente **metadata-api** si occupa di esporre i servizi web per le ricerche indicizzate sui metadati del prodotto [Yucca Data Service](..).
# Prerequisites
Si rimanda ai file [README.md](../README.md) del prodotto per i dettagli specifici.
# Configurations
Nel codice sorgente sono stati inseriti dei segnaposto per identificare le variabili della configurazione appplicativa, ad esempio `@@variabile@@`: questa notazione consente di riconoscerle facilmente all'inderno del progetto.

La tabella seguente contiene l'elenco delle variabili della configurazione applicativa, la loro posizione all'interno del progetto e una breve descrizione o un valore di esempio.
|Percorso|Variabile|Descrizione o esempio|
|-:|-|-|
|[src/main/resources/dev/log4j.properties](src/main/resources/dev/log4j.properties#L1)|log4j.rootLogger|DEBUG, myConsoleAppender, file|
|[src/main/resources/dev/log4j.properties](src/main/resources/dev/log4j.properties#L4)|log4j.appender.myConsoleAppender|`org.apache.log4j.ConsoleAppender`|
|[src/main/resources/dev/log4j.properties](src/main/resources/dev/log4j.properties#L5)|log4j.appender.myConsoleAppender.layout|`org.apache.log4j.PatternLayout`|
|[src/main/resources/dev/log4j.properties](src/main/resources/dev/log4j.properties#L6)|log4j.appender.myConsoleAppender.layout.ConversionPattern|`%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L1)|METADATAAPI_BASE_URL|`https://api.example.com/metadataapi/api/`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L2)|STORE_BASE_URL|`https://userportal.example.com/store/`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L3)|USERPORTAL_BASE_URL|`https://userportal.example.com/userportal/`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L4)|SERVICE_BASE_URL|`http://service.example.com:90/wso001/services/`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L5)|MANAGEMENT_BASE_URL||
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L6)|EXPOSED_API_BASE_URL|`https://api.example.com:443/api/`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L8)|OAUTH_BASE_URL|`https://api.example.com/`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L9)|OAUTH_USERNAME|admin|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L10)|OAUTH_ROLES_WEBSERVICE_URL|`https://sso.example.com/services/UserAdmin`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L12)|SEARCH_ENGINE_BASE_URL|`master1.example.com:2181,master2.example.com:2181/solr`|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L13)|SOLR_TYPE_ACCESS|ZOOKEEPER|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L14)|SOLR_USERNAME||
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L15)|SOLR_SECURITY_DOMAIN_NAME|KERBEROS-DOMAIN|
|[src/main/resources/dev/MetadataApiConfig.properties](src/main/resources/dev/MetadataApiConfig.properties#L16)|SEARCH_ENGINE_COLLECTION|domain_metasearch|
|[src/main/resources/dev/MetadataApiSecret.properties](src/main/resources/dev/MetadataApiSecret.properties#L1)|OAUTH_PASSWORD||
# Installing
Si rimanda ai file [README.md](../README.md) del prodotto per i dettagli specifici.
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
