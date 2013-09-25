package com.axiell.ehub.provider.askews;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import com.askews.api.IeBookService;

@WebServiceClient(name = "eBookService", targetNamespace = "http://tempuri.org/")
public class AskewsSoapService extends Service {
    public AskewsSoapService(URL wsdlDocumentLocation) {
        super(wsdlDocumentLocation, new QName("http://tempuri.org/", "eBookService"));
    }

    @WebEndpoint(name = "BasicHttpBinding_IeBookService")
    public IeBookService getBasicHttpBindingIeBookService() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IeBookService"), IeBookService.class);
    }
}
