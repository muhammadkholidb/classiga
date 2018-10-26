/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.data.error;

public enum Errors {

    // Validation 
    REQUIRED_PARAMETERS_NOT_FOUND("0001", "error.commondata.validation.RequiredParametersNotFound"),
    REQUIRED_PARAMETER_NOT_FOUND("0002", "error.commondata.validation.RequiredParameterNotFound"),
    INVALID_EMAIL_ADDRESS("0003", "error.commondata.validation.InvalidEmailAddress"),
    INVALID_NUMBER("0004", "error.commondata.validation.InvalidNumeric"),
    EMPTY_USERNAME("0005", "error.commondata.validation.EmptyUsername"),
    USERNAME_TOO_SHORT("0006", "error.commondata.validation.UsernameTooShort"),
    INVALID_YES_NO("0007", "error.commondata.validation.InvalidYesNo"),
    INVALID_JSON_ARRAY("0008", "error.commondata.validation.InvalidJSONArray"),
    INVALID_JSON_OBJECT("0009", "error.commondata.validation.InvalidJSONObject"),
    INVALID_DATE("0010", "error.commondata.validation.InvalidDate"),
    EMPTY_VALUE("0011", "error.commondata.validation.EmptyValue"),
    VALUES_NOT_EQUALS("0012", "error.commondata.validation.ValuesNotEquals"),
    // User group
    CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS("0013", "error.commondata.CantRemoveUserGroupCauseUserExists"),
    USER_GROUP_NOT_FOUND("0014", "error.commondata.UserGroupNotFound"),
    USER_GROUP_ALREADY_EXISTS("0015", "error.commondata.UserGroupAlreadyExists"),
    // System
    SYSTEM_NOT_FOUND("0016", "error.commondata.SystemNotFound"),
    // User
    USER_ALREADY_EXISTS("0017", "error.commondata.UserAlreadyExists"),
    USER_ALREADY_EXISTS_WITH_EMAIL("0018", "error.commondata.UserAlreadyExistsWithEmail"),
    USER_ALREADY_EXISTS_WITH_USERNAME("0019", "error.commondata.UserAlreadyExistsWithUsername"),
    USER_NOT_FOUND("0020", "error.commondata.UserNotFound"),
    INVALID_OLD_PASSWORD("0021", "error.commondata.InvalidOldPassword"),
    CANT_LOGIN_CAUSE_USER_NOT_ACTIVE("0022", "error.commonrest.CantLoginCauseUserNotActive"),
    CANT_LOGIN_CAUSE_USER_GROUP_NOT_ACTIVE("0023", "error.commonrest.CantLoginCauseUserGroupNotActive"),
    // Menu permission
    USER_GROUP_MENU_PERMISSION_NOT_FOUND("0024", "error.commondata.UserGroupMenuPermissionNotFound"),
    // Other
    UNKNOWN("9999", "error.unknown");

    private final String code;
    private final String messageCode;

    private Errors(String code, String messageCode) {
        this.code = code;
        this.messageCode = messageCode;
    }

    public String code() {
        return this.code;
    }
    
    public String messageCode() {
        return this.messageCode;
    }
    
}
