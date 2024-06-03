package com.dario.shortly.view;

import com.dario.shortly.core.service.LinkService;
import com.dario.shortly.view.route.GenerationResult;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

import static com.vaadin.flow.component.Key.ENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class LinkGenerator extends VerticalLayout {

    private static final String SHORTEN_BUTTON_TEXT = "Shorten link";

    private final LinkService linkService;
    private final TextField longLinkText = new TextField("Long link", "Enter a looong link");
    private final Button shortenButton = new Button(SHORTEN_BUTTON_TEXT);
    private final LoadingIcon loadingIcon = new LoadingIcon();

    public LinkGenerator(LinkService linkService) {
        this.linkService = linkService;

        longLinkText.setWidthFull();
        longLinkText.getStyle().set("padding-top", "0px");
        longLinkText.addKeyPressListener(ENTER, event -> generateLinkAndShowResultPage());

        shortenButton.setWidthFull();
        shortenButton.addClickListener(event -> generateLinkAndShowResultPage());

        var cardLayout = new VerticalLayout(longLinkText, shortenButton);
        cardLayout.addClassName("card-layout");

        var container = new VerticalLayout(new Headline(), cardLayout);
        container.setMaxWidth("700px");

        add(container);
        setAlignItems(CENTER);
        setPadding(false);
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

        var ui = UI.getCurrent();
        startLoading();

        linkService.save(longLink, shortLinkId)
                .whenComplete((result, exception) ->
                        ui.access(() -> {
                            stopLoading();
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

    private void startLoading() {
        shortenButton.setEnabled(false);
        shortenButton.setText("");
        shortenButton.setIcon(loadingIcon);
        longLinkText.setEnabled(false);
    }

    private void stopLoading() {
        shortenButton.setEnabled(true);
        shortenButton.setText(SHORTEN_BUTTON_TEXT);
        shortenButton.setIcon(null);
        longLinkText.setEnabled(true);
    }
}
