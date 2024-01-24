package com.techaccelarators.ifind.commons.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techaccelarators.ifind.commons.encryption.RegisteredEncryptors;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.val;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate5.type.EncryptedStringType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class),
        @TypeDef(
                name = RegisteredEncryptors.ENCRYPTED_STRING_TYPE,
                typeClass = EncryptedStringType.class,
                parameters = {
                        @org.hibernate.annotations.Parameter(name = "encryptorRegisteredName", value = RegisteredEncryptors.HIBERNATE_STRING_ENCRYPTOR)
                }
        )
})
@MappedSuperclass
@Getter
public class BaseEntity implements Serializable {

    @NaturalId
    @Column(nullable = false, updatable = false, unique = true)
    private String uuid;

    @Version
    @JsonIgnore
    private Long version;

    @PrePersist
    void setupUuid() {
        val instant = Instant.now();
        long seconds = instant.getEpochSecond();
        long nano = instant.getNano();
        this.uuid = String.valueOf((seconds * 1_000_000_000) + nano);
    }

}

