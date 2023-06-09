package com.fluffy.universe.controllers;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.models.User;
import com.fluffy.universe.services.MailService;
import com.fluffy.universe.services.PostService;
import com.fluffy.universe.services.UserService;
import com.fluffy.universe.utils.*;
import com.google.common.base.Strings;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserController extends Controller {
    public UserController(Javalin application) {
        super("", application);
    }

    private static String validateEmail(String email, boolean shouldBeUnique) {
        if (email == null || email.isEmpty()) {
            return "Enter your email address";
        } else if (!ValidationUtils.isValidMail(email)) {
            return "Enter a valid email address";
        } else if (shouldBeUnique && UserService.getUserByEmail(email) != null) {
            return "Email is already is use";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validatePassword(String password, boolean shouldBeFormatted) {
        if (password == null || password.isEmpty()) {
            return "Enter a password";
        } else if (shouldBeFormatted && !ValidationUtils.isValidPassword(password)) {
            return "Password must be between 8 and 30 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return "Confirm your password";
        } else if (!confirmPassword.equals(password)) {
            return "Passwords do not match";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateFirstName(String firstName) {
        return (firstName == null || firstName.isEmpty())
                ? "Enter first name"
                : ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateLastName(String lastName) {
        return (lastName == null || lastName.isEmpty())
                ? "Enter last name"
                : ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateGender(String gender) {
        if (gender == null || !List.of("", "male", "female").contains(gender)) {
            return "Select a valid gender";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateBirthday(String birthday) {
        if ("".equals(birthday)) {
            return ErrorBag.NO_ERROR_MESSAGE;
        } else if (birthday == null || !ValidationUtils.isDateValid(birthday)) {
            return "Enter a valid date of birth";
        } else if (LocalDate.parse(birthday).isAfter(LocalDate.now())) {
            return "Enter your date of birth";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateAddress(String address) {
        if (address == null) {
            return "Enter a valid address";
        } else if (address.length() > 255) {
            return "Entered address is too long";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    private static String validateWebsite(String website) {
        if (website == null) {
            return "Enter a valid website address";
        } else if (website.isEmpty()) {
            return ErrorBag.NO_ERROR_MESSAGE;
        } else if (!website.startsWith("http://") && !website.startsWith("https://")) {
            return "URL must start with 'http://' or 'https://'";
        } else if (website.length() > 255) {
            return "The entered website address is too long";
        }
        return ErrorBag.NO_ERROR_MESSAGE;
    }

    public void signUpPage(Context context) {
        render(context, "/views/pages/models/user/sign-up.vm");
    }

    public void signInPage(Context context) {
        render(context, "/views/pages/models/user/sign-in.vm");
    }

    public void forgotPasswordPage(Context context) {
        render(context, "/views/pages/models/user/forgot-password.vm");
    }

    public void resetPasswordPage(Context context) {
        String resetPasswordToken = context.pathParam("token");
        User user = UserService.getUserByResetPasswordToken(resetPasswordToken);

        if (user == null) {
            throw new HttpException(HttpCode.FORBIDDEN, "Access forbidden.");
        }

        render(context, "/views/pages/models/user/reset-password.vm");
    }

    public void accountPage(Context context) {
        disableCaching(context);
        render(context, "/views/pages/models/user/account.vm");
    }

    public void dashboardPage(Context context) {
        Map<String, Object> model = SessionUtils.getCurrentModel(context);
        model.put("posts", PostService.getPostsWithCommentCount());
        render(context, "/views/pages/models/user/dashboard.vm");
    }

    public void signUp(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("sign-up");

        String email = context.formParam("email");
        String password = context.formParam("password");
        String confirmPassword = context.formParam("confirm-password");
        String firstName = context.formParam("first-name");
        String lastName = context.formParam("last-name");

        errorBag.add("email", email, validateEmail(email, true));
        errorBag.add("password", "", validatePassword(password, true));
        errorBag.add("confirm-password", "", validateConfirmPassword(password, confirmPassword));
        errorBag.add("first-name", firstName, validateFirstName(firstName));
        errorBag.add("last-name", lastName, validateLastName(lastName));

        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Sign up failed!", "Please ensure that all required fields are filled in correctly and try again.", AlertType.WARNING);
            context.redirect("/sign-up");
            return;
        }

        User user = new User();
        user.setRoleId(Role.USER.getId());
        user.setFirstName(SecurityUtils.escape(firstName));
        user.setLastName(SecurityUtils.escape(lastName));
        user.setEmail(SecurityUtils.escape(email));
        user.setPassword(UserService.encodePassword(password));
        UserService.saveUser(user);

        context.sessionAttribute(SessionKey.USER, user);
        serverData.setAlertWindow("Congratulations!", "You have successfully signed up. Welcome to our community!", AlertType.SUCCESS);
        context.redirect("/");
    }

    public void signIn(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("sign-in");

        String email = context.formParam("email");
        String password = context.formParam("password");
        User user = UserService.getUserByEmail(email);

        errorBag.add("email", email, validateEmail(email, false));
        errorBag.add("password", "", validatePassword(password, false));

        if (user == null || !UserService.isCorrectPassword(password, user.getPassword())) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Sign in failed!", "Invalid email or password. Please check your input and try again.", AlertType.WARNING);
            context.redirect("/sign-in");
            return;
        }

        context.sessionAttribute(SessionKey.USER, user);
        context.redirect("/");
    }

    public void signOut(Context context) {
        context.sessionAttribute(SessionKey.USER, null);
        context.redirect("/");
    }

    public void forgotPassword(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("forgot-password");
        String email = context.formParam("email");

        errorBag.add("email", email, validateEmail(email, false));
        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Invalid input!", "The email you entered is not valid.", AlertType.WARNING);
            context.redirect("/forgot-password");
            return;
        }

        User user = UserService.getUserByEmail(email);
        if (user != null) {
            String resetPasswordToken = UUID.randomUUID().toString();
            user.setResetPasswordToken(resetPasswordToken);
            UserService.saveUser(user);
            try {
                MailService.sendResetLink(user);
            } catch (MessagingException e) {
                throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Failed to send email");
            }
        }

        serverData.setAlertWindow("Success!", "We have sent you an email with instructions on how to reset your password.", AlertType.SUCCESS);
        context.redirect("/sign-in");
    }

    public void resetPassword(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("password-reset");

        String resetPasswordToken = context.pathParam("token");
        String password = context.formParam("password");
        String confirmPassword = context.formParam("confirm-password");

        User user = UserService.getUserByResetPasswordToken(resetPasswordToken);
        if (user == null) {
            throw new HttpException(HttpCode.FORBIDDEN, "Access forbidden.");
        }

        errorBag.add("password", "", validatePassword(password, true));
        errorBag.add("confirm-password", "", validateConfirmPassword(password, confirmPassword));

        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Invalid input!", "Please make sure your new password meets the requirements and try again.", AlertType.WARNING);
            context.redirect("/reset-password/" + resetPasswordToken);
            return;
        }

        user.setResetPasswordToken(null);
        user.setPassword(UserService.encodePassword(password));
        UserService.saveUser(user);

        serverData.setAlertWindow("Congratulations!", "Your password has been successfully reset. You can now use your new password to log in to your account.", AlertType.SUCCESS);
        context.redirect("/sign-in");
    }

    public void updateAccount(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("account");

        String firstName = context.formParam("first-name");
        String lastName = context.formParam("last-name");
        String gender = context.formParam("gender");
        String birthday = context.formParam("birthday");
        String address = context.formParam("address");
        String website = context.formParam("website");

        if (context.formParam("_method") == null
                || context.formParam("email") != null
                || firstName == null
                || lastName == null
                || gender == null
                || birthday == null
                || address == null
                || website == null) {
            throw new HttpException(HttpCode.BAD_REQUEST);
        }

        errorBag.add("first-name", firstName, validateFirstName(firstName));
        errorBag.add("last-name", lastName, validateLastName(lastName));
        errorBag.add("gender", gender, validateGender(gender));
        errorBag.add("birthday", birthday, validateBirthday(birthday));
        errorBag.add("address", address, validateAddress(address));
        errorBag.add("website", website, validateWebsite(website));

        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Invalid input!", "The information you entered on the profile page is not valid.", AlertType.WARNING);
            context.redirect("/account");
            return;
        }

        User user = SessionUtils.getCurrentUser(context);
        user.setFirstName(SecurityUtils.escape(firstName));
        user.setLastName(SecurityUtils.escape(lastName));
        user.setGender("".equals(gender) ? null : String.valueOf(gender.charAt(0)));
        user.setBirthday(Strings.emptyToNull(birthday));
        user.setAddress(SecurityUtils.escape(Strings.emptyToNull(address)));
        user.setWebsite(SecurityUtils.escape(Strings.emptyToNull(website)));
        UserService.saveUser(user);

        serverData.setAlertWindow("Congratulations!", "User account data updated successfully.", AlertType.SUCCESS);
        context.redirect("/account");
    }

    @Override
    public void registerRoutes(Javalin application) {
        application.get("/sign-in", this::signInPage, Role.GUEST);
        application.get("/sign-up", this::signUpPage, Role.GUEST);
        application.get("/forgot-password", this::forgotPasswordPage, Role.GUEST);
        application.get("/reset-password/{token}", this::resetPasswordPage, Role.GUEST);
        application.get("/account", this::accountPage, Role.USER);
        application.get("/dashboard", this::dashboardPage, Role.USER);
        application.post("/sign-in", this::signIn, Role.GUEST);
        application.post("/sign-up", this::signUp, Role.GUEST);
        application.post("/sign-out", this::signOut, Role.USER);
        application.post("/forgot-password", this::forgotPassword, Role.GUEST);
        application.post("/reset-password/{token}", this::resetPassword, Role.GUEST);
        application.post("/account", this::updateAccount, Role.USER);
    }
}
