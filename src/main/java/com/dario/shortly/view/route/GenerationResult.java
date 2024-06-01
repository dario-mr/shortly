package com.dario.shortly.view.route;

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

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;
import static org.springframework.util.StringUtils.hasText;

@Route("result")
@PageTitle("Shortly")
@JsModule("./src/copyToClipboard.js")
public class GenerationResult extends VerticalLayout implements BeforeEnterObserver { // TODO add top banner

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEvent) {
        setMargin(true);
        setWidth("auto");
        addClassName("card-layout");

        var paramsMap = beforeEvent.getLocation().getQueryParameters().getParameters();
        var longLink = paramsMap.get("longLink").get(0);
        var shortLink = paramsMap.get("shortLink").get(0);
        if (!hasText(longLink) || !hasText(shortLink)) {
            add(new Text("URL is invalid"));
            return;
        }

        var shortLinkText = new TextField("Short URL");
        shortLinkText.getStyle().set("padding-top", "0px");
        shortLinkText.setWidthFull();
        shortLinkText.setReadOnly(true);
        shortLinkText.setValue(shortLink);

        var copyToClipboard = new Button("Copy to Clipboard");
        copyToClipboard.setWidth("10.5em");
        copyToClipboard.addClickListener(event -> {
            UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", shortLinkText.getValue());
            copyToClipboard.setText("✅ Copied!");
        });

        var longLinkText = new TextField("Long URL");
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

        add(longLinkText, secondRow, shortenAnother);
    }

}
