package com.dario.shortly.view;

import com.dario.shortly.core.service.LinkService;
import com.dario.shortly.view.route.GenerationResult;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

import static com.vaadin.flow.component.Key.ENTER;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class LinkGenerator extends VerticalLayout {

    private final LinkService linkService;

    private static final String SHORTEN_BUTTON_TEXT = "Shorten URL";

    private final TextField longLinkText = new TextField("Long URL", "Enter a valid link");
    private final Button shortenButton = new Button(SHORTEN_BUTTON_TEXT);
    private final ProgressBar progressBar = new ProgressBar(); // TODO once button gif works, remove progressBar

    public LinkGenerator(LinkService linkService) {
        this.linkService = linkService;
        setWidth("auto");

        longLinkText.setWidthFull();
        longLinkText.getStyle().set("padding-top", "0px");
        longLinkText.addKeyPressListener(ENTER, event -> generateLinkAndShowResultPage());

        shortenButton.setWidthFull();
        shortenButton.getStyle().set("padding-top", "1.5em");
        shortenButton.getStyle().set("padding-bottom", "1.5em");
        shortenButton.addClickListener(event -> generateLinkAndShowResultPage());

        var row = new VerticalLayout(longLinkText, shortenButton);
        row.addClassName("card-layout");
        row.setWidthFull();
        row.setPadding(true);

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        add(new Headline(), row, progressBar);
    }

    private void generateLinkAndShowResultPage() {
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
//        shortenButton.setText("");
//        shortenButton.setIcon(new Image("/images/loading.gif", "loading")); // TODO fix: the icon exceeds the button size
        var ui = UI.getCurrent();

        linkService.save(longLink, shortLinkId)
                .whenComplete((result, exception) ->
                        ui.access(() -> {
                            progressBar.setVisible(false);
                            shortenButton.setEnabled(true);
                            shortenButton.setText(SHORTEN_BUTTON_TEXT);
                            shortenButton.setIcon(null);

                            if (exception != null) {
                                log.error("Error saving link to DB: {}", exception.getMessage(), exception);
                                return;
                            }

                            ui.getPage().fetchCurrentURL(currentUrl -> {
                                var currentUrlString = currentUrl.toString().replace("http://", "").replace("https://", "");
                                var parameters = QueryParameters.simple(Map.of(
                                        "longLink", longLink,
                                        "shortLink", currentUrlString + shortLinkId
                                ));

                                ui.navigate(GenerationResult.class, parameters);
                            });
                        }));
    }

}
