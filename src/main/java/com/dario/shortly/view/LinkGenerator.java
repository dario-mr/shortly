package com.dario.shortly.view;

import com.dario.shortly.core.service.LinkService;
import com.dario.shortly.view.route.GenerationResult;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

import static com.vaadin.flow.component.Key.ENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;
import static org.springframework.util.StringUtils.hasText;

public class LinkGenerator extends VerticalLayout {

    private final LinkService linkService;

    private final ProgressBar progressBar = new ProgressBar();
    private final Button shortenButton = new Button("Shorten URL");

    public LinkGenerator(LinkService linkService) {
        this.linkService = linkService;
        setWidth("auto");

        var longLinkText = new TextField("Long URL", "Enter a valid link");
        longLinkText.setWidthFull();
        longLinkText.getStyle().set("padding-top", "0px");
        longLinkText.addKeyPressListener(ENTER, event -> generateLinkAndShowResultPage(longLinkText));

        shortenButton.addClickListener(event -> generateLinkAndShowResultPage(longLinkText));

        var row = new HorizontalLayout(longLinkText, shortenButton);
        row.setWidthFull();
        row.setVerticalComponentAlignment(END, shortenButton);
        row.addClassName("card-layout");
        row.setPadding(true);

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        add(new Headline(), row, progressBar);
    }

    private void generateLinkAndShowResultPage(TextField longLinkText) {
        var longLinkValue = longLinkText.getValue();
        if (!hasText(longLinkValue)) {
            return;
        }

        var longLink = longLinkValue.startsWith("http://") || longLinkValue.startsWith("https://")
                ? longLinkValue
                : "http://" + longLinkValue;
        var shortLinkId = RandomStringUtils.randomAlphanumeric(8).toLowerCase(); // TODO generate a proper unique ID

        progressBar.setVisible(true);
        shortenButton.setEnabled(false);
        var ui = UI.getCurrent();

        linkService.save(longLink, shortLinkId)
                .whenComplete((result, throwable) ->
                        ui.access(() -> {
                            progressBar.setVisible(false);
                            shortenButton.setEnabled(true);

                            if (throwable != null) {
                                return;
                            }

                            ui.getPage().fetchCurrentURL(currentUrl -> {
                                var shortLink = currentUrl + shortLinkId;
                                var parameters = QueryParameters.simple(Map.of(
                                        "longLink", longLink,
                                        "shortLink", shortLink
                                ));

                                ui.navigate(GenerationResult.class, parameters);
                            });
                        }));
    }

}
