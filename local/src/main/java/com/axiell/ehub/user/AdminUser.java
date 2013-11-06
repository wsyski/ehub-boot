/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import static com.axiell.ehub.security.HmacSHA1HashFunction.hmacSha1;
import static com.axiell.ehub.util.EhubCharsets.UTF_8;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.io.UnsupportedEncodingException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.InternalServerErrorException;

/**
 * Represents an Administration user in the eHUB.
 */
@Entity
@Table(name = "EHUB_ADMIN_USER")
@Access(AccessType.PROPERTY)
public class AdminUser extends AbstractTimestampAwarePersistable<Long> {
    private static final long serialVersionUID = -3349510792156153723L;
    private static final int SALT_LENGTH = 16;
    private String name;
    private String clearPassword;
    private String encodedPassword;
    private String salt;

    /**
     * Default constructor required by JPA.
     */
    protected AdminUser() {
    }

    /**
     * Constructs a new {@link AdminUser}.
     * 
     * @param name the name of the {@link AdminUser}
     * @param clearPassword the clear password of the {@link AdminUser}
     * @throws NullPointerException if the name or the clear password is null
     */
    AdminUser(String name, String clearPassword) {
        Validate.notNull(name, "The name can't be null");
        Validate.notNull(clearPassword, "The clear password can't be null");
        this.name = name;
        this.clearPassword = clearPassword;
        this.salt = RandomStringUtils.random(SALT_LENGTH, false, true);
        this.encodedPassword = encodePassword(clearPassword);
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    @Column(name = "NAME", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the clear password.
     * 
     * @return the clear password
     */
    @Transient
    public String getClearPassword() {
        return clearPassword;
    }

    /**
     * Sets the clear password.
     * 
     * @param clearPassword password the clear password to set
     */
    public void setClearPassword(String clearPassword) {
        this.clearPassword = clearPassword;
    }

    /**
     * Returns the encoded password.
     * 
     * @return the encoded password
     */
    @Column(name = "PASSWORD", unique = false, nullable = false)
    public String getEncodedPassword() {
        return encodedPassword;
    }

    /**
     * Sets the encoded password.
     * 
     * @param encodedPassword the encoded password to set
     */
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    /**
     * Returns the salt that is used when encoding the clear password.
     * 
     * @return the salt
     */
    @Column(name = "SALT", unique = false, nullable = false, length = SALT_LENGTH)
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the salt that is used when encoding the clear password.
     * 
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Indicates whether the provided clear password is valid. This is done by encoding the provided clear password and
     * then by comparing it to the encoded password of this {@link AdminUser}.
     * 
     * @param clearPassword the clear password to verify
     * @return <code>true</code> if and only if the clear password is valid, <code>false</code> otherwise
     */
    @Transient
    final boolean isValid(final String clearPassword) {
        final String tmpEncodedPassword = encodePassword(clearPassword);
        return getEncodedPassword().equals(tmpEncodedPassword);
    }

    /**
     * Encodes the clear password.
     * 
     * @param clearPassword the clear password to encode
     * @return the encoded password
     */
    private String encodePassword(final String clearPassword) {
        final String baseString = new StringBuilder().append(salt).append(clearPassword).toString();
        final byte[] input;
        final byte[] key;

        try {
            input = baseString.getBytes(UTF_8);
            key = name.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException("Could not get the bytes of strings in '" + UTF_8 + "' encoding", e);
        }

        byte[] digest = hmacSha1(input, key);
        return encodeBase64String(digest);
    }
}
