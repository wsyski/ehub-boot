/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;

/**
 * The eHUB administration interface base {@link WebPage}. All eHUB administration interface {@link WebPage}s should
 * extend this class.
 */
public abstract class AbstractBasePage extends WebPage {
    @Override
    public void renderHead(IHeaderResponse response)
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("css/bootstrap.min.css"));
        response.render(CssHeaderItem.forUrl("css/ehub.css"));
    }


}
