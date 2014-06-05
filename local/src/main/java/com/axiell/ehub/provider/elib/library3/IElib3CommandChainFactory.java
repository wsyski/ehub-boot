package com.axiell.ehub.provider.elib.library3;

interface IElib3CommandChainFactory {

    GetFormatsCommandChain createGetFormatsCommandChain();

    CreateLoanCommandChain createCreateLoanCommandChain();

    GetContentCommandChain createGetContentCommandChain();
}
