# Insert api kafka

The library exposes the required APIs to send one or more messages to a kafka topic.

## Use the library
To use the library is necessary to add the dependency in the `pom.xml` file
```
<dependency>
    <groupId>org.csi.yucca</groupId>
    <artifactId>insert-api-kafka</artifactId>
    <version>${last.insert-api-kafka.version}</version>
</dependency> 
```

## Instance the KafkaWriter
To instance the KafkaWriter is enough to use the constructor of `org.csi.yucca.insertapi.producer.KafkaWriterImpl`.

E.g.
```
org.csi.yucca.insertapi.producer.KafkaWriter kafkaWriter = new org.csi.yucca.insertapi.producer.KafkaWriterImpl()
```

## Initialize
It is required to initialize the KafkaWriter using the method `Initialize()` that uses two kinds of configuration:
- `kafkaConfig` : the configuration to write on Kafka
- `darwinConfig`: the configuration to register the Avro schema on PostgresDB through the Darwin library

The `kafkaConfig` contains the mandatory fields:
  - `kafkaBrokers`: list of the Kafka brokers 
  - `otherProperties`: used to change the default values of other Kafka properties.
    - The default value of `acks` is set to `-1`.
    - The fixed values of `key.serializer` and `value.serializer` are set to `org.apache.kafka.common.serialization.ByteArraySerializer` 
      (it is not possible change them by kafkaConfig).

The `darwinConfig` contains the following mandatory fields:
   - `host`: postgresDB host
   - `database`: postgresDB database
   - `user`: postgresDB user
   - `password`: postgresDB password
   - `properties`: custom properties useful for the Darwin library. If it is empty it is used the following defaults values: 
     - `table`: "schema_registry"
     - `type`: "cached_lazy"
     - `createTable`: "false"
     - `endianness`: "LITTLE_ENDIAN"

For more details about the Darwin library and its configurations it's possible to look on [https://github.com/agile-lab-dev/darwin](https://github.com/agile-lab-dev/darwin).

## Send a message
To generate a Kafka message and to write it on the associated Kafka topic it's possible to use the following method:
```
void send(String tenantCode, String datasetCode, int datasetVersion, String solrCollection, String docJson) throws Exception;
```
The topic name is chosen using the `tenantCode` and the rule `sdp_<tenantCode>`. 
- There is a check that `tenantCode` contains only Alphanumeric lowercase characters, `_` and `-` (NOT: spaces, dot and others)
- The topic must exist before sending the Kafka message: the topic creation is managed by Wasp Yucca.

The Kafka message is written on the Kafka topic in Avro format and the schema is managed by the Darwinlibrary.

The Kafka message is created using the following parameters:
- `datasetCode`   : it will be used to define a partition on the HDFS staging area (useful for the CSV creation)
- `datasetVersion`: it will be used to define a partition on the HDFS staging area (useful for the CSV creation)
- `solrCollection`: define the collection to store on Solr
- `docJson`       : define the data to store on Solr (and after on Csv). It must be a JSON

Of course if the message isn't written on Kafka for some problems, the method throws an exception.    


## Send a list of messages
To generate a list of Kafka messages and to write them on the associated topic it's possible to use the following method:
```
void send(String tenantCode, String datasetCode, int datasetVersion, String solrCollection, List<String> docJson) throws Exception;
```
It works like the above method but it manages a list of JSON instead of only one.
 
Note: note that it writes messages for the same `topic`, `datasetCode`, `datasetVersion` and Solr `collection`
