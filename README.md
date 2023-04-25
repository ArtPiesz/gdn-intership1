# Currency exchange rates application
This application uses NBP Web API to provide data about exchange rates of currencies.
## Usage
* to start the server, run this command after building project:
 
        java -jar gdn-intership1.jar
* To query operation 1, run this command (which should have *{"averageExchangeRate":5.2768}* as the returning information):
        
        curl http://localhost:8888/average-exchange-rate/{CC}/{DATE}
        
        CC - currency code
        DATE - date as YYYY-MM-DD
        
        as in example:
        
        curl http://localhost:8888/average-exchange-rate/GBP/2023-01-02
* To query operation 2, run this command (which should have *{"min":x,"max":y}* as the returning information, x,y values depend on last N quotations):
        
        curl http://localhost:8888/minmax-average-value/{CC}/{N}
        
        CC - currency code
        N - number of last quotations
        
        as in example:
        
        curl http://localhost:8888/minmax-average-value/GBP/10
* To query operation 3, run this command (which should have *{"maxDifference":x}* as the returning information, x value depends on last N quotations):

        curl http://localhost:8888/major-difference/{CC}/{N}
        
        CC - currency code
        N - number of last quotations
        
        as in example:
        
        curl http://localhost:8888/major-difference/GBP/10
