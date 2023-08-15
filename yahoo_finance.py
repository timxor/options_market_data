# https://algotrading101.com/learn/yahoo-finance-api-guide/
# pip install yfinance --upgrade --no-cache-dir

import yfinance as yf
pltr_ticker = yf.Ticker("PLTR")
print("pltr_ticker.info")
print("")
print("")
print(pltr_ticker.info)
print("")


# https://algotrading101.com/learn/yahoo-finance-api-guide/#:~:text=get_cash_flow(%22aapl%22)%0Acash_flow_statement-,How%20do%20I%20download%20options%20data%3F,-So%20far%20we%E2%80%99ve
# pip install yahoo_fin
# pip install yahoo_fin --upgrade
# pip install requests_html

import yahoo_fin.options as options
pltr_expiration_dates = options.get_expiration_dates("pltr")
print("pltr_expiration_dates")
print("")
print("")
print(pltr_expiration_dates)
print("")


# get calls data
options.get_calls("pltr")


# get puts data
options.get_puts("pltr", "09/16/2023")










