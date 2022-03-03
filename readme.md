## How GUI interacts with database
### SERVER 
* We always grab an **entire list** and then parse the list inside the .java
* Gets 'item', 'price', 'name' via item number in menu_key
  * E.g., passing '501' to get '501' (for checking), '5 finger original', '$6.00'
* Gets 'quantity' via item number in weeksales
  * weeksales unique IDs are in `date_itemNum` format, so there's a clientside (server gui side) date variable
* Updates 'quantity' in the same way
### MANAGER
