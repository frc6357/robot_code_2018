# frc6357/robot_code

The Spring Konstant 6357 Git Repo
-------------------------------

The Spring Konstant is a second year team from Dripping Springs, Texas. Our team is comprised of 40 students. In 2017 we went to World Championships, in Houston. Our team has been heavily involved in our community, striving to influence the next generation of STEM professionals. We have taken part in many community events, such as science night, Founders Day, and STEM day, and we started our very own FTC team this year, Team 13760, at Sycamore Springs Middle School.

GIT and Continuous Integration:
-------------------------------

Please also note that the git master branch requires Travis CI builds. This is a process that is intended to protect our code - when you check in a change, the project is automatically built and pre-configured tests run to determine if it's a "good" commit. To ensure that this process owrks correctly, it is imperative that you do all development on the "develop" branch. Select the develop branch by changing into your robot_code_2018 directory and typing:

git checkout develop

Changes will be promoted from the develop branch to the master branch when they are known to be good.

Installing Tools and Libraries:
-------------------------------

If you have no already done so, install Eclipse for Java and the FRC Plugins by following the instructions at https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/599679-installing-eclipse-c-java. This will place a copy of wpilib (the large set of Java and C++ libraries provided by FIRST to make our software job a lot easier) into your home directory. To allow us to share the Eclipse project file successfully, we already have a copy of WPILib inside the robot_code_2018 directory tree, however, and the project will use this copy.

Before doing anything else, start Eclipse and turn OFF the WPILib's "AutoManage User Libraries" setting. Find this under the "WPILib Preferences" panel in Eclipse preferences (from the "Window/Preferences..."" menu on Windows or "Eclipse/Preferences..." on MacOS). Failure to do this before importing the project WILL BREAK OTHER PEOPLE so please, please make sure this setting is disabled!

Update your local copy of the Robotbuilder tool with extensions to support components from CTRE. You can find a Windows installer for this at http://www.ctr-electronics.com/downloads/installers/CTRE%20Phoenix%20Framework%20v5.1.3.1.zip or, if you're using Linux or macOS, a ZIP of the necessary files at http://www.ctr-electronics.com//downloads/lib/CTRE_Phoenix_FRCLibs_NON-WINDOWS_v5.1.3.1.zip. If installing manually, unzip the downloaded file to a temporary directory then follow the Robotbuilder instructions in README.txt. Note that the Robotbuilder/extensions directory mentioned will likely not exist if you have not already run Robotbuilder so you may have to create it. There is no need to install the Java libraries from the "java" directory of the CTRE package because these are already included in the robot_code_2018 repo.

Importing the Eclipse Project:
------------------------------

The root of the Eclipse project is the robot_code_2018/SpringKonstant folder. After you clone the robot_code_2018 project from git, make sure you are on the "develop" branch (from a command prompt, change to your robot_code_2018 directory and type "git checkout develop") then open Eclipse and choose "File/Open Projects from File System..." and choose your local robot_code_2018/SpringKonstant folder. This should result in a project named SpringKonstant in your Eclipse Package Explorer.

The repo contains a copy of the WPILib libraries (JARs) and all other third-party libraries it uses so you should not need to modify the project Build Path to build it on your own machine. If you see problems, please speak to Mr. Wilson who has been fighting with settings to make the project file path-independent for weeks!

Recommended Editor Settings:
----------------------------

Use of tab characters in source files is the root of all formatting evil and is to be avoided at all costs. To ensure that you do not inadvertently include tabs, you can configure the Eclipse editor to show whitespace characters thus making tabs easily visible. Do this from Eclipse preferences: 

Select Editors/Text Editors in the left pane then make the following settings:

"Displayed tab width"        - 4
"Insert spaces for tabs"     - Checked
"Show whitespace characters" - Checked

Select Java/Code Style/Formatter in the left pane then press "Edit.." and make the following settings:

"Profile name"              - Eclipse SpringKonstant
"Tab policy"                - Spaces only
"Indentation size"          - 4
"Tab size"                  - 4

 then press "Apply and Close" to make the change take effect. If you don't like the default settings for "Show whitespace characters", you can press "configure visibility" next to its checkbox to set the opacity of the whitespace characters and to turn on or off particular cases. Leave all "Tab" options enabled, however! 

You may also like to install the AnyEdit Tools plug-in which adds a bunch of useful editor features including the ability to swap tabs for spaces. Read about this plugin and install it from http://marketplace.eclipse.org/content/anyedit-tools.


