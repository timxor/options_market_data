import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OptionsDataService {
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private final String apiKey;
    private final HttpClient client;

    public OptionsDataService(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String fetchHistoricalOptions(String symbol, String date) throws Exception {
        URI uri = URI.create(String.format("%s?function=HISTORICAL_OPTIONS&symbol=%s&date=%s&apikey=%s",
                BASE_URL, symbol, date, apiKey));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Domain models for structured response
    public record OptionsData(
            String symbol,
            String date,
            List<OptionContract> options
    ) {}

    public record OptionContract(
            String contractName,
            String type,
            double strike,
            String expiration,
            double lastPrice,
            double bid,
            double ask,
            double impliedVolatility,
            double delta,
            double gamma,
            double theta,
            double vega,
            double rho
    ) {}
}