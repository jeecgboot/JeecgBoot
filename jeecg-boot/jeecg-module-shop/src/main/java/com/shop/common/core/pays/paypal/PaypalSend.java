package com.shop.common.core.pays.paypal;

import com.alibaba.fastjson.JSON;
import com.shop.common.core.pays.paypal.config.PaypalPaymentIntent;
import com.shop.common.core.pays.paypal.config.PaypalPaymentMethod;
import com.shop.entity.Pays;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：用于创建 paypal 国际支付收款
 * 调用 createPayment 方法即可发起支付
 * 作者：Panyoujie
 * 时间：2021-08-24
 */
public class PaypalSend {

    private static final String mode = "live"; // live （正式环境）  sandbox （测试环境）

    public static Payment createPayment(Pays pays,
                                        String total,
                                        String currency,
                                        PaypalPaymentMethod method,
                                        PaypalPaymentIntent intent,
                                        String description) throws PayPalRESTException {

        Map mapTypes = JSON.parseObject(pays.getConfig());
        String clientId = mapTypes.get("clientId").toString();
        String clientSecret = mapTypes.get("clientSecret").toString();
        String successUrl = mapTypes.get("return_url").toString() + "/paypal/success";
        String cancelUrl = mapTypes.get("return_url").toString() + "/paypal/cancel";

        APIContext apiContext = apiContext(clientId, clientSecret, mode);

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(total);

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);
        apiContext.setMaskRequestId(true);
        return payment.create(apiContext);
    }

    public static Payment executePayment(String clientId, String clientSecret, String paymentId, String payerId) throws PayPalRESTException {
        APIContext apiContext = apiContext(clientId, clientSecret, mode);
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public static Map<String, String> paypalSdkConfig(String mode) {
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", mode);
        return sdkConfig;
    }

    public static OAuthTokenCredential authTokenCredential(String clientId, String clientSecret, String mode) {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig(mode));
    }

    public static APIContext apiContext(String clientId, String clientSecret, String mode) throws PayPalRESTException {
        APIContext apiContext = new APIContext(authTokenCredential(clientId, clientSecret, mode).getAccessToken());
        apiContext.setConfigurationMap(paypalSdkConfig(mode));
        return apiContext;
    }

}
