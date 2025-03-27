import org.jeecg.loader.vo.PredicatesVo;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @Description: 测试
 * @author: lsq
 * @date: 2023年10月13日 11:32
 */
public class TestRoutes {

    @Test
    public void TestRoutes() {
        List<PredicatesVo> list = new ArrayList<>();
        PredicatesVo a = new PredicatesVo();
        a.setName("path");
        String[] aArr={"/sys/**","/eoa/**"};
        a.setArgs(Arrays.asList(aArr));
        list.add(a);

        PredicatesVo b = new PredicatesVo();
        b.setName("path");
        String[] bArr={"/sys/**","/demo/**"};
        b.setArgs(Arrays.asList(bArr));
        list.add(b);

        Map<String, List<String>> groupedPredicates = new HashMap<>();
        for (PredicatesVo predicatesVo : list) {
            String name = predicatesVo.getName();
            List<String> args1 = predicatesVo.getArgs();
            groupedPredicates.computeIfAbsent(name, k -> new ArrayList<>()).addAll(args1);
        }
        System.out.println(groupedPredicates);
    }

}
