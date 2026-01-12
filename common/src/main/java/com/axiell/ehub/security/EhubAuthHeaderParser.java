package com.axiell.ehub.security;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.IAuthHeaderParser;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.InvalidAuthorizationHeaderSignatureRuntimeException;
import com.axiell.authinfo.MissingOrUnparseableAuthorizationHeaderRuntimeException;
import com.axiell.authinfo.MissingSecretKeyRuntimeException;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.ErrorCause;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static com.axiell.ehub.util.EhubUrlCodec.decode;

public class EhubAuthHeaderParser implements IAuthHeaderParser {
  private static final String EHUB_CONSUMER_ID = "ehub_consumer_id";
  private static final String EHUB_PATRON_ID = "ehub_patron_id";
  private static final String EHUB_LIBRARY_CARD = "ehub_library_card";
  private static final String EHUB_PIN = "ehub_pin";
  private static final String EHUB_EMAIL = "ehub_email";
  private static final String EHUB_BIRTH_DATE = "ehub_birth_date";
  private static final String EHUB_SIGNATURE = "ehub_signature";

  private static final Pattern PARAMETER_PATTERN = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
  private static final int FIRST_GROUP = 1;
  private static final int SECOND_GROUP = 2;

  private final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver;

    public EhubAuthHeaderParser(IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        this.authHeaderSecretKeyResolver = authHeaderSecretKeyResolver;
    }

    private static String getSignature(final Long ehubConsumerId, final Patron patron, final String secretKey) {
    if (StringUtils.isBlank(secretKey)) {
      throw new MissingSecretKeyRuntimeException();
    }
    Signature signature = new Signature(getSignatureItems(ehubConsumerId, patron), secretKey);
    return signature.toString();
  }

  private static Map<String, String> parseAuthorizationHeader(String value) {
    final Map<String, String> parameterMap = new HashMap<>();
    final String[] parameters = value.split("\\s*,\\s*");

    for (String parameter : parameters) {
      Matcher matcher = PARAMETER_PATTERN.matcher(parameter);
      putParameterToMap(parameterMap, matcher);
    }
    return parameterMap;
  }

  private static void putParameterToMap(final Map<String, String> parameterMap, Matcher matcher) {
    if (matcher.matches()) {
      final String name = decode(matcher.group(FIRST_GROUP));
      final String value = decode(matcher.group(SECOND_GROUP));
      parameterMap.put(name, value);
    }
  }

  private static long getEhubConsumerId(final Map<String, String> authHeaderParams) {
    final String ehubConsumerId = authHeaderParams.get(EHUB_CONSUMER_ID);
    if (StringUtils.isBlank(ehubConsumerId)) {
      throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
    }
    return Long.parseLong(ehubConsumerId);
  }

  private static String getPatronId(final Map<String, String> authHeaderParams) {
    return authHeaderParams.get(EHUB_PATRON_ID);
  }

  private static String getLibraryCard(final Map<String, String> authHeaderParams) {
    return authHeaderParams.get(EHUB_LIBRARY_CARD);
  }

  private static String getPin(final Map<String, String> authHeaderParams) {
    return authHeaderParams.get(EHUB_PIN);
  }

  private static String getEmail(final Map<String, String> authHeaderParams) {
    return authHeaderParams.get(EHUB_EMAIL);
  }

  private static String getActualSignature(final Map<String, String> authHeaderParams) {
    final String base64EncodedSignature = authHeaderParams.get(EHUB_SIGNATURE);
    if (StringUtils.isBlank(base64EncodedSignature)) {
      throw new InvalidAuthorizationHeaderSignatureRuntimeException();
    }
    return base64EncodedSignature;
  }

  static List<String> getSignatureItems(final Long ehubConsumerId, final Patron patron) {
    List<String> signatureItems = new ArrayList<>();
    if (ehubConsumerId != null) {
      signatureItems.add(String.valueOf(ehubConsumerId));
    }
    if (patron != null) {
      if (patron.hasId()) {
        signatureItems.add(patron.getId());
      }
      if (patron.hasLibraryCard()) {
        signatureItems.add(patron.getLibraryCard());
      }
      if (patron.hasPin()) {
        signatureItems.add(patron.getPin());
      }
      if (patron.hasEmail()) {
        signatureItems.add(patron.getEmail());
      }
    }
    return signatureItems;
  }

