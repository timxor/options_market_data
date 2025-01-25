# alpha vantage api examples

alpha vantage api examples

Set and get ```YOUR_API_KEY``` from https://www.alphavantage.co/support/#api-key

## historical options data api

https://www.alphavantage.co/documentation/#historical-options


## Example API URLs for PLTR:

#### 1. Most recent options data:

https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&apikey=YOUR_API_KEY
```curl "https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&apikey=YOUR_API_KEY"```


#### 2. Specific date options data:

https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&date=2024-01-17&apikey=YOUR_API_KEY
```curl "https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&date=2024-01-17&apikey=YOUR_API_KEY"```


#### 3. CSV format:

https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&date=2024-01-17&apikey=YOUR_API_KEY&datatype=csv
```curl "https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&date=2024-01-17&apikey=YOUR_API_KEY&datatype=csv" > pltr_options.csv```


#### 4. JSON format:

```curl "https://www.alphavantage.co/query?function=HISTORICAL_OPTIONS&symbol=PLTR&apikey=YOUR_API_KEY" | jq '.'```
























