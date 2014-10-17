package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.v267.patron.*;

import javax.jws.WebService;

@WebService(serviceName = "PatronWebServiceService", portName = "patron", targetNamespace = "http://patron.v267.palma.services.arena.axiell.com/",
        wsdlLocation = "com/axiell/ehub/lms/palma_267/patron.wsdl", endpointInterface = "com.axiell.arena.services.palma.v267.patron.Patron")
public class PalmaPatronService_267 extends AbstractPalmaService implements Patron {
    private static final String CONTEXT_PATH = "com.axiell.arena.services.palma.v267.patron";

    @Override
    public AuthenticatePatronResponse.AuthenticatePatronResult authenticatePatron(AuthenticatePatron.AuthenticatePatronParam authenticatePatronParam) {
        AuthenticatePatronResponse response = getFileResponseUnmarshaller().unmarshalFromFile(PALMA_AUTHENTICATE_PATRON_RESPONSE_XML);
        return response.getAuthenticatePatronResult();
    }

    @Override
    public ChangeCardPinResponse.ChangeCardPinResult changeCardPin(ChangeCardPin.ChangeCardPinParam changeCardPinParam) {
        return null;
    }

    @Override
    public ChangePhoneResponse.ChangePhoneNumberResult changePhone(ChangePhone.ChangePhoneNumberParam changePhoneNumberParam) {
        return null;
    }

    @Override
    public RemoveEmailResponse.RemoveEmailAddressResult removeEmail(RemoveEmail.RemoveEmailAddressParam removeEmailAddressParam) {
        return null;
    }

    @Override
    public AddEmailResponse.AddEmailAddressResult addEmail(AddEmail.AddEmailAddressParam addEmailAddressParam) {
        return null;
    }

    @Override
    public RemovePhoneResponse.RemovePhoneNumberResult removePhone(RemovePhone.RemovePhoneNumberParam removePhoneNumberParam) {
        return null;
    }

    @Override
    public ChangeEmailResponse.ChangeEmailAddressResult changeEmail(ChangeEmail.ChangeEmailAddressParam changeEmailAddressParam) {
        return null;
    }

    @Override
    public GetPatronStatusResponse.PatronStatusResult getPatronStatus(GetPatronStatus.PatronStatusParam patronStatusParam) {
        return null;
    }

    @Override
    public AddPhoneResponse.AddPhoneNumberResult addPhone(AddPhone.AddPhoneNumberParam addPhoneNumberParam) {
        return null;
    }

    @Override
    public GetPatronInformationResponse.PatronInformationResult getPatronInformation(GetPatronInformation.PatronInformationParam patronInformationParam) {
        return null;
    }

    @Override
    public GetMessagesResponse.GetMessagesResult getMessages(GetMessages.GetMessagesParam getMessagesParam) {
        return null;
    }

    @Override
    protected String getContextPath() {
        return CONTEXT_PATH;
    }
}
