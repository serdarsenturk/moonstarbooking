package com.serdarsenturk.moonstarbooking;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

import com.vaadin.flow.theme.material.Material;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme(themeClass = Material.class)
@PWA(name = "moonstarbooking", shortName = "moonstarbooking", offlineResources = {"images/logo1.png"})
public class MoonstarbookingApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) {
		LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(MoonstarbookingApplication.class, args));
	}

}
