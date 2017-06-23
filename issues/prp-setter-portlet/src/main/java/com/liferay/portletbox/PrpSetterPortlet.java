/**
 * Software License Agreement (BSD License)
 * 
 * Copyright (c) 2013, Liferay Inc.
 * All rights reserved.
 * 
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 * 
 * * The name of Liferay Inc. may not be used to endorse or promote products
 *   derived from this software without specific prior
 *   written permission of Liferay Inc.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.liferay.portletbox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.liferay.portletbox.issuesutil.HTMLUtil;
import com.liferay.portletbox.issuesutil.TableWriter;


/**
 * @author  Vernon Singleton
 */
public class PrpSetterPortlet extends GenericPortlet {

   @Override
   public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
   IOException {

      System.err.println("prp setter processAction: ");
      // Writer stringWriter = new ConsoleHTMLWriter();
      StringWriter stringWriter = new StringWriter();

      // output the parameters on the Action request -

      stringWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(
          stringWriter,
          PortletRequest.ACTION_PHASE,
          "publicParameterMap",
          actionRequest.getPublicParameterMap()
      );
      stringWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(stringWriter, PortletRequest.ACTION_PHASE, "privateParameterMap",
            actionRequest.getPrivateParameterMap());
      stringWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(stringWriter, PortletRequest.ACTION_PHASE, "parameterMap", actionRequest.getParameterMap());
      stringWriter.write(HTMLUtil.BR_TAG);

      String writtenStuff = stringWriter.toString();
      actionRequest.getPortletSession().setAttribute("ActionString", writtenStuff);
   }

   @Override
   public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
         throws PortletException, IOException {

      System.err.println("prp setter serverResource: ");
      resourceResponse.setContentType("text/html");

      PrintWriter printWriter = resourceResponse.getWriter();
      printWriter.write("<html><body>");

      String resourceId = resourceRequest.getResourceID();
      printWriter.write("getResourceId()="+resourceId);
      printWriter.write(HTMLUtil.BR_TAG);

      printWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(printWriter, PortletRequest.RESOURCE_PHASE, "publicParameterMap",
            resourceRequest.getPublicParameterMap());
      printWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(printWriter, PortletRequest.RESOURCE_PHASE, "privateParameterMap",
            resourceRequest.getPrivateParameterMap());
      printWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(printWriter, PortletRequest.RESOURCE_PHASE, "parameterMap",
            resourceRequest.getParameterMap());
      printWriter.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeParameters(printWriter, PortletRequest.RESOURCE_PHASE, resourceRequest);
      printWriter.write(HTMLUtil.BR_TAG);

      printWriter.write("Cacheability is set to: " + resourceRequest.getCacheability());
      printWriter.write(HTMLUtil.BR_TAG);

      printWriter.write("<span>");

      writeTableOfLinksForResourceResponse(printWriter, resourceResponse);

      printWriter.write("</span>");
      printWriter.write("</body></html>");
   }

   @Override
   protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
   IOException {

      System.err.println("prp setter doView: ");
      PrintWriter writer = renderResponse.getWriter();

      // If available, write out messages from action request -

      String actionString = (String) renderRequest.getPortletSession().getAttribute("ActionString");
      if (actionString != null) {
         writer.write("BEGIN - Messages from Action Phase:<br/>");
         writer.write(actionString);
         writer.write("END - Messages from Action Phase:<br/>");
         writer.write(HTMLUtil.BR_TAG);
         renderRequest.getPortletSession().removeAttribute("ActionString");
      }

      // Display current parameters -

      HTMLUtil.writeMapCompact(writer, PortletRequest.RENDER_PHASE, "getPublicParameterMap()", renderRequest.getPublicParameterMap());
      writer.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(writer, PortletRequest.RENDER_PHASE, "getPrivateParameterMap()", renderRequest.getPrivateParameterMap());
      writer.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeMapCompact(writer, PortletRequest.RENDER_PHASE, "getParameterMap()", renderRequest.getParameterMap());
      writer.write(HTMLUtil.BR_TAG);
      HTMLUtil.writeParameters(writer, PortletRequest.RENDER_PHASE, renderRequest);
      writer.write(HTMLUtil.BR_TAG);

      writer.write("renderRequest.getParameter(\"publicRenderParameter1\")=[");

      String publicRenderParameter1 = renderRequest.getParameter("publicRenderParameter1");

      if (publicRenderParameter1 == null) {
         publicRenderParameter1 = "null";
      }

      writer.write(publicRenderParameter1);
      writer.write("]");
      writer.write(HTMLUtil.BR_TAG);

      writer.write(HTMLUtil.HR_TAG);

      // Create URLs for tests -

      TableWriter tw = new TableWriter(writer);
      tw.startTable();

      // Create render URL w/o parameters -

      String testName = "createRenderURL() w/o parameters";
      PortletURL renderURL = renderResponse.createRenderURL();

      tw.writeURL(testName,  renderURL.toString() );

      // set public & private render parameters -

      testName = "createRenderURL(), set private & public parameters";
      renderURL = renderResponse.createRenderURL();

      renderURL.setParameter("publicRenderParameter1", "2");
      renderURL.setParameter("privateRenderParameter1", "1");

      tw.writeURL(testName,  renderURL.toString() );

      // set public & private render parameters, making use of map -

      testName = "createRenderURL(), renderURL.getParameterMap().put()";
      renderURL = renderResponse.createRenderURL();

      renderURL.setParameter("publicRenderParameter1", "2");
      renderURL.setParameter("privateRenderParameter1", "1");

      String[] values = { "Fred", "Wilma", "Pebbles" };
      renderURL.getParameterMap().put("privateRenderParameter2", values);

      tw.writeURL(testName,  renderURL.toString() );

      // render URL, multivalue parameters

      {
         testName = "Render URL, multivalue parms";
         renderURL = renderResponse.createRenderURL();

         String[] vals = {"A", "B", "C"};
         try { renderURL.setParameter("publicRenderParameter1", vals); }
         catch(Exception e) {writer.write("In test: "+testName+":<br/>"+"setParameter() failed.<br/>" + e.toString() + "<br/>");}

         String[] vals2 = {"D", "E", "F"};
         try { renderURL.setParameter("privateRenderParameter1", vals2); }
         catch(Exception e) {writer.write("In test: "+testName+":<br/>"+"setParameter() failed.<br/>" + e.toString() + "<br/>");}

         tw.writeURL(testName,  renderURL.toString() );
      }

      writer.write(HTMLUtil.HR_TAG);

      tw.endTable();

   }

