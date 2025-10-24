package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.excel.SheetManager;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.SkuOrderPage;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class SendInventoryJob implements Job {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final static String[] PURCHASE_INVENTORY_TITLES = {
            "SKU",
            "Nom Anglais",
            "Achat WIA en cours",
            "Stock disponible",
            "Cmd en cours",
            "Stock " + (new SimpleDateFormat("dd/MM").format(new Date())),
            "Ventes 7j",
            "Ventes 28j",
            "Ventes 42j"
    };
    @Value("${jeecg.path.inventoryDir}")
    private String INVENTORY_DIR;

    @Autowired
    private IClientService clientService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started SendInventoryJob");
        List<String> clientCodes = new ArrayList<>();
        List<Client> clients = null;
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("clients")) {
                    JSONArray clientJsonArray = jsonObject.getJSONArray("clients");
                    for (int i = 0; i < clientJsonArray.length(); i++) {
                        clientCodes.add(clientJsonArray.getString(i));
                    }
                    if (clientCodes.isEmpty()) {
                        log.error("No client code given, aborting");
                        return;
                    }
                    clients = clientService.getClientsByCodes(clientCodes);
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        if (clients == null) {
            clients = clientService.getActiveClientsToReceiveInventory();
        } else {
            log.info("Client list overwritten by job parameters : {}", parameter);
        }
        log.info("{} clients to receive inventory", clients.size());

        for (Client client : clients) {
            String clientCode = client.getInternalCode();
            log.info("Current client {}", clientCode);
            String email = client.getEmail();
            if (!email.matches(EMAIL_REGEX)) {
                log.error("Client {} doesn't have a valid email {}, skipping", clientCode, email);
                continue;
            }
            List<SkuOrderPage> inventory = skuService.getInventoryByClientCode(clientCode);
            log.info("{} inventory size {}", clientCode, inventory.size());
            try {
                log.info("Starting exporting inventory to Excel file");
                Path path = exportInventoryToExcel(inventory, client, date);
                log.info("Finished exporting inventory to Excel file with success");
                sendInventoryByMail(client, path);

            } catch (IOException e) {
                log.error("Error while exporting inventory to Excel file : {}", e.getMessage());
            }
        }
        log.info("Finished SendInventoryJob");
    }

    private Path exportInventoryToExcel(List<SkuOrderPage> skuOrders, Client client, String date) throws IOException {
        SheetManager sheetManager = SheetManager.createInventoryXLSX();
        sheetManager.startInventorySheet();
        for (String title : PURCHASE_INVENTORY_TITLES) {
            sheetManager.write(title);
            sheetManager.nextCol();
        }
        sheetManager.moveCol(0);
        sheetManager.nextRow();

        for (SkuOrderPage skuPurchase : skuOrders) {
            sheetManager.write(skuPurchase.getErpCode());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getEnName());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getPurchasingAmount());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getAvailableAmount());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getQtyInOrdersNotShipped());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getStock());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSalesLastWeek());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSalesFourWeeks());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSalesSixWeeks());
            sheetManager.moveCol(0);
            sheetManager.nextRow();
        }

        String internalCode = client.getInternalCode();
        String invoiceEntity = client.getInvoiceEntity();
        Path target = Paths.get(INVENTORY_DIR, internalCode + "_(" + invoiceEntity + ")_" + date + "_Inventaire_SKU.xlsx");
        int i = 2;
        while (Files.exists(target)) {
            target = Paths.get(INVENTORY_DIR, internalCode + "_(" + invoiceEntity + ")_" + date + "_Inventaire_SKU_(" + i + ").xlsx");
            i++;
        }
        Path path = Files.createFile(target);
        sheetManager.export(target);
        sheetManager.getWorkbook().close();
        System.gc();
        return path;
    }

    private void sendInventoryByMail(Client client, Path path) {
        log.info("Sending inventory to client {}", client.getInternalCode());
        String subject = "Votre inventaire du jour";
        Properties prop = emailService.getMailSender();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("client", client);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("client/dailyInventory.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            emailService.sendMessageWithAttachment(client.getEmail(), subject, htmlBody, path.toString(), session);
            log.info("Inventory sent to client {} with success", client.getInternalCode());
        }
        catch(Exception e) {
            log.error("Failed to send inventory to client {}",e.getMessage());
        }
    }
}
