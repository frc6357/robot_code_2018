# frc6357/robot_code


Continuous Integration:
-----------------------

Please also note that the Master branch requires Travis CI builds. This is a process that is intended to protect our code. Do all development on the "develop" branch. Select the develop branch by changing into your robot_code_2018 directory and typing "git checkout develop". 

Setting up Eclipse:
-------------------

NB: At the time of writing, the repo is essentially empty so don't bother trying to set up the Eclipse project yet! You can follow the instructions on setting up your editor, though.

The root of the Eclipse project is the SpringKonstant folder. After you clone the robot_code project from git, open Eclipse and choose "File/Open Projects from File System..." and choose your local robot_code/SpringKonstant folder. This should result in a project named SpringKonstant in your Eclipse Package Explorer.

The Eclipse project file will assume that you have wpilib installed in the default location, ~/wpilib on Linux/macOS or the wpilib directory under your home directory on Windows. Make sure that you have this set up before you start. You will also need to copy various user libraries into the wpilib/user/java/lib directory to ensure that your code will link.


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

