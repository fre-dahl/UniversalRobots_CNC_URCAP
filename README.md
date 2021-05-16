
#About

This README is meant to be a source of documentation on the "CNC URCap",
based on the DMH-UR-LIB: master.script.

The "CNC URCap" is made to provide a better end user experience of the already functioning
current solution. By providing polyscope with a more intuitive graphical user interface,
thus streamlining the process of setting up future robot programs for communication
between the UR Robot and the CNC machine.

Made by Seal student group: URCap 2020,
with help from Daniel Christopher Lindquist Nilsen. Engineer. Seal Engineering.

Group Members:
Daniel Nikolai Fiko . Student.
Cezary Kalbarczyk. Student.
Frederik Dahl. Student.

University Faculty:
Haris Jasarevic. Engineer.

Thanks to Seal Engineering, Daniel C. Lindquist Nilsen and Haris Jasarevic for the opportunity
and for providing us with helpful information, technical expertise and resources.


#The URCaps Starter Package (short)

The Starter package is a development environment provided by Universal Robots.
complete with Eclipse Java IDE, URCaps Software Development Kit (SDK) and URSim.

This was our foundation for development, run on a Viritual OS. Featuring a complete framework
for developing, testing and building an URCap. Also provided within:
Extensive documentation with examples from specific use-cases of various aspects of the API.

