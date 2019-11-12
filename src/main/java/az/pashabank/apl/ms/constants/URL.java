package az.pashabank.apl.ms.constants;

/**
 * URLs for restful API
 */
public final class URL {

    // Web Controller
    public static final String EMPTY = "";
    public static final String SLASH = "/";
    public static final String NEW_ORDER = "/newOrder";

    public static final String INDEX = "/index";
    public static final String ECOMM_FAIL = "/ecomm/fail";
    public static final String ECOMM_OK = "/ecomm/ok";
    public static final String LOGOUT = "/logout";

    // Rest API Controller
    public static final String POST_CHECK_PAYMENT_STATUS = "/checkPaymentStatus/{lang}";
    public static final String POST_CREATE_NEW_COUPON_ORDER_STEP1 = "/newCouponOrder/step1";
    public static final String POST_CREATE_NEW_COUPON_ORDER_STEP2 = "/newCouponOrder/step2/{lang}";
    public static final String POST_CREATE_NEW_COUPON_ORDER_STEP3 = "/newCouponOrder/step3/{lang}";
    public static final String POST_CHECK_COUPON_PAYMENT_STATUS = "/checkCouponPaymentStatus/{lang}";
    public static final String GET_CRS_QUESTIONS = "/crsQuestions/{lang}";
    public static final String GET_BRANCH_LIST = "/getBranchList/{lang}";
    public static final String GET_CARD_PRODUCTS = "/getCardProductList/{lang}";
    public static final String GET_CITY_LIST = "/getCity/{contryCode}";
    public static final String GET_COUNTRY_LIST = "/getCountryList/{lang}";
    public static final String REFRESH_COUNTRY_LIST = "/refreshCountryList/{lang}";
    public static final String GET_SECURITY_QUESTION_LIST = "/securityQuestions/{lang}";
    public static final String POST_LOGIN = "/signin";
    public static final String POST_VALIDATE_COUPON_CODE = "/validateCouponCode/{lang}";

    public static final String ECOMM_REGISTER_PAYMENT = "/registerPayment/";
    public static final String ECOMM_GET_PAYMENT_STATUS = "/getPaymentStatus/{lang}/";
    public static final String THY_POST_GET_MEMBER_DETAILS = "/getMemberDetails/";
    public static final String THY_POST_MEMBER_OPERATIONS = "/memberOperations/";
    public static final String THY_POST_GET_SPECIFIC_COMBOBOX = "/getSpecificComboBox/";
    public static final String MS_SMS_SEND_SMS = "/sendSms/";

    private URL() {
    }

}
