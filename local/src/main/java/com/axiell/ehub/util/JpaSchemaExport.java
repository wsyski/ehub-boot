package com.axiell.ehub.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that provides the possibility to export an database schema from the JPA annotated objects.
 */
@SuppressWarnings("deprecation")
public class JpaSchemaExport {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaSchemaExport.class);

    /**
     * Main method.
     * 
     * @param args a list of arguments to the schema export  
     * @throws IOException if an I/O exception occured
     */
    public static void main(String[] args) throws IOException {
        execute(args[0], args[1], args[2], Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]));
    }

    /**
     * Exports the database schema.
     * 
     * @param persistenceUnitName
     * @param hbm2ddlFile
     * @param outputFile
     * @param isCreate
     * @param isFormat
     * @throws IOException
     */
    public static void execute(String persistenceUnitName, String hbm2ddlFile, String outputFile, boolean isCreate, boolean isFormat) throws IOException {
        LOGGER.debug("Starting schema export");
        Properties properties = new Properties();
        properties.load(new FileInputStream(hbm2ddlFile));
        Ejb3Configuration cfg = new Ejb3Configuration().configure(persistenceUnitName, properties);
        Configuration hbmcfg = cfg.getHibernateConfiguration();
        SchemaExport schemaExport = new SchemaExport(hbmcfg);
        schemaExport.setOutputFile(outputFile);
        schemaExport.setFormat(isFormat);
        schemaExport.setDelimiter(";");
        schemaExport.execute(true, false, false, isCreate);
        LOGGER.debug("Schema exported to " + outputFile);
    }
}
