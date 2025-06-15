package com.zimaberlin.zimasocial.context.contentmoderation.user;

public class TrustScoreNotTooLowYet extends Exception {
    private final String code;
    private String message;
    public TrustScoreNotTooLowYet() {
        super("Trust score not too low yet");
        this.code = "trust_score_not_too_low_yer";
    }
}
