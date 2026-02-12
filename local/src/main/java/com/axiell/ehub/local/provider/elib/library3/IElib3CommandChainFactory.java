package com.axiell.ehub.local.provider.elib.library3;

interface IElib3CommandChainFactory {

    GetFormatsCommandChain createGetFormatsCommandChain();

    CreateLoanCommandChain createCreateLoanCommandChain();

    GetContentCommandChain createGetContentCommandChain();
}
