package com.dario.shortly.view;

import com.dario.shortly.core.service.LinkService;
import com.dario.shortly.view.route.GenerationResult;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

import static com.vaadin.flow.component.Key.ENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;
import static org.springframework.util.StringUtils.hasText;

public class LinkGenerator extends VerticalLayout {

    private final LinkService linkService;

    public LinkGenerator(LinkService linkService) {
        this.linkService = linkService;
        setWidth("auto");

        var longLinkText = new TextField("Long URL", "Enter a valid link");
        longLinkText.setWidthFull();
        longLinkText.getStyle().set("padding-top", "0px");
        longLinkText.addKeyPressListener(ENTER, event -> generateLinkAndShowResultPage(longLinkText));

        var shortenButton = new Button("Shorten URL");
        shortenButton.addClickListener(event -> generateLinkAndShowResultPage(longLinkText));

        var row = new HorizontalLayout(longLinkText, shortenButton);
        row.setWidthFull();
        row.setVerticalComponentAlignment(END, shortenButton);
        row.addClassName("card-layout");
        row.setPadding(true);

        add(new Headline(), row);
    }

    private void generateLinkAndShowResultPage(TextField longLinkText) {
        var longLink = longLinkText.getValue();
        if (hasText(longLink)) {
            if (!longLink.startsWith("http://") && !longLink.startsWith("https://")) {
                longLink = "http://" + longLink;
            }
            var shortLinkId = RandomStringUtils.randomAlphanumeric(8).toLowerCase(); // TODO generate a proper unique ID

            linkService.save(longLink, shortLinkId);

            var finalLongLink = longLink;
            UI.getCurrent().getPage().fetchCurrentURL(currentUrl -> {
                var shortLink = currentUrl + shortLinkId;

                var parameters = QueryParameters.simple(Map.of(
                        "longLink", finalLongLink,
                        "shortLink", shortLink
                ));

                getUI().ifPresent(ui -> ui.navigate(GenerationResult.class, parameters));
            });
        }
    }

}
