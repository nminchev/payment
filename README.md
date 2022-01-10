# Payment System

The Payment System accepts payment transactions from merchants. The system is implemented via Spring Boot, Hibernate, MySQL, Maven, JUnit. The system has no user interface.

Start the Payment System with the following steps:
- 
- create the DB with 'payment.sql' file located under src/main/resources
- configure MySQL properties in application.properties located under src/main/resources
- configure 'key.public.folder' and 'key.private.folder' in application.properties where the keys are stored on you system
- use the JUnit tests under PaymentControllerTest.java to post transactions to the Payment System


The system support the following functionalities:
-
- Merchant import - New merchants are imported from CSV file 'merchantImport.csv' located under src/main/resources once the application is started. 
- Payment system does not accept transactions for inactive merchants
- For every merchant there are generated 2 RSA keys - PublicKey and PrivateKey and JWT Token during merchant creation
- JWT Token is used to authenticate merchant in every POST request for a payment transaction
- API Authentication used JWT authentication layer
- RSA PublicKey is used to encrypt the payload transaction data posted to the Payment System
- RSA PrivateKey is used to decrypt the payload transaction data in the Payment System
- The system supports Authorize, Charge, Refund and Reversal transactions as JSON POST request
- The system validates every submitted transaction to the system
- There is a background Job for deleting transactions older than an hour