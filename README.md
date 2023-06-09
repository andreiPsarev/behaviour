<div style="text-align: center;">

![Universe](./_images/logo.png)
</div>

## Introduction
This project is a Java-based application built with the Javalin framework. Its primary goal is to provide a platform for users to create their own world by sharing their thoughts, experiences, and impressions with their audience. With this blog, users can share their ideas with the world and connect with others who share similar interests.

## Features
* **User authentication:** users can [sign up](./_images/sign-up.png), [sign in](./_images/sign-in.png), and [sign out](./_images/sign-out.png) securely.
* **Password recovery:** users can [reset their password](./_images/reset-password.png) if they forget it.
* **Account management:** users can [edit their account](./_images/account-editing.png) information.
* **Create and publish posts:** users can [write and publish posts](./_images/posting.png) on their own blog.
* **Leave comments:** users can [comment on posts](./_images/commenting.png) and interact with other readers.
* **View posts and comments:** users can [view their own posts, comments](./_images/view-posting.png), as well as other users' posts and comments.

## Getting Started

### Prerequisites
To build and run this project, you will need:
* [Java 17+](https://download.java.net/openjdk/jdk17/ri/openjdk-17+35_windows-x64_bin.zip)
* [Maven](https://dlcdn.apache.org/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.zip)
* [SQLite](https://www.sqlite.org/2023/sqlite-tools-win32-x86-3410000.zip)
* [Make](https://deac-fra.dl.sourceforge.net/project/gnuwin32/make/3.81/make-3.81-bin.zip) and its [dependencies](https://altushost-swe.dl.sourceforge.net/project/gnuwin32/make/3.81/make-3.81-dep.zip) (optional, but recommended for an optimized development workflow)
* [MailHog](https://github.com/mailhog/MailHog/releases/download/v1.0.1/MailHog_windows_amd64.exe) (required if you want to test email functionality locally. Otherwise, you can modify the run properties to use a public SMTP server)

Your next step will be to follow these instructions:
1. Download Java 17 (or newer) zip-archive from the provided link and extract it to a location of your choice.
2. If you are not using a bundled version of Maven from an IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows), download the Maven zip-archive and extract it to a location of your choice as well.
3. SQLite command line tool called sqlite3.exe is already included in the project files folder, so you don't need to install it separately.
4. If you want to optimize your development workflow, you can install Make with its dependencies for Windows. To do this, download the Make zip-archive and extract the bin folder with the make.exe program. Place the dependencies (such as libintl3.dll and libiconv2.dll) in the same folder.
5. Modify your Path system environment variable to include the paths to the bin folders for Java, Maven, SQLite, and Make (if installed). Make sure the paths include the bin folder name.
6. MailHog doesn't require installation as it is a standalone local SMTP server. You can simply place the executable file wherever you prefer.

### Installation
1. Download or clone the project repository to your local machine using `git clone https://github.com/Fluffy777/universe.git .` (where `.` will be your current folder).
2. Open a command shell from the root folder of the project.
3. Run the following command to generate an SQLite database in the project folder: `make migrate`
4. Build the project by running the following command: `make`

### Configuration
To configure the web application, edit the parameter values in a .properties file. By default, the file is called `application.properties` if you use the make tool. Otherwise, you can name it anything and specify it as a call argument. You can also use environment variables. The settings of the application's aspects, including the embedded Jetty web server properties, mailing configuration, security strength, and database connection details, are determined by their usage. The project repository has [a file with default values set](application.properties), which you can use. For more flexibility, refer to the table below.

|Key|Value|
|---|---|
|application.host|The IP address to which the application should bind|
|application.port|The port on which the application should run|
|application.bcryptStrength|The strength of the bcrypt encryption algorithm|
|database.filename|The path to the SQLite database in either absolute or relative form|
|mail.from|The email address of the sender of the email|
|mail.user|The identifier of the sender of the email on SMTP server|
|mail.password|The credential that is used to authenticate the email sender on SMTP server|
|mail.host|The IP address to which the SMTP server should bind|
|mail.port|The port on which the SMTP server should run|
|mail.ssl|Determines whether to use SSL encryption or not|
|mail.auth|Determines whether or not the email sender should be authenticated on the SMTP server|

### Run
1. If you prefer to use a local SMTP server, run MailHog. Note that the default host is set to 0.0.0.0, which means "localhost" in the context of this program. The default ports are 1025 for SMTP connections and 8025 for HTTP connections, which provide a useful web interface for email management.
2. Start the application by running the following command from the command shell: `make run`

That's it! The application should be up and running now, and you can access it at the specified port and URL in your web browser.
