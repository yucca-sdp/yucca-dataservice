# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
Il prodotto **Yucca Data Service** è composto dalle seguenti componenti:
- [admin-api](https://github.com/yucca-sdp/yucca-dataservice/tree/master/admin-api) (API di amministrazione)
- [admindb](https://github.com/yucca-sdp/yucca-dataservice/tree/master/admindb) (Script SQL per la gestione del database PostgreSQL)
- [binary-api](https://github.com/yucca-sdp/yucca-dataservice/tree/master/binary-api) (API per la gestione dei file binari)
- [data-api](https://github.com/yucca-sdp/yucca-dataservice/tree/master/data-api) (API per le interrogazioni sui dati)
- [insert-api-kafka](https://github.com/yucca-sdp/yucca-dataservice/tree/master/insert-api-kafka) (API per l'inserimento dei dati tramite Apache Kafka)
- [insertdata-api](https://github.com/yucca-sdp/yucca-dataservice/tree/master/insertdata-api) (API per l'inserimento dei dati in modalità real-time)
- [metadata-api](https://github.com/yucca-sdp/yucca-dataservice/tree/master/metadata-api) (API per le ricerche indicizzate sui metadati)
# Prerequisites
I prerequisiti per l'installazione del prodotto sono i seguenti:
## Software
- [OpenJDK 8](https://openjdk.java.net/install/) o equivalenti
- [Apache Maven 3](https://maven.apache.org/download.cgi)
- [Oracle JBoss 6.4 GA](https://developers.redhat.com/products/eap/download)
- [PostgreSQL 9.2.7](https://www.postgresql.org/download/

Si rimanda ai file README&#46;md delle singole componenti per i dettagli specifici.
# Configurations
Nei file README.md delle singole componenti verranno elencate le variabili per la loro configurazione.
# Installing
## Istruzioni per la compilazione
- Per effettuare con sucesso la compilazione del prodotto è necessario scaricare manualmente la libreria [ojdbc6.jar](https://download.oracle.com/otn/utilities_drivers/jdbc/11204/ojdbc6.jar) e copiarla nella directory [admin-api/lib](https://github.com/yucca-sdp/yucca-dataservice/tree/master/admin-api/lib).
- Da riga di comando eseguire `mvn -Dmaven.test.skip=true -P dev clean package`
- La compilazione genera le seguenti unità di installazione:
    - `admin-api/target/adminapi.war`
	- `admindb/target/admindb-*.zip`
	- `binary-api/target/binaryapi-*.war`
	- `data-api/target/dataapi-*.war`
	- `insert-api-kafka/target/insert-api-kafka-*.war`
	- `insertdata-api/target/insertdataapi-*.war`
	- `metadata-api/target/metadataapi.war`
## Istruzioni per l'installazione
- Creare uno schema standard sul database PostgreSQL.
- Estrarre il file `admindb-1.1.0-001.zip`: all'interno sono presenti gli script SQL da eseguire sul database designato per l'installazione del prodotto.
- Eseguire sullo schema designato per l'installazione del prodotto gli script:
    - `init_01_create-postgresql.sql`
	- `init_02_create-sequence.sql`
	- `init_03_insert-data.sql`
	- `init_04_yucca-ecosystem.sql`
	- `init_05_updates.sql`
- Effettuare il deploy dei file `adminapi.war`, `binaryapi-*.war`, `dataapi-*.war`, `insert-api-kafka-*.war`, `insertdataapi-*.war` e `metadataapi.war` secondo procedura standard JBoss.
## Informazioni aggiuntive
- Lo script `drop_01_postgresql.sql` potrà essere utilizzato per la rimozione delle tabelle dallo schema.
# Versioning
Per la gestione del codice sorgente viene utilizzata la metodologia [Semantic Versioning](https://semver.org/).
# Authors
Gli autori della piattaforma Yucca sono:
- [Alessandro Franceschetti](mailto:alessandro.franceschetti@csi.it)
- [Claudio Parodi](mailto:claudio.parodi@csi.it)
# Copyrights
(C) Copyright 2019 - 2021 Regione Piemonte
# License
Questo software è distribuito con licenza [EUPL-1.2-or-later](https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12)

Consulta il file [LICENSE.txt](LICENSE.txt) per i dettagli sulla licenza.

Per le componenti che utilizzano librerie la cui licenza prevede un'integrazione, sono state inserite le informazioni necessarie nel file THIRD_PARTY_NOTE.
