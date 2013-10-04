package com.axiell.ehub.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class that provides the possibility to export an database schema from
 * the JPA annotated objects.
 */
@SuppressWarnings("deprecation")
public final class JpaSchemaExport {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaSchemaExport.class);
    private static final int PERSISTENCE_UNIT_NAME_ARGUMENT = 0;
    private static final int HBM2DDL_FILE_ARGUMENT = 1;
    private static final int OUTPUT_FILE_ARGUMENT = 2;
    private static final int CREATE_ARGUMENT = 3;
    private static final int FORMAT_ARGUMENT = 4;

    /**
     * Private constructor that prevents direct instantiation.
     */
    private JpaSchemaExport() {	
    }
    
    /**
     * Main method.
     * 
     * @param args
     *            a list of arguments to the schema export
     * @throws IOException
     *             if an I/O exception occured
     */
    public static void main(String[] args) throws IOException {
	execute(args[PERSISTENCE_UNIT_NAME_ARGUMENT],
            args[HBM2DDL_FILE_ARGUMENT],
            args[OUTPUT_FILE_ARGUMENT],
            Boolean.parseBoolean(args[CREATE_ARGUMENT]),
            Boolean.parseBoolean(args[FORMAT_ARGUMENT]));
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
        Properties properties = loadProperties(hbm2ddlFile);
        Ejb3Configuration cfg = new Ejb3Configuration().configure(persistenceUnitName, properties);
        Configuration hbmcfg = cfg.getHibernateConfiguration();
        SchemaExport schemaExport = new SchemaExport(hbmcfg);
        schemaExport.setOutputFile(outputFile);
        schemaExport.setFormat(isFormat);
        schemaExport.setDelimiter(";");
        schemaExport.execute(true, false, false, isCreate);
        LOGGER.debug("Schema exported to " + outputFile);
    }

    private static Properties loadProperties(String hbm2ddlFile) throws IOException {
	final Properties properties = new Properties();
	InputStream is = null;

	try {
	    is = new FileInputStream(hbm2ddlFile);
	    properties.load(is);
	} finally {
	    closeStream(is);
	}
	return properties;
    }

    private static void closeStream(InputStream is) {
	if (is != null) {
	    try {
		is.close();
	    } catch (IOException e) {
		LOGGER.error("Could not close stream", e);
	    }
	}
    }
}
