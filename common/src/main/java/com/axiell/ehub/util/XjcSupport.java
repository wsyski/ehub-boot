package com.axiell.ehub.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * Provides the possibility to marshal and unmarshal XML documents.
 */
public final class XjcSupport {

    /**
     * Private constructor that prevents direct instantiation.
     */
    private XjcSupport() {
    }

    /**
     * Marshals the provided DTO object to an XML document.
     *
     * @param dto the DTO to marshal
     * @return an XML document as a String
     */
    public static String marshal(JAXBElement<?> dto) {
        try {
            Marshaller marshaller = getJaxbContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            StringWriter writer = new StringWriter();
            marshaller.marshal(dto, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            throw new InternalServerErrorException("Unable to marshal DTO", ex);
        }
    }

    /**
     * Marshals the provided DTO object to an XML document.
     *
     * @param dto the DTO to marshal
     * @return an XML document as a String
     */
    public static String marshal(Object dto) {
        try {
            Marshaller marshaller = getJaxbContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(dto, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            throw new InternalServerErrorException("Unable to marshal DTO", ex);
        }
    }

    /**
     * Unmarshals the provided XML document.
     *
     * @param xml the XML document to unmarshal
     * @return the Java object
     * @throws JAXBException if an exception occurred when unmarshalling the XML document
     */
    public static Object unmarshal(String xml) throws JAXBException {
        Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return unmarshaller.unmarshal(reader);

    }

    /**
     * Unmarshals the provided XML document.
     *
     * @param xml  the XML document to unmarshal
     * @param type the type of the Java object
     * @return the Java object of the specified type
     * @throws JAXBException if an exception occurred when unmarshalling the XML document
     */
    public static <T> T unmarshal(String xml, Class<T> type) throws JAXBException {
        Object obj = unmarshal(xml);
        return type.cast(obj);
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(final Date date) {
        if (date == null)
            return null;
        
        final GregorianCalendar gregorianCalendar = newGregorianCalendar(date);
        return newXmlGregorianCalendar(gregorianCalendar);
    }

    private static GregorianCalendar newGregorianCalendar(final Date date) {
	final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
	return calendar;
    }

    private static XMLGregorianCalendar newXmlGregorianCalendar(GregorianCalendar gregorianCalendar) {
	final DatatypeFactory factory = newDatatypeFactory();
        return factory.newXMLGregorianCalendar(gregorianCalendar);
    }

    private static DatatypeFactory newDatatypeFactory() {
        try {
            return DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
    }
    
    /**
     * Returns a {@link javax.xml.bind.JAXBContext}.
     * <p/>
     * <p>
     * This method together with the class {@link com.axiell.ehub.util.XjcSupport.JaxbContextHolder} exploits the
     * guarantee that the {@link javax.xml.bind.JAXBContext} will not be initialized until it is used.
     * </p>
     *
     * @return a {@link javax.xml.bind.JAXBContext}
     */
    private static JAXBContext getJaxbContext() {
        return JaxbContextHolder.JAXB_CONTEXT;
    }

    /**
     * The lazy initialization holder class for the {@link javax.xml.bind.JAXBContext} field.
     */
    private static class JaxbContextHolder {
        /**
         * The domain JAXB context path.
         */
        private static final Class<?>[] CONTEXT_PATH =
                {PendingLoan.class, ReadyLoan.class, Formats.class, EhubError.class, ContentProviderLoan.class};

        /**
         * The domain {@link javax.xml.bind.JAXBContext} singleton.
         */
        private static final JAXBContext JAXB_CONTEXT = createContext();

        /**
         * Creates a new instance of {@link javax.xml.bind.JAXBContext}.
         *
         * @return a new instance of {@link javax.xml.bind.JAXBContext}
         */
        private static JAXBContext createContext() {
            try {
                return JAXBContext.newInstance(CONTEXT_PATH);
            } catch (JAXBException ex) {        	
                throw new InternalServerErrorException("Could not create a JAXB context for the context path '" + Arrays.toString(CONTEXT_PATH) + "'", ex);
            }
        }
    }
}