[Universal Robots+](https://www.universal-robots.com/plus/developer/)


#The URCap

The CNC URCap is composed of:

##installation
An installation node that provides a user interface to enable/disable Robot Service Call polling
and functionality for the polyscope program screen to insert nodes corresponding to urscript
functions (master.script).
When disabled, no CNC background threads are run and no CNC program nodes are allowed.

##program
A program node service with currently 4 alternative contributions to a robot program:
(ie. CNC: [functionality name] ([urscript eqvivalent]))

CNC: Open Door (executeOpenDoor())
CNC: Close Door (executeCloseDoor())
CNC: Open Chuck (executeUnclamp())
CNC: Close Chuck (executeClamp())

These nodes are selected from a drop-down menu(1.0.1Beta), or buttons(1.0.2current)
In addition, the UI show whether or not the CNC functionality in enabled:
1.0.1 Beta: "enabled/disabled" shows up on screen.
1.0.2 current: When CNC disabled, buttons are greyed out.

The 1.0.1 version has a drop-down menu, while 1.0.2 has buttons.
The 1.0.1 and 1.0.2's CNCInstallationNode classes are identical, but the
CNCProgramNode java classes differ and are much simpler for in 1.0.2.


#									The Java Program (Preface)

##Disclaimer
The Jave project use (Version 1.11.0) of the API, with all dependencies already set up
on building a new URCap. Maven functionality is already set up for installing on Ursim for
testing or building the project as a .urcap for production. Universal Robots recommend using
the Starter Package as the framework for development and using the latest version of the API.

##Naming convention
The recommended practice for naming classes is followed throughout the CNC URCap.
While reading the different classes defined in the program, keep that in mind while
considering the general flow of a URCap in polyscope described below.

[name of the urcap] [type] [service, contribution or view]
(ie. CNCProgramNodeContribution)

##URCap and the general flow of a program
A URCap is a software bundle, that is operating as a child process of polyscope.
From Universal Robots+ site:

>PolyScope will register the URCap, and interact with the URCap in various ways, e.g. during the
>startup of PolyScope, or based on user interactions in the user interface.

>Each URCap can contribute multiple new functionalities to PolyScope, a such functionality is
>generally referred to as a service. A service could for instance be a Program Node,
>an Installation Node or a Toolbar. As an example, a gripper URCap could include a service which
>is an Installation Node, e.g. for configuring the mounting of and connection to the gripper.
>Another service offered by this URCap could be a program node, which could be used to open the
>gripper, and a similar service, another program node, but for closing the gripper. The fourth
>service could be a toolbar, that allows live control of the gripper. In this way, one URCap is
>offering 4 services to PolyScope; 1 Installation Node Service, 2 Program Node Services
>and 1 Toolbar Service.

Activator:

When polyscope starts up it looks for installed URCaps and calls the Activator in each through
the start() method, where it registers it's services.

Service:

This class set some static properties (ie. allowing children in a program contribution)
and provides it's respective "service" to the URCap:

  creates the UI
  creates the contribution(installation)/contributions(program)

User Interface ("View" in the case of Swing):

This is the class where you handle the various GUI elements and event-listeners
of the node. There is only One instance of this object per. service.
But through the Data Model and current contribution, updates it's content on event-triggers.

>When the type of service is needed e.g. for a Program Node, when a user inserts it into a
>program, PolyScope calls the methods in the Service, to create this node.
>This will create a User Interface, and a Contribution.

Contribution:

The contribution is the logic, or "controller" code. It has a Data Model that store the
various configurations of the node. When the user performs a specific action in the UI, the
UI will report this to the active contribution, which will then store the settings in it's
Data Model.

When the user executes a robot program the contribution generates the URScript based on
the data stored in the Data Model. In the case of Installation contribution, before the program.
In the case of Program contribution, the respective position in the program tree.


(The URCap API is very much designed on the principle of "Model View Control")

#									The Java Program (CNC URCap)

With the preface in mind, the CNC URCap has communication between the Installation contribution
and the Program contributions as the program nodes only execute when the CNC functionality is
enabled in the Installation.

##Activator.java

Registers the Swing-based services:

  - CNCInstallationNodeService
  - CNCProgramNodeService

on Polyscope startup.

##CNCInstallationNodeService.Java

Sets the Header-title of the the URCap in Polyscope and creates instances of:

  - CNCInstallationNodeContribution (contribution)
  - CNCInstallationNodeView (view)

passing on the required arguments. In the case of the contribution:

  - InstallationAPIProvider (Not used in the contribution)
    This provides an extension of the base Application API with functionality specific
    to the Installation node.
  - CNCInstallationNodeView
    This provides access to the GUI
  - DataModel (basically a series of hashMaps for storing various variable types)
    Stores whether or not the CNC checkbox is checked

##CNCInstallationNodeContribution.Java

Public methods:

getListenerForCheckBox()  creates and returns an ItemListerer for the view. On state change,
                          (box checked/unchecked) calls the method setCheckBoxEnabled(boolean)

isCheckBoxEnabled()       calls the model and returns if the checkbox is enabled. This is
                          used both private and public (program contribution)
                          default is false.

openView()                this gets called when you open the CNC URCap in
                          polyscope's Installation menu. It calls the setEnableCheckbox(boolean)
                          method in view.

generateScript(ScriptWriter)  this calls the private method writeScript(ScriptWriter) if the
                          CNC functionality is enabled. This gets called in robot program
                          before start.

private methods:

setCheckBoxEnabled(boolean)   This is called when the checkbox is changed in the view,
                          and updates the model.

writeScript(ScriptWriter) This gets called in generateScript(ScriptWriter) if CNC is enabled.
                          This is where the CNC URScript is generated through the
                          CNCScriptLibrary class' static methods.

##CNCInstallationNodeView

This class mostly handle the creation of Java Swing UI elements, for the Installation view.

the main method is: buildUI(JPanel, CNCInstallationNodeContribution) which populate the
view with a description and checkbox. The contribution is further passed in as an argument
to the private createCheckBox(CNCInstallationNodeContribution) method to get access to the
contribution's getListenerForCheckBox() method.

setEnableCheckBox(boolean checked) is called on opening the view.

##CNCProgramNodeService.Java

This class has 3 primary functions:

1. Creates the Program Node View (only one instance).
2. Creates the Program Node Contributions. As many instances as you have of the node in the
  program-tree. Instances are created on adding new nodes to the program-tree.
3. Determines whether or not to have nodes contain children nodes. Either by populating it
  with child-nodes in polyscope or by having the program node insert them on creation.

  After the service has been registered by polyscope through the Activator, this setting
  cannot be undone. A ProgramNodeContribution either has children allowed OR not.

##CNCProgramNodeContribution.Java (1.0.2)

The constructor takes 3 arguments of the following types:

1. ProgramAPIProvider
  gives access to the programAPI and the UndoRedoManager (keeps track of changes in the node).
2. CNCProgramNodeView
  gives access to the view to handle what happens to the view once this contribution-instance
  are opened/closed in polyscope.
3. DataModel
  A generic collection of various hashmaps. This is the where all the data is stored and gathered.

Enum Function:            Holds the name and urscript definitions of the functions.

onButtonAction(String)    stores the value represented by a pressed button in the DataModel.

getFunction()             returns the stored Function name from the DataModel. Default is
                          the "placeholder".
-----------------------------------------------------------------------------------------------
getInstallation()         through the programAPI, the method returns the
                          CNCInstallationNodeContribution. Used to get access to installation
                          contribution methods...

getInstallationStatus()   ...like the isCheckBoxEnabled() method. This checks if the CNC
                          functionality is enabled.
-----------------------------------------------------------------------------------------------
assignDefinition(ScriptWriter)  assigns the correct urscript to be written based on what
                          the current value stored in the DataModel.

openView()                On opening the view in polyscope, the buttons are enabled or greyed
                          out depending on the "getInstallationStatus()"

getTitle()                The title of the respective node.

isDefined()               returns whether or not the program can be run. It will return false
                          if the node is a placeholder (no button pressed) or if CNC is
                          disabled.

generateScript(ScriptWriter)  writes the urscript.


##CNCInstallationNodeView

This class is pretty self-explanatory in v.1.0.2.

buildUI(JPanel, ContributionProvider) This main method builds the UI and adds it to the Jpanel

setButtonsEnabled(boolean)  this is used in the contribution to determine if the buttons
                          are enabled (can be pressed).

createButtons(...JButtons, ContributionProvider)  creates the button panels and creates the
                          actionlisteners to execute the contributions' onButtonAction()
                          method provided by the ContributionProvider.


UTILIY CLASSES:


##CNCScriptLibrary

A "library" of the functions and threads rewritten from "master.script" and divided into
logical static methods. Used by in the CNCInstallationNodeContribution.

##Style

Another Utility class with reusable styling preferences. Not really necessary in this Urcap.

##IOHandler

This was a class i made to get access to the robots digital IO. In hope of showing the state of
chunk and door in polyscope. But the Input event driven nature of the API makes it impossible
to show this in real-time. At least within the program-node and installation-node programAPI.
But in cases you would like to know an IO on some button press or entering a view this class
can provide for that.
