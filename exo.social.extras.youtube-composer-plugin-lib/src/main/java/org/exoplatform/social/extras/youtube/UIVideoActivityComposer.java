package org.exoplatform.social.plugin.videolink;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.plugin.videolink.util.VideoEmbedTool;
import org.exoplatform.social.webui.activity.UIActivitiesContainer;
import org.exoplatform.social.webui.composer.UIActivityComposer;
import org.exoplatform.social.webui.composer.UIComposer;
import org.exoplatform.social.webui.composer.UIComposer.PostContext;
import org.exoplatform.social.webui.profile.UIUserActivitiesDisplay;
import org.exoplatform.social.webui.space.UISpaceActivitiesDisplay;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormStringInput;
import org.json.JSONObject;

@ComponentConfig(
        template = "classpath:groovy/social/plugin/videolink/UIVideoActivityComposer.gtmpl",
        events = {
                @EventConfig(listeners = org.exoplatform.social.plugin.videolink.UIVideoActivityComposer.SearchVideo.class),
                @EventConfig(listeners = org.exoplatform.social.plugin.videolink.UIVideoActivityComposer.SelectVideoFromResultList.class),
                @EventConfig(listeners = org.exoplatform.social.plugin.videolink.UIVideoActivityComposer.AttachActionListener.class),
                @EventConfig(listeners = org.exoplatform.social.plugin.videolink.UIVideoActivityComposer.ChangeLinkContentActionListener.class),
                @EventConfig(listeners = UIActivityComposer.CloseActionListener.class),
                @EventConfig(listeners = UIActivityComposer.SubmitContentActionListener.class),
                @EventConfig(listeners = UIActivityComposer.ActivateActionListener.class)
        }
)

public class UIVideoActivityComposer extends UIActivityComposer {

  public static final String LINK_PARAM = "link";
  public static final String IMAGE_PARAM = "image";
  public static final String TITLE_PARAM = "title";
  public static final String HTML_PARAM = "htmlembed";
  public static final String COMMENT_PARAM = "comment";

  //private static final String MSG_ERROR_ATTACH_LINK = "UIComposerLinkExtension.msg.error.Attach_Link";
  private static final String HTTP = "http://";
  private static final String HTTPS = "https://";
  private JSONObject videoJson;
  private boolean linkInfoDisplayed_ = false;
  private Map<String, String> templateParams;

  /**
   * constructor
   */
  public UIVideoActivityComposer() {
    setReadyForPostingActivity(false);
    addChild(new UIFormStringInput("InputLink", "InputLink", null));
  }

  public void setLinkInfoDisplayed(boolean displayed) {
    linkInfoDisplayed_ = displayed;
  }

  public boolean isLinkInfoDisplayed() {
    return linkInfoDisplayed_;
  }

  public void setTemplateParams(Map<String, String> tempParams) {
    templateParams = tempParams;
  }

  public Map<String, String> getTemplateParams() {
    return templateParams;
  }

  public void clearVideoJson() {
    videoJson = null;
  }

  public JSONObject getVideoJson() {
    return videoJson;
  }

  /**
   * sets link url to gets content
   * @param url
   * @throws Exception
   */
  private void setLink(String url) throws Exception {
    if (!(url.contains(HTTP) || url.contains(HTTPS))) {
      url = HTTP + url;
    }

    videoJson = VideoEmbedTool.getoembedData(url);
    templateParams = new HashMap<String, String>();
    templateParams.put(LINK_PARAM, url);
    templateParams.put(TITLE_PARAM, videoJson.getString(VideoEmbedTool.OEMBED_TITLE));
    templateParams.put(HTML_PARAM, videoJson.getString(VideoEmbedTool.OEMBED_HTML));

    setLinkInfoDisplayed(true);
  }

  static public class AttachActionListener extends EventListener<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer> {

    @Override
    public void execute(Event<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer> event) throws Exception {
      WebuiRequestContext requestContext = event.getRequestContext();
      org.exoplatform.social.plugin.videolink.UIVideoActivityComposer uiComposerLinkExtension = event.getSource();
      String url = requestContext.getRequestParameter(OBJECTID);
      try {
        uiComposerLinkExtension.setLink(url.trim());
      } catch (Exception e) {
        uiComposerLinkExtension.setReadyForPostingActivity(false);
        // Comment this below line code for temporary fixing issue SOC-1091. Check later.
        // uiApplication.addMessage(new ApplicationMessage(MSG_ERROR_ATTACH_LINK, null, ApplicationMessage.WARNING));
        return;
      }
      requestContext.addUIComponentToUpdateByAjax(uiComposerLinkExtension);
      event.getSource().setReadyForPostingActivity(true);
    }
  }

