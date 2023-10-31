# stock_data.py

import yfinance as yf

# Define the ticker symbol
ticker_symbol = "PLTR"

# Download stock data
data = yf.download(ticker_symbol, start="2023-01-01", end="2023-12-31")

# Save the data to a CSV file
data.to_csv(f"{ticker_symbol}_stock_data.csv")

