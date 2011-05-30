package org.exoplatform.social.plugin.videolink;

import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "classpath:groovy/social/plugin/videolink/UIVideoActivity.gtmpl", events = {
		@EventConfig(listeners = BaseUIActivity.ToggleDisplayLikesActionListener.class),
		@EventConfig(listeners = BaseUIActivity.ToggleDisplayCommentFormActionListener.class),
		@EventConfig(listeners = BaseUIActivity.LikeActivityActionListener.class),
		@EventConfig(listeners = BaseUIActivity.SetCommentListStatusActionListener.class),
		@EventConfig(listeners = BaseUIActivity.PostCommentActionListener.class),
		@EventConfig(listeners = BaseUIActivity.DeleteActivityActionListener.class, confirm = "UIActivity.msg.Are_You_Sure_To_Delete_This_Activity"),
		@EventConfig(listeners = BaseUIActivity.DeleteCommentActionListener.class, confirm = "UIActivity.msg.Are_You_Sure_To_Delete_This_Comment")
	}
)

public class UIVideoActivity extends BaseUIActivity {
	public static final String ACTIVITY_TYPE = "VIDEO_ACTIVITY";
	private String linkSource = "";
	private String linkTitle = "";
	private String linkImage = "";
	private String linkHTML = "";
	private String linkComment = "";

	public String getLinkComment() {
		return linkComment;
	}

	public void setLinkComment(String linkComment) {
		this.linkComment = linkComment;
	}

	public String getLinkHTML() {
		return linkHTML;
	}

	public void setLinkHTML(String linkHTML) {
		this.linkHTML = linkHTML;
	}

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}

	public String getLinkSource() {
		return linkSource;
	}

	public void setLinkSource(String linkSource) {
		this.linkSource = linkSource;
	}

	public String getLinkTitle() {
		return linkTitle;
	}

	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
}
