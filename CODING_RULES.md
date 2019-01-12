Coding Rules
------------

We'll flesh this out into a real coding standard soon but, for now, here are a few rules that programmers should adopt when working in the robot_code repository.

1. Do not commit any changes until Eclipse shows no (absolutely none, zero) errors or warnings when building the SpringKonstant project. Errors are pretty obvious to avoid but warnings are there for a reason and, if you leave any visible, people tend to start ignoring them and, hence, miss the ones that are really important.

2. Indent by 4 spaces.

3. Please don't use tabs in your code since this messes up indentation on other people's editors if they don't have the same settings as you. See README.md which contains information on how to configure Eclipse to use spaces rather than tabs for intentation.

4. If modifying an existing file, adhere to the bracing convention already adopted there (we can argue about the prefered style later :-) )

5. Fully brace all code. Don't, for example, think you can get away without the braces if you only have a single line following an "if" statement. Although this is syntactically legal, it's a great way to inject unintentional errors.

6. Use Windows-style (CR+LF) line delimiters. Most of the programmers working on the project are Windows-based and some Windows editors get confused by Linux/MacOS style line delimiters. This can be configured in Eclipse in the Preferences/General/Workspace window under "New text file line delimiter" and problems in existing files can be fixed from the Eclipse menu via File/Convert Line Delimiters To/Windows.

7. Use UTF-8 text file encoding. This can be configured in Eclipse in the Preferences/General/Workspace window under "Text file encoding".

8. Use the proper, God's-intended coding style of putting brackets in vertical alignment when writing ifs, elses, methods, classes, etc.

For javadoc documentation, every class will be preceded with a javadoc comment (/**) and be followed by a short description of the function of the class.
When finished with your comment, it can be ended with "*/". For those interested, html commands like <p>text</p> is available
Every method will have documentation according to the following format:
/**
 * The first part of the comment will be a short description of the method's function
 * After you've typed your description, it will be followed by the parameters
 * @param paramName paramDescription (description must include potential values that the parameter can encompass)
 * Note: if there are no parameters, type @param None
 * @return returnType returnDescription
 * Note: if void, type @return Nothing.
 * If any exception is thrown, finish with 
 * @exception exceptionType exceptionDescription (i.e. when it is thrown)
 */

When you want to generate javadocs, open a terminal and type javadoc Robot.java
Alternatively, go to eclipse, open the Project tab, and click "Generate JavaDoc"

EXAMPLE:

/**
 * This class is an example of how to write javadocs
 */
public class Test
{
	/**
	 * This method adds two integers and returns the sum of those integers.
	 * Both the parameters and the return value are integers.
	 * It's also a static method, so it should be referenced from a class pointer
	 * @param num1 This is the first number that the user passes and must be an int
	 * @param num2 This is the second number to be passed and also must be an int
	 * @return int Returns the integer sum of the two parameters
	 */
	public static int addNum(int num1, int num2)
	{
		return num1+num2;
	}
}
Notes:
Add SK19 to the start of all main subsystems directly to the robot, as well as Capitalize the first letter of every word of class names.
