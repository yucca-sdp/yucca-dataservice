# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
La componente **admin-api** si occupa di esporre i servizi web di amministrazione del prodotto [Yucca Data Service](https://github.com/yucca-sdp/yucca-dataservice).
# Prerequisites
Si rimanda ai file [README.md](https://github.com/yucca-sdp/yucca-dataservice/blob/master/README.md) del prodotto per i dettagli specifici.
# Configurations
Nel codice sorgente sono stati inseriti dei segnaposto per identificare le variabili della configurazione appplicativa, ad esempio `@@variabile@@`: questa notazione consente di riconoscerle facilmente all'inderno del progetto.

La tabella seguente contiene l'elenco delle variabili della configurazione applicativa, la loro posizione all'interno del progetto e una breve descrizione o un valore di esempio.
|Percorso|Variabile|Descrizione o esempio|
|-:|-|-|
|[src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java](src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java#L114)|mailsender.host||
|[src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java](src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java#L117)|mailsender.username||
|[src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java](src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java#L121)|mail.smtp.starttls.enable||
|[src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java](src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java#L122)|mail.smtp.auth||
|[src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java](src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java#L123)|mail.transport.protocol||
|[src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java](src/main/java/org/csi/yucca/adminapi/conf/AppConfig.java#L124)|mail.debug||
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L2)|store.url|`https://store.example.com/store/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L5)|publisher.url|`https://publisher.example.com/publisher/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L6)|publisher.consoleAddress|`https://publisher.example.com`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L7)|publisher.baseExposedApiUrl|`https://api.example.com:443/api/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L8)|publisher.httpOk|`HTTP/1.1 200 OK`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L9)|publisher.responseOk|`"error" : false`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L10)|publisher.baseApiUrl|`http://api.example.com/api/odata/SmartDataOdataService.svc/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L13)|solr.type.access|kerberos|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L14)|solr.url|`solr1.example.com:2181,solr2.example.com:2181/solr`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L15)|solr.collection|sdp_metasearch|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L16)|solr.security.domain.name|KERBEROS-SECURITY-DOMAIN|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L19)|hive.jdbc.url|`jdbc:hive2://hive.example.com:8443/;ssl=true;?hive.server2.transport.mode=http;hive.server2.thrift.http.path=gateway/default/hive`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L22)|datainsert.base.url|`http://api.example.com/dataset/input/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L23)|datainsert.delete.url|`http://api.example.com/insertdataapi/dataset/delete/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L27)|gateway-api.base.url|`https://api.example.com/api/`|
|[src/main/resources/adminapi.properties](src/main/resources/adminapi.properties#L30)|userportal.url|`https://userportal.example.com`|
|[src/main/resources/adminapiSecret.properties](src/main/resources/adminapiSecret.properties#L2)|hive.jdbc.user||
|[src/main/resources/adminapiSecret.properties](src/main/resources/adminapiSecret.properties#L3)|hive.jdbc.password||
|[src/main/resources/adminapiSecret.properties](src/main/resources/adminapiSecret.properties#L7)|store.user||
|[src/main/resources/adminapiSecret.properties](src/main/resources/adminapiSecret.properties#L8)|store.password||
|[src/main/resources/adminapiSecret.properties](src/main/resources/adminapiSecret.properties#L11)|solr.username||
|[src/main/resources/adminapiSecret.properties](src/main/resources/adminapiSecret.properties#L12)|solr.password||
|[src/main/resources/ambiente_deployment.properties](src/main/resources/ambiente_deployment.properties#L1)|collprefix||
|[src/main/resources/datasource.properties](src/main/resources/datasource.properties#L1)|driver.class.name|org.postgresql.Driver|
|[src/main/resources/datasource.properties](src/main/resources/datasource.properties#L2)|url|`jdbc:postgresql://database.example.com:5432/DB_NAME/`|
|[src/main/resources/datasource.properties](src/main/resources/datasource.properties#L3)|datasource.username||
|[src/main/resources/datasource.properties](src/main/resources/datasource.properties#L4)|password||
|[src/main/resources/datasource.properties](src/main/resources/datasource.properties#L5)|max.idle|4|
|[src/main/resources/datasource.properties](src/main/resources/datasource.properties#L6)|max.active|9|
|[src/main/resources/email-service.properties](src/main/resources/email-service.properties#L1)|req.action.mail-to||
|[src/main/resources/email-service.properties](src/main/resources/email-service.properties#L2)|req.action.mail-from||
|[src/main/resources/email-service.properties](src/main/resources/email-service.properties#L3)|req.action.mail-name||
|[src/main/resources/email-service.properties](src/main/resources/email-service.properties#L4)|info.opendata.creation.mail-to||
|[src/main/resources/jms-message.properties](src/main/resources/jms-message.properties#L1)|default.broker.url|`failover://tcp://api.example.com:61616`|
|[src/main/resources/jms-message.properties](src/main/resources/jms-message.properties#L2)|response.queue|fabricControllerQueueNew|
|[src/main/resources/jms-message.properties](src/main/resources/jms-message.properties#L3)|broker.user||
|[src/main/resources/jms-message.properties](src/main/resources/jms-message.properties#L4)|broker.password||
|[src/main/resources/jwt.properties](src/main/resources/jwt.properties#L4)|jwt.token.secret||
|[src/main/resources/jwt.properties](src/main/resources/jwt.properties#L7)|jwt.auth.header||
|[src/main/resources/jwt.properties](src/main/resources/jwt.properties#L10)|jwt.expire.hours||
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L2)|log4j.rootLogger|DEBUG, stdout, file|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L5)|log4j.appender.stdout|org.apache.log4j.ConsoleAppender|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L6)|log4j.appender.stdout.Target|System.out|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L7)|log4j.appender.stdout.layout|org.apache.log4j.PatternLayout|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L8)|log4j.appender.stdout.layout.ConversionPattern|`%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n`|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L11)|log4j.appender.file|org.apache.log4j.RollingFileAppender|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L13)|log4j.appender.file.File|`${catalina.home}/logs/myapp.log`|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L14)|log4j.appender.file.MaxFileSize|5MB|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L15)|log4j.appender.file.MaxBackupIndex|10|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L16)|log4j.appender.file.layout|org.apache.log4j.PatternLayout|
|[src/main/resources/log4j.properties](src/main/resources/log4j.properties#L17)|log4j.appender.file.layout.ConversionPattern|`%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n`|
|[src/main/resources/web-services.properties](src/main/resources/web-services.properties#L1)|adminservice.soap.endpoint|`https://mb.example.com:9446/wso003/services/EventProcessorAdminService.EventProcessorAdminServiceHttpsSoap11Endpoint`|
|[src/main/resources/web-services.properties](src/main/resources/web-services.properties#L2)|adminservice.soap.endpoint.user|admin|
|[src/main/resources/web-services.properties](src/main/resources/web-services.properties#L3)|adminservice.soap.endpoint.password||
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