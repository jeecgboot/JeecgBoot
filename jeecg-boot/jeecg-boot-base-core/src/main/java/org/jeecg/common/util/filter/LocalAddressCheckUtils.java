package org.jeecg.common.util.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.vo.Firewall;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SSRF 出站地址校验工具：
 * <ul>
 *     <li>访问本服务自身（本机地址 + 本服务监听端口）放行；</li>
 *     <li>命中白名单 jeecg.firewall.localAddressWhiteList 即放行：域名、IP、CIDR 网段、地址:端口（只放行该端口）；</li>
 *     <li>链路本地（含云元数据 169.254.169.254）、0.0.0.0/8、[::] 始终拒绝，写进白名单也不放行；</li>
 *     <li>本机其他端口拒绝；内网段（10/172.16/192.168/100.64/fc00::/7）默认放行，allowLocalAddress=false 时拒绝；</li>
 *     <li>IPv6 过渡地址（NAT64、6to4、Teredo、IPv4兼容/映射）按内嵌 IPv4 判断。</li>
 * </ul>
 *
 * @author scott
 * @since 2026-10-27 【GHSA-5g5g-43q2-g87v】【GHSA-2m5w-w7v9-qwx7】【GHSA-hw5p-65q6-qcxr】从 SsrfFileTypeFilter 抽取，专门用于 SSRF 校验
 */
@Slf4j
public class LocalAddressCheckUtils {

    private LocalAddressCheckUtils() {
    }

