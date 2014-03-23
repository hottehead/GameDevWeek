@echo off
echo Start 1 Server at default Port, and 2 Clients.
echo need data folder and a GameDevWeek.jar
echo - this folder -
echo +- data
echo +- GameDevWeek.jar
echo +- runGDW.bat
start /b java -cp GameDevWeek.jar de.hochschuletrier.gdw.ws1314.Main -server
start /b java -cp GameDevWeek.jar de.hochschuletrier.gdw.ws1314.Main
start /b java -cp GameDevWeek.jar de.hochschuletrier.gdw.ws1314.Main
