import java.time.Instant;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class test_signature {
    public static void main(String[] args) throws Exception {
        String key = "secret";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        
        String timestamp = "1719660000000";
        String payload = "{\"orderId\":\"ORDER-001\",\"platform\":\"SHOPIFY\",\"customer\":{\"name\":\"John Doe\",\"email\":\"john@example.com\"},\"items\":[{\"sku\":\"SKU-001\",\"quantity\":2,\"price\":19.99}]}";
        String data = timestamp + "." + payload;
        
        byte[] hash = mac.doFinal(data.getBytes("UTF-8"));
        System.out.println("Java signature: " + HexFormat.of().formatHex(hash));
    }
}
