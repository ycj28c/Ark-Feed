# Ark-Feed
This quick project is used to monitor the Ark ETF operation, report to personal slack if there are any shares changes.  
It is using github action as scheduler, the tmp files are stored in Amazon S3, everything is automatically.  

PS: Didn't find way to run Java main class by github action, so the program is running by "mvn test" way.  
PS: The connection configuration and authorization setting can pass by Maven, the value could encrpy by github secrets.  

# Resource
[Ark Invest](https://ark-funds.com/)  
[Slack](https://slack.com/)  
