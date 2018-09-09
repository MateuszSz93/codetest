package com.pierceecom.blog;

import com.pierceecom.blog.connector.BlogConnector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

import static java.lang.System.out;
import static java.util.Objects.isNull;

public class SimpleBlogClientApplication {

    private final BlogConnector connector = new BlogConnector();

    public static void main(String[] args) {

        final ApplicationContext context = new AnnotationConfigApplicationContext(SimpleBlogClientApplication.class);
        final SimpleBlogClientApplication contextBean = context.getBean(SimpleBlogClientApplication.class);

        contextBean.runMenu();
    }

    private void runMenu() {

        displayMenu();

        while (true) {
            final Scanner scanner = new Scanner(System.in);

            switch (scanner.nextLine()) {
            case "1":
                connector.getAllPosts();
                break;
            case "2":
                out.print("Write post ID: ");
                connector.getPost(scanner.nextLine());
                break;
            case "3":
                out.print("Write post ID (or press enter to get random ID): ");
                final String id = scanner.nextLine();
                out.print("Write content: ");
                final String content = scanner.nextLine();
                out.print("Write title: ");
                final String title = scanner.nextLine();

                if (isNull(content) || isNull(title)) {
                    out.println("Content and title can not be null.");
                } else {
                    connector.createPost(id, content, title);
                }
                break;
            case "4":
                out.print("Write post ID: ");
                connector.deletePost(scanner.nextLine());
                break;
            default:
                displayMenu();
            }
        }
    }

    private void displayMenu() {

        out.println("Write 1 to display all posts.");
        out.println("Write 2 to display single post.");
        out.println("Write 3 to add post.");
        out.println("Write 4 to remove post.");
    }
}