  static public class ChangeLinkContentActionListener extends EventListener<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer> {
    @Override
    public void execute(Event<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer> event) throws Exception {
      WebuiRequestContext requestContext = event.getRequestContext();
      org.exoplatform.social.plugin.videolink.UIVideoActivityComposer uiComposerLinkExtension = event.getSource();
      
      Map<String, String> tempParams = new HashMap<String, String>();
      if( uiComposerLinkExtension.getTemplateParams() != null ){

          tempParams.put(LINK_PARAM, requestContext.getRequestParameter(LINK_PARAM));
          tempParams.put(HTML_PARAM, uiComposerLinkExtension.getTemplateParams().get(HTML_PARAM));
          tempParams.put(TITLE_PARAM, requestContext.getRequestParameter(TITLE_PARAM));
      }

      uiComposerLinkExtension.setTemplateParams(tempParams);
      requestContext.addUIComponentToUpdateByAjax(uiComposerLinkExtension);
      UIComponent uiParent = uiComposerLinkExtension.getParent();
      if (uiParent != null) {
        uiParent.broadcast(event, event.getExecutionPhase());
      }
    }
  }

  public static class SelectVideoFromResultList extends EventListener<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer>{
    @Override
    public void execute(Event<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer> event) throws Exception {
      WebuiRequestContext requestContext = event.getRequestContext();
      org.exoplatform.social.plugin.videolink.UIVideoActivityComposer uiComposerLinkExtension = event.getSource();

    }
  }

  public static class SearchVideo extends EventListener<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer>{

    @Override
    public void execute(Event<org.exoplatform.social.plugin.videolink.UIVideoActivityComposer> event) throws Exception {
      WebuiRequestContext requestContext = event.getRequestContext();
      org.exoplatform.social.plugin.videolink.UIVideoActivityComposer uiComposerLinkExtension = event.getSource();


    }
  }

  @Override
  protected void onActivate(Event<UIActivityComposer> arg0) {
  }

  @Override
  protected void onClose(Event<UIActivityComposer> arg0) {
    setReadyForPostingActivity(false);
  }

  @Override
  protected void onSubmit(Event<UIActivityComposer> arg0) {
  }

  @Override
  public void onPostActivity(PostContext postContext, UIComponent source,
                             WebuiRequestContext requestContext, String postedMessage) throws Exception {
    final UIComposer uiComposer = (UIComposer) source;
    ActivityManager activityManager = uiComposer.getApplicationComponent(ActivityManager.class);
    IdentityManager identityManager = uiComposer.getApplicationComponent(IdentityManager.class);
    String remoteUser = requestContext.getRemoteUser();
    Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, remoteUser, false);

    UIApplication uiApplication = requestContext.getUIApplication();
    Map<String, String> templateParams = getTemplateParams();
    templateParams.put(COMMENT_PARAM, postedMessage);
    setTemplateParams(templateParams);

    if (templateParams.size() == 0) {
      uiApplication.addMessage(new ApplicationMessage("UIComposer.msg.error.Empty_Message",
              null,
              ApplicationMessage.WARNING));
      return;
    }

    String title = "Shared a video: <a href=\"${" + LINK_PARAM + "}\">${" + TITLE_PARAM + "} </a>";
    ExoSocialActivity activity = new ExoSocialActivityImpl(userIdentity.getId(),
            UIVideoActivity.ACTIVITY_TYPE,
            title,
            null);
    activity.setTemplateParams(templateParams);

    if (postContext == UIComposer.PostContext.SPACE) {
      UISpaceActivitiesDisplay uiDisplaySpaceActivities = (UISpaceActivitiesDisplay) getActivityDisplay();
      Space space = uiDisplaySpaceActivities.getSpace();

      Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME,
              space.getPrettyName(),
              false);

      activityManager.saveActivity(spaceIdentity, activity);

      UIActivitiesContainer activitiesContainer = uiDisplaySpaceActivities.getActivitiesLoader().getActivitiesContainer();
      activitiesContainer.addActivity(activity);
      requestContext.addUIComponentToUpdateByAjax(activitiesContainer);
      requestContext.addUIComponentToUpdateByAjax(uiComposer);
    } else if (postContext == PostContext.USER) {
      UIUserActivitiesDisplay uiUserActivitiesDisplay = (UIUserActivitiesDisplay) getActivityDisplay();
      String ownerName = uiUserActivitiesDisplay.getOwnerName();
      Identity ownerIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME,
              ownerName, false);

      activityManager.saveActivity(ownerIdentity, activity);

      if (uiUserActivitiesDisplay.getSelectedDisplayMode() == UIUserActivitiesDisplay.DisplayMode.MY_STATUS) {
        UIActivitiesContainer activitiesContainer = uiUserActivitiesDisplay.getActivitiesLoader().getActivitiesContainer();
        if (activitiesContainer.getChildren().size() == 1) {
          uiUserActivitiesDisplay.setSelectedDisplayMode(UIUserActivitiesDisplay.DisplayMode.MY_STATUS);
        } else {
          activitiesContainer.addActivity(activity);
          requestContext.addUIComponentToUpdateByAjax(activitiesContainer);
          requestContext.addUIComponentToUpdateByAjax(uiComposer);
        }
      } else{
        uiUserActivitiesDisplay.setSelectedDisplayMode(UIUserActivitiesDisplay.DisplayMode.MY_STATUS);
      }
    }
  }


}
