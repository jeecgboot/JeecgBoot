package org.jeecg.modules.business.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    Map<String,?> getKpis(LocalDateTime start, LocalDateTime end, List<String> roles, String username);

    Map<String,?> getPackageStatuses(int period);
}
