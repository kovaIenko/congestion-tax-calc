# Congestion Tax Calculator

Welcome the Volvo Cars Congestion Tax Calculator assignment.

This repository contains a developer [assignment](ASSIGNMENT.md) used as a basis for candidate intervew and evaluation.

Clone this repository to get started. Due to a number of reasons, not least privacy, you will be asked to zip your solution and mail it in, instead of submitting a pull-request. In order to maintain an unbiased reviewing process, please ensure to **keep your name or other Personal Identifiable Information (PII) from the code**.


Run up the application. 

You have to know that now *congestion-tax-calc* is Spring Boot based app with Java 17. 
You don't need to install any additional libs.

1. open this app with your IDE
2. Start this java app with entry class CongestionTaxCalcApplication as a Spring app
3. You will find the tests of the controller and its endpoints
4. In order to check app is upp, you should be able to get a response "We are healthy" by GET http://localhost:8080/health
5. Then we have to initialize city toll rules with PUT http://localhost:8080/tax-calculation-city-rules
   ```json
   {
   "city": "Gothenburg",
   "dateformat": "yyyy-MM-dd HH:mm:ss",
   "public_holidays": [
   "2013-01-01",
   "2013-03-28",
   "2013-03-29",
   "2013-04-01",
   "2013-04-30",
   "2013-05-01",
   "2013-05-08",
   "2013-05-09",
   "2013-06-05",
   "2013-06-06",
   "2013-06-21",
   "2013-11-01",
   "2013-12-24",
   "2013-12-25",
   "2013-12-26",
   "2013-12-31"
   ],
   "city_toll_periods": [
   {
   "startHour": 6,
   "startMinute": 0,
   "endHour": 6,
   "endMinute": 29,
   "fee": 8
   },
   {
   "startHour": 6,
   "startMinute": 30,
   "endHour": 6,
   "endMinute": 59,
   "fee": 13
   },
   {
   "startHour": 7,
   "startMinute": 0,
   "endHour": 7,
   "endMinute": 59,
   "fee": 18
   },
   {
   "startHour": 8,
   "startMinute": 0,
   "endHour": 8,
   "endMinute": 29,
   "fee": 13
   },
   {
   "startHour": 8,
   "startMinute": 30,
   "endHour": 14,
   "endMinute": 59,
   "fee": 8
   },
   {
   "startHour": 15,
   "startMinute": 0,
   "endHour": 15,
   "endMinute": 29,
   "fee": 13
   },
   {
   "startHour": 15,
   "startMinute": 30,
   "endHour": 16,
   "endMinute": 59,
   "fee": 18
   },
   {
   "startHour": 17,
   "startMinute": 0,
   "endHour": 17,
   "endMinute": 59,
   "fee": 13
   },
   {
   "startHour": 18,
   "startMinute": 0,
   "endHour": 18,
   "endMinute": 29,
   "fee": 8
   },
   {
   "startHour": 18,
   "startMinute": 30,
   "endHour": 5,
   "endMinute": 59,
   "fee": 0
   }
   ],
   "free_vehicle_types": [
   "Motorcycle",
   "Tractor",
   "Emergency",
   "Diplomat",
   "Foreign",
   "Military"
   ]
   }
6. In case got the previous response you are able to use remove calculator for tax congestion.
   You have to send a POST http://localhost:8080/congestion-tax-calculation, please use body of request as a JSON:
   ```json
   {
   "city": "Gothenburg",
   "vehicle": {
   "type": "Car"
   },
   "passages": [
   "2013-01-14 21:00:00",
   "2013-01-15 21:00:00",
   "2013-02-07 06:23:27",
   "2013-02-07 15:27:00",
   "2013-02-08 06:27:00",
   "2013-02-08 06:20:27",
   "2013-02-08 14:35:00",
   "2013-02-08 15:29:00",
   "2013-02-08 15:47:00",
   "2013-02-08 16:01:00",
   "2013-02-08 16:48:00",
   "2013-02-08 17:49:00",
   "2013-02-08 18:29:00",
   "2013-02-08 18:35:00",
   "2013-03-26 14:25:00",
   "2013-03-28 14:07:27"
   ]
   }
      ```
