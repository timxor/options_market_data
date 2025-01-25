# yfinance_script.py

import yfinance as yf
import pandas as pd

# Replace 'PLTR' with your desired ticker symbol
ticker = "PLTR"

# Create a Ticker object
pltr = yf.Ticker(ticker)

# Get the list of expiration dates for options
expiration_dates = pltr.options

# Create a directory to store the CSV files
import os
if not os.path.exists(ticker):
    os.makedirs(ticker)

# Loop through each expiration date
for date in expiration_dates:
    options = pltr.option_chain(date)
    
    # Combine the call and put options data into a single DataFrame
    combined_options = pd.concat([options.calls, options.puts], keys=['Calls', 'Puts'])
    
    # Save the combined options data to a CSV file
    combined_filename = f'{ticker}_{date}_combined.csv'
    combined_options.to_csv(os.path.join(ticker, combined_filename), index=False)
    
    print(f"Combined options data for {date} saved as {combined_filename}.")

print("All combined options data downloaded and saved.")
