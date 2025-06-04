# web-scraper


[![Coverage Status](https://coveralls.io/repos/github/timxor/options_market_data/badge.svg?branch=main)](https://coveralls.io/github/timxor/options_market_data?branch=main)


web-scraper


## Quick Start

Clone, build, run:


```
git clone https://github.com/timxor/options_market_data.git

cd options_market_data

cd web-scraper


# Clean the project (removes target directory)
mvn clean


# Compile the project
mvn compile


# Clean and Compile
mvn clean compile


# Run the application using the exec plugin
mvn exec:java


# Clean, Compile and Run
mvn clean compile exec:java
```


### ChromeDriver and Chrome

Check that you have compatible versions for chrome and chromedriver.

ChromeDriver:

``` 
chromedriver --version

# ChromeDriver 134.0.6998.88 (7e3d5c978c6d3a6eda25692cfac7f893a2b20dd0-refs/branch-heads/6998@{#1898})
```

Google Chrome:

```
/Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --version

# Google Chrome 134.0.6998.89 
```
