# SqlEight

### Purpose
I wrote this library for my own personal use when needing to query database schemas which I don't own and only read a
 subset of tables / views from. I usually use Hibernate or some other JPA implementation for schemas which I own and
store into. However there are times which a simple query or select is necessary but JPA or a full blown Spring setup is 
too much weight. 

I was interested in using the Apache DbUtils library for this purpose but it's not kept up with modern Java 8 idioms and
I'm really not interested in using inheritance to control the behavior, I favor composition instead.

I wrote this in an afternoon after reviewing some training on generics and lambdas. Its mostly focused on helping with a
common pattern I have when reading a schema I don't own.
 