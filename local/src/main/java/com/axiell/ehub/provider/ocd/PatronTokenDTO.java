package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_LIBRARY_ID;

public class PatronTokenDTO {
    @JsonProperty(value = "LibraryId")
    private String libraryId;
    @JsonProperty(value = "UserName")
    private String userName;
    @JsonProperty(value = "Password")
    private String password;

    PatronTokenDTO(ContentProviderConsumer contentProviderConsumer, Patron patron) {
        this.libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);;
        PatronDTO patronDTO = new PatronDTO(patron);
        this.userName = patronDTO.getUserName();
        this.password = patronDTO.getPassword();
    }
}
