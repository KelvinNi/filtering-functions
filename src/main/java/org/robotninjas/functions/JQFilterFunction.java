package org.robotninjas.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import net.thisptr.jackson.jq.JsonQuery;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

@SuppressWarnings("unused")
public class JQFilterFunction implements Function<String, String> {

    private static final String DefaultFilter = ".";

    private final ObjectMapper mapper;
    private volatile JsonQuery jq;

    @SuppressWarnings("unused")
    public JQFilterFunction() {
        try {
            mapper = new ObjectMapper();
            jq = JsonQuery.compile(DefaultFilter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String process(String in, Context context) throws Exception {
        switch (context.getTopicName()) {
            case "persistent://sample/standalone/ns1/exclamation-input2":
                jq = JsonQuery.compile(in);
                return null;
            case "persistent://sample/standalone/ns1/exclamation-input":
                JsonNode node = mapper.readTree(in);
                List<JsonNode> out = jq.apply(node);
                return mapper.writeValueAsString(out);
            default:
                return null;
        }
    }

}
