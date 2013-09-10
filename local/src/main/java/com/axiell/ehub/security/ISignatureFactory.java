package com.axiell.ehub.security;

interface ISignatureFactory {

    Signature createExpectedSignature(Long ehubConsumerId, String libraryCard, String pin);
}