    /**
     * 校验HTTP(S) URL，防止SSRF攻击（最小化拦截，只挡真正危险的目标）。
     * 规则：
     * 1. 仅允许 http / https 协议；
     * 2. 解析主机IP，拒绝 loopback（127.x / ::1）、link-local（169.254.x，含云元数据 169.254.169.254 / fe80:）、0.0.0.0/8、[::]，
     *    以及内嵌这些地址的 IPv6 过渡地址（NAT64、6to4、Teredo、IPv4兼容/映射）；
     * 3. 访问本服务自身（本机地址 + 本服务监听端口）放行。
     * 4. 命中白名单 jeecg.firewall.localAddressWhiteList（域名、IP、CIDR 网段、地址:端口）即放行，链路本地、0.0.0.0 除外。
     * 注意：RFC1918 私网段（10/172.16/192.168）默认允许通过，兼容企业内网 MinIO/OSS/文件服务等合法用途；
     *    配置 jeecg.firewall.allowLocalAddress=false 后拦截。
     *
     * @param fileUrl HTTP(S) URL
     * @author scott
     * @since 2026-10-27 【GHSA-5g5g-43q2-g87v】由 SsrfFileTypeFilter.checkSsrfHttpUrl 迁移
     */
    public static void checkUrl(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            throw new JeecgBootException("非法URL：地址为空");
        }
        URI uri;
        try {
            uri = new URI(fileUrl);
        } catch (URISyntaxException e) {
            throw new JeecgBootException("非法URL：格式错误");
        }
        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            throw new JeecgBootException("非法URL：仅允许 http / https 协议");
        }
        String host = uri.getHost();
        if (StringUtils.isBlank(host)) {
            throw new JeecgBootException("非法URL：主机名为空");
        }
        // 去掉 IPv6 的中括号
        if (host.startsWith("[") && host.endsWith("]")) {
            host = host.substring(1, host.length() - 1);
        }
        try {
            checkAddresses(host, uri);
        } catch (UnknownHostException e) {
            throw new JeecgBootException("非法URL：主机名无法解析");
        }
    }

    /**
     * 解析主机并校验所有解析结果，不合法时抛出 JeecgBootException
     *
     * @param host 主机名（IPv6 已去掉中括号）
     * @param uri  目标地址（用于取端口判断是否访问本服务自身）
     * @throws UnknownHostException 主机名无法解析
     */
    private static void checkAddresses(String host, URI uri) throws UnknownHostException {
        Firewall firewall = getFirewall();
        boolean blockPrivate = firewall != null && Boolean.FALSE.equals(firewall.getAllowLocalAddress());
        List<String> whiteList = firewall == null ? null : firewall.getLocalAddressWhiteList();
        InetAddress[] addresses = InetAddress.getAllByName(host);
        // 访问本服务自身（如本地存储文件 http://localhost:8080/jeecg-boot/sys/common/static/xx.png）不拦截
        if (isSelfServiceAddress(addresses, uri)) {
            return;
        }
        int port = getPort(uri);
        // 命中白名单即放行（域名、IP、网段、地址:端口），与开关无关；链路本地（云元数据）、0.0.0.0 仍拒绝
        if (isInWhiteList(host, addresses, port, whiteList)) {
            for (InetAddress addr : addresses) {
                if (isNeverAllowedAddress(addr)) {
                    throw new JeecgBootException("非法URL：禁止访问链路本地或任意地址 " + addr.getHostAddress());
                }
            }
            return;
        }
        String hint = "，如需放行请在 jeecg.firewall.localAddressWhiteList 中配置该地址，或 地址:端口（如 " + (host.contains(":") ? "[" + host + "]" : host) + ":" + port + "）";
        for (InetAddress addr : addresses) {
            if (addr.isLoopbackAddress()) {
                throw new JeecgBootException("非法URL：禁止访问本机或链路本地地址 " + addr.getHostAddress() + hint);
            }
            checkDangerousAddress(addr, addr);
            if (blockPrivate && isPrivateAddress(addr)) {
                throw new JeecgBootException("非法URL：禁止访问内网地址 " + addr.getHostAddress() + hint);
            }
        }
    }

    /**
     * 是否命中白名单：命中"地址:端口"，或每个解析结果都命中域名/IP/CIDR 网段条目
     */
    private static boolean isInWhiteList(String host, InetAddress[] addresses, int port, List<String> whiteList) {
        if (whiteList == null || whiteList.isEmpty() || addresses.length == 0) {
            return false;
        }
        if (isInHostPortWhiteList(addresses, port, whiteList)) {
            return true;
        }
        for (InetAddress addr : addresses) {
            if (!isInLocalAddressWhiteList(host, addr, whiteList)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 目标端口，未写端口时 https 为 443、其余为 80
     */
    private static int getPort(URI uri) {
        return uri.getPort() != -1 ? uri.getPort() : ("https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80);
    }

    /**
     * 是否命中白名单中的"地址:端口"条目：端口一致，且目标解析结果全部属于该条目地址的解析结果（写 localhost:9999 时 127.0.0.1:9999 同样命中）
     */
    private static boolean isInHostPortWhiteList(InetAddress[] addresses, int port, List<String> whiteList) {
        if (whiteList == null || whiteList.isEmpty() || addresses.length == 0) {
            return false;
        }
        for (String entry : whiteList) {
            if (StringUtils.isBlank(entry)) {
                continue;
            }
            String[] hostPort = parseHostPort(entry.trim());
            if (hostPort == null || Integer.parseInt(hostPort[1]) != port) {
                continue;
            }
            try {
                List<InetAddress> allowed = Arrays.asList(InetAddress.getAllByName(hostPort[0]));
                if (Arrays.stream(addresses).allMatch(allowed::contains)) {
                    return true;
                }
            } catch (Exception e) {
                log.warn("SSRF 白名单配置无法解析，已忽略: {}", entry);
            }
        }
        return false;
    }

    /**
     * 解析"地址:端口"条目：host:port 或 [IPv6]:port，返回 {host, port}；IP、CIDR、域名、不带中括号的 IPv6 返回 null
     */
    private static String[] parseHostPort(String item) {
        if (item.startsWith("[")) {
            int end = item.indexOf("]:");
            return end > 0 && item.substring(end + 2).matches("\\d{1,5}") ? new String[]{item.substring(1, end), item.substring(end + 2)} : null;
        }
        int idx = item.indexOf(':');
        if (idx <= 0 || idx != item.lastIndexOf(':') || item.contains("/") || !item.substring(idx + 1).matches("\\d{1,5}")) {
            return null;
        }
        return new String[]{item.substring(0, idx), item.substring(idx + 1)};
    }

    /**
     * 白名单也不能放行的地址：链路本地（含云元数据 169.254.169.254）、0.0.0.0/8、[::]，IPv6 过渡地址按内嵌 IPv4 判断
     */
    private static boolean isNeverAllowedAddress(InetAddress addr) {
        // ::1 字节上形如 IPv4 兼容地址（内嵌 0.0.0.1），属于本机回环，不按内嵌地址判断
        if (addr.isLoopbackAddress()) {
            return false;
        }
        byte[] b = addr.getAddress();
        if (addr.isLinkLocalAddress() || addr.isAnyLocalAddress() || (b.length == 4 && b[0] == 0)) {
            return true;
        }
        for (InetAddress embedded : getEmbeddedIpv4(addr)) {
            if (isNeverAllowedAddress(embedded)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验单个地址：本机、链路本地（含云元数据 169.254.169.254）、0.0.0.0/8、[::] 一律拒绝；
     * IPv6 过渡地址（NAT64、6to4、Teredo、IPv4兼容/映射）取出内嵌的 IPv4 再按同样规则校验
     *
     * @param addr     当前校验的地址
     * @param original 原始解析地址（用于提示）
     */
    private static void checkDangerousAddress(InetAddress addr, InetAddress original) {
        byte[] b = addr.getAddress();
        boolean thisNetwork = b.length == 4 && b[0] == 0;
        if (addr.isLoopbackAddress() || addr.isLinkLocalAddress() || addr.isAnyLocalAddress() || thisNetwork) {
            throw new JeecgBootException("非法URL：禁止访问本机或链路本地地址 " + original.getHostAddress());
        }
        for (InetAddress embedded : getEmbeddedIpv4(addr)) {
            checkDangerousAddress(embedded, original);
        }
    }

    /**
     * 取出 IPv6 过渡地址中内嵌的 IPv4 地址，非过渡地址返回空列表
     * <ul>
     *     <li>IPv4兼容 ::a.b.c.d、IPv4映射 ::ffff:a.b.c.d</li>
     *     <li>NAT64 64:ff9b::/96、64:ff9b:1::/48（取末 32 位）</li>
     *     <li>6to4 2002::/16（第 3~6 字节）</li>
     *     <li>Teredo 2001:0::/32（服务器地址第 5~8 字节；客户端地址末 32 位按位取反）</li>
     * </ul>
     */
    private static List<InetAddress> getEmbeddedIpv4(InetAddress addr) {
        List<InetAddress> result = new ArrayList<>();
        if (!(addr instanceof Inet6Address)) {
            return result;
        }
        byte[] b = addr.getAddress();
        try {
            boolean first10Zero = true;
            for (int i = 0; i < 10; i++) {
                if (b[i] != 0) {
                    first10Zero = false;
                    break;
                }
            }
            boolean compatOrMapped = first10Zero && ((b[10] == 0 && b[11] == 0) || (b[10] == (byte) 0xff && b[11] == (byte) 0xff));
            boolean nat64 = b[0] == 0x00 && b[1] == 0x64 && b[2] == (byte) 0xff && b[3] == (byte) 0x9b;
            if (compatOrMapped || nat64) {
                result.add(InetAddress.getByAddress(Arrays.copyOfRange(b, 12, 16)));
            } else if (b[0] == 0x20 && b[1] == 0x02) {
                result.add(InetAddress.getByAddress(Arrays.copyOfRange(b, 2, 6)));
            } else if (b[0] == 0x20 && b[1] == 0x01 && b[2] == 0x00 && b[3] == 0x00) {
                result.add(InetAddress.getByAddress(Arrays.copyOfRange(b, 4, 8)));
                byte[] client = Arrays.copyOfRange(b, 12, 16);
                for (int i = 0; i < client.length; i++) {
                    client[i] = (byte) ~client[i];
                }
                result.add(InetAddress.getByAddress(client));
            }
        } catch (UnknownHostException ignored) {
            // 4 字节数组不会触发
        }
        return result;
    }

    /**
     * 是否内网地址：10/8、172.16/12、192.168/16、100.64/10（运营商级NAT）、fc00::/7（IPv6唯一本地地址），IPv6 过渡地址按内嵌 IPv4 判断
     */
    private static boolean isPrivateAddress(InetAddress addr) {
        byte[] b = addr.getAddress();
        if (addr.isSiteLocalAddress()) {
            return true;
        }
        if (b.length == 4 && (b[0] & 0xff) == 100 && (b[1] & 0xc0) == 64) {
            return true;
        }
        if (b.length == 16 && (b[0] & 0xfe) == 0xfc) {
            return true;
        }
        for (InetAddress embedded : getEmbeddedIpv4(addr)) {
            if (isPrivateAddress(embedded)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 单个地址是否命中白名单的域名/IP/CIDR 条目：域名精确匹配（不区分大小写）、IP 精确匹配或 CIDR 网段匹配
     */
    private static boolean isInLocalAddressWhiteList(String host, InetAddress addr, List<String> whiteList) {
        if (whiteList == null || whiteList.isEmpty()) {
            return false;
        }
        byte[] target = addr.getAddress();
        for (String entry : whiteList) {
            if (StringUtils.isBlank(entry)) {
                continue;
            }
            String item = entry.trim();
            // "地址:端口" 条目由 isInHostPortWhiteList 处理
            if (parseHostPort(item) != null) {
                continue;
            }
            if (item.equalsIgnoreCase(host)) {
                return true;
            }
            try {
                String ip = item;
                int prefix = -1;
                if (item.contains("/")) {
                    ip = item.substring(0, item.indexOf('/'));
                    prefix = Integer.parseInt(item.substring(item.indexOf('/') + 1).trim());
                }
                // 只按字面量 IP 解析，非 IP 条目（域名）跳过，避免触发 DNS 查询
                if (!ip.matches("^[0-9.]+$") && !ip.contains(":")) {
                    continue;
                }
                byte[] net = InetAddress.getByName(ip).getAddress();
                if (net.length != target.length) {
                    continue;
                }
                int bits = prefix < 0 ? net.length * 8 : prefix;
                if (matchPrefix(net, target, bits)) {
                    return true;
                }
            } catch (Exception e) {
                log.warn("SSRF 白名单配置无法解析，已忽略: {}", item);
            }
        }
        return false;
    }

    /**
     * 比较两个地址的前 bits 位是否相同
     */
    private static boolean matchPrefix(byte[] net, byte[] target, int bits) {
        int fullBytes = bits / 8;
        for (int i = 0; i < fullBytes; i++) {
            if (net[i] != target[i]) {
                return false;
            }
        }
        int rest = bits % 8;
        if (rest == 0) {
            return true;
        }
        int mask = (0xff << (8 - rest)) & 0xff;
        return (net[fullBytes] & mask) == (target[fullBytes] & mask);
    }

    /**
     * 是否访问本服务自身：解析结果全部为本机地址（回环或本机网卡 IP），且端口等于本服务实际监听端口。
     * 端口取自服务器自身（请求连接器端口或 server.port），不读请求头，无法伪造；本机其他端口（Redis、数据库等）仍按规则拦截
     */
    private static boolean isSelfServiceAddress(InetAddress[] addresses, URI uri) {
        if (addresses == null || addresses.length == 0) {
            return false;
        }
        int targetPort = uri.getPort() != -1 ? uri.getPort() : ("https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80);
        Integer selfPort = getSelfServerPort();
        if (selfPort == null || selfPort != targetPort) {
            return false;
        }
        for (InetAddress addr : addresses) {
            if (!isLocalMachineAddress(addr)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否本机地址：回环地址，或绑定在本机某块网卡上的 IP（0.0.0.0、组播不算）
     */
    private static boolean isLocalMachineAddress(InetAddress addr) {
        if (addr.isLoopbackAddress()) {
            return true;
        }
        if (addr.isAnyLocalAddress() || addr.isMulticastAddress()) {
            return false;
        }
        try {
            return NetworkInterface.getByInetAddress(addr) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 本服务实际监听端口：有请求上下文时取连接器端口，否则读 server.port（默认 8080），获取失败返回 null
     */
    private static Integer getSelfServerPort() {
        try {
            jakarta.servlet.http.HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            if (request != null) {
                return request.getLocalPort();
            }
        } catch (Exception ignored) {
            // 无请求上下文（定时任务、异步线程等）
        }
        try {
            return SpringContextUtils.getApplicationContext().getEnvironment().getProperty("server.port", Integer.class, 8080);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取平台安全配置，非 Spring 环境（如单元测试）返回 null，按默认值处理
     */
    private static Firewall getFirewall() {
        try {
            JeecgBaseConfig config = SpringContextUtils.getBean(JeecgBaseConfig.class);
            return config == null ? null : config.getFirewall();
        } catch (Exception e) {
            return null;
        }
    }
}
