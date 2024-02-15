
## questions

1. I have an interesting case for calculating tax congestion. 
For example, what would be with tax if the owner of car would leave his car at parking station for night? Is it okey we charge him 2 times? 
I mean 2 passages Across Midnight. Or another one question: how do we need to consider these passages? 
Test Case: Passages like 23:50 on one day and 00:10 on the next. 

2. Any way to use third party services to get public holidays base on the timezone?

3. For now, I have removed classes were inherited from Vehicle but I am assuming that in the future some 
congestion taxes could depend on the configurations of vehicles. WDYT?

## extra work I would like to do in case have extra times

1. I would extract some city rules for congestion tax calculator 
 some additional date rules like weekends or months(I still have them CongestionTaxCalculator.isTollFreeDate(), 
would be nice to extract them to the endpoint params)

3. I would make my code safer, for example handling various scenarios in the endpoints,
but first you need to decide which parameters will be mandatory.

6. Maybe make sense to pass a configuration file instead of passing different separate params for init step.

7. Of course, I would cover more code by tests with IT