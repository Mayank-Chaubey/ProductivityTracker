package com.productivitytracker;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public final class LocalTomcatServer {
    private LocalTomcatServer() {
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("port", "8080"));
        File baseDir = new File("target/local-tomcat");
        File webappDir = new File("src/main/webapp");
        File classesDir = new File("target/classes");

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addWebapp("/ProductivityTracker", webappDir.getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(
                resources,
                "/WEB-INF/classes",
                classesDir.getAbsolutePath(),
                "/"
        ));
        context.setResources(resources);

        tomcat.start();
        System.out.println("ProductivityTracker running at http://localhost:" + port + "/ProductivityTracker/");
        tomcat.getServer().await();
    }
}
