package com.dario.shortly.view.route;

import com.dario.shortly.view.Headline;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.olli.ClipboardHelper;

import static com.vaadin.flow.component.icon.VaadinIcon.CHECK;
import static com.vaadin.flow.component.icon.VaadinIcon.COPY;
import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;
import static org.springframework.util.StringUtils.hasText;

@Route("result")
@PageTitle("Shortly")
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

        var copiedNotification = new Notification("Copied!", 1_500, TOP_CENTER);
        copiedNotification.addThemeVariants(LUMO_SUCCESS);

        var copyToClipboard = new Button();
        copyToClipboard.setIcon(COPY.create());
        copyToClipboard.addClickListener(event -> {
            copyToClipboard.setIcon(CHECK.create());
            copiedNotification.open();
        });
        var clipboardHelper = new ClipboardHelper(shortLinkText.getValue(), copyToClipboard);

        var longLinkText = new TextField("Long link");
        longLinkText.getStyle().set("padding-top", "0px");
        longLinkText.setWidthFull();
        longLinkText.setReadOnly(true);
        longLinkText.setValue(longLink);

        var secondRow = new HorizontalLayout(shortLinkText, clipboardHelper);
        secondRow.setWidthFull();
        secondRow.setVerticalComponentAlignment(END, clipboardHelper);

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
