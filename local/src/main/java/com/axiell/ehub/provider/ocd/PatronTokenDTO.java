package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.codehaus.jackson.annotate.JsonProperty;

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
        this.userName = patron.getLibraryCard();
        this.password = patron.getPin();
    }
}
