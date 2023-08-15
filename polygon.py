

# api docs
# https://polygon.io/blog/introducing-stock-options-apis


# find all of the Norwegian Cruise Lines options contracts expiring in 2022, we can query
nclh_2022_endpoint = "https://api.polygon.io/v3/reference/options/contracts?underlying_ticker=NCLH&order=asc&sort=ticker&expiration_date.gte=2022-01-01&expiration_date.lt=2023-01-01&limit=1000&apiKey=*"


# This gives us all 400+ active NCLH contracts that expire in 2022.

# {
#     "results":[{
#         "cfi": "OCASPS",
#         "contract_type": "call",
#         "exercise_style": "american",
#         "expiration_date": "2022-03-04",
#         "primary_exchange": "BATO",
#         "shares_per_contract": 100,
#         "strike_price": 14,
#         "ticker": "O:NCLH220304C00014000",
#         "underlying_ticker": "NCLH"
#     },
#     ...
#     {
#         "cfi": "OPASPS",
#         "contract_type": "put",
#         "exercise_style": "american",
#         "expiration_date": "2022-09-16",
#         "primary_exchange": "BATO",
#         "shares_per_contract": 100,
#         "strike_price": 40,
#         "ticker": "O:NCLH220916P00040000",
#         "underlying_ticker": "NCLH"
#     }],
#     "status":"OK",
#     "count":442
# }




# if you've upgraded to one of our paid options plans,
# you can get all of the most recent quotes,
# aggregate bars,
# break even price, etc.
# for that contract in a single call using our Snapshot API.




