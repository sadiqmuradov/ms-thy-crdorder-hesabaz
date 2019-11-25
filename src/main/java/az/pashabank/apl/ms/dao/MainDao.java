package az.pashabank.apl.ms.dao;

import az.pashabank.apl.ms.enums.ResultCode;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MainDao implements IMainDao {

    private static final MainLogger LOGGER = MainLogger.getLogger(MainDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String registerPayment(Payment payment) {
        String tranId = null;
        try {
            SimpleJdbcCall call =
                    new SimpleJdbcCall(jdbcTemplate)
                            .withCatalogName("pkg_payments")
                            .withFunctionName("register_transaction_hesabaz")
                            .declareParameters(
                                    new SqlParameter("p_client_id", Types.INTEGER),
                                    new SqlParameter("p_ecomm_trans", Types.VARCHAR),
                                    new SqlParameter("p_amount", Types.NUMERIC),
                                    new SqlParameter("p_currency", Types.VARCHAR),
                                    new SqlParameter("p_ip_address", Types.VARCHAR),
                                    new SqlParameter("p_description", Types.VARCHAR),
                                    new SqlParameter("p_lang", Types.VARCHAR),
                                    new SqlParameter("p_app_id", Types.INTEGER),
                                    new SqlOutParameter("return", Types.VARCHAR)
                            );

            SqlParameterSource in =
                    new MapSqlParameterSource()
                            .addValue("p_client_id", payment.getClientId())
                            .addValue("p_ecomm_trans", payment.getEcommTrans())
                            .addValue("p_amount", payment.getAmount())
                            .addValue("p_currency", payment.getCurrency())
                            .addValue("p_ip_address", payment.getIpAddress())
                            .addValue("p_description", payment.getDescription())
                            .addValue("p_lang", payment.getLang())
                            .addValue("p_app_id", payment.getAppId())
                    ;

            tranId = call.executeFunction(String.class, in);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return tranId;
    }

    @Override
    public void updatePaymentStatus(Payment payment, boolean isSuccessful, String description) {
        try {
            SimpleJdbcCall call =
                    new SimpleJdbcCall(jdbcTemplate)
                            .withCatalogName("pkg_payments")
                            .withProcedureName("update_transaction_status_hesabaz")
                            .declareParameters(
                                    new SqlParameter("p_transaction_id", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_trans", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_result", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_result_ps", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_result_code", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_3dsecure", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_rrn", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_approval_code", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_card_number", Types.VARCHAR),
                                    new SqlParameter("p_ecomm_aav", Types.VARCHAR),
                                    new SqlParameter("p_success", Types.SMALLINT),
                                    new SqlParameter("p_payment_desc", Types.VARCHAR)
                            );

            SqlParameterSource in =
                    new MapSqlParameterSource()
                            .addValue("p_transaction_id", payment.getTransactionId())
                            .addValue("p_ecomm_trans", payment.getEcommTrans())
                            .addValue("p_ecomm_result", payment.getEcommResult())
                            .addValue("p_ecomm_result_ps", payment.getEcommResultPs())
                            .addValue("p_ecomm_result_code", payment.getEcommResultCode())
                            .addValue("p_ecomm_3dsecure", payment.getEcomm3dSecure())
                            .addValue("p_ecomm_rrn", payment.getEcommRrn())
                            .addValue("p_ecomm_approval_code", payment.getEcommApprovalCode())
                            .addValue("p_ecomm_card_number", payment.getEcommCardNumber())
                            .addValue("p_ecomm_aav", payment.getEcommAav())
                            .addValue("p_success", isSuccessful ? 1 : 0)
                            .addValue("p_payment_desc", description)
                    ;

            call.execute(in);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean markApplicationAsPaid(int appId) {
        return markPaidOrSent("PAID", appId);
    }

    @Override
    public boolean markApplicationAsSent(int appId) {
        return markPaidOrSent("SENT", appId);
    }

    private boolean markPaidOrSent(String markType, int appId) {
        boolean result = false;
        StringBuilder procedureNameBuilder = new StringBuilder("mark_application_hesabaz_as_");
        if (markType.equals("PAID")) {
            procedureNameBuilder.append("paid");
        } else {
            procedureNameBuilder.append("sent");
        }
        try {
            SimpleJdbcCall call =
                    new SimpleJdbcCall(jdbcTemplate)
                            .withCatalogName("pkg_thy_applications")
                            .withProcedureName(procedureNameBuilder.toString())
                            .declareParameters(
                                    new SqlParameter("p_app_id", Types.INTEGER)
                            );

            SqlParameterSource in =
                    new MapSqlParameterSource()
                            .addValue("p_app_id", appId);

            call.execute(in);
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public String getValueFromEmailConfig(String key) {
        String sqlText = "SELECT value FROM thy_email_template_config WHERE key = ?";
        try {
            return jdbcTemplate.queryForObject(sqlText, String.class, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<String> getActiveMails() {
        List<String> list = new ArrayList<>();
        String sqlText = "SELECT * FROM v_thy_active_mails_hesabaz";
        try {
            list = jdbcTemplate.query(sqlText, (rs, rowNum) -> rs.getString("mail"));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public OperationResponse<String> makePaymentToFlex(int paymentId) {
        OperationResponse operationResponse = new OperationResponse(ResultCode.ERROR);
        try {
            SimpleJdbcCall call =
                    new SimpleJdbcCall(jdbcTemplate)
                            .withCatalogName("flex_payments")
                            .withProcedureName("make_flex_payment_hesabaz")
                            .declareParameters(
                                    new SqlParameter("p_payment_id", Types.INTEGER),
                                    new SqlOutParameter("p_res", Types.SMALLINT),
                                    new SqlOutParameter("p_trn_ref_no", Types.VARCHAR),
                                    new SqlOutParameter("p_error_code", Types.VARCHAR)
                            );

            SqlParameterSource in =
                    new MapSqlParameterSource()
                            .addValue("p_payment_id", paymentId);

            Map<String, Object> execute = call.execute(in);

            Short res = execute.get("p_res") != null ? (Short) execute.get("p_res") : 0;
            String trnRefNo = execute.get("p_trn_ref_no") != null ? (String) execute.get("p_trn_ref_no") : "";
            String errorCode = execute.get("p_error_code") != null ? (String) execute.get("p_error_code") : "";

            if (res == 1) {
                LOGGER.info("makePaymentToFlex. Flex payment is successful. paymentId: {}, res: {}", paymentId, res);
                operationResponse.setCode(ResultCode.OK);
                operationResponse.setData(trnRefNo);
            } else {
                LOGGER.error("makePaymentToFlex. Input - paymentId: {}, Output - res: {}, errorCode: {}", paymentId, res, errorCode);
                operationResponse.setData(errorCode);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return operationResponse;
    }

}
