package org.jeecg.modules.demo.test.vo;

import java.util.List;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import lombok.Data;

@Data
public class JeecgOrderMainPage {
	
	private JeecgOrderMain jeecgOrderMain;
	private List<JeecgOrderCustomer> jeecgOrderCustomerList;
	private List<JeecgOrderTicket> jeecgOrderTicketList;
	
}