   public void writeTableOfLinksForResourceResponse(PrintWriter printWriter, ResourceResponse resourceResponse) {

      System.err.println("prp setter writeTableOfLinksForResourceResponse: ");
      TableWriter tw = new TableWriter(printWriter);
      tw.startTable();

      // from doView  ...

      // from serveResource ...

      // Create resource URL with no further processing -

      String testName = "createResourceURL(), no new parameters";
      ResourceURL resourceURL = resourceResponse.createResourceURL();
      resourceURL.setResourceID(testName);

      tw.writeURL(testName,  resourceURL.toString() );

      // Create resource URL, setting parameter -

      testName = "createResourceURL(), set new parameter";
      resourceURL = resourceResponse.createResourceURL();
      resourceURL.setResourceID(testName);
      resourceURL.setParameter("resourceURLParameter4", "44");

      tw.writeURL(testName,  resourceURL.toString() );

      // Create resource URL, setting parameter, cacheability=FULL -

      testName = "createResourceURL(), set parm, cache=FULL";
      resourceURL = resourceResponse.createResourceURL();
      resourceURL.setResourceID(testName);
      try {resourceURL.setCacheability(ResourceURL.FULL);}
      catch(Exception e) {printWriter.write("In test: "+testName+":<br/>"+"setCacheability() failed.<br/>" + e.toString() + "<br/>");}
      resourceURL.setParameter("resourceURLParameter4", "setInResURL-FULL");

      tw.writeURL(testName,  resourceURL.toString() );

      // Create resource URL, setting parameter, cacheability=PORTLET -

      testName = "createResourceURL(), set parm, cache=PORTLET";
      resourceURL = resourceResponse.createResourceURL();
      resourceURL.setResourceID(testName);
      try {resourceURL.setCacheability(ResourceURL.PORTLET);}
      catch(Exception e) {printWriter.write("In test: "+testName+":<br/>"+"setCacheability() failed.<br/>" + e.toString() + "<br/>");}
      resourceURL.setParameter("resourceURLParameter4", "setInResURL-PORTLET");

      tw.writeURL(testName,  resourceURL.toString() );

      // Create resource URL, setting parameter, cacheability=PAGE -

      testName = "createResourceURL(), set parm, cache=PAGE";
      resourceURL = resourceResponse.createResourceURL();
      resourceURL.setResourceID(testName);
      try {resourceURL.setCacheability(ResourceURL.PAGE);}
      catch(Exception e) {printWriter.write("In test: "+testName+":<br/>"+"setCacheability() failed.<br/>" + e.toString() + "<br/>");}
      resourceURL.setParameter("resourceURLParameter4", "setInResURL-PAGE");

      tw.writeURL(testName,  resourceURL.toString() );

      // Create render URL, set public & private render parameters

      testName = "createRenderURL(), set parameters";
      PortletURL renderURL = null;
      try {renderURL = resourceResponse.createRenderURL();}
      catch(Exception e) {printWriter.write("In test: "+testName+":<br/>"+"createRenderURL() failed.<br/>" + e.toString() + "<br/>"); renderURL=null;}

      if (renderURL != null) {
         renderURL.setParameter("publicRenderParameter1", "Public parameter set during Resource Phase");
         renderURL.setParameter("privateRenderParameter9", "Private parameter set during Resource Phase");
         tw.writeURL(testName,  renderURL.toString() );
      }

      // Create action URL, set public & private render parameters

      testName = "createActionURL(), set parameters";
      PortletURL actionURL = null;
      try {actionURL = resourceResponse.createActionURL();}
      catch(Exception e) {printWriter.write("In test: "+testName+":<br/>"+"createActionURL() failed.<br/>" + e.toString() + "<br/>"); actionURL=null;}

      if (actionURL != null) {
         actionURL.setParameter("publicRenderParameter1", "Public parameter set during Resource Phase");
         actionURL.setParameter("privateRenderParameter9", "Private parameter set during Resource Phase");
         tw.writeButton(testName,  actionURL.toString() );
      }

      // Create render URL, set no parameters

      testName = "createRenderURL(), set no parameters";
      renderURL = null;
      try {renderURL = resourceResponse.createRenderURL();}
      catch(Exception e) {printWriter.write("In test: "+testName+":<br/>"+"createRenderURL() failed.<br/>" + e.toString() + "<br/>"); renderURL=null;}

      if (renderURL != null) {
         tw.writeURL(testName,  renderURL.toString() );
      }

      tw.endTable();
   }

}
