package com.axiell.ehub.lms.palma;

import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

public class FileResponseUnmarshaller {
    private String contextPath;

    public FileResponseUnmarshaller(final String contextPath) {
        this.contextPath = contextPath;
    }

    public <T> T unmarshalFromFile(final String filePath) {
        try {
            Unmarshaller unmarshaller = getJaxbUnmarshaller();
            @SuppressWarnings("unchecked")
            JAXBElement<T> jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(new ClassPathResource(filePath).getFile());
            return jaxbElement.getValue();
        } catch (JAXBException | IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private Unmarshaller getJaxbUnmarshaller() {
        JAXBContext jc;
        Unmarshaller unmarshaller;
        try {
            jc = JAXBContext.newInstance(contextPath);
            unmarshaller = jc.createUnmarshaller();
        } catch (JAXBException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        return unmarshaller;
    }
}
