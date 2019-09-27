# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
La componente **binary-api** si occupa di esporre i servizi web per la gestione dei file binari del prodotto [Yucca Data Service](..).
# Prerequisites
Si rimanda ai file [README.md](../README.md) del prodotto per i dettagli specifici.
# Configurations
Nel codice sorgente sono stati inseriti dei segnaposto per identificare le variabili della configurazione appplicativa, ad esempio `@@variabile@@`: questa notazione consente di riconoscerle facilmente all'inderno del progetto.

La tabella seguente contiene l'elenco delle variabili della configurazione applicativa, la loro posizione all'interno del progetto e una breve descrizione o un valore di esempio.
|Percorso|Variabile|Descrizione o esempio|
|-:|-|-|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L2)|store.url|`https://store.example.com/store/`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L5)|publisher.url|`https://publisher.example.com/publisher/`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L6)|publisher.consoleAddress|`https://publisher.example.com`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L7)|publisher.baseExposedApiUrl|`https://api.example.com:443/api/`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L8)|publisher.httpOk|`HTTP/1.1 200 OK`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L9)|publisher.responseOk|`"error" : false`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L10)|publisher.baseApiUrl|`http://api.example.com/api/odata/SmartDataOdataService.svc/`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L13)|solr.collection|sdp_metasearch|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L14)|solr.type.access|kerberos|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L15)|solr.url|`solr1.example.com:2181,solr2.example.com:2181/solr`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L16)|solr.security.domain.name|KERBEROS-SECURITY-DOMAIN|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L19)|hive.jdbc.url|`jdbc:hive2://hive.example.com:8443/;ssl=true;?hive.server2.transport.mode=http;hive.server2.thrift.http.path=gateway/default/hive`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L22)|datainsert.base.url|`http://api.example.com/dataset/input/`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L23)|datainsert.delete.url|`http://api.example.com/insertdataapi/dataset/delete/`|
|[src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L26)|gateway-api.base.url|`https://api.example.com/api/`|
|[src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L2)|hive.jdbc.user||
|[src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L3)|hive.jdbc.password||
|[src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L7)|store.user||
|[src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L8)|store.password||
|[src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L11)|solr.username||
|[src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L12)|solr.password||
|[src/main/resources/dev/ambiente_deployment.properties](src/main/resources/dev/ambiente_deployment.properties#L1)|collprefix||
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L2)|driver.class.name|org.postgresql.Driver|
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L3)|url|`jdbc:postgresql://database.example.com:5432/DB_NAME/`|
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L4)|datasource.username||
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L5)|password||
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L6)|max.idle|4|
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L7)|max.active|9|
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L10)|store.url|`https://store.example.com/store/`|
|[src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L13)|hive.jdbc.url|`jdbc:hive2://hive.example.com:8443/;ssl=true;?hive.server2.transport.mode=http;hive.server2.thrift.http.path=gateway/default/hive`|
|[src/main/resources/dev/email-service.properties](src/main/resources/dev/email-service.properties#L1)|req.action.mail-to||
|[src/main/resources/dev/email-service.properties](src/main/resources/dev/email-service.properties#L2)|req.action.mail-from||
|[src/main/resources/dev/email-service.properties](src/main/resources/dev/email-service.properties#L3)|req.action.mail-name||
|[src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L1)|default.broker.url|`failover://tcp://api.example.com:61616`|
|[src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L2)|response.queue|fabricControllerQueueNew|
|[src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L3)|broker.user||
|[src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L4)|broker.password||
|[src/main/resources/dev/jwt.properties](src/main/resources/dev/jwt.properties#L4)|jwt.token.secret||
|[src/main/resources/dev/jwt.properties](src/main/resources/dev/jwt.properties#L7)|jwt.auth.header||
|[src/main/resources/dev/jwt.properties](src/main/resources/dev/jwt.properties#L10)|jwt.expire.hours||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L2)|MONGO_HOST||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L3)|MONGO_PORT||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L4)|MONGO_DB_SUPPORT||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L5)|MONGO_DB_AUTH||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L6)|MONGO_DB_AUTH_FLAG||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L7)|MONGO_COLLECTION_SUPPORT_DATASET|metadata|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L8)|MONGO_COLLECTION_SUPPORT_API|api|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L9)|MONGO_COLLECTION_SUPPORT_STREAM|stream|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L10)|MONGO_COLLECTION_SUPPORT_TENANT|tenant|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L11)|MONGO_COLLECTION_SUPPORT_STATISTICS|statistics|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L12)|MONGO_COLLECTION_TENANT_ARCHIVEDATA|archivedata|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L13)|MONGO_COLLECTION_TENANT_ARCHIVEMEASURES|archivemeasures|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L14)|MONGO_COLLECTION_TENANT_DATA|data|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L15)|MONGO_COLLECTION_TENANT_MEASURES|measures|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L16)|MONGO_USERNAME||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L17)|HDFS_ROOT_DIR|datalake|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L18)|HDFS_USERNAME||
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L19)|BASE_API_URL|`http://api.example.com/api/`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L20)|STORE_API_ADDRESS|`https://userportal.example.com/store/apis/info?`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L21)|DAMMI_INFO|`http://api.example.com/dammiInfo`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L22)|CONSOLE_ADDRESS|`https://userportal.example.com`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L23)|HTTP_OK|`HTTP/1.1 200 OK`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L24)|RESPONSE_OK|`"error" : false`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L25)|HDFS_LIBRARY|webhdfs|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L27)|DATA_INSERT_BASE_URL|`http://api.example.com/insertdataapi/media/input/`|
|[src/main/resources/dev/SDPDataApiConfig.properties](src/main/resources/dev/SDPDataApiConfig.properties#L28)|API_ADMIN_SERVICES_URL|`http://adminapi.example.com:90/adminapi/`|
|[src/main/resources/dev/SDPDataApiSecret.properties](src/main/resources/dev/SDPDataApiSecret.properties#L3)|MONGO_PASSWORD||
|[src/main/resources/dev/SDPDataApiSecret.properties](src/main/resources/dev/SDPDataApiSecret.properties#L4)|KNOX_URL|`https://knox.example.com:8443/gateway/default/webhdfs/v1KNOX_PWD=@@KNOX_PWD@@`|
|[src/main/resources/dev/SDPDataApiSecret.properties](src/main/resources/dev/SDPDataApiSecret.properties#L5)|KNOX_PWD||
|[src/main/resources/dev/SDPDataApiSecret.properties](src/main/resources/dev/SDPDataApiSecret.properties#L6)|KNOX_USER||
|[src/main/resources/dev/SDPDataApiSecret.properties](src/main/resources/dev/SDPDataApiSecret.properties#L7)|KNOX_GROUP||
|[src/main/resources/dev/SDPDataApiSecret.properties](src/main/resources/dev/SDPDataApiSecret.properties#L9)|INSERTAPI_URL|`http://api.example.com/dataset/input/`|
|[src/main/resources/dev/web-services.properties](src/main/resources/dev/web-services.properties#L1)|adminservice.soap.endpoint|`https://mb.example.com:9446/wso003/services/EventProcessorAdminService.EventProcessorAdminServiceHttpsSoap11Endpoint`|
|[src/main/resources/dev/web-services.properties](src/main/resources/dev/web-services.properties#L2)|adminservice.soap.endpoint.user|admin|
|[src/main/resources/dev/web-services.properties](src/main/resources/dev/web-services.properties#L3)|adminservice.soap.endpoint.password||
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

Questa componente utilizza librerie la cui licenza prevede un'integrazione: sono state inserite le informazioni necessarie nel file THIRD_PARTY_NOTE.