import javax.smartcardio.*;
import java.net.URI;
import java.net.http.*;
import java.util.List;

public class NfcReader {

    private static final String API_URL = "http://localhost:8080/api/v1/nfc/scan/";

    public static void main(String[] args) throws Exception {

        // 1. Get all available terminals (readers)
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();

        if (terminals.isEmpty()) {
            System.out.println("No NFC reader found. Check if ACR122U is plugged in.");
            return;
        }

        CardTerminal terminal = terminals.get(0);
        System.out.println("Reader detected: " + terminal.getName());
        System.out.println("Waiting for NFC card...");

        // 2. Keep listening for cards
        while (true) {
            terminal.waitForCardPresent(0);

            try {
                // 3. Connect and send GET UID command
                Card card = terminal.connect("*");
                CardChannel channel = card.getBasicChannel();

                CommandAPDU getUID = new CommandAPDU(new byte[]{
                        (byte) 0xFF, (byte) 0xCA, 0x00, 0x00, 0x00
                });

                ResponseAPDU response = channel.transmit(getUID);

                // 4. Convert and print
                String uid = bytesToHex(response.getData());
                System.out.println("UID: " + uid);

                // 5. Send UID to API as path variable
                sendToApi(uid);

                // 6. Wait for card removal then loop
                card.disconnect(false);
                terminal.waitForCardAbsent(0);
                System.out.println("Card removed. Waiting for next card...");

            } catch (CardException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void sendToApi(String uid) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + uid))  // http://localhost:8080/api/v1/nfc/scan/"UID"
                .header("accept", "*/*")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("API Response: " + response.statusCode() + " - " + response.body());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}