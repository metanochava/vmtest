# vmtest


1 - Install maven and git on your computer;

2 - clone the directory in your computer's folder using this command -> git clone https://github.com/metanochava/vmtest.git;

3 - At the root of the cloned directory run the command "mvn clean install";

4 - Download the glassfish server and install it;

5 - copy the api.war file found in the target folder generated in the compilation of the cloned project to the glassfish server folder "\ domains \ domain1 \ autodeploy"

6 - find the file "asadmin.bat" infolder bin of glassfish server and click twice, then run the command "start-domain" to start the server;

7 - Download the postman and install.

8 - Import the postaman file "VM API.postman_collection.json" which is at the root of the cloned directory to test the api according to the exercise.
