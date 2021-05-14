***
<h1> Install </h1>
<li> install PostgreSQL 13.2 </li>
<li> add settings at <b>application.properties</b> and <b>liquibase.properties</b> </li>
<li> get https://github.com/BalikhinLB/subtitle-ui-public and execute "npm run build"</li>
<li> fix setting in pom.xml maven-resources-plugin </li>
<li> build with mvn spring-boot:run and you will get ui on localhost:8080</li>
<li> username: admin, password: admin</li>

<h1> Future </h1>
It`s a lot of work here. But now program work and it do what i mean.
Now i work on unit tests and then i want to do:
- add email verification;
- timer for deleting not verified users;

