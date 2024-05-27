package id.co.map.spk.enums;

public enum ClientResponseStatus {

    //SPK Status
    SPK_ADDED(1000, "SPK Created."),
    SPK_UPDATED(10001, "SPK Updated."),
    SPK_STATUS_UPDATED(1001, "Successfully update SPK."),
    INTERNAL_ORDER_CREATED(1002, "Internal order creation successful."),
    PURCHASE_ORDER_CREATED(1003, "Purchase Order Creation Successful"),
    ENTRY_SHEET_CREATED(1004, "Payment is Confirmed."),
    COMPLETED(1005, "SPK is Completed."),
    SPK_NEED_TO_CLOSE(1006, "All Services is confirmed but Total Amount is not equals to SPK Amount"),
    SPK_ASSET_UPDATED(1007, "Asset class is changed."),
    INTERNAL_ORDER_CLOSED_SUCCESS(1008, "Internal Order is Closed"),

    SPK_ALREADY_CANCELED( 4001,"This SPK has been canceled."),
    SPK_ALREADY_REJECTED( 4002,"This SPK has been rejected."),
    SPK_ERROR_CANCELED( 4003,"Sorry. SPK can not be canceled..."),
    INTERNAL_ORDER_FAILED(4004, "Internal order creation failed."),
    INTERNAL_ORDER_CREATION_NOT_ALLOWED(4005, "SPK status should VERIFIED to create Internal Order."),
    PURCHASE_ORDER_FAILED(4006, "Purchase order creation is failed."),
    PURCHASE_ORDER_CREATION_NOT_ALLOWED(4007, "SPK status should INTERNAL ORDER CREATED to create Internal Order."),
    ENTRY_SHEET_UNABLE_CREATE(4008, "Unable to create Entry Sheet"),
    ENTRY_SHEET_FAILED(4009, "Entry Sheet creation is failed."),
    REQUEST_CLOSE_UNABLE(4010, "Could not close this SPK."),
    INTERNAL_ORDER_CLOSED_FAILED(4011, "Close Internal Order is Failed"),
    SPK_HAS_APPROVED(4012, "SPK Has Been Approved"),
    // end of SPK Status

    //user status
    USER_ADDED(1010, "Success add user."),
    USER_PASSWORD_UPDATED(1011, "Successfully update password"),
    USER_ACTIVATED(1012, "User is activated"),
    USER_AD_ACTIVATED(10121, "User AD is activated"),
    USER_DELETED(1013, "User has been deleted"),
    USER_EMAIL_CHANGED(1014, "User email is successfully changed"),
    USER_ROLE_UPDATE(1015, "User role is successfully changed"),
    USER_COMPANY_DELETED(1016, "Company is removed from this user"),
    USER_COMPANY_ADDED(1017, "Success added companies to user"),
    USER_SBU_ADDED(1018, "Success added sbus to user"),
    USER_SBU_DELETED(1019, "Sbu is removed from this user"),

    USER_INVALID_CURRENT_PASSWORD(4020, "Invalid current password!"),
    USER_NEW_PASSWORD_NOT_MATCH(4021, "Password did not match!"),
    USER_INVALID_REGISTRATION_KEY(4022, "Invalid Registration Key"),
    USER_NOT_FOUND(4024, "User not found."),
    USER_AD_NOT_FOUND(40241, "User AD not found."),
    USER_INACTIVE(4025, "You have not activate your account."),
    //end of user status

    //Company
    COMPANY_ADDED(1020, "Success add company."),
    COMPANY_NAME_CHANGED(1021, "Company Name is successfully changed"),
    COMPANY_NPWP_CHANGED(1022, "NPWP is successfully changed"),
    COMPANY_ADD1_CHANGED(1023, "Address 1 is successfully changed"),
    COMPANY_ADD2_CHANGED(1024, "Address 2 is successfully changed"),
    COMPANY_COA_CHANGED(1025, "COA is successfully changed"),
    COMPANY_HEADIMG_CHANGED(1026, "Header Image is successfully changed"),
    COMPANY_COUNTRY_CHANGED(1027, "Country is successfully changed"),
    //end of company status

    //forgot password
    FP_CREATED(1030, "Please check your inbox. Forgot password link will be there..."),
    FP_UPDATED(1031, "Password have been changed."),

    //Sbu
    SBU_ADDED(1040, "Success add sbu."),
    SBU_DELETED(1041, "Success remove sbu."),
    SBU_RULE_CHANGED(1042, "Approval Rule is successfully changed"),
    SBU_CODEDESC_CHANGED(1043, "Sbu Code / Desc is successfully changed"),
    //end of sbu status

    //Approval Rules
    APPROVAL_RULE_ADDED(1050, "Success add Approval Rule."),
    ROLE_DELETED(1051, "Approval Rule has been Deleted."),
    APPROVAL_RULE_UPDATE(1052, "Success Update Approval Rule."),
    //end of sbu status

    //Country
    COUNTRY_ADD(1060, "Success add country."),
    COUNTRY_DELETED(1061, "Success remove country."),
    COUNTRY_CODE_CHANGE(1062, "Success change country code."),
    COUNTRY_NAME_CHANGE(1063, "Success change country name."),
    COUNTRY_LABEL_CHANGE(1064, "Success change Label Print."),
    //end of country

    FP_INVALID_KEY(4001, "Invalid forgot password url.. :("),
    FP_REPLACED(4002, "Forgot password link have replaced with newest link. Please check your inbox..."),
    FP_LINK_COMPLETED(4003, "Forgot password link have been used."),
    // end of forgot password

    SERVER_ERROR(5000, "Something went wrong."),
    SQL_ERROR(5001, "SQL Server Error."),;

    private final int code;
    private final String message;

    ClientResponseStatus(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }
}
