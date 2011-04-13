package org.exoplatform.social.plugin.youtubelink;


import java.util.Map;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

public class UIYoutubeActivityBuilder extends BaseUIActivityBuilder {
	  private static final Log LOG = ExoLogger.getLogger(UIYoutubeActivityBuilder.class);
	  
	  @Override
	  protected void extendUIActivity(BaseUIActivity uiActivity, ExoSocialActivity activity) {
	    UIYoutubeActivity uiYoutubeActivity = (UIYoutubeActivity) uiActivity;

	    Map<String, String> templateParams = activity.getTemplateParams();
        uiYoutubeActivity.setLinkSource(templateParams.get(UIYoutubeActivityComposer.LINK_PARAM));
        uiYoutubeActivity.setLinkTitle(templateParams.get(UIYoutubeActivityComposer.TITLE_PARAM));
        uiYoutubeActivity.setLinkImage(templateParams.get(UIYoutubeActivityComposer.IMAGE_PARAM));
        uiYoutubeActivity.setLinkDescription(templateParams.get(UIYoutubeActivityComposer.DESCRIPTION_PARAM));
        uiYoutubeActivity.setLinkComment(templateParams.get(UIYoutubeActivityComposer.COMMENT_PARAM));
	  }
}
