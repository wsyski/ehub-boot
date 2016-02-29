package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.axiell.ehub.provider.ocd.CheckoutDTO.FileDTO;

class Checkout {
    private static final String SUCCESS = "SUCCESS";
    private final CheckoutDTO checkoutDTO;

    Checkout(final CheckoutDTO checkoutDTO) {
        this.checkoutDTO = checkoutDTO;
    }

    boolean isSuccessful() {
        final String output = checkoutDTO.getOutput();
        return SUCCESS.equals(output);
    }

    String getTransactionId() {
        return checkoutDTO.getTransactionId();
    }

    Date getExpirationDate() {
        return checkoutDTO.getExpirationDate();
    }

    List<String> getDownloadUrls() {
        String downloadUrl = checkoutDTO.getDownloadUrl();
        return downloadUrl == null ? getFileDownloadUrls() : Collections.singletonList(downloadUrl);
    }

    private List<String> getFileDownloadUrls() {
        List<FileDTO> files = checkoutDTO.getFiles();
        if (noFiles(files)) {
            throw new InternalServerErrorException("No files found for record isbn: '" + checkoutDTO.getIsbn() + "' and content provider name: 'OCD'",
                    ErrorCause.INTERNAL_SERVER_ERROR);
        }
        return files.parallelStream().map(fileDTO -> OcdDownloadUrlHandler.resolve(fileDTO.getDownloadUrl())).collect(Collectors.toList());
    }

    private boolean noFiles(List<FileDTO> files) {
        return files == null || files.isEmpty();
    }
}
