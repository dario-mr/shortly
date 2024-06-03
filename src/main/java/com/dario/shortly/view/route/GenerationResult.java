package com.dario.shortly.view.route;

import com.dario.shortly.view.Headline;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.component.icon.VaadinIcon.CHECK;
import static com.vaadin.flow.component.icon.VaadinIcon.COPY;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;
import static org.springframework.util.StringUtils.hasText;

@Route("result")
@PageTitle("Shortly")
@JsModule("./src/copyToClipboard.js")
public class GenerationResult extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEvent) {
        var paramsMap = beforeEvent.getLocation().getQueryParameters().getParameters();
        var longLink = paramsMap.get("longLink").get(0);
        var shortLink = paramsMap.get("shortLink").get(0);
        if (!hasText(longLink) || !hasText(shortLink)) {
            add(new Text("URL is invalid"));
            return;
        }

        var shortLinkText = new TextField("Short link");
        shortLinkText.getStyle().set("padding-top", "0px");
        shortLinkText.setWidthFull();
        shortLinkText.setReadOnly(true);
        shortLinkText.setValue(shortLink);

        var copyToClipboard = new Button();
        copyToClipboard.setIcon(COPY.create());
        copyToClipboard.addClickListener(event -> {
            UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", shortLinkText.getValue()); // TODO fix: not working on iOS...
            copyToClipboard.setIcon(CHECK.create());
        });

        var longLinkText = new TextField("Long link");
        longLinkText.getStyle().set("padding-top", "0px");
        longLinkText.setWidthFull();
        longLinkText.setReadOnly(true);
        longLinkText.setValue(longLink);

        var secondRow = new HorizontalLayout(shortLinkText, copyToClipboard);
        secondRow.setWidthFull();
        secondRow.setVerticalComponentAlignment(END, copyToClipboard);

        var shortenAnother = new Button("Shorten another");
        shortenAnother.setWidthFull();
        shortenAnother.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(Home.class)));

        var cardLayout = new VerticalLayout(longLinkText, secondRow, shortenAnother);
        cardLayout.addClassName("card-layout");

        var container = new VerticalLayout(new Headline(), cardLayout);
        container.setMaxWidth("700px");

        add(container);
        setAlignItems(CENTER);
        setPadding(false);
    }
}
