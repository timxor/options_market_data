# Project Overview

This repository collects and processes options market data using a mix of Java and Python.
Below is a high level description of the available modules and how to get started.

## Directory Summary

- **alpha_vantage_api** – Java code to query the [Alpha Vantage](https://www.alphavantage.co/) API for historical options data.
- **web-scraper** – Java based web scraper used to download options chains from brokerage sites.
- **python_examples** – Small Python scripts demonstrating data collection using `yfinance` and other libraries.
- **charts** – Example charts produced from collected data.
- **csv_file_examples** – Sample CSV files containing options chain information.
- **old_quotes** – Archive of older quotes that were previously scraped.

## Quick Start

1. Clone the repository:
   ```bash
   git clone https://github.com/timxor/options_market_data.git
   cd options_market_data
   ```
2. Build and run the Java web scraper:
   ```bash
   cd web-scraper
   mvn clean compile exec:java
   ```
3. Run the Python examples (requires Python 3):
   ```bash
   cd ../python_examples
   python -m venv my_env && source my_env/bin/activate
   pip install -r requirements.txt
   python yfinance_script.py
   ```

For detailed instructions see the README files located in each directory.
