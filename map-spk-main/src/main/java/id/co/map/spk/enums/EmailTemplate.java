package id.co.map.spk.enums;

public enum EmailTemplate {

    SUBMITTED_SPK("spk-submitted-email-template.ftl"),
    CANCELED_SPK("spk-canceled-email-template.ftl"),
    REJECTED_SPK("spk-rejected-email-template.ftl"),
    DONE_APPROVED_SPK("spk-done-approved-email-template.ftl"),
    APPROVED_SPK("spk-approved-email-template.ftl"),
    IO_CREATED_SPK("spk-io-created-email-template.ftl"),
    REQUEST_CLOSE_SPK("spk-request-close-email-template.ftl"),
    APPROVED_CLOSE_SPK("spk-close-approved-email-template.ftl"),
    CLOSED_SPK("spk-closed-email-template.ftl"),
    CLOSED_FAILED("spk-close-failed-email-template.ftl"),
    FORGOT_PASSWORD("forgot-account-or-password-email-template.ftl"),

    NEW_USER("new-user-email-template.ftl");

    private String message;

    EmailTemplate(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}
