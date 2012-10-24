/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
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
package org.exoplatform.bonitasoft.uiextension;

import java.util.List;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

import org.exoplatform.bonitasoft.services.process.ProcessManager;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.ecm.webui.component.explorer.UIJCRExplorer;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

@ComponentConfig(template = "classpath:templates/CommentsDisplay/UICommentsDisplay.gtmpl")
public class UICommentsDisplay extends UIComponent {
  private static Log logger = ExoLogger.getLogger(UIApproveAction.class);
  private static final String STATE_PROPERTY = "publication:currentState";
  private static final String DRAFT = "draft";

  public UICommentsDisplay() {}

 


  /**
   * return the allowed action on uiextension
   * 
   * @return
   */
  

  @SuppressWarnings("unchecked")
 

  public String getCommentsFromNode() throws Exception{

      Node node = getAncestorOfType(UIJCRExplorer.class).getCurrentNode();
      String comment = "No Comments";
      if (node.hasProperty("exo:comment")) {
          comment = node.getProperty("exo:comment").getString();
      }
    if (comment != null) {
      return comment;
    } else {
      return "";
    }
  }

 


}
