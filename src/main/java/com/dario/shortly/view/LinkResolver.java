package com.dario.shortly.view;

import com.dario.shortly.core.service.LinkService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

public class LinkResolver extends VerticalLayout {

    public LinkResolver(LinkService linkService, String shortLinkId) {
        setAlignItems(CENTER);

        var optionalLink = linkService.findByShortLinkId(shortLinkId);
        optionalLink.ifPresentOrElse(
                link -> UI.getCurrent().getPage().setLocation(link.longLink()),
                () -> add(new Text("Link not found"))
        );
    }
}
