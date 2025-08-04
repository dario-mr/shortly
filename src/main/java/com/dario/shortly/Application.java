package com.dario.shortly;

import com.dario.shortly.util.PathResolver;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Push
@SpringBootApplication
@Theme(value = "my-theme", variant = Lumo.LIGHT)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", PathResolver.resolve("/images/favicon.png?v=3"), "160x160");
    }
}
