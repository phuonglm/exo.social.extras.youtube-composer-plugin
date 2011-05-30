/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
 
/**
 * UIComposerVideoLinkExtension.js
 */
 
 (function() {
  var window_ = this,
      Util = eXo.social.Util,
      SUGGEST_SEARCH = "please input the search term",
      GRAY_COLOR = "gray",
      BLACK_COLOR = "black",
      uiComposerVideoLinkExtension;
  
  function changeLinkContent() {
    var link = this.linkData.link,
        title = this.linkData.title;
    	htmlcode = this.linkData.htmlcode;
    var queryString = 'link='+encodeURIComponent(link)
                    + '&title='+encodeURIComponent(title)
                    + '&htmlcode='+encodeURIComponent(htmlcode);
    var url = this.changeLinkContentUrl.replace(/&amp;/g, "&") + "&ajaxRequest=true";
    eXo.social.PortalHttpRequest.ajaxPostRequest(url, queryString, true, function(req) {
     //callbacked
    });
  }
  
  /**
   * creates input/ textarea element for edit inline
   * if tagName = input 
   * <input type="text" id="editableText" value="" />
   * if tagName = textarea
   * <textarea cols="10" rows="3">value</textarea>
   */
  function addEditableText(oldEl, tagName) {
    var textContent = oldEl.innerText; //IE
    if (textContent === undefined) {
        textContent = oldEl.textContent;
    }
    textContent = textContent.trim();
    var editableEl = document.createElement(tagName);
    if ('input' === tagName) {
      editableEl.setAttribute('type', 'text');
      editableEl.setAttribute('size', 50);
      editableEl.setAttribute('class', 'InputTitle');
      editableEl.setAttribute('className', 'InputTitle');
      
    } else if ('textarea' === tagName) {
      editableEl.setAttribute('cols', 50);
      editableEl.setAttribute('rows', 5);
      editableEl.setAttribute('class', 'InputDescription');
      editableEl.setAttribute('className', 'InputDescription');
    }
    //editableEl.setAttribute('id', "UIEditableText");
    editableEl.value = textContent;
    //insertafter and hide oldEl
    Util.insertAfter(editableEl, oldEl);
    oldEl.style.display='none';
    editableEl.focus();
    //ENTER -> done
    Util.addEventListener(editableEl, 'keypress', function(e) {
        if (Util.isEnterKey(e)) {
            updateElement(this);
            return false;
        }
    }, false);
    
    Util.addEventListener(editableEl, 'blur', function() {
        updateElement(this);
    }, false);
    
    var updateElement = function(editableEl) {
        //hide this, set new value and display
        var oldEl = editableEl.previousSibling;
        if (oldEl.innerText != null) { //IE
            oldEl.innerText = editableEl.value;
        } else {
            oldEl.textContent = editableEl.value;
        }
        //updates data
        //detects element by class, if class contains ContentTitle -> update title,
        // if class contains ContentDescription -> update description
        oldEl.style.display="block";
        if (Util.hasClass(oldEl, 'Title')) {
          uiComposerVideoLinkExtension.linkData.title = editableEl.value;
          changeLinkContent.apply(uiComposerVideoLinkExtension);
        } else if (Util.hasClass(oldEl, 'Content')) {
          uiComposerVideoLinkExtension.linkData.description = editableEl.value;
          changeLinkContent.apply(uiComposerVideoLinkExtension);
        }
        editableEl.parentNode.removeChild(editableEl);
    }
}
  
  function UIComposerVideoLinkExtension(params) {
    uiComposerVideoLinkExtension = this;
    this.configure(params);
    this.init();
  }
  
  UIComposerVideoLinkExtension.prototype.configure = function(params) {
    this.linkInfoDisplayed = params.linkInfoDisplayed || false;
    this.inputLinkId = params.inputLinkId || 'inputLink';
    this.attachButtonId = params.attachButtonId || 'attachButton';
    this.attachUrl = params.attachUrl || null;
    this.searchUrl = params.searchUrl || null;
    this.selectVideoUrl = params.selectVideoUrl || null;
    this.changeLinkContentUrl = params.changeLinkContentUrl || null;
    this.linkData = params.linkData || {};
    this.lasQuery = params.lasQuery || null;
    SUGGEST_SEARCH = this.lasQuery;
    if (!this.attachUrl) {
      alert('error: attachUrl is null!');
    }
  }
  
  UIComposerVideoLinkExtension.prototype.resetIsReady = function() {
    
    if (this.linkInfoDisplayed) {
      
    } else {

    }
  }
  
  UIComposerVideoLinkExtension.prototype.init = function() {
  
    
    var shareButton = Util.getElementById('ShareButton');

    uiComposerVideoLinkExtension = this;
    if (this.linkInfoDisplayed) {
      //trick: enable share button
      if (shareButton) {
        shareButton.disabled = false;
      }
      
      this.linkTitle = Util.getElementById('LinkTitle');
      this.linkDescription = Util.getElementById('LinkDescription');
      Util.addEventListener(this.linkTitle, 'click', function(evt) {
        addEditableText(this, 'input');
      }, false);
      
      
      this.images = [];

    } else {

      if (shareButton) {
        shareButton.disabled = true;
      }
      this.inputLink = Util.getElementById(this.inputLinkId);
      this.attachButton = Util.getElementById(this.attachButtonId);
      this.inputLink.value = SUGGEST_SEARCH;
      this.inputLink.style.color = GRAY_COLOR;
      var uiComposerVideoLinkExtension = this;
      var inputLink = this.inputLink;
      Util.addEventListener(inputLink, 'focus', function(evt) {
        if (inputLink.value === SUGGEST_SEARCH) {
          inputLink.style.color = BLACK_COLOR;
        }
      }, false);
      
      Util.addEventListener(inputLink, 'blur', function(evt) {
        if (inputLink.value === SUGGEST_SEARCH) {
          inputLink.style.color = GRAY_COLOR;
        }
      }, false);
      
      Util.addEventListener(inputLink, 'keypress', function(evt) {
        //if enter submit link
      }, false);
      this.attachButton.disabled = false;
      Util.addEventListener(this.attachButton, 'click', function(evt) {
        if (inputLink.value === '' || inputLink.value === SUGGEST_SEARCH) {
          return;
        }
        var url = uiComposerVideoLinkExtension.searchUrl.replace(/&amp;/g, "&") + '&objectId='+ encodeURIComponent(inputLink.value) + '&ajaxRequest=true';
        ajaxGet(url);
      }, false);
      

      videoPreviewArea = eXo.social.Util.getElementById("VideoPreviewArea");
      videoImages = eXo.core.DOMUtil.findChildrenByClass(videoPreviewArea,"img","");

      for( var i = 0; i < videoImages.length; i++ ) {
      	videoimage = videoImages[i];
      	eXo.social.Util.addEventListener(videoimage, 'click', function(evt) {
            var url = uiComposerVideoLinkExtension.attachUrl.replace(/&amp;/g, "&") + '&objectId='+ encodeURIComponent(this.alt) + '&ajaxRequest=true';
            ajaxGet(url);
          }, false);
      }
    }
  }
  
  //expose
  window_.eXo = window_.eXo || {};
  window_.eXo.social = window_.eXo.social || {};
  window_.eXo.social.webui = window_.eXo.social.webui || {};
  window_.eXo.social.webui.UIComposerVideoLinkExtension = UIComposerVideoLinkExtension;
 })();
