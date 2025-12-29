package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.Client;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 客户
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
public interface IClientService extends IService<Client> {

	/**
	 * 添加一对多
	 *
	 */
	void saveMain(Client client,List<Shop> shopList,List<ClientSku> clientSkuList) ;

	/**
	 * 修改一对多
	 *
	 */
    void updateMain(Client client, List<Shop> shopList);

	/**
	 * 删除一对多
	 */
	void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	void delBatchMain (Collection<? extends Serializable> idList);
	String getClientEntity(String id);
	Map<String, String> getClientsEntity(List<String> ids);
	String getClientIdByCode(String code);
	String getActiveClientIdByCode(String code);
	/**
	 * Get current user's client information
	 * @return client or null if current user's role is not client
	 */
	Client getCurrentClient();

    List<Client> getClientsByType(String type);

    Client getClientFromOrder(String orderId);

    Client getClientFromPurchase(String purchaseId);

    List<String> getClientsFromPurchases(List<String> purchaseIds);

    Client getClientBySku(String skuId);

    Client getClientFromInvoice(String invoiceNumber);

    void anonymizePersonalData(int directClientAnonymizationPeriod);

	Client getByShopId(String shopId);

	Client getClientFromCredit(String invoiceNumber);

    List<String> getClientsByCode(List<String> clientCodes);

    void updateClientEmailPreference(String clientId, Boolean receiveInvoiceByEmail);

    List<Client> getActiveClientsToReceiveInventory();

    List<Client> getClientsByCodes(List<String> clientCodes);
	List<Client> getClientsWithAutoInvoice();

	List<String> findClientIdsBySalesId(String salesId);
	/**
	 * Save the salespersons assigned to a client.
	 */
	void saveClientSalespersons(String clientId, List<String> salespersonIds);

}
