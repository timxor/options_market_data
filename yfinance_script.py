# yfinance_script.py

# pip install yfinance


# import yfinance as yf
# import pandas as pd
# 
# # Define the stock symbol for the company you're interested in
# stock_symbol = "PLTR"  # Change this to the symbol you want
# 
# # Create a Ticker object
# ticker = yf.Ticker(stock_symbol)
# 
# # Get the options data
# options_data = ticker.options
# 
# # Convert options_data to a DataFrame
# options_df = pd.DataFrame(options_data)
# 
# # Save the DataFrame to a CSV file
# options_df.to_csv("PLTR_options_data.csv", index=True)


# import yfinance as yf
# import pandas as pd
# 
# # Replace 'PLTR' with your desired ticker symbol
# ticker = "PLTR"
# 
# # Create a Ticker object
# pltr = yf.Ticker(ticker)
# 
# # Get the options chain data
# options = pltr.option_chain()
# 
# # Save the data to a CSV file
# options.calls.to_csv(f'{ticker}_options_calls.csv', index=False)
# options.puts.to_csv(f'{ticker}_options_puts.csv', index=False)
# 
# print("Options data saved as CSV files.")



# 
# import yfinance as yf
# import pandas as pd
# 
# # Replace 'PLTR' with your desired ticker symbol
# ticker = "PLTR"
# 
# # Create a Ticker object
# pltr = yf.Ticker(ticker)
# 
# # Get the list of expiration dates for options
# expiration_dates = pltr.options
# 
# # Create a directory to store the CSV files
# import os
# if not os.path.exists(ticker):
#     os.makedirs(ticker)
# 
# # Loop through each expiration date
# for date in expiration_dates:
#     options = pltr.option_chain(date)
# 
#     # Save the call and put options data for this expiration date to CSV files
#     call_filename = f'{ticker}_{date}_calls.csv'
#     put_filename = f'{ticker}_{date}_puts.csv'
# 
#     options.calls.to_csv(os.path.join(ticker, call_filename), index=False)
#     options.puts.to_csv(os.path.join(ticker, put_filename), index=False)
# 
#     print(f"Options data for {date} saved as {call_filename} and {put_filename}.")
# 
# print("All options data downloaded and saved.")







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
