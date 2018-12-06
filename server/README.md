# Badass Battleship Server
This is the server for Badass Battleship, written in Java plus Spark for our REST API.

## Import Project & run it
This is an Eclipse Maven project. Import the `server` folder (the one this README resides in). Right click on the file named 'pom.xml' and hit 'Run As > Build' and after that, 'Run As > Install'. 

Then, you should be able to start your server by just running the project. The client is hosted with the server,
so make sure to access localhost:4567 after you started everything.
The main file is `Battleship.java`.

## Who Did What
* Tobi Schweiger: Server-side architecture and barebones, match logic, client-side code
* Michael Bracy: Server-side grid generation and generic logic, about page
* Eric Welch: Server-side ship placement logic