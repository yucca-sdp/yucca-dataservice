# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
La componente **insertdata-api** si occupa di esporre i servizi web per l'inserimento dei dati in modalità real-time del prodotto [Yucca Data Service](https://github.com/yucca-sdp/yucca-dataservice).
# Prerequisites
Si rimanda ai file [README.md](https://github.com/yucca-sdp/yucca-dataservice/blob/master/README.md) del prodotto per i dettagli specifici.
# Configurations
Nel codice sorgente sono stati inseriti dei segnaposto per identificare le variabili della configurazione appplicativa, ad esempio `@@variabile@@`: questa notazione consente di riconoscerle facilmente all'inderno del progetto.

La tabella seguente contiene l'elenco delle variabili della configurazione applicativa, la loro posizione all'interno del progetto e una breve descrizione o un valore di esempio.

| Percorso | Variabile | Descrizione o esempio | 
| ---: | --- | --- | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L2) | store.url | `https://store.example.com/store/` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L5) | publisher.url | `https://publisher.example.com/publisher/` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L6) | publisher.consoleAddress | `https://publisher.example.com` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L7) | publisher.baseExposedApiUrl | `https://api.example.com:443/api/` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L8) | publisher.httpOk | `HTTP/1.1 200 OK` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L9) | publisher.responseOk | `"error" : false` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L10) | publisher.baseApiUrl | `http://api.example.com/api/odata/SmartDataOdataService.svc/` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L13) | solr.collection | sdp_metasearch | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L14) | solr.type.access | kerberos | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L15) | solr.url | `solr1.example.com:2181,solr2.example.com:2181/solr` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L16) | solr.security.domain.name | KERBEROS-SECURITY-DOMAIN | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L19) | hive.jdbc.url | `jdbc:hive2://hive.example.com:8443/;ssl=true;?hive.server2.transport.mode=http;hive.server2.thrift.http.path=gateway/default/hive` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L22) | datainsert.base.url | `http://api.example.com/dataset/input/` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L23) | datainsert.delete.url | `http://api.example.com/insertdataapi/dataset/delete/` | 
| [src/main/resources/dev/adminapi.properties](src/main/resources/dev/adminapi.properties#L26) | gateway-api.base.url | `https://api.example.com/api/` | 
| [src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L2) | hive.jdbc.user |  | 
| [src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L3) | hive.jdbc.password |  | 
| [src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L7) | store.user |  | 
| [src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L8) | store.password |  | 
| [src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L11) | solr.username |  | 
| [src/main/resources/dev/adminapiSecret.properties](src/main/resources/dev/adminapiSecret.properties#L12) | solr.password |  | 
| [src/main/resources/dev/ambiente_deployment.properties](src/main/resources/dev/ambiente_deployment.properties#L1) | collprefix |  | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L2) | driver.class.name | org.postgresql.Driver | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L3) | url | `jdbc:postgresql://database.example.com:5432/DB_NAME/` | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L4) | datasource.username |  | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L5) | password |  | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L6) | max.idle | 4 | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L7) | max.active | 9 | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L10) | store.url | `https://store.example.com/store/` | 
| [src/main/resources/dev/datasource.properties](src/main/resources/dev/datasource.properties#L13) | hive.jdbc.url | `jdbc:hive2://hive.example.com:8443/;ssl=true;?hive.server2.transport.mode=http;hive.server2.thrift.http.path=gateway/default/hive` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L2) | ADMIN_API_URL | `http://adminapi.example.com:90/adminapi/` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L6) | ENABLE_DATA_MASSIVE_SERVICE_HTTP | true | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L7) | ENABLE_STREAM_MASSIVE_SERVICE_HTTP | true | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L8) | ENABLE_MEDIA_MASSIVE_SERVICE_HTTP | true | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L9) | ENABLE_STREAM_REALTIME_SERVICE_JMS | true | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L10) | ENABLE_STREAM_VALIDATION_SERVICE_HTTP | false | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L11) | ENABLE_STREAM_VALIDATION_SERVICE_JMS | false | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L12) | ENABLE_SOCIAL_TWITTER_SERVICE_CRON | false | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L14) | ENABLE_AUTHENTICATION_FILTER_HTTP | false | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L18) | PHOENIX_URL | `jdbc:phoenix:phoenix.example.com:2181:/hbase-secure:domain@DOMAIN.EXAMPLE:COM:/etc/security/keytabs/domain.headless.keytab` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L19) | SOLR_URL | `solr1.example.com:2181,solr2.example.com:2181/solr` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L20) | SOLR_TYPE_ACCESS | KNOX | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L21) | SOLR_USERNAME |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L22) | SOLR_PASSWORD |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L23) | SOLR_SECURITY_DOMAIN_NAME | NO_SECURITY | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L24) | SOLR_INDEXER_ENABLED | true | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L27) | KNOX_SDNET_ULR | `https://knox.example.com:8443/gateway/default/webhdfs/v1/datalake/` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L28) | KNOX_SDNET_USERNAME |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L29) | KNOX_SDNET_PASSWORD |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L30) | MAIL_SERVER | `mail.example.com` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L31) | MAIL_TO_ADDRESS | `support@example.com` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L32) | MAIL_FROM_ADDRESS | `support@example.com` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L33) | DELETE_MAIL_SUBJECT_404 | `"CANCELLAZIONE DATASET - 404 File non trovati su HDFS"` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L34) | DELETE_MAIL_BODY_404 | `"Non sono stati trovati i file richiesti sul server. Path richiesto: "` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L35) | DELETE_MAIL_SUBJECT_500 | `"CANCELLAZIONE DATASET - 500 Errore sul server HDFS"` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L36) | DELETE_MAIL_BODY_500 | `"Errore nel tentativo di cancellazione del seguente file: "` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L37) | DELETE_MAIL_SUBJECT_200 | `"CANCELLAZIONE DATASET - Cancellazione effettuata con successo"` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L38) | DELETE_MAIL_BODY_200 | `"E' stato eliminato correttamente il seguente file: "` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L42) | JMS_MB_INTERNAL_URL | `failover:(tcp://mb1.example.com:61626,tcp://mb2.example.com:61626)` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L43) | JMS_MB_INTERNAL_USERNAME |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L44) | JMS_MB_INTERNAL_PASSWORD |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L45) | JMS_MB_EXTERNAL_URL | `failover:(tcp://stream1.example.com:61616,tcp://stream2.example.com:61616)` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L46) | JMS_MB_EXTERNAL_USERNAME |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L47) | JMS_MB_EXTERNAL_PASSWORD |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L49) | RBAC_USER_STORE_WEBSERVICE_URL | `https://sso.example.com/services/RemoteUserStoreManagerService` | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L50) | RBAC_WEBSERVICE_USER |  | 
| [src/main/resources/dev/InsertdataApiConfig2.properties](src/main/resources/dev/InsertdataApiConfig2.properties#L51) | RBAC_WEBSERVICE_PASSWORD |  | 
| [src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L1) | default.broker.url | `failover://tcp://api.example.com:61616` | 
| [src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L2) | response.queue | fabricControllerQueueNew | 
| [src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L3) | broker.user |  | 
| [src/main/resources/dev/jms-message.properties](src/main/resources/dev/jms-message.properties#L4) | broker.password |  | 
| [src/main/resources/dev/jwt.properties](src/main/resources/dev/jwt.properties#L4) | jwt.token.secret |  | 
| [src/main/resources/dev/jwt.properties](src/main/resources/dev/jwt.properties#L7) | jwt.auth.header |  | 
| [src/main/resources/dev/jwt.properties](src/main/resources/dev/jwt.properties#L10) | jwt.expire.hours |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L2) | ADMIN_API_URL | `http://adminapi.example.com:90/adminapi/` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L6) | ENABLE_DATA_MASSIVE_SERVICE_HTTP | false | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L7) | ENABLE_STREAM_MASSIVE_SERVICE_HTTP | false | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L8) | ENABLE_MEDIA_MASSIVE_SERVICE_HTTP | false | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L9) | ENABLE_STREAM_REALTIME_SERVICE_JMS | false | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L10) | ENABLE_STREAM_VALIDATION_SERVICE_HTTP | true | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L11) | ENABLE_STREAM_VALIDATION_SERVICE_JMS | true | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L12) | ENABLE_SOCIAL_TWITTER_SERVICE_CRON | true | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L14) | ENABLE_AUTHENTICATION_FILTER_HTTP | true | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L18) | PHOENIX_URL | `jdbc:phoenix:phoenix.example.com:2181:/hbase-secure:domain@DOMAIN.EXAMPLE:COM:/etc/security/keytabs/domain.headless.keytab` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L19) | SOLR_URL | `solr1.example.com:2181,solr2.example.com:2181/solr` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L20) | SOLR_TYPE_ACCESS | KNOX | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L21) | SOLR_USERNAME |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L22) | SOLR_PASSWORD |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L23) | SOLR_SECURITY_DOMAIN_NAME | NO_SECURITY | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L24) | SOLR_INDEXER_ENABLED | true | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L27) | KNOX_SDNET_ULR | `https://knox.example.com:8443/gateway/default/webhdfs/v1/datalake/` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L28) | KNOX_SDNET_USERNAME |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L29) | KNOX_SDNET_PASSWORD |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L30) | MAIL_SERVER | `mail.example.com` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L31) | MAIL_TO_ADDRESS | `support@example.com` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L32) | MAIL_FROM_ADDRESS | `support@example.com` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L33) | DELETE_MAIL_SUBJECT_404 | `"CANCELLAZIONE DATASET - 404 File non trovati su HDFS"` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L34) | DELETE_MAIL_BODY_404 | `"Non sono stati trovati i file richiesti sul server. Path richiesto: "` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L35) | DELETE_MAIL_SUBJECT_500 | `"CANCELLAZIONE DATASET - 500 Errore sul server HDFS"` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L36) | DELETE_MAIL_BODY_500 | `"Errore nel tentativo di cancellazione del seguente file: "` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L37) | DELETE_MAIL_SUBJECT_200 | `"CANCELLAZIONE DATASET - Cancellazione effettuata con successo"` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L38) | DELETE_MAIL_BODY_200 | `"E' stato eliminato correttamente il seguente file: "` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L42) | JMS_MB_INTERNAL_URL | `failover:(tcp://mb1.example.com:61626,tcp://mb2.example.com:61626)` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L43) | JMS_MB_INTERNAL_USERNAME |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L44) | JMS_MB_INTERNAL_PASSWORD |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L45) | JMS_MB_EXTERNAL_URL | `failover:(tcp://stream1.example.com:61616,tcp://stream2.example.com:61616)` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L46) | JMS_MB_EXTERNAL_USERNAME |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L47) | JMS_MB_EXTERNAL_PASSWORD |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L49) | RBAC_USER_STORE_WEBSERVICE_URL | `https://sso.example.com/services/RemoteUserStoreManagerService` | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L50) | RBAC_WEBSERVICE_USER |  | 
| [src/main/resources/dev/ValidationdataApiConfig.properties](src/main/resources/dev/ValidationdataApiConfig.properties#L51) | RBAC_WEBSERVICE_PASSWORD |  | 
| [src/main/resources/dev/web-services.properties](src/main/resources/dev/web-services.properties#L1) | adminservice.soap.endpoint | `https://mb.example.com:9446/wso003/services/EventProcessorAdminService.EventProcessorAdminServiceHttpsSoap11Endpoint` | 
| [src/main/resources/dev/web-services.properties](src/main/resources/dev/web-services.properties#L2) | adminservice.soap.endpoint.user | admin | 
| [src/main/resources/dev/web-services.properties](src/main/resources/dev/web-services.properties#L3) | adminservice.soap.endpoint.password |  | 

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
