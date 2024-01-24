package com.techaccelarators.ifind.common.encryption;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.usertype.UserType;
import org.jasypt.hibernate5.type.EncryptedBigDecimalAsStringType;
import org.jasypt.hibernate5.type.EncryptedBigDecimalType;
import org.jasypt.hibernate5.type.EncryptedBigIntegerAsStringType;
import org.jasypt.hibernate5.type.EncryptedBigIntegerType;
import org.jasypt.hibernate5.type.EncryptedByteAsStringType;
import org.jasypt.hibernate5.type.EncryptedDoubleAsStringType;
import org.jasypt.hibernate5.type.EncryptedFloatAsStringType;
import org.jasypt.hibernate5.type.EncryptedIntegerAsStringType;
import org.jasypt.hibernate5.type.EncryptedLongAsStringType;
import org.jasypt.hibernate5.type.EncryptedShortAsStringType;
import org.jasypt.hibernate5.type.EncryptedStringType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisteredEncryptors {

    public static final String HIBERNATE_STRING_ENCRYPTOR = "hibernateStringEncryptor";

    public static final String HIBERNATE_BIG_DECIMAL_ENCRYPTOR = "hibernateBigDecimalEncryptor";

    public static final String HIBERNATE_BIG_INTEGER_ENCRYPTOR = "hibernateBigIntegerEncryptor";

    public static final String ENCRYPTED_STRING_TYPE = "encryptedString";

    public static final Class<? extends UserType> ENCRYPTED_STRING_TYPE_CLASS = EncryptedStringType.class;

    public static final String ENCRYPTED_DOUBLE_TYPE = "encryptedDouble";

    public static final Class<? extends UserType> ENCRYPTED_DOUBLE_TYPE_CLASS = EncryptedBigDecimalType.class;

    public static final String ENCRYPTED_LONG_TYPE = "encryptedLong";

    public static final Class<? extends UserType> ENCRYPTED_LONG_TYPE_CLASS = EncryptedBigIntegerType.class;

    public static final String ENCRYPTED_FLOAT_TYPE = "encryptedFloat";

    public static final Class<? extends UserType> ENCRYPTED_FLOAT_TYPE_CLASS = EncryptedBigDecimalType.class;

    public static final String ENCRYPTED_INTEGER_TYPE = "encryptedInteger";

    public static final Class<? extends UserType> ENCRYPTED_INTEGER_TYPE_CLASS = EncryptedBigIntegerType.class;

    public static final String ENCRYPTED_LONG_AS_STRING_TYPE = "encryptedLongAsString";

    public static final Class<? extends UserType> ENCRYPTED_LONG_AS_STRING_TYPE_CLASS = EncryptedLongAsStringType.class;

    public static final String ENCRYPTED_INTEGER_AS_STRING_TYPE = "encryptedIntegerAsString";

    public static final Class<? extends UserType> ENCRYPTED_INTEGER_AS_STRING_TYPE_CLASS = EncryptedIntegerAsStringType.class;

    public static final String ENCRYPTED_DOUBLE_AS_STRING_TYPE = "encryptedDoubleAsString";

    public static final Class<? extends UserType> ENCRYPTED_DOUBLE_AS_STRING_TYPE_CLASS = EncryptedDoubleAsStringType.class;

    public static final String ENCRYPTED_FLOAT_AS_STRING_TYPE = "encryptedFloatAsString";

    public static final Class<? extends UserType> ENCRYPTED_FLOAT_AS_STRING_TYPE_CLASS = EncryptedFloatAsStringType.class;

    public static final String ENCRYPTED_BYTE_AS_STRING_TYPE = "encryptedByteAsString";

    public static final Class<? extends UserType> ENCRYPTED_BYTE_AS_STRING_TYPE_CLASS = EncryptedByteAsStringType.class;

    public static final String ENCRYPTED_SHORT_AS_STRING_TYPE = "encryptedShortAsString";

    public static final Class<? extends UserType> ENCRYPTED_SHORT_AS_STRING_TYPE_CLASS = EncryptedShortAsStringType.class;

    public static final String ENCRYPTED_BIG_DECIMAL_AS_STRING_TYPE = "encryptedBigDecimalAsString";

    public static final Class<? extends UserType> ENCRYPTED_BIG_DECIMAL_AS_STRING_TYPE_CLASS = EncryptedBigDecimalAsStringType.class;

    public static final String ENCRYPTED_BIG_INTEGER_AS_STRING_TYPE = "encryptedBigIntegerAsString";

    public static final Class<? extends UserType> ENCRYPTED_BIG_INTEGER_AS_STRING_TYPE_CLASS = EncryptedBigIntegerAsStringType.class;


}
