package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.CommandData;

interface IOcdAuthenticator {

    BearerToken authenticate(CommandData data);
}
