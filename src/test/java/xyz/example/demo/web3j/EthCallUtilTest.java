package xyz.example.demo.web3j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import sun.reflect.Reflection;
import xyz.example.demo.bean.DeployedContracts;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.web3j.abi.Utils.convert;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class EthCallUtilTest {
    @Data
    public static class Function {
        @JsonIgnore
        private DeployedContracts belongsTo;
        private String name;
        private List<Type> inputParameters;
        private List<TypeReference<Type>> outputParameters;
    }

    public Type parse(String className, Object value) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<?> aClass = Class.forName(StringUtils.capitalize(className));
        if (!aClass.isAssignableFrom(Type.class))
            return null;
//        aClass.getConstructor().newInstance(value)
        return null;
    }


    @Test
    void testEthUtils() throws IOException {
        Set<Function> functions = new LinkedHashSet<>();
        String path = "contract/UserContract.abi;contract/DeviceContract.abi;contract/TaskContract.abi";
        Set<String> fields = new HashSet<>();
        for (Field field : Function.class.getDeclaredFields()) {
            fields.add(field.getName());
        }
        for (String p : path.split(";")) {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(p);
            String utf8 = IOUtils.toString(resourceAsStream, "utf8");
            JSONArray objects = JSON.parseArray(utf8);

            objects.forEach(ob -> {
                log.info(ob.getClass() + "");
                JSONObject ob1 = (JSONObject) ob;

                Function function = JSON.parseObject(ob.toString(), Function.class);
                function.setBelongsTo(DeployedContracts.valueOf(p.substring(p.indexOf("/") + 1, p.lastIndexOf("."))));
                functions.add(function);

            });
        }
        log.info(JSON.toJSONString(functions));
    }
}