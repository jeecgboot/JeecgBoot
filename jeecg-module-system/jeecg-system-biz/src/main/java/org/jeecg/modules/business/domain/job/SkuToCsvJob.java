package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.ISkuService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class SkuToCsvJob implements Job {

    @Autowired
    private ISkuService skuService;

    @Value("${jeecg.path.skuCsvPath}")
    private String FILE_PATH;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("SkuToCsv Job is running ...");
        List<Sku> skus = new ArrayList();
        Sku firstLine = new Sku();
        firstLine.setId("#");
        firstLine.setErpCode("erp_code");
        firstLine.setImageSource("image_source");
        skus.add(firstLine);
        skus.addAll(skuService.listImgUrls());
        List<String> csvLines = skus.stream()
                .map(sku -> String.format("%s,%s,%s", sku.getId(), sku.getErpCode(), sku.getImageSource())).collect(Collectors.toList());
        File csvFile = new File(FILE_PATH);

        try (PrintWriter writer = new PrintWriter(csvFile)) {
            csvLines.forEach(writer::println);
        } catch (FileNotFoundException e) {
            log.error("Error writing to file", e);
        }
        log.info("SkuToCsv Job is done.");
    }
}
