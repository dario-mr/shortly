package com.dario.shortly.view.route;

import com.dario.shortly.core.service.LinkService;
import com.dario.shortly.view.LinkGenerator;
import com.dario.shortly.view.LinkResolver;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

@Route("")
@PageTitle("Shortly")
@RequiredArgsConstructor
public class Home extends Div implements HasUrlParameter<String> { // TODO add top banner

    private final LinkService linkService;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String shortLinkId) {
        if (shortLinkId == null) {
            add(new LinkGenerator(linkService));
            return;
        }

        add(new LinkResolver(linkService, shortLinkId));
    }
}