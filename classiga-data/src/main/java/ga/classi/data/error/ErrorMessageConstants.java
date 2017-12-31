package ga.classi.data.error;

public interface ErrorMessageConstants {

    // Validation
    String REQUIRED_PARAMETERS_NOT_FOUND            = "error.commondata.validation.RequiredParametersNotFound";
    String REQUIRED_PARAMETER_NOT_FOUND             = "error.commondata.validation.RequiredParameterNotFound";
    String INVALID_EMAIL_ADDRESS                    = "error.commondata.validation.InvalidEmailAddress";
    String INVALID_NUMERIC                          = "error.commondata.validation.InvalidNumeric";            // Invalid numeric {0} for data {1}
    String EMPTY_USERNAME                           = "error.commondata.validation.EmptyUsername";
    String USERNAME_TOO_SHORT                       = "error.commondata.validation.UsernameTooShort";
    String INVALID_YES_NO                           = "error.commondata.validation.InvalidYesNo";              // Invalid value {0} for data {1}
    String INVALID_JSON_ARRAY                       = "error.commondata.validation.InvalidJSONArray";
    String INVALID_JSON_OBJECT                      = "error.commondata.validation.InvalidJSONObject";
    String EMPTY_VALUE                              = "error.commondata.validation.EmptyValue";                // The value of {0} cannot be empty

    // User Group
    String CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS = "error.commondata.CantRemoveUserGroupCauseUserExists";
    String USER_GROUP_NOT_FOUND                     = "error.commondata.UserGroupNotFound";
    String USER_GROUP_ALREADY_EXISTS                = "error.commondata.UserGroupAlreadyExists";

    // System
    String SYSTEM_NOT_FOUND                         = "error.commondata.SystemNotFound";

    // User
    String USER_ALREADY_EXISTS                      = "error.commondata.UserAlreadyExists";
    String USER_ALREADY_EXISTS_WITH_EMAIL           = "error.commondata.UserAlreadyExistsWithEmail";
    String USER_ALREADY_EXISTS_WITH_USERNAME        = "error.commondata.UserAlreadyExistsWithUsername";
    String USER_NOT_FOUND                           = "error.commondata.UserNotFound";
    String CANT_LOGIN_CAUSE_USER_NOT_ACTIVE         = "error.commonrest.CantLoginCauseUserNotActive";
    String CANT_LOGIN_CAUSE_USER_GROUP_NOT_ACTIVE   = "error.commonrest.CantLoginCauseUserGroupNotActive";

    // User Group Menu Permission
    String USER_GROUP_MENU_PERMISSION_NOT_FOUND     = "error.commondata.UserGroupMenuPermissionNotFound";

}
