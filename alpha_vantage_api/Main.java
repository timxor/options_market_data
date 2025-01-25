public class Main {
    public static void main(String[] args) {
        String apiKey = "YOUR_API_KEY";
        OptionsDataService service = new OptionsDataService(apiKey);

        try {
            // Get PLTR options data for a specific date
            String optionsData = service.fetchHistoricalOptions("PLTR", "2024-01-17");
            System.out.println(optionsData);

            // Get most recent PLTR options data (omit date parameter)
            String recentOptionsData = service.fetchHistoricalOptions("PLTR", null);
            System.out.println(recentOptionsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}