@echo off
::Check ftp site for updated versions of files, download them to directories if found, and create symlinks in base dir.
::Gets filenames / versions by comparing data in a comma separated value text file from the ftp server with a local copy
::  of the file from the last download (if it exists). Downloads all files listed in the remote csv file if the local
::  file doesn't exist. Downloads listed files as binary files. Expects the first field in the csv file to be the
::  filename of the files to download, the second field to be the local directory to download the binary files to inside
::  the given LOCAL_DEST_DIR (see below), and the third field to be the name of the symlink to create in LOCAL_DEST_DIR
::  for the downloaded binary file(s). Must be run with administrative privileges to create symlinks. Checks for
::  administrative privileges and relaunches with a prompt to allow administrative privileges if it doesn't have them.
::See comments above VERSION_FILENAME for specifics about the format of the comma separated value file.

::Check for administrative privileges by attempting to create an existing registry key and relaunch with request for
::  administrative privileges if it fails
reg add HKLM /F>nul 2>&1  && (
    goto HAS_ADMIN
) || (
    echo This file requires administrative privileges and starts a new window to request them.
    powershell -ex unrestricted -Command "Start-Process -Verb RunAs -FilePath '%comspec%' -ArgumentList '/c ""%~fnx0""""'"
)
goto :eof

:HAS_ADMIN
setlocal

::Configure options for use
set FTP_HOST=
if not defined FTP_HOST (
    echo FTP_HOST not defined.
    goto :INSTRUCT
)

set FTP_USERNAME=
if not defined FTP_USERNAME (
    echo FTP_USERNAME not defined.
    goto :INSTRUCT
)

set FTP_PASSWORD=
if not defined FTP_PASSWORD (
    echo FTP_PASSWORD not defined.
    goto :INSTRUCT
)

set FTP_SRC_DIR=
if not defined FTP_SRC_DIR (
    echo FTP_SRC_DIR not defined.
    goto :INSTRUCT
)

::Set VERSION_FILENAME to the name of a text file with comma separated values stored on the ftp site that lists the
::  binary files desired to be downloaded. The first field must be the full name of the binary file to be downloaded,
::  though the contents of the entire line will be compared for differences. The second field must be the name of the
::  local subdirectory to create and save the downloaded file in. This field is usually where version information for
::  the binary file should be (see example below). The third field must be the name of the local symlink to create in
::  LOCAL_DEST_DIR that will link to the downloaded file in the created subdirectory of LOCAL_DEST_DIR. If the text file
::  has any lines that should not be used in the comparison (headers, etc), they must be at the top of the file and
::  VERSION_FILENAME_SKIP_LINES should be set to the number of lines that should not be used. The file comparison is
::  done with findstr, which has some bugs that can be worked around by making sure that every line with comparison data
::  ends with a comma and that there is an empty line at the end of the file.  See example file below with two header
::  lines and three comparison lines and note that the comparison lines end with a comma and the file ends with an empty
::  line. VERSION_FILENAME_SKIP_LINES would be set to 2 to skip the headers for this example.
::*************************************************EXAMPLE DATA FILE****************************************************
::#Due to bugs in findstr, end each non-header line with a comma and end the file on a blank line.
::#FileName,FolderName,LocalSymLinkName
::selenium-server-standalone-3.4.0.jar,selenium-server-standalone-3.4.0,selenium-server-standalone.jar,
::chromedriver.exe,chromedriver_win32-2.37.544315,chromedriver.exe,
::geckodriver.exe,geckodriver-v0.20.0-win64,geckodriver.exe,
::IEDriverServer.exe,IEDriverServer_Win32_3.4.0,IEDriverServer.exe,
::MicrosoftWebDriver.exe,MicrosoftWebDriver-5.16299,MicrosoftWebDriver.exe,
::
::***********************************************END EXAMPLE DATA FILE**************************************************

set VERSION_FILENAME=versions.txt
if not defined VERSION_FILENAME (
    echo VERSION_FILENAME not defined.
    goto :INSTRUCT
)

::See --- IMPORTANT --- note below when changing this value.
set VERSION_FILENAME_SKIP_LINES=2
if not defined VERSION_FILENAME_SKIP_LINES (
    echo VERSION_FILENAME_SKIP_LINES not defined.
    goto :INSTRUCT
)

set TMP_VERSION_FILENAME=tmp_%VERSION_FILENAME%
if not defined TMP_VERSION_FILENAME (
    echo TMP_VERSION_FILENAME not defined.
    goto :INSTRUCT
)

set SHORT_V_FN=no_header_%VERSION_FILENAME%
if not defined SHORT_V_FN (
    echo SHORT_V_FN not defined.
    goto :INSTRUCT
)

set TMP_SHORT_V_FN=no_header_tmp_%VERSION_FILENAME%
if not defined TMP_SHORT_V_FN (
    echo TMP_SHORT_V_FN not defined.
    goto :INSTRUCT
)

set FTP_COMMANDS=download.ftp
if not defined FTP_COMMANDS (
    echo FTP_COMMANDS not defined.
    goto :INSTRUCT
)

set DIR_LIST_FILENAME=cur_dir_list.txt
if not defined DIR_LIST_FILENAME (
    echo DIR_LIST_FILENAME not defined.
    goto :INSTRUCT
)

set DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME=req_dir_list.txt
if not defined DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME (
    echo DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME not defined.
    goto :INSTRUCT
)

::Do not put quotes around value of LOCAL_DEST_DIR if it has spaces because usages later are quoted instead.
set LOCAL_DEST_DIR=
if not defined LOCAL_DEST_DIR (
    echo LOCAL_DEST_DIR not defined.
    goto :INSTRUCT
)

::Change directory to LOCAL_DEST_DIR while remembering current directory to come back to later. Quote directory name in
::  case it contains spaces
pushd "%LOCAL_DEST_DIR%"

::Get the VERSION_FILENAME file from the ftp server and save it as TMP_VERSION_FILENAME
echo Getting the remote data file to compare with the local data file.
echo.
call:FTP_DOWNLOAD %VERSION_FILENAME% %TMP_VERSION_FILENAME% ascii

::Check for local version of VERSION_FILENAME. If not found, create directories, download all files, and create symlinks.
if not exist %VERSION_FILENAME% (
    echo.
    echo No local data file found. Downloading all files listed in remote data file.
    echo.
    rem Copy TMP_VERSION_FILENAME data without headers to TMP_SHORT_V_FN so headers will be ignored going forward.
    call:DEL_HEADERS %TMP_VERSION_FILENAME% %TMP_SHORT_V_FN%
    call:MAKE_DIRS %TMP_SHORT_V_FN% %TMP_SHORT_V_FN%
    call:FTP_DOWNLOAD %TMP_SHORT_V_FN% %TMP_SHORT_V_FN%
    echo.
    call:MAKE_LINKS %TMP_SHORT_V_FN% %TMP_SHORT_V_FN%
    goto DL_DONE
)

::Create SHORT_V_FN from VERSION_FILENAME without headers and TMP_SHORT_V_FN from TMP_VERSION_FILENAME without headers
::  to avoid using headers in comparison.

::--- IMPORTANT --- If ftp server's VERSION_FILENAME was updated to add / remove header lines, local VERSION_FILENAME
::  should be updated *before* this file is executed so the *number* of header lines matches the *number* of header
::  lines in ftp server's VERSION_FILENAME along with updating the value of VERSION_FILENAME_SKIP_LINES to the same
::  *number* of header lines. The content of the local VERSION_FILENAME headers doesn't matter, only the *number*, and
::  the ftp server's VERSION_FILENAME will replace the local VERSION_FILENAME so the ftp server's headers will now be in
::  the local VERSION_FILENAME.

call:DEL_HEADERS %VERSION_FILENAME% %SHORT_V_FN%
call:DEL_HEADERS %TMP_VERSION_FILENAME% %TMP_SHORT_V_FN%

::Compare the SHORT_V_FN and TMP_SHORT_V_FN files to see if we need to download anything else.
echo.
echo Comparing local data file to remote data file to see if any downloads are needed.
findstr /V /G:%SHORT_V_FN% %TMP_SHORT_V_FN%

::If ERRORLEVEL=1, the compared files are the same so no files need to be downloaded
if [%ERRORLEVEL%] == [1] goto NO_NEW

::One or more files have a new version, download only those that need to be downloaded
echo.
echo Downloading files. See list above.

call:MAKE_DIRS %SHORT_V_FN% %TMP_SHORT_V_FN%
echo.
call:FTP_DOWNLOAD %SHORT_V_FN% %TMP_SHORT_V_FN%
echo.
call:MAKE_LINKS %SHORT_V_FN% %TMP_SHORT_V_FN%
goto DL_DONE

:DL_DONE
::Output message and cleanup
echo.
echo New / changed files downloaded.
goto :CLEANUP

:NO_NEW
::Output message and cleanup
echo.
echo There are no downloads needed. All files appear to be up to date.
goto :CLEANUP

:INSTRUCT
echo Edit this file to configure required information before running.
goto :FINALLY

:CLEANUP
::Prepare to ask user about deleting directories (and contained files) not listed in TMP_SHORT_V_FN if needed.
::Create list of local directories as DIR_LIST_FILENAME. Make sure to add a trailing comma at the end of each line to
::  work around bug in findstr later. In case of failure that left earlier DIR_LIST_FILENAME around, delete it to start
::  with an empty file instead of appending to an existing file.
if exist %DIR_LIST_FILENAME% del /q %DIR_LIST_FILENAME%
for /D %%N in (*) do (
    echo %%N,>> %DIR_LIST_FILENAME%
)

::Create DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME from TMP_SHORT_V_FN to compare to DIR_LIST_FILENAME with same number of
::  columns. Make sure to add a trailing comma at the end of each line to work around bug in findstr later. In case of
::  failure that left earlier DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME around, delete it to start with an empty file
::  instead of appending to an existing file.
if exist %DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME% del /q %DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME%
for /F "tokens=2 delims=," %%N in ('findstr /G:%TMP_SHORT_V_FN% %TMP_SHORT_V_FN%') do (
    echo %%N,>>%DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME%
)

::Find directories not listed in DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME and ask user if they should be deleted, default
::  answer to keep them.
setlocal EnableDelayedExpansion
::Only output delete directories instructions if there are directories to delete, track with env variable showing if
::  instructions have been displayed. Escape command characters with caret due to EnableDelayedExpansion and set /P.
for /F "delims=," %%N in ('findstr /V /G:%DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME% %DIR_LIST_FILENAME%') do (
    echo.
    if not defined _INSTRUCTED (
        set _INSTRUCTED=y
        echo Do you want to delete existing local directories ^(and contained files^) not listed in updated %VERSION_FILENAME%^?
        echo Note that these directories ^(and contained files^) may no longer be available on the ftp server.
        echo Also note that any symlinks that point to these directories ^(or contained files^) will be left alone.
        echo.
    )
    rem Quote directory name in case it contains spaces.
    set /P DELETE_UNLISTED=Delete "%%N" [y/n] ^(default is n^)^? ^:
    if /I [!DELETE_UNLISTED!] == [y] (
        rem If %%N contains a space, it must be quoted to be deleted so quote all.
            echo Deleting "%%N".
            rd /s /q "%%N"
    ) else (
        echo Keeping "%%N"
    )
)
endlocal

::Delete VERSION_FILENAME and rename TMP_VERSION_FILENAME to VERSION_FILENAME to keep track of the changes. Note that
::  the timestamp on local copy of VERSION_FILENAME will show the last time this file was executed since the ftp server
::  copy of VERSION_FILENAME is always downloaded and kept as the local VERSION_FILENAME afterward.
if exist %VERSION_FILENAME% del /q %VERSION_FILENAME%
ren %TMP_VERSION_FILENAME% %VERSION_FILENAME%

::Delete files that are no longer needed
if exist %FTP_COMMANDS% del /q %FTP_COMMANDS%
if exist %SHORT_V_FN% del /q %SHORT_V_FN%
if exist %TMP_SHORT_V_FN% del /q %TMP_SHORT_V_FN%
if exist %DIR_LIST_FILENAME% del /q %DIR_LIST_FILENAME%
if exist %DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME% del /q %DIR_LIST_FROM_TMP_SHORT_V_FN_FILENAME%

::Return to starting directory and pause to allow review of completed commands in case a new window was launched
popd
echo.
echo Done! Pausing for review of results.

:FINALLY
pause
endlocal
goto :eof

:FTP_DOWNLOAD
::%1 is original file for comparison, %2 is (possibly) newer file for comparison, %3 is ftp download mode default binary
echo open %FTP_HOST%> %FTP_COMMANDS%
echo user %FTP_USERNAME% %FTP_PASSWORD%>> %FTP_COMMANDS%
::Quote directory name in case it contains spaces
echo lcd "%LOCAL_DEST_DIR%">> %FTP_COMMANDS%
echo cd %FTP_SRC_DIR%>> %FTP_COMMANDS%
echo hash>> %FTP_COMMANDS%

::Configure download mode
if [%3] == [] (
    echo binary>> %FTP_COMMANDS%
) else (
    echo %3>> %FTP_COMMANDS%
)

::Check for existence of %2 and download it if not found, otherwise use it in comparison of what to download and where
::  to put each download.
::If %2 exists and is equal to %1, do findstr /G (no existing files, get all), else do findstr /V /G (only get updates).
if exist %2 (
    if [%1] == [%2] (
        for /f "tokens=1,2 delims=," %%N in ('findstr /G:%1 %2') do (
            rem Quote directory name in case it contains spaces
            call:FTP_FILES_TO_GET %FTP_COMMANDS% "%%O" %%N
        )
    ) else (
        for /f "tokens=1,2 delims=," %%N in ('findstr /V /G:%1 %2') do (
            rem Quote directory name in case it contains spaces
            call:FTP_FILES_TO_GET %FTP_COMMANDS% "%%O" %%N
        )
    )
) else (
    echo get %VERSION_FILENAME% %TMP_VERSION_FILENAME%>> %FTP_COMMANDS%
)
echo quit>> %FTP_COMMANDS%
ftp -n -i -s:%FTP_COMMANDS%
exit /b

::Helper function for FTP_DOWNLOAD to input commands to change to download directory, get file, change to base directory.
::%1 is file to add commands to, %2 is directory to download in, %3 is file to download.
:FTP_FILES_TO_GET
::Don't quote directory name here. Passed directory name should be quoted instead.
echo lcd %2>> %1
echo get %3>> %1
echo lcd ..>> %1
exit /b

::Copy / append file contents to other file possibly removing first lines from input file. Depends on
::  VERSION_FILENAME_SKIP_LINES.
::%1 is input file name, %2 is desired output file name.
:DEL_HEADERS
::If VERSION_FILENAME_SKIP_LINES = 0, just copy the file, otherwise use for loop to skip VERSION_FILENAME_SKIP_LINES.
::  Note that for loop with skip=0 gives error message.
if %VERSION_FILENAME_SKIP_LINES% EQU 0 (
    copy %1 %2
) else (
    for /F "skip=%VERSION_FILENAME_SKIP_LINES% tokens=*" %%F in (%1) do (
        echo %%F>>%2
    )
)
exit /b

::Make needed directories to store files to be downloaded.
::%1 is original file for comparison, %2 is (possibly) newer file for comparison.
:MAKE_DIRS
::If %1 is equal to %2, do findstr /G (no existing files, make all dirs), else do findstr /V /G (only get updated dirs).
::Quote directory names in case they contain spaces.
if [%1] == [%2] (
    for /F "tokens=2 delims=," %%N in ('findstr /G:%1 %2') do (
        if not exist "%%N" md "%%N"
    )
) else (
    for /F "tokens=2 delims=," %%N in ('findstr /V /G:%1 %2') do (
        if not exist "%%N" md "%%N"
    )
)
exit /b

::Make symlinks in base directory to listed files in their subdirectories, including deleting existing symlinks if new
::  symlinks need the same name. This section requires administrative privileges to use mklink.
::%1 is original file for comparison, %2 is (possibly) newer file for comparison.
:MAKE_LINKS
::If %1 is equal to %2, do findstr /G (no existing files, make all links), else do findstr /V /G (only do updated links).
::Quote directory\filenames in case directory names contain spaces.
if [%1] == [%2] (
    for /F "tokens=1,2,3 delims=," %%N in ('findstr /G:%1 %2') do (
        mklink %%P "%%O\%%N"
    )
) else (
    for /F "tokens=1,2,3 delims=," %%N in ('findstr /V /G:%1 %2') do (
        if exist %%P (
            del %%P
        )
        if exist "%%O\%%N" (
            mklink %%P "%%O\%%N"
        )
    )
)
exit /b
