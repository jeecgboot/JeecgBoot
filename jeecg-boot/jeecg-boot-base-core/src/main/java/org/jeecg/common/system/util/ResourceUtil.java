package org.jeecg.common.system.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.annotation.EnumDict;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 枚举字典数据 资源加载工具类
 *
 * @Author taoYan
 * @Date 2022/7/8 10:40
 **/
@Slf4j
public class ResourceUtil {

    /**
     * 多个包扫描根路径
     *
     * 之所以让用户手工配置扫描路径，是为了避免不必要的类加载开销，提升启动性能。
     * 请务必将所有枚举类所在包路径添加到此配置中。
     */
    private final static String[] BASE_SCAN_PACKAGES = {
            "org.jeecg.common.constant.enums",
            "org.jeecg.modules.message.enums"
    };
    
    /**
     * 枚举字典数据
     */
    private final static Map<String, List<DictModel>> enumDictData = new HashMap<>(5);
    /**
     * 所有枚举java类
     */

    private final static String CLASS_ENUM_PATTERN="/**/*Enum.class";

    /**
     * 初始化状态标识
     */
    private static volatile boolean initialized = false;

    /**
     * 枚举类中获取字典数据的方法名
     */
    private final static String METHOD_NAME = "getDictList";

    /**
     * 获取枚举字典数据
     * 获取枚举类对应的字典数据 SysDictServiceImpl#queryAllDictItems()
     *
     * @return 枚举字典数据
     */
    public static Map<String, List<DictModel>> getEnumDictData() {
        if (!initialized) {
            synchronized (ResourceUtil.class) {
                if (!initialized) {
                    long startTime = System.currentTimeMillis();
                    log.debug("【枚举字典加载】开始初始化枚举字典数据...");

                    initEnumDictData();
                    initialized = true;

                    long endTime = System.currentTimeMillis();
                    log.debug("【枚举字典加载】枚举字典数据初始化完成，共加载 {} 个字典，总耗时: {}ms", enumDictData.size(), endTime - startTime);
                }
            }
        }
        return enumDictData;
    }

    /**
     * 使用多包路径扫描方式初始化枚举字典数据
     */
    private static void initEnumDictData() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        long scanStartTime = System.currentTimeMillis();
        List<Resource> allResources = new ArrayList<>();

        // 扫描多个包路径
        for (String basePackage : BASE_SCAN_PACKAGES) {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + CLASS_ENUM_PATTERN;

            try {
                Resource[] resources = resourcePatternResolver.getResources(pattern);
                allResources.addAll(Arrays.asList(resources));
                log.debug("【枚举字典加载】扫描包 {} 找到 {} 个枚举类文件", basePackage, resources.length);
            } catch (Exception e) {
                log.warn("【枚举字典加载】扫描包 {} 时出现异常: {}", basePackage, e.getMessage());
            }
        }

        long scanEndTime = System.currentTimeMillis();
        log.debug("【枚举字典加载】文件扫描完成，总共找到 {} 个枚举类文件，扫描耗时: {}ms", allResources.size(), scanEndTime - scanStartTime);

        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        long processStartTime = System.currentTimeMillis();
        int processedCount = 0;

        for (Resource resource : allResources) {
            try {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String classname = reader.getClassMetadata().getClassName();

                // 提前检查是否有@EnumDict注解，避免不必要的Class.forName
                if (hasEnumDictAnnotation(reader)) {
                    processEnumClass(classname);
                    processedCount++;
                }
            } catch (Exception e) {
                log.debug("处理资源异常: {} - {}", resource.getFilename(), e.getMessage());
            }
        }

        long processEndTime = System.currentTimeMillis();
        log.debug("【枚举字典加载】处理完成，实际处理 {} 个带注解的枚举类，处理耗时: {}ms", processedCount, processEndTime - processStartTime);
    }

    /**
     * 检查类是否有EnumDict注解（通过元数据，避免类加载）
     */
    private static boolean hasEnumDictAnnotation(MetadataReader reader) {
        try {
            return reader.getAnnotationMetadata().hasAnnotation(EnumDict.class.getName());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 处理单个枚举类
     */
    private static void processEnumClass(String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            EnumDict enumDict = clazz.getAnnotation(EnumDict.class);

            if (enumDict != null) {
                String key = enumDict.value();
                if (oConvertUtils.isNotEmpty(key)) {
                    Method method = clazz.getDeclaredMethod(METHOD_NAME);
                    List<DictModel> list = (List<DictModel>) method.invoke(null);
                    enumDictData.put(key, list);
                    log.debug("成功加载枚举字典: {} -> {}", key, classname);
                }
            }
        } catch (Exception e) {
            log.debug("处理枚举类异常: {} - {}", classname, e.getMessage());
        }
    }

    /**
     * 用于后端字典翻译 SysDictServiceImpl#queryManyDictByKeys(java.util.List, java.util.List)
     *
     * @param dictCodeList 字典编码列表
     * @param keys         键值列表
     * @return 字典数据映射
     */
    public static Map<String, List<DictModel>> queryManyDictByKeys(List<String> dictCodeList, List<String> keys) {
        Map<String, List<DictModel>> enumDict = getEnumDictData();
        Map<String, List<DictModel>> map = new HashMap<>();

        // 使用更高效的查找方式
        Set<String> dictCodeSet = new HashSet<>(dictCodeList);
        Set<String> keySet = new HashSet<>(keys);

        for (String code : enumDict.keySet()) {
            if (dictCodeSet.contains(code)) {
                List<DictModel> dictItemList = enumDict.get(code);
                for (DictModel dm : dictItemList) {
                    String value = dm.getValue();
                    if (keySet.contains(value)) {
                        // 修复bug：获取或创建该dictCode对应的list，而不是每次都创建新的list
                        List<DictModel> list = map.computeIfAbsent(code, k -> new ArrayList<>());
                        list.add(new DictModel(value, dm.getText()));
                        //break;
                    }
                }
            }
        }
        return map;
    }
    
}