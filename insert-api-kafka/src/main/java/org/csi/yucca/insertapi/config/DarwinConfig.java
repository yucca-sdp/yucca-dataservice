/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.insertapi.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Properties;

public class DarwinConfig {

    private final Config config;

    /**
     * Create the darwin config
     *
     * @param host       : host of postgres
     * @param database   : database used by darwin
     * @param user       : user to log on postgres
     * @param password   : password to log on postgres
     * @param properties : other properties to overwrite the default configuration of darwin
     */
    public DarwinConfig(String host, String database, String user, String password, Properties properties) {
        Properties _properties = new Properties();
        _properties.put("table", "schema_registry");
        _properties.put("type", "cached_lazy");
        _properties.put("createTable", "false"); // leave the table creation in charge of Wasp
        _properties.put("endianness", "LITTLE_ENDIAN");
        _properties.putAll(properties);
        _properties.put("host", host);
        _properties.put("db", database);
        _properties.put("username", user);
        _properties.put("password", password);
        this.config = ConfigFactory.parseProperties(_properties);
    }

    public DarwinConfig(String host, String database, String user, String password) {
        this(host, database, user, password, new Properties());
    }

    public Config getConfig() {
        return config;
    }
}

