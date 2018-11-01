/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@NoArgsConstructor
@Data
@MappedSuperclass
public abstract class BaseEntity {

    // Read http://www.baeldung.com/intro-to-project-lombok

    public static final String F_ID                 = "id";
    public static final String F_VERSION            = "version";
    public static final String F_CREATE_TIME_MILLIS = "createTimeMillis";
    public static final String F_UPDATE_TIME_MILLIS = "updateTimeMillis";
    public static final String F_DELETE_TIME_MILLIS = "deleteTimeMillis";
    public static final String F_DELETED            = "deleted";
    public static final String F_RH                 = "rh";
    public static final String F_SRH                = "srh";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Column(name = "rh", length = 40, nullable = false, unique = true)
    private String rh;
    
    @Column(name = "srh", length = 10, nullable = false, unique = true)
    private String srh;
    
    @JsonIgnore
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @JsonIgnore
    @Column(name = "create_time_millis", nullable = false)
    private Long createTimeMillis;

    @JsonIgnore
    @Column(name = "update_time_millis", nullable = false)
    private Long updateTimeMillis;

    @JsonIgnore
    @Column(name = "delete_time_millis")
    private Long deleteTimeMillis;

    @JsonIgnore
    @Column(name = "deleted", length = 1, nullable = false)
    private String deleted;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public void setDeleted() {
        this.deleted = CommonConstants.YES;
        this.deleteTimeMillis = System.currentTimeMillis();
    }
    
    public Boolean isDeleted() {
        return CommonConstants.YES.equals(this.deleted);
    }

    protected abstract void setValuesOnCreate();
    
    protected abstract void setValuesOnUpdate();
    
    protected abstract StringBuilder getHashElements();
    
    // Inspired by git commit id / hash, row hash (rh) uses SHA1 and it has short row hash (srh) which is the "n first characters" of the actual row hash
    // https://stackoverflow.com/questions/34764195/how-does-git-create-unique-commit-hashes-mainly-the-first-few-characters/34764586
    // https://stackoverflow.com/questions/18134627/how-much-of-a-git-sha-is-generally-considered-necessary-to-uniquely-identify-a
    
    private String getHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(this.version);
        sb.append(this.createTimeMillis);
        sb.append(this.updateTimeMillis);
        sb.append(this.deleted);
        sb.append(ObjectUtils.defaultIfNull(this.deleteTimeMillis, StringConstants.EMPTY));
        sb.append(getHashElements());
        sb.append(RandomStringUtils.random(32));    // RandomStringUtils.random() returns non letter characters such as: 锪椢獬ꃅ諔諔궏ꃅ뚱뇉여獬ﻄ蚹㙰ﻄ
        return DigestUtils.sha1Hex(sb.toString());
    }
    
    @PrePersist
    public void onCreate() {
        log.debug("Before create ..."); 
        updateTimeMillis = createTimeMillis = System.currentTimeMillis();
        deleted = CommonConstants.NO;
        rh = getHash();
        srh = rh.substring(0, 10);
        setValuesOnCreate();
    }

    @PreUpdate
    public void onUpdate() {
        log.debug("Before update ..."); 
        updateTimeMillis = System.currentTimeMillis();
        rh = getHash();
        srh = rh.substring(0, 10);
        setValuesOnUpdate();
    }
    
}
