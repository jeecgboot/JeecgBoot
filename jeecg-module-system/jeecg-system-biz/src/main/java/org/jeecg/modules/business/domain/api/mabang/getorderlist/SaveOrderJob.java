package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.JSONArray;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.jeecg.modules.business.domain.remote.RemoteFileSystem;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveOrderJob implements Job {

    @Value("${jeecg.s3.upload.prefix}")
    private String dir;

    @Value("${jeecg.s3.key.access}")
    private String ACCESS_KEY;

    @Value("${jeecg.s3.key.secret}")
    private String SECRET_KEY;

    @Autowired
    @Setter
    private IPlatformOrderMabangService platformOrderMabangService;


    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<JSONArray> data = retrieveTodayOrderFromRemote();
        saveOrderToDB(data);

    }

    /**
     * Retrieves today's json files of array of orders to local.
     * Each file as a json array.
     *
     * @return List of json array
     * @throws IOException error when saving data
     */
    private List<JSONArray> retrieveTodayOrderFromRemote() throws IOException {
        // Prefix of the files to be retrieved
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String file_prefix = format.format(now);

        //Connect to Remote File System
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        RemoteFileSystem fs = new RemoteFileSystem(credentials, dir);

        // Names of all valid files
        List<String> validFileNames = fs.ls(file_prefix);

        // Beginning retrieval
        List<JSONArray> data = new ArrayList<>();
        Path tmp = Files.createTempFile("tmpJsonContainer", "json");
        for (String validFileName : validFileNames) {
            // download to a temporary file
            fs.wget(validFileName, tmp);
            // read the file, convert its content to json
            String jsonString = FileUtils.readFileToString(tmp.toFile(), "UTF-8");
            // parse it as json array
            JSONArray arrayOfOrder = JSONArray.parseArray(jsonString);
            data.add(arrayOfOrder);
        }
        return data;
    }

    /**
     * Save orders in json object format to DB.
     *
     * @param data array of json object of orders
     */
    private void saveOrderToDB(List<JSONArray> data) {
        for (JSONArray array : data) {
            List<Order> orders = array.toJavaList(Order.class);
            platformOrderMabangService.saveOrderFromMabang(orders);
        }
    }
}
