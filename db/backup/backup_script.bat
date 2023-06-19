:: MySQL backup script
::
:: DESCRIPTION
::
:: Create a mysqldump gziped file for each database and put them into
:: separate folders.
::
:: DEPENDENCIES
::
:: 7zip for Windows (optional)
::
:: COMMAIL (optional)
::   - http://msdn.microsoft.com/en-us/library/e1y530dz(v=vs.90).aspx
::
:: REFERENCES
::   - http://dev.mysql.com/doc/refman/5.1/en/mysqldump.html
:: ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: ----------------------------------------------------------------------------
:: START
:: ----------------------------------------------------------------------------
 
@echo off
color 0E
title MySQL backup.
 
:: Set some variables
set bkupdir=C:\Users\logau\Downloads\Test\db\backup
::Dir where your SQL executables are (needs to have mysqldump in this foder to work)
set mysqldir="C:\Program Files\MySQL\MySQL Server 8.0\"
set logdir=C:\Users\logau\Downloads\Test\db\logs
:: Database settings
set db=wia_app
set dbuser=gauthier
set dbpass=WIASourcing2021
::Path of your 7zip
set zip="C:\Program Files\7-Zip\7z.exe"
:: The number of day after which they get deleted
set deleteDay=30
set endtime=0
 
:GETTIME
 
:: get the date and then parse it into variables
for /f "tokens=1,2,3 delims=/ " %%i in ('date /t') do (
  set dd=%%i
  set mm=%%j
  set yy=%%k
)
:: get the time and then parse it into variables
for /f "tokens=1,2,3 delims=:" %%i in ('time /t') do (
  set hh=%%i
  set ii=%%j
)
::on récupère les secondes
set ss=%time:~6,2%
:: If this is the second time through then go to the end of the file
if "%endtime%"=="1" goto END
 
:: Create the filename suffix
set fn=_%yy%%mm%%dd%_%hh%%ii%%ss%
 
:: Write to the log file
echo Beginning MySQLDump Process > %logdir%\LOG%fn%.txt
echo Start Time = %yy%-%mm%-%dd% %hh%:%ii%:%ss% >> %logdir%\LOG%fn%.txt
echo --------------------------- >> %logdir%\LOG%fn%.txt
echo. >> %logdir%\LOG%fn%.txt
 
:: Create the backup sub-directory if it does not exist
if not exist %bkupdir%\%db%\ (
echo Making Directory %db%
echo Making Directory %db% >> %logdir%\LOG%fn%.txt
mkdir %bkupdir%\%db%
) else (
echo Directory %db% Exists
echo Directory %db% Exists >> %logdir%\LOG%fn%.txt

::delete backups older than %deleteDay% days
echo Deleting files older than %deleteDay% days
echo Deleting files older than %deleteDay% days >> %logdir%\LOG%fn%.txt
forfiles /p %logdir%\ /s /m *.* /d -%deleteDay% /C "cmd /c echo deleting : @path"
forfiles /p %logdir%\ /s /m *.* /d -%deleteDay% /C "cmd /c echo deleting : @path >> %logdir%\LOG%fn%.txt"
forfiles /p %logdir%\ /s /m *.* /d -%deleteDay% /C "cmd /c del @path"

::delete logfiles older than %deleteDay% days
echo Deleting logs older than %deleteDay% days
echo Deleting logs older than %deleteDay% days >> %logdir%\LOG%fn%.txt
forfiles /p %bkupdir%\%db%\ /s /m *.* /d -%deleteDay% /C "cmd /c echo deleting : @path"
forfiles /p %bkupdir%\%db%\ /s /m *.* /d -%deleteDay% /C "cmd /c echo deleting : @path >> %logdir%\LOG%fn%.txt"
forfiles /p %bkupdir%\%db%\ /s /m *.* /d -%deleteDay% /C "cmd /c del @path"
)

:: Run mysqldump on each database and compress the data by piping through gZip
echo Backing up database %db%%fn%.sql.7z
echo Backing up database %db%%fn%.sql.7z >> %logdir%\LOG%fn%.txt
:: l'option SI de 7zip permet de récupérer le fichier à zipper via l'entrée standard Stdin
%mysqldir%\bin\mysqldump.exe -e --user=%dbuser% --password=%dbpass% -h localhost --databases %db% | %zip% a -si "%bkupdir%\%db%\%db%%fn%.sql.7z">>%logdir%\LOG%fn%.txt

echo Done.
echo Done. >> %logdir%\LOG%fn%.txt

 
:: Go back and get the end time for the script
set endtime=1
goto :GETTIME
 
:END
:: Write to the log file
echo. >> %logdir%\LOG%fn%.txt
echo --------------------------- >> %logdir%\LOG%fn%.txt
echo MySQLDump Process Finished >> %logdir%\LOG%fn%.txt
echo End Time = %yy%-%mm%-%dd% %hh%:%ii%:%ss% >> %logdir%\LOG%fn%.txt
echo. >> %logdir%\LOG%fn%.txt
 
:: Send the log file in an e-mail
:: c:\commail\commail -host=smtp.yourcompany.com -from="server <server@yourcompany.com>" -to=serveradmins@yourcompany.com -subject="MySQL Backup" -msg=%logdir%\LOG%fn%.txt
 
:: ----------------------------------------------------------------------------
:: END advanced