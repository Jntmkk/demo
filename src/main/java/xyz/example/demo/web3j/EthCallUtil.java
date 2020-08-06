package xyz.example.demo.web3j;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

import java.io.IOException;
import java.util.*;
@Data
public class EthCallUtil {
    @Autowired
    Web3j web3j;
    List<Type> types;
    List<TypeReference<?>> outParams;
    String accountAddress;
    String contractAddress;
    String functionName;
    public List<Object> get() throws IOException {
        Function function = new Function(functionName, types, outParams);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(accountAddress, contractAddress, FunctionEncoder.encode(function));
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        List<Object> list = new LinkedList<>();
        FunctionReturnDecoder.decode(send.getValue(), function.getOutputParameters()).forEach(type -> {
            list.add(type.getValue());
        });
        return list;
    }


}
