package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.ShippingInvoiceMapper;
import org.jeecg.modules.business.service.IShippingInvoiceService;
import org.jeecg.modules.business.vo.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@Service
@Slf4j
public class ShippingInvoiceServiceImpl extends ServiceImpl<ShippingInvoiceMapper, ShippingInvoice> implements IShippingInvoiceService {

    @Autowired
    private ShippingInvoiceMapper shippingInvoiceMapper;
    @Getter
    private static enum EXTENSION {
        XLSX(".xlsx"),
        XLS(".xls"),
        CSV(".csv"),
        PDF(".pdf"),
        JPG(".jpg"),
        PNG(".png"),
        JPEG(".jpeg");
        private final String extension;

        EXTENSION(String extension) {
            this.extension = extension;
        }
    }
    @Value("${jeecg.path.purchaseInvoiceDir}")
    private String PURCHASE_INVOICE_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDir}")
    private String SHIPPING_INVOICE_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String INVOICE_DETAIL_LOCATION;
    @Value("${jeecg.path.shippingInvoicePdfDir}")
    private String INVOICE_PDF_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDetailPdfDir}")
    private String INVOICE_DETAIL_PDF_LOCAION;
    @Value("${jeecg.path.upload}")
    private String UPLOAD_DIR;
    @Autowired
    public ShippingInvoiceServiceImpl(ShippingInvoiceMapper shippingInvoiceMapper) {
        this.shippingInvoiceMapper = shippingInvoiceMapper;
    }

    @Override
    @Transactional
    public void saveMain(ShippingInvoice shippingInvoice) {
        shippingInvoiceMapper.insert(shippingInvoice);
    }

    @Override
    @Transactional
    public void updateMain(ShippingInvoice shippingInvoice) {
        shippingInvoiceMapper.updateById(shippingInvoice);

        //1.先删除子表数据

        //2.子表数据重新插入
    }

    @Override
    @Transactional
    public void delMain(String id) {
        shippingInvoiceMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            shippingInvoiceMapper.deleteById(id);
        }
    }

    @Override
    @Transactional
    public Client getShopOwnerFromInvoiceNumber(String invoiceNumber) {
        return shippingInvoiceMapper.fetchShopOwnerFromInvoiceNumber(invoiceNumber);
    }

    @Override
    public Currency getInvoiceCurrencyByCode(String invoiceCode) {
        return shippingInvoiceMapper.fetchInvoiceCurrencyByCode(invoiceCode);
    }

    @Override
    @Transactional
    public String getShippingInvoiceId(String invoiceNumber) {
        return shippingInvoiceMapper.fetchShippingInvoiceId(invoiceNumber);
    }
    @Override
    @Transactional
    public ShippingInvoice getShippingInvoice(String invoiceNumber) {
        return shippingInvoiceMapper.fetchShippingInvoice(invoiceNumber);
    }

    @Override
    @Transactional
    public List<PlatformOrder> getPlatformOrder(String invoiceNumber) {
        return shippingInvoiceMapper.fetchPlatformOrder(invoiceNumber);
    }

    @Override
    @Transactional
    public List<PlatformOrderContent> getPlatformOrderContent(String platformOrderId) {
        return shippingInvoiceMapper.fetchPlatformOrderContent(platformOrderId);
    }

    /** Finds the absolute path of invoice file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param invoiceNumber
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getPath(String dirPath, String invoiceNumber) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(invoiceNumber))
                    .filter(path -> path.getFileName().toString().endsWith(EXTENSION.XLSX.getExtension()))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /** Finds the absolute path of invoice file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param invoiceNumber
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getPath(String dirPath, String invoiceNumber, String invoiceEntity) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(invoiceNumber))
                    .filter(path -> path.getFileName().toString().contains(invoiceEntity))
                    .filter(path -> path.getFileName().toString().endsWith(EXTENSION.XLSX.getExtension()))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }
    /** Finds the absolute path of any file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param filename
     * @param extension
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getAttachementPath(String dirPath, String filename, String extension) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(filename))
                    .filter(path -> path.getFileName().toString().endsWith(extension))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    @Override
    public boolean deleteAttachmentFile(String filepath) {
        String filename = filepath.substring(filepath.lastIndexOf("/")+1, filepath.lastIndexOf("."));
        System.out.println(filename);
        String extension = filepath.substring(filepath.lastIndexOf("."));

        List<Path> attachmentPathList = getAttachementPath(UPLOAD_DIR, filename, extension);
        boolean isFileDeleted = false;

        if(attachmentPathList.isEmpty()) {
            log.error("FILE NOT FOUND : " + filepath);
        } else {
            for (Path path : attachmentPathList) {
                log.info(path.toString());
            }
            try {
                File attachmentFile = new File(attachmentPathList.get(0).toString());
                if(attachmentFile.delete()) {
                    log.info("Attachment file {} delete successful.", attachmentPathList.get(0).toString());
                    isFileDeleted = true;
                } else {
                    log.error("Attachment file delete fail.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isFileDeleted;
    }

    @Override
    public void setPaid(List<String> invoiceNumbers) {
        shippingInvoiceMapper.setPaid(invoiceNumbers);
    }

    @Override
    public Period getInvoicePeriod(List<String> shopIdList) {
        return shippingInvoiceMapper.getInvoicePeriod(shopIdList);
    }

    @Override
    public void cancelInvoice(String invoiceId) {
        shippingInvoiceMapper.cancelInvoice(invoiceId);
    }
}