  //TODO: Remove when all Arena installations are upgraded
  @Deprecated
  static List<String> getSignatureCompatibilityItems(final Long ehubConsumerId, final Patron patron) {
    List<String> signatureItems = new ArrayList<>();
    if (ehubConsumerId != null) {
      signatureItems.add(String.valueOf(ehubConsumerId));
    }
    if (patron != null) {
      if (patron.hasLibraryCard()) {
        signatureItems.add(patron.getLibraryCard());
      }
      if (patron.hasPin()) {
        signatureItems.add(patron.getPin());
      }
      if (patron.hasEmail()) {
        signatureItems.add(patron.getEmail());
      }
    }
    return signatureItems;
  }

  private static Patron getPatron(final Map<String, String> authHeaderParams) {
    final String patronId = getPatronId(authHeaderParams);
    final String libraryCard = getLibraryCard(authHeaderParams);
    final String pin = getPin(authHeaderParams);
    final String email = getEmail(authHeaderParams);
    return new Patron.Builder().id(patronId).libraryCard(libraryCard).pin(pin).email(email).build();
  }

  public AuthInfo parse(final String value) {
    if (StringUtils.isBlank(value)) {
      throw new MissingOrUnparseableAuthorizationHeaderRuntimeException();
    }
    final Map<String, String> authHeaderParams = parseAuthorizationHeader(value);
    Long ehubConsumerId = getEhubConsumerId(authHeaderParams);
    Patron patron = getPatron(authHeaderParams);
    final String actualSignature = getActualSignature(authHeaderParams);
    AuthInfo authInfo = new AuthInfo.Builder().ehubConsumerId(ehubConsumerId).patron(patron).build();
    if (authHeaderSecretKeyResolver.isValidate()) {
      final String secretKey = authHeaderSecretKeyResolver.getSecretKey(authInfo);

      final Signature expectedSignature = new Signature(getSignatureItems(ehubConsumerId, patron), secretKey);

      //TODO: Remove when all Arena installations are upgraded
      final Signature expectedCompatibilitySignature = new Signature(getSignatureCompatibilityItems(ehubConsumerId, patron), secretKey);

      if (!expectedSignature.isValid(actualSignature) && !expectedCompatibilitySignature.isValid(actualSignature)) {
        throw new InvalidAuthorizationHeaderSignatureRuntimeException();
      }
    }
    return authInfo;
  }

  @Override
  public String serialize(final AuthInfo authInfo) {
    final String secretKey = authHeaderSecretKeyResolver.getSecretKey(authInfo);
    final StringJoiner claimJoiner = new StringJoiner(", ");
    BiConsumer<String, String> s = (c, v) -> claimJoiner.add(c + "=\"" + authInfoEncode(v) + "\"");
    Long eHubConsumerId = authInfo.getEhubConsumerId();
    if (Objects.nonNull(eHubConsumerId)) {
      s.accept(EHUB_CONSUMER_ID, String.valueOf(eHubConsumerId));
    }
    Patron patron = authInfo.getPatron();
    if (Objects.nonNull(patron)) {
      if (patron.hasId()) s.accept(EHUB_PATRON_ID, patron.getId());
      if (patron.hasLibraryCard()) s.accept(EHUB_LIBRARY_CARD, patron.getLibraryCard());
      if (patron.hasPin()) s.accept(EHUB_PIN, patron.getPin());
      if (patron.hasEmail()) s.accept(EHUB_EMAIL, patron.getEmail());
      if (patron.hasBirthDate()) s.accept(EHUB_BIRTH_DATE, patron.getBirthDate().format(DateTimeFormatter.ISO_DATE));
    }
    s.accept(EHUB_SIGNATURE, getSignature(eHubConsumerId, patron, secretKey));
    return claimJoiner.toString();
  }

//  @Required
//  public void setAuthHeaderSecretKeyResolver(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
//    this.authHeaderSecretKeyResolver = authHeaderSecretKeyResolver;
//  }
}