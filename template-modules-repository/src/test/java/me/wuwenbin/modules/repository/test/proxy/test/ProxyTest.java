package me.wuwenbin.modules.repository.test.proxy.test;

import me.wuwenbin.modules.repository.provider.select.page.support.PageExtraFactory;
import me.wuwenbin.modules.repository.provider.select.page.support.fake.column.FakeColPageExtra;
import me.wuwenbin.modules.repository.test.User;
import me.wuwenbin.modules.repository.test.proxy.PublicService;
import me.wuwenbin.modules.repository.test.proxy.ServiceProxyFactory;
import me.wuwenbin.modules.repository.test.proxy.UserService;
import org.junit.Test;

import java.io.IOException;

/**
 * created by Wuwenbin on 2017/10/28 at 11:24
 */
public class ProxyTest {

    @Test
    public void proxyTest() throws Exception {
        PublicService publicService = new ServiceProxyFactory<>(PublicService.class).newInstance();
        publicService.test();
    }

    @Test
    public void proxyTest2() throws Exception {
        UserService userService = new ServiceProxyFactory<>(UserService.class).newInstance();
//        userService.name();
//        Person p1 = new Person();
//        Person p2 = new Person();
//        Person p3 = new Person();
//        List<Person> pl = Stream.of(p1, p2, p3).collect(Collectors.toList());
//        Set<Person> sps = Stream.of(p1, p2, p3).collect(Collectors.toSet());
//        Map<String, Object> sp = new HashMap<>();
//        sp.put("p1", p1);
//        sp.put("p2", p2);
//        sp.put("p4", p3);
//        userService.age();
//        userService.name("wwb","123",1);
//        userService.findList2();
//        userService.empty();
//        userService.save("b", p1, p2, p3);
//        userService.save1(sp);
//        userService.save2(new Person[]{p1, p2, p3}, "2");
//        userService.save3(pl);
//        userService.save4(sps);
//        userService.test();
//        userService.saveTest("1", "2");

//        userService.testPoint3("1", "2", "3");
        userService.saveTestUser(new User());
    }

    @Test
    public void test3() throws IOException, ClassNotFoundException {
//        HashSet targetClass = new HashSet();
//        System.out.println(targetClass instanceof Set);
    }

    @Test
    public void testPageExtra() {
        String sql = "select title from table";
        FakeColPageExtra fakeColPageExtra = PageExtraFactory.getPageExtra(FakeColPageExtra.class, "rownum");
        String finalSql = PageExtraFactory.finalSql(fakeColPageExtra, sql);
        System.out.println(finalSql);
    }
}
