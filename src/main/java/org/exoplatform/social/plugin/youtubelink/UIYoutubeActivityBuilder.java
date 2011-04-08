package org.exoplatform.social.plugin.youtubelink;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.storage.ActivityStorageException;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class UIYoutubeActivityBuilder extends BaseUIActivityBuilder {
	  private static final Log LOG = ExoLogger.getLogger(UIYoutubeActivityBuilder.class);
	  
	  @Override
	  protected void extendUIActivity(BaseUIActivity uiActivity, ExoSocialActivity activity) {
	    UIYoutubeActivity uiYoutubeActivity = (UIYoutubeActivity) uiActivity;

	    Map<String, String> templateParams = activity.getTemplateParams();
	    if (templateParams != null) {
	        uiYoutubeActivity.setLinkSource(templateParams.get(UIYoutubeActivityComposer.LINK_PARAM));

	        uiYoutubeActivity.setLinkTitle(templateParams.get(UIYoutubeActivityComposer.TITLE_PARAM));

	        uiYoutubeActivity.setLinkImage(templateParams.get(UIYoutubeActivityComposer.IMAGE_PARAM));

	        uiYoutubeActivity.setLinkDescription(templateParams.get(UIYoutubeActivityComposer.DESCRIPTION_PARAM));

	        uiYoutubeActivity.setLinkComment(templateParams.get(UIYoutubeActivityComposer.COMMENT_PARAM));
	    } else {
	      try {
	        JSONObject jsonObj = new JSONObject(activity.getTitle());
	        uiYoutubeActivity.setLinkSource(jsonObj.getString(UIYoutubeActivityComposer.LINK_PARAM));
	        uiYoutubeActivity.setLinkTitle(jsonObj.getString(UIYoutubeActivityComposer.TITLE_PARAM));
	        uiYoutubeActivity.setLinkImage(jsonObj.getString(UIYoutubeActivityComposer.IMAGE_PARAM));
	        uiYoutubeActivity.setLinkDescription(jsonObj.getString(UIYoutubeActivityComposer.DESCRIPTION_PARAM));
	        uiYoutubeActivity.setLinkComment(jsonObj.getString(UIYoutubeActivityComposer.COMMENT_PARAM));
	        saveToNewDataFormat(activity, uiYoutubeActivity);
	      } catch (JSONException e) {
	        LOG.error("Error with link activity's title data");
	      }
	    }
	  }

	  private void saveToNewDataFormat(ExoSocialActivity activity, UIYoutubeActivity uiLinkActivity) {
	    String linkTitle = "Shared a link: <a href=\"${" + UIYoutubeActivityComposer.LINK_PARAM + "}\">" +
	            "${" + UIYoutubeActivityComposer.TITLE_PARAM + "} </a>";
	    activity.setTitle(linkTitle);
	    Map<String, String> templateParams = new HashMap<String, String>();
	    templateParams.put(UIYoutubeActivityComposer.LINK_PARAM, uiLinkActivity.getLinkSource());
	    templateParams.put(UIYoutubeActivityComposer.TITLE_PARAM, uiLinkActivity.getLinkTitle());
	    templateParams.put(UIYoutubeActivityComposer.IMAGE_PARAM, uiLinkActivity.getLinkImage());
	    templateParams.put(UIYoutubeActivityComposer.DESCRIPTION_PARAM, uiLinkActivity.getLinkDescription());
	    templateParams.put(UIYoutubeActivityComposer.COMMENT_PARAM, uiLinkActivity.getLinkComment());
	    ActivityManager am = (ActivityManager) PortalContainer.getInstance().getComponentInstanceOfType(ActivityManager.class);
	    try {
	      am.saveActivity(activity);
	    } catch (ActivityStorageException ase) {
	      LOG.warn("Could not save new data format for document activity.", ase);
	    }
	  }
}
