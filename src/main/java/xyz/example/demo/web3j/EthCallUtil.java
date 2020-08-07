package xyz.example.demo.web3j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.IOTools;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import springfox.documentation.service.ApiListing;
import xyz.example.demo.bean.DeployedContractAddress;
import xyz.example.demo.bean.DeployedContracts;
import xyz.example.demo.models.User;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Data
@Component
public class EthCallUtil {
    String baseNum = "org.web3j.abi.datatypes.generated.";
    String base = "org.web3j.abi.datatypes.";
    String path = "contract/UserContract.abi;contract/DeviceContract.abi;contract/TaskContract.abi";

    public EthCallUtil(Web3j web3j, DeployedContractAddress deployedContractAddress) {
        this.web3j = web3j;
        this.deployedContractAddress = deployedContractAddress;
    }

    Web3j web3j;
    Map<String, EthFunctionWrapper> maps = new HashMap<>();
    DeployedContractAddress deployedContractAddress;


    @Data
    public static class EthFunctionWrapper {
        DeployedContracts deployedContracts;
        JSONObject jsonObject;

        public EthFunctionWrapper(DeployedContracts deployedContracts, JSONObject jsonObject) {
            this.deployedContracts = deployedContracts;
            this.jsonObject = jsonObject;
        }

        public EthFunctionWrapper() {
        }
    }

    public String getUserAddress(String username) throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, IOException {
        User getUserInformation = getObject("getUserInformation", "0x61C048AaC3Cf99FE97217A8b28F6ce32EB8B8ADE", User.class, username);
        return getUserInformation.getAddress();
    }

    @PostConstruct
    void init() throws IOException {
        for (String p : path.split(";")) {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(p);
            String utf8 = IOUtils.toString(resourceAsStream, "utf8");
            JSONArray objects = JSON.parseArray(utf8);
            for (Object ob : objects) {
                JSONObject ob1 = (JSONObject) ob;
                if (!ob1.get("type").equals("function"))
                    continue;
                maps.put(ob1.getString("name"), new EthFunctionWrapper(DeployedContracts.valueOf(p.substring(p.indexOf("/") + 1, p.lastIndexOf("."))), ob1));
            }
            ;
        }
    }

    /**
     * 获取合约函数的返回值，只适合用view修饰的函数。
     *
     * @param functionName   函数名
     * @param accountAddress 查询人地址
     * @param args           查询参数，类型需要与abi文件一致
     * @return 返回值列表，由于返回值没有名字所以返回值顺序与合约定义的一致。
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IOException
     */
    public List<Object> getValue(String functionName, String accountAddress, Object... args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        if (!maps.containsKey(functionName))
            return null;
        EthFunctionWrapper jsonObject = maps.get(functionName);
        JSONArray o = (JSONArray) jsonObject.jsonObject.get("inputs");
        if (o.size() != args.length)
            return null;
        /**
         * 构造输入
         */
        List<Type> input = new LinkedList<>();
        for (int i = 0; i < o.size(); i++) {
            JSONObject jsonObject1 = (JSONObject) o.get(i);
            String className = (String) jsonObject1.get("type");
            if (className.contains("int")) {
                className = baseNum + StringUtils.capitalize(className);
                Type t = (Type) Class.forName(className).getConstructor(BigInteger.class).newInstance(BigInteger.valueOf((Long) args[i]));
                input.add(t);
            } else {
                className = base + StringUtils.capitalize(className);
                Class type = Class.forName(className);
                Type o1 = (Type) type.getConstructor(type).newInstance(args[i]);
                input.add(o1);
            }

        }
        /**
         * 构造输出
         */
        List<TypeReference<?>> output = new LinkedList<>();
        JSONArray outs = (JSONArray) jsonObject.jsonObject.get("outputs");
        for (Object out : outs) {
            JSONObject out1 = (JSONObject) out;
            String className = (String) out1.get("type");
            TypeReference<?> o1 = TypeReference.makeTypeReference(className);
            output.add(o1);
        }

        Function function = new Function(functionName, input, output);
        List<Object> results = new ArrayList<>();
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(accountAddress, deployedContractAddress.getContractAddress(jsonObject.deployedContracts), FunctionEncoder.encode(function));
        String value = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send().getValue();
        FunctionReturnDecoder.decode(value, function.getOutputParameters()).forEach(type -> results.add(type.getValue()));
        return results;
    }

    public <T> T getObject(String functionName, String accountAddress, Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        List<Object> value = getValue(functionName, accountAddress, args);
        return getObject(value, cls);
    }

    public <T> T getObject(List<Object> list, Class<T> cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<? super T> superclass = cls.getSuperclass();
        if (superclass != Object.class)
            throw new RuntimeException("转换类型只能继承Object");
        Field[] declaredFields = cls.getDeclaredFields();
        if (declaredFields.length != list.size())
            throw new RuntimeException("待转化类属性数量与输入不匹配！");
        List<Object> classList = new LinkedList<>();
        List<Object> params = new LinkedList<>();
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            classList.add(declaredField.getClass());
            Object cast = declaredField.getType().cast(list.get(i));
            params.add(cast);
        }
        T t = cls.getConstructor(classList.toArray(new Class[list.size()])).newInstance(params.toArray(new Object[list.size()]));
        return t;
    }


}
