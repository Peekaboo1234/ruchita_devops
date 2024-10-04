####Installing JDK 17 (OpenJDK)
You can download OpenJDK 17 from the official OpenJDK website or a trusted mirror. Once downloaded, follow the installation instructions provided for your operating system.

###After installing java
Set a path in Environment variables(System Variables) in My Computer
Variable Name: JAVA_HOME
Variable value: C:\Program Files\Java\jdk-17 (location of the Java installation directory)

On Both User and System Variables
Variable Name: Path
Variable value: %JAVA_HOME%\bin (location of bin folder inside Java installation directory)

Try to run the command "javac" in command prompt


### Installing TestNG in Eclipse
Go to Eclipse Help and click on "Install New Software".
Click the "Add" button.
Enter "TestNG" as the name and use "http://dl.bintray.com/testng-team/testng-eclipse-release/" as the location.
Select the TestNG entry and click "Next" to install.
Accept the license agreement and complete the installation process.


### Installing Apache Maven 3.9.6
Download Apache Maven 3.9.6 from a trusted mirror.
Extract the downloaded zip file to a location on your system.

Set the following Environment variable(System Varaibles) in My Computer
Variable Name: M2_HOME
Variable value: C:\Program Files (x86)\apache-maven-3.9.6 (location of the Maven installation directory)

add a path in Environment variables(User Varaibles) in My Computer
Variable Name: Path
Variable value: %M2_HOME%\bin (location of bin folder inside Maven installation directory)

Try to run the command "mvn -version" in command prompt


#For runnning the complete project 
open the project and open the command prompt on that project
run the command ' mvn clean install '


#For running the testNG file
open the project and open the command prompt on that project
run the command 'mvn clean test -Dsurefire.suiteXmlFiles=FileName(Either ParallelSuite.xml OR GroupingSuite.xml)'





