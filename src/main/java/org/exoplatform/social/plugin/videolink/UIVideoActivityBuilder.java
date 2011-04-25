package org.exoplatform.social.plugin.videolink;


import java.util.Map;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

public class UIVideoActivityBuilder extends BaseUIActivityBuilder {
	  private static final Log LOG = ExoLogger.getLogger(UIVideoActivityBuilder.class);
	  
	  @Override
	  protected void extendUIActivity(BaseUIActivity uiActivity, ExoSocialActivity activity) {
	    UIVideoActivity uiVideoActivity = (UIVideoActivity) uiActivity;

	    Map<String, String> templateParams = activity.getTemplateParams();
        uiVideoActivity.setLinkSource(templateParams.get(UIVideoActivityComposer.LINK_PARAM));
        uiVideoActivity.setLinkTitle(templateParams.get(UIVideoActivityComposer.TITLE_PARAM));
        uiVideoActivity.setLinkImage(templateParams.get(UIVideoActivityComposer.IMAGE_PARAM));
        uiVideoActivity.setLinkHTML(templateParams.get(UIVideoActivityComposer.HTML_PARAM));
        uiVideoActivity.setLinkComment(templateParams.get(UIVideoActivityComposer.COMMENT_PARAM));
	  }
}
